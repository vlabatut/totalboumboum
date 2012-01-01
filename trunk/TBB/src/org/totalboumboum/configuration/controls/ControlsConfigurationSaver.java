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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ControlsConfigurationSaver
{
	public static void saveControlsConfiguration(ControlsConfiguration controlsConfiguration) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		HashMap<Integer,ControlSettings> controlSettings = controlsConfiguration.getControlSettings();
		Iterator<Entry<Integer,ControlSettings>> it = controlSettings.entrySet().iterator();
		while(it.hasNext())
		{	Entry<Integer,ControlSettings> entry = it.next();
			Integer index = entry.getKey();
			ControlSettings value = entry.getValue();
			String fileName = nf.format(index);
			ControlSettingsSaver.saveControlSettings(fileName,value);
		}
	}
}
