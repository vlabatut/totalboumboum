package fr.free.totalboumboum.gui.menus.tournament.load;

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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.archive.GameArchive;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.game.save.SaveData;
import fr.free.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class LoadMenu extends InnerMenuPanel implements DataPanelListener
{	private static final long serialVersionUID = 1L;

	public LoadMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		ArrayList<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonDelete = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_DELETE,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonConfirm = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonCancel = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		levelData = new SaveData(container,baseFolder);
		levelData.setTitleKey(GuiKeys.MENU_TOURNAMENT_LOAD_TITLE);
		levelData.addListener(this);
		container.setDataPart(levelData);
		refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// PANELS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SaveData levelData;

	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;
	private JButton buttonDelete;

	private void refreshButtons()
	{	GameArchive gameArchive = levelData.getSelectedGameArchive();
		if(gameArchive==null)
		{	buttonDelete.setEnabled(false);
			buttonConfirm.setEnabled(false);
		}
		else
		{	buttonDelete.setEnabled(true);
			buttonConfirm.setEnabled(true);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT CONTAINER			/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamentSplitPanel tournamentPanel;
	private String baseFolder = FileTools.getSavesPath();
	
	public void setTournamentPanel(TournamentSplitPanel tournamentPanel)
	{	this.tournamentPanel = tournamentPanel;
	}

	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
		if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_DELETE))
		{	GameArchive selectedArchive = levelData.getSelectedGameArchive();
			if(selectedArchive!=null)
			{	String folder = selectedArchive.getFolder();
				String path = baseFolder+File.separator+folder;
				File file = new File(path);
				FileTools.deleteDirectory(file);
				levelData.refresh();			
			}
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_CONFIRM))
		{	try
			{	String folder = levelData.getSelectedGameArchive().getFolder();
				String path = FileTools.getSavesPath()+File.separator+folder;
				String fileName = FileTools.FILE_ARCHIVE+FileTools.EXTENSION_DATA;
				File file = new File(path+File.separator+fileName);
				FileInputStream in = new FileInputStream(file);
				ObjectInputStream oIn = new ObjectInputStream(in);
				AbstractTournament tournament = (AbstractTournament)oIn.readObject();
				oIn.close();
				tournament.reloadPortraits();
				//
				tournamentPanel.setTournament(tournament);
				replaceWith(tournamentPanel);
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}
			catch (ClassNotFoundException e1)
			{	e1.printStackTrace();
			}
			catch (ParserConfigurationException e1)
			{	e1.printStackTrace();
			}
			catch (SAXException e1)
			{	e1.printStackTrace();
			}
			//
			replaceWith(parent);
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	
	}

	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER			/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void dataPanelSelectionChanged()
	{	refreshButtons();
	}
}
