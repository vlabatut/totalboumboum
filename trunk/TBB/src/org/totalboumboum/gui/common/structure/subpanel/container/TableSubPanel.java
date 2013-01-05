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

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.common.structure.subpanel.content.TableContentPanel;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class TableSubPanel extends SubPanel<TableContentPanel>
{	private static final long serialVersionUID = 1L;

	public TableSubPanel(int width, int height, Mode mode, int lines, int cols, boolean header)
	{	this(width,height,mode,lines,1,cols,header);		
	}
	
	public TableSubPanel(int width, int height, Mode mode, int lines, int colGroups, int colSubs, boolean header)
	{	super(width,height,mode);

		TableContentPanel tablePanel = new TableContentPanel(getDataWidth(),getDataHeight(),lines,colGroups,colSubs,header);
		setDataPanel(tablePanel);
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
	public void reinit(int lines, int colGroups, int colSubs)
	{	getDataPanel().reinit(lines,colGroups,colSubs);		
	}
	
	public void reinit(int lines, int cols)
	{	reinit(lines,1,cols);		
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

	public void setLineKeysSimple(int line, List<String> keys, List<Boolean> imageFlags)
	{	getDataPanel().setLineKeysSimple(line,keys,imageFlags);
	}
	public void setLineKeysMultiple(int line, List<List<String>> keys, List<List<Boolean>> imageFlags)
	{	getDataPanel().setLineKeysMultiple(line,keys,imageFlags);
	}
	
	public void setLineIconsSimple(int line, List<BufferedImage> icons, List<String> tooltips)
	{	getDataPanel().setLineIconsSimple(line,icons,tooltips);
	}
	public void setLineIconsMultiple(int line, List<List<BufferedImage>> icons, List<List<String>> tooltips)
	{	getDataPanel().setLineIconsMultiple(line,icons,tooltips);
	}
	
	public void setLineTextsSimple(int line, List<String> texts, List<String> tooltips)
	{	getDataPanel().setLineTextsSimple(line,texts,tooltips);
	}
	public void setLineTextsMultiple(int line, List<List<String>> texts, List<List<String>> tooltips)
	{	getDataPanel().setLineTextsMultiple(line,texts,tooltips);
	}

	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int[] getLabelPositionSimple(MyLabel label)
	{	return getDataPanel().getLabelPositionSimple(label);
	}
	public int[] getLabelPositionMultiple(MyLabel label)
	{	return getDataPanel().getLabelPositionMultiple(label);
	}
	
	public MyLabel getLabel(int line, int col)
	{	return getLabel(line,0,col);
	}
	public MyLabel getLabel(int line, int colGroup, int colSub)
	{	return getDataPanel().getLabel(line,colGroup,colSub);
	}
	
	public void setLabelKey(int line, int col, String key, boolean imageFlag)
	{	setLabelKey(line,0,col,key,imageFlag);
	}
	public void setLabelKey(int line, int colGroup, int colSub, String key, boolean imageFlag)
	{	getDataPanel().setLabelKey(line,colGroup,colSub,key,imageFlag);
	}

	public void setLabelIcon(int line, int col, BufferedImage icon, String tooltip)
	{	setLabelIcon(line,0,col,icon,tooltip);
	}
	public void setLabelIcon(int line, int colGroup, int colSub, BufferedImage icon, String tooltip)
	{	getDataPanel().setLabelIcon(line,colGroup,colSub,icon,tooltip);
	}

	public void setLabelText(int line, int col, String text, String tooltip)
	{	setLabelText(line,0,col,text,tooltip);
	}
	public void setLabelText(int line, int colGroup, int colSub, String text, String tooltip)
	{	getDataPanel().setLabelText(line,colGroup,colSub,text,tooltip);
	}
	
	public void setLabelBackground(int line, int col, Color bg)
	{	setLabelBackground(line,0,col,bg);
	}
	public void setLabelBackground(int line, int colGroup, int colSub, Color bg)
	{	getDataPanel().setLabelBackground(line,colGroup,colSub,bg);
	}
	
	public void setLabelForeground(int line, int col, Color fg)
	{	setLabelForeground(line,0,col,fg);
	}
	public void setLabelForeground(int line, int colGroup, int colSub, Color fg)
	{	getDataPanel().setLabelForeground(line,colGroup,colSub,fg);
	}

	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setColSubMinWidth(int colSub, int width)
	{	getDataPanel().setColSubMinWidth(colSub,width);
	}
	public void setColSubPrefWidth(int colSub, int width)
	{	getDataPanel().setColSubPrefWidth(colSub,width);
	}
	public void setColSubMaxWidth(int colSub, int width)
	{	getDataPanel().setColSubMaxWidth(colSub,width);
	}
	public void setColSubWidth(int colSub, int width, int mode)
	{	getDataPanel().setColSubWidth(colSub,width,mode);
	}
	
	public void unsetColSubMinWidth(int colSub)
	{	getDataPanel().unsetColSubMinWidth(colSub);
	}
	public void unsetColSubPrefWidth(int colSub)
	{	getDataPanel().unsetColSubPrefWidth(colSub);
	}
	public void unsetColSubMaxWidth(int colSub)
	{	getDataPanel().unsetColSubMaxWidth(colSub);
	}
	public void unsetColSubWidth(int colSub, int mode)
	{	getDataPanel().unsetColSubWidth(colSub,mode);
	}
	
	public List<MyLabel> getColumn(int col)
	{	return getDataPanel().getColumn(col);
	}
	public List<MyLabel> getColSub(int colGroup, int colSub)
	{	return getDataPanel().getColSub(colGroup,colSub);
	}
	
	public int getColumnCount()
	{	return getDataPanel().getColumnCount();
	}
	public int getColGroupCount()
	{	return getDataPanel().getColGroupCount();
	}
	public int getColSubCount()
	{	return getDataPanel().getColSubCount();
	}
	
	public void addColumn(int index)
	{	getDataPanel().addColumn(index);
	}
	public void addColSub(int subIndex)
	{	getDataPanel().addColSub(subIndex);
	}
	public void addColGroup(int groupIndex)
	{	getDataPanel().addColGroup(groupIndex);
	}
	public void setColumnKeys(int col, List<String> keys, List<Boolean> imageFlags)
	{	getDataPanel().setColumnKeys(col,keys,imageFlags);
	}
	public void setColumnKeys(int colGroup, int colSub, List<String> keys, List<Boolean> imageFlags)
	{	getDataPanel().setColumnKeys(colGroup,colSub,keys,imageFlags);
	}
	public void setColumnIcons(int col, List<BufferedImage> icons, List<String> tooltips)
	{	getDataPanel().setColumnIcons(col,icons,tooltips);
	}
	public void setColumnIcons(int colGroup, int colSub, List<BufferedImage> icons, List<String> tooltips)
	{	getDataPanel().setColumnIcons(colGroup,colSub,icons,tooltips);
	}
	public void setColumnTexts(int col, List<String> texts, List<String> tooltips)
	{	getDataPanel().setColumnTexts(col,texts,tooltips);
	}
	public void setColumnTexts(int colGroup, int colSub, List<String> texts, List<String> tooltips)
	{	getDataPanel().setColumnTexts(colGroup,colSub,texts,tooltips);
	}
	public void setColumnBackground(int col, Color bg)
	{	getDataPanel().setColumnBackground(col,bg);
	}
	public void setColumnBackground(int colGroup, int colSub, Color bg)
	{	getDataPanel().setColumnBackground(colGroup,colSub,bg);
	}
	public void setColumnForeground(int col, Color fg)
	{	getDataPanel().setColumnForeground(col,fg);
	}
	public void setColumnForeground(int colGroup, int colSub, Color fg)
	{	getDataPanel().setColumnForeground(colGroup,colSub,fg);
	}


	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setContentBySubLineKeys(List<List<String>> keys, List<List<Boolean>> imageFlags, boolean wholeLine)
	{	getDataPanel().setContentBySubLineKeys(keys,imageFlags,wholeLine);
	}
	public void setContentBySubLineIcon(List<List<BufferedImage>> icons, List<List<String>> tooltips, boolean wholeLine)
	{	getDataPanel().setContentBySubLineIcon(icons,tooltips,wholeLine);
	}
	public void setContentBySubLineText(List<List<String>> texts, List<List<String>> tooltips, boolean wholeLine)
	{	getDataPanel().setContentBySubLineText(texts,tooltips,wholeLine);
	}
}
