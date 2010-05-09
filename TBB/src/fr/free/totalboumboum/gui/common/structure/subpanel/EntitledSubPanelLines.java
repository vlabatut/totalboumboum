package fr.free.totalboumboum.gui.common.structure.subpanel;

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
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.tools.GuiTools;

public class EntitledSubPanelLines extends EntitledSubPanel
{	private static final long serialVersionUID = 1L;

	public EntitledSubPanelLines(int width, int height, int lines)
	{	super(width,height);

		// init table
		remove(0);
		add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),0);
		remove(4); 	// remove glue
//		remove(2); 	// remove glue

		setNewTable(lines);
	}
	
	private UntitledSubPanelLines getTable()
	{	return (UntitledSubPanelLines)getDataPanel();
	}
	
	public void setNewTable(int lines)
	{	int tableHeight = height - getTitleHeight() - GuiTools.subPanelMargin;
		int tableWidth = width;
		UntitledSubPanelLines tablePanel = new UntitledSubPanelLines(tableWidth,tableHeight,lines,false);
		tablePanel.setOpaque(false);
		setDataPanel(tablePanel);
	}

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getLineFontSize()
	{	return getTable().getLineFontSize();	
	}
	public int getHeaderFontSize()
	{	return getTable().getHeaderFontSize();	
	}
	public int getHeaderHeight()
	{	return getTable().getHeaderHeight();	
	}
	public int getLineHeight()
	{	return getTable().getLineHeight();	
	}

	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Line getLine(int line)
	{	return getTable().getLine(line);
	}

	public int getLineCount()
	{	return getTable().getLineCount();
	}
	
	public void addLine(int index)
	{	getTable().addLine(index);
	}
	
	public void setLineKeys(int line, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	getTable().setLineKeys(line,keys,imageFlags);
	}
	
	public void setLineIcons(int line, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	getTable().setLineIcons(line,icons,tooltips);
	}
	
	public void setLineTexts(int line, ArrayList<String> texts, ArrayList<String> tooltips)
	{	getTable().setLineTexts(line,texts,tooltips);
	}

	public void setLineBackground(int line, Color bg)
	{	getTable().setLineBackground(line,bg);
	}

	public void setLineForeground(int line, Color fg)
	{	getTable().setLineForeground(line,fg);
	}
	
	public void removeAllLines()
	{	getTable().removeAllLines();
	}

	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int[] getLabelPosition(JLabel label)
	{	return getTable().getLabelPosition(label);
	}
	
	public JLabel getLabel(int line, int col)
	{	return getTable().getLabel(line,col);
	}
	
	public void addLabel(int line, int col)
	{	getTable().addLabel(line,col);
	}

	public void setLabelKey(int line, int col, String key, boolean imageFlag)
	{	getTable().setLabelKey(line,col,key,imageFlag);
	}

	public void setLabelIcon(int line, int col, BufferedImage icon, String tooltip)
	{	getTable().setLabelIcon(line,col,icon,tooltip);
	}

	public void setLabelText(int line, int col, String text, String tooltip)
	{	getTable().setLabelText(line,col,text,tooltip);
	}
	
	public void setLabelBackground(int line, int col, Color bg)
	{	getTable().setLabelBackground(line,col,bg);
	}
	
	public void setLabelForeground(int line, int col, Color fg)
	{	getTable().setLabelForeground(line,col,fg);
	}

	public int getLabelColumn(int line, JLabel label)
	{	return getTable().getLabelColumn(line,label);
	}
	
	public void setLabelMinWidth(int line, int col, int width)
	{	getTable().setLabelMinWidth(line,col,width);
	}
	public void setLabelPreferredWidth(int line, int col, int width)
	{	getTable().setLabelPreferredWidth(line,col,width);
	}
	public void setLabelMaxWidth(int line, int col, int width)
	{	getTable().setLabelMaxWidth(line,col,width);
	}
	
	public void unsetLabelMinWidth(int line, int colSub)
	{	getTable().unsetLabelMinWidth(line,colSub);
	}
	public void unsetLabelPreferredWidth(int line, int colSub)
	{	getTable().unsetLabelPreferredWidth(line,colSub);
	}
	public void unsetLabelMaxWidth(int line, int colSub)
	{	getTable().unsetLabelMaxWidth(line,colSub);
	}
}
