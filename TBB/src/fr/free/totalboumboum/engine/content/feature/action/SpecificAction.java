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
import fr.free.totalboumboum.engine.content.feature.action.general.GeneralAction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class SpecificAction extends AbstractAction
{
	/** 
	 * direction of the action 
	 */
	private Direction direction;
	/** 
	 * contact between the actor and the target 
	 */
	private Contact contact;
	/** 
	 * compared directions of the target and the action  
	 */
	private Orientation orientation;
	/** 
	 * position of the target in termes of tile
	 */
	private TilePosition tilePosition;
	/** 
	 * role of the acting sprite 
	 */
	private Sprite actor;
	/** 
	 * role of the targeted sprite 
	 */
	private Sprite target;
	/**
	 * generalisation of this specific action
	 */
	private GeneralAction generalAction;

	@SuppressWarnings("unused")
	private SpecificAction(String name)
	{	super(name);
		actor = null;
		target = null;
		direction = null;
		contact = null;
		tilePosition = null;
		orientation = null;
	}
	public SpecificAction(String name, Sprite actor, Sprite target, Direction direction)
	{	super(name);
		this.actor = actor;
		this.target = target;
		this.direction = direction;
		updateContact();
		updateTilePosition();
		updateOrientation();
		updateGeneralAction();
	}
	public SpecificAction(String name, Sprite actor, Sprite target, Direction direction, Contact contact, TilePosition tilePosition, Orientation orientation)
	{	super(name);
		this.actor = actor;
		this.target = target;
		this.direction = direction;
		this.contact = contact;
		this.tilePosition = tilePosition;
		this.orientation = orientation;
		updateGeneralAction();
	}
	
	/**
	 * on met en place le type de contact.
	 * s'il n'y a pas de cible, le contact est NONE
	 */
	private void updateContact()
	{	if(actor.isCollidingSprite(target))
			contact = Contact.COLLISION;
		else if((actor.isIntersectingSprite(target)))
			contact = Contact.INTERSECTION;
		else
			contact = Contact.NONE;
	}	
	
	/**
	 * on met en place l'orientation (comparaison de la direction de l'action et
	 * de la directiond de la cible). s'il n'y a pas de cible, l'orientation
	 * est NONE. si l'acteur et la cible/case sont
	 * au même endroit, on considère qu'ils ont la même orientation (SAME).
	 */
	private void updateOrientation()
	{	Direction d = Direction.getCompositeFromSprites(actor,target);
		if(d==direction || d==Direction.NONE || 
				(direction.isPrimary() && (d.containsPrimary(direction))))
			orientation = Orientation.SAME;
		else if(d == direction.getOpposite())
			orientation = Orientation.OPPOSITE;
		else
			orientation = Orientation.OTHER;
	}	
	
	/**
	 * détermine la position de la case de la cible relativement à celle de l'acteur.
	 * S'il n'y a pas de cible, on obtient SAME.
	 * Si l'acteur n'a pas de case, on obtient aussi SAME.
	 */
	private void updateTilePosition()
	{	Tile actorTile = actor.getTile();
		if(actorTile == null)
			tilePosition = TilePosition.SAME;
		else
		{	Tile targetTile = null;
			if(target==null)
				tilePosition = TilePosition.SAME;
			else	
			{	targetTile = target.getTile();
				if(actorTile==targetTile)
					tilePosition = TilePosition.SAME;
				else if(actorTile.isNeighbour(targetTile))
					tilePosition = TilePosition.NEIGHBOUR;
				else
					tilePosition = TilePosition.FAR;
			}
		}
	}
	
	private void updateGeneralAction()
	{	//NOTE à virer car remplacé par allowAction dans permission ?
		generalAction = new GeneralAction(name);
		generalAction.addActor(actor.getClass());
		generalAction.addDirection(direction);
		generalAction.addContact(contact);
		generalAction.addOrientation(orientation);
		generalAction.addTilePosition(tilePosition);
		if(target!=null)
			generalAction.addTarget(target.getClass());
	}
	
	public Direction getDirection()
	{	return direction;
	}
	public void setDirection(Direction direction)
	{	this.direction = direction;
	}
	
	public Contact getContact()
	{	return contact;
	}
	public void setContact(Contact contact)
	{	this.contact = contact;
	}
	
	public Orientation getOrientation()
	{	return orientation;
	}
	public void setOrientation(Orientation orientation)
	{	this.orientation = orientation;
	}
	
	public TilePosition getTilePosition()
	{	return tilePosition;
	}
	public void setTilePosition(TilePosition tilePosition)
	{	this.tilePosition = tilePosition;
	}

	public Sprite getActor()
	{	return actor;
	}
	@SuppressWarnings("unused")
	private void setActor(Sprite actor)
	{	this.actor = actor;		
	}
	
	public Sprite getTarget()
	{	return target;
	}
	@SuppressWarnings("unused")
	private void setTarget(Sprite target)
	{	this.target = target;		
	}
	
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
	
	public String toString()
	{	String result = name+"("+actor+">"+target;
		result = result+","+direction+")";
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
	public void setGeneralAction(GeneralAction generalAction)
	{	this.generalAction = generalAction;
	}
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
			// general action
			if(generalAction!=null)
			{	generalAction.finish();
				generalAction = null;
			}
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
