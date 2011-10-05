package fr.free.totalboumboum.engine.content.feature.event;

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

public class ActionEvent extends AbstractEvent
{	
	public static final String ENV_COMBUSTION = "ENV_COMBUSTION";
	public static final String ENV_ITEM_ENCOUNTERED = "ENV_ITEM_ENCOUNTERED";
//	public static final String ENV_TERMINATED_BOMB = "ENV_TERMINATED_BOMB";
	
	private SpecificAction action;
	
	public ActionEvent(SpecificAction action)
	{	this.action = action;	
	}
	
	public SpecificAction getAction()
	{	return action;	
	}
	
	public void finish()
	{	if(!finished)
		{	super.finish();
			// action
			action.finish();
			action = null;
		}
	}
}