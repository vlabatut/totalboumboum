package fr.free.totalboumboum.engine.content.sprite;

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

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.explosion.ExplosionLoader;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;



public abstract class SpriteFactoryLoader
{	
	public static Element openFile(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_SPRITE+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_SPRITE+FileTools.EXTENSION_SCHEMA);
		Element result = XmlTools.getRootFromFile(dataFile,schemaFile);
		//
		return result;
	}

	protected static <T extends Sprite> void loadExplosion(Element root, Level level, SpriteFactory<T> result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Explosion explosion = new Explosion();
		Element elt = root.getChild(XmlTools.ELT_EXPLOSION);
		if(elt!=null)
		{	String name = elt.getAttribute(XmlTools.ATT_NAME).getValue().trim();
			String folder = level.getInstancePath()+File.separator+FileTools.FOLDER_EXPLOSIONS;
			folder = folder + File.separator+name;
			explosion = ExplosionLoader.loadExplosion(folder,level);
		}
		result.setExplosion(explosion);
	}
	
	public static BufferedImage previewSprite(String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Element element = SpriteFactoryLoader.openFile(folder);
		Element prev = element.getChild(XmlTools.ELT_PREVIEW);
		String filePath = folder+File.separator+prev.getAttribute(XmlTools.ATT_FILE).getValue().trim();
		BufferedImage result = ImageTools.loadImage(filePath,null);
		return result;
	}
}
