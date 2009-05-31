package fr.free.totalboumboum.engine.content.feature.action.cry;

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

import fr.free.totalboumboum.engine.content.feature.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.action.IncompatibleParameterException;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

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
public class SpecificCry extends SpecificAction<GeneralCry>
{
	public SpecificCry(Sprite actor, Sprite target) throws IncompatibleParameterException
	{	super(ActionName.CRY,actor,target);
	}
}
