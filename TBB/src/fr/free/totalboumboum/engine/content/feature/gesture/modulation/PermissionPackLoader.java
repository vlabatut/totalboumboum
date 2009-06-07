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

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralActionLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class PermissionPackLoader
{	
//	private static final String STATE = "state";
	private static final String ACTOR = "actor";
	private static final String TARGET = "target";
	private static final String THIRD = "third";

	public static PermissionPack loadPermissionPack(String individualFolder, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	PermissionPack result = new PermissionPack();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_PERMISSIONS+FileTools.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FileTools.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PERMISSIONS+FileTools.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadPermissionsElement(root,individualFolder,level,result);
		}
		return result;
	}
	
    @SuppressWarnings("unchecked")
	private static void loadPermissionsElement(Element root, String individualFolder, Level level, PermissionPack result) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	List<Element> gesturesList = root.getChildren(XmlTools.ELT_GESTURE);
		Iterator<Element> i = gesturesList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			PermissionGesture permissionGesture = loadGestureElement(tp,individualFolder,level);
			result.addPermissionGesture(permissionGesture);
		}
	}
    
    private static PermissionGesture loadGestureElement(Element root, String individualFolder, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// name
    	String gestureName = root.getAttribute(XmlTools.ATT_NAME).getValue();
    	// file
    	String fileName = root.getAttribute(XmlTools.ATT_FILE).getValue();
    	String localFilePath = individualFolder+File.separator+fileName;
    	// opening
		File dataFile = new File(localFilePath);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GESTUREPERMISSIONS+FileTools.EXTENSION_SCHEMA);
		Element elt = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		PermissionGesture result = loadGesturePermissions(elt,gestureName,level);
		result.setName(gestureName);
		return result;
    }
    
    private static PermissionGesture loadGesturePermissions(Element root, String gestureName, Level level) throws IOException, ClassNotFoundException
    {	PermissionGesture result = new PermissionGesture();
		result.setName(gestureName);
    	// state modulations
		Element stateElt = root.getChild(XmlTools.ELT_STATE_MODULATIONS);
		loadModulationsElement(stateElt,gestureName,level,result);
    	// actor permissions
		Element actorElt = root.getChild(XmlTools.ELT_ACTOR_PERMISSIONS);
		loadPermissionsElement(actorElt,ACTOR,gestureName,level,result);
		// target permissions
		Element targetElt = root.getChild(XmlTools.ELT_TARGET_PERMISSIONS);
		loadPermissionsElement(targetElt,TARGET,gestureName,level,result);
		// third permissions
		Element thirdElt = root.getChild(XmlTools.ELT_THIRD_PERMISSIONS);
		loadPermissionsElement(thirdElt,THIRD,gestureName,level,result);
    	//
		return result;
    }
    
    @SuppressWarnings("unchecked")
	private static void loadPermissionsElement(Element root, String type, String gestureName, Level level, PermissionGesture permissionGesture) throws IOException, ClassNotFoundException
    {	List<Element> permissionsList = root.getChildren(XmlTools.ELT_PERMISSION);
		Iterator<Element> i = permissionsList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			AbstractActionPermission abstractPermission = loadActionPermissionElement(type,gestureName,tp,level);
			permissionGesture.addPermission(abstractPermission);
		}
    }
    
    @SuppressWarnings("unchecked")
	private static void loadModulationsElement(Element root, String gestureName, Level level, PermissionGesture permissionGesture) throws IOException, ClassNotFoundException
    {	List<Element> modulationsList = root.getChildren(XmlTools.ELT_MODULATION);
		Iterator<Element> i = modulationsList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			StateModulation stateModulation = loadStateModulationElement(gestureName,tp,level);
			permissionGesture.addModulation(stateModulation);
		}
    }
    
    private static StateModulation loadStateModulationElement(String gestureName, Element root, Level level) throws IOException, ClassNotFoundException
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
		
    private static AbstractActionPermission loadActionPermissionElement(String type, String gestureName, Element root, Level level) throws IOException, ClassNotFoundException
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
		AbstractActionPermission result;
		if(type.equals(ACTOR))
			result = new ActorPermission(action);
		else if(type.equals(TARGET))
			result = new TargetPermission(action);
		else //if(type.equals(THIRD))
			result = new ThirdPermission(action);
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
