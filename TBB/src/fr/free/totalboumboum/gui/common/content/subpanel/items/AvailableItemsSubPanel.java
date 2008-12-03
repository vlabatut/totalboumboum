package fr.free.totalboumboum.gui.common.content.subpanel.items;

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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.container.itemset.ItemsetPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.zone.Zone;
import fr.free.totalboumboum.engine.content.sprite.SpritePreview;
import fr.free.totalboumboum.gui.common.structure.subpanel.EntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class AvailableItemsSubPanel extends EntitledSubPanelTable
{	private static final long serialVersionUID = 1L;

	public AvailableItemsSubPanel(int width, int height)
	{	super(width,height,1,1,1);
		
		// title
		String titleKey = GuiKeys.COMMON_ITEMS_AVAILABLE_TITLE;
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
		int colGroups = 5;
		
		if(levelPreview!=null)
		{	if(levelPreview.getItemsetPreview().size()>lines*colGroups)
			{	lines = 5;
				colGroups = 6;
			}
			setNewTable(colGroups,colSubs,lines);
			int lineHeight = getTable().getLineHeight();
			int iconWidth = lineHeight;
			getTable().setColSubMinWidth(0,lineHeight);
			getTable().setColSubPreferredWidth(0,lineHeight);
			getTable().setColSubMaxWidth(0,lineHeight);
			int textWidth = (width - (iconWidth*colGroups+GuiTools.subPanelMargin*(colSubs*colGroups+1)))/colGroups;
			getTable().setColSubMinWidth(1,textWidth);
			getTable().setColSubPreferredWidth(1,textWidth);
			getTable().setColSubMaxWidth(1,textWidth);			
			
			// data
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(0);
			ItemsetPreview itemsetPreview = levelPreview.getItemsetPreview();
			Zone zone = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound().getHollowLevel().getZone();
			HashMap<String,Integer> itemList = zone.getItemCount();
			Iterator<Entry<String,SpritePreview>> i = itemsetPreview.getItemPreviews().entrySet().iterator();
			int line = 0;
			int colGroup = 0;
			while(i.hasNext())
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
				{	getTable().setLabelIcon(line,colGroup,colSub,image,tooltip);
					Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
					getTable().setLabelForeground(line,colGroup,colSub,fg);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					getTable().setLabelBackground(line,colGroup,colSub,bg);
					colSub++;
				}
				{	String text = value;
					getTable().setLabelText(line,colGroup,colSub,text,tooltip);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					getTable().setLabelBackground(line,colGroup,colSub,bg);
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
			int lineHeight = getTable().getLineHeight();
			getTable().setColSubMinWidth(0,lineHeight);
			getTable().setColSubPreferredWidth(0,lineHeight);
			getTable().setColSubMaxWidth(0,lineHeight);
			getTable().setColSubMaxWidth(1,Integer.MAX_VALUE);			
		}
	}

	public LevelPreview getLevelPreview()
	{	return levelPreview;	
	}
}
