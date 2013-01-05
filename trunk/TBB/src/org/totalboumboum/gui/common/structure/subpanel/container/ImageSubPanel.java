package org.totalboumboum.gui.common.structure.subpanel.container;

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

import org.totalboumboum.gui.common.structure.subpanel.content.ImageContentPanel;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ImageSubPanel extends SubPanel<ImageContentPanel>
{	private static final long serialVersionUID = 1L;
	
	public ImageSubPanel(int width, int height, Mode mode)
	{	super(width,height,mode);

		ImageContentPanel imagePanel = new ImageContentPanel(getDataWidth(),getDataHeight());
		setDataPanel(imagePanel);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setImage(BufferedImage newImage, String tooltip)
	{	getDataPanel().setImage(newImage,tooltip);
	}
	
	public BufferedImage getImage()
	{	return getDataPanel().getImage();		
	}
}
