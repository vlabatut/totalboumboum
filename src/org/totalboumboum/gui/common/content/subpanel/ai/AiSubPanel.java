package org.totalboumboum.gui.common.content.subpanel.ai;

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.AiPreview;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;

/**
 * Panel displaying AI agent info.
 * 
 * @author Vincent Labatut
 */
public class AiSubPanel extends TableSubPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Number of lines */
	private static final int LINES = 10;
	/** Number of columns by group */
	private static final int COL_SUBS = 2;
	/** Number of column groups */
	private static final int COL_GROUPS = 1;
	
	/**
	 * Builds a new panel.
	 * 
	 * @param width
	 * 		Width of the panel.
	 * @param height
	 * 		Height of the panel.
	 */
	public AiSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,COL_GROUPS,COL_SUBS,true);
		
		setAiPreview(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** AI preview currently displayed */
	private AiPreview aiPreview;

	/**
	 * Returns the AI preview currently displayed.
	 * 
	 * @return
	 * 		AI preview currently displayed.
	 */
	public AiPreview getAiPreview()
	{	return aiPreview;	
	}
	
	/**
	 * Changes the AI preview currently displayed.
	 * 
	 * @param selectedAi
	 * 		New AI preview.
	 */
	public void setAiPreview(AiPreview selectedAi)
	{	this.aiPreview = selectedAi;
		
		// sizes
		reinit(LINES,COL_GROUPS,COL_SUBS);
		
		// icons
		List<String> keys = new ArrayList<String>();
		if(showName)
			keys.add(GuiKeys.COMMON_AI_NAME);
		if(showPack)
			keys.add(GuiKeys.COMMON_AI_PACK);
		if(showAuthor)
		{	if(selectedAi==null)
				keys.add(GuiKeys.COMMON_AI_AUTHOR);
			else
			{	for(int i=0;i<selectedAi.getAuthors().size();i++)
					keys.add(GuiKeys.COMMON_AI_AUTHOR);
			}
		}
		
		if(selectedAi!=null)
		{	// text
			List<String> values = new ArrayList<String>();
			if(showName)
				values.add(selectedAi.getFolder());
			if(showPack)
				values.add(selectedAi.getPack());
			if(showAuthor)
			{	for(int i=0;i<selectedAi.getAuthors().size();i++)
					values.add(selectedAi.getAuthors().get(i));
			}			
			
			// content
			for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = values.get(line);
					String tooltip = text;
					setLabelText(line,colSub,text,tooltip);
					Color fg = GuiColorTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,0,fg);
					Color bg;
					if(line>0)
						bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					else
						bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
			}
		}
		else
		{	for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = null;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys.get(line)+GuiKeys.TOOLTIP);
					setLabelText(line,colSub,text,tooltip);
					Color bg;
					if(line>0)
						bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
					else
						bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
			}
		}

		// col widths
		int iconWidth = getHeaderHeight();
		setColSubMinWidth(0,iconWidth);
		setColSubPrefWidth(0,iconWidth);
		setColSubMaxWidth(0,iconWidth);
		int maxWidth = getDataWidth()-(COL_SUBS-1)*GuiSizeTools.subPanelMargin-iconWidth;
		setColSubMinWidth(1,maxWidth);
		setColSubPrefWidth(1,maxWidth);
		setColSubMaxWidth(1,maxWidth);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether to show the AI name */
	private boolean showName = true;
	/** Whether to show the AI pack */
	private boolean showPack = true;
	/** Whether to show the AI author */
	private boolean showAuthor = true;

	/**
	 * Enable/disable the display of the AI name.
	 * 
	 * @param showName
	 * 		{@code true} to show the AI name.
	 */
	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	/**
	 * Enable/disable the display of the AI author.
	 * 
	 * @param showAuthor
	 * 		{@code true} to show the AI author.
	 */
	public void setShowAuthor(boolean showAuthor)
	{	this.showAuthor = showAuthor;
	}

	/**
	 * Enable/disable the display of the AI pack.
	 * 
	 * @param showPack
	 * 		{@code true} to show the AI name.
	 */
	public void setShowPack(boolean showPack)
	{	this.showPack = showPack;
	}
}
