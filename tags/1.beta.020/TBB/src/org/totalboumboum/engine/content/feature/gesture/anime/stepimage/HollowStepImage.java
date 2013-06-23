package org.totalboumboum.engine.content.feature.gesture.anime.stepimage;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRule;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRulesMap;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowStepImage extends AbstractStepImage implements Serializable
{	private static final long serialVersionUID = 1L;

	public HollowStepImage(String imageFileName, double xShift, double yShift, ColorRulesMap colorRulesMap)
	{	super(xShift,yShift);
		this.fileName = imageFileName;
		this.colorRulesMap = colorRulesMap;
		if(colorRulesMap==null)
			System.out.println();
	}
	
	/////////////////////////////////////////////////////////////////
	// FILE NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String fileName = null;

	public String getImagesFileName()
	{	return fileName;
	}
		
	/////////////////////////////////////////////////////////////////
	// COLOR RULES MAP	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ColorRulesMap colorRulesMap = null;
	
	public ColorRulesMap getColorRulesMap()
	{	return colorRulesMap;
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used when generating an actual Factory from a HollowFactory
	 */
	public StepImage fill(double zoom, PredefinedColor color) throws IOException
	{	
//System.out.println(fileName);		
		ColorRule colorRule = colorRulesMap.getColorRule(color);
		BufferedImage image = Configuration.getEngineConfiguration().retrieveFromImageCache(fileName,colorRule,zoom);
		StepImage result = new StepImage(image,xShift*zoom,yShift*zoom);
		
		return result;
	}
}
