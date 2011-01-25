package org.totalboumboum.engine.content.feature.action;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class SpecificAction
{	
	protected SpecificAction(ActionName name, Sprite actor)
	{	this.name = name;
		this.actor = actor;
		this.target = null;
		this.direction = actor.getCurrentFacingDirection();
		circumstance.initCircumstance(actor,target);
		this.tile = actor.getTile();
		initGeneralAction();
	}

	protected SpecificAction(ActionName name, Tile tile, Sprite target)
	{	this.name = name;
		this.actor = null;
		this.target = target;
		this.direction = Direction.NONE;
		circumstance.initCircumstance();
		this.tile = tile;
		initGeneralAction();
	}

	protected SpecificAction(ActionName name, Sprite actor, Sprite target)
	{	this.name = name;
		this.actor = actor;
		this.target = target;
		this.direction = actor.getCurrentFacingDirection();
		circumstance.initCircumstance(actor,target);
		this.tile = actor.getTile();
		initGeneralAction();
	}

	protected SpecificAction(ActionName name, Sprite actor, Tile tile)
	{	this.name = name;
		this.actor = actor;
		this.target = null;
		this.direction = actor.getCurrentFacingDirection();
		circumstance.initCircumstance(actor,target);
		this.tile = tile;
		initGeneralAction();
	}

	protected SpecificAction(ActionName name, Sprite actor, Sprite target, Direction direction, Contact contact, TilePosition tilePosition, Orientation orientation)
	{	this.name = name;
		this.actor = actor;
		this.target = target;
		this.direction = direction;
		circumstance.setContact(contact);
		circumstance.setTilePosition(tilePosition);
		circumstance.setOrientation(orientation);
		this.tile = actor.getTile();
		initGeneralAction();
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** name of this action */
	private ActionName name;

	public ActionName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction of the action */
	private Direction direction = Direction.NONE;

	public Direction getDirection()
	{	return direction;
	}
	
	protected void setDirection(Direction direction)
	{	this.direction = direction;		
	}
	
	/////////////////////////////////////////////////////////////////
	// CIRCUMSTANCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Circumstance circumstance = new Circumstance();

	public Circumstance getCircumstance()
	{	return circumstance;
	}
	
	public Contact getContact()
	{	return circumstance.getContact();
	}
	
	public Orientation getOrientation()
	{	return circumstance.getOrientation();
	}
	
	public TilePosition getTilePosition()
	{	return circumstance.getTilePosition();
	}

	/////////////////////////////////////////////////////////////////
	// ACTOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sprite performing the action */
	private Sprite actor = null;

	public Sprite getActor()
	{	return actor;
	}
	
	/////////////////////////////////////////////////////////////////
	// TARGET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sprite targeted by the action */
	private Sprite target = null;

	public Sprite getTarget()
	{	return target;
	}

	/////////////////////////////////////////////////////////////////
	// TILE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** tile targeted by the action */
	private Tile tile = null;

	public Tile getTile()
	{	return tile;
	}

	/////////////////////////////////////////////////////////////////
	// MODULATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * process the total ability for this action, considering:
	 * 	- actor original ability
	 * 	- actor modulation (depending on its current gesture)
	 * 	- target modulation (same thing, and only if the target exists)
	 * 	- environment modulation (considering all sprites in the actor and target tiles) 
	 */
/*	public ActionAbility computeAbility()
	{	// actor original ability 
		ActionAbility result = actor.getAbility(this); //TODO écrire getAbility(action), les autres sont-ils utiles?
		result = (ActionAbility)result.copy();
		
		// actor modulation
		if(result.isActive())
		{	ActorModulation actorModulation = actor.getActorModulation(this);
			if(actorModulation!=null) //TODO peut être que c'est plus simple de renvoyer systmétiquement une modulation, mais avec une puissance de 0?
				result = actorModulation.modulate(result); //TODO écrire cette méthode aussi, qui renvoie une nouvelle ability		
		}
		
		// target modulation (if there's one!)
		if(result.isActive() && target!=null)//TODO quand on cherche une modulation pour un sprite donné, ça dépend de son gesture courant. si pas de gesture, alors il renvoie null
		{	TargetModulation targetModulation = target.getTargetModulation(this);
			if(targetModulation!=null)
				result = targetModulation.modulate(result); 		
		}
		
		// environement modulation
		if(result.isActive())
		{	// list of the involved sprites
			ArrayList<Sprite> sprites = new ArrayList<Sprite>();
			Tile tileA = actor.getTile();
			if(tileA!=null)
			{	for(Sprite s: tileA.getSprites())
				{	if(s!=target && s!=actor)
						sprites.add(s);					
				}
			}
			if(target!=null)
			{	Tile tileT = target.getTile();
				if(tileT!=null)
				{	for(Sprite s: tileT.getSprites())
					{	if(!sprites.contains(s) && s!=target && s!=actor)
							sprites.add(s);					
					}
				}
			}
			// check each one of them
			Iterator<Sprite> i = sprites.iterator();
			while(i.hasNext() && result.isActive())
			{	Sprite tempSprite = i.next();
				ThirdModulation thirdModulation = tempSprite.getThirdModulation(this);
				if(thirdModulation==null)
					result = thirdModulation.modulate(result); 		
			}
		}
		return result;
	}
*/
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*
 * TODO is it necessary?
	public boolean equals(Object action)
	{	boolean result = false;
		if(action instanceof SpecificAction<?>)
		{	SpecificAction<?> a = (SpecificAction<?>)action;
			// actor
			if(result)
				result = actor==a.getActor();
			// target
			if(result)
				result = target==a.getTarget();
			// direction
			if(result)
				result = direction.equals(a.getDirection());
			// contact
			if(result)
				result = contact.equals(a.getContact());
			// orientation
			if(result)
				result = orientation.equals(a.getOrientation());
			// tile position
			if(result)
				result = tilePosition.equals(a.getTilePosition());
		}
		return result;
	}
*/
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = name + "(";
		result = result + actor + " > ";
		result = result + target + " ";
		result = result + "["+direction+"] ";
		result = result + "["+circumstance.getContact()+"] ";
		result = result + "["+circumstance.getOrientation()+"] ";
		result = result + "["+circumstance.getTilePosition()+"])";			
		return result;
	}
/*	
 * TODO is it necessary ?
	public SpecificAction copy()
	{	SpecificAction result;
		result = new SpecificAction(name);
		result.setActor(actor);
		result.setTarget(target);
		result.setDirection(direction);
		result.setContact(contact);
		result.setOrientation(orientation);
		result.setTilePosition(tilePosition);
		result.setGeneralAction(generalAction.copy());
		return result;
	}
*/

	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralAction generalAction;
	
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}
		
	private void initGeneralAction()
	{	generalAction = getName().createGeneralAction();
		if(getActor()!=null)
			generalAction.addActor(getActor().getRole());
		generalAction.addDirection(getDirection());
		generalAction.addContact(getContact());
		generalAction.addOrientation(getOrientation());
		generalAction.addTilePosition(getTilePosition());
		if(getTarget()!=null)
			generalAction.addTarget(getTarget().getRole());
	}
	
	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
//	protected ActionAbility actionAbility = null;
	
//	private void initActionAbility()
//	{	if(actionAbility == null)	
//			actionAbility = actor.modulateAction(this);
//	}
	
//	public float getStrength()
//	{	float result;
//		initActionAbility();
//		result = actionAbility.getStrength();
//		return result;
//	}
	
	/** tests if this action is allowed (through the modulation system)
	 * by the actor, the target and the environment.
	 */
//	public boolean isPossible()
//	{	boolean result;
//		initActionAbility();
//		result = actionAbility.isActive();
//		return result;
//	}
	
}
