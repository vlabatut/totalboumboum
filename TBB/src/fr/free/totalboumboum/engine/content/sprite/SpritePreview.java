package fr.free.totalboumboum.engine.content.sprite;

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
import java.util.TreeSet;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;

public class SpritePreview
{
	/////////////////////////////////////////////////////////////////
	// NAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	
	public String getName()
	{	return name;
	}
	
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String author;
	
	public String getAuthor()
	{	return author;
	}
	
	public void setAuthor(String author)
	{	this.author = author;
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String source;
	
	public String getSource()
	{	return source;
	}
	
	public void setSource(String source)
	{	this.source = source;
	}
	
	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage image;

	public BufferedImage getImage()
	{	return image;
	}
	public void setImage(BufferedImage image)
	{	this.image = image;
	}

	/////////////////////////////////////////////////////////////////
	// PACKAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String pack;
	
	public String getPack()
	{	return pack;
	}
	
	public void setPack(String pack)
	{	this.pack = pack;
	}
	
	/////////////////////////////////////////////////////////////////
	// FOLDER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String folder;
	
	public String getFolder()
	{	return folder;
	}
	
	public void setFolder(String folder)
	{	this.folder = folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TreeSet<PredefinedColor> colors = new TreeSet<PredefinedColor>();
	
	public void addColor(PredefinedColor color)
	{	colors.add(color);		
	}
	
	public boolean hasColor(PredefinedColor color)
	{	return colors.contains(color);		
	}
}
