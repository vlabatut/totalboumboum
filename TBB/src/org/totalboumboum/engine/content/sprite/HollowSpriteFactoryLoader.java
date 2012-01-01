package org.totalboumboum.engine.content.sprite;

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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.HollowGesture;
import org.totalboumboum.engine.content.feature.gesture.HollowGesturePack;
import org.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
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
public abstract class HollowSpriteFactoryLoader
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
	// GESTURES						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected static void initDefaultGestures(HollowGesturePack gesturePack, Role role)
	{	{	// gesture NONE
			GestureName gestureName = GestureName.NONE;
			HollowGesture gesture = gesturePack.getGesture(gestureName);
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
			HollowGesture gesture = gesturePack.getGesture(gestureName);
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
			HollowGesture temp = gesturePack.getGesture(GestureName.APPEARING);
			// copy of APPEARING
			HollowGesture gesture = temp.copy();
			gesture.setName(GestureName.ENTERING);
			// but animes are taken from STANDING if there's no anime for APPEARING
			if(temp.hasNoAnimes())
				temp = gesturePack.getGesture(GestureName.STANDING);
			gesture.setAnimes(temp);
			gesturePack.addGesture(gesture,GestureName.ENTERING);
		}
		{	// gesture PREPARED
			HollowGesture temp = gesturePack.getGesture(GestureName.APPEARING);
			// copy of APPEARING
			HollowGesture gesture = temp.copy();
			gesture.setName(GestureName.PREPARED);
			// with the animes of STANDING
			temp = gesturePack.getGesture(GestureName.STANDING);
			gesture.setAnimes(temp);			
			gesturePack.addGesture(gesture,GestureName.PREPARED);
		}
		{	// gesture HIDING
			GestureName gestureName = GestureName.HIDING;
			HollowGesture gesture = gesturePack.getGesture(gestureName);
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
	// EPLOSION				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected static String loadExplosionElement(Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String result = null;
		Element elt = root.getChild(XmlNames.EXPLOSION);
		if(elt!=null)
			result = elt.getAttribute(XmlNames.NAME).getValue().trim();
		return result;
	}
}
