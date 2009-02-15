package fr.free.totalboumboum.gui.common.structure.subpanel.temp.fait.contentpanels;

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

import fr.free.totalboumboum.gui.common.structure.subpanel.temp.Column;
import fr.free.totalboumboum.gui.common.structure.subpanel.temp.fait.BasicSubPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class UntitledSubPanelColumns extends BasicSubPanel
{	private static final long serialVersionUID = 1L;

	public UntitledSubPanelColumns(int width, int height, int cols)
	{	super(width,height);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// size
		colWidth = (int)((width - (cols+1)*GuiTools.subPanelMargin)/((float)cols));
		colHeight = height - 2*GuiTools.subPanelMargin;
		
		// table
		add(Box.createHorizontalGlue());
		add(Box.createHorizontalGlue());
		for(int col=0;col<cols;col++)
			addColumn(col);
	}

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int colHeight;
	private int colWidth;

	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int cols = 0;

	public Column getColumn(int col)
	{	return (Column)getComponent(col*2+1);		
	}

	public int getColumnCount()
	{	return cols;
	}
	
	public void addColumn(int index)
	{	if(index>0)
		{	
//			JLabel lbl = new JLabel(".");
//			add(lbl,2*index);
//			lbl.setBackground(Color.WHITE);
//			lbl.setOpaque(true);			
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*index);
//			add(Box.createVerticalGlue(),2*index);
		}
		else if(cols>0)
		{	
//			JLabel lbl = new JLabel(".");
//			add(lbl,2*index);
//			lbl.setBackground(Color.WHITE);
//			lbl.setOpaque(true);
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*index+1);
//			add(Box.createVerticalGlue(),2*index,2*index+1);
		}
			
		cols++;
		Column column;
		
		column = new Column(colWidth,colHeight,1);

		add(column,2*index+1);
	}
	
	public void setColumnKeys(int col, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	Column l = getColumn(col);
		l.setKeys(keys,imageFlags);
	}
	
	public void setColumnIcons(int col, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	Column l = getColumn(col);
		l.setIcons(icons,tooltips);
	}
	
	public void setLineTexts(int col, ArrayList<String> texts, ArrayList<String> tooltips)
	{	Column l = getColumn(col);
		l.setTexts(texts,tooltips);
	}

	public void setLineBackground(int col, Color bg)
	{	Column l = getColumn(col);
		l.setBackgroundColor(bg);
	}

	public void setLineForeground(int col, Color fg)
	{	Column l = getColumn(col);
		l.setForegroundColor(fg);
	}
	
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int[] getLabelPosition(JLabel label)
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
	
	public JLabel getLabel(int line, int col)
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

	public int getLabelLine(int col, JLabel label)
	{	int result = -1;
		Column l = getColumn(col);
		result = l.getLabelLine(label);
		return result;
	}
	
	public void setLabelMinHeight(int line, int col, int height)
	{	Column l = getColumn(col);
		l.setLabelMinHeight(line,height);
	}
	public void setLabelPreferredHeight(int line, int col, int height)
	{	Column l = getColumn(col);
		l.setLabelPreferredHeight(line,height);
	}
	public void setLabelMaxHeight(int line, int col, int height)
	{	Column l = getColumn(col);
		l.setLabelMaxHeight(line,height);
	}
	
	public void unsetLabelMinHeight(int line, int col)
	{	Column l = getColumn(col);
		l.unsetLabelMinHeight(line);
	}
	public void unsetLabelPreferredHeight(int line, int col)
	{	Column l = getColumn(col);
		l.unsetLabelPreferredHeight(line);
	}
	public void unsetLabelMaxHeight(int line, int col)
	{	Column l = getColumn(col);
		l.unsetLabelMaxHeight(line);
	}
}
