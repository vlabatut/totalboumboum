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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorMap;

public class HollowAnimeStep extends AbstractAnimeStep implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> imagesFileNames = new ArrayList<String>();
	private List<ColorMap> imagesColorMaps = new ArrayList<ColorMap>();

	public List<String> getImagesFileNames()
	{	return imagesFileNames;
	}
	
	public void addImageFileName(String imageFileName, double xShift, double yShift, ColorMap colorMap)
	{	imagesFileNames.add(imageFileName);
		xShifts.add(xShift);
		yShifts.add(yShift);
		imagesColorMaps.add(colorMap);
	}
	
	/////////////////////////////////////////////////////////////////
	// SHADOW			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String shadowFileName = null;
	private ColorMap shadowColorMap = null;

	public void setShadowFileName(String shadowFileName, ColorMap shadowColorMap)
	{	this.shadowFileName = shadowFileName;
		this.shadowColorMap = shadowColorMap;	
		Configuration.getEngineConfiguration().addToImageCache(shadowFileName,shadowColorMap);
	}
	
	public String getShadowFileName()
	{	return shadowFileName;
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used to clone an abstract HollowFactory to be completed
	 * by additional data (useless for now, might be usefull later) 
	 */
/*	public HollowAnimeStep surfaceCopy()
	{	HollowAnimeStep result = new HollowAnimeStep();
		
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
*/
	/**
	 * used when generating an actual Factory from a HollowFactory
	 */
	public AnimeStep fill(double zoom, PredefinedColor color) throws IOException
	{	AnimeStep result = new AnimeStep();
		
		// images
		for(int i=0;i<imagesFileNames.size();i++)
		{	String imageFileName = imagesFileNames.get(i);
			BufferedImage image = Configuration.getEngineConfiguration().retrieveFromImageCache(imageFileName,color,zoom);
			double xShift = xShifts.get(i);
			double yShift = yShifts.get(i);
			result.addImage(image,xShift,yShift);
		}
		
		// duration
		result.duration = duration;
		
		// shadow
		if(shadowFileName!=null)
		{	BufferedImage shadow = Configuration.getEngineConfiguration().retrieveFromImageCache(shadowFileName,color,zoom);
			result.setShadow(shadow,shadowXShift,shadowYShift);
		}
		
		// bound
		result.boundYShift = boundYShift;

		return result;
	}
	
	public void cache()
	{	// images
		for(int i=0;i<imagesFileNames.size();i++)
		{	String imageFileName = imagesFileNames.get(i);
			ColorMap colorMap = imagesColorMaps.get(i);
			Configuration.getEngineConfiguration().addToImageCache(imageFileName,colorMap);
		}
		
		// shadow
		if(shadowFileName!=null)
			Configuration.getEngineConfiguration().addToImageCache(shadowFileName,shadowColorMap);
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();

			// images
			imagesFileNames.clear();
			shadowFileName = null;

			// misc
		}
	}
}
