package fr.free.totalboumboum.engine.content.feature.explosion;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.fireset.Fireset;
import fr.free.totalboumboum.engine.container.fireset.FiresetLoader;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class ExplosionLoader
{	
	public static Explosion loadExplosion(String pathFolder, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = pathFolder;
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_EXPLOSION+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_EXPLOSION+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Explosion result = loadExplosionElement(root,level);
		return result;
	}
	
    private static Explosion loadExplosionElement(Element root, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	Explosion result = new Explosion();
    	// fire
    	Element elt = XmlTools.getChildElement(root, XmlTools.ELT_FIRESET);
    	Fireset fireset = loadFiresetElement(elt,level);
    	//
    	result.setFireset(fireset);
    	return result;
    }
    
    private static Fireset loadFiresetElement(Element root, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String name = root.getAttribute(XmlTools.ATT_NAME).trim();
		String folder = level.getInstancePath()+File.separator+FileTools.FOLDER_FIRES;
		folder = folder + File.separator+name;
    	Fireset result = FiresetLoader.loadFireset(folder,level);
    	return result;
    }
}
