package org.totalboumboum.gui.common.structure.subpanel.container;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.ContentPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.images.ImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class SubPanel<T extends ContentPanel> extends BasicPanel
{	private static final long serialVersionUID = 1L;

	public SubPanel(int width, int height, Mode mode)
	{	super(width,height);
	
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);

		// title
		{	String text = null;
			title = new JLabel(text);
			String tooltip = null;
			title.setToolTipText(tooltip);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);						
			title.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
			title.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			title.setOpaque(true);
		}
		// data
		{	//data = new EmptyContentPanel(dataWidth,dataHeight);
			data = null;
		}
		
		// dimension
		this.mode = mode;
		setDim(width,height);
		setMode(mode);
	}

	public void setMode(Mode mode)
	{	// init
		this.mode = mode;
		setDim(getWidth(),getHeight());
		removeAll();
		
		// content
		switch(mode)
		{	case TITLE:
				add(Box.createGlue());
				add(title);
				add(Box.createGlue());
				if(data!=null)
					add(data);
				else
					add(new JPanel());
				add(Box.createGlue());
				break;
			case BORDER:
				add(Box.createGlue());
				if(data!=null)
					add(data);
				else
					add(new JPanel());
				add(Box.createGlue());
				break;
			case NOTHING:
				if(data!=null)
					add(data);
				else
					add(new JPanel());
				break;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void switchDisplay(boolean display)
	{	data.switchDisplay(display);
	}	
	
	/////////////////////////////////////////////////////////////////
	// DIMENSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setDim(int width, int height)
	{	// dimension
		super.setDim(width,height);
		
		// content
		titleWidth = width - 2*GuiTools.subPanelMargin;
		titleHeight = GuiTools.subPanelTitleHeight;
		titleFontSize = GuiTools.getFontSize(titleHeight*GuiTools.FONT_RATIO);
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)titleFontSize);
		title.setFont(font);
		Dimension dim;
		switch(mode)
		{	case TITLE:
				dataHeight = height - 3*GuiTools.subPanelMargin - titleHeight;
				dataWidth = titleWidth;
				dim = new Dimension(titleWidth,titleHeight);
				title.setMinimumSize(dim);
				title.setPreferredSize(dim);
				title.setMaximumSize(dim);
				break;
			case BORDER:
				dataHeight = height - 2*GuiTools.subPanelMargin;
				dataWidth = width - 2*GuiTools.subPanelMargin;
				break;
			case NOTHING:
				dataHeight = height;
				dataWidth = width;
				break;
		}
		if(data!=null)
			data.setDim(dataWidth,dataHeight);
	}

	/////////////////////////////////////////////////////////////////
	// TITLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JLabel title;
	private int titleHeight;
	private int titleWidth;
	private int titleFontSize;

	public void setTitleKey(String key, boolean imageFlag)
	{	String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		// is there an available icon ?
		if(imageFlag)
		{	BufferedImage icon = GuiTools.getIcon(key);
			setTitleIcon(icon,tooltip);				
		}
		// if not : use text
		else
		{	String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
			setTitleText(text,tooltip);
		}
	}

	public void setTitleIcon(BufferedImage icon, String tooltip)
	{	double zoom = titleHeight/(double)icon.getHeight();
		icon = ImageTools.getResizedImage(icon,zoom,true);
		ImageIcon icn = new ImageIcon(icon);
		title.setText(null);
		title.setIcon(icn);					
		title.setToolTipText(tooltip);
	}

	public void setTitleText(String text, String tooltip)
	{	title.setText(text);
		title.setToolTipText(tooltip);
	}
	public JLabel getTitleLabel()
	{	return title;
	}

	public int getTitleHeight()
	{	return titleHeight;
	}
	public int getTitleWidth()
	{	return titleWidth;
	}
	public int getTitleFontSize()
	{	return titleFontSize;
	}
	
	public void setTitleBackground(Color bg)
	{	title.setBackground(bg);		
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private T data;
	private int dataHeight;
	private int dataWidth;

	public void setDataPanel(T panel)
	{	int index = 0;
		switch(mode)
		{	case TITLE:
				index = 3;
				break;
			case BORDER:
				index = 1;
				break;
			case NOTHING:
				index = 0;
				break;
		}
		if(!(getComponent(index) instanceof Box.Filler))
			remove(index);
		data = panel;
		data.setDim(dataWidth,dataHeight);
		data.setAlignmentX(CENTER_ALIGNMENT);
		data.setAlignmentY(CENTER_ALIGNMENT);
		add(data,index);
	}
	
	public T getDataPanel()
	{	return data;	
	}
	
	public int getDataWidth()
	{	return dataWidth;	
	}
	
	public int getDataHeight()
	{	return dataHeight;	
	}
	
	/////////////////////////////////////////////////////////////////
	// MODE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Mode mode;

	public enum Mode
	{	TITLE,BORDER,NOTHING
	}
}
