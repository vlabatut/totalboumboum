package org.totalboumboum.gui.common.structure.subpanel.container;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.common.structure.subpanel.content.LinesContentPanel;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LinesSubPanel extends SubPanel<LinesContentPanel>
{	private static final long serialVersionUID = 1L;

	public LinesSubPanel(int width, int height, Mode mode, int lines, int cols, boolean header)
	{	super(width,height,mode);

		LinesContentPanel linesPanel = new LinesContentPanel(getDataWidth(),getDataHeight(),lines,cols,header);
		setDataPanel(linesPanel);
	}
	
	/////////////////////////////////////////////////////////////////
	// FONT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Font getHeaderFont()
	{	return getDataPanel().getHeaderFont();	
	}
	
	public Font getLineFont()
	{	return getDataPanel().getLineFont();	
	}
	
	public int getLineFontSize()
	{	return getDataPanel().getLineFontSize();	
	}
	
	public int getHeaderFontSize()
	{	return getDataPanel().getHeaderFontSize();	
	}

	/////////////////////////////////////////////////////////////////
	// HEADER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean hasHeader()
	{	return getDataPanel().hasHeader();	
	}

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void reinit(int lines, int cols)
	{	getDataPanel().reinit(lines,cols);		
	}

	public int getHeaderHeight()
	{	return getDataPanel().getHeaderHeight();	
	}
	
	public int getLineHeight(int line)
	{	return getDataPanel().getLineHeight(line);
	}

	public int getLineHeight()
	{	return getDataPanel().getLineHeight();	
	}

	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Line getLine(int line)
	{	return getDataPanel().getLine(line);
	}

	public int getLineCount()
	{	return getDataPanel().getLineCount();
	}
	
	public void addLine(int index)
	{	getDataPanel().addLine(index);
	}
	
	public void addLine(int index, int cols)
	{	getDataPanel().addLine(index,cols);
	}
	
	public void setLineKeys(int line, List<String> keys, List<Boolean> imageFlags)
	{	getDataPanel().setLineKeys(line,keys,imageFlags);
	}
	
	public void setLineIcons(int line, List<BufferedImage> icons, List<String> tooltips)
	{	getDataPanel().setLineIcons(line,icons,tooltips);
	}
	
	public void setLineTexts(int line, List<String> texts, List<String> tooltips)
	{	getDataPanel().setLineTexts(line,texts,tooltips);
	}

	public void setLineBackground(int line, Color bg)
	{	getDataPanel().setLineBackground(line,bg);
	}

	public void setLineForeground(int line, Color fg)
	{	getDataPanel().setLineForeground(line,fg);
	}

	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int[] getLabelPosition(MyLabel label)
	{	return getDataPanel().getLabelPosition(label);
	}
	
	public MyLabel getLabel(int line, int col)
	{	return getDataPanel().getLabel(line,col);
	}
	
	public void addLabel(int line, int col)
	{	getDataPanel().addLabel(line,col);
	}

	public void setLabelKey(int line, int col, String key, boolean imageFlag)
	{	getDataPanel().setLabelKey(line,col,key,imageFlag);
	}

	public void setLabelIcon(int line, int col, BufferedImage icon, String tooltip)
	{	getDataPanel().setLabelIcon(line,col,icon,tooltip);
	}

	public void setLabelText(int line, int col, String text, String tooltip)
	{	getDataPanel().setLabelText(line,col,text,tooltip);
	}
	
	public void setLabelBackground(int line, int col, Color bg)
	{	getDataPanel().setLabelBackground(line,col,bg);
	}
	
	public void setLabelForeground(int line, int col, Color fg)
	{	getDataPanel().setLabelForeground(line,col,fg);
	}

	public int getLabelColumn(int line, MyLabel label)
	{	return getDataPanel().getLabelColumn(line,label);
	}
	
	public void setLabelMinWidth(int line, int col, int width)
	{	getDataPanel().setLabelMinWidth(line,col,width);
	}
	public void setLabelPrefWidth(int line, int col, int width)
	{	getDataPanel().setLabelPrefWidth(line,col,width);
	}
	public void setLabelMaxWidth(int line, int col, int width)
	{	getDataPanel().setLabelMaxWidth(line,col,width);
	}
	public void setLabelWidth(int line, int col, int width, int mode)
	{	getDataPanel().setLabelWidth(line,col,width,mode);
	}
	
	public void unsetLabelMinWidth(int line, int colSub)
	{	getDataPanel().unsetLabelMinWidth(line,colSub);
	}
	public void unsetLabelPrefWidth(int line, int colSub)
	{	getDataPanel().unsetLabelPrefWidth(line,colSub);
	}
	public void unsetLabelMaxWidth(int line, int colSub)
	{	getDataPanel().unsetLabelMaxWidth(line,colSub);
	}
	public void unsetLabelWidth(int line, int col, int mode)
	{	getDataPanel().unsetLabelWidth(line,col,mode);
	}
}
