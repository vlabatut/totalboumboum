package fr.free.totalboumboum.engine.content.sprite;

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
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.GameVariables;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.explosion.ExplosionLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public abstract class SpriteFactoryLoader
{	
	/////////////////////////////////////////////////////////////////
	// FILE OPENING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Element openFile(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_SPRITE+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_SPRITE+FileTools.EXTENSION_SCHEMA);
		Element result = XmlTools.getRootFromFile(dataFile,schemaFile);

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// GENERAL ELEMENT LOADING		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected static <T extends Sprite, U extends SpriteFactory<T>> void loadGeneralElement(Element root, SpriteFactory<T> result, HashMap<String,U> abstractSprites)
	{	Element elt = root.getChild(XmlTools.ELT_GENERAL);
		
		// name
		String name = elt.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setName(name);
		
		// base
		String baseStr = elt.getAttributeValue(XmlTools.ATT_BASE);
		result.setBase(baseStr);
		
		// init
		GesturePack gesturePack;
		ArrayList<AbstractAbility> abilities;
		Explosion explosion;
		if(baseStr!=null)
		{	SpriteFactory<T> base = abstractSprites.get(baseStr);
			loadGeneralElement(result,base);
		}
		else
		{	gesturePack = new GesturePack();
			abilities = new ArrayList<AbstractAbility>();
			explosion = new Explosion();
			result.setGesturePack(gesturePack);
			result.setAbilities(abilities);
			result.setExplosion(explosion);
		}
	}
	
	protected static <T extends Sprite> void loadGeneralElement(Element root, SpriteFactory<T> result, SpriteFactory<T> base)
	{	Element elt = root.getChild(XmlTools.ELT_GENERAL);
	
		// name
		String name = elt.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setName(name);
		
		loadGeneralElement(result,base);
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Sprite> void loadGeneralElement(SpriteFactory<T> result, SpriteFactory<T> base)
	{	GesturePack gesturePack = base.getGesturePack().copy();
		ArrayList<AbstractAbility> abilities = base.getAbilities();
		Explosion explosion = base.getExplosion();
		//
		result.setGesturePack(gesturePack.copy());
		result.setAbilities((ArrayList<AbstractAbility>)abilities.clone());
		result.setExplosion(explosion);
	}
	
	/////////////////////////////////////////////////////////////////
	// EPLOSION LOADING		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected static <T extends Sprite> Explosion loadExplosionElement(Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Explosion explosion = null;
		Element elt = root.getChild(XmlTools.ELT_EXPLOSION);
		if(elt!=null)
		{	String name = elt.getAttribute(XmlTools.ATT_NAME).getValue().trim();
			String folder = GameVariables.instancePath+File.separator+FileTools.FOLDER_EXPLOSIONS;
			folder = folder + File.separator+name;
			explosion = ExplosionLoader.loadExplosion(folder);
		}
		return explosion;
	}
}
