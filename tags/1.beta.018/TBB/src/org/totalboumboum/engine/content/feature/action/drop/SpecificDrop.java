package org.totalboumboum.engine.content.feature.action.drop;

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

import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.sprite.Sprite;

/** 
 * putting an object on the ground.
 * usually a player dropping a bomb
 * 
 * 	<br>actor: 			hero [any]
 * 	<br>target: 		bomb [any]
 * 	<br>direction:		any or none
 * 	<br>contact:		none (the target is not supposed to be ongame)
 * 	<br>tilePosition:	undefined
 * 	<br>orientation:	undefined
 * 
 * @author Vincent Labatut
 *
 */
public class SpecificDrop extends SpecificAction
{
	public SpecificDrop(Sprite actor, Sprite target)
	{	super(ActionName.DROP,actor,target);
	}
/*	
	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralDrop generalAction;

	@Override
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}
	
	@Override
	protected void initGeneralAction() 
	{	generalAction = new GeneralDrop();
		super.initGeneralAction(generalAction);
	}
*/
}
