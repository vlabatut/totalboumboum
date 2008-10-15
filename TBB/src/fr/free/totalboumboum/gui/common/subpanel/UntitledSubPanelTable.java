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
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class UntitledSubPanelTable extends SubPanel
{	private static final long serialVersionUID = 1L;

	private int columns = 0;
	private int lines = 0;
	private Font headerFont;
	private Font lineFont;
	private boolean header;
	
	private int headerHeight;
	private int lineHeight;
	private int lineFontSize;
	private int headerFontSize;
	

	public UntitledSubPanelTable(int width, int height, int columns, int lines, boolean header)
	{	super(width,height);
		
		// init
		this.header = header;
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		// size
		lineHeight = (int)((height - (lines+1)*GuiTools.SUBPANEL_MARGIN)/(lines+0.5));
		lineFontSize = GuiTools.getFontSize(lineHeight*GuiTools.FONT_RATIO);
		headerHeight = height - lineHeight*(lines-1);
		headerFontSize = GuiTools.getFontSize(headerHeight*GuiTools.FONT_RATIO);
		
		// fonts
		headerFont = GuiTools.getGameFont().deriveFont(headerFontSize);
		lineFont = GuiTools.getGameFont().deriveFont(lineFontSize);

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
//setMaximumSize(new Dimension(Integer.MAX_VALUE,GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
			add(lbl,index);		
		}
		
		// data
		for(int line=0+start;line<lines;line++)
		{	String txt = null;
			JLabel lbl = new JLabel(txt);
			lbl.setFont(lineFont);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
			lbl.setOpaque(true);
//setMaximumSize(new Dimension(Integer.MAX_VALUE,GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT)));
			add(lbl,index+line*columns);
		}
		
		// layout
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
		SpringUtilities.makeCompactGrid(this,lines,columns,margin,margin,margin,margin);
	}
	
	public Component getComponent(int line, int col)
	{	Component result = getComponent(col+line*columns);;
		return result;
	}
}
