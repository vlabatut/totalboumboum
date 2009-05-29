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
 * represents the role of the actor or the target during an action.
 */
public enum Role
{	/** no object required (likely: no target) */
	NONE,
	/** the actor or target is a block sprite */
	BLOCK,
	/** the actor or target is a bomb sprite */
	BOMB,
	/** the actor or target is a fire sprite */
	FIRE,
	/** the actor or target is a floor sprite */
	FLOOR,
	/** the actor or target is a hero sprite */
	HERO,
	/** the actor or target is an item sprite */
	ITEM;
}
