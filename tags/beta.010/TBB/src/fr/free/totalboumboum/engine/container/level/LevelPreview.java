package fr.free.totalboumboum.engine.container.level;

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

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;

import fr.free.totalboumboum.engine.container.itemset.ItemsetPreview;

public class LevelPreview
{
	
	/////////////////////////////////////////////////////////////////
	// MISC 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String instanceName;
	private String themeName;
	private String author;
	private String title;
	private String source;
	private int visibleHeight;
	private int visibleWidth;
	
	public String getInstanceName()
	{	return instanceName;
	}
	public void setInstanceName(String instanceName)
	{	this.instanceName = instanceName;
	}

	public String getThemeName()
	{	return themeName;
	}
	public void setThemeName(String themeName)
	{	this.themeName = themeName;
	}

	public String getAuthor()
	{	return author;
	}
	public void setAuthor(String author)
	{	this.author = author;
	}
	
	public String getTitle()
	{	return title;
	}
	public void setTitle(String title)
	{	this.title = title;
	}
	
	public String getSource()
	{	return source;
	}
	public void setSource(String source)
	{	this.source = source;
	}
	
	public int getVisibleHeight()
	{	return visibleHeight;
	}
	public void setVisibleHeight(int visibleHeight)
	{	this.visibleHeight = visibleHeight;
	}
	
	public int getVisibleWidth()
	{	return visibleWidth;
	}
	public void setVisibleWidth(int visibleWidth)
	{	this.visibleWidth = visibleWidth;
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
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String folder;
	private String pack;
	
	public String getFolder()
	{	return folder;
	}
	
	public void setFolder(String folder)
	{	this.folder = folder;
	}
	
	public String getPack()
	{	return pack;
	}
	
	public void setPack(String pack)
	{	this.pack = pack;
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
