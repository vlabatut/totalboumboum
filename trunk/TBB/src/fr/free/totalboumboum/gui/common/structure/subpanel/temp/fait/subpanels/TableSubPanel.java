package fr.free.totalboumboum.gui.common.structure.subpanel.temp.fait.subpanels;

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
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.common.structure.subpanel.temp.fait.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.temp.fait.contentpanels.TableContentPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TableSubPanel extends EntitledSubPanel implements SubPanelTable
{	private static final long serialVersionUID = 1L;

	public TableSubPanel(int width, int height, int colGroups, int colSubs, int lines)
	{	super(width,height);

		// init table
		remove(0);
		add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),0);
		remove(4); 	// remove glue
//		remove(2); 	// remove glue

		setNewTable(colGroups,colSubs,lines);
	}
	
	private TableContentPanel getTable()
	{	return (TableContentPanel)getDataPanel();
	}
	
	public void setNewTable(int colGroups, int colSubs, int lines)
	{	int tableHeight = height - getTitleHeight() - GuiTools.subPanelMargin;
		int tableWidth = width;
		TableContentPanel tablePanel = new TableContentPanel(tableWidth,tableHeight,colGroups,colSubs,lines,false);
		tablePanel.setOpaque(false);
		setDataPanel(tablePanel);
	}

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void reinit(int columns, int lines)
	{	getTable().reinit(columns,lines);
	}
	@Override
	public void reinit(int colGroups, int colSubs, int lines)
	{	getTable().reinit(colGroups,colSubs,lines);
	}

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int getLineFontSize()
	{	return getTable().getLineFontSize();
	}
	@Override
	public int getHeaderFontSize()
	{	return getTable().getHeaderFontSize();
	}
	@Override
	public int getHeaderHeight()
	{	return getTable().getHeaderHeight();
	}
	@Override
	public int getLineHeight()
	{	return getTable().getLineHeight();
	}
	
	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void setColSubMinWidth(int colSub, int width)
	{	getTable().setColSubMinWidth(colSub,width);
	}
	@Override
	public void setColSubPreferredWidth(int colSub, int width)
	{	getTable().setColSubPreferredWidth(colSub,width);
	}
	@Override
	public void setColSubMaxWidth(int colSub, int width)
	{	getTable().setColSubMaxWidth(colSub,width);
	}
	@Override
	public void unsetColSubMinWidth(int colSub)
	{	getTable().unsetColSubMinWidth(colSub);
	}
	@Override
	public void unsetColSubPreferredWidth(int colSub)
	{	getTable().unsetColSubPreferredWidth(colSub);
	}
	@Override
	public void unsetColSubMaxWidth(int colSub)
	{	getTable().unsetColSubMaxWidth(colSub);
	}
	@Override
	public ArrayList<JLabel> getColumn(int col)
	{	return getTable().getColumn(col);
	}
	@Override
	public ArrayList<JLabel> getColSub(int colGroup, int colSub)
	{	return getTable().getColSub(colGroup,colSub);
	}
	@Override
	public int getColumnCount()
	{	return getTable().getColumnCount();
	}
	@Override
	public int getColGroupCount()
	{	return getTable().getColGroupCount();
	}
	@Override
	public int getColSubCount()
	{	return getTable().getColSubCount();
	}
	@Override
	public void addColumn(int index)
	{	getTable().addColumn(index);
	}
	@Override
	public void addColSub(int subIndex)
	{	getTable().addColSub(subIndex);
	}
	@Override
	public void addColGroup(int groupIndex)
	{	getTable().addColGroup(groupIndex);
	}
	@Override
	public void setColumnKeys(int col, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	getTable().setColumnKeys(col,keys,imageFlags);
	}
	@Override
	public void setColumnKeys(int colGroup, int colSub, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	getTable().setColumnKeys(colGroup,colSub,keys,imageFlags);
	}
	@Override
	public void setColumnIcons(int col, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	getTable().setColumnIcons(col,icons,tooltips);
	}
	@Override
	public void setColumnIcons(int colGroup, int colSub, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	getTable().setColumnIcons(colGroup,colSub,icons,tooltips);
	}
	@Override
	public void setColumnTexts(int col, ArrayList<String> texts, ArrayList<String> tooltips)
	{	getTable().setColumnTexts(col,texts,tooltips);
	}
	@Override
	public void setColumnTexts(int colGroup, int colSub, ArrayList<String> texts, ArrayList<String> tooltips)
	{	getTable().setColumnTexts(colGroup,colSub,texts,tooltips);
	}
	@Override
	public void setColumnBackground(int col, Color bg)
	{	getTable().setColumnBackground(col,bg);
	}
	@Override
	public void setColumnBackground(int colGroup, int colSub, Color bg)
	{	getTable().setColumnBackground(colGroup,colSub,bg);
	}
	@Override
	public void setColumnForeground(int col, Color fg)
	{	getTable().setColumnForeground(col,fg);
	}
	@Override
	public void setColumnForeground(int colGroup, int colSub, Color fg)
	{	getTable().setColumnForeground(colGroup,colSub,fg);
	}

	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int getLineCount()
	{	return getTable().getLineCount();
	}
	@Override
	public ArrayList<JLabel> getLine(int index)
	{	return getTable().getLine(index);
	}
	@Override
	public void addLine(int index)
	{	getTable().addLine(index);
	}
	@Override
	public void setLineKeysSimple(int line, ArrayList<String> keys, ArrayList<Boolean> imageFlags)
	{	getTable().setLineKeysSimple(line,keys,imageFlags);
	}
	@Override
	public void setLineKeys(int line, ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlags)
	{	getTable().setLineKeys(line,keys,imageFlags);
	}
	@Override
	public void setLineIconsSimple(int line, ArrayList<BufferedImage> icons, ArrayList<String> tooltips)
	{	getTable().setLineIconsSimple(line,icons,tooltips);
	}
	@Override
	public void setLineIcons(int line, ArrayList<ArrayList<BufferedImage>> icons, ArrayList<ArrayList<String>> tooltips)
	{	getTable().setLineIcons(line,icons,tooltips);
	}
	@Override
	public void setLineTextsSimple(int line, ArrayList<String> texts, ArrayList<String> tooltips)
	{	getTable().setLineTextsSimple(line,texts,tooltips);
	}
	@Override
	public void setLineTexts(int line, ArrayList<ArrayList<String>> texts, ArrayList<ArrayList<String>> tooltips)
	{	getTable().setLineTexts(line,texts,tooltips);
	}
	@Override
	public void setLineBackground(int line, Color bg)
	{	getTable().setLineBackground(line,bg);
	}
	@Override
	public void setLineForeground(int line, Color fg)
	{	getTable().setLineForeground(line,fg);
	}

	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public JLabel getLabel(int line, int col)
	{	return getTable().getLabel(line,col);
	}
	@Override
	public JLabel getLabel(int line, int colGroup, int colSub)
	{	return getTable().getLabel(line,colGroup,colSub);
	}
	@Override
	public void setLabelKey(int line, int col, String key, boolean imageFlag)
	{	getTable().setLabelKey(line,col,key,imageFlag);
	}
	@Override
	public void setLabelKey(int line, int colGroup, int colSub, String key, boolean imageFlag)
	{	getTable().setLabelKey(line,colGroup,colSub,key,imageFlag);
	}
	@Override
	public void setLabelIcon(int line, int col, BufferedImage icon, String tooltip)
	{	getTable().setLabelIcon(line,col,icon,tooltip);
	}
	@Override
	public void setLabelIcon(int line, int colGroup, int colSub, BufferedImage icon, String tooltip)
	{	getTable().setLabelIcon(line,colGroup,colSub,icon,tooltip);
	}
	@Override
	public void setLabelText(int line, int col, String text, String tooltip)
	{	getTable().setLabelText(line,col,text,tooltip);
	}
	@Override
	public void setLabelText(int line, int colGroup, int colSub, String text, String tooltip)
	{	getTable().setLabelText(line,colGroup,colSub,text,tooltip);
	}
	@Override
	public void setLabelBackground(int line, int col, Color bg)
	{	getTable().setLabelBackground(line,col,bg);
	}
	@Override
	public void setLabelBackground(int line, int colGroup, int colSub, Color bg)
	{	getTable().setLabelBackground(line,colGroup,colSub,bg);
	}
	@Override
	public void setLabelForeground(int line, int col, Color fg)
	{	getTable().setLabelForeground(line,col,fg);
	}
	@Override
	public void setLabelForeground(int line, int colGroup, int colSub, Color fg)
	{	getTable().setLabelForeground(line,colGroup,colSub,fg);
	}
	@Override
	public int[] getLabelPositionSimple(JLabel label)
	{	return getTable().getLabelPositionSimple(label);
	}
	@Override
	public int[] getLabelPosition(JLabel label)
	{	return getTable().getLabelPosition(label);
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void setContentBySubLineKeys(ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlags, boolean wholeLine)
	{	getTable().setContentBySubLineKeys(keys,imageFlags,wholeLine);
	}
	@Override
	public void setContentBySubLineIcon(ArrayList<ArrayList<BufferedImage>> icons, ArrayList<ArrayList<String>> tooltips, boolean wholeLine)
	{	getTable().setContentBySubLineIcon(icons,tooltips,wholeLine);
	}
	@Override
	public void setContentBySubLineText(ArrayList<ArrayList<String>> texts, ArrayList<ArrayList<String>> tooltips, boolean wholeLine)
	{	getTable().setContentBySubLineText(texts,tooltips,wholeLine);
	}
}
