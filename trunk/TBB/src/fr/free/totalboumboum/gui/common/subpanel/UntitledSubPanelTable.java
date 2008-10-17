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
		if(header)
		{	lineHeight = (int)((height - (lines+1)*GuiTools.subPanelMargin)/(lines+GuiTools.TABLE_HEADER_RATIO-1));
			headerHeight = height - (lines+1)*GuiTools.subPanelMargin - lineHeight*(lines-1);
			headerFontSize = GuiTools.getFontSize(headerHeight*GuiTools.FONT_RATIO);
			headerFont = GuiTools.getGameFont().deriveFont((float)headerFontSize);
		}
		else
			lineHeight = (int)((height - (lines+1)*GuiTools.subPanelMargin)/((float)lines));
		lineFontSize = GuiTools.getFontSize(lineHeight*GuiTools.FONT_RATIO);
		lineFont = GuiTools.getGameFont().deriveFont((float)lineFontSize);

		// table
		this.colGroups = colGroups;
		this.lines = lines;
		for(int col=0;col<colSubs;col++)
			addColumn(col);
	}
	
	private void updateLayout()
	{	SpringLayout layout = new SpringLayout();
		setLayout(layout);
		int margin = GuiTools.subPanelMargin;
		SpringUtilities.makeCompactGrid(this,lines,getColumnCount(),margin,margin,margin,margin);		
	}
	
	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public void setSubColumnsWidth(int colSub, int width)
	{	int start = 0;
		for(int colGroup=0;colGroup<colGroups;colGroup++)
		{	if(header)
			{	start = 1;
				Dimension dim = new Dimension(width,headerHeight);
				JLabel label = getLabel(0,colGroup,colSub);
				label.setMaximumSize(dim);
			}
			for(int line=start;line<lines;line++)
			{	Dimension dim = new Dimension(width,lineHeight);
				JLabel label = getLabel(line,colGroup,colSub);
				label.setMaximumSize(dim);
			}
		}
	}
	
	public void unsetsubColumnsWidth(int colSub)
	{	for(int colGroup=0;colGroup<colGroups;colGroup++)
			for(int line=0;line<lines;line++)
			{	JLabel label = getLabel(line,colGroup,colSub);
				label.setMaximumSize(null);
			}
	}
	
	public ArrayList<JLabel> getColumn(int col)
	{	return getSubColumn(0,col);		
	}
	public ArrayList<JLabel> getSubColumn(int colGroup, int colSub)
	{	ArrayList<JLabel> result = new ArrayList<JLabel>();
		for(int line=0;line<lines;line++)
		{	JLabel label = getLabel(line,colGroup,colSub);
			result.add(label);
		}
		return result;
	}

	public int getColumnCount()
	{	return colGroups*colSubs;
	}
	
	public void addColumn(int index)
	{	addSubColumn(index);
	}

	public void addSubColumn(int subIndex)
	{	colSubs++;
		int start = 0;
	
		// header
		if(header)
		{	start = 1;
			for(int grp=0;grp<colGroups;grp++)
			{	int index = subIndex+grp*colSubs;
				String txt = null;
				JLabel lbl = new JLabel(txt);
				lbl.setFont(headerFont);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
				lbl.setOpaque(true);
				add(lbl,index);
			}
		}
		
		//data
		for(int line=start;line<lines;line++)
		{	for(int grp=0;grp<colGroups;grp++)
			{	int index = subIndex+grp*colSubs;
				String txt = null;
				JLabel lbl = new JLabel(txt);
				lbl.setFont(lineFont);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
				lbl.setOpaque(true);
				int idx = index+line*getColumnCount();
				add(lbl,idx);
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

	public void setColumnBackground(int col, Color bg)
	{	setColumnBackground(0,col,bg);
	}
	public void setColumnBackground(int colGroup, int colSub, Color bg)
	{	for(int line=0;line<lines;line++)
		{	JLabel label = getLabel(line,colGroup,colSub);
			label.setBackground(bg);
		}
	}

	public void setColumnForeground(int col, Color fg)
	{	setColumnForeground(0,col,fg);
	}
	public void setColumnForeground(int colGroup, int colSub, Color fg)
	{	for(int line=0;line<lines;line++)
		{	JLabel label = getLabel(line,colGroup,colSub);
			label.setForeground(fg);
		}
	}

	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	public ArrayList<JLabel> getLine(int index)
	{	ArrayList<JLabel> result = new ArrayList<JLabel>();
		for(int grp=0;grp<colGroups;grp++)
			for(int sub=0;sub<colSubs;sub++)
			{	JLabel label = getLabel(index,grp,sub);
				result.add(label);
			}
		return result;
	}
	
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
	public void setLineKeys(int line, ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlags)
	{	Iterator<ArrayList<String>> groupKeys = keys.iterator();
		Iterator<ArrayList<Boolean>> groupFlags = imageFlags.iterator();
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

	public void setLineBackground(int line, Color bg)
	{	for(int grp=0;grp<colGroups;grp++)
			for(int sub=0;sub<colSubs;sub++)
			{	JLabel label = getLabel(line,grp,sub);
				label.setBackground(bg);
			}
	}

	public void setLineForeground(int line, Color fg)
	{	for(int grp=0;grp<colGroups;grp++)
			for(int sub=0;sub<colSubs;sub++)
			{	JLabel label = getLabel(line,grp,sub);
				label.setForeground(fg);
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
		if(line==0 && header)
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
	{	setLabelText(line,0,col,text,tooltip);
	}
	public void setLabelText(int line, int colGroup, int colSub, String text, String tooltip)
	{	JLabel label = getLabel(line,colGroup,colSub);
		label.setIcon(null);
		label.setText(text);
		label.setToolTipText(tooltip);
	}
	
	public void setLabelBackground(int line, int col, Color bg)
	{	setLabelBackground(line,0,col,bg);
	}
	public void setLabelBackground(int line, int colGroup, int colSub, Color bg)
	{	JLabel label = getLabel(line, colGroup, colSub);
		label.setBackground(bg);
	}
	
	public void setLabelForeground(int line, int col, Color fg)
	{	setLabelForeground(line,0,col,fg);
	}
	public void setLabelForeground(int line, int colGroup, int colSub, Color fg)
	{	JLabel label = getLabel(line, colGroup, colSub);
		label.setForeground(fg);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public void setContentBySubLineKeys(ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlags, boolean wholeLine)
	{	if(wholeLine)
		{	Iterator<ArrayList<String>> keysIt = keys.iterator();
			Iterator<ArrayList<Boolean>> flagsIt = imageFlags.iterator();
			int line = 0;
			if(header)
				line = 1;
			while(keysIt.hasNext() && line<lines)
			{	int colGroup=0;
				while(keysIt.hasNext() && colGroup<colGroups)
				{	ArrayList<String> tempKeys = keysIt.next();
					ArrayList<Boolean> tempFlags = flagsIt.next();
					Iterator<String> keyIt = tempKeys.iterator();
					Iterator<Boolean> flagIt = tempFlags.iterator();
					for(int colSub=0;colSub<colSubs;colSub++)
					{	String key = keyIt.next();
						Boolean flag = flagIt.next();
						setLabelKey(line,colGroup,colSub,key,flag);					
					}
					colGroup++;
				}
				line++;
			}
		}
		else
		{	Iterator<ArrayList<String>> keysIt = keys.iterator();
			Iterator<ArrayList<Boolean>> flagsIt = imageFlags.iterator();
			int start = 0;
			if(header)
				start = 1;
			int colGroup = 0;
			while(keysIt.hasNext() && colGroup<colGroups)
			{	int line=start;
				while(keysIt.hasNext() && line<lines)
				{	ArrayList<String> tempKeys = keysIt.next();
					ArrayList<Boolean> tempFlags = flagsIt.next();
					Iterator<String> keyIt = tempKeys.iterator();
					Iterator<Boolean> flagIt = tempFlags.iterator();
					for(int colSub=0;colSub<colSubs;colSub++)
					{	String key = keyIt.next();
						Boolean flag = flagIt.next();
						setLabelKey(line,colGroup,colSub,key,flag);					
					}
					line++;
				}
				colGroup++;
			}
		}
	}
	
	public void setContentBySubLineIcon(ArrayList<ArrayList<BufferedImage>> icons, ArrayList<ArrayList<String>> tooltips, boolean wholeLine)
	{	if(wholeLine)
		{	Iterator<ArrayList<BufferedImage>> iconsIt = icons.iterator();
			Iterator<ArrayList<String>> tooltipsIt = tooltips.iterator();
			int line = 0;
			if(header)
				line = 1;
			while(iconsIt.hasNext() && line<lines)
			{	int colGroup=0;
				while(iconsIt.hasNext() && colGroup<colGroups)
				{	ArrayList<BufferedImage> tempIcons = iconsIt.next();
					ArrayList<String> tempTooltips = tooltipsIt.next();
					Iterator<BufferedImage> iconIt = tempIcons.iterator();
					Iterator<String> tooltipIt = tempTooltips.iterator();
					for(int colSub=0;colSub<colSubs;colSub++)
					{	BufferedImage icon = iconIt.next();
						String tooltip = tooltipIt.next();
						setLabelIcon(line,colGroup,colSub,icon,tooltip);					
					}
					colGroup++;
				}
				line++;
			}
		}
		else
		{	Iterator<ArrayList<BufferedImage>> iconsIt = icons.iterator();
			Iterator<ArrayList<String>> tooltipsIt = tooltips.iterator();
			int start = 0;
			if(header)
				start = 1;
			int colGroup = 0;
			while(iconsIt.hasNext() && colGroup<colGroups)
			{	int line=start;
				while(iconsIt.hasNext() && line<lines)
				{	ArrayList<BufferedImage> tempIcons = iconsIt.next();
					ArrayList<String> tempTooltips = tooltipsIt.next();
					Iterator<BufferedImage> iconIt = tempIcons.iterator();
					Iterator<String> tooltipIt = tempTooltips.iterator();
					for(int colSub=0;colSub<colSubs;colSub++)
					{	BufferedImage icon = iconIt.next();
						String tooltip = tooltipIt.next();
						setLabelIcon(line,colGroup,colSub,icon,tooltip);					
					}
					line++;
				}
				colGroup++;
			}
		}
	}
	
	public void setContentBySubLineText(ArrayList<ArrayList<String>> texts, ArrayList<ArrayList<String>> tooltips, boolean wholeLine)
	{	if(wholeLine)
		{	Iterator<ArrayList<String>> textsIt = texts.iterator();
			Iterator<ArrayList<String>> tooltipsIt = tooltips.iterator();
			int line = 0;
			if(header)
				line = 1;
			while(textsIt.hasNext() && line<lines)
			{	int colGroup=0;
				while(textsIt.hasNext() && colGroup<colGroups)
				{	ArrayList<String> tempTexts = textsIt.next();
					ArrayList<String> tempTooltips = tooltipsIt.next();
					Iterator<String> textIt = tempTexts.iterator();
					Iterator<String> tooltipIt = tempTooltips.iterator();
					for(int colSub=0;colSub<colSubs;colSub++)
					{	String text = textIt.next();
						String tooltip = tooltipIt.next();
						setLabelText(line,colGroup,colSub,text,tooltip);					
					}
					colGroup++;
				}
				line++;
			}
		}
		else
		{	Iterator<ArrayList<String>> textsIt = texts.iterator();
			Iterator<ArrayList<String>> tooltipsIt = tooltips.iterator();
			int start = 0;
			if(header)
				start = 1;
			int colGroup = 0;
			while(textsIt.hasNext() && colGroup<colGroups)
			{	int line=start;
				while(textsIt.hasNext() && line<lines)
				{	ArrayList<String> tempTexts = textsIt.next();
					ArrayList<String> tempTooltips = tooltipsIt.next();
					Iterator<String> textIt = tempTexts.iterator();
					Iterator<String> tooltipIt = tempTooltips.iterator();
					for(int colSub=0;colSub<colSubs;colSub++)
					{	String text = textIt.next();
						String tooltip = tooltipIt.next();
						setLabelText(line,colGroup,colSub,text,tooltip);					
					}
					line++;
				}
				colGroup++;
			}
		}
	}
}
