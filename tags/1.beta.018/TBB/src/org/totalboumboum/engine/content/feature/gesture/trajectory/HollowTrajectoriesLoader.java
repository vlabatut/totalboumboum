package org.totalboumboum.engine.content.feature.gesture.trajectory;

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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ImageShift;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.HollowGesture;
import org.totalboumboum.engine.content.feature.gesture.HollowGesturePack;
import org.totalboumboum.engine.content.feature.gesture.trajectory.direction.HollowTrajectoryDirection;
import org.totalboumboum.engine.content.feature.gesture.trajectory.step.HollowTrajectoryStep;
import org.totalboumboum.game.round.RoundVariables;
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
public class HollowTrajectoriesLoader
{	
	public static void loadTrajectories(String individualFolder, HollowGesturePack pack) throws ParserConfigurationException, SAXException, IOException
	{	File dataFile = new File(individualFolder+File.separator+FileNames.FILE_TRAJECTORIES+FileNames.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FilePaths.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_TRAJECTORIES+FileNames.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadTrajectories(root,pack);
		}
	}
	
    @SuppressWarnings("unchecked")
	private static void loadTrajectories(Element root, HollowGesturePack pack) throws IOException
	{	List<Element> gesturesList = root.getChildren();
		Iterator<Element> i = gesturesList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			loadGestureElement(tp,pack);
		}
	}
    
    /**
     * load a gesture (and if required all the associated directions) 
     */
    @SuppressWarnings("unchecked")
	private static void loadGestureElement(Element root, HollowGesturePack pack) throws IOException
    {	// zoom
    	double zoomFactor = RoundVariables.zoomFactor;
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		if(engineConfiguration.isSpriteMemoryCached() || engineConfiguration.isSpriteFileCached())
			zoomFactor = 1;
    		
    	// name
		String name = root.getAttribute(XmlNames.NAME).getValue().toUpperCase(Locale.ENGLISH);
		GestureName gestureName = GestureName.valueOf(name);
		HollowGesture gesture = pack.getGesture(gestureName);
		
    	// repeat flag
		String repeatStr = root.getAttribute(XmlNames.REPEAT).getValue();
		boolean repeat = false;
		if(!repeatStr.equals(""))
			repeat = Boolean.parseBoolean(repeatStr);
		
		// X interaction coefficient
		double xInteraction = 0;
		Attribute attribute = root.getAttribute(XmlNames.XINTERACTION);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue().trim());
			xInteraction = zoomFactor*temp;
		}
		
		// Y interaction coefficient
		double yInteraction = 0;
		attribute = root.getAttribute(XmlNames.YINTERACTION);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue().trim());
			yInteraction = zoomFactor*temp;
		}
		
		// proportional flag
		boolean proportional = false;
		attribute = root.getAttribute(XmlNames.PROPORTIONAL);
		if(attribute!=null)
			proportional = Boolean.parseBoolean(attribute.getValue());
		
		// list of directions
		List<Element> directionsList = root.getChildren(XmlNames.DIRECTION);
		Iterator<Element> i = directionsList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			HollowTrajectoryDirection trajectoryDirection = loadDirectionElement(gestureName,tp,zoomFactor,repeat,xInteraction,yInteraction,proportional);
			gesture.addTrajectoryDirection(trajectoryDirection);
		}
    }
		
    /**
     * load a direction for a given gesture
     */
    @SuppressWarnings("unchecked")
	private static HollowTrajectoryDirection loadDirectionElement(GestureName gestureName, Element root,double zoomFactor,
    		boolean repeat, double xInteraction, double yInteraction, boolean proportional) throws IOException
    {	HollowTrajectoryDirection result = new HollowTrajectoryDirection();
    	result.setRepeat(repeat);
    	result.setGestureName(gestureName);
 		result.setProportional(proportional);
    	
		// direction
    	String strDirection = root.getAttribute(XmlNames.NAME).getValue().trim();
    	Direction direction = Direction.NONE;
		if(!strDirection.equals(""))
			direction = Direction.valueOf(strDirection.toUpperCase(Locale.ENGLISH));
		result.setDirection(direction);
		
		// interaction coefficients
		{	// X interaction coefficient
			Attribute attribute = root.getAttribute(XmlNames.XINTERACTION);
			if(attribute!=null)
			{	double temp = Double.parseDouble(attribute.getValue().trim());
				xInteraction = zoomFactor*temp;
			}
		   	result.setXInteraction(xInteraction);
			// Y interaction coefficient
			attribute = root.getAttribute(XmlNames.YINTERACTION);
			if(attribute!=null)
			{	double temp = Double.parseDouble(attribute.getValue().trim());
				yInteraction = zoomFactor*temp;
			}
	    	result.setYInteraction(yInteraction);
		}

		// forced position
		Element forcePosition = root.getChild(XmlNames.FORCE_POSITION);
		if(forcePosition!=null)
		{	// time
			String strTime = forcePosition.getAttribute(XmlNames.TIME).getValue().trim();
			// direction
			result.setForcedPositionTime(Long.parseLong(strTime));
			// X position
			Attribute attribute = forcePosition.getAttribute(XmlNames.XPOSITION);
			if(attribute!=null)
			{	result.setForceXPosition(true);
				double temp = Double.parseDouble(attribute.getValue().trim());
				result.setForcedXPosition(zoomFactor*temp);
			}
			// Y position
			attribute = forcePosition.getAttribute(XmlNames.YPOSITION);
			if(attribute!=null)
			{	result.setForceYPosition(true);
				double temp = Double.parseDouble(attribute.getValue().trim());
				result.setForcedYPosition(zoomFactor*temp);
			}
			// Z position
			attribute = forcePosition.getAttribute(XmlNames.ZPOSITION);
			if(attribute!=null)
			{	result.setForceZPosition(true);
				double temp = Double.parseDouble(attribute.getValue().trim());
				result.setForcedZPosition(zoomFactor*temp);
			}
		}
		else
		{	result.setForceXPosition(false);
			result.setForceYPosition(false);
			result.setForceZPosition(false);
		}
		
		// steps
		Element steps = root.getChild(XmlNames.STEPS);
		if(steps!=null)
		{	List<Element> stepsList = steps.getChildren(XmlNames.STEP);
			Iterator<Element> i = stepsList.iterator();
			while(i.hasNext())
			{	Element tp = i.next();
				HollowTrajectoryStep trajectoryStep = loadStepElement(tp,zoomFactor);
				result.add(trajectoryStep);
			}
		}
		
		return result;
    }
    
    /**
     * load a step of an animation
     */
    private static HollowTrajectoryStep loadStepElement(Element root, double zoomFactor) throws IOException
    {	HollowTrajectoryStep result = new HollowTrajectoryStep();
    	String strDuration = root.getAttribute(XmlNames.DURATION).getValue();
    	int duration = Integer.parseInt(strDuration);
	    //
    	double xShift = 0;
	    Attribute attribute = root.getAttribute(XmlNames.XSHIFT);
	    if(attribute!=null)
	    {	double temp = Double.parseDouble(attribute.getValue().trim());
			xShift = zoomFactor*temp;
	    }
	    //
	    double yShift = 0;
	    attribute = root.getAttribute(XmlNames.YSHIFT);
	    if(attribute!=null)
	    {	double temp = Double.parseDouble(attribute.getValue().trim());
			yShift = zoomFactor*temp;
	    }
	    //
	    double zShift = 0;
	    attribute = root.getAttribute(XmlNames.ZSHIFT);
	    if(attribute!=null)
	    {	double temp = Double.parseDouble(attribute.getValue().trim());
	    	zShift = zoomFactor*temp;
	    }
	    // bound shift
		ImageShift boundZShift = ImageShift.DOWN;
		attribute = root.getAttribute(XmlNames.BOUND_ZSHIFT);
	    if(attribute!=null)
			boundZShift = ImageShift.valueOf(attribute.getValue().trim().toUpperCase(Locale.ENGLISH));
	    //
		result.setDuration(duration);
		result.setXShift(xShift);
		result.setYShift(yShift);
		result.setZShift(zShift);
		result.setBoundZShift(boundZShift);
		return result;
    }	
}
