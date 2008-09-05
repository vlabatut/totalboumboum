package fr.free.totalboumboum.data.profile;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class ProfileLoader
{	
	public static Profile loadProfile(String name) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	Profile result;
		String profilesFolder = FileTools.getProfilesPath();
		String individualFolder = profilesFolder+File.separator+name;
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_PROFILE+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PROFILE+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		result = loadProfileElement(root,individualFolder);
		return result;
	}
	
	private static Profile loadProfileElement(Element root,String individualFolder) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	Profile result = new Profile();
    	// general properties
    	Element general = XmlTools.getChildElement(root,XmlTools.ELT_GENERAL);
		loadGeneralElement(general,result);
		// sprite info
		Element character = XmlTools.getChildElement(root,XmlTools.ELT_CHARACTER);
		loadSpriteElement(character,result);
		// controls file
		String controlFile = individualFolder+File.separator+FileTools.FILE_CONTROLS+FileTools.EXTENSION_DATA;
		ControlSettings controlSettings = ControlSettingsLoader.loadControlSettings(controlFile);
		result.setControlSettings(controlSettings);
		//
		return result;
	}
    
    private static void loadGeneralElement(Element root, Profile result)
    {	// name
    	String name = root.getAttribute(XmlTools.ATT_NAME);
    	result.setName(name);
    	// artificial intelligence
    	String ai = null;
    	if(root.hasAttribute(XmlTools.ATT_AI))
	    	ai =  root.getAttribute(XmlTools.ATT_AI).trim();
    	result.setAi(ai);
    }
    
    private static void loadSpriteElement(Element root, Profile result)
    {	// packname
    	String spritePackname = root.getAttribute(XmlTools.ATT_PACKNAME);
    	result.setSpritePack(spritePackname);
    	// name
    	String spriteName = root.getAttribute(XmlTools.ATT_NAME);
    	result.setSpriteName(spriteName);
    	// color
    	String spriteColorStr = root.getAttribute(XmlTools.ATT_COLOR).trim().toUpperCase();
    	PredefinedColor spriteColor = PredefinedColor.valueOf(spriteColorStr);
    	result.setSpriteColor(spriteColor);
    }	        
}
