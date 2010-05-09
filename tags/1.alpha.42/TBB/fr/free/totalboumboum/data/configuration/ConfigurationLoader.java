package fr.free.totalboumboum.data.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.TournamentLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ConfigurationLoader
{	
	public static Configuration loadConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Configuration result = new Configuration();
		String individualFolder = FileTools.getSettingsPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_CONFIGURATION+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_CONFIGURATION+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadConfigurationElement(root,result);
		return result;
	}

	private static void loadConfigurationElement(Element root, Configuration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
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
		// tournament
		element = root.getChild(XmlTools.ELT_TOURNAMENT);
		if(element!=null)
			loadTournamentElement(element,result);
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
}
