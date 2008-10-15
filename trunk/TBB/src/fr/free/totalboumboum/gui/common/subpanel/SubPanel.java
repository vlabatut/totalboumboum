package fr.free.totalboumboum.gui.common.subpanel;

import java.awt.Dimension;

import javax.swing.JPanel;

public class SubPanel extends JPanel
{	private static final long serialVersionUID = 1L;
	
	public SubPanel(int width, int height)
	{	this.width = width;
		this.height = height;
		Dimension dim = new Dimension(width,height);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
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
}
