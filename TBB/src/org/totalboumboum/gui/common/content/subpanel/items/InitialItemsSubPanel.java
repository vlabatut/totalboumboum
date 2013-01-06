package org.totalboumboum.gui.common.content.subpanel.items;

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
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.totalboumboum.engine.container.itemset.ItemsetPreview;
import org.totalboumboum.engine.container.level.preview.LevelPreview;
import org.totalboumboum.engine.content.sprite.SpritePreview;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class InitialItemsSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 4;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 1;

	public InitialItemsSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.TITLE,LINES,COL_GROUPS,COL_SUBS,false);
		
		// title
		String titleKey = GuiKeys.COMMON_ITEMS_INITIAL_TITLE;
		setTitleKey(titleKey,true);
	
	}
	
	/////////////////////////////////////////////////////////////////
	// LEVEL PREVIEW	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelPreview levelPreview;	
	
	public void setLevelPreview(LevelPreview levelPreview)
	{	this.levelPreview = levelPreview;
		int lines = LINES;
		int colGroups = COL_GROUPS;
	
		// widths
		int lineHeight = getLineHeight();
		int iconWidth = lineHeight;
		int textWidth = (getDataWidth() - (iconWidth*colGroups+GuiSizeTools.subPanelMargin*(COL_SUBS*colGroups-1)))/colGroups;
		
		if(levelPreview!=null)
		{	if(levelPreview.getInitialItems().size()>lines*colGroups)
			{	lines = 8;
				colGroups = 4;
			}
			// table
			reinit(lines,colGroups,COL_SUBS);
			
			// data
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(0);
			ItemsetPreview itemsetPreview = levelPreview.getItemsetPreview();
			HashMap<String,Integer> initialItems = levelPreview.getInitialItems();
			Iterator<Entry<String,Integer>> i = initialItems.entrySet().iterator();
			int line = 0;
			int colGroup = 0;
			while(i.hasNext())
			{	// init
				Entry<String,Integer> temp = i.next();
				String itemName = temp.getKey();
				int number = temp.getValue();
				SpritePreview spritePreview = itemsetPreview.getItemPreview(itemName);
				String name = spritePreview.getName();
				BufferedImage image = spritePreview.getImage(null);
				String tooltip = name+": "+number;
				String value = Integer.toString(number);
				//
				int colSub = 0;
				{	setLabelIcon(line,colGroup,colSub,image,tooltip);
					Color fg = GuiColorTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,colGroup,colSub,fg);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colGroup,colSub,bg);
					colSub++;
				}
				{	String text = value;
					setLabelText(line,colGroup,colSub,text,tooltip);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colGroup,colSub,bg);
					colSub++;
				}
				line++;
				if(line==lines)
				{	line = 0;
					colGroup++;
				}
			}	
		}
		else
		{	//table
			reinit(lines,colGroups,COL_SUBS);
		}
		
		// widths
		setColSubMinWidth(0,lineHeight);
		setColSubPrefWidth(0,lineHeight);
		setColSubMaxWidth(0,lineHeight);
		setColSubMinWidth(1,textWidth);			
		setColSubPrefWidth(1,textWidth);			
		setColSubMaxWidth(1,textWidth);			
	}
	
	public LevelPreview getLevelPreview()
	{	return levelPreview;	
	}
}
