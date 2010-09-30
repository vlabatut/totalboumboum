package org.totalboumboum.engine.content.feature.gesture.anime;

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
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ImageShift;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.HollowGesture;
import org.totalboumboum.engine.content.feature.gesture.HollowGesturePack;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRulesMap;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRulesMapLoader;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.HollowAnimeDirection;
import org.totalboumboum.engine.content.feature.gesture.anime.step.HollowAnimeStep;
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
public class HollowAnimesLoader
{	
	public static void loadAnimes(String folderPath, HollowGesturePack pack) throws IOException, ParserConfigurationException, SAXException
	{	File dataFile = new File(folderPath+File.separator+FileNames.FILE_ANIMES+FileNames.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FilePaths.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ANIMES+FileNames.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			
			// loading existing animes
			loadAnimesElement(root,folderPath,pack);
			
			// completing missing animes with replacement animes
			completeAnimes(pack);
		}
	}
    
    private static void loadAnimesElement(Element root, String individualFolder, HollowGesturePack pack) throws IOException, ParserConfigurationException, SAXException
    {	// local folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// scale
		double scale = 1;
		attribute = root.getAttribute(XmlNames.SCALE);
		if(attribute!=null)
			scale = Double.parseDouble(attribute.getValue());
		pack.setScale(scale);
		
		// bound height
		double boundHeight = 0;
		attribute = root.getAttribute(XmlNames.BOUND_HEIGHT);
		if(attribute!=null)
			boundHeight = Double.parseDouble(attribute.getValue())*scale;
		pack.setBoundHeight(boundHeight);
		
		// colors ?
		Element elt = root.getChild(XmlNames.COLORS);
		ColorRulesMap colorRulesMap;
		if(elt==null)
		{	colorRulesMap = new ColorRulesMap();
			colorRulesMap.setLocalPath(localFilePath);
		}
		else
		{	colorRulesMap = ColorRulesMapLoader.loadColorsElement(elt,localFilePath);
		}
		pack.setColorRulesMap(colorRulesMap);
		
		// shadows ?
		elt = root.getChild(XmlNames.SHADOWS);
		if(elt!=null)
			loadShadowsElement(elt,pack);

		// images ?
		elt = root.getChild(XmlNames.IMAGES);
		if(elt!=null)
			loadImagesElement(elt,pack);
		
		// gestures
		Element gestures = root.getChild(XmlNames.GESTURES);
		loadGesturesElement(gestures,pack);
	}
    
	@SuppressWarnings("unchecked")
	private static void loadImagesElement(Element root, HollowGesturePack pack) throws IOException
    {	// folder
    	String localFilePath = "";
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
			localFilePath = attribute.getValue()+File.separator;
		
		// images
    	List<Element> imgs = root.getChildren(XmlNames.IMAGE);
    	Iterator<Element> i = imgs.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
    		loadImageElement(tp,localFilePath,pack);
    	}
    }
    
    private static void loadImageElement(Element root, String individualFolder, HollowGesturePack pack) throws IOException
    {	// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+attribute.getValue()+File.separator;
		
		// file
    	String localPath = localFilePath + root.getAttribute(XmlNames.FILE).getValue().trim();
    	
    	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
    	// result
    	pack.addCommonImageFileName(name,localPath);
    }
    
    @SuppressWarnings("unchecked")
	private static void loadShadowsElement(Element root, HollowGesturePack pack) throws IOException
    {	// folder
    	String localFilePath = "";
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
			localFilePath = attribute.getValue()+File.separator;
		
		// shadows
    	List<Element> shdws = root.getChildren(XmlNames.SHADOW);
    	Iterator<Element> i = shdws.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
    		loadShadowElement(tp,localFilePath,pack);
    	}
    }
    
    private static void loadShadowElement(Element root, String individualFolder, HollowGesturePack pack) throws IOException
    {	// file
    	String localPath = individualFolder + root.getAttribute(XmlNames.FILE).getValue().trim();
    	
    	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
    	// result
    	pack.addShadowFileName(name,localPath);
    }
    
    @SuppressWarnings("unchecked")
	private static void loadGesturesElement(Element root, HollowGesturePack pack) throws IOException
    {	// folder
    	String localFilePath = "";
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+attribute.getValue()+File.separator;
		
		// gestures
    	List<Element> gesturesList = root.getChildren();
    	Iterator<Element> i = gesturesList.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
			loadGestureElement(tp,pack,localFilePath);
    	}
    }
    
    /**
     * load a gesture (and if required all the associated directions) 
     */
    @SuppressWarnings("unchecked")
	private static void loadGestureElement(Element root, HollowGesturePack pack, String filePath) throws IOException
    {	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue().toUpperCase(Locale.ENGLISH);
		GestureName gestureName = GestureName.valueOf(name);
    	HollowGesture gesture = pack.getGesture(gestureName);
    	
    	// images folder
    	String localFilePath = filePath;
    	Attribute attribute = root.getAttribute(XmlNames.FOLDER);
    	if(attribute!=null)
			localFilePath = localFilePath+attribute.getValue()+File.separator;
		
    	// repeat flag
		String repeatStr = root.getAttribute(XmlNames.REPEAT).getValue();
		boolean repeat = false;
		if(!repeatStr.equals(""))
			repeat = Boolean.parseBoolean(repeatStr);
		
		// proportional flag
		boolean proportional = false;
		attribute = root.getAttribute(XmlNames.PROPORTIONAL);
		if(attribute!=null)
			proportional = Boolean.parseBoolean(attribute.getValue());
		
		// horizontal shift
		double xShift = 0;
		attribute = root.getAttribute(XmlNames.XSHIFT);
		if(attribute!=null)
			xShift = Double.parseDouble(attribute.getValue())*pack.getScale();

		// vertical shift
		double yShift = 0;
		attribute = root.getAttribute(XmlNames.YSHIFT);
		if(attribute!=null)
			yShift = Double.parseDouble(attribute.getValue())*pack.getScale();
		
		// shadow
		String shadowName = null;
		attribute = root.getAttribute(XmlNames.SHADOW);
		if(attribute!=null)
		{	shadowName = attribute.getValue().trim();
			String shadowFilename = pack.getShadowFileName(shadowName);
			if(shadowFilename==null)
				shadowName = localFilePath+shadowName;
		}
		
		// shadow horizontal shift
		double shadowXShift = 0;
		attribute = root.getAttribute(XmlNames.SHADOW_XSHIFT);
		if(attribute!=null)
			shadowXShift = Double.parseDouble(attribute.getValue())*pack.getScale();
		
		// shadow vertical shift
		double shadowYShift = 0;
		attribute = root.getAttribute(XmlNames.SHADOW_YSHIFT);
		if(attribute!=null)
			shadowYShift = Double.parseDouble(attribute.getValue())*pack.getScale();
		
		// bound shift
		ImageShift boundYShift = ImageShift.DOWN;
		attribute = root.getAttribute(XmlNames.BOUND_YSHIFT);
		if(attribute!=null)
			boundYShift = ImageShift.valueOf(attribute.getValue().trim().toUpperCase(Locale.ENGLISH));
		
		// directions
		List<Element> directionsList = root.getChildren(XmlNames.DIRECTION);
    	Iterator<Element> i = directionsList.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
			HollowAnimeDirection animeDirection = loadDirectionElement(gestureName,pack,repeat,proportional,tp,localFilePath,xShift,yShift,
					shadowName,shadowXShift,shadowYShift,boundYShift);
			gesture.addAnimeDirection(animeDirection,animeDirection.getDirection());
		}
    	completeDirections(gesture);
    }
    
    /**
     * load a direction for a given gesture
     */
    @SuppressWarnings("unchecked")
	private static HollowAnimeDirection loadDirectionElement(GestureName gestureName, HollowGesturePack pack, boolean repeat, boolean proportional, 
    		Element root, String filePath, 
    		double xShift, double yShift, 
    		String shadowName, double shadowXShift, double shadowYShift, ImageShift boundYShift) throws IOException
    {	HollowAnimeDirection result = new HollowAnimeDirection();
		
    	// direction
		String strDirection = root.getAttribute(XmlNames.NAME).getValue().trim();
		Direction direction = Direction.NONE;
		if(!strDirection.equals(""))
			direction = Direction.valueOf(strDirection.toUpperCase(Locale.ENGLISH));
    	result.setDirection(direction);
    	result.setGestureName(gestureName);
    	result.setBoundHeight(pack.getBoundHeight());
    	result.setRepeat(repeat);
    	result.setProportional(proportional);
		
    	// folder
    	String localFilePath = filePath;
    	Attribute attribute = root.getAttribute(XmlNames.FOLDER);
    	if(attribute!=null)
			localFilePath = localFilePath+attribute.getValue()+File.separator;
		
    	// horizontal shift
		attribute = root.getAttribute(XmlNames.XSHIFT);
		if(attribute!=null)
			xShift = Double.parseDouble(attribute.getValue())*pack.getScale();
		
		// vertical shift
		attribute = root.getAttribute(XmlNames.YSHIFT);
		if(attribute!=null)
			yShift = Double.parseDouble(attribute.getValue())*pack.getScale();
		
		// shadow
		attribute = root.getAttribute(XmlNames.SHADOW);
		if(attribute!=null)
		{	shadowName = attribute.getValue().trim();
			String shadowFilename = pack.getShadowFileName(shadowName);
			if(shadowFilename==null)
				shadowName = localFilePath+shadowName;
		}
		
		// shadow horizontal shift
		attribute = root.getAttribute(XmlNames.SHADOW_XSHIFT);
		if(attribute!=null)
			shadowXShift = Double.parseDouble(attribute.getValue())*pack.getScale();
		
		// shadow vertical shift
		attribute = root.getAttribute(XmlNames.SHADOW_YSHIFT);
		if(attribute!=null)
			shadowYShift = Double.parseDouble(attribute.getValue())*pack.getScale();
		
		// bound shift
		attribute = root.getAttribute(XmlNames.BOUND_YSHIFT);
		if(attribute!=null)
			boundYShift = ImageShift.valueOf(attribute.getValue().trim());
    	
		// steps
	    List<Element> stepsList = root.getChildren(XmlNames.STEP);
    	Iterator<Element> i = stepsList.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
    		HollowAnimeStep animeStep = loadStepElement(tp,pack,localFilePath,xShift,yShift,shadowName,shadowXShift,shadowYShift,boundYShift);
    		result.add(animeStep);
    	}
    	return result;
    }
    
    /**
     * load a step of an animation
     */
    @SuppressWarnings("unchecked")
	private static HollowAnimeStep loadStepElement(Element root, HollowGesturePack pack, String filePath, 
    		double xShift, double yShift,
    		String shadowName, double shadowXShift, double shadowYShift, ImageShift boundYShift) throws IOException
    {	HollowAnimeStep result = new HollowAnimeStep();    	
    
    	// duration
    	int duration = 0;
    	Attribute attribute = root.getAttribute(XmlNames.DURATION);
    	if(attribute!=null)
    		duration = Integer.parseInt(attribute.getValue());
		result.setDuration(duration);
   	
    	// horizontal shift
		attribute = root.getAttribute(XmlNames.XSHIFT);
		if(attribute!=null)
		{	String xShiftStr = attribute.getValue();
			xShift = Double.parseDouble(xShiftStr)*pack.getScale();
		}
		
		// vertical shift
		attribute = root.getAttribute(XmlNames.YSHIFT);
		if(attribute!=null)
		{	String yShiftStr = attribute.getValue();
			yShift = Double.parseDouble(yShiftStr)*pack.getScale();
		}
		
		// shadow
		attribute = root.getAttribute(XmlNames.SHADOW);
		if(attribute!=null)
		{	shadowName = attribute.getValue().trim();
			String shadowFilename = pack.getShadowFileName(shadowName);
			if(shadowFilename==null)
				shadowName = filePath+attribute.getValue().trim();

			// shadow horizontal shift
			attribute = root.getAttribute(XmlNames.SHADOW_XSHIFT);
			if(attribute!=null)
			{	String shadowXShiftStr = attribute.getValue();
				shadowXShift = Double.parseDouble(shadowXShiftStr)*pack.getScale();
			}
			
			// shadow vertical shift
			attribute = root.getAttribute(XmlNames.SHADOW_YSHIFT);
			if(attribute!=null)
			{	String shadowYShiftStr = attribute.getValue();
				shadowYShift = Double.parseDouble(shadowYShiftStr)*pack.getScale();
			}
			
		}
		if(shadowName!=null)
		{	String shadowFilename = pack.getShadowFileName(shadowName);
			ColorRulesMap shadowColorRulesMap = pack.getShadowColorRulesMap(shadowName);
			if(shadowFilename==null)
			{	shadowFilename = shadowName;
				shadowColorRulesMap = pack.getColorRulesMap();
			}
			result.setShadow(shadowFilename,shadowXShift,shadowYShift,shadowColorRulesMap);
			//if(shadowFilename.equals("shadow.png"))
			//	System.out.println("shadowFilename==shadow.png");
		}
		
		// bound shift
		attribute = root.getAttribute(XmlNames.BOUND_YSHIFT);
		if(attribute!=null)
			boundYShift = ImageShift.valueOf(attribute.getValue().trim());
		result.setBoundYShift(boundYShift);
		
		// default image
		attribute = root.getAttribute(XmlNames.FILE);
		if(attribute!=null)
		{	String strImage = attribute.getValue().trim();
			String imageFileName = filePath+strImage;
			ColorRulesMap colorRulesMap = pack.getColorRulesMap();
			result.addImage(imageFileName,xShift,yShift,colorRulesMap);
		}
		attribute = root.getAttribute(XmlNames.NAME);
		if(attribute!=null)
		{	String key = attribute.getValue().trim();
			String imageFileName = pack.getCommonImageFileName(key);
			ColorRulesMap colorRulesMap = pack.getCommonImageRulesMap(key);
			result.addImage(imageFileName,xShift,yShift,colorRulesMap);
		}
		
		// other images
		List<Element> imagesElt = root.getChildren();
		for(Element imageElt: imagesElt)
			loadImageStepElement(imageElt,pack,filePath,xShift,yShift,result);
			
		return result;
    }	

    private static void loadImageStepElement(Element root, HollowGesturePack pack, String filePath, 
    		double xShift, double yShift, HollowAnimeStep result) throws IOException
	{
    	// horizontal shift
    	Attribute attribute = root.getAttribute(XmlNames.XSHIFT);
		if(attribute!=null)
		{	String xShiftStr = attribute.getValue();
			xShift = Double.parseDouble(xShiftStr)*pack.getScale();
		}
		
		// vertical shift
		attribute = root.getAttribute(XmlNames.YSHIFT);
		if(attribute!=null)
		{	String yShiftStr = attribute.getValue();
			yShift = Double.parseDouble(yShiftStr)*pack.getScale();
		}
		
		// image
    	attribute = root.getAttribute(XmlNames.FILE);
		if(attribute!=null)
		{	String strImage = attribute.getValue().trim();
			String imageFileName = filePath+strImage;
			ColorRulesMap colorRulesMap = pack.getColorRulesMap();
			result.addImage(imageFileName,xShift,yShift,colorRulesMap);
		}
		else
		{	attribute = root.getAttribute(XmlNames.NAME);
			if(attribute!=null)
			{	String key = attribute.getValue().trim();
				String imageFileName = pack.getCommonImageFileName(key);
				ColorRulesMap colorRulesMap = pack.getCommonImageRulesMap(key);
				result.addImage(imageFileName,xShift,yShift,colorRulesMap);
			}
		}
	}
    
    /**
     * complete the missing gestures (i.e those not defined in the XML file but
     * necessary for the game) by using the existing ones.
     * @param pack
     * @param animesReplacements
     */
    private static void completeAnimes(HollowGesturePack pack)
    {	for(Entry<GestureName,GestureName> e: pack.getAnimesReplacements().entrySet())
    	{	GestureName gest = e.getKey();
    		GestureName repl = e.getValue();
    		completeAnime(pack,gest,repl);
    	}    	
    }
    
    private static void completeAnime(HollowGesturePack pack, GestureName gestName, GestureName replName)
    {	if(replName!=null)
    	{	HollowGesture gesture = pack.getGesture(gestName);
	    	// create the gesture if necessary
	    	if(gesture==null)
	    	{	gesture = new HollowGesture();
	    		gesture.setName(gestName);
	    	}
	    	// complete its animes if necessary
	    	if(gesture.hasNoAnimes())
	    	{	// get the replacement animes
	    		GestureName replName2 = pack.getAnimesReplacements().get(replName);
	        	// TODO if a compulsory anime is missing, should be detected here (except for abstract sprite, where it doesn't matter)
	    		if(replName2!=null)
					completeAnime(pack,replName,replName2);
	    		HollowGesture replacement = pack.getGesture(replName);
				// set it in the considered gesture
				gesture.setAnimes(replacement);
	    	}
    	}
    }
    
    /**
     * complete the missing directions in a given gesture (i.e those not defined in the XML file but
     * necessary for the game) by using the existing ones.
     * @param pack
     * @param animesReplacements
     */
	private static void completeDirections(HollowGesture gesture)
	{	if(!gesture.hasNoAnimes())
		{	Direction directions[] = Direction.values();
			for(Direction direction: directions)
				completeDirection(gesture,direction);
		}
    }
    
    private static void completeDirection(HollowGesture gesture, Direction direction)
    {	if(gesture.getAnimeDirection(direction)==null)
    	{	if(direction==Direction.NONE)
		    {	HollowAnimeDirection anime = gesture.getAnimeDirection(Direction.DOWN);
		    	if(anime==null)
	    		{	anime = new HollowAnimeDirection();
	    			anime.setDirection(direction);
	    			anime.setGestureName(gesture.getName());
	    			HollowAnimeStep as = new HollowAnimeStep();
	    			anime.add(as);
	    		}
		    	gesture.addAnimeDirection(anime,Direction.NONE);
		    }
	    	else if(direction.isPrimary())
	    	{	HollowAnimeDirection anime = gesture.getAnimeDirection(Direction.NONE);
	    		gesture.addAnimeDirection(anime,direction);
	    	}
	    	else // composite
	    	{	Direction primary = direction.getHorizontalPrimary();
	    		completeDirection(gesture,primary);
	    		HollowAnimeDirection anime = gesture.getAnimeDirection(primary);
	    		gesture.addAnimeDirection(anime,direction);
	    	}
	    }
    }    
}
