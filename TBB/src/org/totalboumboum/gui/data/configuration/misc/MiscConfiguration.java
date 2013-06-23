package org.totalboumboum.gui.data.configuration.misc;

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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.totalboumboum.gui.data.language.Language;
import org.totalboumboum.tools.images.ImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class MiscConfiguration
{

	public MiscConfiguration copy()
	{	MiscConfiguration result = new MiscConfiguration(); 
		// language
		Language languageCopy = language.copy();
		result.setLanguage(languageName,languageCopy);
		// font
		Font fontCopy = font.deriveFont(10f);
		result.setFont(fontName,fontCopy);
		// background
		BufferedImage imageCopy = ImageTools.copyBufferedImage(background);
		result.setBackground(backgroundName,imageCopy); 
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// LANGUAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String languageName;
	private Language language;
		
	public void setLanguage(String name, Language language)
	{	this.language = language;
		this.languageName = name;
	}
	public Language getLanguage()
	{	return language;	
	}
	public String getLanguageName()
	{	return languageName;	
	}
		
	/////////////////////////////////////////////////////////////////
	// FONT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Font font;
	private String fontName;
	
	public Font getFont()
	{	return font;	
	}
	public String getFontName()
	{	return fontName;
	}
	public void setFont(String name, Font font)
	{	this.font = font;
		this.fontName = name;
	}

	/////////////////////////////////////////////////////////////////
	// BACKGROUND		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage background;
	private String backgroundName;
	private BufferedImage darkBackground;
	
	public void setBackground(String name, BufferedImage background)
	{	// normal image
		backgroundName = name;
		this.background = background;
		// dark image		
		BufferedImage darkImage = ImageTools.copyBufferedImage(background);
		Graphics2D g = (Graphics2D)darkImage.getGraphics();
		g.setComposite(AlphaComposite.Clear);
		g.setPaintMode();
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0,0,darkImage.getWidth(),darkImage.getHeight());
		g.dispose();
		darkBackground = darkImage;
	}
	public String getBackgroundName()
	{	return backgroundName;	
	}
	public BufferedImage getBackground()
	{	return background;	
	}
	public BufferedImage getDarkBackground()
	{	return darkBackground;	
	}
}
