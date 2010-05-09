package fr.free.totalboumboum.engine.content.feature.action.jump;

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
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

/** 
 * begining an aerial move on its own (not peing punched)
 * for instance: hero jumping
 * 
 * 	<br>actor: 			any (probably a hero)
 * 	<br>target: 		none
 * 	<br>direction:		any or none
 * 	<br>contact:		none
 * 	<br>tilePosition:	undefined
 * 	<br>orientation:	undefined
 *  
 */
public class SpecificJump extends SpecificAction
{
	public SpecificJump(Sprite actor)
	{	super(ActionName.JUMP,actor);
	}

/*	
	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralJump generalAction;

	@Override
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}
	
	@Override
	protected void initGeneralAction() 
	{	generalAction = new GeneralJump();
		super.initGeneralAction(generalAction);
	}
*/	
}
