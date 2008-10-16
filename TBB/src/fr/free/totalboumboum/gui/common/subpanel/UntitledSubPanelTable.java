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

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class UntitledSubPanelTable extends SubPanel
{	private static final long serialVersionUID = 1L;

	private int colGroups = 0;
	private int colSubs = 0;
	private int lines = 0;
	private Font headerFont;
	private Font lineFont;
	private boolean header;
	
	private int headerHeight;
	private int lineHeight;
	private int lineFontSize;
	private int headerFontSize;
	

	public UntitledSubPanelTable(int width, int height, int columns, int lines, boolean header)
	{	this(width,height,1,columns,lines,header);
	}

	public UntitledSubPanelTable(int width, int height, int colGroups, int colSubs, int lines, boolean header)
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
		this.colGroups = colGroups;
		this.colSubs = colSubs;
		int columns = getColumnCount();
		this.lines = lines;
		for(int col=0;col<columns;col++)
			addColumn(col);
	}
	
	private void updateLayout()
	{	SpringLayout layout = new SpringLayout();
		setLayout(layout);
		int margin = GuiTools.SUBPANEL_MARGIN;
		SpringUtilities.makeCompactGrid(this,lines,getColumnCount(),margin,margin,margin,margin);		
	}
	
	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getColumnCount()
	{	return colGroups*colSubs;
	}
	
	public void addColumn(int index)
	{	addSubColumn(index);
	}

	public void addSubColumn(int subIndex)
	{	colSubs++;
		int start = 0;
	
		for(int grp=0;grp<colGroups;grp++)
		{	int index = subIndex+grp*colSubs;
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
				lbl.setFont(lineFont);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
				lbl.setOpaque(true);
				add(lbl,index+line*getColumnCount());
			}
		}
		updateLayout();
	}
	
	public void setColumnKeys(int col, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	setColumnKeys(0,col,keys,imageFlags);
	}
	public void setColumnKeys(int colGroup, int colSub, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	Iterator<String> lineKeys = keys.iterator();
		Iterator<Boolean> lineFlags = imageFlags.iterator();
		int line = 0;
		while(lineKeys.hasNext())
		{	String key = lineKeys.next();
			Boolean flag = lineFlags.next();
			setLabelKey(line,colGroup,colSub,key,flag);
			line++;
		}			
	}
	
	public void setColumnIcons(int col, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	setColumnIcons(0,col,icons,tooltips);
	}
	public void setColumnIcons(int colGroup, int colSub, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	Iterator<BufferedImage> lineIcons = icons.iterator();
		Iterator<String> lineTooltips = tooltips.iterator();
		int line = 0;
		while(lineIcons.hasNext())
		{	BufferedImage icon = lineIcons.next();
			String tooltip = lineTooltips.next();
			setLabelIcon(line,colGroup,colSub,icon,tooltip);
			line++;
		}			
	}
	
	public void setColumnTexts(int col, ArrayList<String> texts, ArrayList<String> tooltips)
	{	setColumnTexts(0,col,texts,tooltips);
	}
	public void setColumnTexts(int colGroup, int colSub, ArrayList<String> texts, ArrayList<String> tooltips)
	{	Iterator<String> lineTexts = texts.iterator();
		Iterator<String> lineTooltips = tooltips.iterator();
		int line = 0;
		while(lineTexts.hasNext())
		{	String text = lineTexts.next();
			String tooltip = lineTooltips.next();
			setLabelText(line,colGroup,colSub,text,tooltip);
			line++;
		}			
	}
		
	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public void addLine(int index)
	{	int cols = getColumnCount();
		int line = index*cols;
		for(int col=0;col<cols;col++)
		{	String txt = null;
			JLabel lbl = new JLabel(txt);
			lbl.setFont(lineFont);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
			lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
			lbl.setOpaque(true);
			add(lbl,line);
		}
		updateLayout();
	}

	public void setLineKeysSimple(int line, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	ArrayList<ArrayList<String>> newKeys = new ArrayList<ArrayList<String>>();
		newKeys.add(keys);
		ArrayList<ArrayList<Boolean>> newFlags = new ArrayList<ArrayList<Boolean>>();
		newFlags.add(imageFlags);
		setLineKeys(line,newKeys,newFlags);
	}
	public void setLineKeys(int line, ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlag)
	{	Iterator<ArrayList<String>> groupKeys = keys.iterator();
		Iterator<ArrayList<Boolean>> groupFlags = imageFlag.iterator();
		int colGroup = 0;
		while(groupKeys.hasNext())
		{	int colSub = 0;
			ArrayList<String> tempKeys = groupKeys.next();
			ArrayList<Boolean> tempFlags = groupFlags.next();
			Iterator<String> colKeys = tempKeys.iterator();
			Iterator<Boolean> colFlags = tempFlags.iterator();
			while(colKeys.hasNext())
			{	String key = colKeys.next();
				Boolean flag = colFlags.next();
				setLabelKey(line,colGroup,colSub,key,flag); 
				colSub++;
			}			
			colGroup++;
		}
	}
	
	public void setLineIconsSimple(int line, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	ArrayList<ArrayList<BufferedImage>> newIcons = new ArrayList<ArrayList<BufferedImage>>();
		newIcons.add(icons);
		ArrayList<ArrayList<String>> newTooltips = new ArrayList<ArrayList<String>>();
		newTooltips.add(tooltips);
		setLineIcons(line,newIcons,newTooltips);
	}
	public void setLineIcons(int line, ArrayList<ArrayList<BufferedImage>> icons, ArrayList<ArrayList<String>> tooltips)
	{	Iterator<ArrayList<BufferedImage>> groupIcons = icons.iterator();
		Iterator<ArrayList<String>> groupTooltips = tooltips.iterator();
		int colGroup = 0;
		while(groupIcons.hasNext())
		{	int colSub = 0;
			ArrayList<BufferedImage> tempIcons = groupIcons.next();
			ArrayList<String> tempTooltips = groupTooltips.next();
			Iterator<BufferedImage> colIcons = tempIcons.iterator();
			Iterator<String> colTooltips = tempTooltips.iterator();
			while(colIcons.hasNext())
			{	BufferedImage icon = colIcons.next();
				String tooltip = colTooltips.next();
				setLabelIcon(line,colGroup,colSub,icon,tooltip); 
				colSub++;
			}			
			colGroup++;
		}
	}
	
	public void setLineTextsSimple(int line, ArrayList<String> texts, ArrayList<String> tooltips)
	{	ArrayList<ArrayList<String>> newTexts = new ArrayList<ArrayList<String>>();
		newTexts.add(texts);
		ArrayList<ArrayList<String>> newTooltips = new ArrayList<ArrayList<String>>();
		newTooltips.add(tooltips);
		setLineTexts(line,newTexts,newTooltips);
	}
	public void setLineTexts(int line, ArrayList<ArrayList<String>> texts, ArrayList<ArrayList<String>> tooltips)
	{	Iterator<ArrayList<String>> groupTexts = texts.iterator();
		Iterator<ArrayList<String>> groupTooltips = tooltips.iterator();
		int colGroup = 0;
		while(groupTexts.hasNext())
		{	int colSub = 0;
			ArrayList<String> tempTexts = groupTexts.next();
			ArrayList<String> tempTooltips = groupTooltips.next();
			Iterator<String> colTexts = tempTexts.iterator();
			Iterator<String> colTooltips = tempTooltips.iterator();
			while(colTexts.hasNext())
			{	String text = colTexts.next();
				String tooltip = colTooltips.next();
				setLabelText(line,colGroup,colSub,text,tooltip); 
				colSub++;
			}			
			colGroup++;
		}
	}

	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public JLabel getLabel(int line, int col)
	{	return getLabel(line,0,col);
	}
	public JLabel getLabel(int line, int colGroup, int colSub)
	{	JLabel result = (JLabel)getComponent(colSub+colGroup*colSubs+line*getColumnCount());;
		return result;
	}
	
	public void setLabelKey(int line, int col, String key, boolean imageFlag)
	{	setLabelKey(line,0,col,key,imageFlag);
	}
	public void setLabelKey(int line, int colGroup, int colSub, String key, boolean imageFlag)
	{	String tooltip = GuiTools.getText(key+GuiTools.TOOLTIP);
		// is there an available icon ?
		if(imageFlag)
		{	BufferedImage icon = GuiTools.getIcon(key);
			setLabelIcon(line,colGroup,colSub,icon,tooltip);		
		}
		// if not : use text
		else
		{	String text = GuiTools.getText(key);
			setLabelText(line,colGroup,colSub,text,tooltip);
		}
	}

	public void setLabelIcon(int line, int col, BufferedImage icon, String tooltip)
	{	setLabelIcon(line,0,col,icon,tooltip);
	}
	public void setLabelIcon(int line, int colGroup, int colSub, BufferedImage icon, String tooltip)
	{	JLabel label = getLabel(line,colGroup,colSub);
		int h;
		if(line==0)
			h = headerHeight;
		else
			h = lineHeight;
		double zoom = h/(double)icon.getHeight();
		icon = ImageTools.resize(icon,zoom,true);
		ImageIcon icn = new ImageIcon(icon);
		label.setText(null);
		label.setIcon(icn);
		label.setToolTipText(tooltip);
	}

	public void setLabelText(int line, int col, String text, String tooltip)
	{	setLabelText(line,col,text,tooltip);
	}
	public void setLabelText(int line, int colGroup, int colSub, String text, String tooltip)
	{	JLabel label = getLabel(line,colGroup,colSub);
		label.setIcon(null);
		label.setText(text);
		label.setToolTipText(tooltip);
	}
	
}
