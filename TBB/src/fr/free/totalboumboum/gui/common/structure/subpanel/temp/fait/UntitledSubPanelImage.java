package fr.free.totalboumboum.gui.common.structure.subpanel.temp.fait;

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class UntitledSubPanelImage extends SubPanel
{	private static final long serialVersionUID = 1L;

	public UntitledSubPanelImage(int width, int height)
	{	super(width,height);
		
		int margin = GuiTools.subPanelMargin;
		//background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS);
		setLayout(layout);

		// inner label
		imageLabel = new JLabel();
		imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		imageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setVerticalAlignment(SwingConstants.CENTER);
		// dimension
		imageLabelHeight = height - 2*margin;
		imageLabelWidth = width - 2*margin;
		Dimension dim = new Dimension(imageLabelWidth,imageLabelHeight);
		imageLabel.setMinimumSize(dim);
		imageLabel.setPreferredSize(dim);
		imageLabel.setMaximumSize(dim);
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
		// add to SubPanel
		add(Box.createVerticalGlue());
		add(imageLabel);
		add(Box.createVerticalGlue());
	}
	
	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JLabel imageLabel;
	private int imageLabelHeight;
	private int imageLabelWidth;
	private BufferedImage image;

	public void setImage(BufferedImage newImage, String tooltip)
	{	if(newImage!=null)
		{	// resize
			float zoomX = imageLabelWidth/(float)newImage.getWidth();
			float zoomY = imageLabelHeight/(float)newImage.getHeight();
			float zoom = Math.min(zoomX,zoomY);
			image = ImageTools.resize(newImage,zoom,true);
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
