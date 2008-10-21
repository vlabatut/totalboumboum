package fr.free.totalboumboum;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.ConfigurationLoader;
import fr.free.totalboumboum.gui.quicklaunch.QuickFrame;
import fr.free.totalboumboum.tools.XmlTools;

public class QuickLauncher
{

	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	
		// init
		XmlTools.init();
		ConfigurationLoader.loadConfiguration();
		// graphic conf
		GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice graphDevice = graphEnv.getDefaultScreenDevice();
		final GraphicsConfiguration graphicConf = graphDevice.getDefaultConfiguration();		
		
		// create GUI
		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	try
				{	new QuickFrame(graphicConf);
				}
				catch (IllegalArgumentException e)
				{	e.printStackTrace();
				}
				catch (SecurityException e)
				{	e.printStackTrace();
				}
				catch (ParserConfigurationException e)
				{	e.printStackTrace();
				}
				catch (SAXException e)
				{	e.printStackTrace();
				}
				catch (IOException e)
				{	e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{	e.printStackTrace();
				}
				catch (NoSuchFieldException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
			}
		});		
	}

}
