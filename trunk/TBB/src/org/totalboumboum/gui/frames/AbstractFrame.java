package org.totalboumboum.gui.frames;

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

import javax.swing.JFrame;
import javax.swing.ToolTipManager;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.statistics.StatisticsConfiguration;
import org.totalboumboum.configuration.statistics.StatisticsConfigurationSaver;
import org.totalboumboum.gui.tools.GuiFileTools;
import org.xml.sax.SAXException;


import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractFrame extends JFrame implements WindowListener
{	private static final long serialVersionUID = 1L;

	public AbstractFrame(String title) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init
		super(title);
		
		// listener
		addWindowListener(this);
		setFocusable(true);
		
		// set icon
		String iconPath = GuiFileTools.getIconsPath()+File.separator+GuiFileTools.FILE_FRAME;
		Image icon = Toolkit.getDefaultToolkit().getImage(iconPath);
		setIconImage(icon);
		
		// set tooltip delay
		ToolTipManager.sharedInstance().setInitialDelay(200);
		
		// windows properties
		setResizable(false);	
	}
	
	/////////////////////////////////////////////////////////////////
	// VIDEO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private DisplayMode originalMode;
	private GraphicsDevice device;

	public void makeVisible(GraphicsDevice device, DisplayMode newMode)
	{	try
		{	this.device = device;
			setUndecorated(true);
			device.setFullScreenWindow(this);
			originalMode = device.getDisplayMode();
			device.setDisplayMode(newMode);
//			setSize(newMode.getWidth(),newMode.getHeight());
			validate();
		}
		catch(Exception e)
		{	device.setFullScreenWindow(null);
			dispose();
			makeVisible();
		}
	}
	public void makeVisible()
	{	device = null;
		originalMode = null;
		setUndecorated(false);
		// size the frame
//		Dimension contentDimension = Configuration.getVideoConfiguration().getPanelDimension();
//		getContentPane().setMinimumSize(contentDimension);
//		getContentPane().setPreferredSize(contentDimension);
//		getContentPane().setMaximumSize(contentDimension);
		pack();
		// center the frame
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    setLocation((screenWidth-getSize().width)/2,(screenHeight-getSize().height)/2);						
		// show the frame
		setVisible(true);
		toFront();
		validate();
		repaint();
	}
	
	public void revertFullScreen()
	{	if(device!=null)
			device.setDisplayMode(originalMode);
	}
	
	/////////////////////////////////////////////////////////////////
	// WINDOW LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void windowActivated(WindowEvent e)
	{	
	}

	public void windowDeactivated(WindowEvent e)
	{	
	}

	public void windowDeiconified(WindowEvent e)
	{	
	}

	public void windowIconified(WindowEvent e)
	{	
	}

	public void windowClosed(WindowEvent e)
	{
	}

	public void windowOpened(WindowEvent e)
	{
	}

	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void exit(boolean quicklaunch)
	{	// cancel fullscreen
		revertFullScreen();
		// save engine stats		
		try
		{	StatisticsConfiguration statConfig =  Configuration.getStatisticsConfiguration();
			statConfig.updateLaunchStats(quicklaunch);
			StatisticsConfigurationSaver.saveStatisticsConfiguration(statConfig);
		}
		catch (IOException e1)
		{	e1.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{	e.printStackTrace();
		}
		catch (SAXException e)
		{	e.printStackTrace();
		}		
		// end process
		System.exit(0);		
	}
}

