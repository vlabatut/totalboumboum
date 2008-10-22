package fr.free.totalboumboum.gui.options.video;

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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.configuration.controls.ControlSettings;
import fr.free.totalboumboum.configuration.controls.ControlsConfigurationSaver;
import fr.free.totalboumboum.configuration.video.VideoConfiguration;
import fr.free.totalboumboum.configuration.video.VideoConfigurationSaver;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.options.controls.ControlsData;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class VideoMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;

	private VideoData videoData;

	public VideoMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_CONFIRM,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonCancel = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_OPTIONS_BUTTON_CANCEL,this);
		add(Box.createVerticalGlue());		

		// panels
		videoData = new VideoData();
		container.setDataPart(videoData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_CONFIRM))
		{	VideoConfiguration videoConfiguration = videoData.getVideoConfiguration();
			Configuration.setVideoConfiguration(videoConfiguration);
			try
			{	VideoConfigurationSaver.saveVideoConfiguration(Configuration.getVideoConfiguration());
			}
			catch (IllegalArgumentException e1)
			{	e1.printStackTrace();
			}
			catch (SecurityException e1)
			{	e1.printStackTrace();
			}
			catch (ParserConfigurationException e1)
			{	e1.printStackTrace();
			}
			catch (SAXException e1)
			{	e1.printStackTrace();
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}
			catch (IllegalAccessException e1)
			{	e1.printStackTrace();
			}
			catch (NoSuchFieldException e1)
			{	e1.printStackTrace();
			}
//TODO propager �ventuellement au round (car il n'y a pas modification mais remplacement, donc si c d�j� affect� � un player..
			replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_OPTIONS_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}
