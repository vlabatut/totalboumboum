package fr.free.totalboumboum.gui.game.save;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SaveMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;

	public SaveMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		ArrayList<String> texts = GuiKeys.getKeysLike(GuiKeys.GAME_SAVE_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiTools.createButton(GuiKeys.GAME_SAVE_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonCancel = GuiTools.createButton(GuiKeys.GAME_SAVE_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		levelData = new SaveData(container,FileTools.getTournamentsPath());
		container.setDataPart(levelData);
	}

	/////////////////////////////////////////////////////////////////
	// PANELS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SaveData levelData;

	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament;
	
	public void setTournament(AbstractTournament tournament)
	{	this.tournament = tournament;		
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.GAME_SAVE_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_SAVE_BUTTON_CANCEL))
		{	String path = FileTools.getSavesPath()+levelData.getSelectedFile();
			File file = new File(path);
			FileOutputStream out = new FileOutputStream(file);
			ObjectOutputStream oOut = new ObjectOutputStream(out);
			oOut.writeObject(tournament);
			oOut.close();
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
