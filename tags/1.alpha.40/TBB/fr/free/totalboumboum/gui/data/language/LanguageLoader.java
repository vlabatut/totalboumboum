package fr.free.totalboumboum.gui.data.language;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.tools.GuiFileTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class LanguageLoader
{	
	public static Language loadLanguage(String name) throws ParserConfigurationException, SAXException, IOException
	{	Language result = new Language();
		String individualFolder = GuiFileTools.getLanguagesPath();
		File dataFile = new File(individualFolder+File.separator+name+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LANGUAGE+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadLanguageElement(root,result);
		return result;
	}

	private static void loadLanguageElement(Element root, Language result)
	{	List<Element> elements = root.getChildren(XmlTools.ELT_TEXT);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadTextElement(temp,result);
		}
	}

	private static void loadTextElement(Element root, Language result)
	{	String key = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.addText(key, value);	
	}
}
