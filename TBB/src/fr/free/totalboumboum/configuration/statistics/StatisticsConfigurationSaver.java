package fr.free.totalboumboum.configuration.statistics;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class StatisticsConfigurationSaver
{	
	public static void saveStatisticsConfiguration(StatisticsConfiguration statisticsConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveStatisticsElement(statisticsConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_STATISTICS+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveStatisticsElement(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlTools.ELT_STATISTICS); 
		
		// include quick starts
		Element includeQuickStartsElement = saveIncludeQuickStartsElement(statisticsConfiguration);
		result.addContent(includeQuickStartsElement);
		
		// include simulations
		Element includeSimulationsElement = saveIncludeSimulationsElement(statisticsConfiguration);
		result.addContent(includeSimulationsElement);
		
		// glicko-2
		Element glicko2Element = saveGlicko2Element(statisticsConfiguration);
		result.addContent(glicko2Element);

		return result;
	}
	
	private static Element saveIncludeQuickStartsElement(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlTools.ELT_INCLUDE_QUICKSTARTS);
		String includeQuickStart = Boolean.toString(statisticsConfiguration.getIncludeQuickStarts());
		result.setAttribute(XmlTools.ATT_VALUE,includeQuickStart);
		return result;
	}

	private static Element saveIncludeSimulationsElement(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlTools.ELT_INCLUDE_SIMULATIONS);
		String includeSimulations = Boolean.toString(statisticsConfiguration.getIncludeSimulations());
		result.setAttribute(XmlTools.ATT_VALUE,includeSimulations);
		return result;
	}
	
	private static Element saveGlicko2Element(StatisticsConfiguration statisticsConfiguration)
	{	Element result = new Element(XmlTools.ELT_GLICKO2);
		
		// default rating
		String defaultRating = Integer.toString(statisticsConfiguration.getDefaultRating());
		result.setAttribute(XmlTools.ATT_DEFAULT_RATING,defaultRating);
		
		// default rating deviation
		String defaultRatingDeviation = Integer.toString(statisticsConfiguration.getDefaultRatingDeviation());
		result.setAttribute(XmlTools.ATT_DEFAULT_RATING_DEVIATION,defaultRatingDeviation);
		
		// default rating volatility
		String defaultRatingVolatility = Float.toString(statisticsConfiguration.getDefaultRatingVolatility());
		result.setAttribute(XmlTools.ATT_DEFAULT_RATING_VOLATILITY,defaultRatingVolatility);
		
		// games per period
		String gamesPerPeriod = Integer.toString(statisticsConfiguration.getGamesPerPeriod());
		result.setAttribute(XmlTools.ATT_GAMES_PER_PERIOD,gamesPerPeriod);
		
		return result;
	}
}
