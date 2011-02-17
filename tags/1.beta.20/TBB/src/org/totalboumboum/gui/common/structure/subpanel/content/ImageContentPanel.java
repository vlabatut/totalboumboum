package org.totalboumboum.gui.common.structure.subpanel.content;

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.images.ImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ImageContentPanel extends ContentPanel
{	private static final long serialVersionUID = 1L;

	public ImageContentPanel(int width, int height)
	{	super(width,height);
//		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS);
		setLayout(layout);

		// inner label
		imageLabel = new MyLabel();
		imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		imageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setVerticalAlignment(SwingConstants.CENTER);
		// alignment
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setVerticalAlignment(SwingConstants.CENTER);
		// colors
		imageLabel.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
		imageLabel.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
		imageLabel.setOpaque(true);
		// content
		imageLabel.setText(null);
		imageLabel.setToolTipText(null);
		
		add(imageLabel);
		setDim(width,height);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void switchDisplay(boolean display)
	{	imageLabel.switchDisplay(display);
	}	
	
	/////////////////////////////////////////////////////////////////
	// DIMENSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void setDim(int width, int height)
	{	super.setDim(width, height);
		Dimension dim = new Dimension(width,height);
		imageLabel.setMinimumSize(dim);
		imageLabel.setPreferredSize(dim);
		imageLabel.setMaximumSize(dim);
	}
		
	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MyLabel imageLabel;
	private BufferedImage image;

	public void setImage(BufferedImage newImage, String tooltip)
	{	if(newImage!=null)
		{	// resize
			float zoomX = getWidth()/(float)newImage.getWidth();
			float zoomY = getHeight()/(float)newImage.getHeight();
			float zoom = Math.min(zoomX,zoomY);
			image = ImageTools.getResizedImage(newImage,zoom,true);
			// put image
			ImageIcon icon = new ImageIcon(image);
			imageLabel.setIcon(icon);
			// change color
			Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			imageLabel.setBackground(bg);
		}
		else
		{	// remove image
			imageLabel.setIcon(null);
			// change color
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			imageLabel.setBackground(bg);
		}
		// tooltip
		imageLabel.setToolTipText(tooltip);			
	}
	
	public BufferedImage getImage()
	{	return image;	
	}
}
