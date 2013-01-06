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
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.preview.LevelPreview;
import org.totalboumboum.engine.content.sprite.SpritePreview;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.images.ImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AvailableItemsSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 4;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 5;

	public AvailableItemsSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.TITLE,LINES,COL_GROUPS,COL_SUBS,false);
		// title
		String titleKey = GuiKeys.COMMON_ITEMS_AVAILABLE_TITLE;
		setTitleKey(titleKey,true);
	}
	
	/////////////////////////////////////////////////////////////////
	// LEVEL PREVIEW	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelPreview levelPreview;	
	private HollowLevel hollowLevel;	
	
	public void setLevel(LevelPreview levelPreview, HollowLevel hollowLevel)
	{	this.levelPreview = levelPreview;
		this.hollowLevel = hollowLevel;
		int lines = LINES;
		int colGroups = COL_GROUPS;
		
		// col widths
		int lineHeight = getLineHeight();
		int iconWidth = lineHeight;
		int textWidth = (getDataWidth() - (iconWidth*colGroups+GuiSizeTools.subPanelMargin*(COL_SUBS*colGroups-1)))/colGroups;

		if(levelPreview!=null)
		{	if(levelPreview.getItemsetPreview().size()>lines*colGroups)
			{	lines = 5;
				colGroups = 6;
			}
			reinit(lines,colGroups,COL_SUBS);
			
			// data
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(0);
			ItemsetPreview itemsetPreview = levelPreview.getItemsetPreview();
			HashMap<String,Integer> itemList = hollowLevel.getItemCount();
			Iterator<Entry<String,SpritePreview>> i = itemsetPreview.getItemPreviews().entrySet().iterator();
			int line = 0;
			int colGroup = 0;
			while(i.hasNext() && (line<LINES || colGroup<COL_GROUPS) )
			{	// init
				Entry<String,SpritePreview> temp = i.next();
				String itemName = temp.getKey();
				SpritePreview spritePreview = temp.getValue();
				String name = spritePreview.getName();
				BufferedImage image = spritePreview.getImage(null);
				int number = 0;
				if(itemList.containsKey(itemName))
					number = itemList.get(itemName);
				String tooltip;
				tooltip = name+": "+number;				
				if(number==0)
				{	image = ImageTools.getGreyScale(image);
				}
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
//		setColSubMaxWidth(1,Integer.MAX_VALUE);			
	}

	public LevelPreview getLevelPreview()
	{	return levelPreview;	
	}

	public HollowLevel getHollowLevel()
	{	return hollowLevel;	
	}
}
