package fr.free.totalboumboum.gui.common.structure.subpanel;

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

import javax.swing.Box;

import fr.free.totalboumboum.gui.tools.GuiTools;

public class EntitledSubPanelTable extends EntitledSubPanel
{	private static final long serialVersionUID = 1L;

	public EntitledSubPanelTable(int width, int height, int colGroups, int colSubs, int lines)
	{	super(width,height);

		// init table
		remove(0);

		add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)),0);

		setNewTable(colGroups,colSubs,lines);
	}
	
	public UntitledSubPanelTable getTable()
	{	return (UntitledSubPanelTable)getDataPanel();
	}
	
	public void setNewTable(int colGroups, int colSubs, int lines)
	{	int tableHeight = height - getTitleHeight() - GuiTools.subPanelMargin;
		int tableWidth = width;
		UntitledSubPanelTable tablePanel = new UntitledSubPanelTable(tableWidth,tableHeight,colGroups,colSubs,lines,false);
		tablePanel.setOpaque(false);
		setDataPanel(tablePanel);
		remove(4); 	// remove glue
		remove(2); 	// remove glue
	}
}
