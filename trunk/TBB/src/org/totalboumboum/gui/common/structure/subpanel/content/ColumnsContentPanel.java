package org.totalboumboum.gui.common.structure.subpanel.content;

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
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ColumnsContentPanel extends ContentPanel
{	private static final long serialVersionUID = 1L;

	public ColumnsContentPanel(int width, int height, int cols)
	{	super(width,height);
//		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);
		setOpaque(false);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);

		this.cols = 0; //temporary
		setDim(width,height);		
		reinit(cols);
	}

	public void reinit(int cols)
	{	// content
		removeAll();
		this.cols = 0;
		for(int col=0;col<cols;col++)
			addColumn(col);
		setDim(getWidth(),getHeight());
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void switchDisplay(boolean display)
	{	for(int col=0;col<cols;col++)
			getColumn(col).switchDisplay(display);
	}	
	
	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int colHeight;
	private int colWidth;

	public void setDim(int width, int height)
	{	super.setDim(width, height);
		
		// size
		if(cols==0)
		{	colWidth = width;			
		}
		else
		{	colWidth = (int)((width - (cols-1)*GuiSizeTools.subPanelMargin)/((float)cols));
		}
		colHeight = height;
		
		// content
		updateColumnsWidths();
	}
	
	public int getColumnHeight()
	{	return colHeight;		
	}

	public int getColumnWidth()
	{	return colWidth;		
	}

	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int cols = 0;

	public Column getColumn(int col)
	{	return (Column)getComponent(col*2);		
	}

	public int getColumnCount()
	{	return cols;
	}
	
	public void addColumn(int index)
	{	if(index>0)
			add(Box.createRigidArea(new Dimension(GuiSizeTools.subPanelMargin,GuiSizeTools.subPanelMargin)),2*index-1);
		else if(cols>0)
			add(Box.createRigidArea(new Dimension(GuiSizeTools.subPanelMargin,GuiSizeTools.subPanelMargin)),2*index);
			
		cols++;
		Column column;		
		column = new Column(colWidth,colHeight,1);
		column.setAlignmentX(CENTER_ALIGNMENT);
		column.setAlignmentY(CENTER_ALIGNMENT);
		add(column,2*index);
	}
	
	public void setColumnKeys(int col, List<String> keys, List<Boolean> imageFlags)
	{	Column l = getColumn(col);
		l.setKeys(keys,imageFlags);
	}
	
	public void setColumnIcons(int col, List<BufferedImage> icons, List<String> tooltips)
	{	Column l = getColumn(col);
		l.setIcons(icons,tooltips);
	}
	
	public void setColumnTexts(int col, List<String> texts, List<String> tooltips)
	{	Column l = getColumn(col);
		l.setTexts(texts,tooltips);
	}

	public void setColumnBackground(int col, Color bg)
	{	Column l = getColumn(col);
		l.setBackgroundColor(bg);
	}

	public void setColumnForeground(int col, Color fg)
	{	Column l = getColumn(col);
		l.setForegroundColor(fg);
	}

	private void updateColumnsWidths()
	{	for(int col=0;col<cols;col++)
		{	Column cl = getColumn(col);
			cl.setDim(colWidth,colHeight);
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int[] getLabelPosition(MyLabel label)
	{	int result[] = null;
		int col = 0;
		while(col<cols && result==null)
		{	Column l = getColumn(col);
			int line = l.getLabelPosition(label);
			if(line!=-1)
			{	result = new int[2];
				result[0] = line;
				result[1] = col;
			}
			else	
				col++;
		}
		return result;
	}
	
	public MyLabel getLabel(int line, int col)
	{	Column l = getColumn(col);
		return l.getLabel(line);
	}
	
	public void addLabel(int line, int col)
	{	Column l = getColumn(col);
		l.addLabel(line);
	}

	public void setLabelKey(int line, int col, String key, boolean imageFlag)
	{	Column l = getColumn(col);
		l.setLabelKey(line,key,imageFlag);
	}

	public void setLabelIcon(int line, int col, BufferedImage icon, String tooltip)
	{	Column l = getColumn(col);
		l.setLabelIcon(line,icon,tooltip);
	}

	public void setLabelText(int line, int col, String text, String tooltip)
	{	Column l = getColumn(col);
		l.setLabelText(line,text,tooltip);
	}
	
	public void setLabelBackground(int line, int col, Color bg)
	{	Column l = getColumn(col);
		l.setLabelBackground(line,bg);
	}
	
	public void setLabelForeground(int line, int col, Color fg)
	{	Column l = getColumn(col);
		l.setLabelForeground(line,fg);
	}

	public int getLabelColumn(int col, MyLabel label)
	{	int result = -1;
		Column l = getColumn(col);
		result = l.getLabelLine(label);
		return result;
	}
	
	public void setLabelMinHeight(int line, int col, int height)
	{	Column l = getColumn(col);
		l.setLabelMinHeight(line,height);
	}
	public void setLabelPrefHeight(int line, int col, int height)
	{	Column l = getColumn(col);
		l.setLabelPreferredHeight(line,height);
	}
	public void setLabelMaxHeight(int line, int col, int height)
	{	Column l = getColumn(col);
		l.setLabelMaxHeight(line,height);
	}
	public void setLabelHeight(int line, int col, int height, int mode)
	{	Column c = getColumn(col);
		c.setLabelHeight(line,height,mode);
	}
	
	public void unsetLabelMinHeight(int line, int col)
	{	Column l = getColumn(col);
		l.unsetLabelMinHeight(line);
	}
	public void unsetLabelPrefHeight(int line, int col)
	{	Column l = getColumn(col);
		l.unsetLabelPreferredHeight(line);
	}
	public void unsetLabelMaxHeight(int line, int col)
	{	Column l = getColumn(col);
		l.unsetLabelMaxHeight(line);
	}
	public void unsetLabelHeight(int line, int col, int mode)
	{	Column c = getColumn(col);
		c.unsetLabelHeight(line,mode);
	}
}
