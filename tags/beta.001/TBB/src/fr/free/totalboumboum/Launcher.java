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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.configuration.ConfigurationLoader;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.data.configuration.GuiConfigurationLoader;
import fr.free.totalboumboum.gui.menus.main.MainFrame;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.XmlTools;

public class Launcher
{	

	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	
		// splashscreen
		SplashScreen splash = SplashScreen.getSplashScreen();
		
		// init
		updateSplash(splash,"[Loading XML schemas]");
		XmlTools.init();
		updateSplash(splash,"[Loading configuration]");
		Configuration config = ConfigurationLoader.loadConfiguration();
		updateSplash(splash,"[Loading GUI]");
		final GuiConfiguration configuration = GuiConfigurationLoader.loadConfiguration(config);
		updateSplash(splash,"[Initializing GUI]");
		BufferedImage img = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		GuiTools.init(configuration,g);
		ToolTipManager.sharedInstance().setInitialDelay(200);
		updateSplash(splash,"[Done]");
		
		// create GUI
		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	try
				{	new MainFrame(configuration);
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
	
	private static void updateSplash(SplashScreen splash, String msg)
	{	if(splash!=null)
		{	Graphics2D g = (Graphics2D)splash.createGraphics();
			Rectangle size = splash.getBounds();
			g.setComposite(AlphaComposite.Clear);
			g.fillRect(0,0,size.width,size.height);
			g.setPaintMode();
			g.setColor(new Color(204,18,128));
			g.setFont(new Font("Arial",Font.PLAIN,10));
	        g.drawString(msg,70,315);
	        splash.update();
		}
	}
}
