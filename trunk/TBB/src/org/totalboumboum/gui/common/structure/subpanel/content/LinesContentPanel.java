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
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LinesContentPanel extends ContentPanel
{	private static final long serialVersionUID = 1L;
	
	public LinesContentPanel(int width, int height, int lines, boolean header)
	{	this(width,height,lines,1,header);		
	}
	
	public LinesContentPanel(int width, int height, int lines, int cols, boolean header)
	{	super(width,height);
//		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		setOpaque(false);
		
		this.header = header;

		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);

		this.lines = 0;
		setDim(width,height);		
		reinit(lines,cols);
	}

	public void reinit(int lines, int cols)
	{	// content
		removeAll();
		this.lines = 0;
		for(int line=0;line<lines;line++)
			addLine(line,cols);
		setDim(getWidth(),getHeight());
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void switchDisplay(boolean display)
	{	for(int line=0;line<lines;line++)
			getLine(line).switchDisplay(display);
	}	
	
	/////////////////////////////////////////////////////////////////
	// FONTS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Font headerFont;
	private Font lineFont;
	private int lineFontSize;
	private int headerFontSize;

	public Font getHeaderFont()
	{	return headerFont;	
	}
	
	public Font getLineFont()
	{	return lineFont;	
	}
	
	public int getLineFontSize()
	{	return lineFontSize;	
	}
	
	public int getHeaderFontSize()
	{	return headerFontSize;	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEADER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean header;
	
	public boolean hasHeader()
	{	return header;	
	}

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Integer> lineHeights = new ArrayList<Integer>();
	private int headerWidth;
	private int lineWidth;

	public int getHeaderHeight()
	{	int result = getHeight();
		if(lineHeights.size()>0)
			result = lineHeights.get(0);
		return result;
	}
	
	public int getLineHeight(int line)
	{	return lineHeights.get(line);	
	}
	
	public int getLineHeight()
	{	int result = getHeight();
		if(lineHeights.size()>0)
			result = lineHeights.get(lineHeights.size()-1);
		return result;
	}
	
	public void setDim(int width, int height)
	{	super.setDim(width,height);
		lineHeights.clear();
		// lines
		
		if(lines==0)
		{	if(header)
				headerFontSize = GuiTools.getFontSize(getHeaderHeight()*GuiTools.FONT_RATIO);
			else
				headerFontSize = 0;
		}
		else
		{	int margins = (lines-1)*GuiTools.subPanelMargin;
			// with header
			if(header)
			{	int lineHeight = (int)((height - margins)/(lines+GuiTools.TABLE_HEADER_RATIO-1));
				int headerHeight = (int)(lineHeight*GuiTools.TABLE_HEADER_RATIO);
				headerFontSize = GuiTools.getFontSize(headerHeight*GuiTools.FONT_RATIO);
				lineHeights.add(headerHeight);
				for(int i=1;i<lines;i++)
					lineHeights.add(lineHeight);
			}
			// without header
			else
			{	int lineHeight = (int)((height - margins)/((float)lines));
				headerFontSize = 0;
				for(int i=0;i<lines;i++)
					lineHeights.add(lineHeight);
			}
			// adjust line heights
			int leftOver = height - margins;
			for(Integer lh: lineHeights)
				leftOver = leftOver - lh;
			int line = 0;
			while(leftOver>0)
			{	int lh = lineHeights.get(line);
				lh++;
				lineHeights.set(line,lh++);
				leftOver--;
				line = (line + 1) % lines;				
			}			
		}

		lineFontSize = GuiTools.getFontSize(getLineHeight()*GuiTools.FONT_RATIO);
		lineFont = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)lineFontSize);
		lineWidth = width;
		headerFont = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)headerFontSize);
		headerWidth = lineWidth;
		// content
		updateLinesHeights();
	}
	
	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int lines = 0;

	public Line getLine(int line)
	{	
//if(line>=getComponentCount())		
//	System.out.println();
		return (Line)getComponent(line*2);		
	}

	public int getLineCount()
	{	return lines;
	}
	
	public void addLine(int index)
	{	addLine(index,1);		
	}
	
	public void addLine(int index, int cols)
	{	if(index>0)
//			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*index-1);
			add(Box.createGlue(),2*index-1);
		else if(lines>0)
//			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*index);
			add(Box.createGlue(),2*index);
			
		lines++;
		Line line;
		
		// header
		if(header && index==0)
		{	line = new Line(headerWidth,getHeaderHeight(),cols);
			line.setBackgroundColor(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			line.setForegroundColor(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
		}
		//data
		else
			line = new Line(lineWidth,getLineHeight(),cols);

		line.setAlignmentX(CENTER_ALIGNMENT);
		line.setAlignmentY(CENTER_ALIGNMENT);
		add(line,2*index);
	}
	
	public void setLineKeys(int line, List<String> keys, List<Boolean> imageFlags)
	{	Line l = getLine(line);
		l.setKeys(keys,imageFlags);
	}
	
	public void setLineIcons(int line, List<BufferedImage> icons, List<String> tooltips)
	{	Line l = getLine(line);
		l.setIcons(icons,tooltips);
	}
	
	public void setLineTexts(int line, List<String> texts, List<String> tooltips)
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
	
	private void updateLinesHeights()
	{	for(int line=0;line<lines;line++)
		{	Line ln = getLine(line);
			int lineHeight = lineHeights.get(line);
			ln.setDim(lineWidth,lineHeight);
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int[] getLabelPosition(MyLabel label)
	{	int result[] = {-1, -1};
		int line = 0;
		while(line<lines && result[0]==-1)
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
	
	public MyLabel getLabel(int line, int col)
	{	Line l = getLine(line);
		return l.getLabel(col);
	}
	
	public void addLabel(int line, int col)
	{	Line l = getLine(line);
		l.addLabel(col);
		Font font;
		if(line==0 && header)
			font = headerFont;
		else
			font = lineFont;
		l.getLabel(col).setFont(font);
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

	public int getLabelColumn(int line, MyLabel label)
	{	int result = -1;
		Line l = getLine(line);
		result = l.getLabelColumn(label);
		return result;
	}
	
	public void setLabelMinWidth(int line, int col, int width)
	{	setLabelWidth(line,col,width,0);
	}
	public void setLabelPrefWidth(int line, int col, int width)
	{	setLabelWidth(line,col,width,1);
	}
	public void setLabelMaxWidth(int line, int col, int width)
	{	setLabelWidth(line,col,width,2);
	}
	public void setLabelWidth(int line, int col, int width, int mode)
	{	Line l = getLine(line);
		l.setLabelWidth(col,width,mode);
	}
	
	public void unsetLabelMinWidth(int line, int col)
	{	Line l = getLine(line);
		l.unsetLabelMinWidth(col);
	}
	public void unsetLabelPrefWidth(int line, int col)
	{	Line l = getLine(line);
		l.unsetLabelPreferredWidth(col);
	}
	public void unsetLabelMaxWidth(int line, int col)
	{	Line l = getLine(line);
		l.unsetLabelMaxWidth(col);
	}
	public void unsetLabelWidth(int line, int col, int mode)
	{	Line l = getLine(line);
		l.unsetLabelWidth(col,mode);
	}
}
