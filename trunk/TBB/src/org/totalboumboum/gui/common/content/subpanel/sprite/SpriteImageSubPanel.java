package org.totalboumboum.gui.common.content.subpanel.sprite;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import org.totalboumboum.engine.content.sprite.SpritePreview;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.ColumnsSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.Column;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.images.ImageTools;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * This class is used to display sprites in the
 * resource browser. It includes a set of color
 * buttons, allowing to see the various version
 * of colored sprites such as characters and bombs.
 * 
 * @author Vincent Labatut
 */
public class SpriteImageSubPanel extends ColumnsSubPanel implements MouseListener
{	/** Class id for serialization */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds an empty panel.
	 * 
	 * @param width
	 * 		Width of this panel, in pixels.
	 * @param height
	 * 		Height of this panel, in pixels.
	 */
	public SpriteImageSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,3);
		setSpritePreview(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// IMAGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object containing the image to be displayed */
	private SpritePreview spritePreview;
	/** Currently selected color (can be {@code null}) */
	private PredefinedColor selectedColor;
	
	/**
	 * Returns the object containing
	 * the currently displayed image.
	 * 
	 * @return
	 * 		The current sprite preview object.
	 */
	public SpritePreview getSpritePreview()
	{	return spritePreview;	
	}
	
	/**
	 * Changes the sprite displayed by
	 * this panel.
	 * 
	 * @param spritePreview
	 * 		The preview object for the new sprite.
	 */
	public void setSpritePreview(SpritePreview spritePreview)
	{	this.spritePreview = spritePreview;
		selectedColor = null;
		
		reinit(3);
		
		// sizes
		PredefinedColor colorValues[] = PredefinedColor.values();		
		lines = colorValues.length/2;
		if(colorValues.length%2 > 0)
			lines++;
		lineHeight = (getDataHeight() - (lines-1)*GuiTools.subPanelMargin)/lines;
		int firstLineHeight = getDataHeight() - (lines-1)*GuiTools.subPanelMargin - (lines-1)*lineHeight;
		if(showColors)
			rightWidth = getDataWidth() - 2*lineHeight - 2*GuiTools.subPanelMargin;
		else
			rightWidth = getDataWidth();

		int col = 0;
		// colors 0
		{	Column cl = getColumn(col);
			cl.setDim(lineHeight,getDataHeight());
			for(int i=1;i<lines;i++)
				cl.addLabel(0);
			for(int line=0;line<lines;line++)
			{	int h = lineHeight;
				if(line==0)
					h = firstLineHeight;
				cl.setLabelMinHeight(line,h);
				cl.setLabelPreferredHeight(line,h);
				cl.setLabelMaxHeight(line,h);
				MyLabel label = cl.getLabel(line);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
			}
			Color bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			cl.setBackgroundColor(bg);
			col++;
		}
		
		// colors 1
		{	Column cl = getColumn(col);
			cl.setDim(lineHeight,getDataHeight());
			for(int i=1;i<lines;i++)
				cl.addLabel(0);
			for(int line=0;line<lines;line++)
			{	int h = lineHeight;
				if(line==0)
					h = firstLineHeight;
				cl.setLabelMinHeight(line,h);
				cl.setLabelPreferredHeight(line,h);
				cl.setLabelMaxHeight(line,h);
				MyLabel label = cl.getLabel(line);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
			}
			Color bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			cl.setBackgroundColor(bg);
			col++;
		}
		
		// image
		{	Column cl = getColumn(col);
			cl.setDim(rightWidth,getDataHeight());
			int line = 0;
			cl.setLabelMinHeight(line,getDataHeight());
			cl.setLabelPreferredHeight(line,getDataHeight());
			cl.setLabelMaxHeight(line,getDataHeight());
			MyLabel label = cl.getLabel(line);
			label.addMouseListener(this);
			label.setMouseSensitive(true);
			Color bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			cl.setBackgroundColor(bg);
			col++;
		}
		
		selectColor(null);
	}
	
	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Numbers of lines of color buttons */
	private int lines;
	/** Height of the color buttons */
	private int lineHeight;
	/** Width of the color buttons */
	private int rightWidth;

	/**
	 * Changes the color of the sprite
	 * currently displayed (can be {@code null}.
	 * 
	 * @param color
	 * 		New color.
	 */
	private void selectColor(PredefinedColor color)
	{	BufferedImage image;
		selectedColor = color;
		
		PredefinedColor colorValues[] = PredefinedColor.values();
		boolean colors[] = new boolean[PredefinedColor.values().length];
		// no sprite selected
		if(spritePreview==null)
		{	image = null;
			for(int i=0;i<colors.length;i++)
				colors[i] = false;
		}
		// one sprite selected
		else
		{	image = spritePreview.getImage(selectedColor);
			for(int i=0;i<colors.length;i++)
				if(spritePreview.hasColor(colorValues[i]))
					colors[i] = true;
		}

		int line = 0;
		int col = 0;
		int cols = 2;
		for(int i=0;i<colors.length;i++)
		{	// colors
			String text = null;
			String tooltip = null;
			Color bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			if(colors[i])
			{	String colorKey = colorValues[i].toString();
				colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
				colorKey = GuiKeys.COMMON_COLOR+colorKey;
				text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey); 
				tooltip = text;
				bg = colorValues[i].getColor();
			}
			setLabelBackground(line,col,bg);
			setLabelText(line,col,text,tooltip);
			// index
			col++;
			if(col==cols)
			{	col=0;
				line++;
			}
		}
		// image
		MyLabel label = getLabel(0,2);
		Dimension prefDim = label.getPreferredSize();
		int imgWidth = (int)(prefDim.width*0.9);
		int imgHeight = (int)(prefDim.height*0.9);
		if(image!=null)
		{	float zoomX = imgWidth/(float)image.getWidth();
			float zoomY = imgHeight/(float)image.getHeight();
			float zoom = Math.min(zoomX,zoomY);
			image = ImageTools.getResizedImage(image,zoom,true);
			ImageIcon icon = new ImageIcon(image);
			label.setIcon(icon);
			label.setText(null);
			Color bg;
			if(selectedColor==null)
				bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
			else
			{	Color clr = selectedColor.getColor();
				int alpha = GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
				bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				
			}
			label.setBackground(bg);
		}
		else
		{	label.setIcon(null);
			label.setText(null);
			Color bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			label.setBackground(bg);
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether or not color buttons should be displayed (i.e. only for colored sprites) */
	private boolean showColors = true;
	
	/**
	 * Hides/shows the color buttons.
	 * 
	 * @param showColors
	 * 		{@code true} for show, {@code false} for hide.
	 */
	public void setShowColors(boolean showColors)
	{	this.showColors = showColors;
	}

	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e)
	{	
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{	
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	MyLabel label = (MyLabel)e.getComponent();
		// colors
		int[] pos = getLabelPosition(label);
		PredefinedColor color = null;
		if(pos[1]==0 || pos[1]==1)
		{	PredefinedColor colors[] = PredefinedColor.values();
			int index = pos[0]*2+pos[1];
			color = colors[index];
		}
		selectColor(color);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
