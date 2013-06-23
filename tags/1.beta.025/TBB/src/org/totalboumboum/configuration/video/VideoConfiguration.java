package org.totalboumboum.configuration.video;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class VideoConfiguration
{
	public VideoConfiguration copy()
	{	VideoConfiguration result = new VideoConfiguration();
		result.fullScreen = fullScreen; 
		result.smoothGraphics = smoothGraphics; 
		result.borderColor = borderColor;
		result.panelDimension = (Dimension)panelDimension.clone();
		return result;
	}

	
	/////////////////////////////////////////////////////////////////
	// DEVICE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GraphicsDevice graphicsDevice;
	
	public void setGraphicsDevice(GraphicsDevice graphicsDevice)
	{	this.graphicsDevice = graphicsDevice;		
	}
	public GraphicsDevice getGraphicsDevice()
	{	return graphicsDevice;		
	}

	/////////////////////////////////////////////////////////////////
	// FULL SCREEN		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean fullScreen;

	public void setFullScreen(boolean fullScreen)
	{	this.fullScreen = fullScreen;		
	}
	public boolean getFullScreen()
	{	return fullScreen;		
	}
	
	/////////////////////////////////////////////////////////////////
	// SMOOTHING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean smoothGraphics;

	public void setSmoothGraphics(boolean smoothGraphics)
	{	this.smoothGraphics = smoothGraphics;		
	}
	public boolean getSmoothGraphics()
	{	return smoothGraphics;		
	}
	
	/////////////////////////////////////////////////////////////////
	// BORDER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Color borderColor;

	public Color getBorderColor()
	{	return borderColor;
	}
	public void setBorderColor(Color borderColor)
	{	this.borderColor = borderColor;
	}
	
	/////////////////////////////////////////////////////////////////
	// PANEL DIMENSION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Dimension panelDimension;
	
	public void setPanelDimension(int width, int height)
	{	panelDimension = new Dimension(width,height);
	}
	public Dimension getPanelDimension()
	{	return panelDimension;	
	}
}
