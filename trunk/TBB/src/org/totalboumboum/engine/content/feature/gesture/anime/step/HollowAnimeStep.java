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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.ImageShift;
import org.totalboumboum.engine.content.feature.gesture.anime.color.Colormap;

public class HollowAnimeStep extends AbstractAnimeStep implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> imagesFileNames = new ArrayList<String>();

	public List<String> getImagesFileNames()
	{	return imagesFileNames;
	}
	
	public void addImageFileName(String imageFileName, double xShift, double yShift, Colormap colormap)
	{	imagesFileNames.add(imageFileName);
		xShifts.add(xShift);
		yShifts.add(yShift);
		Configuration.getEngineConfiguration().addToImageCache(imageFileName,colormap);
	}
	
	/////////////////////////////////////////////////////////////////
	// SHADOW			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String shadowFileName = null;

	public void setShadowFileName(String shadowFileName)
	{	this.shadowFileName = shadowFileName;
		Configuration.getEngineConfiguration().addToImageCache(shadowFileName,null);
	}
	
	public String getShadowFileName()
	{	return shadowFileName;
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public HollowAnimeStep deepCopy(double zoom, PredefinedColor color) throws IOException
	{	HollowAnimeStep result = new HollowAnimeStep();
		
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
	// I/O				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeObject(ObjectOutputStream out) throws IOException
	{	// all fields except images
		out.defaultWriteObject();
		
		// image
		out.writeObject(new Integer(imagesFileNames.size()));
		for(int i=0;i<imagesFileNames.size();i++)
		{	String imageFileName = imagesFileNames.get(i);
			Double xShift = new Double(getXShifts().get(i));
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

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;

	public void finish()
	{	if(!finished)
		{	super.finish();

			// images
			imagesFileNames.clear();

			// misc
			shadowFileName = null;
		}
	}
}
