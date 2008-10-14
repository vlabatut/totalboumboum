package fr.free.totalboumboum.gui.common;

import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class SubPanel extends JPanel
{	private static final long serialVersionUID = 1L;
	
	public SubPanel(int width, int height)
	{	setDim(width,height);
	}

	/////////////////////////////////////////////////////////////////
	// DIMENSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected int width;
	protected int height;
	
	public int getWidth()
	{	return width;
	}
	public int getHeight()
	{	return height;
	}
	public void setDim(int width, int height)
	{	this.width = width;
		this.height = height;
		Dimension dim = new Dimension(width,height);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
	}
	

}
