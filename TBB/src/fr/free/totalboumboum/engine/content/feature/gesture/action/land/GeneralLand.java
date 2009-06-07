package fr.free.totalboumboum.engine.content.feature.gesture.action.land;

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

import java.util.Arrays;
import java.util.List;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.Contact;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.IncompatibleParameterException;
import fr.free.totalboumboum.engine.content.feature.gesture.action.Orientation;
import fr.free.totalboumboum.engine.content.feature.gesture.action.Role;
import fr.free.totalboumboum.engine.content.feature.gesture.action.TilePosition;

/** 
 * action d'atterrir (hors d�placement sur le plan, qui est li� � movehigh)
 * INTRANSITIVE
 * 
 * <p>ABILITY PERFORM
 *	<br>N/D 
 *
 * <p>ABILITY REFUSE
 * 	<br>N/D
 * 
 * <p>ABILITY PREVENT
 * 	<br>param�tre: actor=oui (hero,bombe)
 * 	<br>param�tre: target=N/D
 * 	<br>param�tre: direction=N/D
 * 	<br>param�tre: strength=bool
 * 	<br>param�tre: kind=N/D
 * 	<br>param�tre: scope=oui
 * 	<br>param�tre: restriction=SPRITE_TRAVERSE
 */
/** 
 * finishing an aerial move and landing on the floor.
 * for instance a hero after a jump, or a bomb after having been punched
 * 
 * 	<br>actor: 			any (probably a hero or a bomb)
 * 	<br>target: 		none
 * 	<br>direction:		any or none
 * 	<br>contact:		none
 * 	<br>tilePosition:	undefined
 * 	<br>orientation:	undefined
 *  
 */
public class GeneralLand extends GeneralAction<SpecificLand>
{	
	/////////////////////////////////////////////////////////////////
	// ACTORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** roles of the acting sprite */
	private static final List<Role> allowedActors = Arrays.asList(new Role[]{
//		Role.NONE,
		Role.BLOCK,
		Role.BOMB,
		Role.FIRE,
		Role.FLOOR,
		Role.HERO,
		Role.ITEM
    });
	
	@Override
	public void addActor(Role actor) throws IncompatibleParameterException
	{	if(!allowedActors.contains(actor))
			throw new IncompatibleParameterException("actor",actor.toString());
		else
		{	if(!getActors().contains(actor))
				actors.add(actor);
		}
	}

	/////////////////////////////////////////////////////////////////
	// TARGETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** role of the targeted sprite */
	private static final List<Role> allowedTargets = Arrays.asList(new Role[]{
		Role.NONE
//		Role.BLOCK,
//		Role.BOMB,
//		Role.FIRE,
//		Role.FLOOR,
//		Role.HERO,
//		Role.ITEM
    });
	
	@Override
	public void addTarget(Role target) throws IncompatibleParameterException
	{	if(!allowedTargets.contains(target))
			throw new IncompatibleParameterException("target",target.toString());
		else
		{	if(!targets.contains(target))
				targets.add(target);
		}
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** directions of the action */
	private static final List<Direction> allowedDirections = Arrays.asList(new Direction[]{	
		Direction.NONE,
		Direction.UP,
		Direction.UPRIGHT,
		Direction.RIGHT,
		Direction.DOWNRIGHT,
		Direction.DOWN,
		Direction.DOWNLEFT,
		Direction.LEFT,
		Direction.UPLEFT
	});

	@Override
	public void addDirection(Direction direction) throws IncompatibleParameterException
	{	if(!allowedDirections.contains(direction))
			throw new IncompatibleParameterException("direction",direction.toString());
		else
		{	if(!directions.contains(direction))
				directions.add(direction);
		}
	}

	/////////////////////////////////////////////////////////////////
	// CONTACTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** contacts between the actor and the target */
	private static final List<Contact> allowedContacts = Arrays.asList(new Contact[]{
		Contact.NONE
//		Contact.COLLISION,
//		Contact.INTERSECTION
    });

	@Override
	public void addContact(Contact contact) throws IncompatibleParameterException
	{	if(!allowedContacts.contains(contact))
			throw new IncompatibleParameterException("contact",contact.toString());
		else
		{	if(!contacts.contains(contact))
				contacts.add(contact);
		}
	}

	/////////////////////////////////////////////////////////////////
	// TILE POSITIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** positions of the target in termes of tiles */
	private static final List<TilePosition> allowedTilePositions = Arrays.asList(new TilePosition[]{
		TilePosition.UNDEFINED
//		TilePosition.NEIGHBOR,
//		TilePosition.REMOTE,
//		TilePosition.SAME
    });
	
	@Override
	public void addTilePosition(TilePosition tilePosition) throws IncompatibleParameterException
	{	if(!allowedTilePositions.contains(tilePosition))
			throw new IncompatibleParameterException("tilePosition",tilePosition.toString());
		else
		{	if(!getTilePositions().contains(tilePosition))
				tilePositions.add(tilePosition);
		}
	}

	/////////////////////////////////////////////////////////////////
	// ORIENTATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** compared directions of the target and the action */
	private static final List<Orientation> allowedOrientations = Arrays.asList(new Orientation[]{
		Orientation.UNDEFINED
//		Orientation.OPPOSITE,
//		Orientation.OTHER,
//		Orientation.SAME
    });

	@Override
	public void addOrientation(Orientation orientation) throws IncompatibleParameterException
	{	if(!allowedOrientations.contains(orientation))
			throw new IncompatibleParameterException("orientation",orientation.toString());
		else
		{	if(!orientations.contains(orientation))
				orientations.add(orientation);
		}
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void finish()
	{	if(!finished)
		{	super.finish();
		}
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "LAND";
		result = result + super.toString();
		return result;
	}
}
