package org.totalboumboum.gui.tools;

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

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GuiVideoTools
{
	/**
	 * defines the list of all available graphic resolutions on the
	 * current default display, sorted by growing order
	 * @return
	 */
	public static TreeSet<Dimension> getAvailableResolutions()
	{	GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = graphEnv.getDefaultScreenDevice();
		DisplayMode modes[] = device.getDisplayModes();
		TreeSet<Dimension> result = new TreeSet<Dimension>(new Comparator<Dimension>()
		{	@Override
			public int compare(Dimension arg0, Dimension arg1)
			{	int result;
				// width
					result = arg0.width-arg1.width;
				// height
				if(result==0)
					result = arg0.height-arg1.height;
				return result;
			}
		});
		for(DisplayMode m: modes)
		{	Dimension dim = new Dimension(m.getWidth(),m.getHeight());
			result.add(dim);
		}
		return result;
	}	
}
