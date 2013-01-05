package org.totalboumboum.gui.common.content.subpanel.sprite;

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.content.sprite.SpritePreview;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * This class displays the various details
 * decribing some sprite (author, name, etc.).
 * 
 * @author Vincent Labatut
 */
public class SpriteInfoSubPanel extends TableSubPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Number of lines in the panel */ 
	private static final int LINES = 10;
	/** Number of columns in each column group */
	private static final int COL_SUBS = 2;
	/** Number of column groups */
	private static final int COL_GROUPS = 1;
	
	/**
	 * Builds a standard sprite info panel.
	 * 
	 * @param width
	 * 		Width in pixels.
	 * @param height
	 * 		Height in pixels.
	 */
	public SpriteInfoSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,COL_GROUPS,COL_SUBS,true);
		setSpritePreview(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The concerned sprite */
	private SpritePreview spritePreview;

	/**
	 * Returns the sprite currently displayed.
	 * 
	 * @return
	 * 		The sprite, or {@code null} for no sprite.
	 */
	public SpritePreview getSpritePreview()
	{	return spritePreview;	
	}
	
	/**
	 * Change the currently displayed sprite.
	 * 
	 * @param spritePreview
	 * 		New sprite to display.
	 */
	public void setSpritePreview(SpritePreview spritePreview)
	{	this.spritePreview = spritePreview;
		
		// sizes
		reinit(LINES,COL_GROUPS,COL_SUBS);
		
		// icons
		List<String> keys = new ArrayList<String>();
		if(showName)
			keys.add(GuiKeys.COMMON_SPRITE_NAME);
		if(showPack)
			keys.add(GuiKeys.COMMON_SPRITE_PACK);
		if(showAuthor)
			keys.add(GuiKeys.COMMON_SPRITE_AUTHOR);
		if(showSource)
			keys.add(GuiKeys.COMMON_SPRITE_SOURCE);
		
		if(spritePreview!=null)
		{	// text
			List<String> values = new ArrayList<String>();
			if(showName)
				values.add(spritePreview.getName());
			if(showPack)
				values.add(spritePreview.getPack());
			if(showAuthor)
				values.add(spritePreview.getAuthor());
			if(showSource)
				values.add(spritePreview.getSource());
			
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
		int maxWidth = getDataWidth()-(COL_SUBS-1)*GuiTools.subPanelMargin-iconWidth;
		setColSubMinWidth(1,maxWidth);
		setColSubPrefWidth(1,maxWidth);
		setColSubMaxWidth(1,maxWidth);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the sprite name should be displayed */
	private boolean showName = true;
	/** Whether the sprite pack should be displayed */
	private boolean showPack = true;
	/** Whether the sprite author should be displayed */
	private boolean showAuthor = true;
	/** Whether the sprite source should be displayed */
	private boolean showSource = true;

	/**
	 * Changes the flag controling the
	 * name display.
	 * 
	 * @param showName
	 * 		Whether the sprite name should be displayed.
	 */
	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	/**
	 * Changes the flag controling the
	 * pack name display.
	 * 
	 * @param showPack
	 * 		Whether the sprite pack name should be displayed.
	 */
	public void setShowPack(boolean showPack)
	{	this.showPack = showPack;
	}

	/**
	 * Changes the flag controling the
	 * author display.
	 * 
	 * @param showAuthor
	 * 		Whether the sprite author should be displayed.
	 */
	public void setShowAuthor(boolean showAuthor)
	{	this.showAuthor = showAuthor;
	}

	/**
	 * Changes the flag controling the
	 * source display.
	 * 
	 * @param showSource
	 * 		Whether the sprite source should be displayed.
	 */
	public void setShowSource(boolean showSource)
	{	this.showSource = showSource;
	}
}
