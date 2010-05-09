package fr.free.totalboumboum.configuration.game.quickmatch;

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
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.configuration.profile.ProfilesSelectionSaver;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class QuickMatchConfigurationSaver
{	
	public static void saveQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGameQuickMatchElement(quickMatchConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_GAME_QUICKMATCH+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME_QUICKMATCH+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGameQuickMatchElement(QuickMatchConfiguration quickMatchConfiguration)
	{	Element result = new Element(XmlTools.GAME_QUICKMATCH); 
		
		// options
		Element optionsElement = saveQuickMatchOptionsElement(quickMatchConfiguration);
		result.addContent(optionsElement);

		// settings
		Element settingsElement = saveQuickMatchSettingsElement(quickMatchConfiguration);
		result.addContent(settingsElement);

		// players
		Element playersElement = new Element(XmlTools.PLAYERS);
		ProfilesSelection quickMatchSelected = quickMatchConfiguration.getProfilesSelection();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,quickMatchSelected);
		result.addContent(playersElement);

		// levels
		Element levelsElement = new Element(XmlTools.LEVELS);
		LevelsSelection levelsSelection = quickMatchConfiguration.getLevelsSelection();
		LevelsSelectionSaver.saveLevelsSelection(levelsElement,levelsSelection);
		result.addContent(levelsElement);

		return result;
	}
	
	private static Element saveQuickMatchOptionsElement(QuickMatchConfiguration quickMatchConfiguration)
	{	Element result = new Element(XmlTools.OPTIONS);
		
		// use last players
		String useLastPlayers = Boolean.toString(quickMatchConfiguration.getUseLastPlayers());
		result.setAttribute(XmlTools.USE_LAST_PLAYERS,useLastPlayers);

		// use last levels
		String useLastLevels = Boolean.toString(quickMatchConfiguration.getUseLastLevels());
		result.setAttribute(XmlTools.USE_LAST_LEVELS,useLastLevels);
		
		// use last settings
		String useLastSettings = Boolean.toString(quickMatchConfiguration.getUseLastSettings());
		result.setAttribute(XmlTools.USE_LAST_SETTINGS,useLastSettings);
		
		return result;
	}
	
	private static Element saveQuickMatchSettingsElement(QuickMatchConfiguration quickMatchConfiguration)
	{	Element result = new Element(XmlTools.SETTINGS);

		// levels
		{	Element levelsElement = new Element(XmlTools.LEVELS);
			result.addContent(levelsElement);
			// random order
			String randomOrder = Boolean.toString(quickMatchConfiguration.getLevelsRandomOrder());
			levelsElement.setAttribute(XmlTools.RANDOM_ORDER,randomOrder);
		}
		// players
		{	Element playersElement = new Element(XmlTools.PLAYERS);
			result.addContent(playersElement);
			// random location
			String randomLocation = Boolean.toString(quickMatchConfiguration.getPlayersRandomLocation());
			playersElement.setAttribute(XmlTools.RANDOM_LOCATION,randomLocation);
		}
		// limits
		{	Element limitsElement = new Element(XmlTools.LIMITS);
			result.addContent(limitsElement);
			// limit points
			String points = Integer.toString(quickMatchConfiguration.getLimitPoints());
			limitsElement.setAttribute(XmlTools.POINTS,points);
			// limit rounds
			String rounds = Integer.toString(quickMatchConfiguration.getLimitRounds());
			limitsElement.setAttribute(XmlTools.ROUNDS,rounds);
			// limit time
			String time = Integer.toString(quickMatchConfiguration.getLimitTime());
			limitsElement.setAttribute(XmlTools.TIME,time);
		}
		// points
		{	Element pointsElement = new Element(XmlTools.POINTS);
			result.addContent(pointsElement);
			// share
			String share = Boolean.toString(quickMatchConfiguration.getPointsShare());
			pointsElement.setAttribute(XmlTools.SHARE,share);
			// draw
			String draw = quickMatchConfiguration.getPointsDraw().toString();
			pointsElement.setAttribute(XmlTools.DRAW,draw);
			// values
			ArrayList<Integer> values = quickMatchConfiguration.getPoints();
			for(int r=0;r<values.size();r++)
			{	Element valueElement = new Element(XmlTools.VALUE);
				pointsElement.addContent(valueElement);
				// limit rounds
				String rank = Integer.toString(r+1);
				valueElement.setAttribute(XmlTools.RANK,rank);
				// limit time
				String points = Integer.toString(values.get(r));
				valueElement.setAttribute(XmlTools.POINTS,points);
			}
		}

		return result;
	}
}
