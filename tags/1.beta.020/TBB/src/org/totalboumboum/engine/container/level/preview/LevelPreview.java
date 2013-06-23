package org.totalboumboum.engine.container.level.preview;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import org.totalboumboum.engine.container.itemset.ItemsetPreview;
import org.totalboumboum.engine.container.level.info.LevelInfo;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LevelPreview implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// LEVEL INFO 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelInfo levelInfo;
	
	public LevelInfo getLevelInfo()
	{	return levelInfo;		
	}
	
	public void setLevelInfo(LevelInfo levelInfo)
	{	this.levelInfo = levelInfo;	
	}
	
	/////////////////////////////////////////////////////////////////
	// VISUAL PREVIEW	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage visualPreview = null;
	
	public BufferedImage getVisualPreview()
	{	return visualPreview;
	}
	
	public void setVisualPreview(BufferedImage visualPreview)
	{	this.visualPreview = visualPreview;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMSET PREVIEW	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ItemsetPreview itemsetPreview;

	public ItemsetPreview getItemsetPreview()
	{	return itemsetPreview;
	}
	public void setItemsetPreview(ItemsetPreview itemsetPreview)
	{	this.itemsetPreview = itemsetPreview;
	}

	/////////////////////////////////////////////////////////////////
	// INITIAL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,Integer> initialItems;

	public HashMap<String, Integer> getInitialItems()
	{	return initialItems;
	}
	public void setInitialItems(HashMap<String, Integer> initialItems)
	{	this.initialItems = initialItems;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Set<Integer> allowedPlayerNumbers;
	
	public Set<Integer> getAllowedPlayerNumbers()
	{	return allowedPlayerNumbers;    	
    }

    public void setAllowedPlayerNumbers(Set<Integer> allowedPlayerNumbers)
    {	this.allowedPlayerNumbers = allowedPlayerNumbers;    	
    }
}
