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

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.feature.ImageShift;
import org.totalboumboum.tools.images.ImageTools;


public class AnimeStep implements Serializable
{	private static final long serialVersionUID = 1L;

	public AnimeStep()
	{	image = null;
		duration = 0;
		xShift = 0;
		yShift = 0;
		shadow = null;
		shadowXShift = 0;
		shadowYShift = 0;
		boundYShift = ImageShift.DOWN;
	}
	
	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient long imageSize = 0;
	private transient BufferedImage image;

	public BufferedImage getImage()
	{	return image;
	}
	
	public void setImage(BufferedImage image, long size)
	{	this.image = image;
		this.imageSize = size;
	}
	
	/////////////////////////////////////////////////////////////////
	// DURATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long duration;

	public long getDuration()
	{	return duration;
	}
	
	public void setDuration(long duration)
	{	this.duration = duration;
	}
	
	/////////////////////////////////////////////////////////////////
	// SHIFTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double xShift;
	private double yShift;

	public double getXShift()
	{	return xShift;
	}
	
	public void setXShift(double xShift)
	{	this.xShift = xShift;
	}

	public double getYShift()
	{	return yShift;
	}
	
	public void setYShift(double yShift)
	{	this.yShift = yShift;
	}

	/////////////////////////////////////////////////////////////////
	// SHADOW			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient long shadowSize = 0;
	private transient BufferedImage shadow;

	public boolean hasShadow()
	{	return shadow != null;
	}
	
	public void setShadow(BufferedImage shadow, long size)
	{	this.shadow = shadow;
		this.shadowSize = size;
	}
	
	public BufferedImage getShadow()
	{	return shadow;
	}

	/////////////////////////////////////////////////////////////////
	// SHADOW SHIFTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double shadowXShift;
	private double shadowYShift;

	public double getShadowXShift()
	{	return shadowXShift;
	}
	public void setShadowXShift(double shadowXShift)
	{	this.shadowXShift = shadowXShift;
	}

	public double getShadowYShift()
	{	return shadowYShift;
	}
	public void setShadowYShift(double shadowYShift)
	{	this.shadowYShift = shadowYShift;
	}

	/////////////////////////////////////////////////////////////////
	// BOUND SHIFTS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ImageShift boundYShift;

	public ImageShift getBoundYShift()
	{	return boundYShift;
	}
	
	public void setBoundYShift(ImageShift boundYShift)
	{	this.boundYShift = boundYShift;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	
	public AnimeStep copy(ArrayList<BufferedImage> images, ArrayList<BufferedImage> copyImages)
	{	AnimeStep result = new AnimeStep();
		// image
		if(image!=null)
		{	int index = images.indexOf(image);
			BufferedImage copyImg = copyImages.get(index);
			result.setImage(copyImg);
		}
		// duration
		result.setDuration(getDuration());
		// shifts
		result.setXShift(xShift);
		result.setYShift(yShift);
		// shadow
		if(shadow!=null)
		{	int index = images.indexOf(shadow);
			BufferedImage copyImg = copyImages.get(index);
			result.setShadow(copyImg);
		}
		result.setShadowXShift(shadowXShift);
		result.setShadowYShift(shadowYShift);
		// bound
		result.setBoundYShift(boundYShift);
		//
		return result;
	}
*/
	public AnimeStep copy()
	{	AnimeStep result = new AnimeStep();
		
		// image
		result.setImage(image,imageSize);
		
		// duration
		result.setDuration(duration);
		
		// shifts
		result.setXShift(xShift);
		result.setYShift(yShift);
		
		// shadow
		result.setShadow(shadow,shadowSize);
		result.setShadowXShift(shadowXShift);
		result.setShadowYShift(shadowYShift);
		
		// bound
		result.setBoundYShift(boundYShift);

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			// misc
			boundYShift = null;
			image = null;
			shadow = null;
		}
	}

	/////////////////////////////////////////////////////////////////
	// I/O				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	private void writeObject(ObjectOutputStream out) throws IOException
	{	// all fields except images
		out.defaultWriteObject();
		
		// image
		if(this.image != null)
		{	BufferedImageWrapper cp = new BufferedImageWrapper(image);
			out.writeObject(new Boolean(true));
			out.writeObject(cp);
		}
		else
			out.writeObject(new Boolean(false));
		
		// shadow
		if(this.shadow != null)
		{	BufferedImageWrapper cp = new BufferedImageWrapper(shadow);
			out.writeObject(new Boolean(true));
			out.writeObject(cp);
		}
		else
			out.writeObject(new Boolean(false));
	}
*/
/*	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{	// all fields except images
		in.defaultReadObject();
		
		// image
		Boolean flag = (Boolean)in.readObject();
		if(flag)
		{	BufferedImageWrapper cp = (BufferedImageWrapper) in.readObject();
			image = cp.getIm();
		}

		// image
		flag = (Boolean)in.readObject();
		if(flag)
		{	BufferedImageWrapper cp = (BufferedImageWrapper) in.readObject();
			shadow = cp.getIm();
		}
	}
*/
	/////////////////////////////////////////////////////////////////
	// CACHE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public long getMemSize()
	{	long result = 0;
		result = imageSize + shadowSize;
		return result;
	}
	
	public AnimeStep cacheCopy(double zoom, HashMap<BufferedImage,BufferedImage> imgs)
	{	AnimeStep result = new AnimeStep();
		
		// image
		if(image!=null)
		{	BufferedImage copyImg = imgs.get(image);
			if(copyImg==null)
			{	copyImg = ImageTools.resize(image,zoom,Configuration.getVideoConfiguration().getSmoothGraphics());
				imgs.put(image,copyImg);
			}
			result.setImage(copyImg,Math.round(Math.pow(zoom,2)*imageSize));
		}
		
		// duration
		result.duration = duration;
		
		// shifts
		result.xShift = xShift*zoom;
		result.yShift =  yShift*zoom;
		
		// shadow
		if(shadow!=null)
		{	BufferedImage copyImg = imgs.get(shadow);
			if(copyImg==null)
			{	copyImg = ImageTools.resize(shadow,zoom,Configuration.getVideoConfiguration().getSmoothGraphics());
				imgs.put(shadow,copyImg);
			}
			result.setShadow(copyImg,Math.round(Math.pow(zoom,2)*shadowSize));
		}
		result.shadowXShift = shadowXShift*zoom;
		result.shadowYShift = shadowYShift*zoom;
		
		// bound
		result.boundYShift = boundYShift;

		return result;
	}
}
