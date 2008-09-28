package fr.free.totalboumboum.engine.container.itemset;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactoryLoader;
import fr.free.totalboumboum.engine.content.sprite.item.ItemFactory;
import fr.free.totalboumboum.engine.content.sprite.item.ItemFactoryLoader;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;


public class ItemsetPreviewer
{	
	public static HashMap<String,BufferedImage> previewItemset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_ITEMSET+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ITEMSET+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		HashMap<String,BufferedImage> result = previewItemsetElement(root,individualFolder);
		return result;
    }
    
    private static HashMap<String,BufferedImage> previewItemsetElement(Element root, String folder) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	HashMap<String,BufferedImage> result = new HashMap<String,BufferedImage>();
    	String individualFolder = folder;
		List<Element> items = root.getChildren(XmlTools.ELT_ITEM);	
		Iterator<Element> i = items.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			previewItemElement(temp,individualFolder,result);
		}
		return result;
	}
    
    private static void previewItemElement(Element root, String folder, HashMap<String,BufferedImage> itemPreviews) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			individualFolder = individualFolder+File.separator+attribute.getValue().trim();
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		// preview
		BufferedImage itemPreview = SpriteFactoryLoader.previewSprite(individualFolder);
		itemPreviews.put(name,itemPreview);
    }     
}