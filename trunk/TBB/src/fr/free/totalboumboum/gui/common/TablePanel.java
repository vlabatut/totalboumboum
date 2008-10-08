package fr.free.totalboumboum.gui.common;

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

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TablePanel extends JPanel
{	private static final long serialVersionUID = 1L;

	private int columns = 0;
	private int lines = 0;
	private Font headerFont;
	private Font regularFont;
	private boolean header;
//	private Configuration configuration;
	

	public TablePanel(int width, int height, int columns, int lines, boolean header, GuiConfiguration configuration)
	{	// init
//		this.configuration = configuration;
		this.header = header;
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		// size
		Dimension dim = new Dimension(width,height);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		
		// fonts
		headerFont = configuration.getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_RESULTS_HEADER_FONT_SIZE));
		regularFont = configuration.getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_RESULTS_LINE_FONT_SIZE));

		// table
		this.lines = lines;
		for(int col=0;col<columns;col++)
			addColumn(col);
	}
	
	public int getColumnCount()
	{	return columns;		
	}
	
	public void addColumn(int index)
	{	columns++;
		int start = 0;
	
		// header
		if(header)
		{	start = 1;
			String txt = null;
			JLabel lbl = new JLabel(txt);
			lbl.setFont(headerFont);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			lbl.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
			lbl.setOpaque(true);
			add(lbl,index);		
		}
		
		// data
		for(int line=0+start;line<lines;line++)
		{	String txt = null;
			JLabel lbl = new JLabel(txt);
			lbl.setFont(regularFont);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
			lbl.setOpaque(true);
			add(lbl,index+line*columns);
		}
		
		// layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
		SpringUtilities.makeCompactGrid(this,lines,columns,margin,margin,margin,margin);
	}
	
	public JLabel getLabel(int line, int col)
	{	JLabel result = (JLabel)getComponent(col+line*columns);;
		return result;
	}
}
