package fr.free.totalboumboum.engine.content.feature.action.push;

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
 * action de pousser un autre objet (peut être un kick)
 * TRANSITIVE
 * 
 * <p>ABILITY PERFORM
 * 	<br>paramètre: actor=self
 * 	<br>paramètre: target=oui (bomb)
 * 	<br>paramètre: direction=oui
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=N/D
 * 	<br>paramètre: restriction=N/D
 * 
 * <p>ABILITY REFUSE
 * 	<br>paramètre: actor=oui (hero)
 * 	<br>paramètre: target=self
 * 	<br>paramètre: direction=oui
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=N/D
 * 	<br>paramètre: restriction=N/D
 * 
 * <p>ABILITY PREVENT
 * 	<br>paramètre: actor=oui (hero)
 * 	<br>paramètre: target=oui (bomb)
 * 	<br>paramètre: direction=oui
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=N/D
 * 	<br>paramètre: restriction=N/D
 */
/** 
 * pushing an object to make it move on the ground (unlink a punch, which aims at moving it in the air)
 * for instance: a hero pushing a bomb to make it slide
 * 
 * 	<br>actor: 			any (probably a hero)
 * 	<br>target: 		any (probably a bomb or a wall)
 * 	<br>direction:		any (not none)
 * 	<br>contact:		intersection or collision
 * 	<br>tilePosition:	same or neighbor
 * 	<br>orientation:	same
 *  
 */
public class GeneralPush extends GeneralAction<SpecificPush>
{
	
	public GeneralPush()
	{	super(ActionName.PUSH);	
		Role[] allowedActors = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
		setAllowedActors(allowedActors);
		Role[] allowedTargets = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
		setAllowedTargets(allowedTargets);
		Direction[] allowedDirections = {Direction.UP,Direction.UPRIGHT,Direction.RIGHT,Direction.DOWNRIGHT,Direction.DOWN,Direction.DOWNLEFT,Direction.LEFT,Direction.UPLEFT};
		setAllowedDirections(allowedDirections);
		Contact[] allowedContacts = {Contact.COLLISION,Contact.INTERSECTION};
		setAllowedContacts(allowedContacts);
		TilePosition[] allowedTilePositions = {TilePosition.NEIGHBOR,TilePosition.SAME};
		setAllowedTilePositions(allowedTilePositions);
		Orientation[] allowedOrientations = {Orientation.SAME};
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
