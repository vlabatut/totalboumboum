package org.totalboumboum.tools.images;

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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.IndexColorModel;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorMap;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ImageTools
{

	/////////////////////////////////////////////////////////////////
	// FILE ACCESS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    public static BufferedImage loadImage(String path, ColorMap colormap) throws IOException
    {	BufferedImage image;
    	FileInputStream in = new FileInputStream(path);
    	BufferedInputStream inBuff = new BufferedInputStream(in);
		try
		{	image = ImageIO.read(inBuff);
		}
		catch (IOException e)
		{	System.out.println(path);
			throw e;
		}
    	if(colormap!=null)
    	{	image = getColoredImage(image,colormap);    	
    	}
    	else
    	{	// optimizing : using a model adapted to the graphical environment
//        	image =  getCompatibleImage(image); //can't anymore since the recoloring must be performed later
    	}
		inBuff.close();
    	return image;
    }
    
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    private static GraphicsEnvironment ge;
    private static GraphicsConfiguration gc;
    // get the graphical environment
	static 
    {	ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gc = ge.getDefaultScreenDevice().getDefaultConfiguration();		
    }
    
    //TODO I guess this should be disabled in favor of getCompatibleImage
    public static BufferedImage copyBufferedImage(BufferedImage image)
	{	int type = image.getType();
		BufferedImage copy = new BufferedImage(image.getWidth(),image.getHeight(), type/*BufferedImage.TYPE_INT_ARGB*/);
	  	
		// create a graphics context
	  	Graphics2D g2d = copy.createGraphics();
	    // g2d.setComposite(AlphaComposite.Src);	  	
	  	
	  	// copy image
	  	g2d.drawImage(image,0,0,null);
	  	g2d.dispose();
	  	return copy;
	}	

	public static BufferedImage getCompatibleImage(BufferedImage image)
    {	// get the original color model
		ColorModel cm = image.getColorModel();			
		int transparency = cm.getTransparency();
		
		// create a new image compatible with both the environment and the original model
		BufferedImage result =  gc.createCompatibleImage(image.getWidth(),image.getHeight(),transparency);
		
		// copy image
		Graphics2D g2d = result.createGraphics();
		g2d.drawImage(image,0,0,null);
		g2d.dispose();

    	return result;
    }
    
    /**
     * Resize the specified image using the specified zoom coefficient and smoothing method.
     * @param imgOld
     * @param zoom
     * @param smooth
     * @return
     */
    public static BufferedImage getResizedImage(BufferedImage imgOld, double zoom, boolean smooth)
    {	// dimensions
    	int xDim = (int)(imgOld.getWidth()*zoom);
		int yDim = (int)(imgOld.getHeight()*zoom);
if(xDim<0 || yDim<0)
	System.out.println("ImageTools.resize(): Zoom Error");

		// colors and stuff
//		int type = imgOld.getType();
//		if(type == BufferedImage.TYPE_CUSTOM)
//			type = BufferedImage.TYPE_INT_ARGB;
//   		ColorModel colorModel = imgOld.getColorModel();
//   	BufferedImage result;
//		if(colorModel instanceof IndexColorModel)
//		{	//result = new BufferedImage(xDim,yDim,type,(IndexColorModel)colorModel);
//			String[] names = imgOld.getPropertyNames();
//			Hashtable<String,Object> props = new Hashtable<String,Object>();
//			if(names!=null)
//			{	for(int k=0;k<names.length;k++)
//	    		{	Object prop = imgOld.getProperty(names[k]);
//	    			props.put(names[k], prop);
//	    		}
//			}
//			WritableRaster raster = imgOld.getRaster().createCompatibleWritableRaster(xDim,yDim);		
//			result = new BufferedImage(colorModel,raster,imgOld.isAlphaPremultiplied(),props);
//		}
//		else
//			result = new BufferedImage(xDim,yDim,type);
//int type = BufferedImage.TYPE_INT_ARGB;
//BufferedImage result = new BufferedImage(xDim,yDim,type);

		//get the original color model
		ColorModel cm = imgOld.getColorModel();			
		int transparency = cm.getTransparency();
		// create a new image compatible with both the environment and the original model
		BufferedImage result =  gc.createCompatibleImage(xDim,yDim,transparency);
		
		// draw resized image
		Graphics2D g = result.createGraphics();
		g.setComposite(AlphaComposite.Src);
		if(smooth)
		{	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		else
		{	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);		
		//g.drawImage(imgOld, 0, 0, xDim, yDim, null); //older version: 12219 vs 10687 (ms)
		AffineTransform xform = AffineTransform.getScaleInstance(zoom,zoom);
		g.drawRenderedImage(imgOld, xform);
		g.dispose();
    	
		// optimizing : using a model adapted to the graphical environment
//		result =  getCompatibleImage(result);
    	return result; 
    }
    
	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /**
     * Get a neutral image and applies the specified colormap to get a colored image.
     */
    public static BufferedImage getColoredImage(BufferedImage image, ColorMap colormap)
   	{	BufferedImage result = image;
    	
   		// check if the image is indexed (else it has no sense to apply a colormap)
   		ColorModel colorModel = image.getColorModel();
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
			result = new BufferedImage(newCm,raster,image.isAlphaPremultiplied(),props);
		}
		
		return result;
	}

    /**
     * Get the greyscale version of the specified image. 
     * @param image
     * @return
     */
    public static BufferedImage getGreyScale(BufferedImage image)
    {	// new greyscaled image
    	int width = image.getWidth();
    	int height = image.getHeight();
		BufferedImage result = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = result.getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
		
		// make it compatible
//		result = getCompatibleImage(result);

    	return result;    	
    }
    
    /**
     * process a new version of the image, which is darker if param<1 and lighter if param>1
     * method taken from here: http://java.sun.com/developer/JDCTechTips/2004/tt0210.html
     * @param image
     * @param param
     * @return
     */
    public static BufferedImage getDarkenedImage(BufferedImage image, float param)
    {	float[] brightKernel = {param};
        BufferedImageOp bright = new ConvolveOp(new Kernel(1, 1, brightKernel));
        BufferedImage result = bright.filter(image, null);
        return result;
    }

    /**
     * Fill the specified image with the specified color.
     * Alpha transparency is preserved.
     * @param image
     * @param rgb
     * @return
     */
    public static BufferedImage getFilledImage(BufferedImage image, Integer rgb)
   	{	BufferedImage result = copyBufferedImage(image);
    	int width = result.getWidth();
   		int height = result.getHeight();
    	for(int x=0;x<width;x++)
    	{	for(int y=0;y<height;y++)
    		{	int pixel = result.getRGB(x,y);
//System.out.println("rgb: "+rgb);
//System.out.println("x,y: "+x+","+y);    		
//System.out.println("\tpixel: "+pixel);    		
//Color pixelColour = new Color(pixel, true);
				int a = (pixel >> 24) & 0xff;
//int r = (pixel >> 16) & 0xff;
//int g = (pixel >> 8) & 0xff;
//int b = (pixel) & 0xff;
//System.out.println("\ta,r,g,b: "+a+","+r+","+g+","+b);
//System.out.println("\ta,r,g,b: "+pixelColour.getAlpha()+","+pixelColour.getRed()+","+pixelColour.getGreen()+","+pixelColour.getBlue());
    			if(a>0)
    			{	pixel = rgb + 256*256*256*a;
//System.out.println("\tpixel2: "+pixel);    		
//pixelColour = new Color(pixel, true);
//a = (pixel >> 24) & 0xff;
//r = (pixel >> 16) & 0xff;
//g = (pixel >> 8) & 0xff;
//b = (pixel) & 0xff;
//System.out.println("\ta,r,g,b: "+pixelColour.getAlpha()+","+pixelColour.getRed()+","+pixelColour.getGreen()+","+pixelColour.getBlue());
    				result.setRGB(x,y,pixel);
    			}
    		}
    	}
//System.out.println("--------------------------------");    		
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /**
     * produces an image (red cross) used to replace missing images,
     * i.e. images which couldn't be found at loading 
     */
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
}
