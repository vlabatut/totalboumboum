package org.totalboumboum.gui.tools;

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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.gui.data.configuration.GuiConfiguration;

/**
 * Sets of fields and methods related
 * to GUI management.
 * 
 * @author Vincent Labatut
 */
public class GuiFontTools
{	
	/////////////////////////////////////////////////////////////////
	// FONTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object necessary to perform font calculations */
	private static Graphics graphics;
	/** Font height relatively to the containing label (or component) */
	public final static float FONT_RATIO = 0.8f; 
	/** Font height relatively to the container title font */
	public final static float FONT_TEXT_RATIO = 0.75f;  

	/**
	 * Returns the graphical object necessary 
	 * to perform font calculations.
	 * 
	 * @return
	 * 		Current {@code Graphics} object.
	 */
	public static Graphics getGraphics()
	{	return graphics;	
	}
	
	/**
	 * Initializes all fonts required
	 * by the GUI.
	 */
	public static void init()
	{	BufferedImage img = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
		graphics = img.getGraphics();			
	}
	
	/**
	 * Processes the maximal font size for the specified height limit,
	 * whatever the displayed text will be.
	 * 
	 * @param limit
	 * 		A height in pixels.
	 * @return
	 * 		The corresponding font size.
	 */
	public static int getFontSize(double limit)
	{	int result = 0;
		int fheight;
		do
		{	result++;
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)result);
			graphics.setFont(font);
			FontMetrics metrics = graphics.getFontMetrics(font);
			fheight = metrics.getHeight();
		}
		while(fheight<limit);
		return result;
	}

	/**
	 * Processes the maximal font size for the specified width 
	 * and height limits, and given text.
	 * 
	 * @param width
	 * 		Width in pixels.
	 * @param height
	 * 		Height in pixels.
	 * @param text
	 * 		Text to be displayed.
	 * @return
	 * 		Appropriate font size.
	 */
	public static int getFontSize(double width, double height, String text)
	{	int result = 0;
		int fheight,fwidth;
		do
		{	result++;
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)result);
			graphics.setFont(font);
			FontMetrics metrics = graphics.getFontMetrics(font);
			fheight = metrics.getHeight();
			Rectangle2D bounds = metrics.getStringBounds(text,graphics);
			fwidth = (int)bounds.getWidth();
		}
		while(fheight<height && fwidth<width);
		return result-1;
	}
	
	/**
	 * Processes the pixel height corresponding 
	 * to the specified font size.
	 *  
	 * @param fontSize
	 * 		Font size of interest.
	 * @return
	 * 		Corresponding height in pixels.
	 */
	public static int getPixelHeight(float fontSize)
	{	int result;
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
		graphics.setFont(font);
		FontMetrics metrics = graphics.getFontMetrics(font);
		result = (int)(metrics.getHeight()*1.2);
		return result;
	}
	
	/**
	 * Processes the pixel width corresponding to 
	 * the specified font size.
	 * 
	 * @param fontSize
	 * 		Font size of interest.
	 * @param text
	 * 		Text of interest.
	 * @return
	 * 		Corresponding width in pixels.
	 */
	public static int getPixelWidth(float fontSize, String text)
	{	int result;
		fontSize = fontSize + 6;
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
		graphics.setFont(font);
		FontMetrics metrics = graphics.getFontMetrics(font);
		Rectangle2D bounds = metrics.getStringBounds(text,graphics);
		result = (int)bounds.getWidth();
		return result;
	}
	
	/**
	 * Processes the maximal font size fot the 
	 * given dimensions and the set of texts.
	 * 
	 * @param width
	 * 		Width of interest.
	 * @param height
	 * 		Height of interest.
	 * @param texts
	 * 		Texts of interest.
	 * @return
	 * 		Appropriate font size.
	 */
	public static int getOptimalFontSize(double width, double height, List<String> texts)
	{	int result;
		Iterator<String> it = texts.iterator();
		int longest = 0;
		String longestString = null;
		while(it.hasNext())
		{	String text = it.next();
			int length = getPixelWidth(10,text);
			if(length>longest)
			{	longest = length;
				longestString = text;
			}
		}
		result = getFontSize(width,height,longestString);
		return result;
	}

	/**
	 * Processes the maximal width for the given 
	 * list of texts, relatively to the given font size.
	 * 
	 * @param fontSize
	 * 		Font size of interest.
	 * @param texts
	 * 		Texts of interest.
	 * @return
	 * 		Appropriate width.
	 */
	public static int getMaximalWidth(float fontSize, List<String> texts)
	{	int result = 0;
		Iterator<String> it = texts.iterator();
		while(it.hasNext())
		{	String text = it.next();
			int length = getPixelWidth(10,text);
			if(length>result)
				result = length;
		}
		return result;
	}
}
