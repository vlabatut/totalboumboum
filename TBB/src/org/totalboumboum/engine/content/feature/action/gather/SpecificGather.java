package org.totalboumboum.engine.content.feature.action.gather;

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

import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.sprite.Sprite;

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
 * @author Vincent Labatut
 *
 */
public class SpecificGather extends SpecificAction
{
	public SpecificGather(Sprite actor, Sprite target)
	{	super(ActionName.GATHER,actor,target);
	}
	
/*	
	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralGather generalAction;

	@Override
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}
	
	@Override
	protected void initGeneralAction() 
	{	generalAction = new GeneralGather();
		super.initGeneralAction(generalAction);
	}
*/	
}
