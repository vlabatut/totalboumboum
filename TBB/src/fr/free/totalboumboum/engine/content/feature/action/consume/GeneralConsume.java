package fr.free.totalboumboum.engine.content.feature.action.consume;

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
 * action de faire br�ler un autre objet
 * TRANSITIVE
 * 
 * <p>ABILITY PERFORM
 * 	<br>param�tre: actor=self
 * 	<br>param�tre: target=oui (g�n�ral: toutes les classes)
 * 	<br>param�tre: direction=N/D
 * 	<br>param�tre: strength=bool
 * 	<br>param�tre: kind=N/D
 * 	<br>param�tre: scope=N/D
 * 	<br>param�tre: restriction=N/D
 * 
 * <p>ABILITY REFUSE
 * 	<br>param�tre: actor=oui (fire?)
 * 	<br>param�tre: target=N/D
 * 	<br>param�tre: direction=N/D
 * 	<br>param�tre: strength=bool
 * 	<br>param�tre: kind=N/D
 * 	<br>param�tre: scope=N/D
 * 	<br>param�tre: restriction=N/D
 * 
 * <p>ABILITY PREVENT
 * 	<br>param�tre: actor=oui
 * 	<br>param�tre: target=oui
 * 	<br>param�tre: direction=N/D
 * 	<br>param�tre: strength=bool
 * 	<br>param�tre: kind=N/D
 * 	<br>param�tre: scope=N/D
 * 	<br>param�tre: restriction=N/D
 */	
/** 
 * putting fire to another object, usually performed by fire (but not necessarily).
 * different from detonating, which is performed by bombs and results in the production of fire.
 * 
 * 	<br>actor: 			any (probably fire, but not necessarily)
 * 	<br>target: 		any
 * 	<br>direction:		any or none
 * 	<br>contact:		any or none
 * 	<br>tilePosition:	any or undefined
 * 	<br>orientation:	any or undefined
 *  
 */
public class GeneralConsume extends GeneralAction<SpecificConsume>
{
	
	public GeneralConsume()
	{	super(ActionName.CONSUME);	
		Role[] allowedActors = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
		setAllowedActors(allowedActors);
		Role[] allowedTargets = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
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
