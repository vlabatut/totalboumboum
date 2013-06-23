package org.totalboumboum.engine.content.feature.gesture.anime.stepimage;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

/**
 * 
 * @author Vincent Labatut
 *
 */
public class StepImage extends AbstractStepImage
{	
	public StepImage(BufferedImage image, double xShift, double yShift)
	{	super(xShift,yShift);
		this.image = image;
	}

	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage image = null;

	public BufferedImage getImage()
	{	return image;
	}
	
	public void setImage(BufferedImage image)
	{	this.image = image;		
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public StepImage copy()
	{	StepImage result = new StepImage(image,xShift,yShift);
		return result;
	}
}
