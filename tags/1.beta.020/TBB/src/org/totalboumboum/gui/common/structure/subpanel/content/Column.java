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
public class Column extends ContentPanel
{	
	private static final long serialVersionUID = 1L;
	private int lines = 0;
	
	public Column(int width, int height, int lines)
	{	super(width, height);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
	
		setOpaque(false);
		
		setDim(width,height);
		
		// lines
		for(int line=0;line<lines;line++)
			addLabel(line);
	}

	public void setDim(int width, int height)
	{	super.setDim(width,height);
		// font
		float lineHeight;
		if(lines<=1)
			lineHeight = height;
		else
			lineHeight = (height-GuiTools.subPanelMargin*(lines-1))/lines;
		lineFontSize = GuiTools.getFontSize(lineHeight*GuiTools.FONT_RATIO);
		lineFont = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)lineFontSize);
		// content
		updateLabelsWidths();
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void switchDisplay(boolean display)
	{	for(int line=0;line<lines;line++)
			getLabel(line).switchDisplay(display);
	}	
	
	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int lineFontSize;
	private Font lineFont;
	
	public int getLineFontSize()
	{	return lineFontSize;	
	}
	
	public void setKeys(List<String> keys, List<Boolean> imageFlags)
	{	Iterator<String> colKeys = keys.iterator();
		Iterator<Boolean> colFlags = imageFlags.iterator();
		int col = 0;
		while(colKeys.hasNext())
		{	String key = colKeys.next();
			Boolean flag = colFlags.next();
			setLabelKey(col,key,flag);
			col++;
		}			
	}

	public void setIcons(List<BufferedImage> icons, List<String> tooltips)
	{	Iterator<BufferedImage> colIcons = icons.iterator();
		Iterator<String> colTooltips = tooltips.iterator();
		int col = 0;
		while(colIcons.hasNext())
		{	BufferedImage icon = colIcons.next();
			String tooltip = colTooltips.next();
			setLabelIcon(col,icon,tooltip);
			col++;
		}			
	}

	public void setTexts(List<String> texts, List<String> tooltips)
	{	Iterator<String> colTexts = texts.iterator();
		Iterator<String> colTooltips = tooltips.iterator();
		int col = 0;
		while(colTexts.hasNext())
		{	String text = colTexts.next();
			String tooltip = colTooltips.next();
			setLabelText(col,text,tooltip);
			col++;
		}			
	}

	public void setBackgroundColor(Color color)
	{	for(int line=0;line<lines;line++)
			setLabelBackground(line,color);
	}

	public void setForegroundColor(Color color)
	{	for(int line=0;line<lines;line++)
			setLabelForeground(line,color);
	}
		
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public MyLabel getLabel(int line)
	{	return (MyLabel)getComponent(line*2);
	}
	
	public void addLabel(int line)
	{	// separator
		if(line>0)
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*line-1);
		else if(lines>0)
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*line);
		// new label
		String txt = null;
		MyLabel lbl = new MyLabel(txt);
		lbl.setFont(lineFont);
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setAlignmentX(CENTER_ALIGNMENT);
		lbl.setAlignmentY(CENTER_ALIGNMENT);
		lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
		lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
		lbl.setOpaque(true);
//		Dimension dim = new Dimension(height,height);
//		lbl.setPreferredSize(dim);
		add(lbl,2*line);
		lines++;
	}

	public void setLabelKey(int line, String key, boolean imageFlag)
	{	String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		// is there an available icon ?
		if(imageFlag)
		{	BufferedImage icon = GuiTools.getIcon(key);
			setLabelIcon(line,icon,tooltip);		
		}
		// if not : use text
		else
		{	String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
			setLabelText(line,text,tooltip);
		}
	}

	public void setLabelIcon(int line, BufferedImage icon, String tooltip)
	{	MyLabel label = getLabel(line);
		double zoom = getWidth()/(double)icon.getHeight();
		icon = ImageTools.getResizedImage(icon,zoom,true);
		ImageIcon icn = new ImageIcon(icon);
		label.setText(null);
		label.setIcon(icn);
		label.setToolTipText(tooltip);
	}

	public void setLabelText(int line, String text, String tooltip)
	{	MyLabel label = getLabel(line);
		label.setIcon(null);
		label.setText(text);
		label.setToolTipText(tooltip);
	}
	
	public void setLabelBackground(int line, Color bg)
	{	MyLabel label = getLabel(line);
		label.setBackground(bg);
	}
	
	public void setLabelForeground(int line, Color fg)
	{	MyLabel label = getLabel(line);
		label.setForeground(fg);
	}

	public int getLabelLine(MyLabel label)
	{	int result = -1;
		int line = 0;
		while(line<lines && result==-1)
		{	MyLabel l = getLabel(line);
			if(label == l)
				result = line;
			else
				line++;
		}
		return result;
	}
	
	public void setLabelMinHeight(int line, int height)
	{	setLabelHeight(line,height,0);		
	}
	public void setLabelPreferredHeight(int line, int height)
	{	setLabelHeight(line,height,1);		
	}
	public void setLabelMaxHeight(int line, int height)
	{	setLabelHeight(line,height,2);		
	}
	public void setLabelHeight(int line, int height, int mode)
	{	Dimension lineDim = new Dimension(getWidth(),height);
		MyLabel label = getLabel(line);
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

	public void unsetLabelMinHeight(int line)
	{	unsetLabelHeight(line,0);		
	}
	public void unsetLabelPreferredHeight(int line)
	{	unsetLabelHeight(line,1);		
	}
	public void unsetLabelMaxHeight(int line)
	{	unsetLabelHeight(line,2);		
	}
	public void unsetLabelHeight(int line, int mode)
	{	MyLabel label = getLabel(line);
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
		int line = 0;
		while(line<lines && result==-1)
		{	MyLabel l = getLabel(line);
			if(l==label)
				result = line;
			else
				line++;
		}
		return result;
	}
	
	public int getLineCount()
	{	return lines;	
	}
	
	private void updateLabelsWidths()
	{	for(int line=0;line<lines;line++)
		{	MyLabel lbl = getLabel(line);
			// minimum size
			Dimension min = lbl.getMinimumSize();
			if(min!=null)
			{	Dimension dim = new Dimension(getWidth(),min.height);
				lbl.setMinimumSize(dim);
			}
			// preferred size
			Dimension pref = lbl.getPreferredSize();
			if(pref!=null)
			{	Dimension dim = new Dimension(getWidth(),pref.height);
				lbl.setPreferredSize(dim);
			}
			// maximum size
			Dimension max = lbl.getMaximumSize();
			if(max!=null)
			{	Dimension dim = new Dimension(getWidth(),max.height);
				lbl.setMaximumSize(dim);
			}
		}		
	}
}
