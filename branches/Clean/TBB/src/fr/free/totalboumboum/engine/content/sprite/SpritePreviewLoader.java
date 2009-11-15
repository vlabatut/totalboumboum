package fr.free.totalboumboum.engine.content.sprite;

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
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.anime.Colormap;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

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
	{	String folder = FileTools.getHeroesPath()+File.separator+packName+File.separator+spriteName;
		Element root = SpriteFactoryLoader.openFile(folder);
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
		Element root = SpriteFactoryLoader.openFile(folder);
		SpritePreview result = loadSpriteElement(root,folder,abstractPreviews);
		result.setPack(null);// à compléter en extrayant le pack du chemin folder
		result.setFolder(new File(folder).getName());
		return result;
	}

	private static SpritePreview loadSpriteElement(Element root, String folder, HashMap<String,SpritePreview> abstractPreviews) throws IOException, ParserConfigurationException, SAXException
	{	// init
		Element elt = root.getChild(XmlTools.GENERAL);
		String baseStr = elt.getAttributeValue(XmlTools.BASE);
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
	{	Element elt = root.getChild(XmlTools.GENERAL);
		String name = elt.getAttribute(XmlTools.NAME).getValue().trim();
		result.setName(name);		
	}
	
	private static void loadAuthorElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.AUTHOR);
		String name = null; 
		if(elt!=null)
			name = elt.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setAuthor(name);		
	}
	
	private static void loadSourceElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.SOURCE);
		String name = null; 
		if(elt!=null)
			name = elt.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setSource(name);		
	}
	
	@SuppressWarnings("unchecked")
	private static void loadImages(String folder, SpritePreview result) throws ParserConfigurationException, SAXException, IOException
	{	String folderPath = folder+File.separator+FileTools.FILE_ANIMES;
		File dataFile = new File(folderPath+File.separator+FileTools.FILE_ANIMES+FileTools.EXTENSION_XML);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FileTools.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ANIMES+FileTools.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			
			// init
	    	GestureName defaultGesture = GestureName.STANDING;
	    	Element gesturesElement = root.getChild(XmlTools.GESTURES);
	    	Attribute gesturesFolderAtt = gesturesElement.getAttribute(XmlTools.FOLDER);
	    	String gesturesFolder = "";
	    	String gestureFolder = "";
	    	String directionFolder = "";
	    	String stepFile = "";
	    	if(gesturesFolderAtt!=null)
	    		gesturesFolder = File.separator+gesturesFolderAtt.getValue();
	    	
	    	// look for the gesture
    		List<Element> gestureList = gesturesElement.getChildren(XmlTools.GESTURE);
	    	Iterator<Element> it = gestureList.iterator();
	    	boolean found = false;
	    	while(it.hasNext() && !found)
	    	{	Element gestureElt = it.next();
	    		String name = gestureElt.getAttributeValue(XmlTools.NAME);
	    		if(name.equalsIgnoreCase(defaultGesture.toString()))
	    		{	found = true;
    				Attribute f = gestureElt.getAttribute(XmlTools.FOLDER);
    				if(f!=null)
    					gestureFolder = File.separator+f.getValue();
    				
    				// look for the direction
	    			List<Element> directionList = gestureElt.getChildren(XmlTools.DIRECTION);
	    			String defaultDirection;
	    			if(directionList.size()==1)
	    				defaultDirection = Direction.NONE.toString();
	    			else
	    				defaultDirection = Direction.DOWN.toString();
	    			Iterator<Element> it2 = directionList.iterator();
	    			boolean found2 = false;
	    			while(it2.hasNext() && !found2)
	    			{	Element directionElt = it2.next();
	    				String name2 = directionElt.getAttributeValue(XmlTools.NAME);
	    				if(name2.equalsIgnoreCase(defaultDirection))
	    				{	found2 = true;
		    				Attribute fold = directionElt.getAttribute(XmlTools.FOLDER);
		    				if(fold!=null)
		    					directionFolder = File.separator+fold.getValue();
		    				// take the first step
		    				List<Element> stepList = directionElt.getChildren(XmlTools.STEP);
		    				Element stepElt = stepList.get(0);
			    			stepFile = stepElt.getAttributeValue(XmlTools.FILE);
	    				}
	    			}
	    		}
	    	}
	    	
	    	if(found)
	    	{	// get the preview picture
				String imgPath = folderPath+gesturesFolder+gestureFolder+directionFolder+File.separator+stepFile;
				BufferedImage image = ImageTools.loadImage(imgPath,null);
				result.setImage(null,image);			
				
				// get the colors
				Element elt = root.getChild(XmlTools.COLORS);
				if(elt!=null)
				{	List<Element> clrs = elt.getChildren();
					Iterator<Element> iter = clrs.iterator();
			    	while(iter.hasNext())
			    	{	BufferedImage img;
			    		Element temp = iter.next();
			    		String name = temp.getAttribute(XmlTools.NAME).getValue().trim();
			    		name = name.toUpperCase(Locale.ENGLISH);
			    		PredefinedColor color = PredefinedColor.valueOf(name);
			    		Object obj = ImageTools.loadColorsElement(elt,folderPath,color);
						if(obj instanceof Colormap)
						{	Colormap colormap = (Colormap)obj;
							img = ImageTools.loadImage(imgPath, colormap);
						}
						else
						{	String colorFolder = File.separator+(String)obj;
							String imagePath = folderPath+gesturesFolder+colorFolder+gestureFolder+directionFolder+File.separator+stepFile;
							img = ImageTools.loadImage(imagePath,null);
						}
			    		result.setImage(color,img);
			    	}				
				}	    		
	    	}
		}
	}
}
