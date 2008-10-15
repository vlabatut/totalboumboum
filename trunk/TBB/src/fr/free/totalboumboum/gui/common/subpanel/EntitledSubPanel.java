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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class EntitledSubPanel extends SubPanel
{	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JComponent data;

	public EntitledSubPanel(int width, int height, GuiConfiguration configuration)
	{	
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);

		// size
		Dimension dim = new Dimension(width,height);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);

		// content
		int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
		int contentWidth = width - 2*margin;
		int titleHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
		int dataHeight = height - 3*margin - titleHeight;
		
		add(Box.createVerticalGlue());
		// title
		{	String txt = null;
			title = new JLabel(txt);
			Font font = configuration.getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_RESULTS_HEADER_FONT_SIZE));
			title.setFont(font);
			String text = null;
			title.setText(text);
			String tooltip = null;
			title.setToolTipText(tooltip);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
			title.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			title.setOpaque(true);
			dim = new Dimension(contentWidth,titleHeight);
			title.setPreferredSize(dim);
			title.setMinimumSize(dim);
			title.setMaximumSize(dim);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(title);
		}
		//
		add(Box.createVerticalGlue());
		// data
		{	data = new JPanel();
			data.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			dim = new Dimension(contentWidth,dataHeight);
			data.setPreferredSize(dim);
			data.setMinimumSize(dim);
			data.setMaximumSize(dim);
			add(data);
		}
		add(Box.createVerticalGlue());	
	}
	
	public void setTitle(String text, String tooltip)
	{	title.setText(text);
		title.setToolTipText(tooltip);
	}
	public void setTitle(BufferedImage icon, String tooltip)
	{	int titleHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
		double zoom = titleHeight/(double)icon.getHeight();
		icon = ImageTools.resize(icon,zoom,true);
		ImageIcon icn = new ImageIcon(icon);
		title.setText(null);
		title.setIcon(icn);		
		title.setToolTipText(tooltip);
	}
	
	public void setDataPanel(JComponent panel)
	{	remove(data);
		data = panel;	
		add(data,3);
	}
	
	public JComponent getDataPanel()
	{	return data;	
	}
	
}
