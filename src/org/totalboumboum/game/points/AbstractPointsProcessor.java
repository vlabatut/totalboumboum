package org.totalboumboum.game.points;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * Class used to process points at the end of a confrontation
 * (round, match, tournament). {@code PointsProcessor} objects 
 * can be combined to obtain complex processing.
 * 
 * @author Vincent Labatut
 */
public abstract class AbstractPointsProcessor implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Process the points using the process implemented by
	 * this processor object, using the specified
	 * statistcs holder.
	 * 
	 * @param holder
	 * 		Data source for the processing.
	 * @return
	 * 		Resulting array of points.
	 */
	public abstract float[] process(StatisticHolder holder);
	
	/////////////////////////////////////////////////////////////////
	// NOTES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Textual description of the processor */
	private final List<String> notes = new ArrayList<String>();

	/**
	 * Changes the textual description of the processor.
	 * 
	 * @param notes
	 * 		New textual description of the processor.
	 */
	public void setNotes(List<String> notes)
	{	this.notes.addAll(notes);
	}
	
	/**
	 * Returns the textual description of the processor.
	 * 
	 * @return
	 * 		Textual description of the processor.
	 */
	public List<String> getNotes()
	{	return notes;
	}
}
