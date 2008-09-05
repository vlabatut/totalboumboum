package fr.free.totalboumboum.engine.content.feature.permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbilityLoader;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.GeneralActionLoader;
import fr.free.totalboumboum.engine.loop.Loop;
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
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_PERMISSIONS+FileTools.EXTENSION_DATA);
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
	
    private static void loadPermissionsElement(Element root, String individualFolder, Level level, PermissionPack result) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	ArrayList<Element> gesturesList = XmlTools.getChildElements(root,XmlTools.ELT_GESTURE);
		Iterator<Element> i = gesturesList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			PermissionGesture permissionGesture = loadGestureElement(tp,individualFolder,level);
			result.addPermissionGesture(permissionGesture);
		}
	}
    
    private static PermissionGesture loadGestureElement(Element root, String individualFolder, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// name
    	String gestureName = root.getAttribute(XmlTools.ATT_NAME);
    	// file
    	String fileName = root.getAttribute(XmlTools.ATT_FILE);
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
		Element stateElt = XmlTools.getChildElement(root,XmlTools.ELT_STATE_MODULATIONS);
		loadModulationsElement(stateElt,gestureName,level,result);
    	// actor permissions
		Element actorElt = XmlTools.getChildElement(root,XmlTools.ELT_ACTOR_PERMISSIONS);
		loadPermissionsElement(actorElt,ACTOR,gestureName,level,result);
		// target permissions
		Element targetElt = XmlTools.getChildElement(root,XmlTools.ELT_TARGET_PERMISSIONS);
		loadPermissionsElement(targetElt,TARGET,gestureName,level,result);
		// third permissions
		Element thirdElt = XmlTools.getChildElement(root,XmlTools.ELT_THIRD_PERMISSIONS);
		loadPermissionsElement(thirdElt,THIRD,gestureName,level,result);
    	//
		return result;
    }
    
    private static void loadPermissionsElement(Element root, String type, String gestureName, Level level, PermissionGesture permissionGesture) throws IOException, ClassNotFoundException
    {	ArrayList<Element> permissionsList = XmlTools.getChildElements(root,XmlTools.ELT_PERMISSION);
		Iterator<Element> i = permissionsList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			AbstractActionPermission abstractPermission = loadActionPermissionElement(type,gestureName,tp,level);
			permissionGesture.addPermission(abstractPermission);
		}
    }
    
    private static void loadModulationsElement(Element root, String gestureName, Level level, PermissionGesture permissionGesture) throws IOException, ClassNotFoundException
    {	ArrayList<Element> modulationsList = XmlTools.getChildElements(root,XmlTools.ELT_MODULATION);
		Iterator<Element> i = modulationsList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			StateModulation stateModulation = loadStateModulationElement(gestureName,tp,level);
			permissionGesture.addModulation(stateModulation);
		}
    }
    
    private static StateModulation loadStateModulationElement(String gestureName, Element root, Level level) throws IOException, ClassNotFoundException
    {	// strength
		String strengthStr = root.getAttribute(XmlTools.ATT_STRENGTH).trim();
		float strength;
		if(strengthStr.equals(AbstractAbility.MAXIMUM_VALUE))
			strength = Float.MAX_VALUE;
		else
			strength = Float.parseFloat(strengthStr);
		// frame
		boolean frame = Boolean.parseBoolean(root.getAttribute(XmlTools.ATT_FRAME));
		// name
		Element nameElt = XmlTools.getChildElement(root,XmlTools.ELT_NAME);
		String name = nameElt.getAttribute(XmlTools.ATT_VALUE).trim();
		// result
		StateModulation result = new StateModulation(name);
    	result.setFrame(frame);
    	result.setStrength(strength);
		result.setGestureName(gestureName);
		return result;
    }
		
    private static AbstractActionPermission loadActionPermissionElement(String type, String gestureName, Element root, Level level) throws IOException, ClassNotFoundException
    {	// strength
		String strengthStr = root.getAttribute(XmlTools.ATT_STRENGTH).trim();
		float strength;
		if(strengthStr.equals(AbstractAbility.MAXIMUM_VALUE))
			strength = Float.MAX_VALUE;
		else
			strength = Float.parseFloat(strengthStr);
		// frame
		boolean frame = Boolean.parseBoolean(root.getAttribute(XmlTools.ATT_FRAME));
		// action
		Element actionElt = XmlTools.getChildElement(root,XmlTools.ELT_ACTION);
		GeneralAction action = GeneralActionLoader.loadActionElement(actionElt);
    	// actor restrictions
		ArrayList<AbstractAbility> actorRestrictions = new ArrayList<AbstractAbility>();
		if(XmlTools.hasChildElement(root,XmlTools.ELT_ACTOR_RESTRICTIONS))
		{	Element actorRestrElt = XmlTools.getChildElement(root,XmlTools.ELT_ACTOR_RESTRICTIONS);
			actorRestrictions = AbilityLoader.loadAbilitiesElement(actorRestrElt,level);
		}
    	// target restrictions
		ArrayList<AbstractAbility> targetRestrictions = new ArrayList<AbstractAbility>();
		if(XmlTools.hasChildElement(root,XmlTools.ELT_TARGET_RESTRICTIONS))
		{	Element targetRestrElt = XmlTools.getChildElement(root,XmlTools.ELT_TARGET_RESTRICTIONS);
			targetRestrictions = AbilityLoader.loadAbilitiesElement(targetRestrElt,level);
		}
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
