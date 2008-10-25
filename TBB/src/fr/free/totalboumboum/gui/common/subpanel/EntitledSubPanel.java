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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class EntitledSubPanel extends SubPanel
{	private static final long serialVersionUID = 1L;

	public EntitledSubPanel(int width, int height)
	{	super(width,height);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);

		// size
		titleHeight = (int)(GuiTools.SUBPANEL_TITLE_RATIO*GuiTools.panelMargin);
		titleWidth = width - 2*GuiTools.subPanelMargin;
		titleFontSize = GuiTools.getFontSize(titleHeight*GuiTools.FONT_RATIO);
		dataWidth = titleWidth;
		dataHeight = height - 3*GuiTools.subPanelMargin - titleHeight;
		
		add(Box.createVerticalGlue());
		
		// title
		{	String text = null;
			title = new JLabel(text);
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(titleFontSize);
			title.setFont(font);
			String tooltip = null;
			title.setToolTipText(tooltip);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
			title.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			title.setOpaque(true);
			Dimension dim = new Dimension(titleWidth,titleHeight);
			title.setPreferredSize(dim);
			title.setMinimumSize(dim);
			title.setMaximumSize(dim);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(title);
		}

		add(Box.createVerticalGlue());
		
		// data
		{	data = new SubPanel(dataWidth,dataHeight);
			data.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			add(data);
		}
		
		add(Box.createVerticalGlue());	
	}
	
	/////////////////////////////////////////////////////////////////
	// TITLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JLabel title;
	private int titleHeight;
	private int titleWidth;
	private int titleFontSize;

	public void setTitleKey(String key, boolean imageFlag)
	{	String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiTools.TOOLTIP);
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
		icon = ImageTools.resize(icon,zoom,true);
		ImageIcon icn = new ImageIcon(icon);
		title.setText(null);
		title.setIcon(icn);					
		title.setToolTipText(tooltip);
	}

	public void setTitleText(String text, String tooltip)
	{	title.setText(text);
		title.setToolTipText(tooltip);
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
	
	/////////////////////////////////////////////////////////////////
	// DATA PART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SubPanel data;
	private int dataHeight;
	private int dataWidth;

	public void setDataPanel(SubPanel panel)
	{	remove(data);
		data = panel;	
		add(data,3);
	}
	
	public SubPanel getDataPanel()
	{	return data;	
	}
	
}
