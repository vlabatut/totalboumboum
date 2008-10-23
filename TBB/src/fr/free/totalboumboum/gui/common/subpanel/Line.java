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
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class Line extends SubPanel
{	
	private static final long serialVersionUID = 1L;
	private int columns = 0;
	
	public Line(int width, int height, int cols)
	{	super(width, height);
		
		// background
		setOpaque(false);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// font
		lineFontSize = GuiTools.getFontSize(height*GuiTools.FONT_RATIO);
		lineFont = GuiConfiguration.getFont().deriveFont((float)lineFontSize);
		
		// columns
		add(Box.createHorizontalGlue());
		add(Box.createHorizontalGlue());
		for(int col=0;col<cols;col++)
			addLabel(col);
	}

	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int lineFontSize;
	private Font lineFont;
	
	public int getLineFontSize()
	{	return lineFontSize;	
	}
	
	public void setKeys(ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	Iterator<String> lineKeys = keys.iterator();
		Iterator<Boolean> lineFlags = imageFlags.iterator();
		int col = 0;
		while(lineKeys.hasNext())
		{	String key = lineKeys.next();
			Boolean flag = lineFlags.next();
			setLabelKey(col,key,flag);
			col++;
		}			
	}

	public void setIcons(ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	Iterator<BufferedImage> lineIcons = icons.iterator();
		Iterator<String> lineTooltips = tooltips.iterator();
		int col = 0;
		while(lineIcons.hasNext())
		{	BufferedImage icon = lineIcons.next();
			String tooltip = lineTooltips.next();
			setLabelIcon(col,icon,tooltip);
			col++;
		}			
	}

	public void setTexts(ArrayList<String> texts, ArrayList<String> tooltips)
	{	Iterator<String> lineTexts = texts.iterator();
		Iterator<String> lineTooltips = tooltips.iterator();
		int col = 0;
		while(lineTexts.hasNext())
		{	String text = lineTexts.next();
			String tooltip = lineTooltips.next();
			setLabelText(col,text,tooltip);
			col++;
		}			
	}

	public void setBackgroundColor(Color color)
	{	for(int col=0;col<columns;col++)
			setLabelBackground(col,color);
	}

	public void setForegroundColor(Color color)
	{	for(int col=0;col<columns;col++)
			setLabelForeground(col,color);
	}
		
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public JLabel getLabel(int col)
	{	return (JLabel)getComponent(col*2+1);
	}
	
	public void addLabel(int col)
	{	if(col>0)
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*col);
		else if(columns>0)
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*col+1);
		String txt = null;
		JLabel lbl = new JLabel(txt);
		lbl.setFont(lineFont);
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
		lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
		lbl.setOpaque(true);
//		Dimension dim = new Dimension(height,height);
//		lbl.setPreferredSize(dim);
		add(lbl,2*col+1);
		columns++;
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
	
	public void setLabelMinWidth(int col, int width)
	{	setLabelWidth(col,width,0);		
	}
	public void setLabelPreferredWidth(int col, int width)
	{	setLabelWidth(col,width,1);		
	}
	public void setLabelMaxWidth(int col, int width)
	{	setLabelWidth(col,width,2);		
	}
	private void setLabelWidth(int line, int width, int mode)
	{	Dimension lineDim = new Dimension(width,height);
		JLabel label = getLabel(line);
		switch(mode)
		{	case 0:
				label.setMinimumSize(lineDim);
				break;
			case 1:
				label.setPreferredSize(lineDim);
				break;
			case 2:
				label.setMaximumSize(lineDim);
				break;
		}
	}	

	public void unsetLabelMinWidth(int colSub)
	{	unsetLabelWidth(colSub,0);		
	}
	public void unsetLabelPreferredWidth(int colSub)
	{	unsetLabelWidth(colSub,1);		
	}
	public void unsetLabelMaxWidth(int colSub)
	{	unsetLabelWidth(colSub,2);		
	}
	private void unsetLabelWidth(int line, int mode)
	{	JLabel label = getLabel(line);
		switch(mode)
		{	case 0:
				label.setMinimumSize(null);
				break;
			case 1:
				label.setPreferredSize(null);
				break;
			case 2:
				label.setMaximumSize(null);
				break;
		}
	}
	
	public int getLabelPosition(JLabel label)
	{	int result = -1;
		int col = 0;
		while(col<columns && result==-1)
		{	JLabel l = getLabel(col);
			if(l==label)
				result = col;
			else
				col++;
		}
		return result;
	}

}
