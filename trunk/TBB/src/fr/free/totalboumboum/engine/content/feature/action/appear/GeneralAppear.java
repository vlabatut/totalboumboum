package fr.free.totalboumboum.engine.content.feature.action.appear;

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

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.action.Contact;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.IncompatibleParameterException;
import fr.free.totalboumboum.engine.content.feature.action.Orientation;
import fr.free.totalboumboum.engine.content.feature.action.Role;
import fr.free.totalboumboum.engine.content.feature.action.TilePosition;

/** 
 * action d'apparaitre de nulle part (suite à téléport, ou drop) 
 * INTRANSITIVE
 * 
 * <p>ABILITY PERFORM
 * 	<br>N/D
 * 
 * <p>ABILITY REFUSE
 *  <br>N/D
 *  
 * <p>ABILITY PREVENT
 * 	<br>paramètre: actor=oui
 * 	<br>paramètre: target=oui (floor)
 * 	<br>paramètre: direction=N/D
 * 	<br>paramètre: strength=bool
 * 	<br>paramètre: kind=N/D
 * 	<br>paramètre: scope=oui
 * 	<br>paramètre: restriction=SPRITE_TRAVERSE
 */	
/** 
 * appearing in a tile, coming from nowhere (after a teleport, a drop, at the begining of a round, etc)
 * 
 * 	<br>actor: 			any
 * 	<br>target: 		any (probably a floor but not necessarily
 * 	<br>direction:		any or none
 * 	<br>contact:		any or none
 * 	<br>tilePosition:	any or undefined
 * 	<br>orientation:	any or undefined
 *  
 */
public class GeneralAppear extends GeneralAction<SpecificAppear>
{
	private static Role[] allowedActors = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
	private static Role[] allowedTargets = {Role.BLOCK,Role.BOMB,Role.FIRE,Role.FLOOR,Role.HERO,Role.ITEM};
	private static Direction[] allowedDirections = {Direction.NONE,Direction.UP,Direction.UPRIGHT,Direction.RIGHT,Direction.DOWNRIGHT,Direction.DOWN,Direction.DOWNLEFT,Direction.LEFT,Direction.UPLEFT};
	private static Contact[] allowedContacts = {Contact.NONE,Contact.COLLISION,Contact.INTERSECTION};
	private static TilePosition[] allowedTilePositions = {TilePosition.UNDEFINED,TilePosition.NEIGHBOR,TilePosition.REMOTE,TilePosition.SAME};
	private static Orientation[] allowedOrientations = {Orientation.UNDEFINED,Orientation.OPPOSITE,Orientation.OTHER,Orientation.SAME};
	
	public GeneralAppear()
	{	super(ActionName.APPEAR);	
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addActor(Role actor) throws IncompatibleParameterException
	{	if(Arrays.asList(allowedActors).contains(actor) && !getActors().contains(actor))
			super.addActor(actor);		
	}
	
	/////////////////////////////////////////////////////////////////
	// TARGETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addTarget(Role target) throws IncompatibleParameterException
	{	if(Arrays.asList(allowedTargets).contains(target) && !getTargets().contains(target))
			super.addTarget(target);		
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addDirection(Direction direction) throws IncompatibleParameterException
	{	if(Arrays.asList(allowedDirections).contains(direction) && !getDirections().contains(direction))
			super.addDirection(direction);		
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTACTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addContact(Contact contact) throws IncompatibleParameterException
	{	if(Arrays.asList(allowedContacts).contains(contact) && !getContacts().contains(contact))
			super.addContact(contact);		
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE POSITIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addTilePosition(TilePosition tilePosition) throws IncompatibleParameterException
	{	if(Arrays.asList(allowedTilePositions).contains(tilePosition) && !getTilePositions().contains(tilePosition))
			super.addTilePosition(tilePosition);		
	}
	
	/////////////////////////////////////////////////////////////////
	// ORIENTATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addOrientation(Orientation orientation) throws IncompatibleParameterException
	{	if(Arrays.asList(allowedOrientations).contains(orientation) && !getOrientations().contains(orientation))
			super.addOrientation(orientation);
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public GeneralAction<?> copy(GeneralAction<?> action) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
