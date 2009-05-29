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
 * represents the compared directions of the action and of the target
 * (the levels is closed, so the target direction has to be considered in terms of shortest distance)  
 */
public enum Orientation
{	/** no orientation can be defined (there's no target) */
	UNDEFINED,
	/** the action is performed facing the target */
	SAME,
	/** the action is performed back to the target */
	OPPOSITE,
	/** the action is not performed facing nor back to the target */
	OTHER;
}