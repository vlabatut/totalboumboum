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
 * 
 * @author Vincent Labatut
 *
 */
public abstract class Loop implements Runnable, Serializable
{	private static final long serialVersionUID = 1L;
	public static final int INFO_ALPHA_LEVEL = 100;
	
	public Loop(Round round)
	{	this.round = round;
	}	
	
	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	Round round;
	
	public Round getRound()
	{	return round;	
	}
	
	/////////////////////////////////////////////////////////////////
	// LOOP END			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	boolean isOver = false;
	
	public void setOver(boolean isOver)
	{	this.isOver = isOver;
	}
	
	public boolean isOver()
	{	return isOver;
	}

	/////////////////////////////////////////////////////////////////
	// CELEBRATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void initCelebration()
	{	
	}

	public void reportVictory(int index)
	{	
	}
	
	public void reportDefeat(int index)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
