package fr.free.totalboumboum.gui.common.structure.subpanel;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
	
	public void setWidth(int width)
	{	this.width = width;
		Dimension dim = new Dimension(width,height);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
	}

	public int getHeight()
	{	return height;
	}
}
