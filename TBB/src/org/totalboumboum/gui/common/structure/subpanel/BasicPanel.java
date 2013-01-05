package org.totalboumboum.gui.common.structure.subpanel;

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

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class BasicPanel extends JPanel
{	private static final long serialVersionUID = 1L;
	
	public BasicPanel(int width, int height)
	{	this.width = width;
		this.height = height;
		Dimension dim = new Dimension(width,height);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
		
		setAlignmentX(CENTER_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void switchDisplay(boolean display)
	{		
	}	
	
	/////////////////////////////////////////////////////////////////
	// DIMENSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int width;
	private int height;
	
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
