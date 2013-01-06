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

import java.awt.Dimension;

import org.totalboumboum.configuration.Configuration;

/**
 * Sets of fields and methods related
 * to size management in the GUI.
 * 
 * @author Vincent Labatut
 */
public class GuiSizeTools
{	
	/**
	 * Initializes all sizes used to define
	 * GUI components.
	 */
	public static void init()
	{	// panel
		Dimension panelDimension = Configuration.getVideoConfiguration().getPanelDimension();
		int width = panelDimension.width;
		int height = panelDimension.height;
				
		// margins
		panelMargin = (int)(width*PANEL_MARGIN_RATIO);
		subPanelMargin = (int)(height*SUBPANEL_MARGIN_RATIO);
		
		// titles
		subPanelTitleHeight = (int)(panelMargin*SUBPANEL_TITLE_RATIO);
		
		// buttons
		buttonTextHeight = (int)(height*BUTTON_TEXT_HEIGHT_RATIO);
		buttonTextWidth = (int)(width*BUTTON_TEXT_WIDTH_RATIO);
//		buttonIconSize = (int)(height*BUTTON_ICON_SIZE_RATIO);
		buttonHorizontalSpace = (int)(width*BUTTON_HORIZONTAL_SPACE_RATIO);
		buttonVerticalSpace = (int)(height*BUTTON_VERTICAL_SPACE_RATIO);
	}
	
	/////////////////////////////////////////////////////////////////
	// MARGINS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ratio used to process the margin between two panels */
	private final static float PANEL_MARGIN_RATIO = 0.025f; 
	/** Margin between the components of a frame */
	public static int panelMargin;
	/** Ratio used to process the margin between two subpanels */
	private final static float SUBPANEL_MARGIN_RATIO = 0.005f;
	/** Margin between the components of a panel */
	public static int subPanelMargin;

	/////////////////////////////////////////////////////////////////
	// TITLES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ratio used to process the subpanel title height relatively to panel margin */
	private final static float SUBPANEL_TITLE_RATIO = 1.5f;
	/** Height of a subpanel title bar relatively to the height of a panel title */
	public static int subPanelTitleHeight;
	/** Ratio used to the process header height relatively to line height */
	public final static float TABLE_HEADER_RATIO = 1.2f;

	/////////////////////////////////////////////////////////////////
	// PANEL SPLIT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ratio used when vertically splitting a frame */
	public final static float VERTICAL_SPLIT_RATIO = 0.25f;
	/** Ratio used when horizontally splitting a frame */
	public final static float HORIZONTAL_SPLIT_RATIO = 0.07f;

	/////////////////////////////////////////////////////////////////
	// MODAL DIALOG		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ratio used to process the size of modal dialogs */
	public final static float MODAL_DIALOG_RATIO = 0.4f;
	
	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ratio used to process the height of a button relatively to the panel height */
	private final static float BUTTON_TEXT_HEIGHT_RATIO = 0.05f;
	/** Height of a button */
	public static int buttonTextHeight;
	/** Ratio used to process the width of a button relatively to the panel width */
	private final static float BUTTON_TEXT_WIDTH_RATIO = 0.33f;
	/** Width of a button */
	public static int buttonTextWidth;
	/** Ratio used to process the height of a button icon relatively to the panel height */
//	private final static float BUTTON_ICON_SIZE_RATIO = 0.07f;
	/** Height of a button icon */
//	public static int buttonIconSize;
	/** Ratio used to process the space between buttons relatively to the panel width */
	private final static float BUTTON_HORIZONTAL_SPACE_RATIO = 0.025f; //
	/** Space between buttons relatively to the panel width */
	public static int buttonHorizontalSpace;
	/** Ratio used to process the space between buttons relatively to the panel height */
	private final static float BUTTON_VERTICAL_SPACE_RATIO = 0.025f;
	/** Space between buttons relatively to the panel height */
	public static int buttonVerticalSpace;
	/** Ratio used to process the margin between buttons */
	public final static float BUTTON_ICON_MARGIN_RATIO = 0.9f;
}
