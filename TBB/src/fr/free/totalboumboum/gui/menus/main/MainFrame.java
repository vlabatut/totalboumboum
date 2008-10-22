package fr.free.totalboumboum.gui.menus.main;

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

import javax.swing.JFrame;
import javax.swing.RepaintManager;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.tools.FullRepaintManager;
import fr.free.totalboumboum.gui.tools.GuiFileTools;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame implements WindowListener,MenuContainer
{	private static final long serialVersionUID = 1L;

	private MainMenu mainMenuPanel;

	public MainFrame() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init
		super("TBB v."+GameConstants.VERSION);
		// listener
		addWindowListener(this);
		// set icon
		String iconPath = GuiFileTools.getIconsPath()+File.separator+GuiFileTools.FILE_FRAME;
		Image icon = Toolkit.getDefaultToolkit().getImage(iconPath);
		setIconImage(icon);
		// set dimensions
		setMinimumSize(Configuration.getVideoConfiguration().getPanelDimension());
		setResizable(false);
		//
		UIManager.put("MenuItemUI","CustomMenuItemUI");
		RepaintManager.setCurrentManager(new FullRepaintManager());
		// put panel
		mainMenuPanel = new MainMenu(this,null);
		currentPanel = mainMenuPanel;
		getContentPane().add(mainMenuPanel, BorderLayout.CENTER);
		// size the frame
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
	}

	// ----------------- window listener methods -------------

	public void windowActivated(WindowEvent e)
	{	//engine.setPause(false);
	}

	public void windowDeactivated(WindowEvent e)
	{	//engine.setPause(true);
	}

	public void windowDeiconified(WindowEvent e)
	{	//engine.setPause(false);
	}

	public void windowIconified(WindowEvent e)
	{	//engine.setPause(true);
	}

	public void windowClosing(WindowEvent e)
	{	//engine.setRunning(false);
		exit();
	}

	public void windowClosed(WindowEvent e)
	{
	}

	public void windowOpened(WindowEvent e)
	{
	}
	
	public void exit()
	{	//saveConfiguration();
		System.exit(0);		
	}
	

	
	/////////////////////////////////////////////////////////////////
	// CONFIGURATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private void saveConfiguration()
	{	try
		{	Configuration.saveConfiguration();
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
	}

	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public MainFrame getFrame()
	{	return this;
	}

	/////////////////////////////////////////////////////////////////
	// MENU PANEL		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MenuPanel currentPanel;

	@Override
	public void setMenuPanel(MenuPanel newPanel)
	{	Container contentPane = getContentPane();
		contentPane.remove(currentPanel);
		currentPanel = newPanel;
		currentPanel.refresh();
		contentPane.add(newPanel, BorderLayout.CENTER);
		contentPane.validate();
		contentPane.repaint();
	}
	
	public void setMainMenuPanel()
	{	setMenuPanel(mainMenuPanel);	
	}

	@Override
	public int getMenuHeight()
	{	return Configuration.getVideoConfiguration().getPanelDimension().height;
	}

	@Override
	public int getMenuWidth()
	{	return Configuration.getVideoConfiguration().getPanelDimension().width;
	}
}

