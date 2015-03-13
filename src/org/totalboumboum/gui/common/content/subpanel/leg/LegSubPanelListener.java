package org.totalboumboum.gui.common.content.subpanel.leg;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.awt.event.MouseEvent;

/**
 * Interface for objects listening to the associated panel.
 *  
 * @author Vincent Labatut
 */
public interface LegSubPanelListener
{
	/**
	 * Called when the leg selection has changed.
	 */
	public void legSelectionChanged();
	
	/**
	 * Called when the previous leg button is clicked.
	 */
	public void legBeforeClicked();
	
	/**
	 * Called when the next leg button is clicked.
	 */
	public void legAfterClicked();
	
	/**
	 * Called when the mouse is pressed on the panel.
	 * 
	 * @param e
	 * 		Corresponding mouse event. 
	 */
	public void mousePressed(MouseEvent e);
}
