package org.totalboumboum.engine.loop;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

public abstract class Loop implements Runnable, Serializable
{	private static final long serialVersionUID = 1L;
	
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
	// LOOP END		/////////////////////////////////////////////
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
	public abstract void initCelebrationDuration();

	public abstract void reportVictory(int index);
	
	public abstract void reportDefeat(int index);

	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addStatisticEvent(StatisticEvent event)
	{	round.addStatisticEvent(event);
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATED		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract boolean isSimulated();
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;	
			// round
			round = null;
		}		
	}	
}
