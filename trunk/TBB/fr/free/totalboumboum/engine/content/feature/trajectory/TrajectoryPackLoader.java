package fr.free.totalboumboum.engine.content.feature.trajectory;

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
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ImageShift;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class TrajectoryPackLoader
{	
/*	
	private String type;
	private double zoomFactor;
*/
	public static TrajectoryPack loadTrajectoryPack(String individualFolder, Level level) throws ParserConfigurationException, SAXException, IOException
	{	TrajectoryPack result = new TrajectoryPack();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_TRAJECTORIES+FileTools.EXTENSION_DATA);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FileTools.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_TRAJECTORIES+FileTools.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadTrajectories(root,level,result);
		}
		return result;
	}
	
    private static void loadTrajectories(Element root, Level level, TrajectoryPack result) throws IOException
	{	ArrayList<Element> gesturesList = XmlTools.getChildElements(root);
		Iterator<Element> i = gesturesList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			TrajectoryGesture trajectoryGesture = loadGestureElement(tp,level);
			result.addTrajectoryGesture(trajectoryGesture);
		}
	}
    
    /**
     * load a gesture (and if required all the associated directions) 
     */
    private static TrajectoryGesture loadGestureElement(Element root, Level level) throws IOException
    {	TrajectoryGesture result = new TrajectoryGesture();
    	double zoomFactor = level.getLoop().getZoomFactor();
    	// name
    	String gestureName = root.getAttribute(XmlTools.ATT_NAME);
    	result.setName(gestureName);
    	// repeat flag
		String repeatStr = root.getAttribute(XmlTools.ATT_REPEAT);
		boolean repeat = false;
		if(!repeatStr.equals(""))
			repeat = Boolean.parseBoolean(repeatStr);
		// X interaction coefficient
		double xInteraction = 0;
		if(root.hasAttribute(XmlTools.ATT_XINTERACTION))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_XINTERACTION).trim());
			xInteraction = zoomFactor*temp;
		}
		// Y interaction coefficient
		double yInteraction = 0;
		if(root.hasAttribute(XmlTools.ATT_YINTERACTION))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_YINTERACTION).trim());
			yInteraction = zoomFactor*temp;
		}
		// proportional flag
		boolean proportional = false;
		if(root.hasAttribute(XmlTools.ATT_PROPORTIONAL))
			proportional = Boolean.parseBoolean(root.getAttribute(XmlTools.ATT_PROPORTIONAL));
		// list of directions
		ArrayList<Element> directionsList = XmlTools.getChildElements(root,XmlTools.ELT_DIRECTION);
		Iterator<Element> i = directionsList.iterator();
		while(i.hasNext())
		{	Element tp = i.next();
			TrajectoryDirection trajectoryDirection = loadDirectionElement(gestureName,tp,zoomFactor,repeat,xInteraction,yInteraction,proportional);
			result.addTrajectoryDirection(trajectoryDirection);
		}
		return result;
    }
		
    /**
     * load a direction for a given gesture
     */
    private static TrajectoryDirection loadDirectionElement(String gestureName, Element root,double zoomFactor,
    		boolean repeat, double xInteraction, double yInteraction, boolean proportional) throws IOException
    {	TrajectoryDirection result = new TrajectoryDirection();
    	result.setRepeat(repeat);
    	result.setGestureName(gestureName);
    	result.setXInteraction(xInteraction);
    	result.setYInteraction(yInteraction);
		result.setProportional(proportional);
    	// direction
    	String strDirection = root.getAttribute(XmlTools.ATT_NAME).trim();
    	Direction direction = Direction.NONE;
		if(!strDirection.equals(""))
			direction = Direction.valueOf(strDirection.toUpperCase());
		result.setDirection(direction);
		// forced position
		if(XmlTools.hasChildElement(root, XmlTools.ELT_FORCE_POSITION))
		{	Element forcePosition = XmlTools.getChildElement(root, XmlTools.ELT_FORCE_POSITION);
			// time
			String strTime = forcePosition.getAttribute(XmlTools.ATT_TIME).trim();
			// direction
			result.setForcedPositionTime(Long.parseLong(strTime));
			// X position
			if(forcePosition.hasAttribute(XmlTools.ATT_XPOSITION))
			{	result.setForceXPosition(true);
				double temp = Double.parseDouble(forcePosition.getAttribute(XmlTools.ATT_XPOSITION).trim());
				result.setForcedXPosition(zoomFactor*temp);
			}
			// Y position
			if(forcePosition.hasAttribute(XmlTools.ATT_YPOSITION))
			{	result.setForceYPosition(true);
				double temp = Double.parseDouble(forcePosition.getAttribute(XmlTools.ATT_YPOSITION).trim());
				result.setForcedYPosition(zoomFactor*temp);
			}
			// Z position
			if(forcePosition.hasAttribute(XmlTools.ATT_ZPOSITION))
			{	result.setForceZPosition(true);
				double temp = Double.parseDouble(forcePosition.getAttribute(XmlTools.ATT_ZPOSITION).trim());
				result.setForcedZPosition(zoomFactor*temp);
			}
		}
		else
		{	result.setForceXPosition(false);
			result.setForceYPosition(false);
			result.setForceZPosition(false);
		}
		// steps
		if(XmlTools.hasChildElement(root, XmlTools.ELT_STEPS))
		{	Element steps = XmlTools.getChildElement(root,XmlTools.ELT_STEPS);
			ArrayList<Element> stepsList = XmlTools.getChildElements(steps, XmlTools.ELT_STEP);
			Iterator<Element> i = stepsList.iterator();
			while(i.hasNext())
			{	Element tp = i.next();
				TrajectoryStep trajectoryStep = loadStepElement(tp,zoomFactor);
				result.add(trajectoryStep);
			}
		}
		return result;
    }
    
    /**
     * load a step of an animation
     */
    private static TrajectoryStep loadStepElement(Element root, double zoomFactor) throws IOException
    {	TrajectoryStep result = new TrajectoryStep();
    	String strDuration = root.getAttribute(XmlTools.ATT_DURATION);
    	int duration = Integer.parseInt(strDuration);
	    //
    	double xShift = 0;
	    if(root.hasAttribute(XmlTools.ATT_XSHIFT))
	    {	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_XSHIFT).trim());
			xShift = zoomFactor*temp;
	    }
	    //
	    double yShift = 0;
	    if(root.hasAttribute(XmlTools.ATT_YSHIFT))
	    {	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_YSHIFT).trim());
			yShift = zoomFactor*temp;
	    }
	    //
	    double zShift = 0;
	    if(root.hasAttribute(XmlTools.ATT_ZSHIFT))
	    {	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_ZSHIFT).trim());
	    	zShift = zoomFactor*temp;
	    }
	    // bound shift
		ImageShift boundZShift = ImageShift.DOWN;
		if(root.hasAttribute(XmlTools.ATT_BOUND_ZSHIFT))
			boundZShift = ImageShift.valueOf(root.getAttribute(XmlTools.ATT_BOUND_ZSHIFT).trim().toUpperCase());
	    //
		result.setDuration(duration);
		result.setXShift(xShift);
		result.setYShift(yShift);
		result.setZShift(zShift);
		result.setBoundZShift(boundZShift);
		return result;
    }	
}
