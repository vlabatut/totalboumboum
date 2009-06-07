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

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.gesture.Gesture;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralActionLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class ModulationsLoader
{	
	private static final String STATE = "state";
	private static final String ACTOR = "actor";
	private static final String TARGET = "target";
	private static final String THIRD = "third";

	public static void loadModulationPack(String individualFolder, GesturePack pack, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	File dataFile = new File(individualFolder+File.separator+FileTools.FILE_MODULATIONS+FileTools.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FileTools.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_MODULATIONS+FileTools.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadModulationsElement(root,individualFolder,pack,level);
		}
	}
	
    @SuppressWarnings("unchecked")
	private static void loadModulationsElement(Element root, String individualFolder, GesturePack pack, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	List<Element> gesturesList = root.getChildren(XmlTools.ELT_GESTURE);
		Iterator<Element> i = gesturesList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			loadGestureElement(tp,individualFolder,pack,level);
		}
	}
    
    private static void loadGestureElement(Element root, String individualFolder, GesturePack pack, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
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
		loadGestureModulations(elt,gesture,level);
    }
    
    private static void loadGestureModulations(Element root, Gesture gesture, Level level) throws IOException, ClassNotFoundException
    {	// state modulations
		Element stateElt = root.getChild(XmlTools.ELT_STATE_MODULATIONS);
		loadModulationsElement(stateElt,STATE,gesture,level);
    	// actor modulations
		Element actorElt = root.getChild(XmlTools.ELT_ACTOR_MODULATIONS);
		loadModulationsElement(actorElt,ACTOR,gesture,level);
		// target modulations
		Element targetElt = root.getChild(XmlTools.ELT_TARGET_MODULATIONS);
		loadModulationsElement(targetElt,TARGET,gesture,level);
		// third modulations
		Element thirdElt = root.getChild(XmlTools.ELT_THIRD_MODULATIONS);
		loadModulationsElement(thirdElt,THIRD,gesture,level);
    }
    
    @SuppressWarnings("unchecked")
	private static void loadModulationsElement(Element root, String type, Gesture gesture, Level level) throws IOException, ClassNotFoundException
    {	List<Element> modulationsList = root.getChildren(XmlTools.ELT_MODULATION);
		Iterator<Element> i = modulationsList.iterator();
		if(type==STATE)
		{	while(i.hasNext())
			{	Element tp = i.next();
				StateModulation stateModulation = loadStateModulationElement(gesture.getName(),tp,level);
				gesture.addModulation(stateModulation);
			}
	 			
		}
		else
		{	while(i.hasNext())
			{	Element tp = i.next();
				AbstractActionModulation abstractModulation = loadActionModulationElement(type,gesture.getName(),tp,level);
				gesture.addModulation(abstractModulation);
			}
		}
    }
    
    private static StateModulation loadStateModulationElement(GestureName gestureName, Element root, Level level) throws IOException, ClassNotFoundException
    {	// strength
		String strengthStr = root.getAttribute(XmlTools.ATT_STRENGTH).getValue().trim();
		float strength;
		if(strengthStr.equals(AbstractAbility.MAXIMUM_VALUE))
			strength = Float.MAX_VALUE;
		else
			strength = Float.parseFloat(strengthStr);
		// frame
		boolean frame = Boolean.parseBoolean(root.getAttribute(XmlTools.ATT_FRAME).getValue());
		// name
		Element nameElt = root.getChild(XmlTools.ELT_NAME);
		String name = nameElt.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		// result
		StateModulation result = new StateModulation(name);
    	result.setFrame(frame);
    	result.setStrength(strength);
		result.setGestureName(gestureName);
		return result;
    }
		
    private static AbstractActionModulation loadActionModulationElement(String type, GestureName gestureName, Element root, Level level) throws IOException, ClassNotFoundException
    {	// strength
		String strengthStr = root.getAttribute(XmlTools.ATT_STRENGTH).getValue().trim();
		float strength;
		if(strengthStr.equals(AbstractAbility.MAXIMUM_VALUE))
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
			actorRestrictions = AbilityLoader.loadAbilitiesElement(actorRestrElt,level);
    	// target restrictions
		ArrayList<AbstractAbility> targetRestrictions = new ArrayList<AbstractAbility>();
		Element targetRestrElt = root.getChild(XmlTools.ELT_TARGET_RESTRICTIONS);
		if(targetRestrElt!=null)
			targetRestrictions = AbilityLoader.loadAbilitiesElement(targetRestrElt,level);
		// result
		AbstractActionModulation result;
		if(type.equals(ACTOR))
			result = new ActorModulation(action);
		else if(type.equals(TARGET))
			result = new TargetModulation(action);
		else //if(type.equals(THIRD))
			result = new ThirdModulation(action);
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
}
