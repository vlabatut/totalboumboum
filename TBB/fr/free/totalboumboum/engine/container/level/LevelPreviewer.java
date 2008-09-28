package fr.free.totalboumboum.engine.container.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

public class LevelPreviewer
{

    public static BufferedImage previewLevel(String folder) throws ParserConfigurationException, SAXException, IOException    
    {	BufferedImage result;
    	// init
    	String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getLevelsPath()+File.separator+folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		result = previewPreviewElement(individualFolder,root);
		return result;
    }
    
    private static BufferedImage previewPreviewElement(String folder, Element root) throws IOException
    {	Element element = root.getChild(XmlTools.ELT_PREVIEW);
		String filePath = folder+File.separator+element.getAttribute(XmlTools.ATT_FILE).getValue().trim();
		BufferedImage result = ImageTools.loadImage(filePath,null);
    	return result;
    }
}
