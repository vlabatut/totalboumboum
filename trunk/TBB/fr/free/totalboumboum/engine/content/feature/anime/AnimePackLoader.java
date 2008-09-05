package fr.free.totalboumboum.engine.content.feature.anime;

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
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.PredefinedColor;
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
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// scale
		double scale = 1;
		if(root.hasAttribute(XmlTools.ATT_SCALE))
			scale = Double.parseDouble(root.getAttribute(XmlTools.ATT_SCALE));
		result.setScale(scale);
		// bound height
		double boundHeight = 0;
		double zoomFactor = level.getLoop().getZoomFactor();
		if(root.hasAttribute(XmlTools.ATT_BOUND_HEIGHT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_BOUND_HEIGHT));
			boundHeight = zoomFactor*temp/scale;
		}
		// default gesture
		String defaultGesture = root.getAttribute(XmlTools.ATT_DEFAULT);
		result.setDefaultAnime(defaultGesture);
		
		// colors ?
		Object obj;
		if(XmlTools.hasChildElement(root,XmlTools.ELT_COLORS) && color!=null)
		{	obj = loadColorsElement(XmlTools.getChildElement(root,XmlTools.ELT_COLORS),localFilePath,color);
			if(obj instanceof Colormap)
				colormap = (Colormap)obj;
			else
				colorFolder = (String)obj;
		}
		
		if(colorFolder!=null)
			localFilePath = localFilePath+File.separator + colorFolder;
		
		// shadows ?
		if(XmlTools.hasChildElement(root,XmlTools.ELT_SHADOWS))
			loadShadowsElement(XmlTools.getChildElement(root,XmlTools.ELT_SHADOWS),localFilePath,level,images,shadows,colormap,zoomFactor,scale);
		
		// gestures
		Element gestures = XmlTools.getChildElement(root,XmlTools.ELT_GESTURES);
		loadGesturesElement(gestures,boundHeight,localFilePath,result,level,images,shadows,colormap,zoomFactor,scale);
		
		// images
		Iterator<Entry<String,BufferedImage>> i = images.entrySet().iterator();
		while(i.hasNext())
		{	Entry<String,BufferedImage> temp = i.next();
			BufferedImage tempImg = temp.getValue();
			result.addImage(tempImg);
		}
	}
    
    private static Object loadColorsElement(Element root, String individualFolder, PredefinedColor color) throws IOException, ParserConfigurationException, SAXException
    {	Object result=null;
    	// folder
    	String localFilePath = individualFolder;
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// colormaps
    	ArrayList<Element> clrs = XmlTools.getChildElements(root);
    	int i=0;
		while(result==null && i<clrs.size())
    	{	Element temp = clrs.get(i);
    		String name = temp.getAttribute(XmlTools.ATT_NAME).trim();
    		if(name.equalsIgnoreCase(color.toString()))
    		{	// colormap
    			if(temp.getLocalName().equals(XmlTools.ELT_COLORMAP))
    				result = loadColormapElement(temp,localFilePath);
    			// colorsprite
    			else if(temp.getLocalName().equals(XmlTools.ELT_COLORSPRITE))
    				result = loadColorspriteElement(temp);
    		}
    		else
    			i++;
    	}
		if(result==null)
			;// erreur
		return result;
    }
    
    private static Colormap loadColormapElement(Element root, String individualFolder) throws IOException, ParserConfigurationException, SAXException
    {	// file
    	String localPath = individualFolder+File.separator;
    	localPath = localPath + root.getAttribute(XmlTools.ATT_FILE).trim();
    	// colormap
    	Colormap colormap = ColormapLoader.loadColormap(localPath);
    	return colormap;
    }
    
    private static String loadColorspriteElement(Element root) throws IOException, ParserConfigurationException, SAXException
    {	// folder
    	String colorFolder = root.getAttribute(XmlTools.ATT_FOLDER).trim();
    	return colorFolder;
    }
    
    private static void loadShadowsElement(Element root, String individualFolder,
    		Level level,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	// folder
    	String localFilePath = individualFolder;
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// shadows
    	ArrayList<Element> shdws = XmlTools.getChildElements(root,XmlTools.ELT_SHADOW);
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
    	localPath = localPath + root.getAttribute(XmlTools.ATT_FILE).trim();
    	BufferedImage shadow = loadImage(localPath,level,images,colormap,zoomFactor,scale);
    	// name
    	String name = root.getAttribute(XmlTools.ATT_NAME).trim();
		//
    	shadows.put(name,shadow);
    }
    
    private static void loadGesturesElement(Element root, double boundHeight, String filePath, AnimePack animePack,
    		Level level,
    		HashMap<String,BufferedImage> images, HashMap<String,BufferedImage> shadows, Colormap colormap,
    		double zoomFactor, double scale) throws IOException
    {	// folder
    	String localFilePath = filePath;
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// gestures
    	ArrayList<Element> gesturesList = XmlTools.getChildElements(root);
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
		gestureName = root.getAttribute(XmlTools.ATT_NAME);
    	result.setName(gestureName);
    	// images folder
    	String localFilePath = filePath;
    	if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// repeat flag
		String repeatStr = root.getAttribute(XmlTools.ATT_REPEAT);
		boolean repeat = false;
		if(!repeatStr.equals(""))
			repeat = Boolean.parseBoolean(repeatStr);
		// proportional flag
		boolean proportional = false;
		if(root.hasAttribute(XmlTools.ATT_PROPORTIONAL))
			proportional = Boolean.parseBoolean(root.getAttribute(XmlTools.ATT_PROPORTIONAL));
		// horizontal shift
		double xShift = 0;
		if(root.hasAttribute(XmlTools.ATT_XSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_XSHIFT));
			xShift = zoomFactor*temp/scale;
		}
		// vertical shift
		double yShift = 0;
		if(root.hasAttribute(XmlTools.ATT_YSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_YSHIFT));
			yShift = zoomFactor*temp/scale;
		}
		// shadow
		BufferedImage shadow = null;
		if(root.hasAttribute(XmlTools.ATT_SHADOW))
		{	shadow = shadows.get(root.getAttribute(XmlTools.ATT_SHADOW).trim());
			if(shadow==null)
			{	String imgPath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_SHADOW).trim();
				shadow = loadImage(imgPath,level,images,colormap,zoomFactor,scale);
			}
		}
		// shadow horizontal shift
		double shadowXShift = 0;
		if(root.hasAttribute(XmlTools.ATT_SHADOW_XSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_SHADOW_XSHIFT));
			shadowXShift = zoomFactor*temp/scale;
		}
		// shadow vertical shift
		double shadowYShift = 0;
		if(root.hasAttribute(XmlTools.ATT_SHADOW_YSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_SHADOW_YSHIFT));
			shadowYShift = zoomFactor*temp/scale;
		}
		// bound shift
		ImageShift boundYShift = ImageShift.DOWN;
		if(root.hasAttribute(XmlTools.ATT_BOUND_YSHIFT))
			boundYShift = ImageShift.valueOf(root.getAttribute(XmlTools.ATT_BOUND_YSHIFT).trim().toUpperCase());
		// directions
		ArrayList<Element> directionsList = XmlTools.getChildElements(root,XmlTools.ELT_DIRECTION);
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
		String strDirection = root.getAttribute(XmlTools.ATT_NAME).trim();
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
    	if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// horizontal shift
		if(root.hasAttribute(XmlTools.ATT_XSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_XSHIFT));
			xShift = zoomFactor*temp/scale;
		}
		// vertical shift
		if(root.hasAttribute(XmlTools.ATT_YSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_YSHIFT));
			yShift = zoomFactor*temp/scale;
		}
		// shadow
		if(root.hasAttribute(XmlTools.ATT_SHADOW))
		{	shadow = shadows.get(root.getAttribute(XmlTools.ATT_SHADOW).trim());
			if(shadow==null)
			{	String imgPath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_SHADOW).trim();
				shadow = loadImage(imgPath,level,images,colormap,zoomFactor,scale);
			}
		}
		// shadow horizontal shift
		if(root.hasAttribute(XmlTools.ATT_SHADOW_XSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_SHADOW_XSHIFT));
			shadowXShift = zoomFactor*temp/scale;
		}
		// shadow vertical shift
		if(root.hasAttribute(XmlTools.ATT_SHADOW_YSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_SHADOW_YSHIFT));
			shadowYShift = zoomFactor*temp/scale;
		}
		// bound shift
		if(root.hasAttribute(XmlTools.ATT_BOUND_YSHIFT))
			boundYShift = ImageShift.valueOf(root.getAttribute(XmlTools.ATT_BOUND_YSHIFT).trim());
    	// steps
	    ArrayList<Element> stepsList = XmlTools.getChildElements(root, XmlTools.ELT_STEP);
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
    	if(root.hasAttribute(XmlTools.ATT_DURATION))
    		duration = Integer.parseInt(root.getAttribute(XmlTools.ATT_DURATION));
    	// image
    	BufferedImage img = null;    	
    	if(root.hasAttribute(XmlTools.ATT_FILE))
    	{	String strImage = root.getAttribute(XmlTools.ATT_FILE).trim();
    		String imgPath = filePath+File.separator+strImage;
    		img = loadImage(imgPath,level,images,colormap,zoomFactor,scale);
    	}
		// horizontal shift
		if(root.hasAttribute(XmlTools.ATT_XSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_XSHIFT));
			xShift = zoomFactor*temp/scale;
		}
		// vertical shift
		if(root.hasAttribute(XmlTools.ATT_YSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_YSHIFT));
			yShift = zoomFactor*temp/scale;
		}
		// shadow
		if(root.hasAttribute(XmlTools.ATT_SHADOW))
		{	shadow = shadows.get(root.getAttribute(XmlTools.ATT_SHADOW).trim());
			if(shadow==null)
			{	String imgPath = filePath+File.separator+root.getAttribute(XmlTools.ATT_SHADOW).trim();
				shadow = loadImage(imgPath,level,images,colormap,zoomFactor,scale);
			}
		}
		// shadow horizontal shift
		if(root.hasAttribute(XmlTools.ATT_SHADOW_XSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_SHADOW_XSHIFT));
			shadowXShift = zoomFactor*temp/scale;
		}
		// shadow vertical shift
		if(root.hasAttribute(XmlTools.ATT_SHADOW_YSHIFT))
		{	double temp = Double.parseDouble(root.getAttribute(XmlTools.ATT_SHADOW_YSHIFT));
			shadowYShift = zoomFactor*temp/scale;
		}
		// bound shift
		if(root.hasAttribute(XmlTools.ATT_BOUND_YSHIFT))
			boundYShift = ImageShift.valueOf(root.getAttribute(XmlTools.ATT_BOUND_YSHIFT).trim());
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
			// resize
			int xDim = (int)(imgOld.getWidth()*zoom);
			int yDim = (int)(imgOld.getHeight()*zoom);
//			double actualZoom = xDim/imgOld.getWidth();
			result = new BufferedImage(xDim, yDim, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = result.createGraphics();
			g.setComposite(AlphaComposite.Src);
			if(level.getConfiguration().getSmoothGraphics())
			{	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			else
			{	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			}
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);		
			//g.drawImage(imgOld, 0, 0, xDim, yDim, null);
			AffineTransform xform = AffineTransform.getScaleInstance(zoom,zoom);
			g.drawRenderedImage(imgOld, xform);
			g.dispose();
			//
			images.put(imgPath, result);
    	}
    	return result;
    }
}
