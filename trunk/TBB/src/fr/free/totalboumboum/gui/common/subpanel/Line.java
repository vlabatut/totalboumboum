package fr.free.totalboumboum.gui.common.subpanel;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class Line extends SubPanel
{	
	private static final long serialVersionUID = 1L;
	private int lineFontSize;
	private int columns = 0;
	
	public Line(int width, int height, int cols)
	{	super(width, height);
		
		// background
		setOpaque(false);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// font
		lineFontSize = GuiTools.getFontSize(lineFontSize*GuiTools.FONT_RATIO);
		
		// columns
		for(int col=0;col<cols;col++)
			addColumn();
	}

	public void addColumn()
	{	if(columns>0)
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
		JLabel label = new JLabel();
		add(label);
		columns++;
	}
	
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public JLabel getLabel(int col)
	{	return (JLabel)getComponent(col*2+1);
	}
	
	public void setLabelKey(int col, String key, boolean imageFlag)
	{	String tooltip = GuiConfiguration.getLanguage().getText(key+GuiTools.TOOLTIP);
		// is there an available icon ?
		if(imageFlag)
		{	BufferedImage icon = GuiTools.getIcon(key);
			setLabelIcon(col,icon,tooltip);		
		}
		// if not : use text
		else
		{	String text = GuiConfiguration.getLanguage().getText(key);
			setLabelText(col,text,tooltip);
		}
	}

	public void setLabelIcon(int col, BufferedImage icon, String tooltip)
	{	JLabel label = getLabel(col);
		double zoom = height/(double)icon.getHeight();
		icon = ImageTools.resize(icon,zoom,true);
		ImageIcon icn = new ImageIcon(icon);
		label.setText(null);
		label.setIcon(icn);
		label.setToolTipText(tooltip);
	}

	public void setLabelText(int col, String text, String tooltip)
	{	JLabel label = getLabel(col);
		label.setIcon(null);
		label.setText(text);
		label.setToolTipText(tooltip);
	}
	
	public void setLabelBackground(int col, Color bg)
	{	JLabel label = getLabel(col);
		label.setBackground(bg);
	}
	
	public void setLabelForeground(int col, Color fg)
	{	JLabel label = getLabel(col);
		label.setForeground(fg);
	}

	public int getLabelColumn(JLabel label)
	{	int result = -1;
		int col = 0;
		while(col<columns && result==-1)
		{	JLabel l = getLabel(col);
			if(label == l)
				result = col;
			else
				col++;
		}
		return result;
	}
}
