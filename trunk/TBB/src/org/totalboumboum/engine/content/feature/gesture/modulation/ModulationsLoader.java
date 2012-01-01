package org.totalboumboum.engine.content.feature.gesture.modulation;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.feature.ability.AbilityLoader;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.CircumstanceLoader;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.GeneralActionLoader;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.HollowGesture;
import org.totalboumboum.engine.content.feature.gesture.HollowGesturePack;
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
public class ModulationsLoader
{	

	public static void loadModulations(String individualFolder, HollowGesturePack pack, Role role) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	File dataFile = new File(individualFolder+File.separator+FileNames.FILE_MODULATIONS+FileNames.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FilePaths.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_MODULATIONS+FileNames.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadModulationsElement(root,individualFolder,pack,role);
		}
	}
	
    @SuppressWarnings("unchecked")
	private static void loadModulationsElement(Element root, String individualFolder, HollowGesturePack pack, Role role) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	List<Element> gesturesList = root.getChildren(XmlNames.GESTURE);
		Iterator<Element> i = gesturesList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			loadGestureElement(tp,individualFolder,pack,role);
		}
	}
    
    private static void loadGestureElement(Element root, String individualFolder, HollowGesturePack pack, Role role) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue().toUpperCase(Locale.ENGLISH);
		GestureName gestureName = GestureName.valueOf(name);
		HollowGesture gesture = pack.getGesture(gestureName);
    	
    	// file
    	String fileName = root.getAttribute(XmlNames.FILE).getValue();
    	String localFilePath = individualFolder+File.separator+fileName;
    	
    	// opening
		File dataFile = new File(localFilePath);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_GESTUREMODULATIONS+FileNames.EXTENSION_SCHEMA);
		Element elt = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		loadGestureModulations(elt,gesture,role);
    }
    
    private static void loadGestureModulations(Element root, HollowGesture gesture, Role role) throws IOException, ClassNotFoundException
    {	// self modulations
		Element selfElt = root.getChild(XmlNames.SELF_MODULATIONS);
		loadModulationsElement(selfElt,ModType.SELF,gesture,role);
    	
		// other modulations
		Element otherElt = root.getChild(XmlNames.OTHER_MODULATIONS);
		loadModulationsElement(otherElt,ModType.OTHER,gesture,role);
    	
		// actor modulations
		Element actorElt = root.getChild(XmlNames.ACTOR_MODULATIONS);
		loadModulationsElement(actorElt,ModType.ACTOR,gesture,role);
		
		// target modulations
		Element targetElt = root.getChild(XmlNames.TARGET_MODULATIONS);
		loadModulationsElement(targetElt,ModType.TARGET,gesture,role);
		
		// third modulations
		Element thirdElt = root.getChild(XmlNames.THIRD_MODULATIONS);
		loadModulationsElement(thirdElt,ModType.THIRD,gesture,role);
    }
    
    @SuppressWarnings("unchecked")
	private static void loadModulationsElement(Element root, ModType type, HollowGesture gesture, Role role) throws IOException, ClassNotFoundException
    {	List<Element> modulationsList = root.getChildren(XmlNames.MODULATION);
		Iterator<Element> i = modulationsList.iterator();
		if(type==ModType.SELF || type==ModType.OTHER)
		{	while(i.hasNext())
			{	Element tp = i.next();
				AbstractStateModulation stateModulation = loadStateModulationElement(type,gesture.getName(),tp);
				gesture.addModulation(stateModulation);
			}	 			
		}
		else
		{	while(i.hasNext())
			{	Element tp = i.next();
				AbstractActionModulation actionModulation = loadActionModulationElement(type,gesture.getName(),tp,role);
				gesture.addModulation(actionModulation);
			}
		}
    }
    
    private static AbstractStateModulation loadStateModulationElement(ModType type, GestureName gestureName, Element root) throws IOException, ClassNotFoundException
    {	// strength
		String strengthStr = root.getAttribute(XmlNames.STRENGTH).getValue().trim();
		float strength;
		if(strengthStr.equals(XmlNames.VAL_MAX))
			strength = Float.MAX_VALUE;
		else
			strength = Float.parseFloat(strengthStr);
		
		// frame
		boolean frame = Boolean.parseBoolean(root.getAttribute(XmlNames.FRAME).getValue());
		
		// name
		Element nameElt = root.getChild(XmlNames.NAME);
		String name = nameElt.getAttribute(XmlNames.VALUE).getValue().trim().toUpperCase(Locale.ENGLISH);
		
		// result
		AbstractStateModulation result;
		if(type.equals(ModType.SELF))
			result = new SelfModulation(name);
		else //if(type.equals(ModType.OTHER))
		{	result = new OtherModulation(name);
			// contacts
			List<Contact> contacts = Contact.loadContactsAttribute(root,XmlNames.CONTACT);
			for(Contact contact: contacts)
				((OtherModulation)result).addContact(contact);
			// tilePositions
			List<TilePosition> tilePositions = TilePosition.loadTilePositionsAttribute(root,XmlNames.TILE_POSITION);
			for(TilePosition tilePosition: tilePositions)
				((OtherModulation)result).addTilePosition(tilePosition);
		}

		// misc
    	result.setFrame(frame);
    	result.setStrength(strength);
		result.setGestureName(gestureName);
		
		return result;
    }
		
    private static AbstractActionModulation loadActionModulationElement(ModType type, GestureName gestureName, Element root, Role role) throws IOException, ClassNotFoundException
    {	// strength
		String strengthStr = root.getAttribute(XmlNames.STRENGTH).getValue().trim();
		float strength;
		if(strengthStr.equals(XmlNames.VAL_MAX))
			strength = Float.MAX_VALUE;
		else
			strength = Float.parseFloat(strengthStr);
		
		// frame
		boolean frame = Boolean.parseBoolean(root.getAttribute(XmlNames.FRAME).getValue());
		
		// action
		Element actionElt = root.getChild(XmlNames.ACTION);
		GeneralAction action = GeneralActionLoader.loadActionElement(actionElt);
		if(type==ModType.ACTOR)
			action.addActor(role);
		else if(type==ModType.TARGET);
			action.addTarget(role);
    	
		// actor restrictions
		List<AbstractAbility> actorRestrictions = new ArrayList<AbstractAbility>();
		Element actorRestrElt = root.getChild(XmlNames.ACTOR_RESTRICTIONS);
		if(actorRestrElt!=null)
			actorRestrictions = AbilityLoader.loadAbilitiesElement(actorRestrElt);
    	
		// target restrictions
		List<AbstractAbility> targetRestrictions = new ArrayList<AbstractAbility>();
		Element targetRestrElt = root.getChild(XmlNames.TARGET_RESTRICTIONS);
		if(targetRestrElt!=null)
			targetRestrictions = AbilityLoader.loadAbilitiesElement(targetRestrElt);
		
		// result
		AbstractActionModulation result;
		if(type.equals(ModType.ACTOR))
			result = new ActorModulation(action);
		else if(type.equals(ModType.TARGET))
			result = new TargetModulation(action);
		else //if(type.equals(ModType.THIRD))
		{	result = new ThirdModulation(action);
			// actor circumstance
			Element actorCircElt = root.getChild(XmlNames.ACTOR_CIRCUMSTANCES);
			Circumstance actorCircumstance = ((ThirdModulation)result).getActorCircumstance();
			CircumstanceLoader.loadCircumstanceElement(actorCircElt,actorCircumstance);
			// target circumstance
			Element targetCircElt = root.getChild(XmlNames.TARGET_CIRCUMSTANCES);
			Circumstance targetCircumstance = ((ThirdModulation)result).getTargetCircumstance();
			CircumstanceLoader.loadCircumstanceElement(targetCircElt,targetCircumstance);
		}
		
		// misc
		result.setFrame(frame);
		result.setStrength(strength);
		result.setGestureName(gestureName);
		
		// restrictions
		Iterator<AbstractAbility> j = actorRestrictions.iterator();
		while(j.hasNext())
		{	AbstractAbility restriction = j.next();
			result.addActorRestriction(restriction);
		}
		j = targetRestrictions.iterator();
		while(j.hasNext())
		{	AbstractAbility restriction = j.next();
			result.addTargetRestriction(restriction);
		}
		
		return result;
    }
    
    private enum ModType
    {	OTHER,
    	SELF,
    	
    	ACTOR,
    	TARGET,
    	THIRD;
    }
}
