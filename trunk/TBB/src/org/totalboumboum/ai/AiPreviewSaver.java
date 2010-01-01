package org.totalboumboum.ai;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import org.jdom.Element;
import org.totalboumboum.tools.FileTools;
import org.totalboumboum.tools.XmlTools;
import org.xml.sax.SAXException;


public class AiPreviewSaver
{	
	public static void saveAiPreview(AiPreview aiPreview) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveAiElement(aiPreview);	
		// save file
		String path = FileTools.getAiPath()+File.separator+aiPreview.getPack()+File.separator+aiPreview.getFolder();
		String engineFile = path+File.separator+FileTools.FILE_AI+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_AI+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveAiElement(AiPreview aiPreview)
	{	Element result = new Element(XmlTools.AI); 
		// notes
		Element notesElement = saveNotesElement(aiPreview);
		result.addContent(notesElement);
		// authors
		Element authorsElement = saveAuthorsElement(aiPreview);
		result.addContent(authorsElement);
		//
		return result;
	}
	
	private static Element saveNotesElement(AiPreview aiPreview)
	{	Element result = new Element(XmlTools.NOTES);
		Iterator<String> it = aiPreview.getNotes().iterator();
		while(it.hasNext())
		{	String temp = it.next();
			Element elt = new Element(XmlTools.LINE);
			elt.setAttribute(XmlTools.VALUE,temp);
			result.addContent(elt);
		}
		return result;
	}
	
	private static Element saveAuthorsElement(AiPreview aiPreview)
	{	Element result = new Element(XmlTools.AUTHORS);
		Iterator<String> it = aiPreview.getAuthors().iterator();
		while(it.hasNext())
		{	String temp = it.next();
			Element elt = new Element(XmlTools.AUTHOR);
			elt.setAttribute(XmlTools.NAME,temp);
			result.addContent(elt);
		}
		return result;
	}
}
