package fr.free.totalboumboum.gui.common.structure.subpanel.inside;

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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.tools.GuiTools;

public class TableContentPanel extends LinesContentPanel
{	private static final long serialVersionUID = 1L;

	public TableContentPanel(int width, int height, int lines, int columns, boolean header)
	{	this(width,height,lines,1,columns,header);
	}

	public TableContentPanel(int width, int height, int lines, int colGroups, int colSubs, boolean header)
	{	super(width,height,lines,colGroups*colSubs,header);
		
		this.colGroups = colGroups;
		this.colSubs = colSubs;
	}
	
	public void reinit(int lines,int colGroups, int colSubs)
	{	super.reinit(lines,colGroups*colSubs);
	}

	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int colGroups;
	private int colSubs;
	
	public void setColSubMinWidth(int colSub, int width)
	{	setColSubWidth(colSub,width,0);		
	}
	public void setColSubPreferredWidth(int colSub, int width)
	{	setColSubWidth(colSub,width,1);		
	}
	public void setColSubMaxWidth(int colSub, int width)
	{	setColSubWidth(colSub,width,2);		
	}
	public void setColSubWidth(int colSub, int width, int mode)
	{	int start = 0;
		for(int colGroup=0;colGroup<colGroups;colGroup++)
		{	int col = colGroup*colSubs + colSub;
			if(hasHeader())
			{	setLabelWidth(start,col,getHeaderHeight(),mode);
				start = 1;
			}
			for(int line=start;line<getLineCount();line++)
				setLabelWidth(line,col,getLineHeight(),mode);
		}
	}	

	public void unsetColSubMinWidth(int colSub)
	{	unsetColSubWidth(colSub,0);		
	}
	public void unsetColSubPreferredWidth(int colSub)
	{	unsetColSubWidth(colSub,1);		
	}
	public void unsetColSubMaxWidth(int colSub)
	{	unsetColSubWidth(colSub,2);		
	}
	public void unsetColSubWidth(int colSub, int mode)
	{	int start = 0;
		for(int colGroup=0;colGroup<colGroups;colGroup++)
		{	int col = colGroup*colSubs + colSub;
			if(hasHeader())
			{	unsetLabelWidth(start,col,mode);
				start = 1;
			}
			for(int line=start;line<getLineCount();line++)
				unsetLabelWidth(line,col,mode);
		}
	}	

	public ArrayList<JLabel> getColumn(int col)
	{	return getColSub(0,col);		
	}
	public ArrayList<JLabel> getColSub(int colGroup, int colSub)
	{	ArrayList<JLabel> result = new ArrayList<JLabel>();
		for(int line=0;line<getLineCount();line++)
		{	JLabel label = getLabel(line,colGroup,colSub);
			result.add(label);
		}
		return result;
	}

	public int getColumnCount()
	{	return colGroups*colSubs;
	}
	public int getColGroupCount()
	{	return colGroups;
	}
	public int getColSubCount()
	{	return colSubs;
	}
	
	public void addColumn(int index)
	{	addColSub(index);
	}

	public void addColSub(int subIndex)
	{	colSubs++;
		int start = 0;
	
		// header
		if(hasHeader())
		{	start = 1;
			for(int grp=0;grp<colGroups;grp++)
			{	int index = subIndex+grp*colSubs;
				String txt = null;
				JLabel lbl = new JLabel(txt);
				lbl.setFont(getHeaderFont());
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
				lbl.setOpaque(true);
				add(lbl,index);
			}
		}
		
		//data
		for(int line=start;line<getLineCount();line++)
		{	for(int grp=0;grp<colGroups;grp++)
			{	int index = subIndex+grp*colSubs;
				String txt = null;
				JLabel lbl = new JLabel(txt);
				lbl.setFont(getLineFont());
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
				lbl.setOpaque(true);
				int idx = index+line*getColumnCount();
				add(lbl,idx);
			}
		}
	}

	public void addColGroup(int groupIndex)
	{	colGroups++;
		int start = 0;
	
		// header
		if(hasHeader())
		{	start = 1;
			for(int sub=0;sub<colSubs;sub++)
			{	int index = sub+groupIndex*colSubs;
				String txt = null;
				JLabel lbl = new JLabel(txt);
				lbl.setFont(getHeaderFont());
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
				lbl.setOpaque(true);
				add(lbl,index);
			}
		}
		
		//data
		for(int line=start;line<getLineCount();line++)
		{	for(int sub=0;sub<colSubs;sub++)
			{	int index = sub+groupIndex*colSubs;
				String txt = null;
				JLabel lbl = new JLabel(txt);
				lbl.setFont(getLineFont());
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
				lbl.setOpaque(true);
				int idx = index+line*getColumnCount();
				add(lbl,idx);
			}
		}
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
	{	for(int line=0;line<getLineCount();line++)
		{	JLabel label = getLabel(line,colGroup,colSub);
			label.setBackground(bg);
		}
	}

	public void setColumnForeground(int col, Color fg)
	{	setColumnForeground(0,col,fg);
	}
	public void setColumnForeground(int colGroup, int colSub, Color fg)
	{	for(int line=0;line<getLineCount();line++)
		{	JLabel label = getLabel(line,colGroup,colSub);
			label.setForeground(fg);
		}
	}

	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public JLabel getLabel(int line, int colGroup, int colSub)
	{	int col = colSub+colGroup*colSubs;
		return getLabel(line,col);
	}
	
	public void setLabelKey(int line, int colGroup, int colSub, String key, boolean imageFlag)
	{	int col = colSub+colGroup*colSubs;
		setLabelKey(line,col,key,imageFlag);
	}

	public void setLabelIcon(int line, int colGroup, int colSub, BufferedImage icon, String tooltip)
	{	int col = colSub+colGroup*colSubs;
		setLabelIcon(line,col,icon,tooltip);
	}

	public void setLabelText(int line, int colGroup, int colSub, String text, String tooltip)
	{	int col = colSub+colGroup*colSubs;
		setLabelText(line,col,text,tooltip);
	}
	
	public void setLabelBackground(int line, int colGroup, int colSub, Color bg)
	{	int col = colSub+colGroup*colSubs;
		setLabelBackground(line,col,bg);
	}
	
	public void setLabelForeground(int line, int colGroup, int colSub, Color fg)
	{	int col = colSub+colGroup*colSubs;
		setLabelForeground(line,col,fg);
	}

	public int[] getLabelPositionSimple(JLabel label)
	{	return super.getLabelPosition(label);
	}

	public int[] getLabelPositionMultiple(JLabel label)
	{	int[] result = {-1,-1,-1};
		int line = 0;
		while(line<getLineCount() && result[0]==-1)
		{	int colGroup = 0;
			while(colGroup<colGroups && result[0]==-1)
			{	int colSub = 0;
				while(colSub<colSubs && result[0]==-1)
				{	JLabel l = getLabel(line,colGroup,colSub);
					if(label == l)
					{	result[0] = line;
						result[1] = colGroup;
						result[2] = colSub;
					}
					colSub++;
				}
				colGroup++;
			}
			line++;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setLineKeysSimple(int line, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	ArrayList<ArrayList<String>> newKeys = new ArrayList<ArrayList<String>>();
		newKeys.add(keys);
		ArrayList<ArrayList<Boolean>> newFlags = new ArrayList<ArrayList<Boolean>>();
		newFlags.add(imageFlags);
		setLineKeysMultiple(line,newKeys,newFlags);
	}
	public void setLineKeysMultiple(int line, ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlags)
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
		setLineIconsMultiple(line,newIcons,newTooltips);
	}
	public void setLineIconsMultiple(int line, ArrayList<ArrayList<BufferedImage>> icons, ArrayList<ArrayList<String>> tooltips)
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
		setLineTextsMultiple(line,newTexts,newTooltips);
	}
	public void setLineTextsMultiple(int line, ArrayList<ArrayList<String>> texts, ArrayList<ArrayList<String>> tooltips)
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
	
	public void setContentBySubLineKeys(ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlags, boolean wholeLine)
	{	if(wholeLine)
		{	Iterator<ArrayList<String>> keysIt = keys.iterator();
			Iterator<ArrayList<Boolean>> flagsIt = imageFlags.iterator();
			int line = 0;
			if(hasHeader())
				line = 1;
			while(keysIt.hasNext() && line<getLineCount())
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
			if(hasHeader())
				start = 1;
			int colGroup = 0;
			while(keysIt.hasNext() && colGroup<colGroups)
			{	int line=start;
				while(keysIt.hasNext() && line<getLineCount())
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
			if(hasHeader())
				line = 1;
			while(iconsIt.hasNext() && line<getLineCount())
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
			if(hasHeader())
				start = 1;
			int colGroup = 0;
			while(iconsIt.hasNext() && colGroup<colGroups)
			{	int line=start;
				while(iconsIt.hasNext() && line<getLineCount())
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
			if(hasHeader())
				line = 1;
			while(textsIt.hasNext() && line<getLineCount())
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
			if(hasHeader())
				start = 1;
			int colGroup = 0;
			while(textsIt.hasNext() && colGroup<colGroups)
			{	int line=start;
				while(textsIt.hasNext() && line<getLineCount())
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
