package fr.free.totalboumboum.tools;

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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.anime.Colormap;
import fr.free.totalboumboum.engine.content.feature.anime.ColormapLoader;


public class ImageTools
{

	/** 
	 * make a copy of the <image> parameter.
	 */
	public static BufferedImage copyBufferedImage(BufferedImage image)
	{	int type = image.getType();
		BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), type/*BufferedImage.TYPE_INT_ARGB*/);
	  	// create a graphics context
	  	Graphics2D g2d = copy.createGraphics();
	    // g2d.setComposite(AlphaComposite.Src);	  	
	  	// copy image
	  	g2d.drawImage(image,0,0,null);
	  	g2d.dispose();
	  	return copy;
	  }	

	/** 
	 * make a BufferedImage copy of im
	 *
	public static BufferedImage makeBufferedImage(Image im, int width, int height)
	{	
		BufferedImage copy = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	  	// create a graphics context
	  	Graphics2D g2d = copy.createGraphics();
	    // g2d.setComposite(AlphaComposite.Src);
	  	// copy image
	  	g2d.drawImage(im,0,0,null);
	  	g2d.dispose();
	  	return copy;
	  }	*/
	
	/**
	 * Load the image from <path>, returning it as a BufferedImage
	 * which is compatible with the graphics device being used.
	 * Uses ImageIO.
	 * @throws IOException 
	 
    public static BufferedImage loadImage(String path) throws IOException 
	{	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage im = ImageIO.read(new File(path));
		ColorModel cm = im.getColorModel();			
		int transparency = cm.getTransparency();
		BufferedImage copy =  gc.createCompatibleImage(im.getWidth(), im.getHeight(),transparency);
		// create a graphics context
		Graphics2D g2d = copy.createGraphics();
		// copy image
		g2d.drawImage(im,0,0,null);
		g2d.dispose();
		return copy;	
	}	
    */
	
    public static BufferedImage loadImage(String path, Colormap colormap) throws IOException
    {	BufferedImage image = ImageIO.read(new File(path));
    	if(colormap!=null)
    	{	ColorModel colorModel = image.getColorModel();
    		if(colorModel instanceof IndexColorModel)
    		{	// build the color map
	    		IndexColorModel cm = (IndexColorModel) colorModel;
	    		int size = cm.getMapSize();
	    		int bits = cm.getPixelSize();
	    		byte reds[] = new byte[size];
	    		byte greens[] = new byte[size];
	    		byte blues[] = new byte[size];
	    		cm.getReds(reds);
	    		cm.getGreens(greens);
	    		cm.getBlues(blues);
	    		// changing the values    		
    			Iterator<Entry<Integer,byte[]>> i = colormap.entrySet().iterator();
    			while(i.hasNext())
    			{	Entry<Integer,byte[]> temp = i.next();
    				int index = temp.getKey();
    				byte tab[] = temp.getValue();
    				reds[index] = tab[0];
    				greens[index] = tab[1];
    				blues[index] = tab[2];
    			}
    			// creating the new model
	    		int trans = cm.getTransparentPixel();
	    		IndexColorModel newCm;
	    		if(trans<0)
	    			newCm = new IndexColorModel(bits, size, reds, greens, blues);
	    		else
	    			newCm = new IndexColorModel(bits, size, reds, greens, blues, trans);	    		
	    		// cloning the image, applying the new color model
	    		String[] names = image.getPropertyNames();
	    		Hashtable<String,Object> props = new Hashtable<String,Object>();
	    		if(names!=null)
	    		{	for(int k=0;k<names.length;k++)
		    		{	Object prop = image.getProperty(names[k]);
		    			props.put(names[k], prop);
		    		}
	    		}
	    		WritableRaster raster = image.copyData(null);
	    		BufferedImage copy = new BufferedImage(newCm,raster,image.isAlphaPremultiplied(),props);
    	  		image = copy;  	  		
    		}
    	}
    	
    	// optimizing : using a model adapted to the graphical environment
    	BufferedImage result =  getCompatibleImage(image);
    	return result;
    }

    private static BufferedImage getCompatibleImage(BufferedImage image)
    {	// get the graphical environment
    	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		// get original the color model
		ColorModel cm = image.getColorModel();			
		int transparency = cm.getTransparency();
		// create a new image compatible with both the environment and the original model
		BufferedImage result =  gc.createCompatibleImage(image.getWidth(),image.getHeight(),transparency);
		// copy image
		Graphics2D g2d = result.createGraphics();
		g2d.drawImage(image,0,0,null);
		g2d.dispose();
		// result
    	return result;
    }
    
    public static Object loadColorsElement(Element root, String individualFolder, PredefinedColor color) throws IOException, ParserConfigurationException, SAXException
    {	Object result=null;
    	// folder
    	String localFilePath = individualFolder;
    	Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
    	if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		// colormaps
    	List<Element> clrs = root.getChildren();
    	int i=0;
		while(result==null && i<clrs.size())
    	{	Element temp = clrs.get(i);
    		String name = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    		if(name.equalsIgnoreCase(color.toString()))
    		{	// colormap
    			if(temp.getName().equals(XmlTools.ELT_COLORMAP))
    				result = loadColormapElement(temp,localFilePath);
    			// colorsprite
    			else if(temp.getName().equals(XmlTools.ELT_COLORSPRITE))
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
    	localPath = localPath + root.getAttribute(XmlTools.ATT_FILE).getValue().trim();
    	// colormap
    	Colormap colormap = ColormapLoader.loadColormap(localPath);
    	return colormap;
    }
    
    private static String loadColorspriteElement(Element root) throws IOException, ParserConfigurationException, SAXException
    {	// folder
    	String colorFolder = root.getAttribute(XmlTools.ATT_FOLDER).getValue().trim();
    	return colorFolder;
    } 
    
    public static BufferedImage resize(BufferedImage imgOld, double zoom, boolean smooth)
    {	int xDim = (int)(imgOld.getWidth()*zoom);
		int yDim = (int)(imgOld.getHeight()*zoom);
//		double actualZoom = xDim/imgOld.getWidth();
		BufferedImage imgNew = new BufferedImage(xDim, yDim, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = imgNew.createGraphics();
		g.setComposite(AlphaComposite.Src);
		if(smooth)
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
    	// optimizing : using a model adapted to the graphical environment
		BufferedImage result =  getCompatibleImage(imgNew);
    	return result; 
    }
    
    public static BufferedImage getAbsentImage(int width, int height)
    {	BufferedImage result = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.createGraphics();

		// red cross
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		int strokeSize = width/5;
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.drawLine(0+strokeSize,0+strokeSize,width-strokeSize,height-strokeSize);
        g2d.drawLine(0+strokeSize,height-strokeSize,width-strokeSize,0+strokeSize);
/*        
        // question mark
        g.setColor(Color.BLACK);
		Font font = new Font("Arial",Font.PLAIN,height);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "?";
		Rectangle2D box = metrics.getStringBounds(text,g);
		int x = (int)Math.round(width/2-box.getWidth()/2);
		int y = (int)Math.round(height/2+box.getHeight()/3);
		g.drawString(text,x,y);
*/
		g.dispose();
		return result;
    }
    
    public static BufferedImage getGreyScale(BufferedImage image)
    {	// new greyscaled image
    	int width = image.getWidth();
    	int height = image.getHeight();
		BufferedImage result = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = result.getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
		// make it compatible
		result =  getCompatibleImage(result);
		//
    	return result;    	
    }
}

