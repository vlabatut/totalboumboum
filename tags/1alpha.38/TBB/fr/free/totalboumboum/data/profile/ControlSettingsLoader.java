package fr.free.totalboumboum.data.profile;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class ControlSettingsLoader
{	
	public static ControlSettings loadControlSettings(String file) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	ControlSettings result;
		File dataFile = new File(file);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_CONTROLS+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		result = loadControlsElement(root);
		return result;
	}
	
    private static ControlSettings loadControlsElement(Element root) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	ControlSettings result = new ControlSettings();
    	List<Element> eventsList = root.getChildren(XmlTools.ELT_EVENT);
		for(int i=0;i<eventsList.size();i++)
			loadEventElement(eventsList.get(i),result);
		return result;
	}
    
    private static void loadEventElement(Element root, ControlSettings result) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
    {	// name
    	String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
    	// autofire
		String autofStr = root.getAttribute(XmlTools.ATT_AUTOFIRE).getValue();
		boolean autofire = Boolean.parseBoolean(autofStr);
		if(autofire)
			result.addAutofire(name);
		// on key
		Element onElt = root.getChild(XmlTools.ELT_ON);
		if(onElt!=null)
			loadKeyElement(onElt,name,true,result);
		// off key
		Element offElt = root.getChild(XmlTools.ELT_OFF);
		if(offElt!=null)
			loadKeyElement(offElt,name,false,result);
    }	
    
    /**
     * mode : true pour ON et false pour OFF
     */
    private static void loadKeyElement(Element root, String name, boolean mode, ControlSettings result) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
    {	// value
    	String cst = root.getAttribute(XmlTools.ATT_KEY).getValue();
    	int value = KeyEvent.class.getField(cst).getInt(KeyEvent.class);
    	if(mode)
    		result.addOnKey(value, name);
    	else
    		result.addOffKey(value, name);
    }	
}
