package fr.free.totalboumboum.gui.common.structure.subpanel.outside;

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

import javax.swing.JLabel;

import fr.free.totalboumboum.gui.common.structure.subpanel.inside.Line;
import fr.free.totalboumboum.gui.common.structure.subpanel.inside.TableContentPanel;

public class TableSubPanel extends SubPanel<TableContentPanel>
{	private static final long serialVersionUID = 1L;

	public TableSubPanel(int width, int height, Mode mode, int lines, int cols, boolean header)
	{	super(width,height,mode);

		TableContentPanel tablePanel = new TableContentPanel(getDataWidth(),getDataHeight(),lines,cols,header);
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
	public void reinit(int lines, int cols)
	{	getDataPanel().reinit(lines,cols);		
	}

	public int getHeaderHeight()
	{	return getDataPanel().getHeaderHeight();	
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
	
	public void setLineKeys(int line, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	getDataPanel().setLineKeys(line,keys,imageFlags);
	}
	
	public void setLineIcons(int line, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	getDataPanel().setLineIcons(line,icons,tooltips);
	}
	
	public void setLineTexts(int line, ArrayList<String> texts, ArrayList<String> tooltips)
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
	public int[] getLabelPosition(JLabel label)
	{	return getDataPanel().getLabelPosition(label);
	}
	
	public JLabel getLabel(int line, int col)
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

	public int getLabelColumn(int line, JLabel label)
	{	return getDataPanel().getLabelColumn(line,label);
	}
	
	public void setLabelMinWidth(int line, int col, int width)
	{	getDataPanel().setLabelMinWidth(line,col,width);
	}
	public void setLabelPreferredWidth(int line, int col, int width)
	{	getDataPanel().setLabelPreferredWidth(line,col,width);
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
	public void unsetLabelPreferredWidth(int line, int colSub)
	{	getDataPanel().unsetLabelPreferredWidth(line,colSub);
	}
	public void unsetLabelMaxWidth(int line, int colSub)
	{	getDataPanel().unsetLabelMaxWidth(line,colSub);
	}
	public void unsetLabelWidth(int line, int col, int mode)
	{	getDataPanel().unsetLabelWidth(line,col,mode);
	}

	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setColSubMinWidth(int colSub, int width)
	{	getDataPanel().setColSubMinWidth(colSub,width);
	}
	public void setColSubPreferredWidth(int colSub, int width)
	{	getDataPanel().setColSubPreferredWidth(colSub,width);
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
	public void unsetColSubPreferredWidth(int colSub)
	{	getDataPanel().unsetColSubPreferredWidth(colSub);
	}
	public void unsetColSubMaxWidth(int colSub)
	{	getDataPanel().unsetColSubMaxWidth(colSub);
	}
	public void unsetColSubWidth(int colSub, int mode)
	{	getDataPanel().unsetColSubWidth(colSub,mode);
	}
	
	public ArrayList<JLabel> getColumn(int col)
	{	return getDataPanel().getColumn(col);
	}
	public ArrayList<JLabel> getColSub(int colGroup, int colSub)
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
	public void setColumnKeys(int col, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	getDataPanel().setColumnKeys(col,keys,imageFlags);
	}
	public void setColumnKeys(int colGroup, int colSub, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	getDataPanel().setColumnKeys(colGroup,colSub,keys,imageFlags);
	}
	public void setColumnIcons(int col, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	getDataPanel().setColumnIcons(col,icons,tooltips);
	}
	public void setColumnIcons(int colGroup, int colSub, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	getDataPanel().setColumnIcons(colGroup,colSub,icons,tooltips);
	}
	public void setColumnTexts(int col, ArrayList<String> texts, ArrayList<String> tooltips)
	{	getDataPanel().setColumnTexts(col,texts,tooltips);
	}
	public void setColumnTexts(int colGroup, int colSub, ArrayList<String> texts, ArrayList<String> tooltips)
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
	public void setContentBySubLineKeys(ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlags, boolean wholeLine)
	{	getDataPanel().setContentBySubLineKeys(keys,imageFlags,wholeLine);
	}
	public void setContentBySubLineIcon(ArrayList<ArrayList<BufferedImage>> icons, ArrayList<ArrayList<String>> tooltips, boolean wholeLine)
	{	getDataPanel().setContentBySubLineIcon(icons,tooltips,wholeLine);
	}
	public void setContentBySubLineText(ArrayList<ArrayList<String>> texts, ArrayList<ArrayList<String>> tooltips, boolean wholeLine)
	{	getDataPanel().setContentBySubLineText(texts,tooltips,wholeLine);
	}
}
