package fr.free.totalboumboum.engine.container.level;

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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.itemset.ItemsetPreviewer;

public class LevelPreview
{
	
	/////////////////////////////////////////////////////////////////
	// MISC 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String author;
	private String title;
	private String source;
	
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

	private HashMap<String,BufferedImage> itemsetPreview;

	public HashMap<String, BufferedImage> getItemsetPreview()
	{	return itemsetPreview;
	}
	public void setItemsetPreview(HashMap<String, BufferedImage> itemsetPreview)
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
/*	
	/////////////////////////////////////////////////////////////////
	// LEVEL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	private HashMap<String,Integer> levelItems;

	public HashMap<String, Integer> getLevelItems()
	{	return levelItems;
	}
	public void setLevelItems(HashMap<String, Integer> levelItems)
	{	this.levelItems = levelItems;
	}
*/	
}
