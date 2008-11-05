package fr.free.totalboumboum.configuration.profile;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.content.feature.anime.Colormap;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

public class PortraitsLoader
{
	protected static Portraits loadPortraits(String spriteFolderPath, PredefinedColor color) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		String folderPath = spriteFolderPath+File.separator+FileTools.FOLDER_PORTRAITS;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_PORTRAITS+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PORTRAITS+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		//
		Portraits result = new Portraits();
		loadPortraitsElement(root,folderPath,color,result);
		completePortraits(result);
		return result;
	}
	
	private static void loadPortraitsElement(Element root, String folderPath, PredefinedColor color, Portraits result) throws IOException, ParserConfigurationException, SAXException
	{	// colors
		String localFilePath = folderPath;
    	Colormap colormap = null;
    	String colorFolder = null;
		Object obj;
		Element elt = root.getChild(XmlTools.ELT_COLORS);;
		if(elt!=null && color!=null)
		{	obj = ImageTools.loadColorsElement(elt,localFilePath,color);
			if(obj instanceof Colormap)
				colormap = (Colormap)obj;
			else
				colorFolder = (String)obj;
		}		
		if(colorFolder!=null)
			localFilePath = localFilePath+File.separator + colorFolder;
		
		// ingame
		Element ingame = root.getChild(XmlTools.ELT_INGAME);
		loadIngameElement(ingame,localFilePath,colormap,result);
		
		// outgame
		Element outgame = root.getChild(XmlTools.ELT_OUTGAME);
		loadOutgameElement(outgame,localFilePath,colormap,result);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadIngameElement(Element root, String folderPath, Colormap colormap, Portraits result) throws IOException, ParserConfigurationException, SAXException
	{	// folder
		String folder = folderPath;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
		{	String f = attribute.getValue().trim();
			folder = folder + File.separator + f;			
		}
		// portraits
		List<Element> elements = root.getChildren(XmlTools.ELT_PORTRAIT);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			String name = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim().toUpperCase(Locale.ENGLISH);
			String file = temp.getAttribute(XmlTools.ATT_FILE).getValue().trim();
			String imagePath = folder + File.separator + file;
			BufferedImage image = ImageTools.loadImage(imagePath,colormap);
			result.addIngamePortrait(name, image);
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadOutgameElement(Element root, String folderPath, Colormap colormap, Portraits result) throws IOException, ParserConfigurationException, SAXException
	{	// folder
		String folder = folderPath;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
		{	String f = attribute.getValue().trim();
			folder = folder + File.separator + f;			
		}
		// portraits
		List<Element> elements = root.getChildren(XmlTools.ELT_PORTRAIT);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			String name = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim().toUpperCase(Locale.ENGLISH);
			String file = temp.getAttribute(XmlTools.ATT_FILE).getValue().trim();
			String imagePath = folder + File.separator + file;
			BufferedImage image = ImageTools.loadImage(imagePath,colormap);
			result.addOutgamePortrait(name,image);
		}
	}
	
	/**
	 * complete the portraits if some images are missing
	 * @param result
	 */
	private static void completePortraits(Portraits result)
	{	// square images
		{	// creation
			int width=64,height=64;
			BufferedImage image = ImageTools.getAbsentImage(width,height);
			// verification
			if(!result.containsIngamePortrait(Portraits.INGAME_LOST))
				result.addIngamePortrait(Portraits.INGAME_LOST,image);
			if(!result.containsIngamePortrait(Portraits.INGAME_NORMAL))
				result.addIngamePortrait(Portraits.INGAME_NORMAL,image);
			if(!result.containsIngamePortrait(Portraits.INGAME_OUT))
				result.addIngamePortrait(Portraits.INGAME_OUT,image);
			if(!result.containsIngamePortrait(Portraits.INGAME_WON))
				result.addIngamePortrait(Portraits.INGAME_WON,image);
			if(!result.containsOutgamePortrait(Portraits.OUTGAME_HEAD))
				result.addOutgamePortrait(Portraits.OUTGAME_HEAD,image);
		}		
		// rectangular images
		{	// creation
			int width=64,height=128;
			BufferedImage image = ImageTools.getAbsentImage(width, height);
			// verification
			if(!result.containsIngamePortrait(Portraits.OUTGAME_BODY))
				result.addIngamePortrait(Portraits.OUTGAME_BODY,image);
		}				
	}
}
