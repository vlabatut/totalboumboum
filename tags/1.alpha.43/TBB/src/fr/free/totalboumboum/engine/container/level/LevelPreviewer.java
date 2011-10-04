package fr.free.totalboumboum.engine.container.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.itemset.ItemsetPreviewer;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

public class LevelPreviewer
{

    public static LevelPreview previewLevel(String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException    
    {	// init
    	String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getLevelsPath()+File.separator+folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		LevelPreview result = previewLevelElement(individualFolder,root);
		return result;
    }

    private static LevelPreview previewLevelElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		Element element;
		LevelPreview result = new LevelPreview();
		
		// misc
		element = root.getChild(XmlTools.ELT_TITLE);
		String title = element.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setTitle(title);
		element = root.getChild(XmlTools.ELT_AUTHOR);
		String author = element.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setAuthor(author);
		element = root.getChild(XmlTools.ELT_SOURCE);
		String source = element.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setSource(source);
		
		// visual preview
		element = root.getChild(XmlTools.ELT_PREVIEW);
		String filePath = folder+File.separator+element.getAttribute(XmlTools.ATT_FILE).getValue().trim();
		BufferedImage image = ImageTools.loadImage(filePath,null);
    	result.setVisualPreview(image);		
		
		// instance
		element = root.getChild(XmlTools.ELT_INSTANCE);
		String instanceName = element.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		String instanceFolder = FileTools.getInstancesPath()+File.separator+instanceName;

		// players stuff preview
		PlayersPreviewer.previewPlayers(folder,result);
/*
		// zone stuff preview
		element = root.getChild(XmlTools.ELT_GLOBAL_DIMENSION);
		String globalHeightStr = element.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		int globalHeight = Integer.parseInt(globalHeightStr);
		String globalWidthStr = element.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		int globalWidth = Integer.parseInt(globalWidthStr);
		Zone zone = ZoneLoader.loadZone(folder,globalHeight,globalWidth);
*/
		// itemset
		String itemFolder = instanceFolder + File.separator+FileTools.FOLDER_ITEMS;
		HashMap<String,BufferedImage> itemsetPreview = ItemsetPreviewer.previewItemset(itemFolder);
		result.setItemsetPreview(itemsetPreview);

		return result;
	}
}