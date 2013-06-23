package org.totalboumboum.gui.common.structure.subpanel.content;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.List;

import javax.swing.SwingConstants;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class TableContentPanel extends LinesContentPanel
{	private static final long serialVersionUID = 1L;

	public TableContentPanel(int width, int height, int lines, int columns, boolean header)
	{	this(width,height,lines,1,columns,header);
	}

	public TableContentPanel(int width, int height, int lines, int colGroups, int colSubs, boolean header)
	{	super(width,height,lines,colGroups*colSubs,header);
		
		this.colGroups = colGroups;
		this.colSubs = colSubs;
		
		for(int i=0;i<colSubs;i++)
		{	minWidths.add(null);
			prefWidths.add(null);
			maxWidths.add(null);
		}
	}
	
	public void reinit(int lines,int colGroups, int colSubs)
	{	super.reinit(lines,colGroups*colSubs);
		minWidths.clear();
		prefWidths.clear();
		maxWidths.clear();
		for(int i=0;i<colSubs;i++)
		{	minWidths.add(null);
			prefWidths.add(null);
			maxWidths.add(null);
		}
	} 

	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Integer> minWidths = new ArrayList<Integer>();
	private List<Integer> prefWidths = new ArrayList<Integer>();
	private List<Integer> maxWidths = new ArrayList<Integer>();
	private int colGroups;
	private int colSubs;
	
	public void setColSubMinWidth(int colSub, int width)
	{	setColSubWidth(colSub,width,0);
	}
	public void setColSubPrefWidth(int colSub, int width)
	{	setColSubWidth(colSub,width,1);
	}
	public void setColSubMaxWidth(int colSub, int width)
	{	setColSubWidth(colSub,width,2);
	}
	public void setColSubWidth(int colSub, int width, int mode)
	{	for(int colGroup=0;colGroup<colGroups;colGroup++)
		{	int col = colGroup*colSubs + colSub;
			int start = 0;
			for(int line=start;line<getLineCount();line++)
				setLabelWidth(line,col,width,mode);
		}
		switch(mode)
		{	case 0:
				minWidths.set(colSub,width);
				break;
			case 1:
				prefWidths.set(colSub,width);
				break;
			case 2:
				maxWidths.set(colSub,width);
				break;
		}
	}	

	public void unsetColSubMinWidth(int colSub)
	{	unsetColSubWidth(colSub,0);		
	}
	public void unsetColSubPrefWidth(int colSub)
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
		switch(mode)
		{	case 0:
				minWidths.set(colSub,null);
				break;
			case 1:
				prefWidths.set(colSub,null);
				break;
			case 2:
				maxWidths.set(colSub,null);
				break;
		}
	}	

	public List<MyLabel> getColumn(int col)
	{	return getColSub(0,col);		
	}
	public List<MyLabel> getColSub(int colGroup, int colSub)
	{	List<MyLabel> result = new ArrayList<MyLabel>();
		for(int line=0;line<getLineCount();line++)
		{	MyLabel label = getLabel(line,colGroup,colSub);
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
		{	for(int grp=0;grp<colGroups;grp++)
			{	int index = subIndex+grp*colSubs;
				addLabel(start,index);
				MyLabel lbl = getLabel(start,index);
				String txt = null;
				lbl.setText(txt);
				lbl.setFont(getHeaderFont());
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
				lbl.setOpaque(true);
				if(minWidths.get(subIndex)!=null)
					setLabelMinWidth(start,index,minWidths.get(subIndex));
				if(prefWidths.get(subIndex)!=null)
					setLabelPrefWidth(start,index,prefWidths.get(subIndex));
				if(maxWidths.get(subIndex)!=null)
					setLabelMaxWidth(start,index,maxWidths.get(subIndex));
			}
			start = 1;
		}
		
		//data
		for(int line=start;line<getLineCount();line++)
		{	for(int grp=0;grp<colGroups;grp++)
			{	int index = subIndex+grp*colSubs;
				addLabel(line,index);
				MyLabel lbl = getLabel(line,index);
				String txt = null;
				lbl.setText(txt);
				lbl.setFont(getLineFont());
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
				lbl.setOpaque(true);
				if(minWidths.get(subIndex)!=null)
					setLabelMinWidth(line,index,minWidths.get(subIndex));
				if(prefWidths.get(subIndex)!=null)
					setLabelPrefWidth(line,index,prefWidths.get(subIndex));
				if(maxWidths.get(subIndex)!=null)
					setLabelMaxWidth(line,index,maxWidths.get(subIndex));
			}
		}
	}

	public void addColGroup(int groupIndex)
	{	colGroups++;
		int start = 0;
		
		// header
		if(hasHeader())
		{	for(int sub=0;sub<colSubs;sub++)
			{	int index = sub+groupIndex*colSubs;
				addLabel(start,index);
				MyLabel lbl = getLabel(start,index);
				String txt = null;
				lbl.setText(txt);
				lbl.setFont(getHeaderFont());
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
				lbl.setOpaque(true);
				if(minWidths.get(sub)!=null)
					setLabelMinWidth(0,index,minWidths.get(sub));
				if(prefWidths.get(sub)!=null)
					setLabelPrefWidth(0,index,prefWidths.get(sub));
				if(maxWidths.get(sub)!=null)
					setLabelMaxWidth(0,index,maxWidths.get(sub));
			}
			start = 1;	
		}
		
		//data
		for(int line=start;line<getLineCount();line++)
		{	for(int sub=0;sub<colSubs;sub++)
			{	int index = sub+groupIndex*colSubs;
				addLabel(line,index);
				MyLabel lbl = getLabel(line,index);
				String txt = null;
				lbl.setText(txt);
				lbl.setFont(getLineFont());
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
				lbl.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
				lbl.setOpaque(true);
				if(minWidths.get(sub)!=null)
					setLabelMinWidth(line,index,minWidths.get(sub));
				if(prefWidths.get(sub)!=null)
					setLabelPrefWidth(line,index,prefWidths.get(sub));
				if(maxWidths.get(sub)!=null)
					setLabelMaxWidth(line,index,maxWidths.get(sub));
			}
		}
	}

	public void setColumnKeys(int col, List<String> keys, List<Boolean> imageFlags)
	{	setColumnKeys(0,col,keys,imageFlags);
	}
	public void setColumnKeys(int colGroup, int colSub, List<String> keys, List<Boolean> imageFlags)
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
	
	public void setColumnIcons(int col, List<BufferedImage> icons, List<String> tooltips)
	{	setColumnIcons(0,col,icons,tooltips);
	}
	public void setColumnIcons(int colGroup, int colSub, List<BufferedImage> icons, List<String> tooltips)
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
	
	public void setColumnTexts(int col, List<String> texts, List<String> tooltips)
	{	setColumnTexts(0,col,texts,tooltips);
	}
	public void setColumnTexts(int colGroup, int colSub, List<String> texts, List<String> tooltips)
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
		{	MyLabel label = getLabel(line,colGroup,colSub);
			label.setBackground(bg);
		}
	}

	public void setColumnForeground(int col, Color fg)
	{	setColumnForeground(0,col,fg);
	}
	public void setColumnForeground(int colGroup, int colSub, Color fg)
	{	for(int line=0;line<getLineCount();line++)
		{	MyLabel label = getLabel(line,colGroup,colSub);
			label.setForeground(fg);
		}
	}

	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public MyLabel getLabel(int line, int colGroup, int colSub)
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

	public int[] getLabelPositionSimple(MyLabel label)
	{	return super.getLabelPosition(label);
	}

	public int[] getLabelPositionMultiple(MyLabel label)
	{	int[] result = {-1,-1,-1};
		int line = 0;
		while(line<getLineCount() && result[0]==-1)
		{	int colGroup = 0;
			while(colGroup<colGroups && result[0]==-1)
			{	int colSub = 0;
				while(colSub<colSubs && result[0]==-1)
				{	MyLabel l = getLabel(line,colGroup,colSub);
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
	public void setLineKeysSimple(int line, List<String> keys, List<Boolean> imageFlags)
	{	List<List<String>> newKeys = new ArrayList<List<String>>();
		newKeys.add(keys);
		List<List<Boolean>> newFlags = new ArrayList<List<Boolean>>();
		newFlags.add(imageFlags);
		setLineKeysMultiple(line,newKeys,newFlags);
	}
	public void setLineKeysMultiple(int line, List<List<String>> keys, List<List<Boolean>> imageFlags)
	{	Iterator<List<String>> groupKeys = keys.iterator();
		Iterator<List<Boolean>> groupFlags = imageFlags.iterator();
		int colGroup = 0;
		while(groupKeys.hasNext())
		{	int colSub = 0;
			List<String> tempKeys = groupKeys.next();
			List<Boolean> tempFlags = groupFlags.next();
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
	
	public void setLineIconsSimple(int line, List<BufferedImage> icons, List<String> tooltips)
	{	List<List<BufferedImage>> newIcons = new ArrayList<List<BufferedImage>>();
		newIcons.add(icons);
		List<List<String>> newTooltips = new ArrayList<List<String>>();
		newTooltips.add(tooltips);
		setLineIconsMultiple(line,newIcons,newTooltips);
	}
	public void setLineIconsMultiple(int line, List<List<BufferedImage>> icons, List<List<String>> tooltips)
	{	Iterator<List<BufferedImage>> groupIcons = icons.iterator();
		Iterator<List<String>> groupTooltips = tooltips.iterator();
		int colGroup = 0;
		while(groupIcons.hasNext())
		{	int colSub = 0;
			List<BufferedImage> tempIcons = groupIcons.next();
			List<String> tempTooltips = groupTooltips.next();
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
	
	public void setLineTextsSimple(int line, List<String> texts, List<String> tooltips)
	{	List<List<String>> newTexts = new ArrayList<List<String>>();
		newTexts.add(texts);
		List<List<String>> newTooltips = new ArrayList<List<String>>();
		newTooltips.add(tooltips);
		setLineTextsMultiple(line,newTexts,newTooltips);
	}
	public void setLineTextsMultiple(int line, List<List<String>> texts, List<List<String>> tooltips)
	{	Iterator<List<String>> groupTexts = texts.iterator();
		Iterator<List<String>> groupTooltips = tooltips.iterator();
		int colGroup = 0;
		while(groupTexts.hasNext())
		{	int colSub = 0;
			List<String> tempTexts = groupTexts.next();
			List<String> tempTooltips = groupTooltips.next();
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
	
	public void setContentBySubLineKeys(List<List<String>> keys, List<List<Boolean>> imageFlags, boolean wholeLine)
	{	if(wholeLine)
		{	Iterator<List<String>> keysIt = keys.iterator();
			Iterator<List<Boolean>> flagsIt = imageFlags.iterator();
			int line = 0;
			if(hasHeader())
				line = 1;
			while(keysIt.hasNext() && line<getLineCount())
			{	int colGroup=0;
				while(keysIt.hasNext() && colGroup<colGroups)
				{	List<String> tempKeys = keysIt.next();
					List<Boolean> tempFlags = flagsIt.next();
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
		{	Iterator<List<String>> keysIt = keys.iterator();
			Iterator<List<Boolean>> flagsIt = imageFlags.iterator();
			int start = 0;
			if(hasHeader())
				start = 1;
			int colGroup = 0;
			while(keysIt.hasNext() && colGroup<colGroups)
			{	int line=start;
				while(keysIt.hasNext() && line<getLineCount())
				{	List<String> tempKeys = keysIt.next();
					List<Boolean> tempFlags = flagsIt.next();
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
	
	public void setContentBySubLineIcon(List<List<BufferedImage>> icons, List<List<String>> tooltips, boolean wholeLine)
	{	if(wholeLine)
		{	Iterator<List<BufferedImage>> iconsIt = icons.iterator();
			Iterator<List<String>> tooltipsIt = tooltips.iterator();
			int line = 0;
			if(hasHeader())
				line = 1;
			while(iconsIt.hasNext() && line<getLineCount())
			{	int colGroup=0;
				while(iconsIt.hasNext() && colGroup<colGroups)
				{	List<BufferedImage> tempIcons = iconsIt.next();
					List<String> tempTooltips = tooltipsIt.next();
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
		{	Iterator<List<BufferedImage>> iconsIt = icons.iterator();
			Iterator<List<String>> tooltipsIt = tooltips.iterator();
			int start = 0;
			if(hasHeader())
				start = 1;
			int colGroup = 0;
			while(iconsIt.hasNext() && colGroup<colGroups)
			{	int line=start;
				while(iconsIt.hasNext() && line<getLineCount())
				{	List<BufferedImage> tempIcons = iconsIt.next();
				List<String> tempTooltips = tooltipsIt.next();
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
	
	public void setContentBySubLineText(List<List<String>> texts, List<List<String>> tooltips, boolean wholeLine)
	{	if(wholeLine)
		{	Iterator<List<String>> textsIt = texts.iterator();
			Iterator<List<String>> tooltipsIt = tooltips.iterator();
			int line = 0;
			if(hasHeader())
				line = 1;
			while(textsIt.hasNext() && line<getLineCount())
			{	int colGroup=0;
				while(textsIt.hasNext() && colGroup<colGroups)
				{	List<String> tempTexts = textsIt.next();
					List<String> tempTooltips = tooltipsIt.next();
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
		{	Iterator<List<String>> textsIt = texts.iterator();
			Iterator<List<String>> tooltipsIt = tooltips.iterator();
			int start = 0;
			if(hasHeader())
				start = 1;
			int colGroup = 0;
			while(textsIt.hasNext() && colGroup<colGroups)
			{	int line=start;
				while(textsIt.hasNext() && line<getLineCount())
				{	List<String> tempTexts = textsIt.next();
					List<String> tempTooltips = tooltipsIt.next();
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
