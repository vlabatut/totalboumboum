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

import fr.free.totalboumboum.configuration.RoundVariables;
import fr.free.totalboumboum.engine.content.feature.Role;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.explosion.ExplosionLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.Gesture;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
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
//if(name==null)
//	System.out.println();

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
		{	// gestures pack
			gesturePack = new GesturePack();
			result.setGesturePack(gesturePack);
			// abilities
			abilities = new ArrayList<AbstractAbility>();
			result.setAbilities(abilities);
			// explosions
			explosion = new Explosion();
			result.setExplosion(explosion);
		}
//if(result.getExplosion()==null)
//		System.out.println(name);
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
	protected static <T extends Sprite> Explosion loadExplosionElement(Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Explosion explosion = null;
		Element elt = root.getChild(XmlTools.ELT_EXPLOSION);
		if(elt!=null)
		{	String name = elt.getAttribute(XmlTools.ATT_NAME).getValue().trim();
			String folder = RoundVariables.instancePath+File.separator+FileTools.FOLDER_EXPLOSIONS;
			folder = folder + File.separator+name;
			explosion = ExplosionLoader.loadExplosion(folder);
		}
		return explosion;
	}
}
