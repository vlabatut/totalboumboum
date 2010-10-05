package org.totalboumboum.engine.content.feature.action.push;

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
 * @author Vincent Labatut
 *
 */
public class SpecificPush extends SpecificAction
{
	public SpecificPush(Sprite actor, Sprite target)
	{	super(ActionName.PUSH,actor,target);
	}
	
/*	
	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralPush generalAction;

	@Override
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}
	
	@Override
	protected void initGeneralAction() 
	{	generalAction = new GeneralPush();
		super.initGeneralAction(generalAction);
	}
*/	
}
