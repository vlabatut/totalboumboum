package fr.free.totalboumboum.gui.common.panel.data;

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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public abstract class EntitledDataPanel extends InnerDataPanel
{	private static final long serialVersionUID = 1L;

	private JLabel title;
	private SubPanel data;
	
	public EntitledDataPanel(SplitMenuPanel container)
	{	super(container);

		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);

		// background
		//setBackground(new Color(50,50,50));
		setOpaque(false);
		
		// size
		setPreferredSize(new Dimension(width,height));
		float titleFontSize = GuiTools.getSize(GuiTools.GAME_TITLE_FONT_SIZE);
		int titleHeight = GuiTools.getPixelHeight(titleFontSize);
		int margin = (int)(width*0.025);
		int dataHeight = height-3*margin-titleHeight;
		int dataWidth = width-2*margin;
			
		add(Box.createVerticalGlue());
	
		// title label
		{	String text = "N/A";
			title = new JLabel(text);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			Font font = getConfiguration().getFont().deriveFont(titleFontSize);
			title.setForeground(GuiTools.COLOR_TITLE_FOREGROUND);
			title.setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
			title.setOpaque(true);
			title.setFont(font);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			Dimension dim = new Dimension(dataWidth,titleHeight);
			title.setPreferredSize(dim);
			title.setMinimumSize(dim);
			title.setMaximumSize(dim);
			add(title);
		}
	
		add(Box.createVerticalGlue());
		
		// data panel
		{	data = new SubPanel(dataWidth,dataHeight);
			data.setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
			add(data);
		}
		
		add(Box.createVerticalGlue());
	}	

	public void setTitle(String text)
	{	title.setText(text);
//		revalidate();
//		repaint();
	}
	
	public void setDataPanel(SubPanel panel)
	{	remove(data);
		data = panel;	
		add(data,3);
	}
	
	public JPanel getDataPanel()
	{	return data;	
	}
}
