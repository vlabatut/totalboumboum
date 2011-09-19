package org.totalboumboum.engine.content.sprite;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorFolder;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorMap;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRule;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRulesMapLoader;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.ImageTools;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SpritePreviewLoader
{
	private static boolean loadAuthor;
	private static boolean loadImages;
	private static boolean loadName;
	private static boolean loadSource;
	
	public static SpritePreview loadHeroPreview(String packName, String spriteName) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	loadAuthor = true;
		loadImages = true;
		loadName = true;
		loadSource = true;
		SpritePreview result = loadHeroPreviewCommon(packName,spriteName);
		return result;
	}

	public static SpritePreview loadHeroPreviewOnlyName(String packName, String spriteName) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	loadAuthor = false;
		loadImages = false;
		loadName = true;
		loadSource = false;
		SpritePreview result = loadHeroPreviewCommon(packName,spriteName);
		return result;
	}

	public static SpritePreview loadHeroPreviewCommon(String packName, String spriteName) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String folder = FilePaths.getHeroesPath()+File.separator+packName+File.separator+spriteName;
		Element root = HollowSpriteFactoryLoader.openFile(folder);
		HashMap<String,SpritePreview> abstractPreviews = new HashMap<String, SpritePreview>();
		SpritePreview result = loadSpriteElement(root,folder,abstractPreviews);
		result.setPack(packName);
		result.setFolder(spriteName);
		return result;
	}

	public static SpritePreview loadSpritePreview(String folder, HashMap<String,SpritePreview> abstractPreviews) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	loadAuthor = true;
		loadImages = true;
		loadName = true;
		loadSource = true;
		Element root = HollowSpriteFactoryLoader.openFile(folder);
		SpritePreview result = loadSpriteElement(root,folder,abstractPreviews);
		result.setPack(null);//TODO à compléter en extrayant le pack du chemin folder
		result.setFolder(new File(folder).getName());
		return result;
	}

	private static SpritePreview loadSpriteElement(Element root, String folder, HashMap<String,SpritePreview> abstractPreviews) throws IOException, ParserConfigurationException, SAXException
	{	// init
		Element elt = root.getChild(XmlNames.GENERAL);
		String baseStr = elt.getAttributeValue(XmlNames.BASE);
		SpritePreview result = abstractPreviews.get(baseStr);
		if(result==null)
			result = new SpritePreview();
		else
			result = result.copy();
		
		// load
		if(loadName)
			loadNameElement(root,result);
		if(loadAuthor)
			loadAuthorElement(root, result);
		if(loadSource)
			loadSourceElement(root, result);
		if(loadImages)
			loadImages(folder,result);
		return result;
	}
	
	private static void loadNameElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlNames.GENERAL);
		String name = elt.getAttribute(XmlNames.NAME).getValue().trim();
		result.setName(name);		
	}
	
	private static void loadAuthorElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlNames.AUTHOR);
		String name = null; 
		if(elt!=null)
			name = elt.getAttribute(XmlNames.VALUE).getValue().trim();
		result.setAuthor(name);		
	}
	
	private static void loadSourceElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlNames.SOURCE);
		String name = null; 
		if(elt!=null)
			name = elt.getAttribute(XmlNames.VALUE).getValue().trim();
		result.setSource(name);		
	}
	
	@SuppressWarnings("unchecked")
	private static void loadImages(String folder, SpritePreview result) throws ParserConfigurationException, SAXException, IOException
	{	String folderPath = folder+File.separator+FileNames.FILE_ANIMES;
		File dataFile = new File(folderPath+File.separator+FileNames.FILE_ANIMES+FileNames.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FilePaths.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ANIMES+FileNames.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			Attribute previewAtt = root.getAttribute(XmlNames.PREVIEW);
	    	boolean found = false;
	    	String gesturesFolder = "";
	    	String gestureFolder = "";
	    	String directionFolder = "";
	    	String stepFile = "";
			
			// check if the preview attribute exists
			if(previewAtt!=null)
			{	found = true;
				stepFile = previewAtt.getValue().trim();
			}
			// else: look for the first image from the STANDING gesture
			// (must be defined as an actual image, not a reference, and not multilayered)
			else
			{	// init
		    	GestureName defaultGesture = GestureName.STANDING;
		    	Element gesturesElement = root.getChild(XmlNames.GESTURES);
		    	Attribute gesturesFolderAtt = gesturesElement.getAttribute(XmlNames.FOLDER);
		    	if(gesturesFolderAtt!=null)
		    		gesturesFolder = File.separator+gesturesFolderAtt.getValue();
		    	
		    	// look for the gesture
	    		List<Element> gestureList = gesturesElement.getChildren(XmlNames.GESTURE);
		    	Iterator<Element> it = gestureList.iterator();
		    	while(it.hasNext() && !found)
		    	{	Element gestureElt = it.next();
		    		String name = gestureElt.getAttributeValue(XmlNames.NAME);
		    		if(name.equalsIgnoreCase(defaultGesture.toString()))
		    		{	found = true;
	    				Attribute f = gestureElt.getAttribute(XmlNames.FOLDER);
	    				if(f!=null)
	    					gestureFolder = File.separator+f.getValue();
	    				
	    				// look for the direction
		    			List<Element> directionList = gestureElt.getChildren(XmlNames.DIRECTION);
		    			String defaultDirection;
		    			if(directionList.size()==1)
		    				defaultDirection = Direction.NONE.toString();
		    			else
		    				defaultDirection = Direction.DOWN.toString();
		    			Iterator<Element> it2 = directionList.iterator();
		    			boolean found2 = false;
		    			while(it2.hasNext() && !found2)
		    			{	Element directionElt = it2.next();
		    				String name2 = directionElt.getAttributeValue(XmlNames.NAME);
		    				if(name2.equalsIgnoreCase(defaultDirection))
		    				{	found2 = true;
			    				Attribute fold = directionElt.getAttribute(XmlNames.FOLDER);
			    				if(fold!=null)
			    					directionFolder = File.separator+fold.getValue();
			    				// take the first step
			    				List<Element> stepList = directionElt.getChildren(XmlNames.STEP);
			    				Element stepElt = stepList.get(0);
				    			stepFile = stepElt.getAttributeValue(XmlNames.FILE);
		    				}
		    			}
		    		}
		    	}
			}
	    	
	    	if(found)
	    	{	Element elt = root.getChild(XmlNames.COLORS);
	    		
	    		// get the colored picture
	    		ColorFolder colorFold = null;
	    		if(elt!=null)
				{	List<Element> clrs = elt.getChildren();
					Iterator<Element> iter = clrs.iterator();
					PredefinedColor mapped = null;
			    	String defname = elt.getAttribute(XmlNames.DEFAULT).getValue().trim();
		    		defname = defname.toUpperCase(Locale.ENGLISH);
			    	PredefinedColor defaultColor = PredefinedColor.valueOf(defname);
		    		while(iter.hasNext())
			    	{	BufferedImage img;
			    		Element temp = iter.next();
			    		String name = temp.getAttribute(XmlNames.NAME).getValue().trim();
			    		name = name.toUpperCase(Locale.ENGLISH);
			    		PredefinedColor color = PredefinedColor.valueOf(name);
			    		ColorRule colorRule = ColorRulesMapLoader.loadColorsElement(elt,folderPath,color);
						if(colorRule instanceof ColorMap)
						{	ColorMap colormap = (ColorMap)colorRule;
							String imagePath = folderPath+gesturesFolder+gestureFolder+directionFolder+File.separator+stepFile;
							img = ImageTools.loadImage(imagePath,colormap);
							if(mapped==null)
							{	BufferedImage img2 = ImageTools.loadImage(imagePath,null);
								result.setImage(null,img2);
								mapped = color;
							}
						}
						else
						{	ColorFolder colorFolder = (ColorFolder)colorRule;
							if(colorFold==null)
								colorFold = colorFolder;
							String imagePath = colorFolder.getFolder()+gestureFolder+directionFolder+File.separator+stepFile;
							img = ImageTools.loadImage(imagePath,null);
							if(mapped==null && color==defaultColor)
							{	result.setImage(null,img);
							}
						}
			    		result.setImage(color,img);
			    	}
				}
	    		// get the colorless picture
	    		else
	    		{	String imgPath;
//		    		if(colorFold==null)
		    			imgPath = folderPath+gesturesFolder+gestureFolder+directionFolder+File.separator+stepFile;
//		    		else
//						imgPath = folderPath+gesturesFolder+colorFold.getFolder()+gestureFolder+directionFolder+File.separator+stepFile;
		    		BufferedImage image = ImageTools.loadImage(imgPath,null);
					result.setImage(null,image);
	    		}
			}
		}
	}
}
