package fr.free.totalboumboum.engine.content.feature.action.gather;

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

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.action.Contact;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.Orientation;
import fr.free.totalboumboum.engine.content.feature.action.Role;
import fr.free.totalboumboum.engine.content.feature.action.TilePosition;

/** 
 * action de récolter (un item)
 * TRANSITIVE
 * 
 * <p>ABILITY PERFORM
 * 	<br>paramètre: actor=self
 * 	<br>paramètre: target=oui (item)
 * 	<br>paramètre: direction=N/D
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=N/D
 * 	<br>paramètre: restriction=N/D
 * 
 * <p>ABILITY REFUSE (ex: item qui ne peut pas être récolté en permanence)
 * 	<br>paramètre: actor=oui (hero)
 * 	<br>paramètre: target=self
 * 	<br>paramètre: direction=N/D
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=N/D
 * 	<br>paramètre: restriction=N/D
 * 
 * <p>ABILITY PREVENT (ex: un bloc qui empêche par intermitence les heros de récolter l'item)
 * 	<br>paramètre: actor=oui (hero)
 * 	<br>paramètre: target=oui (item)
 * 	<br>paramètre: direction=N/D
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=N/D
 * 	<br>paramètre: restriction=éventuellement
 */
/** 
 * picking an object just by walking on it (unlike picking a bomb to carry it). 
 * most of the time a hero gathering an item.
 * 
 * 	<br>actor: 			any (probably a hero)
 * 	<br>target: 		any (probably an item)
 * 	<br>direction:		any or none
 * 	<br>contact:		collision or intersection
 * 	<br>tilePosition:	same
 * 	<br>orientation:	any or undefined
 *  
 */
public class GeneralGather extends GeneralAction<SpecificGather>
{
	
	public GeneralGather()
	{	super(ActionName.GATHER);	
		Role[] allowedActors = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
		setAllowedActors(allowedActors);
		Role[] allowedTargets = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
		setAllowedTargets(allowedTargets);
		Direction[] allowedDirections = {Direction.NONE,Direction.UP,Direction.UPRIGHT,Direction.RIGHT,Direction.DOWNRIGHT,Direction.DOWN,Direction.DOWNLEFT,Direction.LEFT,Direction.UPLEFT};
		setAllowedDirections(allowedDirections);
		Contact[] allowedContacts = {Contact.COLLISION,Contact.INTERSECTION};
		setAllowedContacts(allowedContacts);
		TilePosition[] allowedTilePositions = {TilePosition.SAME};
		setAllowedTilePositions(allowedTilePositions);
		Orientation[] allowedOrientations = {Orientation.UNDEFINED,Orientation.OPPOSITE,Orientation.OTHER,Orientation.SAME};
		setAllowedOrientations(allowedOrientations);
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public GeneralAction<?> copy(GeneralAction<?> action)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
