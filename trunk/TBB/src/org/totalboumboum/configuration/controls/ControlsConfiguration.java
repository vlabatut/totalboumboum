package org.totalboumboum.configuration.controls;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.util.HashMap;

/**
 * This class handles all options regarding
 * the game controls aspects.
 * 
 * @author Vincent Labatut
 */
public class ControlsConfiguration
{
	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map of all control settings */ 
	private HashMap<Integer,ControlSettings> controlSettings = new HashMap<Integer,ControlSettings>();
	
	/**
	 * Returns all control settings.
	 * 
	 * @return
	 * 		Map of control settings.
	 */
	public HashMap<Integer,ControlSettings> getControlSettings()
	{	return controlSettings;	
	}
	
	/**
	 * Change one of the control settings.
	 * 
	 * @param index
	 * 		Index of the settings.
	 * @param controlSetting
	 * 		New controls for the specified index.
	 */
	public void putControlSettings(int index, ControlSettings controlSetting)
	{	controlSettings.put(index,controlSetting);
	}
}
