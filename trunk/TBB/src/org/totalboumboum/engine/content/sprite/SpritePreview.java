package org.totalboumboum.engine.content.sprite;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.Map.Entry;

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SpritePreview
{
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
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
	// IMAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<PredefinedColor,BufferedImage> coloredImages = new HashMap<PredefinedColor, BufferedImage>();
	private BufferedImage normalImage;

	public BufferedImage getImage(PredefinedColor color)
	{	BufferedImage result;
		if(color==null)
			result = normalImage;
		else
			result = coloredImages.get(color);
		return result;
	}
	public void setImage(PredefinedColor color, BufferedImage image)
	{	if(color==null)
			normalImage = image;
		else
			coloredImages.put(color,image);
	}

	public boolean hasColor(PredefinedColor color)
	{	return coloredImages.containsKey(color);		
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
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public SpritePreview copy()
	{	SpritePreview result = new SpritePreview();
		// images
		result.normalImage = normalImage;
		for(Entry<PredefinedColor,BufferedImage> e: coloredImages.entrySet())
			result.setImage(e.getKey(),e.getValue());
		// misc
		result.setName(name);
		result.setAuthor(author);
		result.setSource(source);
		result.setPack(pack);
		result.setFolder(folder);
		return result;
	}
}
