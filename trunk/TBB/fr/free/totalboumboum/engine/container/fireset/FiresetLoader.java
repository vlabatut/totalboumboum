package fr.free.totalboumboum.engine.container.fireset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.sprite.fire.FireFactory;
import fr.free.totalboumboum.engine.content.sprite.fire.FireFactoryLoader;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class FiresetLoader
{	
	public static Fireset loadFireset(String folderPath, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = folderPath;
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_FIRESET+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_FIRESET+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Fireset result = loadFiresetElement(individualFolder,root,level);
		return result;
	}
	
    private static Fireset loadFiresetElement(String folder, Element root, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	Fireset result = new Fireset();
    	ArrayList<Element> elts = XmlTools.getChildElements(root, XmlTools.ELT_FIRE);
    	Iterator<Element> i = elts.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
			String name = temp.getAttribute(XmlTools.ATT_NAME).trim();
    		FireFactory fireFactory = loadFireElement(folder,temp,level);
    		result.addFireFactory(name, fireFactory);
    	}
    	return result;
    }
    
    private static FireFactory loadFireElement(String folder, Element root, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = folder;
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
		{	String f = root.getAttribute(XmlTools.ATT_FOLDER).trim();
			individualFolder = folder+File.separator+f;
		}    	
    	//
    	FireFactory result = FireFactoryLoader.loadFireFactory(individualFolder,level);
    	return result;
    }
}
