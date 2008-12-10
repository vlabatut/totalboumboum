package fr.free.totalboumboum.gui.common.structure.subpanel;

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

import fr.free.totalboumboum.gui.tools.GuiTools;

public class EntitledSubPanelText extends EntitledSubPanel
{	private static final long serialVersionUID = 1L;
	private SubPanelText textPanel;
	
	public EntitledSubPanelText(int width, int height)
	{	super(width,height);

		float fontSize = getTitleFontSize()/2;
		textPanel = new SubPanelText(getDataWidth(),getDataHeight(),fontSize);
		setDataPanel(textPanel);
		textPanel.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setText(String text)
	{	textPanel.setText(text);
	}
	
	/////////////////////////////////////////////////////////////////
	// FONT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setFontSize(float fontSize)
	{	textPanel.setFontSize(fontSize);
	}
	
	public float getFontSize()
	{	return textPanel.getFontSize();
	}
}
