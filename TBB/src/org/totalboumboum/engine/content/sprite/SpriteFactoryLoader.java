package org.totalboumboum.engine.content.sprite;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

public abstract class SpriteFactoryLoader
{	
	/////////////////////////////////////////////////////////////////
	// FILE OPENING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Element openFile(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(folderPath+File.separator+FileNames.FILE_SPRITE+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_SPRITE+FileNames.EXTENSION_SCHEMA);
		Element result = XmlTools.getRootFromFile(dataFile,schemaFile);

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// GENERAL ELEMENT LOADING		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected static <T extends Sprite, U extends SpriteFactory<T>> void loadGeneralElement(Element root, SpriteFactory<T> result, HashMap<String,U> abstractSprites)
	{	Element elt = root.getChild(XmlNames.GENERAL);
		
		// name
		String name = elt.getAttribute(XmlNames.NAME).getValue().trim();
		result.setName(name);
//if(name==null)
//	System.out.println();

		// base
		String baseStr = elt.getAttributeValue(XmlNames.BASE);
		result.setBase(baseStr);
		
		// init
		GesturePack gesturePack;
		ArrayList<AbstractAbility> abilities;
		if(baseStr!=null)
		{	SpriteFactory<T> base = abstractSprites.get(baseStr);
			loadGeneralElement(result,base);
		}
		else
		{	// gestures pack
			gesturePack = new GesturePack();
			result.setGesturePack(gesturePack);
			// abilities
			abilities = new ArrayList<AbstractAbility>();
			result.setAbilities(abilities);
		}
	}
	
	protected static <T extends Sprite> void loadGeneralElement(Element root, SpriteFactory<T> result, SpriteFactory<T> base)
	{	Element elt = root.getChild(XmlNames.GENERAL);
	
		// name
		String name = elt.getAttribute(XmlNames.NAME).getValue().trim();
		result.setName(name);
		
		loadGeneralElement(result,base);
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Sprite> void loadGeneralElement(SpriteFactory<T> result, SpriteFactory<T> base)
	{	GesturePack gesturePack = base.getGesturePack().copy();
		ArrayList<AbstractAbility> abilities = base.getAbilities();
		String explosionName = base.getExplosionName();
		//
		result.setGesturePack(gesturePack.copy());
		result.setAbilities((ArrayList<AbstractAbility>)abilities.clone());
		result.setExplosionName(explosionName);
	}
	
	protected static void initDefaultGestures(GesturePack gesturePack, Role role)
	{	{	// gesture NONE
			GestureName gestureName = GestureName.NONE;
			Gesture gesture = gesturePack.getGesture(gestureName);
			for(ActionName an: ActionName.values())
			{	// any action forbidden as an actor except APPEAR (which can be instant, i.e. without anime nor trajectory)
				// and consume (workaround for the fires)
				if(an!=ActionName.APPEAR && an!=ActionName.CONSUME)
				{	GeneralAction ga = an.createGeneralAction();
					ga.addActor(role);
					ga.addAnyTargets();
					ga.addAnyDirections();
					ga.addAnyContacts();
					ga.addAnyOrientations();
					ga.addAnyTilePositions();
					ActorModulation am = new ActorModulation(ga);
					am.setFrame(true);
					am.setStrength(0);
					am.setGestureName(gestureName);
					gesture.addModulation(am);
				}
				// any action forbidden as a target
				{	GeneralAction ga = an.createGeneralAction();
					ga.addAnyActors();
					ga.addTarget(role);
					ga.addAnyDirections();
					ga.addAnyContacts();
					ga.addAnyOrientations();
					ga.addAnyTilePositions();
					TargetModulation tm = new TargetModulation(ga);
					tm.setFrame(true);
					tm.setStrength(0);
					tm.setGestureName(gestureName);
					gesture.addModulation(tm);
				}
				// no modulation for the rest
			}
		}
		{	// gesture ENDED
			GestureName gestureName = GestureName.ENDED;
			Gesture gesture = gesturePack.getGesture(gestureName);
			for(ActionName an: ActionName.values())
			{	// any action forbidden as an actor
				{	GeneralAction ga = an.createGeneralAction();
					ga.addActor(role);
					ga.addAnyTargets();
					ga.addAnyDirections();
					ga.addAnyContacts();
					ga.addAnyOrientations();
					ga.addAnyTilePositions();
					ActorModulation am = new ActorModulation(ga);
					am.setFrame(true);
					am.setStrength(0);
					am.setGestureName(gestureName);
					gesture.addModulation(am);
				}
				// any action forbidden as a target
				{	GeneralAction ga = an.createGeneralAction();
					ga.addAnyActors();
					ga.addTarget(role);
					ga.addAnyDirections();
					ga.addAnyContacts();
					ga.addAnyOrientations();
					ga.addAnyTilePositions();
					TargetModulation tm = new TargetModulation(ga);
					tm.setFrame(true);
					tm.setStrength(0);
					tm.setGestureName(gestureName);
					gesture.addModulation(tm);
				}
				// no modulation for the rest
			}
		}
		{	// gesture ENTERING
			Gesture temp = gesturePack.getGesture(GestureName.APPEARING);
			// copy of APPEARING
			Gesture gesture = temp.copy();
			gesture.setName(GestureName.ENTERING);
			// but animes are taken from STANDING if there's no anime for APPEARING
			if(temp.hasNoAnimes())
				temp = gesturePack.getGesture(GestureName.STANDING);
			gesture.setAnimes(temp);
			gesturePack.addGesture(gesture,GestureName.ENTERING);
		}
		{	// gesture PREPARED
			Gesture temp = gesturePack.getGesture(GestureName.APPEARING);
			// copy of APPEARING
			Gesture gesture = temp.copy();
			gesture.setName(GestureName.PREPARED);
			// with the animes of STANDING
			temp = gesturePack.getGesture(GestureName.STANDING);
			gesture.setAnimes(temp);			
			gesturePack.addGesture(gesture,GestureName.PREPARED);
		}
		{	// gesture HIDING
			GestureName gestureName = GestureName.HIDING;
			Gesture gesture = gesturePack.getGesture(gestureName);
//if(role==Role.ITEM)
//	System.out.println();
			for(ActionName an: ActionName.values())
			{	// any action forbidden as an actor except APPEAR (which can be instant, i.e. without anime nor trajectory)
				if(an!=ActionName.APPEAR)
				{	GeneralAction ga = an.createGeneralAction();
					ga.addActor(role);
					ga.addAnyTargets();
					ga.addAnyDirections();
					ga.addAnyContacts();
					ga.addAnyOrientations();
					ga.addAnyTilePositions();
					ActorModulation am = new ActorModulation(ga);
					am.setFrame(true);
					am.setStrength(0);
					am.setGestureName(gestureName);
					gesture.addModulation(am);
				}
				// any action forbidden as a target except RELEASE (workaround for the items)
				if(an!=ActionName.RELEASE)
				{	GeneralAction ga = an.createGeneralAction();
					ga.addAnyActors();
					ga.addTarget(role);
					ga.addAnyDirections();
					ga.addAnyContacts();
					ga.addAnyOrientations();
					ga.addAnyTilePositions();
					TargetModulation tm = new TargetModulation(ga);
					tm.setFrame(true);
					tm.setStrength(0);
					tm.setGestureName(gestureName);
					gesture.addModulation(tm);
				}
				// no modulation for the rest
			}
//if(role==Role.ITEM)
//	System.out.println();
		}	
	}
	
	
	/////////////////////////////////////////////////////////////////
	// EPLOSION LOADING		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected static String loadExplosionElement(Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String result = null;
		Element elt = root.getChild(XmlNames.EXPLOSION);
		if(elt!=null)
			result = elt.getAttribute(XmlNames.NAME).getValue().trim();
		return result;
	}
}
