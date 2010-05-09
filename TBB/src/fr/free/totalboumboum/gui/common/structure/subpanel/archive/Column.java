package fr.free.totalboumboum.gui.common.structure.subpanel.archive;

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
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class Column extends SubPanel
{	
	private static final long serialVersionUID = 1L;
	private int lines = 0;
	
	public Column(int width, int height, int lines)
	{	super(width, height);
		
		// background
		setOpaque(false);
		
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// font
		float lineHeight = (height-GuiTools.subPanelMargin*(lines-1))/lines;
		lineFontSize = GuiTools.getFontSize(lineHeight*GuiTools.FONT_RATIO);
		lineFont = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)lineFontSize);
		
		// columns
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());
		for(int line=0;line<lines;line++)
			addLabel(line);
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
	{	Iterator<String> colKeys = keys.iterator();
		Iterator<Boolean> colFlags = imageFlags.iterator();
		int col = 0;
		while(colKeys.hasNext())
		{	String key = colKeys.next();
			Boolean flag = colFlags.next();
			setLabelKey(col,key,flag);
			col++;
		}			
	}

	public void setIcons(ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	Iterator<BufferedImage> colIcons = icons.iterator();
		Iterator<String> colTooltips = tooltips.iterator();
		int col = 0;
		while(colIcons.hasNext())
		{	BufferedImage icon = colIcons.next();
			String tooltip = colTooltips.next();
			setLabelIcon(col,icon,tooltip);
			col++;
		}			
	}

	public void setTexts(ArrayList<String> texts, ArrayList<String> tooltips)
	{	Iterator<String> colTexts = texts.iterator();
		Iterator<String> colTooltips = tooltips.iterator();
		int col = 0;
		while(colTexts.hasNext())
		{	String text = colTexts.next();
			String tooltip = colTooltips.next();
			setLabelText(col,text,tooltip);
			col++;
		}			
	}

	public void setBackgroundColor(Color color)
	{	for(int line=0;line<lines;line++)
			setLabelBackground(line,color);
	}

	public void setForegroundColor(Color color)
	{	for(int line=0;line<lines;line++)
			setLabelForeground(line,color);
	}
		
	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public JLabel getLabel(int line)
	{	return (JLabel)getComponent(line*2+1);
	}
	
	public void addLabel(int line)
	{	if(line>0)
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*line);
//			add(Box.createVerticalGlue(),2*line);
		else if(lines>0)
			add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),2*line+1);
//			add(Box.createVerticalGlue(),2*line+1);
		String txt = null;
		JLabel lbl = new JLabel(txt);
		lbl.setFont(lineFont);
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
		lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
		lbl.setOpaque(true);
//		Dimension dim = new Dimension(height,height);
//		lbl.setPreferredSize(dim);
		add(lbl,2*line+1);
		lines++;
	}

	public void setLabelKey(int line, String key, boolean imageFlag)
	{	String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		// is there an available icon ?
		if(imageFlag)
		{	BufferedImage icon = GuiTools.getIcon(key);
			setLabelIcon(line,icon,tooltip);		
		}
		// if not : use text
		else
		{	String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
			setLabelText(line,text,tooltip);
		}
	}

	public void setLabelIcon(int line, BufferedImage icon, String tooltip)
	{	JLabel label = getLabel(line);
		double zoom = width/(double)icon.getHeight();
		icon = ImageTools.resize(icon,zoom,true);
		ImageIcon icn = new ImageIcon(icon);
		label.setText(null);
		label.setIcon(icn);
		label.setToolTipText(tooltip);
	}

	public void setLabelText(int line, String text, String tooltip)
	{	JLabel label = getLabel(line);
		label.setIcon(null);
		label.setText(text);
		label.setToolTipText(tooltip);
	}
	
	public void setLabelBackground(int line, Color bg)
	{	JLabel label = getLabel(line);
		label.setBackground(bg);
	}
	
	public void setLabelForeground(int line, Color fg)
	{	JLabel label = getLabel(line);
		label.setForeground(fg);
	}

	public int getLabelLine(JLabel label)
	{	int result = -1;
		int line = 0;
		while(line<lines && result==-1)
		{	JLabel l = getLabel(line);
			if(label == l)
				result = line;
			else
				line++;
		}
		return result;
	}
	
	public void setLabelMinHeight(int line, int height)
	{	setLabelHeight(line,height,0);		
	}
	public void setLabelPreferredHeight(int line, int height)
	{	setLabelHeight(line,height,1);		
	}
	public void setLabelMaxHeight(int line, int height)
	{	setLabelHeight(line,height,2);		
	}
	private void setLabelHeight(int line, int height, int mode)
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

	public void unsetLabelMinHeight(int line)
	{	unsetLabelHeight(line,0);		
	}
	public void unsetLabelPreferredHeight(int line)
	{	unsetLabelHeight(line,1);		
	}
	public void unsetLabelMaxHeight(int line)
	{	unsetLabelHeight(line,2);		
	}
	private void unsetLabelHeight(int line, int mode)
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
		int line = 0;
		while(line<lines && result==-1)
		{	JLabel l = getLabel(line);
			if(l==label)
				result = line;
			else
				line++;
		}
		return result;
	}
	
	public int getLineCount()
	{	return lines;	
	}
}
