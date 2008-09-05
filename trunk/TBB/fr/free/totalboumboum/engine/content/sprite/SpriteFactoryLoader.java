package fr.free.totalboumboum.engine.content.sprite;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.explosion.ExplosionLoader;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;



public abstract class SpriteFactoryLoader
{	
	protected static Element openFile(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_SPRITE+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_SPRITE+FileTools.EXTENSION_SCHEMA);
		Element result = XmlTools.getRootFromFile(dataFile,schemaFile);
		//
		return result;
	}

	protected static <T extends Sprite> void loadExplosion(Element root, Level level, SpriteFactory<T> result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Explosion explosion = new Explosion();
		if(XmlTools.hasChildElement(root, XmlTools.ELT_EXPLOSION))
		{	Element elt = XmlTools.getChildElement(root, XmlTools.ELT_EXPLOSION);
			String name = elt.getAttribute(XmlTools.ATT_NAME).trim();
			String folder = level.getInstancePath()+File.separator+FileTools.FOLDER_EXPLOSIONS;
			folder = folder + File.separator+name;
			explosion = ExplosionLoader.loadExplosion(folder,level);
		}
		result.setExplosion(explosion);
	}
}

/**
 * TODO 
 * pb : besoin d'identifier l'instance, mais le level (thème?) n'a pas encore été affecté à la loop...
 * solution : initialiser l'instance en premier ? 
 */
