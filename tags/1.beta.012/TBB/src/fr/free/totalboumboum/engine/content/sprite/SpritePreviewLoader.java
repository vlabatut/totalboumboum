package fr.free.totalboumboum.engine.content.sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.anime.Colormap;
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
		SpritePreview result = loadSpriteElement(root,folder);
		result.setPack(packName);
		result.setFolder(spriteName);
		return result;
	}

	public static SpritePreview loadSpritePreview(String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	loadAuthor = true;
		loadImages = true;
		loadName = true;
		loadSource = true;
		Element root = SpriteFactoryLoader.openFile(folder);
		SpritePreview result = loadSpriteElement(root,folder);
		result.setPack(null);//TODO à compléter en extrayant le pack du chemin folder
		result.setFolder(new File(folder).getName());
		return result;
	}

	private static SpritePreview loadSpriteElement(Element root, String folder) throws IOException, ParserConfigurationException, SAXException
	{	SpritePreview result = new SpritePreview();
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
	{	Element elt = root.getChild(XmlTools.ELT_GENERAL);
		String name = elt.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setName(name);		
	}
	
	private static void loadAuthorElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.ELT_AUTHOR);
		String name = null; 
		if(elt!=null)
			name = elt.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setAuthor(name);		
	}
	
	private static void loadSourceElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.ELT_SOURCE);
		String name = null; 
		if(elt!=null)
			name = elt.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
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
	    	String defaultGesture = root.getAttributeValue(XmlTools.ATT_DEFAULT);
	    	Element gesturesElement = root.getChild(XmlTools.ELT_GESTURES);
	    	Attribute gesturesFolderAtt = gesturesElement.getAttribute(XmlTools.ATT_FOLDER);
	    	String gesturesFolder = "";
	    	String gestureFolder = "";
	    	String directionFolder = "";
	    	String stepFile = "";
	    	if(gesturesFolderAtt!=null)
	    		gesturesFolder = File.separator+gesturesFolderAtt.getValue();
	    	// look for the gesture
	    	{	List<Element> gestureList = gesturesElement.getChildren(XmlTools.ELT_GESTURE);
		    	Iterator<Element> it = gestureList.iterator();
		    	boolean found = false;
		    	while(it.hasNext() && !found)
		    	{	Element gestureElt = it.next();
		    		String name = gestureElt.getAttributeValue(XmlTools.ATT_NAME);
		    		if(name.equalsIgnoreCase(defaultGesture))
		    		{	found = true;
	    				Attribute f = gestureElt.getAttribute(XmlTools.ATT_FOLDER);
	    				if(f!=null)
	    					gestureFolder = File.separator+f.getValue();
	    				// look for the direction
		    			List<Element> directionList = gestureElt.getChildren(XmlTools.ELT_DIRECTION);
		    			String defaultDirection;
		    			if(directionList.size()==1)
		    				defaultDirection = Direction.NONE.toString();
		    			else
		    				defaultDirection = Direction.DOWN.toString();
		    			Iterator<Element> it2 = directionList.iterator();
		    			boolean found2 = false;
		    			while(it2.hasNext() && !found2)
		    			{	Element directionElt = it2.next();
		    				String name2 = directionElt.getAttributeValue(XmlTools.ATT_NAME);
		    				if(name2.equalsIgnoreCase(defaultDirection))
		    				{	found2 = true;
			    				Attribute fold = directionElt.getAttribute(XmlTools.ATT_FOLDER);
			    				if(fold!=null)
			    					directionFolder = File.separator+fold.getValue();
			    				// take the first step
			    				List<Element> stepList = directionElt.getChildren(XmlTools.ELT_STEP);
			    				Element stepElt = stepList.get(0);
				    			stepFile = stepElt.getAttributeValue(XmlTools.ATT_FILE);
		    				}
		    			}
		    		}
		    	}
	    	}
	    	// get the preview picture
			String imgPath = folderPath+gesturesFolder+gestureFolder+directionFolder+File.separator+stepFile;
			BufferedImage image = ImageTools.loadImage(imgPath,null);
			result.setImage(null,image);			
			// get the colors
			Element elt = root.getChild(XmlTools.ELT_COLORS);
			if(elt!=null)
			{	List<Element> clrs = elt.getChildren();
				Iterator<Element> iter = clrs.iterator();
		    	while(iter.hasNext())
		    	{	BufferedImage img;
		    		Element temp = iter.next();
		    		String name = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim();
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
