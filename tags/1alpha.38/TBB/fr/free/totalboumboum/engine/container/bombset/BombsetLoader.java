package fr.free.totalboumboum.engine.container.bombset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.sprite.bomb.BombFactory;
import fr.free.totalboumboum.engine.content.sprite.bomb.BombFactoryLoader;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class BombsetLoader
{	
	public static Bombset loadBombset(String folderPath, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	return loadBombset(folderPath,level,null);
	}
	public static Bombset loadBombset(String folderPath, Level level, PredefinedColor color) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		// loading components
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_BOMBSET+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_BOMBSET+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		Bombset result = loadBombsetElement(root,individualFolder,color,level);
		return result;
	}
	
    private static Bombset loadBombsetElement(Element root, String folder, PredefinedColor color, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	Bombset result = new Bombset();
    	String individualFolder = folder;
    	List<Element> bombs = root.getChildren(XmlTools.ELT_BOMB);
    	Iterator<Element> i = bombs.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadBombElement(temp,individualFolder,color,level,result);
    	}
    	return result;
    }
    
    private static void loadBombElement(Element root, String folder, PredefinedColor color, Level level, Bombset bombset) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    	// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			individualFolder = individualFolder+File.separator+attribute.getValue().trim();
		// required abilities
		ArrayList<AbstractAbility> requiredAbilities = AbilityLoader.loadAbilitiesElement(root,level);
		Iterator<AbstractAbility> i = requiredAbilities.iterator();
		ArrayList<StateAbility> abilities = new ArrayList<StateAbility>();
		while(i.hasNext())
		{	AbstractAbility ablt = i.next();
			if(ablt instanceof StateAbility)
				abilities.add((StateAbility)ablt);
		}
		//
		BombFactory bombFactory = BombFactoryLoader.loadBombFactory(individualFolder,level,name,color,bombset);
		bombset.addBombFactory(bombFactory,abilities);
    }
    
}
