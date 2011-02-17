package org.totalboumboum.gui.common.content.subpanel.players;

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

/**
 * 
 * @author Vincent Labatut
 *
 */
public interface PlayersSelectionSubPanelListener
{
	public void playerSelectionPlayerAdded(int index);
	public void playerSelectionPlayersAdded();
	public void playerSelectionPlayerRemoved(int index);
	public void playerSelectionProfileSet(int index);
	public void playerSelectionHeroSet(int index);
	public void playerSelectionColorSet(int index);
	public void playerSelectionControlsSet(int index);
}
