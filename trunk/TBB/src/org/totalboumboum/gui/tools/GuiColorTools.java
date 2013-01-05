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

import java.awt.Color;

/**
 * Sets of fields and methods related
 * to color management in the GUI.
 * 
 * @author Vincent Labatut
 */
public class GuiColorTools
{	
	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Color of the messages during game loading */
	public final static Color COLOR_SPLASHSCREEN_TEXT = new Color(204,18,128);
	/** Color of the background of modal menus */
	public final static Color COLOR_DIALOG_BACKGROUND = new Color(0,0,0,175);
	/** Regular background color */
	public final static Color COLOR_COMMON_BACKGROUND = new Color(255,255,255,150);
	/** Regular foreground color */
	public final static Color COLOR_TITLE_FOREGROUND = Color.BLACK;
	/** Table lighter background color */
	public final static Color COLOR_TABLE_SELECTED_PALE_BACKGROUND = new Color(204,18,128,50);
	/** Table selection background color */
	public final static Color COLOR_TABLE_SELECTED_BACKGROUND = new Color(204,18,128,80);
	/** Table header selection background color */
	public final static Color COLOR_TABLE_SELECTED_DARK_BACKGROUND = new Color(204,18,128,130);
	/** Table regular background color */
	public final static Color COLOR_TABLE_REGULAR_BACKGROUND = new Color(0,0,0,80);
	/** Table neutral color */
	public final static Color COLOR_TABLE_NEUTRAL_BACKGROUND = new Color(0,0,0,20);
	/** Table regular foreground color */
	public final static Color COLOR_TABLE_REGULAR_FOREGROUND = Color.BLACK;
	/** Table header regular background color */
	public final static Color COLOR_TABLE_HEADER_BACKGROUND = new Color(0,0,0,130);
	/** Table header foreground color */
	public final static Color COLOR_TABLE_HEADER_FOREGROUND = Color.WHITE;
	/** Table alpha level 1 */
	public final static int ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1 = 80; //scores
	/** Table alpha level 2 */
	public final static int ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2 = 140; // rounds/matches
	/** Table alpha level 3 */
	public final static int ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3 = 200; //portrait/name/total/points
	/** Alpha change (used for mouseoverall) */
	public final static int ALPHA_DARKER_CHANGE = 55; 
	
	/**
	 * Change the alpha level of a color.
	 * 
	 * @param color
	 * 		Color to modify.
	 * @param delta
	 * 		Alpha change to apply.
	 * @return
	 * 		New, modified color.
	 */
	public static Color changeColorAlpha(Color color, int delta)
	{	Color result = color;
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int a = color.getAlpha();
		int newAlpha = a + delta;
		newAlpha = Math.max(0,newAlpha);
		newAlpha = Math.min(newAlpha,255);
		result = new Color(r,g,b,newAlpha);
		return result;
	}
	
/*	public static void changeColorMouseEntered(Component component)
	{	Color oldColor = component.getBackground();
		Color newColor = changeColorAlpha(oldColor,+54);
		component.setBackground(newColor);
	}
	
	public static void changeColorMouseExited(Component component)
	{	Color oldColor = component.getBackground();
		int a = oldColor.getAlpha();
		if(a>0 && a<255)
		{	Color newColor = changeColorAlpha(oldColor,-54);
			component.setBackground(newColor);
		}
	}
*/
}
