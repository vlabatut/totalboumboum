package org.totalboumboum.engine.content.feature.action.movelow;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.GeneralAction;

/** 
 * action de se déplacer sur le sol
 * INTRANSITIVE
 * 
 * <p>ABILITY PERFORM
 * 	<br>N/D
 * 
 * <p>ABILITY REFUSE
 * 	<br>N/D
 * 
 * <p>ABILITY PREVENT
 * 	<br>paramètre: actor=oui (hero, bomb)
 * 	<br>paramètre: target=N/D
 * 	<br>paramètre: direction=oui
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=oui
 * 	<br>paramètre: restriction=SPRITE_TRAVERSE
 */
/** 
 * on ground (normal) move.
 * for example: hero walking, bomb sliding, etc.
 * 
 * 	<br>actor: 			any
 * 	<br>target: 		none
 * 	<br>direction:		any (not none)
 * 	<br>contact:		none
 * 	<br>tilePosition:	undefined
 * 	<br>orientation:	undefined
 * 
 * @author Vincent Labatut
 *
 */
public class GeneralMoveLow extends GeneralAction
{	/** Serial */
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** name of the action */
	private final ActionName name = ActionName.MOVELOW;

	public ActionName getName()
	{	return name;	
	}
	
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
	public void addActor(Role actor)
	{	if(allowedActors.contains(actor))
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
	public void addTarget(Role target)
	{	if(allowedTargets.contains(target))
		{	if(!targets.contains(target))
				targets.add(target);
		}
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** directions of the action */
	private static final List<Direction> allowedDirections = Arrays.asList(new Direction[]{	
//		Direction.NONE,
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
	public void addDirection(Direction direction)
	{	if(allowedDirections.contains(direction))
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
	public void addContact(Contact contact)
	{	if(allowedContacts.contains(contact))
		{	if(!containsContact(contact))
				circumstance.addContact(contact);
		}
	}

	/////////////////////////////////////////////////////////////////
	// TILE POSITIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** positions of the target in termes of tiles */
	private static final List<TilePosition> allowedTilePositions = Arrays.asList(new TilePosition[]{
		TilePosition.NONE
//		TilePosition.NEIGHBOR,
//		TilePosition.REMOTE,
//		TilePosition.SAME
    });
	
	@Override
	public void addTilePosition(TilePosition tilePosition)
	{	if(allowedTilePositions.contains(tilePosition))
		{	if(!containsTilePosition(tilePosition))
				circumstance.addTilePosition(tilePosition);
		}
	}

	/////////////////////////////////////////////////////////////////
	// ORIENTATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** compared directions of the target and the action */
	private static final List<Orientation> allowedOrientations = Arrays.asList(new Orientation[]{
		Orientation.NONE
//		Orientation.BACK,
//		Orientation.FACE,
//		Orientation.NEUTRAL,
//		Orientation.OTHER,
     });

	@Override
	public void addOrientation(Orientation orientation)
	{	if(allowedOrientations.contains(orientation))
		{	if(!containsOrientation(orientation))
				circumstance.addOrientation(orientation);
		}
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public GeneralMoveLow copy()
	{	GeneralMoveLow result = new GeneralMoveLow();
		super.copy(result);
		return result;
	}
}
