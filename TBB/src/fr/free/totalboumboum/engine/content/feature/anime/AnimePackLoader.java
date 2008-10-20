package fr.free.totalboumboum.engine.content.feature.anime;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ImageShift;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;


public class AnimePackLoader
{	
	public static AnimePack loadAnimePack(String folderPath, Level level) throws IOException, ParserConfigurationException, SAXException
	{	return loadAnimePack(folderPath,level,null);
	}
	
	public static AnimePack loadAnimePack(String folderPath, Level level, PredefinedColor color) throws IOException, ParserConfigurationException, SAXException
	{	AnimePack result = new AnimePack();
		result.setColor(color);
		File dataFile = new File(folderPath+File.separator+FileTools.FILE_ANIMES+FileTools.EXTENSION_DATA);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FileTools.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ANIMES+FileTools.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadAnimesElement(root,folderPath,level,color,result);
		}
		return result;
	}
    
    private static void loadAnimesElement(Element root,String individualFolder, Level level, PredefinedColor color, AnimePack result) throws IOException, ParserConfigurationException, SAXException
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
		result.setScale(scale);
		// bound height
		double boundHeight = 0;
		double zoomFactor = level.getLoop().getZoomFactor();
		attribute = root.getAttribute(XmlTools.ATT_BOUND_HEIGHT);
		if(attribute!=null)
		{	double temp = Double.parseDouble(attribute.getValue());
			boundHeight = zoomFactor*temp/scale;
		}
		// default gesture
		String defaultGesture = root.getAttribute(XmlTools.ATT_DEFAULT).getValue();
		result.setDefaultAnime(defaultGesture);
		
		// colors ?
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
			loadShadowsElement(elt,localFilePath,level,images,shadows,colormap,zoomFactor,scale);
		
		// gestures
		Element gestures = root.getChild(XmlTools.ELT_GESTURES);
		loadGesturesElement(gestures,boundHeight,localFilePath,result,level,images,shadows,colormap,zoomFactor,scale);
		
		// images
		Iterator<Entry<String,BufferedImage>> i = images.entrySet().iterator();
		while(i.hasNext())
		{	Entry<String,BufferedImage> temp = i.next();
			BufferedImage tempImg = temp.getValue();
			result.addImage(tempImg);
		}
	}
    
    private static void loadShadowsElement(Element root, String individualFolder,
    		Level level,
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
    		loadShadowElement(tp,localFilePath,level,images,shadows,colormap,zoomFactor,scale);
    	}
    }
    
    private static void loadShadowElement(Element root, String individualFolder,
    		Level level,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	// file
    	String localPath = individualFolder+File.separator;
    	localPath = localPath + root.getAttribute(XmlTools.ATT_FILE).getValue().trim();
    	BufferedImage shadow = loadImage(localPath,level,images,colormap,zoomFactor,scale);
    	// name
    	String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		//
    	shadows.put(name,shadow);
    }
    
    private static void loadGesturesElement(Element root, double boundHeight, String filePath, AnimePack animePack,
    		Level level,
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
			AnimeGesture animeGesture = loadGestureElement(tp,boundHeight,localFilePath,level,images,shadows,colormap,zoomFactor,scale);
			animePack.addAnimeGesture(animeGesture);
    	}
    }
    
    /**
     * load a gesture (and if required all the associated directions) 
     */
    private static AnimeGesture loadGestureElement(Element root, double boundHeight, String filePath,
    		Level level,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	AnimeGesture result = new AnimeGesture();
    	// name
    	String gestureName;
		gestureName = root.getAttribute(XmlTools.ATT_NAME).getValue();
    	result.setName(gestureName);
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
				shadow = loadImage(imgPath,level,images,colormap,zoomFactor,scale);
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
			boundYShift = ImageShift.valueOf(attribute.getValue().trim().toUpperCase());
		// directions
		List<Element> directionsList = root.getChildren(XmlTools.ELT_DIRECTION);
    	Iterator<Element> i = directionsList.iterator();
    	while(i.hasNext())
    	{	Element tp = i.next();
			AnimeDirection animeDirection = loadDirectionElement(gestureName,boundHeight,repeat,proportional,tp,localFilePath,xShift,yShift,
					shadow,shadowXShift,shadowYShift,boundYShift
					,level,images,shadows,colormap,zoomFactor,scale);
			result.addAnimeDirection(animeDirection);
		}
    	return result;
    }
    
    /**
     * load a direction for a given gesture
     */
    private static AnimeDirection loadDirectionElement(String gestureName, double boundHeight, boolean repeat, boolean proportional, 
    		Element root, String filePath, 
    		double xShift, double yShift, 
    		BufferedImage shadow, double shadowXShift, double shadowYShift, ImageShift boundYShift,
    		Level level,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	AnimeDirection result = new AnimeDirection();
		// direction
		String strDirection = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		Direction direction = Direction.NONE;
		if(!strDirection.equals(""))
			direction = Direction.valueOf(strDirection.toUpperCase());
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
				shadow = loadImage(imgPath,level,images,colormap,zoomFactor,scale);
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
    		AnimeStep animeStep = loadStepElement(tp,localFilePath,xShift,yShift,shadow,shadowXShift,shadowYShift,boundYShift,level,images,shadows,colormap,zoomFactor,scale);
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
    		Level level,
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
    		img = loadImage(imgPath,level,images,colormap,zoomFactor,scale);
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
				shadow = loadImage(imgPath,level,images,colormap,zoomFactor,scale);
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
   
    private static BufferedImage loadImage(String imgPath, Level level,
    		HashMap<String,BufferedImage> images, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	BufferedImage result;
    	if(images.containsKey(imgPath))
    		result = images.get(imgPath);
    	else
    	{	BufferedImage imgOld = ImageTools.loadImage(imgPath,colormap);
    	 	double zoom = zoomFactor/scale;
			result = ImageTools.resize(imgOld,zoom,level.getConfiguration().getSmoothGraphics());
			images.put(imgPath,result);
    	}
    	return result;
    }
}
