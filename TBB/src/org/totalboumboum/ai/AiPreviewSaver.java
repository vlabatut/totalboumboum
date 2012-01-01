package org.totalboumboum.ai;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Comment;
import org.jdom.Element;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AiPreviewSaver
{	
	public static void saveAiPreview(AiPreview aiPreview) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveAiElement(aiPreview);	
		
		// save file
		String path = FilePaths.getAisPath()+File.separator+aiPreview.getPack()+File.separator+FileNames.FILE_AIS+File.separator+aiPreview.getFolder();
		String engineFile = path+File.separator+FileNames.FILE_AI+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_AI+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveAiElement(AiPreview aiPreview)
	{	Element result = new Element(XmlNames.AI); 
		
		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);
		
		// notes
		Element notesElement = saveNotesElement(aiPreview);
		result.addContent(notesElement);
		
		// authors
		Element authorsElement = saveAuthorsElement(aiPreview);
		result.addContent(authorsElement);

		return result;
	}
	
	private static Element saveNotesElement(AiPreview aiPreview)
	{	Element result = new Element(XmlNames.NOTES);
		Iterator<String> it = aiPreview.getNotes().iterator();
		while(it.hasNext())
		{	String temp = it.next();
			Element elt = new Element(XmlNames.LINE);
			elt.setAttribute(XmlNames.VALUE,temp);
			result.addContent(elt);
		}
		return result;
	}
	
	private static Element saveAuthorsElement(AiPreview aiPreview)
	{	Element result = new Element(XmlNames.AUTHORS);
		Iterator<String> it = aiPreview.getAuthors().iterator();
		while(it.hasNext())
		{	String temp = it.next();
			Element elt = new Element(XmlNames.AUTHOR);
			elt.setAttribute(XmlNames.NAME,temp);
			result.addContent(elt);
		}
		return result;
	}
}
