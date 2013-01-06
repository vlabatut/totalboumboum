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

import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.tools.GameData;

/**
 * Sets of fields and methods related
 * to general GUI management.
 * 
 * @author Vincent Labatut
 */
public class GuiMiscTools
{	
	/////////////////////////////////////////////////////////////////
	// STARTUP			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Startup message index */
	public static final int STARTUP_XML = 0;
	/** Startup message index */
	public static final int STARTUP_CONFIG = 1;
	/** Startup message index */
	public static final int STARTUP_GUI = 2;
	/** Startup message index */
	public static final int STARTUP_INIT = 3;
	/** Startup message index */
	public static final int STARTUP_STATS = 4;
	/** Startup message index */
	public static final int STARTUP_DONE = 5;
	/** Array of startup messages */
	public static final String STARTUP_MESSAGES[] = 
	{	"[Loading XML schemas]",
		"[Loading configuration]",
		"[Loading GUI]",
		"[Initializing GUI]",
		"[Loading statistics]",
		"[Done]"
	};
	/** Copyright message */
	public static final String STARTUP_LEGAL[] = 
	{	"Total Boum Boum version "+GameData.VERSION,
		new Character('\u00A9').toString()+" 2008-2013 Vincent Labatut",
		"Licensed under the GPL v2"
	};
	
	/////////////////////////////////////////////////////////////////
	// HELP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Help message */
	public static final String OPTION_HELP_MESSAGE = "In-line parameters allowed for this software:";
	/** Option index */
	public static final int OPTION_HELP = 0;
	/** Option index */
	public static final int OPTION_QUICK = 1;
	/** Option index */
	public static final int OPTION_WINDOW = 2;
	/** Options tags */
	public static final String OPTIONS[] = 
	{	"help",
		"quick",
		"window"
	};
	/** Option descriptions */
	public static final String OPTIONS_HELP[] = 
	{	"show this page (and does not launch the game)",
		"launch the game in quick mode, i.e. with a minimal graphical interface, and allows playing only one predefined round",
		"force the game to be displayed in a window, even if full screen is set in the game options"
	};

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the position of a swing component
	 * relatively to its swing container.
	 * 
	 * @param container
	 * 		The container of interest.
	 * @param component
	 * 		The component of interest.
	 * @return
	 * 		Index of the component in the container.
	 */
	public static int indexOfComponent(Container container, Component component)
	{	Component[] components = container.getComponents();
		List<Component> list = Arrays.asList(components);
		int result = list.indexOf(component);
		return result;
	}
}
