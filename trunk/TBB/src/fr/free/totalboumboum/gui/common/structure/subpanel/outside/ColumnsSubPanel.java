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
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;

import fr.free.totalboumboum.gui.common.structure.subpanel.inside.Column;
import fr.free.totalboumboum.gui.common.structure.subpanel.inside.ColumnsContentPanel;

public class ColumnsSubPanel extends SubPanel<ColumnsContentPanel>
{	private static final long serialVersionUID = 1L;

	public ColumnsSubPanel(int width, int height, Mode mode, int cols)
	{	super(width,height,mode);

		ColumnsContentPanel linesPanel = new ColumnsContentPanel(getDataWidth(),getDataHeight(),cols);
		setDataPanel(linesPanel);
	}
	
	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void reinit(int cols)
	{	getDataPanel().reinit(cols);
	}

	public int getColumnHeight()
	{	return getDataPanel().getColumnHeight();	
	}
	public int getColumnWidth()
	{	return getDataPanel().getColumnWidth();	
	}

	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Column getColumn(int col)
	{	return getDataPanel().getColumn(col);
	}

	public int getColumnCount()
	{	return getDataPanel().getColumnCount();
	}
	
	public void addColumn(int index)
	{	getDataPanel().addColumn(index);
	}
	
	public void setColumnKeys(int col, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	getDataPanel().setColumnKeys(col,keys,imageFlags);
	}
	
	public void setColumnIcons(int col, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	getDataPanel().setColumnIcons(col,icons,tooltips);
	}
	
	public void setColumnTexts(int col, ArrayList<String> texts, ArrayList<String> tooltips)
	{	getDataPanel().setColumnTexts(col,texts,tooltips);
	}

	public void setColumnBackground(int col, Color bg)
	{	getDataPanel().setColumnBackground(col,bg);
	}

	public void setLineForeground(int col, Color fg)
	{	getDataPanel().setColumnForeground(col,fg);
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

	public int getLabelColumn(int col, JLabel label)
	{	return getDataPanel().getLabelColumn(col,label);
	}
	
	public void setLabelMinHeight(int line, int col, int width)
	{	getDataPanel().setLabelMinHeight(line,col,width);
	}
	public void setLabelPrefHeight(int line, int col, int width)
	{	getDataPanel().setLabelPrefHeight(line,col,width);
	}
	public void setLabelMaxHeight(int line, int col, int width)
	{	getDataPanel().setLabelMaxHeight(line,col,width);
	}
	public void setLabelHeight(int line, int col, int width, int mode)
	{	getDataPanel().setLabelHeight(line,col,width,mode);
	}
	
	public void unsetLabelMinHeight(int line, int colSub)
	{	getDataPanel().unsetLabelMinHeight(line,colSub);
	}
	public void unsetLabelPrefHeight(int line, int colSub)
	{	getDataPanel().unsetLabelPrefHeight(line,colSub);
	}
	public void unsetLabelMaxHeight(int line, int colSub)
	{	getDataPanel().unsetLabelMaxHeight(line,colSub);
	}
	public void unsetLabelHeight(int line, int col, int mode)
	{	getDataPanel().unsetLabelHeight(line,col,mode);
	}
}
