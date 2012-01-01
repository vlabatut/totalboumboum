package org.totalboumboum.configuration.statistics;

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
public class StatisticsConfigurationSaver
{	
	public static void saveStatisticsConfiguration(StatisticsConfiguration statisticsConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveStatisticsElement(statisticsConfiguration);	
		// save file
		String engineFile = FilePaths.getConfigurationPath()+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_STATISTICS+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveStatisticsElement(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlNames.STATISTICS); 
		
		// include quick starts
		Element includeQuickStartsElement = saveIncludeQuickStartsElement(statisticsConfiguration);
		result.addContent(includeQuickStartsElement);
		
		// include simulations
		Element includeSimulationsElement = saveIncludeSimulationsElement(statisticsConfiguration);
		result.addContent(includeSimulationsElement);
		
		// glicko-2
		Element glicko2Element = saveGlicko2Element(statisticsConfiguration);
		result.addContent(glicko2Element);

		// regular launch
		Element regularLaunchElement = saveRegularLaunchELement(statisticsConfiguration);
		result.addContent(regularLaunchElement);
	
		// quick launch
		Element quickLaunchElement = saveQuickLaunchELement(statisticsConfiguration);
		result.addContent(quickLaunchElement);

		return result;
	}
	
	private static Element saveIncludeQuickStartsElement(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlNames.INCLUDE_QUICKSTARTS);
		String includeQuickStart = Boolean.toString(statisticsConfiguration.getIncludeQuickStarts());
		result.setAttribute(XmlNames.VALUE,includeQuickStart);
		return result;
	}

	private static Element saveIncludeSimulationsElement(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlNames.INCLUDE_SIMULATIONS);
		String includeSimulations = Boolean.toString(statisticsConfiguration.getIncludeSimulations());
		result.setAttribute(XmlNames.VALUE,includeSimulations);
		return result;
	}
	
	private static Element saveGlicko2Element(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlNames.GLICKO2);
		
		// default rating
		String defaultRating = Integer.toString(statisticsConfiguration.getDefaultRating());
		result.setAttribute(XmlNames.DEFAULT_RATING,defaultRating);
		
		// default rating deviation
		String defaultRatingDeviation = Integer.toString(statisticsConfiguration.getDefaultRatingDeviation());
		result.setAttribute(XmlNames.DEFAULT_RATING_DEVIATION,defaultRatingDeviation);
		
		// default rating volatility
		String defaultRatingVolatility = Float.toString(statisticsConfiguration.getDefaultRatingVolatility());
		result.setAttribute(XmlNames.DEFAULT_RATING_VOLATILITY,defaultRatingVolatility);
		
		// games per period
		String gamesPerPeriod = Integer.toString(statisticsConfiguration.getGamesPerPeriod());
		result.setAttribute(XmlNames.GAMES_PER_PERIOD,gamesPerPeriod);
		
		return result;
	}

	private static Element saveRegularLaunchELement(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlNames.REGULAR_LAUNCH); 
	
		// count
		String countStr = Long.toString(statisticsConfiguration.getRegularLaunchCount());
		result.setAttribute(XmlNames.COUNT,countStr);

		// time
		String timeStr = Long.toString(statisticsConfiguration.getRegularLaunchTime());
		result.setAttribute(XmlNames.TIME,timeStr);
		
		return result;
	}

	private static Element saveQuickLaunchELement(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlNames.QUICK_LAUNCH); 
	
		// count
		String countStr = Long.toString(statisticsConfiguration.getQuickLaunchCount());
		result.setAttribute(XmlNames.COUNT,countStr);

		// time
		String timeStr = Long.toString(statisticsConfiguration.getQuickLaunchTime());
		result.setAttribute(XmlNames.TIME,timeStr);
		
		return result;
	}
}
