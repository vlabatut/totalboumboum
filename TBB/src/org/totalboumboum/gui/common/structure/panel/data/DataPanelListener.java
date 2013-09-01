package org.totalboumboum.gui.common.structure.panel.data;

import java.awt.event.MouseEvent;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

/**
 * This interface must be implemented
 * by classes listening to data panels.
 * 
 * @author Vincent Labatut
 */
public interface DataPanelListener
{	
	/**
	 * Fires an event when the data
	 * selected in the panel changes.
	 * 
	 * @param object
	 * 		Object representing the event.
	 */
	public void dataPanelSelectionChanged(Object object);
	
	/**
	 * Fires an event when the mouse
	 * is pressed in the listened panel.
	 * 
	 * @param e
	 * 		Associated mouse event.
	 */
	public void mousePressed(MouseEvent e);
}
