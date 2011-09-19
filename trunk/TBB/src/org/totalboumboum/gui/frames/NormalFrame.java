package org.totalboumboum.gui.frames;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import javax.swing.JLayeredPane;
import javax.swing.RepaintManager;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.gui.common.structure.MenuContainer;
import org.totalboumboum.gui.common.structure.dialog.outside.ModalDialogPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.menus.main.MainMenu;
import org.totalboumboum.gui.tools.FullRepaintManager;
import org.totalboumboum.tools.GameData;
import org.xml.sax.SAXException;



import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class NormalFrame extends AbstractFrame implements MenuContainer
{	private static final long serialVersionUID = 1L;

	private MainMenu mainMenuPanel;

	public NormalFrame() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init
		super("TBB v."+GameData.VERSION);
		
		// UI manager
		UIManager.put("MenuItemUI","CustomMenuItemUI");
		RepaintManager.setCurrentManager(new FullRepaintManager());
		
		// put panel
		mainMenuPanel = new MainMenu(this,null);
		currentPanel = mainMenuPanel;
		getContentPane().add(mainMenuPanel, BorderLayout.CENTER);
	}

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private void saveConfiguration()
	{	// TODO en fait c'est la partie qu'il faut enregistrer, car la conf est déjà enregistr�e si elle a été modifiée
		try
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
	public NormalFrame getFrame()
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
//		currentPanel.refresh();
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
	
	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void restart()
	{	// reinit resolution
		revertFullScreen();
		// restart
		String ai = "."+File.separator+"resources"+File.separator+"ai";
		String bin = "."+File.separator+"bin";
		String jdom = "."+File.separator+"resources"+File.separator+"lib"+File.separator+"jdom.jar";
		String cp = bin+File.pathSeparator+jdom+File.pathSeparator+ai;
		String launcher = "org.totalboumboum.Launcher";
		String splash = "."+File.separator+"resources"+File.separator+"gui"+File.separator+"images"+File.separator+"splash.png";
		Runtime runtime = Runtime.getRuntime();
		try
		{	runtime.exec("java -Xmx128m -splash:"+splash+" -classpath "+cp+" "+launcher);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		exit(false);
	}

	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ModalDialogPanel<?> modalDialogPanel;
	
	public void setModalDialog(ModalDialogPanel<?> modalDialogPanel)
	{	this.modalDialogPanel = modalDialogPanel;
		modalDialogPanel.setBounds(0,0,modalDialogPanel.getWidth(),modalDialogPanel.getHeight());
		JLayeredPane layeredPane = getLayeredPane();
//		layeredPane.setLayout(new BoxLayout(layeredPane,BoxLayout.PAGE_AXIS));
		layeredPane.add(modalDialogPanel,JLayeredPane.MODAL_LAYER);
	}
	
	public void unsetModalDialog()
	{	JLayeredPane layeredPane = getLayeredPane();
		layeredPane.remove(modalDialogPanel);
		modalDialogPanel = null;
		repaint();
	}

	/////////////////////////////////////////////////////////////////
	// WINDOW LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void windowClosing(WindowEvent e)
	{	exit(false);
	}
}

