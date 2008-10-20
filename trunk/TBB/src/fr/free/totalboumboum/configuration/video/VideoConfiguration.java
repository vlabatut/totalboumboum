package fr.free.totalboumboum.configuration.video;

import java.awt.Color;
import java.awt.Dimension;

public class VideoConfiguration
{
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Color borderColor;
	private boolean smoothGraphics;

	public void setSmoothGraphics(boolean smoothGraphics)
	{	this.smoothGraphics = smoothGraphics;		
	}
	public boolean getSmoothGraphics()
	{	return smoothGraphics;		
	}
	
	public Color getBorderColor()
	{	return borderColor;
	}
	public void setBorderColor(Color borderColor)
	{	this.borderColor = borderColor;
	}
	
	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Dimension panelDimension;
	
	public void setPanelDimension(int width, int height)
	{	panelDimension = new Dimension(width,height);
	}
	public Dimension getPanelDimension()
	{	return panelDimension;	
	}


}
