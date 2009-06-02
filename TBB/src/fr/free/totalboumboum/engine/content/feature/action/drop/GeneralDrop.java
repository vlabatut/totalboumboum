package fr.free.totalboumboum.engine.content.feature.action.drop;

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
 * action de d�poser un objet (une bombe)
 * TRANSITIVE
 * 
 * <p>ABILITY PERFORM
 * 	<br>param�tre: actor=self
 * 	<br>param�tre: target=oui (bomb)
 * 	<br>param�tre: direction=N/D
 * 	<br>param�tre: strength=bool
 * 	<br>param�tre: kind=type de bombe
 * 	<br>param�tre: scope=N/D
 * 	<br>param�tre: restriction=N/D
 *  
 * <p>ABILITY REFUSE
 * 	<br>N/D
 * 
 * <p>ABILITY PREVENT
 * 	<br>param�tre: actor=oui (hero)
 * 	<br>param�tre: target=oui (bomb)
 * 	<br>param�tre: direction=N/D
 * 	<br>param�tre: strength=bool
 * 	<br>param�tre: kind=type de bombe
 * 	<br>param�tre: scope=N/D
 * 	<br>param�tre: restriction=N/D
 */
/** 
 * putting an object on the ground.
 * usually a player dropping a bomb
 * 
 * 	<br>actor: 			any (probably a hero)
 * 	<br>target: 		any (probably a bomb)
 * 	<br>direction:		any or none
 * 	<br>contact:		none (the target is not supposed to be ongame)
 * 	<br>tilePosition:	undefined
 * 	<br>orientation:	undefined
 *  
 */
public class GeneralDrop extends GeneralAction<SpecificDrop>
{
	
	public GeneralDrop()
	{	super(ActionName.DROP);	
		Role[] allowedActors = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
		setAllowedActors(allowedActors);
		Role[] allowedTargets = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
		setAllowedTargets(allowedTargets);
		Direction[] allowedDirections = {Direction.NONE,Direction.UP,Direction.UPRIGHT,Direction.RIGHT,Direction.DOWNRIGHT,Direction.DOWN,Direction.DOWNLEFT,Direction.LEFT,Direction.UPLEFT};
		setAllowedDirections(allowedDirections);
		Contact[] allowedContacts = {Contact.NONE};
		setAllowedContacts(allowedContacts);
		TilePosition[] allowedTilePositions = {TilePosition.UNDEFINED};
		setAllowedTilePositions(allowedTilePositions);
		Orientation[] allowedOrientations = {Orientation.UNDEFINED};
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
