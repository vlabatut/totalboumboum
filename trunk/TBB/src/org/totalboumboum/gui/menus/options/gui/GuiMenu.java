package org.totalboumboum.gui.menus.options.gui;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.gui.common.structure.dialog.outside.ModalDialogPanelListener;
import org.totalboumboum.gui.common.structure.dialog.outside.QuestionModalDialogPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.data.configuration.misc.MiscConfiguration;
import org.totalboumboum.gui.data.configuration.misc.MiscConfigurationSaver;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GuiMenu extends InnerMenuPanel implements ModalDialogPanelListener
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonConfirm;
	@SuppressWarnings("unused")
	private JButton buttonCancel;

	private GuiData guiData;

	public GuiMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		List<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_OPTIONS_BUTTON);
		int fontSize = GuiFontTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonConfirm = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CONFIRM,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createRigidArea(new Dimension(0,GuiTools.buttonVerticalSpace)));
		buttonCancel = GuiTools.createButton(GuiKeys.MENU_OPTIONS_BUTTON_CANCEL,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		guiData = new GuiData(container);
		container.setDataPart(guiData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CONFIRM))
		{	MiscConfiguration miscConfiguration = guiData.getMiscConfiguration();
			boolean restart = false;
			if(!miscConfiguration.getLanguageName().equals(GuiConfiguration.getMiscConfiguration().getLanguageName()))
				restart = true;
			else if(!miscConfiguration.getFontName().equals(GuiConfiguration.getMiscConfiguration().getFontName()))
				restart = true;
			else if(!miscConfiguration.getBackgroundName().equals(GuiConfiguration.getMiscConfiguration().getBackgroundName()))
				restart = true;
			if(restart)
			{	String key = GuiKeys.MENU_OPTIONS_CONFIRM_TITLE;
				List<String> text = new ArrayList<String>();
				text.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_CONFIRM_QUESTION));
				questionModal = new QuestionModalDialogPanel(getMenuParent(),key,text);
				questionModal.addListener(this);
				getFrame().setModalDialog(questionModal);
			}
			else
				confirm(restart);
		}
		else if(e.getActionCommand().equals(GuiKeys.MENU_OPTIONS_BUTTON_CANCEL))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}

	private void confirm(boolean restart)
	{	MiscConfiguration miscConfiguration = guiData.getMiscConfiguration();
		GuiConfiguration.setMiscConfiguration(miscConfiguration);
		try
		{	MiscConfigurationSaver.saveMiscConfiguration(GuiConfiguration.getMiscConfiguration());
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
		if(restart)
			getFrame().restart();
//TODO propager éventuellement au round (car il n'y a pas modification mais remplacement, donc si c déjà affecté à un player..
		else
			replaceWith(parent);
	}

	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG PANEL LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private QuestionModalDialogPanel questionModal = null;
	
	@Override
	public void modalDialogButtonClicked(String buttonCode)
	{	getFrame().unsetModalDialog();
		if(questionModal!=null)
		{	questionModal = null;
			if(buttonCode.equals(GuiKeys.COMMON_DIALOG_CONFIRM))
			{	confirm(true);			
			}
		}
	}
}
