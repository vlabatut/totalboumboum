package org.totalboumboum.configuration.statistics;

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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class StatisticsConfigurationLoader
{	
	public static StatisticsConfiguration loadStatisticsConfiguration() throws ParserConfigurationException, SAXException, IOException
	{	StatisticsConfiguration result = new StatisticsConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadStatisticsElement(root,result);
		return result;
	}

	private static void loadStatisticsElement(Element root, StatisticsConfiguration result)
	{	Element element; 
		
		// include quick starts
		element = root.getChild(XmlNames.INCLUDE_QUICKSTARTS);
		loadIncludeQuickStartsElement(element,result);
		
		// include simulations
		element = root.getChild(XmlNames.INCLUDE_SIMULATIONS);
		loadIncludeSimulationsElement(element,result);
		
		// glicko-2
		element = root.getChild(XmlNames.GLICKO2);
		loadGlicko2Element(element,result);

		// launch time
		result.initLaunchTime();
		
		// regular launch
		Element regularLaunchElement = root.getChild(XmlNames.REGULAR_LAUNCH);
		loadRegularLaunchElement(regularLaunchElement,result);
		
		// quick launch
		Element quickLaunchElement = root.getChild(XmlNames.QUICK_LAUNCH);
		loadQuickLaunchElement(quickLaunchElement,result);
	}

	private static void loadIncludeQuickStartsElement(Element root, StatisticsConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean includeQuickStarts = Boolean.valueOf(value);
		result.setIncludeQuickStarts(includeQuickStarts);
	}

	private static void loadIncludeSimulationsElement(Element root, StatisticsConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean includeSimulations = Boolean.valueOf(value);
		result.setIncludeSimulations(includeSimulations);
	}

	private static void loadGlicko2Element(Element root, StatisticsConfiguration result)
	{	// default rating
		{	String value = root.getAttribute(XmlNames.DEFAULT_RATING).getValue().trim();
			int defaultRating = Integer.valueOf(value);
			result.setDefaultRating(defaultRating);
		}
		// default rating deviation
		{	String value = root.getAttribute(XmlNames.DEFAULT_RATING_DEVIATION).getValue().trim();
			int defaultRatingDeviation = Integer.valueOf(value);
			result.setDefaultRatingDeviation(defaultRatingDeviation);
		}
		// default rating volatility
		{	String value = root.getAttribute(XmlNames.DEFAULT_RATING_VOLATILITY).getValue().trim();
			float defaultRatingVolatility = Float.valueOf(value);
			result.setDefaultRatingVolatility(defaultRatingVolatility);
		}
		// games per period
		{	String value = root.getAttribute(XmlNames.GAMES_PER_PERIOD).getValue().trim();
			int gamesPerPeriod = Integer.parseInt(value);
			result.setGamesPerPeriod(gamesPerPeriod);
		}		
	}
	
	private static void loadRegularLaunchElement(Element root, StatisticsConfiguration result)
	{	// count
		String strCount = root.getAttribute(XmlNames.COUNT).getValue().trim();
		int count = Integer.parseInt(strCount);
		result.setRegularLaunchCount(count);
		
		// time
		String strTime = root.getAttribute(XmlNames.TIME).getValue().trim();
		int time = Integer.parseInt(strTime);
		result.setRegularLaunchTime(time);
	}
	
	private static void loadQuickLaunchElement(Element root, StatisticsConfiguration result)
	{	// count
		String strCount = root.getAttribute(XmlNames.COUNT).getValue().trim();
		int count = Integer.parseInt(strCount);
		result.setQuickLaunchCount(count);
		
		// time
		String strTime = root.getAttribute(XmlNames.TIME).getValue().trim();
		int time = Integer.parseInt(strTime);
		result.setQuickLaunchTime(time);
	}
}
