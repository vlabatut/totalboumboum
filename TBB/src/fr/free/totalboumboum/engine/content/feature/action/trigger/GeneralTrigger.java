package fr.free.totalboumboum.engine.content.feature.action.trigger;

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
 * action d'activer l'explosion d'une remote bomb
 * TRANSITIVE
 * 
 * <p>ABILITY PERFORM
 * 	<br>paramètre: actor=self
 * 	<br>paramètre: target=oui (bombe)
 * 	<br>paramètre: direction=N/D
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=N/D
 * 	<br>paramètre: restriction=N/D
 * 
 * <p>ABILITY REFUSE (généralement pas utilisé, mais possible)
 * 	<br>N/D
 * 
 * <p>ABILITY PREVENT
 * 	<br>paramètre: actor=oui (hero)
 * 	<br>paramètre: target=all
 * 	<br>paramètre: direction=N/D
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=N/D
 * 	<br>paramètre: restriction=N/D
 */
/** 
 * asking a remote bomb to explode.
 * usually performed by a hero on.. well, on a bomb.
 * 
 * 	<br>actor: 			any (probably hero)
 * 	<br>target: 		bomb
 * 	<br>direction:		any or none
 * 	<br>contact:		any or none
 * 	<br>tilePosition:	any or undefined
 * 	<br>orientation:	any or undefined
 *  
 */
public class GeneralTrigger extends GeneralAction<SpecificTrigger>
{
	
	public GeneralTrigger()
	{	super(ActionName.TRIGGER);	
		Role[] allowedActors = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
		setAllowedActors(allowedActors);
		Role[] allowedTargets = {Role.BOMB};
		setAllowedTargets(allowedTargets);
		Direction[] allowedDirections = {Direction.NONE,Direction.UP,Direction.UPRIGHT,Direction.RIGHT,Direction.DOWNRIGHT,Direction.DOWN,Direction.DOWNLEFT,Direction.LEFT,Direction.UPLEFT};
		setAllowedDirections(allowedDirections);
		Contact[] allowedContacts = {Contact.NONE,Contact.COLLISION,Contact.INTERSECTION};
		setAllowedContacts(allowedContacts);
		TilePosition[] allowedTilePositions = {TilePosition.UNDEFINED,TilePosition.NEIGHBOR,TilePosition.REMOTE,TilePosition.SAME};
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
