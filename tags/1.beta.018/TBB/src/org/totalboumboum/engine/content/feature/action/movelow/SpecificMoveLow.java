package org.totalboumboum.engine.content.feature.action.movelow;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.sprite.Sprite;

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
public class SpecificMoveLow extends SpecificAction
{
	public SpecificMoveLow(Sprite actor, Direction direction)
	{	super(ActionName.MOVELOW,actor);
		setDirection(direction);
	}

/*	
	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralMoveLow generalAction;

	@Override
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}
	
	@Override
	protected void initGeneralAction() 
	{	generalAction = new GeneralMoveLow();
		super.initGeneralAction(generalAction);
	}
*/	
}
