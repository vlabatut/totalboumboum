package org.totalboumboum.gui.menus.tournament.load;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.dialog.outside.ModalDialogPanelListener;
import org.totalboumboum.gui.common.structure.dialog.outside.QuestionModalDialogPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.game.save.SaveData;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
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
 * Panel containing the menu associated to the file-related
 * tournament actions.
 * 
 * @author Vincent Labatut
 */
public class LoadMenu extends InnerMenuPanel implements DataPanelListener, ModalDialogPanelListener
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a new menu.
	 * 
	 * @param container
	 * 		Container panel.
	 * @param parent
	 * 		Parent menu.
	 */
	public LoadMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiSizeTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonDelete = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_DELETE,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonConfirm = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		buttonStats = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_STATISTICS,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonCancel = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
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
	/** Panel used to save tournament data */
	private SaveData levelData;

	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Button used to confirm the choice */
	private JButton buttonConfirm;
	/** Button used to load only stats */
	private JButton buttonStats;
	/** Button used to cancel */
	@SuppressWarnings("unused")
	private JButton buttonCancel;
	/** Button used to delete an existing tournament */
	private JButton buttonDelete;

	@SuppressWarnings("javadoc")
	private void refreshButtons()
	{	GameArchive gameArchive = levelData.getSelectedGameArchive();
		if(gameArchive==null)
		{	buttonDelete.setEnabled(false);
			buttonConfirm.setEnabled(false);
			buttonStats.setEnabled(false);
		}
		else
		{	buttonDelete.setEnabled(true);
			buttonConfirm.setEnabled(true);
			buttonStats.setEnabled(true);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT CONTAINER			/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Panel displaying the tournament */
	private TournamentSplitPanel tournamentPanel;
	/** Folder containing all tournaments */
	private String baseFolder = FilePaths.getSavesPath();
	
	/**
	 * Changes the panel displaying the tournament.
	 * 
	 * @param tournamentPanel
	 * 		Panel displaying the tournament.
	 */
	public void setTournamentPanel(TournamentSplitPanel tournamentPanel)
	{	this.tournamentPanel = tournamentPanel;
	}

	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
		if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_DELETE))
		{	String key = GuiKeys.MENU_TOURNAMENT_LOAD_DELETE_TITLE;
			List<String> text = new ArrayList<String>();
			text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_TOURNAMENT_LOAD_DELETE_QUESTION));
			questionModalDelete = new QuestionModalDialogPanel(getMenuParent(),key,text);
			questionModalDelete.addListener(this);
			getFrame().setModalDialog(questionModalDelete);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_CONFIRM))
		{	try
			{	String folder = levelData.getSelectedGameArchive().getFolder();
				AbstractTournament tournament = GameArchive.loadGame(folder);
				tournamentPanel.setTournament(tournament);
				Configuration.getGameConfiguration().getTournamentConfiguration().setTournament(tournament);
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
			replaceWith(tournamentPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_LOAD_BUTTON_STATISTICS))
		{	try
			{	String folder = levelData.getSelectedGameArchive().getFolder();
				AbstractTournament tournament = GameArchive.loadGame(folder);
				tournamentPanel.setTournamentStats(tournament);
				Configuration.getGameConfiguration().getTournamentConfiguration().setTournament(tournament);
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
			replaceWith(tournamentPanel);
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	//
	}

	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER			/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void dataPanelSelectionChanged(Object object)
	{	refreshButtons();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	// nothing to do
	}
	
	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG PANEL LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Modal question for the deletion of a tournament */
	private QuestionModalDialogPanel questionModalDelete = null;
	
	@Override
	public void modalDialogButtonClicked(String buttonCode)
	{	getFrame().unsetModalDialog();
		if(questionModalDelete!=null)
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
	}
}
