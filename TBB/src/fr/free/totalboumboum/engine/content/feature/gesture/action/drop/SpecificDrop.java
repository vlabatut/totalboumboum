package fr.free.totalboumboum.engine.content.feature.gesture.action.drop;

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

import fr.free.totalboumboum.engine.content.feature.gesture.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.gesture.action.IncompatibleParameterException;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;

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
 */
public abstract class SpecificDrop extends SpecificAction
{
	public SpecificDrop(Hero actor, Bomb target) throws IncompatibleParameterException
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
