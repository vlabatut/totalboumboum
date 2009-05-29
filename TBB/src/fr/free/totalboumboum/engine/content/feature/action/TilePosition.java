package fr.free.totalboumboum.engine.content.feature.action;

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

/**
 * relative positions of the actor and the target, expressed in terms of tiles.
 */
public enum TilePosition
{	/** no position can be defined (there's no target) */
	UNDEFINED,
	/** the actor and target are together in the same tile */
	SAME,
	/** the actor and target are in neighbor tiles */
	NEIGHBOUR,
	/** the actor and target are in remote tiles */
	REMOTE;
}