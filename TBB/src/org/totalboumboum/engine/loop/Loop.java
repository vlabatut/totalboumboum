package org.totalboumboum.engine.loop;

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

import java.io.Serializable;

import org.totalboumboum.game.round.Round;
import org.totalboumboum.statistics.detailed.StatisticEvent;

/**
 * This class is the basis of all kinds of loops.
 * 
 * @author Vincent Labatut
 */
public abstract class Loop implements Runnable, Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a new loop.
	 * 
	 * @param round
	 * 		Round to be played in this loop.
	 */
	public Loop(Round round)
	{	this.round = round;
	}	
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Constant used to process transparent colors */
	public static final int INFO_ALPHA_LEVEL = 100;

	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Round displayed in this loop */
	Round round;
	
	/**
	 * Returns the current round displayed
	 * in this loop.
	 * 
	 * @return
	 * 		Round displayed in this loop.
	 */
	public Round getRound()
	{	return round;	
	}
	
	/////////////////////////////////////////////////////////////////
	// LOOP END			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates whether the associated round is over */
	boolean isOver = false;
	
	/**
	 * Sets this loop to the "over" state,
	 * meaning the round is over.
	 * 
	 * @param isOver
	 * 		State of this loop.
	 */
	public void setOver(boolean isOver)
	{	this.isOver = isOver;
	}
	
	/**
	 * Indicates if this loop is over
	 * or not.
	 * 
	 * @return
	 * 		{@code true} iff it is over.
	 */
	public boolean isOver()
	{	return isOver;
	}

	/////////////////////////////////////////////////////////////////
	// CELEBRATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Starts the celebration phase of the loop,
	 * which concludes (graphically) the round.
	 */
	public void initCelebration()
	{	//
	}

	/**
	 * Indicates the result of the round to the
	 * game engine.
	 * 
	 * @param index
	 * 		Indicates the specified player won the round.
	 */
	public void reportVictory(int index)
	{	//
	}
	
	/**
	 * Indicates the result of the round to the
	 * game engine.
	 * 
	 * @param index
	 * 		Indicates the specified player lost the round.
	 */
	public void reportDefeat(int index)
	{	//
	}

	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Updates the list of statistical events 
	 * describing the evolution of the round.
	 * 
	 * @param event
	 * 		New event to add to the list.
	 */
	public void addStatisticEvent(StatisticEvent event)
	{	round.addStatisticEvent(event);
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether this object has been deleted or not */
	private boolean finished = false;
	
	/**
	 * Cleanly finishes this object,
	 * possibly freeing some memory.
	 */
	public void finish()
	{	if(!finished)
		{	finished = true;	
			// round
			round = null;
		}		
	}	
}
