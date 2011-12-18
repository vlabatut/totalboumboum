package fr.free.totalboumboum.data.configuration;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.limit.LimitConfrontation;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsTotal;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundLoader;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.TournamentLoader;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ConfigurationLoader
{	
	public static Configuration quickloadConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	return loadConfiguration(true);
	}
	public static Configuration loadConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	return loadConfiguration(false);
	}
	private static Configuration loadConfiguration(boolean quickStart) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Configuration result = new Configuration();
		String individualFolder = FileTools.getSettingsPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_CONFIGURATION+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_CONFIGURATION+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadConfigurationElement(root,result,quickStart);
		return result;
	}

	private static void loadConfigurationElement(Element root, Configuration result, boolean quickStart) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Element element; 
		// engine
		element = root.getChild(XmlTools.ELT_FPS);
		loadFpsElement(element,result);
		element = root.getChild(XmlTools.ELT_SPEED);
		loadSpeedElement(element,result);
		// display
		element = root.getChild(XmlTools.ELT_SMOOTH_GRAPHICS);
		loadSmoothGraphicsElement(element,result);
		// panel
		element = root.getChild(XmlTools.ELT_PANEL_DIMENSION);
		loadPanelDimensionElement(element,result);
		// profiles
		element = root.getChild(XmlTools.ELT_PROFILES);
		loadProfilesElement(element,result);
		// round for quick start
		element = root.getChild(XmlTools.ELT_QUICKSTART);
		if(quickStart && element!=null)
			loadQuickstartElement(element,result);
		else
		{	// last tournament
			element = root.getChild(XmlTools.ELT_TOURNAMENT);
			if(element!=null)
				loadTournamentElement(element,result);
		}
	}
	
	private static void loadFpsElement(Element root, Configuration result)
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		int fps = Integer.valueOf(value);
		result.setFps(fps);
	}
	
	private static void loadSpeedElement(Element root, Configuration result)
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		float speedCoeff = Float.valueOf(value);
		result.setSpeedCoeff(speedCoeff);
	}
	
	private static void loadSmoothGraphicsElement(Element root, Configuration result)
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		boolean smoothGraphics = Boolean.valueOf(value);
		result.setSmoothGraphics(smoothGraphics);
	}
	
	private static void loadPanelDimensionElement(Element root, Configuration result)
	{	String valueH = root.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		int height = Integer.valueOf(valueH);
		String valueW = root.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		int width = Integer.valueOf(valueW);
		result.setPanelDimension(width, height);
	}
	
	private static void loadProfilesElement(Element root, Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	List<Element> elements = root.getChildren(XmlTools.ELT_PROFILE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadProfileElement(temp,result);
		}
	}
	
	private static void loadProfileElement(Element root, Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
//		Profile profile = ProfileLoader.loadProfile(value);			
//		result.addProfile(profile);
result.addProfile(value);	
	}

	private static void loadTournamentElement(Element root, Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		AbstractTournament tournament = TournamentLoader.loadTournamentFromName(value,result);			
		result.setTournament(tournament);
	}

	private static void loadQuickstartElement(Element root, Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// single tournament
		SingleTournament tournament = new SingleTournament(result);
		// one round match
		Match match = new Match(tournament);
		{	// notes
			ArrayList<String> notes = new ArrayList<String>();
			notes.add("auto-generated notes");
			match.setNotes(notes);
		}
		{	// limits
			Limits<MatchLimit> limits = new Limits<MatchLimit>();
			MatchLimit limit = new LimitConfrontation(1);
			limits.addLimit(limit);
		}
		{	// points processor
			PointsProcessor pointProcessor = new PointsTotal();
			match.setPointProcessor(pointProcessor);
		}
		tournament.setMatch(match);
		// round
		String name = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		Round round = RoundLoader.loadRoundFromName(name,match);
		match.addRound(round);
		// result
		result.setTournament(tournament);
	}
}