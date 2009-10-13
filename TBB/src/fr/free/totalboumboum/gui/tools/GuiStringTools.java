package fr.free.totalboumboum.gui.tools;

import java.awt.Color;
import java.util.ArrayList;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.game.GameConstants;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;

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
	public static int initControlsTexts(int fontSize, ArrayList<String> controlTexts, ArrayList<String> controlTooltips)
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
		for(int index=1;index<=GameConstants.CONTROL_COUNT;index++)
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
	public static int initColorTexts(int fontSize, ArrayList<String> colorTexts, ArrayList<String> colorTooltips, ArrayList<Color> colorBackgrounds)
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
