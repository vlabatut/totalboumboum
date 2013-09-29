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
 * This class handles all options regarding
 * the video and graphics aspects.
 * 
 * @author Vincent Labatut
 */
public class VideoConfiguration
{
	/**
	 * Copy the current configuration,
	 * to be able to restore it later.
	 * 
	 * @return
	 * 		A copy of this object.
	 */
	public VideoConfiguration copy()
	{	VideoConfiguration result = new VideoConfiguration();
		result.fullScreen = fullScreen; 
		result.smoothGraphics = smoothGraphics; 
		result.borderColor = borderColor;
		result.panelDimension = (Dimension)panelDimension.clone();
		return result;
	}

	
	/////////////////////////////////////////////////////////////////
	// DEVICE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object used for displaying ingame graphics */
	private GraphicsDevice graphicsDevice;
	
	/**
	 * Change the graphics device.
	 * 
	 * @param graphicsDevice
	 * 		New graphics device.
	 */
	public void setGraphicsDevice(GraphicsDevice graphicsDevice)
	{	this.graphicsDevice = graphicsDevice;		
	}
	
	/**
	 * Returns the object used for displaying ingame graphics.
	 * 
	 * @return
	 * 		Current graphics device.
	 */
	public GraphicsDevice getGraphicsDevice()
	{	return graphicsDevice;		
	}

	/////////////////////////////////////////////////////////////////
	// FULL SCREEN		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if the fullscreen mode is on */
	private boolean fullScreen;

	/**
	 * Switches the fullscreen mode.
	 * 
	 * @param fullScreen
	 * 		If {@code true}, the fullscreen mode is switched one.
	 */
	public void setFullScreen(boolean fullScreen)
	{	this.fullScreen = fullScreen;		
	}
	
	/**
	 * Indicates the current state
	 * of the fullscreen mode.
	 * 
	 * @return
	 * 		{@code true} iff the fullscreen mode is on.
	 */
	public boolean getFullScreen()
	{	return fullScreen;		
	}
	
	/////////////////////////////////////////////////////////////////
	// SMOOTHING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether antialiasing is enabled */
	private boolean smoothGraphics;

	/**
	 * Switches the anti-aliasing option.
	 * 
	 * @param smoothGraphics
	 * 		If {@code true}, the antialiasing mode is switched on.
	 */
	public void setSmoothGraphics(boolean smoothGraphics)
	{	this.smoothGraphics = smoothGraphics;		
	}
	
	/**
	 * Indicates the current state
	 * of the antialiasing mode.
	 * 
	 * @return
	 * 		{@code true} iff the antialiasing mode is on.
	 */
	public boolean getSmoothGraphics()
	{	return smoothGraphics;		
	}
	
	/////////////////////////////////////////////////////////////////
	// BORDER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current color of the ingame border */
	private Color borderColor;

	/**
	 * Returns the current color of the 
	 * ingame border, i.e. the part of the
	 * screen not containing the level.
	 * 
	 * @return
	 * 		Color of the ingame border.
	 */
	public Color getBorderColor()
	{	return borderColor;
	}
	
	/**
	 * Changes the color of the ingame
	 * border (part of the screen not
	 * displaying any part of the level).
	 * 
	 * @param borderColor
	 * 		New border color.
	 */
	public void setBorderColor(Color borderColor)
	{	this.borderColor = borderColor;
	}
	
	/////////////////////////////////////////////////////////////////
	// PANEL DIMENSION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Dimension of the panel displaying the game */
	private Dimension panelDimension;
	
	/**
	 * Changes the dimensions of the game panel.
	 * 
	 * @param width
	 * 		Width in pixels.
	 * @param height
	 * 		Height in pixels.
	 */
	public void setPanelDimension(int width, int height)
	{	panelDimension = new Dimension(width,height);
	}
	
	/**
	 * Returns the dimensions (in pixels)
	 * of the panel displying the game.
	 * 
	 * @return
	 * 		Dimensions of the panel displaying the game. 
	 */
	public Dimension getPanelDimension()
	{	return panelDimension;	
	}
}
