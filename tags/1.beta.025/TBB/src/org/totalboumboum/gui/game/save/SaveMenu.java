package org.totalboumboum.gui.game.save;

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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.dialog.outside.InputModalDialogPanel;
import org.totalboumboum.gui.common.structure.dialog.outside.ModalDialogPanelListener;
import org.totalboumboum.gui.common.structure.dialog.outside.QuestionModalDialogPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.stream.file.archive.GameArchive;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.files.FileTools;
import org.xml.sax.SAXException;

/**
 * Menu used to save a game.
 * 
 * @author Vincent Labatut
 */
public class SaveMenu extends InnerMenuPanel implements DataPanelListener, ModalDialogPanelListener
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Swing container of this panel.
	 * @param parent
	 * 		Parent menu item.
	 */
	public SaveMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiSizeTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.GAME_SAVE_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonNew = GuiButtonTools.createButton(GuiKeys.GAME_SAVE_BUTTON_NEW,buttonWidth,buttonHeight,fontSize,this);
		buttonDelete = GuiButtonTools.createButton(GuiKeys.GAME_SAVE_BUTTON_DELETE,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonConfirm = GuiButtonTools.createButton(GuiKeys.GAME_SAVE_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonCancel = GuiButtonTools.createButton(GuiKeys.GAME_SAVE_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
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
	/** Data panel */
	private SaveData levelData;

	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Button used to write on an existing save */
	private JButton buttonConfirm;
	/** Cancel button */
	@SuppressWarnings("unused")
	private JButton buttonCancel;
	/** Button used to delete an existing file */
	private JButton buttonDelete;
	/** Button used to create a new file */
	@SuppressWarnings("unused")
	private JButton buttonNew;
	
	/**
	 * Update the buttons depending on 
	 * the file selection.
	 */
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
	/** Current tournament */
	private AbstractTournament tournament;
	/** Folder browsed while selecting files */
	private String baseFolder = FilePaths.getSavesPath();
	
	/**
	 * Changes the tournament handled
	 * by this menu.
	 * 
	 * @param tournament
	 * 		New tournament.
	 */
	public void setTournament(AbstractTournament tournament)
	{	this.tournament = tournament;
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.GAME_SAVE_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
		if(e.getActionCommand().equals(GuiKeys.GAME_SAVE_BUTTON_NEW))
		{	String key = GuiKeys.GAME_SAVE_NEW_TITLE;
			List<String> text = new ArrayList<String>();
			text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_SAVE_NEW_QUESTION));
			String defaultText = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_SAVE_NEW_NAME);
			inputModalNew = new InputModalDialogPanel(getMenuParent(),key,text,defaultText);
			inputModalNew.addListener(this);
			getFrame().setModalDialog(inputModalNew);

	    }
		if(e.getActionCommand().equals(GuiKeys.GAME_SAVE_BUTTON_DELETE))
		{	String key = GuiKeys.GAME_SAVE_DELETE_TITLE;
			List<String> text = new ArrayList<String>();
			text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_SAVE_DELETE_QUESTION));
			questionModalDelete = new QuestionModalDialogPanel(getMenuParent(),key,text);
			questionModalDelete.addListener(this);
			getFrame().setModalDialog(questionModalDelete);
	    }
		else if(e.getActionCommand().equals(GuiKeys.GAME_SAVE_BUTTON_CONFIRM))
		{	String key = GuiKeys.GAME_SAVE_CONFIRM_TITLE;
			List<String> text = new ArrayList<String>();
			text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.GAME_SAVE_CONFIRM_QUESTION));
			questionModalConfirm = new QuestionModalDialogPanel(getMenuParent(),key,text);
			questionModalConfirm.addListener(this);
			getFrame().setModalDialog(questionModalConfirm);
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	
	}

	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER			/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void dataPanelSelectionChanged(Object object)
	{	refreshButtons();
	}
	
	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG PANEL LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Modal dialog used to enter the name of a new file */
	private InputModalDialogPanel inputModalNew = null;
	/** Modal dialog used to confirm a deletion */
	private QuestionModalDialogPanel questionModalDelete = null;
	/** Modal dialog used to confirm a file replacement */
	private QuestionModalDialogPanel questionModalConfirm = null;
	
	@Override
	public void modalDialogButtonClicked(String buttonCode)
	{	getFrame().unsetModalDialog();
		if(inputModalNew!=null)
		{	String input = inputModalNew.getInput();
			inputModalNew.removeListener(this);
			inputModalNew = null;
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
//				getDataPart().refresh();
//				refreshButtons();
				replaceWith(parent);
			}
		}
		else if(questionModalDelete!=null)
		{	questionModalDelete.removeListener(this);
			questionModalDelete = null;				
			GameArchive selectedArchive = levelData.getSelectedGameArchive();
			if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CONFIRM))
			{	if(selectedArchive!=null)
				{	String folder = selectedArchive.getFolder();
					String path = baseFolder+File.separator+folder;
					File file = new File(path);
					FileTools.deleteDirectory(file);
					levelData.refresh();
				}
			}
		}
		else if(questionModalConfirm!=null)
		{	questionModalConfirm.removeListener(this);
			questionModalConfirm = null;				
			if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CONFIRM))
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
	}
}
