package fr.free.totalboumboum.tools;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import fr.free.totalboumboum.engine.content.feature.anime.Colormap;


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
    {	boolean debug = false;
    	BufferedImage image = ImageIO.read(new File(path));
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
	    		//
	    		if(debug)
	    		{	System.out.println(path);
					System.out.print("R{ ");
					for(int i=0;i<size;i++)
						System.out.printf("%3d ",(reds[i]>=0)?reds[i]:256+reds[i]);
					System.out.println("}");
					System.out.print("G{ ");
					for(int i=0;i<size;i++)
						System.out.printf("%3d ",(greens[i]>=0)?greens[i]:256+greens[i]);
					System.out.println("}");
					System.out.print("B{ ");
					for(int i=0;i<size;i++)
						System.out.printf("%3d ",(blues[i]>=0)?blues[i]:256+blues[i]);
					System.out.println("}");
					System.out.println();
	    		}
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
    			//
	    		if(debug)
	    		{	System.out.print("R{ ");
					for(int j=0;j<size;j++)
						System.out.printf("%3d ",(reds[j]>=0)?reds[j]:256+reds[j]);
					System.out.println("}");
					System.out.print("G{ ");
					for(int j=0;j<size;j++)
						System.out.printf("%3d ",(greens[j]>=0)?greens[j]:256+greens[j]);
					System.out.println("}");
					System.out.print("B{ ");
					for(int j=0;j<size;j++)
						System.out.printf("%3d ",(blues[j]>=0)?blues[j]:256+blues[j]);
					System.out.println("}");
					System.out.println();
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
	    		
//	    		BufferedImage copy = new BufferedImage(image.getWidth(),image.getHeight(),image.getType(),newCm);
//   	  		Graphics2D g2d = copy.createGraphics();
//    	  		g2d.drawImage(image,0,0,null);
//   	  		g2d.dispose();
    	  		image = copy;  	  		
    		}
    	}
    	
    	// optimizing  	
    	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		ColorModel cm = image.getColorModel();			
		int transparency = cm.getTransparency();
		BufferedImage result =  gc.createCompatibleImage(image.getWidth(), image.getHeight(),transparency);
		// create a graphics context
		Graphics2D g2d = result.createGraphics();
		// copy image
		g2d.drawImage(image,0,0,null);
		g2d.dispose();
		result = image;
		//
    	return result;
    }
}
