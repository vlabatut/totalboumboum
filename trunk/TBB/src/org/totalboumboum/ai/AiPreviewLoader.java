package org.totalboumboum.ai;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.game.match.MatchLoader;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;


public class AiPreviewLoader
{
	public static AiPreview loadAiPreview(String pack, String folder) throws ParserConfigurationException, SAXException, IOException
	{	AiPreview result = new AiPreview(pack,folder);
		String path = FilePaths.getAisPath()+File.separator+pack+File.separator+FileNames.FOLDER_AIS+File.separator+folder;
		File dataFile = new File(path+File.separator+FileNames.FILE_AI+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_AI+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadAiElement(root,result);
		return result;
	}
	
	private static void loadAiElement(Element root, AiPreview result)
	{	Element element; 
		// notes
		element = root.getChild(XmlNames.NOTES);
		ArrayList<String> notes = MatchLoader.loadNotesElement(element);
		result.setNotes(notes);
		// authors
		element = root.getChild(XmlNames.AUTHORS);
		loadAuthorsElement(element,result);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadAuthorsElement(Element root, AiPreview result)
	{	List<Element> authors = root.getChildren(XmlNames.AUTHOR);
		Iterator<Element> it = authors.iterator();
		while(it.hasNext())
		{	Element element = it.next();
			String author = element.getAttributeValue(XmlNames.NAME);
			result.addAuthor(author);
		}
	}
	
}
