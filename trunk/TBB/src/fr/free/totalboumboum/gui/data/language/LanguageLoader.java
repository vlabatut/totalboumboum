package fr.free.totalboumboum.gui.data.language;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.tools.GuiFileTools;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;
import fr.free.totalboumboum.gui.tools.GuiXmlTools;

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
	{	List<Element> elements = root.getChildren(GuiXmlTools.ELT_GROUP);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadGroupElement(temp,"",result);
		}
	}

	private static void loadGroupElement(Element root, String name, Language result)
	{	String key = root.getAttribute(GuiXmlTools.ATT_NAME).getValue().trim();
		String newName = name+key;
		// text
		{	List<Element> elements = root.getChildren(GuiXmlTools.ELT_TEXT);
			Iterator<Element> i = elements.iterator();
			while(i.hasNext())
			{	Element temp = i.next();
				loadTextElement(temp,newName,result);
			}
		}
		// other groups
		{	List<Element> elements = root.getChildren(GuiXmlTools.ELT_GROUP);
			Iterator<Element> i = elements.iterator();
			while(i.hasNext())
			{	Element temp = i.next();
				loadGroupElement(temp,newName,result);
			}
		}
	}

	private static void loadTextElement(Element root, String name, Language result)
	{	String key = root.getAttribute(GuiXmlTools.ATT_NAME).getValue().trim();
		String newName = name+key;
		// value
		String value = root.getAttribute(GuiXmlTools.ATT_VALUE).getValue().trim();
		result.addText(newName,value);
		// tooltip
		Attribute att = root.getAttribute(GuiXmlTools.ATT_TOOLTIP);
		if(att!=null)
		{	String tooltip = att.getValue().trim();
			result.addText(newName+GuiTools.TOOLTIP,tooltip);
		}
	}
}
