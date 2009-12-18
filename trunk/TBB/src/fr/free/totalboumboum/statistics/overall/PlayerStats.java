package fr.free.totalboumboum.statistics.overall;

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

import java.io.Serializable;
import java.util.HashMap;

import fr.free.totalboumboum.statistics.detailed.Score;
import fr.free.totalboumboum.tools.StringTools;
import fr.free.totalboumboum.tools.StringTools.TimeUnit;

public class PlayerStats implements Serializable
{	private static final long serialVersionUID = 1L;

	public PlayerStats(int playerID)
	{	this.playerId = playerID;
		for(Score score: Score.values())
			setScore(score,0);
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYER ID		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int playerId;

	public int getPlayerId()
	{	return playerId;
	}

	/////////////////////////////////////////////////////////////////
	// PREVIOUS RANK	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int previousRank = -1;

	public int getPreviousRank()
	{	return previousRank;
	}

	public void setPreviousRank(int previousRank)
	{	this.previousRank = previousRank;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS PLAYED	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long roundsPlayed = 0;

	public long getRoundsPlayed()
	{	return roundsPlayed;
	}

	public void setRoundsPlayed(long roundsPlayed)
	{	this.roundsPlayed = roundsPlayed;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS WON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long roundsWon = 0;

	public long getRoundsWon()
	{	return roundsWon;
	}

	public void setRoundsWon(long roundsWon)
	{	this.roundsWon = roundsWon;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS DRAWN		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long roundsDrawn = 0;

	public long getRoundsDrawn()
	{	return roundsDrawn;
	}

	public void setRoundsDrawn(long roundsDrawn)
	{	this.roundsDrawn = roundsDrawn;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS LOST		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long roundsLost = 0;

	public long getRoundsLost()
	{	return roundsLost;
	}

	public void setRoundsLost(long roundsLost)
	{	this.roundsLost = roundsLost;
	}

	/////////////////////////////////////////////////////////////////
	// SCORES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<Score,Long> scores = new HashMap<Score, Long>();

	public long getScore(Score score)
	{	if(scores.get(score)==null)
			scores.put(score,0l);
		return scores.get(score);
	}

	public void setScore(Score score, long value)
	{	scores.put(score,value);
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = "";
		// rounds
		result = result+" played: "+roundsPlayed;
		result = result+" won: "+roundsWon;
		result = result+" drawn: "+roundsDrawn;
		result = result+" lost: "+roundsLost;
		// scores
		for(Score score: Score.values())
		{	String text = Long.toString(scores.get(score));
			if(score==Score.TIME)
				text = StringTools.formatTime(scores.get(score),TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
			result = result+" "+score.toString()+": "+text;
		}
		return result;
	}
}
