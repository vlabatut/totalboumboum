package fr.free.totalboumboum.engine.content.feature.action;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public abstract class SpecificAction<T extends GeneralAction<?>>
{
/*	
	private SpecificAction(ActionName name)
	{	this.name = name;
		actor = null;
		target = null;
		direction = null;
		contact = null;
		tilePosition = null;
		orientation = null;
	}
*/
	/**
	 * light action, probably just used for a doability test
	 */
	public SpecificAction(ActionName name, Sprite actor)
	{	this(name,actor,null);		
	}

	/**
	 * automatic initialization
	 * @param name
	 * @param actor
	 * @param target
	 */
	public SpecificAction(ActionName name, Sprite actor, Sprite target)
	{	this.name = name;
		this.actor = actor;
		this.target = target;
		this.direction = actor.getCurrentFacingDirection();
		this.contact = Contact.getContact(actor,target);
		this.tilePosition = TilePosition.getTilePosition(actor,target);
		this.orientation = Orientation.getOrientation(actor,target);
		initGeneralAction();
	}
	
	/**
	 * manual initialization
	 * @param name
	 * @param actor
	 * @param target
	 * @param direction
	 * @param contact
	 * @param tilePosition
	 * @param orientation
	 */
	public SpecificAction(ActionName name, Sprite actor, Sprite target, Direction direction, Contact contact, TilePosition tilePosition, Orientation orientation)
	{	this.name = name;
		this.actor = actor;
		this.target = target;
		this.direction = direction;
		this.contact = contact;
		this.tilePosition = tilePosition;
		this.orientation = orientation;
		initGeneralAction();
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** name of this action */
	protected ActionName name;

	public ActionName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction of the action */
	private Direction direction;

	public Direction getDirection()
	{	return direction;
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTACT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** contact between the actor and the target */
	private Contact contact;

	public Contact getContact()
	{	return contact;
	}
	
	/////////////////////////////////////////////////////////////////
	// ORIENTATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** compared directions of the target and the action */
	private Orientation orientation;

	public Orientation getOrientation()
	{	return orientation;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE POSITION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** position of the target in termes of tiles */
	private TilePosition tilePosition;

	public TilePosition getTilePosition()
	{	return tilePosition;
	}

	/////////////////////////////////////////////////////////////////
	// ACTOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sprite performing the action */
	private Sprite actor;

	public Sprite getActor()
	{	return actor;
	}
	
	/////////////////////////////////////////////////////////////////
	// TARGET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sprite targeted by the action */
	private Sprite target;

	public Sprite getTarget()
	{	return target;
	}

	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** generalisation of this specific action */
	private T generalAction;

	public T getGeneralAction()
	{	return generalAction;
	}
	
	private void initGeneralAction()
	{	//NOTE à virer car remplacé par allowAction dans permission ?
		//NOTE is it actually used? (we have GeneralAction.subsume)
		generalAction = new GeneralAction(name);
		generalAction.addActor(actor.getClass());
		generalAction.addDirection(direction);
		generalAction.addContact(contact);
		generalAction.addOrientation(orientation);
		generalAction.addTilePosition(tilePosition);
		if(target!=null)
			generalAction.addTarget(target.getClass());
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object action)
	{	boolean result = false;
		if(action instanceof SpecificAction)
		{	SpecificAction a = (SpecificAction)action;
			// name
			result = name.equalsIgnoreCase(a.getName());
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
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = name + "(";
		result = result + actor + " > ";
		result = result + target + " ";
		result = result + "["+direction+"] ";
		result = result + "["+contact+"] ";
		result = result + "["+orientation+"] ";
		result = result + "["+tilePosition+"])";			
		return result;
	}
/*	
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
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			// general action
			generalAction.finish();
			generalAction = null;
			// misc
			actor = null;
			contact = null;
			direction = null;
			orientation = null;
			target = null;
			tilePosition = null;
		}
	}
}
