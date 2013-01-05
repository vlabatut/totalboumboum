package org.totalboumboum.stream.file.replay;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.ImageTools;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ReplayLoader
{	
	public static FileClientStream loadReplay(String folderName) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	FileClientStream result = new FileClientStream();
		
		// load xml file
		String individualFolder = FilePaths.getReplaysPath() + File.separator + folderName;
		File dataFile = new File(individualFolder + File.separator + FileNames.FILE_REPLAY + FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder + File.separator + FileNames.FILE_REPLAY + FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadReplayElement(root,result);
		result.setFolder(folderName);
		
		// load preview image
		String previewPath = individualFolder + File.separator + FileNames.FILE_PREVIEW + FileNames.EXTENSION_PNG;
		File previewFile = new File(previewPath);
		if(previewFile.exists())
		{	BufferedImage preview = ImageTools.loadImage(previewPath,null);
			result.setPreview(preview);
		}
		
		return result;
	}

	private static void loadReplayElement(Element root, FileClientStream result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// level
		Element levelElement = root.getChild(XmlNames.LEVEL);
		loadLevelElement(levelElement,result);
		
		// date
		Element dateElement = root.getChild(XmlNames.DATE);
		loadDateElement(dateElement,result);
		
		// players
		Element playersElement = root.getChild(XmlNames.PLAYERS);
		loadPlayersElement(playersElement,result);		
	}
	
	private static void loadLevelElement(Element root, FileClientStream result)
	{	// name
		String name = root.getAttributeValue(XmlNames.NAME);
		result.setLevelName(name);
		
		// pack
		String pack = root.getAttributeValue(XmlNames.PACK);
		result.setLevelPack(pack);
	}
	
	private static void loadDateElement(Element root, FileClientStream result)
	{	// save
		String saveStr = root.getAttributeValue(XmlNames.SAVE);
		Date save = TimeTools.dateXmlToJava(saveStr);
		result.setSaveDate(save);
	}

	@SuppressWarnings("unchecked")
	private static void loadPlayersElement(Element root, FileClientStream result)
	{	List<Element> playerList = root.getChildren(XmlNames.PLAYER);
		for(Element playerElement: playerList)
			loadPlayerElement(playerElement,result);
	}

	private static void loadPlayerElement(Element root, FileClientStream result)
	{	// name
		String name = root.getAttributeValue(XmlNames.NAME);
		result.addPlayer(name);
	}
}
