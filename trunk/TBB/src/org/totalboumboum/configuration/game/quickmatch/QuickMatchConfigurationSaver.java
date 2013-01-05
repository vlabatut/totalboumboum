package org.totalboumboum.configuration.game.quickmatch;

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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Comment;
import org.jdom.Element;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.configuration.profiles.ProfilesSelectionSaver;
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
public class QuickMatchConfigurationSaver
{	
	public static void saveQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGameQuickMatchElement(quickMatchConfiguration);	
		
		// save file
		String engineFile = FilePaths.getConfigurationPath()+File.separator+FileNames.FILE_GAME_QUICKMATCH+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_GAME_QUICKMATCH+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGameQuickMatchElement(QuickMatchConfiguration quickMatchConfiguration)
	{	Element result = new Element(XmlNames.GAME_QUICKMATCH); 
		
		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);
	
		// options
		Element optionsElement = saveQuickMatchOptionsElement(quickMatchConfiguration);
		result.addContent(optionsElement);

		// settings
		Element settingsElement = saveQuickMatchSettingsElement(quickMatchConfiguration);
		result.addContent(settingsElement);

		// players
		Element playersElement = new Element(XmlNames.PLAYERS);
		ProfilesSelection quickMatchSelected = quickMatchConfiguration.getProfilesSelection();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,quickMatchSelected);
		result.addContent(playersElement);

		// levels
		Element levelsElement = new Element(XmlNames.LEVELS);
		LevelsSelection levelsSelection = quickMatchConfiguration.getLevelsSelection();
		LevelsSelectionSaver.saveLevelsSelection(levelsElement,levelsSelection);
		result.addContent(levelsElement);

		return result;
	}
	
	private static Element saveQuickMatchOptionsElement(QuickMatchConfiguration quickMatchConfiguration)
	{	Element result = new Element(XmlNames.OPTIONS);
		
		// use last players
		String useLastPlayers = Boolean.toString(quickMatchConfiguration.getUseLastPlayers());
		result.setAttribute(XmlNames.USE_LAST_PLAYERS,useLastPlayers);

		// use last levels
		String useLastLevels = Boolean.toString(quickMatchConfiguration.getUseLastLevels());
		result.setAttribute(XmlNames.USE_LAST_LEVELS,useLastLevels);
		
		// use last settings
		String useLastSettings = Boolean.toString(quickMatchConfiguration.getUseLastSettings());
		result.setAttribute(XmlNames.USE_LAST_SETTINGS,useLastSettings);
		
		return result;
	}
	
	private static Element saveQuickMatchSettingsElement(QuickMatchConfiguration quickMatchConfiguration)
	{	Element result = new Element(XmlNames.SETTINGS);

		// levels
		{	Element levelsElement = new Element(XmlNames.LEVELS);
			result.addContent(levelsElement);
			// random order
			String randomOrder = Boolean.toString(quickMatchConfiguration.getLevelsRandomOrder());
			levelsElement.setAttribute(XmlNames.RANDOM_ORDER,randomOrder);
		}
		// players
		{	Element playersElement = new Element(XmlNames.PLAYERS);
			result.addContent(playersElement);
			// random location
			String randomLocation = Boolean.toString(quickMatchConfiguration.getPlayersRandomLocation());
			playersElement.setAttribute(XmlNames.RANDOM_LOCATION,randomLocation);
		}
		// limits
		{	Element limitsElement = new Element(XmlNames.LIMITS);
			result.addContent(limitsElement);
			// limit points
			String points = Integer.toString(quickMatchConfiguration.getLimitPoints());
			limitsElement.setAttribute(XmlNames.POINTS,points);
			// limit rounds
			String rounds = Integer.toString(quickMatchConfiguration.getLimitRounds());
			limitsElement.setAttribute(XmlNames.ROUNDS,rounds);
			// limit time
			String time = Integer.toString(quickMatchConfiguration.getLimitTime());
			limitsElement.setAttribute(XmlNames.TIME,time);
		}
		// points
		{	Element pointsElement = new Element(XmlNames.POINTS);
			result.addContent(pointsElement);
			// share
			String share = Boolean.toString(quickMatchConfiguration.getPointsShare());
			pointsElement.setAttribute(XmlNames.SHARE,share);
			// draw
			String draw = quickMatchConfiguration.getPointsDraw().toString();
			pointsElement.setAttribute(XmlNames.DRAW,draw);
			// values
			List<Integer> values = quickMatchConfiguration.getPoints();
			for(int r=0;r<values.size();r++)
			{	Element valueElement = new Element(XmlNames.VALUE);
				pointsElement.addContent(valueElement);
				// limit rounds
				String rank = Integer.toString(r+1);
				valueElement.setAttribute(XmlNames.RANK,rank);
				// limit time
				String points = Integer.toString(values.get(r));
				valueElement.setAttribute(XmlNames.POINTS,points);
			}
		}
		// sudden death
		{	Element suddenDeathElement = new Element(XmlNames.SUDDEN_DEATH);
			result.addContent(suddenDeathElement);
			// disabled
			String disabled = Boolean.toString(quickMatchConfiguration.getSuddenDeathDisabled());
			suddenDeathElement.setAttribute(XmlNames.DISABLED,disabled);
		}

		return result;
	}
}
