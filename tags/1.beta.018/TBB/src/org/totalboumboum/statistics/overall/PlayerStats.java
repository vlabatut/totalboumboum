package org.totalboumboum.statistics.overall;

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

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;

import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PlayerStats implements Serializable
{	private static final long serialVersionUID = 1L;

	public PlayerStats(String playerID)
	{	this.playerId = playerID;
		reset();
	}
	
	public void reset()
	{	// scores
		for(Score score: Score.values())
			setScore(score,0);
		previousRank = -1;
		roundsPlayed = 0;
		roundsWon = 0;
		roundsDrawn = 0;
		roundsLost = 0;
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYER ID		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String playerId;

	public String getPlayerId()
	{	return playerId;
	}
	
	public void setPlayerId(String id)
	{	playerId = id;
	}

	/////////////////////////////////////////////////////////////////
	// PREVIOUS RANK	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int previousRank;

	public int getPreviousRank()
	{	return previousRank;
	}

	public void setPreviousRank(int previousRank)
	{	this.previousRank = previousRank;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS PLAYED	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long roundsPlayed;

	public long getRoundsPlayed()
	{	return roundsPlayed;
	}

	public void setRoundsPlayed(long roundsPlayed)
	{	this.roundsPlayed = roundsPlayed;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS WON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long roundsWon;

	public long getRoundsWon()
	{	return roundsWon;
	}

	public void setRoundsWon(long roundsWon)
	{	this.roundsWon = roundsWon;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS DRAWN		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long roundsDrawn;

	public long getRoundsDrawn()
	{	return roundsDrawn;
	}

	public void setRoundsDrawn(long roundsDrawn)
	{	this.roundsDrawn = roundsDrawn;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS LOST		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long roundsLost;

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
		
		// misc
		result = result+" prevrk: "+previousRank;
		
		// rounds
		result = result+" played: "+roundsPlayed;
		result = result+" won: "+roundsWon;
		result = result+" drawn: "+roundsDrawn;
		result = result+" lost: "+roundsLost;
		
		// scores
		for(Score score: Score.values())
		{	String text = Long.toString(scores.get(score));
			if(score==Score.TIME)
				text = TimeTools.formatTime(scores.get(score),TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
			result = result+" "+score.toString()+": "+text;
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT IMPORT/EXPORT	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void exportToText(PrintWriter writer)
	{	// misc
		writer.print(playerId);
		writer.print(";"+previousRank);
		
		// rounds
		writer.print(";"+roundsPlayed);
		writer.print(";"+roundsWon);
		writer.print(";"+roundsDrawn);
		writer.print(";"+roundsLost);
		
		// scores
		for(Score score: Score.values())
			writer.print(";"+scores.get(score));
		
		writer.println();
	}

	public void importFromText(Scanner scanner)
	{	String text = scanner.nextLine();
		String texts[] = text.split(";");
		int t = 0;

		// misc
		playerId = texts[t++];
		previousRank = Integer.parseInt(texts[t++]);
		
		// rounds
		roundsPlayed = Long.parseLong(texts[t++]);
		roundsWon = Long.parseLong(texts[t++]);
		roundsDrawn = Long.parseLong(texts[t++]);
		roundsLost = Long.parseLong(texts[t++]);
		
		// scores
		for(Score score: Score.values())
		{	long value = Long.parseLong(texts[t++]);
			scores.put(score,value);
		}
	}
}
