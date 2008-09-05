package fr.free.totalboumboum.data.language;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class LanguageLoader
{	
	public static Language loadLanguage(String name) throws ParserConfigurationException, SAXException, IOException
	{	Language result = new Language();
		String individualFolder = FileTools.getLanguagesPath();
		File dataFile = new File(individualFolder+File.separator+name+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LANGUAGE+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadLanguageElement(root,result);
		return result;
	}

	private static void loadLanguageElement(Element root, Language result)
	{	ArrayList<Element> elements = XmlTools.getChildElements(root, XmlTools.ELT_TEXT);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadTextElement(temp,result);
		}
	}

	private static void loadTextElement(Element root, Language result)
	{	String key = root.getAttribute(XmlTools.ATT_NAME).trim();
		String value = root.getAttribute(XmlTools.ATT_VALUE).trim();
		result.addText(key, value);	
	}
}
