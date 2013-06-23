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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.images.ImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Line extends ContentPanel
{	
	private static final long serialVersionUID = 1L;
	private int columns = 0;
	
	public Line(int width, int height, int cols)
	{	super(width, height);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		setOpaque(false);

		setDim(width,height);
		
		// columns
		for(int col=0;col<cols;col++)
			addLabel(col);
	}

	public void setDim(int width, int height)
	{	super.setDim(width,height);
		// font
		lineFontSize = GuiTools.getFontSize(height*GuiTools.FONT_RATIO);
		lineFont = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)lineFontSize);
		// content
		updateLabelsHeights();
	}
	
	private void updateLabelsHeights()
	{	for(int col=0;col<columns;col++)
		{	MyLabel lbl = getLabel(col);
			lbl.setFont(lineFont);
			// minimum size
			Dimension min = lbl.getMinimumSize();
			if(min!=null)
			{	Dimension dim = new Dimension(min.width,getHeight());
				lbl.setMinimumSize(dim);
			}
			// preferred size
			Dimension pref = lbl.getPreferredSize();
			if(pref!=null)
			{	Dimension dim = new Dimension(pref.width,getHeight());
				lbl.setPreferredSize(dim);
			}
			// maximum size
			Dimension max = lbl.getMaximumSize();
			if(max!=null)
			{	Dimension dim = new Dimension(max.width,getHeight());
				lbl.setMaximumSize(dim);
			}
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void switchDisplay(boolean display)
	{	for(int col=0;col<columns;col++)
			getLabel(col).switchDisplay(display);
	}	
	
	/////////////////////////////////////////////////////////////////
	// FONT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int lineFontSize;
	private Font lineFont;
	
	public int getLineFontSize()
	{	return lineFontSize;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setKeys(List<String> keys, List<Boolean> imageFlags)
	{	Iterator<String> lineKeys = keys.iterator();
		Iterator<Boolean> lineFlags = imageFlags.iterator();
		int col = 0;
		while(lineKeys.hasNext())
		{	String key = lineKeys.next();
			Boolean flag = lineFlags.next();
			setLabelKey(col,key,flag);
			col++;
		}			
	}

	public void setIcons(List<BufferedImage> icons, List<String> tooltips)
	{	Iterator<BufferedImage> lineIcons = icons.iterator();
		Iterator<String> lineTooltips = tooltips.iterator();
		int col = 0;
		while(lineIcons.hasNext())
		{	BufferedImage icon = lineIcons.next();
			String tooltip = lineTooltips.next();
			setLabelIcon(col,icon,tooltip);
			col++;
		}			
	}

	public void setTexts(List<String> texts, List<String> tooltips)
	{	Iterator<String> lineTexts = texts.iterator();
		Iterator<String> lineTooltips = tooltips.iterator();
		int col = 0;
		while(lineTexts.hasNext())
		{	String text = lineTexts.next();
			String tooltip = lineTooltips.next();
			setLabelText(col,text,tooltip);
			col++;
		}			
	}

	public void setBackgroundColor(Color color)
	{	for(int col=0;col<columns;col++)
			setLabelBackground(col,color);
	}

	public void setForegroundColor(Color color)
	{	for(int col=0;col<columns;col++)
			setLabelForeground(col,color);
	}
		
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public MyLabel getLabel(int col)
	{	return (MyLabel)getComponent(col*2);
	}
	
	public void addLabel(int col)
	{	// separator
		if(col>0)
			add(Box.createGlue(),2*col-1);
//			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*col-1);
		else if(columns>0)
			add(Box.createGlue(),2*col);
//			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*col);
		// new label
		String txt = null;
		MyLabel lbl = new MyLabel(txt);
		lbl.setFont(lineFont);
		lbl.setAlignmentX(CENTER_ALIGNMENT);
		lbl.setAlignmentY(CENTER_ALIGNMENT);
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setVerticalAlignment(SwingConstants.CENTER);
		lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
		lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
		lbl.setOpaque(true);
//		Dimension dim = new Dimension(height,height);
//		lbl.setPreferredSize(dim);
		add(lbl,2*col);
		columns++;
	}

	public void setLabelKey(int col, String key, boolean imageFlag)
	{	String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		// is there an available icon ?
		if(imageFlag)
		{	BufferedImage icon = GuiTools.getIcon(key);
			setLabelIcon(col,icon,tooltip);		
		}
		// if not : use text
		else
		{	String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
			setLabelText(col,text,tooltip);
		}
	}

	public void setLabelIcon(int col, BufferedImage icon, String tooltip)
	{	MyLabel label = getLabel(col);
		double zoom = getHeight()/(double)icon.getHeight();
		icon = ImageTools.getResizedImage(icon,zoom,true);
		ImageIcon icn = new ImageIcon(icon);
		label.setText(null);
		label.setIcon(icn);
		label.setToolTipText(tooltip);
	}

	public void setLabelText(int col, String text, String tooltip)
	{	MyLabel label = getLabel(col);
		label.setIcon(null);
		label.setText(text);
		label.setToolTipText(tooltip);
	}
	
	public void setLabelBackground(int col, Color bg)
	{	MyLabel label = getLabel(col);
		label.setBackground(bg);
	}
	
	public void setLabelForeground(int col, Color fg)
	{	MyLabel label = getLabel(col);
		label.setForeground(fg);
	}

	public int getLabelColumn(MyLabel label)
	{	int result = -1;
		int col = 0;
		while(col<columns && result==-1)
		{	MyLabel l = getLabel(col);
			if(label == l)
				result = col;
			else
				col++;
		}
		return result;
	}
	
	public void setLabelMinWidth(int col, int width)
	{	setLabelWidth(col,width,0);		
	}
	public void setLabelPrefWidth(int col, int width)
	{	setLabelWidth(col,width,1);		
	}
	public void setLabelMaxWidth(int col, int width)
	{	setLabelWidth(col,width,2);		
	}
	public void setLabelWidth(int col, int width, int mode)
	{	Dimension lineDim = new Dimension(width,getHeight());
		MyLabel label = getLabel(col);
		switch(mode)
		{	case 0:
				label.setMinimumSize(lineDim);
				break;
			case 1:
				label.setPreferredSize(lineDim);
				break;
			case 2:
				label.setMaximumSize(lineDim);
				break;
		}
	}	

	public void unsetLabelMinWidth(int colSub)
	{	unsetLabelWidth(colSub,0);		
	}
	public void unsetLabelPreferredWidth(int colSub)
	{	unsetLabelWidth(colSub,1);		
	}
	public void unsetLabelMaxWidth(int colSub)
	{	unsetLabelWidth(colSub,2);		
	}
	public void unsetLabelWidth(int col, int mode)
	{	MyLabel label = getLabel(col);
		switch(mode)
		{	case 0:
				label.setMinimumSize(null);
				break;
			case 1:
				label.setPreferredSize(null);
				break;
			case 2:
				label.setMaximumSize(null);
				break;
		}
	}
	
	public int getLabelPosition(MyLabel label)
	{	int result = -1;
		int col = 0;
		while(col<columns && result==-1)
		{	MyLabel l = getLabel(col);
			if(l==label)
				result = col;
			else
				col++;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getColumnCount()
	{	return columns;	
	}
}
