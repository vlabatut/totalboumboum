package fr.free.totalboumboum.gui.common.structure.subpanel.inside;

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
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;

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
	private int headerHeight;
	private int headerWidth;
	private int lineHeight;
	private int lineWidth;

	public int getHeaderHeight()
	{	return headerHeight;	
	}
	
	public int getLineHeight()
	{	return lineHeight;	
	}
	
	public void setDim(int width, int height)
	{	super.setDim(width,height);
		// lines
		
		if(lines==0)
		{	if(header)
			{	lineHeight = 0;
				headerHeight = height;
				headerFontSize = GuiTools.getFontSize(headerHeight*GuiTools.FONT_RATIO);
			}
			else
			{	lineHeight = height;
				headerHeight = 0;
				headerFontSize = 0;
			}
		}
		else
		{	if(header)
			{	lineHeight = (int)((height - (lines-1)*GuiTools.subPanelMargin)/(lines+GuiTools.TABLE_HEADER_RATIO-1));
				headerHeight = height - (lines-1)*GuiTools.subPanelMargin - lineHeight*(lines-1);
				if(headerHeight-(lines-1)>=lineHeight)
				{	headerHeight = headerHeight - (lines-1);
					lineHeight ++;
				}
				headerFontSize = GuiTools.getFontSize(headerHeight*GuiTools.FONT_RATIO);
			}
			else
			{	lineHeight = (int)((height - (lines-1)*GuiTools.subPanelMargin)/((float)lines));
				headerHeight = 0;
				headerFontSize = 0;
			}
		}
		
/*		
int total;
if(header)
	total = height - headerHeight - (lines-1)*lineHeight;
else
	total = height - lines*lineHeight;
while(lines>1 && total%(lines-1)!=0)
{	lineHeight--;
	if(header)
		headerHeight--;
	//
	if(header)
		total = height - headerHeight - (lines-1)*lineHeight;
	else
		total = height - lines*lineHeight;
}
*/

		lineFontSize = GuiTools.getFontSize(lineHeight*GuiTools.FONT_RATIO);
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
if(line>=getComponentCount())		
	System.out.println();
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
		{	line = new Line(headerWidth,headerHeight,cols);
			line.setBackgroundColor(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			line.setForegroundColor(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
		}
		//data
		else
			line = new Line(lineWidth,lineHeight,cols);

		line.setAlignmentX(CENTER_ALIGNMENT);
		line.setAlignmentY(CENTER_ALIGNMENT);
		add(line,2*index);
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
	
	private void updateLinesHeights()
	{	int start = 0;
		if(header && lines>start)
		{	Line ln = getLine(start);
			ln.setDim(headerWidth,headerHeight);
			start = 1;			
		}
		for(int line=start;line<lines;line++)
		{	Line ln = getLine(line);
			ln.setDim(lineWidth,lineHeight);
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int[] getLabelPosition(JLabel label)
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
	
	public JLabel getLabel(int line, int col)
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

	public int getLabelColumn(int line, JLabel label)
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
