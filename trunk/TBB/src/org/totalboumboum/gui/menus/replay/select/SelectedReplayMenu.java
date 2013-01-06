package org.totalboumboum.gui.menus.replay.select;

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

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.totalboumboum.gui.common.structure.dialog.outside.InputModalDialogPanel;
import org.totalboumboum.gui.common.structure.dialog.outside.ModalDialogPanelListener;
import org.totalboumboum.gui.common.structure.dialog.outside.QuestionModalDialogPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.menus.replay.round.RoundSplitPanel;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.file.archive.GameArchive;
import org.totalboumboum.stream.file.replay.FileClientStream;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.files.FileTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SelectedReplayMenu extends InnerMenuPanel implements DataPanelListener, ModalDialogPanelListener
{	private static final long serialVersionUID = 1L;

	public SelectedReplayMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiSizeTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_REPLAY_LOAD_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonDelete = GuiButtonTools.createButton(GuiKeys.MENU_REPLAY_LOAD_BUTTON_DELETE,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonConfirm = GuiButtonTools.createButton(GuiKeys.MENU_REPLAY_LOAD_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiSizeTools.buttonVerticalSpace)));
		buttonCancel = GuiButtonTools.createButton(GuiKeys.MENU_REPLAY_LOAD_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		levelData = new SelectedReplayData(container,baseFolder);
		levelData.addListener(this);
		container.setDataPart(levelData);
		refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// PANELS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SelectedReplayData levelData;
	private String baseFolder = FilePaths.getReplaysPath();

	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;
	private JButton buttonDelete;

	private void refreshButtons()
	{	FileClientStream replay = levelData.getSelectedReplay();
		if(replay==null)
		{	buttonDelete.setEnabled(false);
			buttonConfirm.setEnabled(false);
		}
		else
		{	buttonDelete.setEnabled(true);
			buttonConfirm.setEnabled(true);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_REPLAY_LOAD_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
		if(e.getActionCommand().equals(GuiKeys.MENU_REPLAY_LOAD_BUTTON_DELETE))
		{	String key = GuiKeys.MENU_REPLAY_LOAD_DELETE_TITLE;
			List<String> text = new ArrayList<String>();
			text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_REPLAY_LOAD_DELETE_QUESTION));
			questionModalDelete = new QuestionModalDialogPanel(getMenuParent(),key,text);
			questionModalDelete.addListener(this);
			getFrame().setModalDialog(questionModalDelete);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_REPLAY_LOAD_BUTTON_CONFIRM))
		{	RoundSplitPanel roundPanel = new RoundSplitPanel(container.getMenuContainer(),container);
			try
			{	FileClientStream selectedReplay = levelData.getSelectedReplay();
				SingleTournament tournament = new SingleTournament(selectedReplay);
				Match match = tournament.getCurrentMatch();
				Round round = match.getCurrentRound();
				roundPanel.setRound(round);
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
			catch (IllegalArgumentException e1)
			{	e1.printStackTrace();
			}
			catch (SecurityException e1)
			{	e1.printStackTrace();
			}
			catch (IllegalAccessException e1)
			{	e1.printStackTrace();
			}
			catch (NoSuchFieldException e1)
			{	e1.printStackTrace();
			}
			replaceWith(roundPanel);
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
	public void dataPanelSelectionChanged(Object object)
	{	refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG PANEL LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private QuestionModalDialogPanel questionModalDelete = null;
	
	@Override
	public void modalDialogButtonClicked(String buttonCode)
	{	getFrame().unsetModalDialog();
		if(questionModalDelete!=null)
		{	questionModalDelete.removeListener(this);
			questionModalDelete = null;
			FileClientStream selectedReplay = levelData.getSelectedReplay();
			if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CONFIRM))
			{	if(selectedReplay!=null)
				{	String folder = selectedReplay.getFolder();
					String path = baseFolder + File.separator + folder;
					File file = new File(path);
					FileTools.deleteDirectory(file);
					levelData.refresh();			
				}			
			}
		}
	}
}
