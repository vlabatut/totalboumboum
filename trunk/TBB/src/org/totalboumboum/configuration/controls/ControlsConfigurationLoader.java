package org.totalboumboum.configuration.controls;

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

import java.io.IOException;
import java.text.NumberFormat;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.controls.ControlSettingsLoader;
import org.totalboumboum.tools.GameData;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ControlsConfigurationLoader
{	
	public static ControlsConfiguration loadControlsConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	ControlsConfiguration result = new ControlsConfiguration();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		for(int i=1;i<=GameData.CONTROL_COUNT;i++)
		{	String fileName = nf.format(i);
			ControlSettings controlSettings = ControlSettingsLoader.loadControlSettings(fileName);
			result.putControlSettings(i,controlSettings);
		}
		return result;
	}
}
