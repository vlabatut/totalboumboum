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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ImageShift;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.feature.gesture.anime.color.Colormap;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.feature.gesture.anime.step.AnimeStep;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.ImageTools;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

public class AnimesLoader
{	
	public static void loadAnimes(String folderPath, GesturePack pack, HashMap<GestureName,GestureName> animesReplacements) throws IOException, ParserConfigurationException, SAXException
	{	loadAnimes(folderPath,pack,null,animesReplacements);
	}
	
	public static void loadAnimes(String folderPath, GesturePack pack, PredefinedColor color, HashMap<GestureName,GestureName> animesReplacements) throws IOException, ParserConfigurationException, SAXException
	{	pack.setColor(color);
		File dataFile = new File(folderPath+File.separator+FileNames.FILE_ANIMES+FileNames.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FilePaths.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ANIMES+FileNames.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadAnimesElement(root,folderPath,pack,color);
			// completing
			completeAnimes(pack,animesReplacements);
		}
	}
    
    private static void loadAnimesElement(Element root, String individualFolder, GesturePack pack, PredefinedColor color) throws IOException, ParserConfigurationException, SAXException
    {	HashMap<String,String>imagesFilenames = new HashMap<String, String>();
    	HashMap<String,String>shadowsFilenames = new HashMap<String, String>();
    	Colormap colormap = null;
    	String colorFolder = null;
		
    	// local folder
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
		
		// zoom
		double zoom = RoundVariables.zoomFactor/scale; //TODO no need for that anymore, since it's now resized only after loading
	//	zoom = 1/scale;
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		if(engineConfiguration.isSpriteMemoryCached() || engineConfiguration.isSpriteFileCached())
			zoom = 1;
		
		// bound height
		double boundHeight = 0;
		attribute = root.getAttribute(XmlNames.BOUND_HEIGHT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			boundHeight = zoom*temp;
		}
		
		// colors ?
		pack.setColor(color);
		Object obj;
		Element elt = root.getChild(XmlNames.COLORS);
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
		elt = root.getChild(XmlNames.SHADOWS);
		if(elt!=null)
			loadShadowsElement(elt,localFilePath,shadowsFilenames);
		
		// images ?
		elt = root.getChild(XmlNames.IMAGES);
		if(elt!=null)
			loadImagesElement(elt,localFilePath,imagesFilenames,colormap);
		
		// gestures
		Element gestures = root.getChild(XmlNames.GESTURES);
		loadGesturesElement(gestures,boundHeight,localFilePath,pack,shadowsFilenames,imagesFilenames,colormap,zoom);
	}
    
	@SuppressWarnings("unchecked")
	private static void loadImagesElement(Element root, String individualFolder,
    		HashMap<String,String> imagesFilenames, Colormap colormap) throws IOException
    {	// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// images
    	List<Element> imgs = root.getChildren(XmlNames.IMAGE);
    	Iterator<Element> i = imgs.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
    		loadImageElement(tp,localFilePath,imagesFilenames,colormap);
    	}
    }
    
    private static void loadImageElement(Element root, String individualFolder,
    		HashMap<String,String> imagesFilenames, Colormap colormap) throws IOException
    {	// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// file
    	String localPath = localFilePath+File.separator;
    	localPath = localPath + root.getAttribute(XmlNames.FILE).getValue().trim();
    	
    	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
    	// result
    	imagesFilenames.put(name,localPath);
    }
    
    @SuppressWarnings("unchecked")
	private static void loadShadowsElement(Element root, String individualFolder,
    		HashMap<String,String> shadowsFilenames) throws IOException
    {	// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// shadows
    	List<Element> shdws = root.getChildren(XmlNames.SHADOW);
    	Iterator<Element> i = shdws.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
    		loadShadowElement(tp,localFilePath,shadowsFilenames);
    	}
    }
    
    private static void loadShadowElement(Element root, String individualFolder,
    		HashMap<String,String> shadowsFilenames) throws IOException
    {	// file
    	String localPath = individualFolder+File.separator;
    	localPath = localPath + root.getAttribute(XmlNames.FILE).getValue().trim();
    	
    	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
    	// result
    	shadowsFilenames.put(name,localPath);
    }
    
    @SuppressWarnings("unchecked")
	private static void loadGesturesElement(Element root, double boundHeight, String filePath, GesturePack pack,
			HashMap<String,String> shadowsFilenames, HashMap<String,String> imagesFilenames, Colormap colormap,
    		double zoom) throws IOException
    {	// folder
    	String localFilePath = filePath;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// gestures
    	List<Element> gesturesList = root.getChildren();
    	Iterator<Element> i = gesturesList.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
			loadGestureElement(tp,pack,boundHeight,localFilePath,shadowsFilenames,imagesFilenames,colormap,zoom);
    	}
    }
    
    /**
     * load a gesture (and if required all the associated directions) 
     */
    @SuppressWarnings("unchecked")
	private static void loadGestureElement(Element root, GesturePack pack, double boundHeight, String filePath,
    		HashMap<String,String> shadowsFilenames, HashMap<String,String> imagesFilenames, Colormap colormap,
    		double zoom) throws IOException
    {	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue().toUpperCase(Locale.ENGLISH);
		GestureName gestureName = GestureName.valueOf(name);
    	Gesture gesture = pack.getGesture(gestureName);
    	
    	// images folder
    	String localFilePath = filePath;
    	Attribute attribute = root.getAttribute(XmlNames.FOLDER);
    	if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
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
		{	double temp = Double.parseDouble(attribute.getValue());
			xShift = zoom*temp;
		}
		// vertical shift
		double yShift = 0;
		attribute = root.getAttribute(XmlNames.YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			yShift = zoom*temp;
		}
		
		// shadow
		String shadowFilename = null;
		attribute = root.getAttribute(XmlNames.SHADOW);
		if(attribute!=null)
		{	shadowFilename = shadowsFilenames.get(attribute.getValue().trim());
			if(shadowFilename==null)
			{	shadowFilename = localFilePath+File.separator+root.getAttribute(XmlNames.SHADOW).getValue().trim();
			}
		}
		
		// shadow horizontal shift
		double shadowXShift = 0;
		attribute = root.getAttribute(XmlNames.SHADOW_XSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowXShift = zoom*temp;
		}
		
		// shadow vertical shift
		double shadowYShift = 0;
		attribute = root.getAttribute(XmlNames.SHADOW_YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowYShift = zoom*temp;
		}
		
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
			AnimeDirection animeDirection = loadDirectionElement(gestureName,boundHeight,repeat,proportional,tp,localFilePath,xShift,yShift,
					shadowFilename,shadowXShift,shadowYShift,boundYShift
					,shadowsFilenames,imagesFilenames,colormap,zoom);
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
    		String shadowFilename, double shadowXShift, double shadowYShift, ImageShift boundYShift,
    		HashMap<String,String> shadowsFilenames, HashMap<String,String> imagesFilenames, Colormap colormap,
    		double zoom) throws IOException
    {	AnimeDirection result = new AnimeDirection();
		
    	// direction
		String strDirection = root.getAttribute(XmlNames.NAME).getValue().trim();
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
    	Attribute attribute = root.getAttribute(XmlNames.FOLDER);
    	if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
    	// horizontal shift
		attribute = root.getAttribute(XmlNames.XSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			xShift = zoom*temp;
		}
		
		// vertical shift
		attribute = root.getAttribute(XmlNames.YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			yShift = zoom*temp;
		}
		
		// shadow
		attribute = root.getAttribute(XmlNames.SHADOW);
		if(attribute!=null)
		{	shadowFilename = shadowsFilenames.get(attribute.getValue().trim());
			if(shadowFilename==null)
			{	shadowFilename = filePath+File.separator+attribute.getValue().trim();
			}
		}
		
		// shadow horizontal shift
		attribute = root.getAttribute(XmlNames.SHADOW_XSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowXShift = zoom*temp;
		}
		
		// shadow vertical shift
		attribute = root.getAttribute(XmlNames.SHADOW_YSHIFT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			shadowYShift = zoom*temp;
		}
		
		// bound shift
		attribute = root.getAttribute(XmlNames.BOUND_YSHIFT);
		if(attribute!=null)
			boundYShift = ImageShift.valueOf(attribute.getValue().trim());
    	
		// steps
	    List<Element> stepsList = root.getChildren(XmlNames.STEP);
    	Iterator<Element> i = stepsList.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
    		AnimeStep animeStep = loadStepElement(tp,localFilePath,xShift,yShift,imagesFilenames,shadowFilename,shadowXShift,shadowYShift,boundYShift,shadowsFilenames,colormap);
    		result.add(animeStep);
    	}
    	return result;
    }
    
    /**
     * load a step of an animation
     */
    @SuppressWarnings("unchecked")
	private static AnimeStep loadStepElement(Element root, String filePath, 
    		double xShift, double yShift, HashMap<String,String> imagesFilenames, 
    		String shadowFilename, double shadowXShift, double shadowYShift, ImageShift boundYShift,
    		HashMap<String,String> shadowsFilenames, Colormap colormap) throws IOException
    {	AnimeStep result = new AnimeStep();    	
    	
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
			xShift = Double.parseDouble(xShiftStr);
		}
		
		// vertical shift
		attribute = root.getAttribute(XmlNames.YSHIFT);
		if(attribute!=null)
		{	String yShiftStr = attribute.getValue();
			yShift = Double.parseDouble(yShiftStr);
		}
		
		// shadow
		attribute = root.getAttribute(XmlNames.SHADOW);
		if(attribute!=null)
		{	shadowFilename = shadowsFilenames.get(attribute.getValue().trim());
			if(shadowFilename==null)
			{	shadowFilename = filePath+File.separator+attribute.getValue().trim();
			}
		}
		result.setShadowFileName(shadowFilename);
		
		// shadow horizontal shift
		attribute = root.getAttribute(XmlNames.SHADOW_XSHIFT);
		if(attribute!=null)
		{	String shadowXShiftStr = attribute.getValue();
			shadowXShift = Double.parseDouble(shadowXShiftStr);
		}
		result.setShadowXShift(shadowXShift);
		
		// shadow vertical shift
		attribute = root.getAttribute(XmlNames.SHADOW_YSHIFT);
		if(attribute!=null)
		{	String shadowYShiftStr = attribute.getValue();
			shadowYShift = Double.parseDouble(shadowYShiftStr);
		}
		result.setShadowYShift(shadowYShift);
		
		// bound shift
		attribute = root.getAttribute(XmlNames.BOUND_YSHIFT);
		if(attribute!=null)
			boundYShift = ImageShift.valueOf(attribute.getValue().trim());
		result.setBoundYShift(boundYShift);
		
		// default image
		attribute = root.getAttribute(XmlNames.FILE);
		if(attribute!=null)
		{	String strImage = attribute.getValue().trim();
			String imageFileName = filePath+File.separator+strImage;
			result.addImageFileName(imageFileName,xShift,yShift,colormap);
		}
		attribute = root.getAttribute(XmlNames.NAME);
		if(attribute!=null)
		{	String strImage = attribute.getValue().trim();
			String imageFileName = imagesFilenames.get(strImage);
			result.addImageFileName(imageFileName,xShift,yShift,colormap);
		}
		
		// other images
		List<Element> imagesElt = root.getChildren();
		for(Element imageElt: imagesElt)
			loadImageStepElement(imageElt,filePath,imagesFilenames,xShift,yShift,colormap,result);
			
		return result;
    }	

    private static void loadImageStepElement(Element root, String filePath, 
    		HashMap<String,String> imagesFilenames, 
    		double xShift, double yShift, Colormap colormap, AnimeStep result) throws IOException
	{
    	// horizontal shift
    	Attribute attribute = root.getAttribute(XmlNames.XSHIFT);
		if(attribute!=null)
		{	String xShiftStr = attribute.getValue();
			xShift = Double.parseDouble(xShiftStr);
		}
		
		// vertical shift
		attribute = root.getAttribute(XmlNames.YSHIFT);
		if(attribute!=null)
		{	String yShiftStr = attribute.getValue();
			yShift = Double.parseDouble(yShiftStr);
		}
		
		// image
    	attribute = root.getAttribute(XmlNames.FILE);
		if(attribute!=null)
		{	String strImage = attribute.getValue().trim();
			String imageFileName = filePath+File.separator+strImage;
			result.addImageFileName(imageFileName,xShift,yShift,colormap);
		}
		else
		{	attribute = root.getAttribute(XmlNames.NAME);
			if(attribute!=null)
			{	String strImage = attribute.getValue().trim();
				String imageFileName = imagesFilenames.get(strImage);
				result.addImageFileName(imageFileName,xShift,yShift,colormap);
			}
		}
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
