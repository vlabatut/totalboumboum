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

import java.io.IOException;
import java.io.Serializable;

import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRulesMap;
import org.totalboumboum.engine.content.feature.gesture.anime.stepimage.HollowStepImage;
import org.totalboumboum.engine.content.feature.gesture.anime.stepimage.StepImage;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowAnimeStep extends AbstractAnimeStep<HollowStepImage> implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// IMAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addImage(String imageFileName, double xShift, double yShift, ColorRulesMap colorRulesMap)
	{	HollowStepImage image = new HollowStepImage(imageFileName,xShift,yShift,colorRulesMap);
		images.add(image);
	}
	
	/////////////////////////////////////////////////////////////////
	// SHADOW			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setShadow(String shadowFileName, double shadowXShift, double shadowYShift, ColorRulesMap shadowColorRulesMap)
	{	HollowStepImage image = new HollowStepImage(shadowFileName,shadowXShift,shadowYShift,shadowColorRulesMap);
		this.shadow = image;
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used to clone an abstract HollowFactory to be completed
	 * by additional data (useless for now, might be usefull later) 
	 */
/*	public HollowAnimeStep copy()
	{	HollowAnimeStep result = new HollowAnimeStep();
		
		// image
		result.imagesFileNames.addAll(imagesFileNames);
		result.imagesColorRulesMaps.addAll(imagesColorRulesMaps);
		
		// duration
		result.setDuration(duration);
		
		// shifts
		result.xShifts.addAll(xShifts);
		result.yShifts.addAll(yShifts);
		
		// shadow
		result.setShadowFileName(shadowFileName,shadowColorRulesMap);
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
		for(HollowStepImage image: images)
		{	StepImage imageCopy = image.fill(zoom,color);
			result.addImage(imageCopy);
		}
		
		// duration
		result.duration = duration;
		
		// shadow
		if(shadow!=null)
		{	StepImage shadowCopy = shadow.fill(zoom,color);
			result.setShadow(shadowCopy);
		}
		
		// bound
		result.boundYShift = boundYShift;

//if(result.getImages().size()>1)		
//	System.out.println();

		return result;
	}
/*	
	public void cache()
	{	// images
		for(int i=0;i<imagesFileNames.size();i++)
		{	String imageFileName = imagesFileNames.get(i);
			ColorRulesMap colorRulesMap = imagesColorRulesMaps.get(i);
			Configuration.getEngineConfiguration().addToImageCache(imageFileName,colorMap);
		}
		
		// shadow
		if(shadowFileName!=null)
			Configuration.getEngineConfiguration().addToImageCache(shadowFileName,shadowColorMap);
	}
*/
}
