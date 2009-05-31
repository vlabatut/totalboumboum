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

import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;

/** 
 * action d'apparaitre de nulle part (suite � t�l�port, ou drop) 
 * INTRANSITIVE
 * 
 * <p>ABILITY PERFORM
 * 	<br>N/D
 * 
 * <p>ABILITY REFUSE
 *  <br>N/D
 *  
 * <p>ABILITY PREVENT
 * 	<br>param�tre: actor=oui
 * 	<br>param�tre: target=oui (floor)
 * 	<br>param�tre: direction=N/D
 * 	<br>param�tre: strength=bool
 * 	<br>param�tre: kind=N/D
 * 	<br>param�tre: scope=oui
 * 	<br>param�tre: restriction=SPRITE_TRAVERSE
 */	
public class SpecificAppear extends SpecificAction<GeneralAppear>
{
	public SpecificAppear(Sprite actor, Floor target)
	{	
		
	}
}
