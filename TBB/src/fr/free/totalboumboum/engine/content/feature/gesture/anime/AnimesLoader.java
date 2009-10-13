package fr.free.totalboumboum.engine.content.feature.gesture.anime;

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.RoundVariables;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ImageShift;
import fr.free.totalboumboum.engine.content.feature.gesture.Gesture;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

public class AnimesLoader
{	
	public static void loadAnimes(String folderPath, GesturePack pack, HashMap<GestureName,GestureName> animesReplacements) throws IOException, ParserConfigurationException, SAXException
	{	loadAnimes(folderPath,pack,null,animesReplacements);
	}
	
	public static void loadAnimes(String folderPath, GesturePack pack, PredefinedColor color, HashMap<GestureName,GestureName> animesReplacements) throws IOException, ParserConfigurationException, SAXException
	{	pack.setColor(color);
		File dataFile = new File(folderPath+File.separator+FileTools.FILE_ANIMES+FileTools.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FileTools.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ANIMES+FileTools.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadAnimesElement(root,folderPath,pack,color);
			// completing
			completeAnimes(pack,animesReplacements);
		}
	}
    
    private static void loadAnimesElement(Element root, String individualFolder, GesturePack pack, PredefinedColor color) throws IOException, ParserConfigurationException, SAXException
    {	HashMap<String,BufferedImage>images = new HashMap<String, BufferedImage>();
    	HashMap<String,BufferedImage>shadows = new HashMap<String, BufferedImage>();
    	Colormap colormap = null;
    	String colorFolder = null;
		
    	// local folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// scale
		double scale = 1;
		attribute = root.getAttribute(XmlTools.ATT_SCALE);
		if(attribute!=null)
			scale = Double.parseDouble(attribute.getValue());
		pack.setScale(scale);
		
		// bound height
		double boundHeight = 0;
		double zoomFactor = RoundVariables.zoomFactor;
		attribute = root.getAttribute(XmlTools.ATT_BOUND_HEIGHT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			boundHeight = zoomFactor*temp/scale;
		}
		
		// colors ?
		pack.setColor(color);
		Object obj;
		Element elt = root.getChild(XmlTools.ELT_COLORS);
		if(elt!=null && color!=null)
		{	obj = ImageTools.loadColorsElement(elt,localFilePath,color);
			if(obj instanceof Colormap)
				colormap = (Colormap)obj;
			else
				colorFolder = (String)obj;
		}
		
		if(colorFolder!=null)
			localFilePath = localFilePath+File.separator + colorFolder;
		
		// shadows ?
		elt = root.getChild(XmlTools.ELT_SHADOWS);
		if(elt!=null)
			loadShadowsElement(elt,localFilePath,pack,images,shadows,colormap,zoomFactor,scale);
		
		// gestures
		Element gestures = root.getChild(XmlTools.ELT_GESTURES);
		loadGesturesElement(gestures,boundHeight,localFilePath,pack,images,shadows,colormap,zoomFactor,scale);
	}
    
    @SuppressWarnings("unchecked")
	private static void loadShadowsElement(Element root, String individualFolder, GesturePack pack,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// shadows
    	List<Element> shdws = root.getChildren(XmlTools.ELT_SHADOW);
    	Iterator<Element> i = shdws.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
    		loadShadowElement(tp,localFilePath,pack,images,shadows,colormap,zoomFactor,scale);
    	}
    }
    
    private static void loadShadowElement(Element root, String individualFolder, GesturePack pack,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	// file
    	String localPath = individualFolder+File.separator;
    	localPath = localPath + root.getAttribute(XmlTools.ATT_FILE).getValue().trim();
    	BufferedImage shadow = loadImage(localPath,images,colormap,zoomFactor,scale);
    	
    	// name
    	String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		
    	// result
    	shadows.put(name,shadow);
    }
    
    @SuppressWarnings("unchecked")
	private static void loadGesturesElement(Element root, double boundHeight, String filePath, GesturePack pack,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	// folder
    	String localFilePath = filePath;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// gestures
    	List<Element> gesturesList = root.getChildren();
    	Iterator<Element> i = gesturesList.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
			loadGestureElement(tp,pack,boundHeight,localFilePath,images,shadows,colormap,zoomFactor,scale);
    	}
    }
    
    /**
     * load a gesture (and if required all the associated directions) 
     */
    @SuppressWarnings("unchecked")
	private static void loadGestureElement(Element root, GesturePack pack, double boundHeight, String filePath,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	// name
    	String name = root.getAttribute(XmlTools.ATT_NAME).getValue().toUpperCase(Locale.ENGLISH);
		GestureName gestureName = GestureName.valueOf(name);
    	Gesture gesture = pack.getGesture(gestureName);
    	
    	// images folder
    	String localFilePath = filePath;
    	Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
    	if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
    	// repeat flag
		String repeatStr = root.getAttribute(XmlTools.ATT_REPEAT).getValue();
		boolean repeat = false;
		if(!repeatStr.equals(""))
			repeat = Boolean.parseBoolean(repeatStr);
		
		// proportional flag
		boolean proportional = false;
		attribute = root.getAttribute(XmlTools.ATT_PROPORTIONAL);
		if(attribute!=null)
			proportional = Boolean.parseBoolean(attribute.getValue());
		
		// horizontal shift
		double xShift = 0;
		attribute = root.getAttribute(XmlTools.ATT_XSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			xShift = zoomFactor*temp/scale;
		}
		// vertical shift
		double yShift = 0;
		attribute = root.getAttribute(XmlTools.ATT_YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			yShift = zoomFactor*temp/scale;
		}
		
		// shadow
		BufferedImage shadow = null;
		attribute = root.getAttribute(XmlTools.ATT_SHADOW);
		if(attribute!=null)
		{	shadow = shadows.get(attribute.getValue().trim());
			if(shadow==null)
			{	String imgPath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_SHADOW).getValue().trim();
				shadow = loadImage(imgPath,images,colormap,zoomFactor,scale);
			}
		}
		
		// shadow horizontal shift
		double shadowXShift = 0;
		attribute = root.getAttribute(XmlTools.ATT_SHADOW_XSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowXShift = zoomFactor*temp/scale;
		}
		
		// shadow vertical shift
		double shadowYShift = 0;
		attribute = root.getAttribute(XmlTools.ATT_SHADOW_YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowYShift = zoomFactor*temp/scale;
		}
		
		// bound shift
		ImageShift boundYShift = ImageShift.DOWN;
		attribute = root.getAttribute(XmlTools.ATT_BOUND_YSHIFT);
		if(attribute!=null)
			boundYShift = ImageShift.valueOf(attribute.getValue().trim().toUpperCase(Locale.ENGLISH));
		
		// directions
		List<Element> directionsList = root.getChildren(XmlTools.ELT_DIRECTION);
    	Iterator<Element> i = directionsList.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
			AnimeDirection animeDirection = loadDirectionElement(gestureName,boundHeight,repeat,proportional,tp,localFilePath,xShift,yShift,
					shadow,shadowXShift,shadowYShift,boundYShift
					,images,shadows,colormap,zoomFactor,scale);
			gesture.addAnimeDirection(animeDirection,animeDirection.getDirection());
		}
    	completeDirections(gesture);
    }
    
    /**
     * load a direction for a given gesture
     */
    @SuppressWarnings("unchecked")
	private static AnimeDirection loadDirectionElement(GestureName gestureName, double boundHeight, boolean repeat, boolean proportional, 
    		Element root, String filePath, 
    		double xShift, double yShift, 
    		BufferedImage shadow, double shadowXShift, double shadowYShift, ImageShift boundYShift,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	AnimeDirection result = new AnimeDirection();
		
    	// direction
		String strDirection = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		Direction direction = Direction.NONE;
		if(!strDirection.equals(""))
			direction = Direction.valueOf(strDirection.toUpperCase(Locale.ENGLISH));
    	result.setDirection(direction);
    	result.setGestureName(gestureName);
    	result.setBoundHeight(boundHeight);
    	result.setRepeat(repeat);
    	result.setProportional(proportional);
		
    	// folder
    	String localFilePath = filePath;
    	Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
    	if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
    	// horizontal shift
		attribute = root.getAttribute(XmlTools.ATT_XSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			xShift = zoomFactor*temp/scale;
		}
		
		// vertical shift
		attribute = root.getAttribute(XmlTools.ATT_YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			yShift = zoomFactor*temp/scale;
		}
		
		// shadow
		attribute = root.getAttribute(XmlTools.ATT_SHADOW);
		if(attribute!=null)
		{	shadow = shadows.get(attribute.getValue().trim());
			if(shadow==null)
			{	String imgPath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_SHADOW).getValue().trim();
				shadow = loadImage(imgPath,images,colormap,zoomFactor,scale);
			}
		}
		
		// shadow horizontal shift
		attribute = root.getAttribute(XmlTools.ATT_SHADOW_XSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowXShift = zoomFactor*temp/scale;
		}
		
		// shadow vertical shift
		attribute = root.getAttribute(XmlTools.ATT_SHADOW_YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowYShift = zoomFactor*temp/scale;
		}
		
		// bound shift
		attribute = root.getAttribute(XmlTools.ATT_BOUND_YSHIFT);
		if(attribute!=null)
			boundYShift = ImageShift.valueOf(attribute.getValue().trim());
    	
		// steps
	    List<Element> stepsList = root.getChildren(XmlTools.ELT_STEP);
    	Iterator<Element> i = stepsList.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
    		AnimeStep animeStep = loadStepElement(tp,localFilePath,xShift,yShift,shadow,shadowXShift,shadowYShift,boundYShift,images,shadows,colormap,zoomFactor,scale);
    		result.add(animeStep);
    	}
    	return result;
    }
    
    /**
     * load a step of an animation
     */
    private static AnimeStep loadStepElement(Element root, String filePath, 
    		double xShift, double yShift, 
    		BufferedImage shadow, double shadowXShift, double shadowYShift, ImageShift boundYShift,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	AnimeStep result = new AnimeStep();    	
    	
    	// duration
    	int duration = 0;
    	Attribute attribute = root.getAttribute(XmlTools.ATT_DURATION);
    	if(attribute!=null)
    		duration = Integer.parseInt(attribute.getValue());
    	
    	// image
    	BufferedImage img = null;    	
    	attribute = root.getAttribute(XmlTools.ATT_FILE);
    	if(attribute!=null)
    	{	String strImage = attribute.getValue().trim();
    		String imgPath = filePath+File.separator+strImage;
    		img = loadImage(imgPath,images,colormap,zoomFactor,scale);
    	}
		
    	// horizontal shift
		attribute = root.getAttribute(XmlTools.ATT_XSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			xShift = zoomFactor*temp/scale;
		}
		
		// vertical shift
		attribute = root.getAttribute(XmlTools.ATT_YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			yShift = zoomFactor*temp/scale;
		}
		
		// shadow
		attribute = root.getAttribute(XmlTools.ATT_SHADOW);
		if(attribute!=null)
		{	shadow = shadows.get(attribute.getValue().trim());
			if(shadow==null)
			{	String imgPath = filePath+File.separator+attribute.getValue().trim();
				shadow = loadImage(imgPath,images,colormap,zoomFactor,scale);
			}
		}
		
		// shadow horizontal shift
		attribute = root.getAttribute(XmlTools.ATT_SHADOW_XSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowXShift = zoomFactor*temp/scale;
		}
		
		// shadow vertical shift
		attribute = root.getAttribute(XmlTools.ATT_SHADOW_YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowYShift = zoomFactor*temp/scale;
		}
		
		// bound shift
		attribute = root.getAttribute(XmlTools.ATT_BOUND_YSHIFT);
		if(attribute!=null)
			boundYShift = ImageShift.valueOf(attribute.getValue().trim());
		
		// anime
		result.setDuration(duration);
		result.setImage(img);
		result.setXShift(xShift);
		result.setYShift(yShift);
		result.setShadow(shadow);
		result.setShadowXShift(shadowXShift);
		result.setShadowYShift(shadowYShift);
		result.setBoundYShift(boundYShift);
		return result;
    }	
   
    private static BufferedImage loadImage(String imgPath,
    		HashMap<String,BufferedImage> images, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	BufferedImage result;
    	if(images.containsKey(imgPath))
    		result = images.get(imgPath);
    	else
    	{	BufferedImage imgOld = ImageTools.loadImage(imgPath,colormap);
    	 	double zoom = zoomFactor/scale;
			result = ImageTools.resize(imgOld,zoom,Configuration.getVideoConfiguration().getSmoothGraphics());
			images.put(imgPath,result);
    	}
    	return result;
    }
    
    /**
     * complete the missing gestures (i.e those not defined in the XML file but
     * necessary for the game) by using the existing ones.
     * @param pack
     * @param animesReplacements
     */
    private static void completeAnimes(GesturePack pack, HashMap<GestureName,GestureName> animesReplacements)
    {	for(Entry<GestureName,GestureName> e: animesReplacements.entrySet())
    	{	GestureName gest = e.getKey();
    		GestureName repl = e.getValue();
    		completeAnime(pack,animesReplacements,gest,repl);
    	}    	
    }
    
    private static void completeAnime(GesturePack pack, HashMap<GestureName,GestureName> animesReplacements, GestureName gestName, GestureName replName)
    {	if(replName!=null)
    	{	Gesture gesture = pack.getGesture(gestName);
	    	// create the gesture if necessary
	    	if(gesture==null)
	    	{	gesture = new Gesture();
	    		gesture.setName(gestName);
	    	}
	    	// complete its animes if necessary
	    	if(gesture.hasNoAnimes())
	    	{	// get the replacement animes
	    		GestureName replName2 = animesReplacements.get(replName);
	        	// TODO if a compulsory anime is missing, should be detected here (except for abstract sprite, where it doesn't matter)
	    		if(replName2!=null)
					completeAnime(pack,animesReplacements,replName,replName2);
				Gesture replacement = pack.getGesture(replName);
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
	private static void completeDirections(Gesture gesture)
	{	if(!gesture.hasNoAnimes())
		{	Direction directions[] = Direction.values();
			for(Direction direction: directions)
				completeDirection(gesture,direction);
		}
    }
    
    private static void completeDirection(Gesture gesture, Direction direction)
    {	if(gesture.getAnimeDirection(direction)==null)
    	{	if(direction==Direction.NONE)
		    {	AnimeDirection anime = gesture.getAnimeDirection(Direction.DOWN);
		    	if(anime==null)
	    		{	anime = new AnimeDirection();
	    			anime.setDirection(direction);
	    			anime.setGestureName(gesture.getName());
	    			AnimeStep as = new AnimeStep();
	    			anime.add(as);
	    		}
		    	gesture.addAnimeDirection(anime,Direction.NONE);
		    }
	    	else if(direction.isPrimary())
	    	{	AnimeDirection anime = gesture.getAnimeDirection(Direction.NONE);
	    		gesture.addAnimeDirection(anime,direction);
	    	}
	    	else // composite
	    	{	Direction primary = direction.getHorizontalPrimary();
	    		completeDirection(gesture,primary);
	    		AnimeDirection anime = gesture.getAnimeDirection(primary);
	    		gesture.addAnimeDirection(anime,direction);
	    	}
	    }
    }    
}
