package fr.free.totalboumboum.engine.content.feature.gesture.modulation;

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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.action.Circumstance;
import fr.free.totalboumboum.engine.content.feature.action.CircumstanceLoader;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.GeneralActionLoader;
import fr.free.totalboumboum.engine.content.feature.gesture.Gesture;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ModulationsLoader
{	

	public static void loadModulations(String individualFolder, GesturePack pack) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	File dataFile = new File(individualFolder+File.separator+FileTools.FILE_MODULATIONS+FileTools.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FileTools.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_MODULATIONS+FileTools.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadModulationsElement(root,individualFolder,pack);
		}
	}
	
    @SuppressWarnings("unchecked")
	private static void loadModulationsElement(Element root, String individualFolder, GesturePack pack) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	List<Element> gesturesList = root.getChildren(XmlTools.ELT_GESTURE);
		Iterator<Element> i = gesturesList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			loadGestureElement(tp,individualFolder,pack);
		}
	}
    
    private static void loadGestureElement(Element root, String individualFolder, GesturePack pack) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// name
    	String name = root.getAttribute(XmlTools.ATT_NAME).getValue().toUpperCase(Locale.ENGLISH);
		GestureName gestureName = GestureName.valueOf(name);
    	Gesture gesture = pack.getGesture(gestureName);
    	// file
    	String fileName = root.getAttribute(XmlTools.ATT_FILE).getValue();
    	String localFilePath = individualFolder+File.separator+fileName;
    	// opening
		File dataFile = new File(localFilePath);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GESTUREMODULATIONS+FileTools.EXTENSION_SCHEMA);
		Element elt = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		loadGestureModulations(elt,gesture);
    }
    
    private static void loadGestureModulations(Element root, Gesture gesture) throws IOException, ClassNotFoundException
    {	// self modulations
		Element selfElt = root.getChild(XmlTools.ELT_SELF_MODULATIONS);
		loadModulationsElement(selfElt,ModType.SELF,gesture);
    	
		// other modulations
		Element otherElt = root.getChild(XmlTools.ELT_OTHER_MODULATIONS);
		loadModulationsElement(otherElt,ModType.OTHER,gesture);
    	
		// actor modulations
		Element actorElt = root.getChild(XmlTools.ELT_ACTOR_MODULATIONS);
		loadModulationsElement(actorElt,ModType.ACTOR,gesture);
		
		// target modulations
		Element targetElt = root.getChild(XmlTools.ELT_TARGET_MODULATIONS);
		loadModulationsElement(targetElt,ModType.TARGET,gesture);
		
		// third modulations
		Element thirdElt = root.getChild(XmlTools.ELT_THIRD_MODULATIONS);
		loadModulationsElement(thirdElt,ModType.THIRD,gesture);
    }
    
    @SuppressWarnings("unchecked")
	private static void loadModulationsElement(Element root, ModType type, Gesture gesture) throws IOException, ClassNotFoundException
    {	List<Element> modulationsList = root.getChildren(XmlTools.ELT_MODULATION);
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
				AbstractActionModulation actionModulation = loadActionModulationElement(type,gesture.getName(),tp);
				gesture.addModulation(actionModulation);
			}
		}
    }
    
    private static AbstractStateModulation loadStateModulationElement(ModType type, GestureName gestureName, Element root) throws IOException, ClassNotFoundException
    {	// strength
		String strengthStr = root.getAttribute(XmlTools.ATT_STRENGTH).getValue().trim();
		float strength;
		if(strengthStr.equals(XmlTools.VAL_MAX))
			strength = Float.MAX_VALUE;
		else
			strength = Float.parseFloat(strengthStr);
		
		// frame
		boolean frame = Boolean.parseBoolean(root.getAttribute(XmlTools.ATT_FRAME).getValue());
		
		// name
		Element nameElt = root.getChild(XmlTools.ELT_NAME);
		String name = nameElt.getAttribute(XmlTools.ATT_VALUE).getValue().trim().toUpperCase(Locale.ENGLISH);
		
		// result
		AbstractStateModulation result;
		if(type.equals(ModType.SELF))
			result = new SelfModulation(name);
		else //if(type.equals(ModType.OTHER))
		{	result = new OtherModulation(name);
			// contacts
			ArrayList<Contact> contacts = Contact.loadContactsAttribute(root,XmlTools.ATT_CONTACT);
			for(Contact contact: contacts)
				((OtherModulation)result).addContact(contact);
			// tilePositions
			ArrayList<TilePosition> tilePositions = TilePosition.loadTilePositionsAttribute(root,XmlTools.ATT_TILE_POSITION);
			for(TilePosition tilePosition: tilePositions)
				((OtherModulation)result).addTilePosition(tilePosition);
		}

		// misc
    	result.setFrame(frame);
    	result.setStrength(strength);
		result.setGestureName(gestureName);
		
		return result;
    }
		
    private static AbstractActionModulation loadActionModulationElement(ModType type, GestureName gestureName, Element root) throws IOException, ClassNotFoundException
    {	// strength
		String strengthStr = root.getAttribute(XmlTools.ATT_STRENGTH).getValue().trim();
		float strength;
		if(strengthStr.equals(XmlTools.VAL_MAX))
			strength = Float.MAX_VALUE;
		else
			strength = Float.parseFloat(strengthStr);
		
		// frame
		boolean frame = Boolean.parseBoolean(root.getAttribute(XmlTools.ATT_FRAME).getValue());
		
		// action
		Element actionElt = root.getChild(XmlTools.ELT_ACTION);
		GeneralAction action = GeneralActionLoader.loadActionElement(actionElt);
    	
		// actor restrictions
		ArrayList<AbstractAbility> actorRestrictions = new ArrayList<AbstractAbility>();
		Element actorRestrElt = root.getChild(XmlTools.ELT_ACTOR_RESTRICTIONS);
		if(actorRestrElt!=null)
			actorRestrictions = AbilityLoader.loadAbilitiesElement(actorRestrElt);
    	
		// target restrictions
		ArrayList<AbstractAbility> targetRestrictions = new ArrayList<AbstractAbility>();
		Element targetRestrElt = root.getChild(XmlTools.ELT_TARGET_RESTRICTIONS);
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
			Element actorCircElt = root.getChild(XmlTools.ELT_ACTOR_CIRCUMSTANCES);
			Circumstance actorCircumstance = ((ThirdModulation)result).getActorCircumstance();
			CircumstanceLoader.loadCircumstanceElement(actorCircElt,actorCircumstance);
			// target circumstance
			Element targetCircElt = root.getChild(XmlTools.ELT_TARGET_CIRCUMSTANCES);
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
