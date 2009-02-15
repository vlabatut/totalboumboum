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
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.tools.GuiTools;

public class UntitledSubPanelLines extends SubPanel
{	private static final long serialVersionUID = 1L;

	public UntitledSubPanelLines(int width, int height, int lines, boolean header)
	{	super(width,height);
		
		// init
		this.header = header;
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// size
		if(header)
		{	lineHeight = (int)((height - (lines+1)*GuiTools.subPanelMargin)/(lines+GuiTools.TABLE_HEADER_RATIO-1));
			headerHeight = height - (lines+1)*GuiTools.subPanelMargin - lineHeight*(lines-1);
			headerFontSize = GuiTools.getFontSize(headerHeight*GuiTools.FONT_RATIO);
		}
		else
			lineHeight = (int)((height - (lines+1)*GuiTools.subPanelMargin)/((float)lines));
		lineFontSize = GuiTools.getFontSize(lineHeight*GuiTools.FONT_RATIO);
		lineWidth = width - 2*GuiTools.subPanelMargin;
		headerWidth = lineWidth;
		
		// table
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());
		for(int line=0;line<lines;line++)
			addLine(line);
	}

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean header;
	private int headerHeight;
	private int headerWidth;
	private int lineHeight;
	private int lineWidth;
	private int lineFontSize;
	private int headerFontSize;

	public int getLineFontSize()
	{	return lineFontSize;	
	}
	public int getHeaderFontSize()
	{	return headerFontSize;	
	}
	public int getHeaderHeight()
	{	return headerHeight;	
	}
	public int getLineHeight()
	{	return lineHeight;	
	}
	
	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int lines = 0;

	public Line getLine(int line)
	{	return (Line)getComponent(line*2+1);		
	}

	public int getLineCount()
	{	return lines;
	}
	
	public void addLine(int index)
	{	if(index>0)
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*index);
//			add(Box.createVerticalGlue(),2*index);
		else if(lines>0)
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*index+1);
//			add(Box.createVerticalGlue(),2*index+1);
			
		lines++;
		Line line;
		
		// header
		if(header && index==0)
		{	line = new Line(headerWidth,headerHeight,1);
			line.setBackgroundColor(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			line.setForegroundColor(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
		}
		//data
		else
			line = new Line(lineWidth,lineHeight,1);

		add(line,2*index+1);
	}
	
	public void setLineKeys(int line, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	Line l = getLine(line);
		l.setKeys(keys,imageFlags);
	}
	
	public void setLineIcons(int line, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	Line l = getLine(line);
		l.setIcons(icons,tooltips);
	}
	
	public void setLineTexts(int line, ArrayList<String> texts, ArrayList<String> tooltips)
	{	Line l = getLine(line);
		l.setTexts(texts,tooltips);
	}

	public void setLineBackground(int line, Color bg)
	{	Line l = getLine(line);
		l.setBackgroundColor(bg);
	}

	public void setLineForeground(int line, Color fg)
	{	Line l = getLine(line);
		l.setForegroundColor(fg);
	}
	
	public void removeAllLines()
	{	removeAll();
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());		
	}
	
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int[] getLabelPosition(JLabel label)
	{	int result[] = null;
		int line = 0;
		while(line<lines && result==null)
		{	Line l = getLine(line);
			int col = l.getLabelPosition(label);
			if(col!=-1)
			{	result = new int[2];
				result[0] = line;
				result[1] = col;
			}
			else	
				line++;
		}
		return result;
	}
	
	public JLabel getLabel(int line, int col)
	{	Line l = getLine(line);
		return l.getLabel(col);
	}
	
	public void addLabel(int line, int col)
	{	Line l = getLine(line);
		l.addLabel(col);
	}

	public void setLabelKey(int line, int col, String key, boolean imageFlag)
	{	Line l = getLine(line);
		l.setLabelKey(col,key,imageFlag);
	}

	public void setLabelIcon(int line, int col, BufferedImage icon, String tooltip)
	{	Line l = getLine(line);
		l.setLabelIcon(col,icon,tooltip);
	}

	public void setLabelText(int line, int col, String text, String tooltip)
	{	Line l = getLine(line);
		l.setLabelText(col,text,tooltip);
	}
	
	public void setLabelBackground(int line, int col, Color bg)
	{	Line l = getLine(line);
		l.setLabelBackground(col,bg);
	}
	
	public void setLabelForeground(int line, int col, Color fg)
	{	Line l = getLine(line);
		l.setLabelForeground(col,fg);
	}

	public int getLabelColumn(int line, JLabel label)
	{	int result = -1;
		Line l = getLine(line);
		result = l.getLabelColumn(label);
		return result;
	}
	
	public void setLabelMinWidth(int line, int col, int width)
	{	Line l = getLine(line);
		l.setLabelMinWidth(col,width);
	}
	public void setLabelPreferredWidth(int line, int col, int width)
	{	Line l = getLine(line);
		l.setLabelPreferredWidth(col,width);
	}
	public void setLabelMaxWidth(int line, int col, int width)
	{	Line l = getLine(line);
		l.setLabelMaxWidth(col,width);
	}
	
	public void unsetLabelMinWidth(int line, int colSub)
	{	Line l = getLine(line);
		l.unsetLabelMinWidth(colSub);
	}
	public void unsetLabelPreferredWidth(int line, int colSub)
	{	Line l = getLine(line);
		l.unsetLabelPreferredWidth(colSub);
	}
	public void unsetLabelMaxWidth(int line, int colSub)
	{	Line l = getLine(line);
		l.unsetLabelMaxWidth(colSub);
	}
}
