package org.totalboumboum.gui.tools;

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

import java.awt.Color;
import java.util.List;

import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GuiStringTools
{
	/**
	 * processes the texts and tooltips for all controls,
	 * and returns the pixel size of the largest one.
	 * @param fontSize
	 * @param controlTexts
	 * @param controlTooltips
	 * @return
	 */
	public static int initControlsTexts(int fontSize, List<String> controlTexts, List<String> controlTooltips)
	{	int result = 0;
		// no control
		{	// text
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_PLAYERS_LIST_DATA_NO_CONTROLS);
			controlTexts.add(text);
			// tooltip
			String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_PLAYERS_LIST_DATA_NO_CONTROLS+GuiKeys.TOOLTIP);
			controlTooltips.add(tooltip);
			// width
			result = GuiTools.getPixelWidth(fontSize,text);
		}
		// control number X
		for(int index=1;index<=GameData.CONTROL_COUNT;index++)
		{	// text
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_PLAYERS_LIST_DATA_CONTROLS)+index;
			controlTexts.add(text);
			// tooltip
			String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_PLAYERS_LIST_DATA_CONTROLS+GuiKeys.TOOLTIP)+" "+index;
			controlTooltips.add(tooltip);
			// width
			int temp = GuiTools.getPixelWidth(fontSize,text);
			if(temp>result)
				result = temp;
		}
		//
		return result;
	}
	
	/**
	 * processes the texts and tooltips for all colors,
	 * and returns the pixel size of the largest one.
	 * @param fontSize
	 * @param colorTexts
	 * @param colorTooltips
	 * @param colorBackgrounds
	 * @return
	 */
	public static int initColorTexts(int fontSize, List<String> colorTexts, List<String> colorTooltips, List<Color> colorBackgrounds)
	{	int result = 0;
		for(PredefinedColor color : PredefinedColor.values())
		{	// text
			String colorKey = color.toString();
			colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
			colorKey = GuiKeys.COMMON_COLOR+colorKey;
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey); 
			String tooltip = text;
			Color clr = color.getColor();
			int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
			Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
			//
			colorTexts.add(text);
			colorTooltips.add(tooltip);
			colorBackgrounds.add(bg);
			// width
			int temp = GuiTools.getPixelWidth(fontSize,text);
			if(temp>result)
				result = temp;
		}
		return result;
	}

}
