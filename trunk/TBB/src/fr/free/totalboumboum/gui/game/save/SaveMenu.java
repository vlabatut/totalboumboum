package fr.free.totalboumboum.gui.game.save;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.archive.GameArchive;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.common.structure.dialog.ModalDialogPanelListener;
import fr.free.totalboumboum.gui.common.structure.dialog.input.InputModalDialogPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SaveMenu extends InnerMenuPanel implements DataPanelListener,ModalDialogPanelListener
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
		buttonNew = GuiTools.createButton(GuiKeys.GAME_SAVE_BUTTON_NEW,buttonWidth,buttonHeight,fontSize,this);
		buttonDelete = GuiTools.createButton(GuiKeys.GAME_SAVE_BUTTON_DELETE,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonConfirm = GuiTools.createButton(GuiKeys.GAME_SAVE_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonCancel = GuiTools.createButton(GuiKeys.GAME_SAVE_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		levelData = new SaveData(container,baseFolder);
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
	@SuppressWarnings("unused")
	private JButton buttonNew;

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
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament;
	private String baseFolder = FileTools.getSavesPath();
	
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
		if(e.getActionCommand().equals(GuiKeys.GAME_SAVE_BUTTON_NEW))
		{	String key = GuiKeys.GAME_SAVE_NEW_TITLE;
			ArrayList<String> text = new ArrayList<String>();
			text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_SAVE_NEW_QUESTION));
			String defaultText = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_SAVE_NEW_NAME);
			inputPanel = new InputModalDialogPanel(getMenuParent(),key,text,defaultText);
			inputPanel.addListener(this);
			getFrame().setModalDialog(inputPanel);

	    }
		if(e.getActionCommand().equals(GuiKeys.GAME_SAVE_BUTTON_DELETE))
		{	GameArchive selectedArchive = levelData.getSelectedGameArchive();
			if(selectedArchive!=null)
			{	String folder = selectedArchive.getFolder();
				String path = baseFolder+File.separator+folder;
				File file = new File(path);
				FileTools.deleteDirectory(file);
				levelData.refresh();			
			}
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_SAVE_BUTTON_CONFIRM))
		{	try
			{	String folder = levelData.getSelectedGameArchive().getFolder();
				GameArchive.saveGame(folder,tournament);
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
	
	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG PANEL LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private InputModalDialogPanel inputPanel;
	
	@Override
	public void modalDialogButtonClicked(String buttonCode)
	{	// dialog
		String input = inputPanel.getInput();
		getFrame().unsetModalDialog();
		inputPanel = null;
		if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CONFIRM))
		{	// create & save
			try
			{	GameArchive.saveGame(input,tournament);
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
//			getDataPart().refresh();
//			refreshButtons();
			replaceWith(parent);
		}
	}
}
