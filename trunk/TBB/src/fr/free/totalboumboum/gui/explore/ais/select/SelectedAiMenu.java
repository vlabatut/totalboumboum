package fr.free.totalboumboum.gui.explore.ais.select;

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

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class SelectedAiMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private JButton buttonBack;

	private SelectedAiData aiData;

	public SelectedAiMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// sizes
		int buttonWidth = getWidth();
		int buttonHeight = GuiTools.buttonTextHeight;
		ArrayList<String> texts = GuiKeys.getKeysLike(GuiKeys.MENU_RESOURCES_AI_BUTTON);
		int fontSize = GuiTools.getOptimalFontSize(buttonWidth*0.8, buttonHeight*0.9, texts);

		// buttons
		add(Box.createVerticalGlue());
		buttonBack = GuiTools.createButton(GuiKeys.MENU_RESOURCES_AI_BUTTON_BACK,buttonWidth,buttonHeight,fontSize,this);
		add(Box.createVerticalGlue());		

		// panels
		aiData = new SelectedAiData(container);
		container.setDataPart(aiData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_RESOURCES_AI_BUTTON_BACK))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	//
	}
}
