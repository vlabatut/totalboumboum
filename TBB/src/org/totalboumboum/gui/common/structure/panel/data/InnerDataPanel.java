package org.totalboumboum.gui.common.structure.panel.data;

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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.gui.common.structure.panel.ContentPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.frames.NormalFrame;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class InnerDataPanel extends ContentPanel
{	
	private static final long serialVersionUID = 1L;
	
	public InnerDataPanel(SplitMenuPanel container)
	{	super(container.getDataWidth(),container.getDataHeight());
		this.container = container;
	}	

	/////////////////////////////////////////////////////////////////
	// CONTAINER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected SplitMenuPanel container;
	
	public SplitMenuPanel getMenuContainer()
	{	return container;
	}

	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public NormalFrame getFrame()
	{	return container.getFrame();
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<DataPanelListener> listeners = new ArrayList<DataPanelListener>();
	
	public void addListener(DataPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(DataPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	protected void fireDataPanelSelectionChange(Object object)
	{	for(DataPanelListener listener: listeners)
			listener.dataPanelSelectionChanged(object);
	}
}
