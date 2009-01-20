package fr.free.totalboumboum.gui.common.content.subpanel.items;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.engine.container.itemset.ItemsetPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.content.sprite.SpritePreview;
import fr.free.totalboumboum.gui.common.structure.subpanel.EntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class InitialItemsSubPanel extends EntitledSubPanelTable
{	private static final long serialVersionUID = 1L;

	public InitialItemsSubPanel(int width, int height)
	{	super(width,height,1,1,1);
		
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
		
		// init
		int lines = 4;
		int colSubs = 2;
		int colGroups = 1;
		
		if(levelPreview!=null)
		{	if(levelPreview.getInitialItems().size()>lines*colGroups)
			{	lines = 8;
				colGroups = 4;
			}
			setNewTable(colGroups,colSubs,lines);
			int lineHeight = getLineHeight();
			setColSubMinWidth(0,lineHeight);
			setColSubPreferredWidth(0,lineHeight);
			setColSubMaxWidth(0,lineHeight);
			setColSubMaxWidth(1,Integer.MAX_VALUE);			
			
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
					Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,colGroup,colSub,fg);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colGroup,colSub,bg);
					colSub++;
				}
				{	String text = value;
					setLabelText(line,colGroup,colSub,text,tooltip);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
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
			setNewTable(colGroups,colSubs,lines);
			// widths
			int lineHeight = getLineHeight();
			setColSubMinWidth(0,lineHeight);
			setColSubPreferredWidth(0,lineHeight);
			setColSubMaxWidth(0,lineHeight);
			setColSubMaxWidth(1,Integer.MAX_VALUE);			
		}
	}
	
	public LevelPreview getLevelPreview()
	{	return levelPreview;	
	}
}
