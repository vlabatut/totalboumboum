package org.totalboumboum.engine.content.feature.gesture.anime.step;

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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.ImageShift;
import org.totalboumboum.engine.content.feature.gesture.anime.color.Colormap;

public class AnimeStep
{	private static final long serialVersionUID = 1L;

	public AnimeStep()
	{	duration = 0;
		shadow = null;
		shadowXShift = 0;
		shadowYShift = 0;
		boundYShift = ImageShift.DOWN;
	}
	
	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient List<BufferedImage> images = new ArrayList<BufferedImage>();
	private List<String> imagesFileNames = new ArrayList<String>();

	public List<BufferedImage> getImages()
	{	return images;
	}
	
	public void addImageFileName(String imageFileName, double xShift, double yShift, Colormap colormap)
	{	imagesFileNames.add(imageFileName);
		xShifts.add(xShift);
		yShifts.add(yShift);
		Configuration.getEngineConfiguration().addToImageCache(imageFileName,colormap);
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
	private transient List<Double> xShifts = new ArrayList<Double>();
	private transient List<Double> yShifts = new ArrayList<Double>();

	public List<Double> getXShifts()
	{	return xShifts;
	}
	
	public List<Double> getYShifts()
	{	return yShifts;
	}

	/////////////////////////////////////////////////////////////////
	// SHADOW			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient BufferedImage shadow = null;
	private String shadowFileName = null;

	public boolean hasShadow()
	{	return shadow != null;
	}
	
	public void setShadowFileName(String shadowFileName)
	{	this.shadowFileName = shadowFileName;
		Configuration.getEngineConfiguration().addToImageCache(shadowFileName,null);
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
	public AnimeStep surfaceCopy()
	{	AnimeStep result = new AnimeStep();
		
		// image
		result.imagesFileNames.addAll(imagesFileNames);
		
		// duration
		result.setDuration(duration);
		
		// shifts
		result.xShifts.addAll(xShifts);
		result.yShifts.addAll(yShifts);
		
		// shadow
		result.setShadowFileName(shadowFileName);
		result.setShadowXShift(shadowXShift);
		result.setShadowYShift(shadowYShift);
		
		// bound
		result.setBoundYShift(boundYShift);

		return result;
	}

	public AnimeStep deepCopy(double zoom, PredefinedColor color) throws IOException
	{	AnimeStep result = new AnimeStep();
		
		// images
		for(int i=0;i<imagesFileNames.size();i++)
		{	String imageFileName = imagesFileNames.get(i);
			double xShift = xShifts.get(i);
			double yShift = yShifts.get(i);
			result.imagesFileNames.add(imageFileName);
			BufferedImage image = Configuration.getEngineConfiguration().retrieveFromImageCache(imageFileName,color,zoom);
			result.images.add(image);
			result.xShifts.add(xShift*zoom);
			result.yShifts.add(yShift*zoom);
		}
		
		// duration
		result.duration = duration;
		
		// shadow
		if(shadowFileName!=null)
		{	result.shadowFileName = shadowFileName;
			BufferedImage image = Configuration.getEngineConfiguration().retrieveFromImageCache(shadowFileName,color,zoom);
			result.shadow = image;
		}
		result.shadowXShift = shadowXShift*zoom;
		result.shadowYShift = shadowYShift*zoom;
		
		// bound
		result.boundYShift = boundYShift;

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;

			// images
			imagesFileNames = null;
			images = null;
			shadowFileName = null;
			shadow = null;

			// misc
			boundYShift = null;
			shadow = null;
		}
	}

	/////////////////////////////////////////////////////////////////
	// I/O				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeObject(ObjectOutputStream out) throws IOException
	{	// all fields except images
		out.defaultWriteObject();
		
		// image
		out.writeObject(new Integer(imagesFileNames.size()));
		for(int i=0;i<imagesFileNames.size();i++)
		{	String imageFileName = imagesFileNames.get(i);
			Double xShift = new Double(xShifts.get(i));
			Double yShift = new Double(yShifts.get(i));
			out.writeObject(imageFileName);
			out.writeObject(xShift);
			out.writeObject(yShift);
		}
		
		// shadow
		if(this.shadowFileName != null)
		{	out.writeObject(new Boolean(true));
			out.writeObject(shadowFileName);
		}
		else
			out.writeObject(new Boolean(false));
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{	// all fields except images
		in.defaultReadObject();
		
		// image
		int count = (Integer) in.readObject();
		for(int i=0;i<count;i++)
		{	String imageFileName = (String) in.readObject();
			Double xShift = (Double) in.readObject();
			Double yShift = (Double) in.readObject();
			addImageFileName(imageFileName,xShift,yShift,null);//TODO to be adapted to sprites using colormaps			
		}

		// image
		boolean flag = (Boolean) in.readObject();
		if(flag)
		{	shadowFileName = (String) in.readObject();
		}
	}
}
