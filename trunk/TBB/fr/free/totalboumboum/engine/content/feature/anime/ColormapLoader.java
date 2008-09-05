package fr.free.totalboumboum.engine.content.feature.anime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class ColormapLoader
{
	public static Colormap loadColormap(String individualFolder) throws IOException, ParserConfigurationException, SAXException
	{	// oprning
		File dataFile = new File(individualFolder+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_COLORMAP+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Colormap result = new Colormap();
		loadColorsElement(root, result);
		return result;
	}
    
    private static void loadColorsElement(Element root, Colormap colormap) throws IOException
    {	// colors
    	ArrayList<Element> colorsList = XmlTools.getChildElements(root,XmlTools.ELT_COLOR);
		for(int i=0;i<colorsList.size();i++)
			loadColorElement(colorsList.get(i),colormap);    	
    }
	
    private static void loadColorElement(Element root, Colormap colormap) throws IOException
    {	// index
		int index = Integer.parseInt(root.getAttribute(XmlTools.ATT_INDEX));
		// RGB
		byte colors[] = new byte[3];
		colors[0] = (byte)Integer.parseInt(root.getAttribute(XmlTools.ATT_RED));
		// green
		colors[1] = (byte)Integer.parseInt(root.getAttribute(XmlTools.ATT_GREEN));
		// blue
		colors[2] = (byte)Integer.parseInt(root.getAttribute(XmlTools.ATT_BLUE));
		// colormap
		colormap.put(index,colors);
    }
}
