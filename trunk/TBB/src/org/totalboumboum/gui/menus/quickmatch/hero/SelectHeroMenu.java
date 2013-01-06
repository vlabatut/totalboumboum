package org.totalboumboum.gui.menus.quickmatch.hero;

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
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.sprite.SpritePreview;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.game.profile.SpriteInfo;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.menus.explore.heroes.select.SelectedHeroData;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SelectHeroMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonCancel;
	@SuppressWarnings("unused")
	private JButton buttonConfirm;

	private Profile profile;
	
	private SelectedHeroData heroData;

	public SelectHeroMenu(SplitMenuPanel container, MenuPanel parent, Profile profile)
	{	super(container, parent);
		this.profile = profile;
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiSizeTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiButtonTools.createButton(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonCancel = GuiButtonTools.createButton(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		heroData = new SelectedHeroData(container);
		container.setDataPart(heroData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_CONFIRM))
		{	SpritePreview heroPreview = heroData.getSelectedHeroPreview();
			if(heroPreview!=null)
			{	// update profile
				SpriteInfo spriteInfo = profile.getSelectedSprite();
				String spriteName = heroPreview.getName();
				spriteInfo.setName(spriteName);
				String spriteFolder = heroPreview.getFolder();
				spriteInfo.setFolder(spriteFolder);
				String spritePack = heroPreview.getPack();
				spriteInfo.setPack(spritePack);
				
				// reload portraits
				try
				{	ProfileLoader.reloadPortraits(profile);
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
				catch (ClassNotFoundException e1)
				{	e1.printStackTrace();
				}
			}
			parent.refresh();
			
			// NOTE this would be so much cleaner with an events system...
			ServerGeneralConnection connection = Configuration.getConnectionsConfiguration().getServerConnection();
			if(connection!=null)
				connection.profileModified(profile);

			replaceWith(parent);
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	//
	}
}
