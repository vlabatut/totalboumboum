package org.totalboumboum.statistics.detailed;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.io.Serializable;

/**
 * This class represents all possible statistical
 * event. These are used to display the evolution
 * of games.
 * 
 * @author Vincent Labatut
 */
public enum StatisticAction implements Serializable
{	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Item lost (for example when diying */
	LOSE_ITEM, 
	/** Item picked up from the floor */
	GATHER_ITEM, 
	/** Item given to another player */
	TRANSMIT_ITEM, 
	/** Item received from another player */
	RECEIVE_ITEM,
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Bomb dropped on the floor */
	DROP_BOMB, 
	/** Player killed by bombing */
	BOMB_PLAYER,
	
	/////////////////////////////////////////////////////////////////
	// PAINT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tile won by painting it */
	WIN_TILE,
	/** Tile lost because another player painted it */
	LOSE_TILE,
	
	/////////////////////////////////////////////////////////////////
	// CROWN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Crown picked up */
	GATHER_CROWN, 
	/** Crown lost (gaven to another player, or through elimination) */ 
	LOSE_CROWN;
}
