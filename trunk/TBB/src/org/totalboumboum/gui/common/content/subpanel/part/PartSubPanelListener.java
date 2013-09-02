package org.totalboumboum.gui.common.content.subpanel.part;

import org.totalboumboum.game.tournament.cup.CupPart;

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
 * Classes implementing this interface can
 * be informed of event occuring inside a cup part
 * panel.
 * 
 * @author Vincent Labatut
 */
public interface PartSubPanelListener
{
	/**
	 * The button pointing at the next part
	 * as been clicked.
	 *  
	 * @param part
	 * 		The concerned part.
	 */
	public void partAfterClicked(CupPart part);

	/**
	 * The button pointing at the previous part
	 * as been clicked.
	 *  
	 * @param part
	 * 		The concerned part.
	 */
	public void partBeforeClicked(CupPart part);

	/**
	 * The part has been selected by
	 * clicking on its title.
	 *  
	 * @param part
	 * 		The concerned part.
	 */
	public void partTitleClicked(CupPart part);
}
