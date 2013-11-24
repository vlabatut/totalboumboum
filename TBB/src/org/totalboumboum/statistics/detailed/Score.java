package org.totalboumboum.statistics.detailed;

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

import java.util.List;

import org.totalboumboum.statistics.overall.PlayerStats.Value;

/**
 * Quantity used to describe a player behavior during a round.
 * 
 * @author Vincent Labatut
 */
public enum Score
{	
	/////////////////////////////////////////////////////////////////
	// VALUES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of bombs dropped */
	BOMBS(Value.BOMBS), 
	/** Number of crowns picked up */
	CROWNS(Value.CROWNS), 
	/** Number of times bombed by other players */
	BOMBEDS(Value.BOMBEDS),
	/** Number of items picked up */
	ITEMS(Value.ITEMS), 
	/** Number of other players bombed */
	BOMBINGS(Value.BOMBINGS),
	/** Number of tiles painted */
	PAINTINGS(Value.PAINTINGS), 
	/** Number of times the player bombed himself */
	SELF_BOMBINGS(Value.SELF_BOMBINGS), 
	/** Time played */
	TIME(Value.TIME);
	
	/**
	 * Builds a new Score value.
	 * 
	 * @param value
	 * 		Value associated to this Score.
	 */
	Score(Value value)
	{	this.value = value;
	}
	
	/////////////////////////////////////////////////////////////////
	// VALUE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Value associated to this score */
	private Value value = null;
	
	/**
	 * Returns the value associated to this score.
	 * 
	 * @return
	 * 		Value associated to this score.
	 */
	public Value getValue()
	{	return value;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes this score for all players in the 
	 * specified round, using the specified event.
	 * 
	 * @param stats
	 * 		Stats object used to get players.
	 * @param event
	 * 		Data used to process the scores.
	 * @return
	 * 		An array containing the scores.
	 */
	public long[] process(StatisticRound stats, StatisticEvent event)
	{	long[] result = new long[stats.getPlayersIds().size()];
		switch(this)
		{	case BOMBS:
				result = processBombs(stats,event);
				break;
			case CROWNS:
				result = processCrowns(stats,event);
				break;
			case BOMBEDS:
				result = processBombeds(stats,event);
				break;
			case ITEMS:
				result = processItems(stats,event);
				break;
			case BOMBINGS:
				result = processBombings(stats,event);
				break;
			case PAINTINGS:
				result = processPaintings(stats,event);
				break;
			case SELF_BOMBINGS:
				result = processSelfBombings(stats,event);
				break;
			case TIME:
				result = processTime(stats,event);
				break;
		}
		return result;
	}

	/**
	 * Processes the dropped bomb scores for all players in the 
	 * specified round, using the specified event.
	 * 
	 * @param stats
	 * 		Stats object used to get players.
	 * @param event
	 * 		Data used to process the scores.
	 * @return
	 * 		An array containing the scores.
	 */
	public long[] processBombs(StatisticRound stats, StatisticEvent event)
	{	// init
		List<String> playersIds = stats.getPlayersIds();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.DROP_BOMB)
		{	int index = playersIds.indexOf(event.getActorId());
			result[index] = result[index] + 1;
		}
		return result;
	}

	/**
	 * Processes the picked crowns scores for all players in the 
	 * specified round, using the specified event.
	 * 
	 * @param stats
	 * 		Stats object used to get players.
	 * @param event
	 * 		Data used to process the scores.
	 * @return
	 * 		An array containing the scores.
	 */
	public long[] processCrowns(StatisticRound stats, StatisticEvent event)
	{	// init
		List<String> playersIds = stats.getPlayersIds();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.GATHER_CROWN)
		{	int index = playersIds.indexOf(event.getActorId());
			result[index] = result[index] + 1;
		}
		else if(event.getAction() == StatisticAction.LOSE_CROWN)
		{	int index = playersIds.indexOf(event.getActorId());
			result[index] = result[index] - 1;
		}
		return result;
	}

	/**
	 * Processes the bombed by opponents for all players in the 
	 * specified round, using the specified event.
	 * 
	 * @param stats
	 * 		Stats object used to get players.
	 * @param event
	 * 		Data used to process the scores.
	 * @return
	 * 		An array containing the scores.
	 */
	public long[] processBombeds(StatisticRound stats, StatisticEvent event)
	{	// init
		List<String> playersIds = stats.getPlayersIds();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.BOMB_PLAYER)
		{	Integer indexActor = playersIds.indexOf(event.getActorId());
			Integer indexTarget = playersIds.indexOf(event.getTargetId());
			if(indexActor==null || !indexActor.equals(indexTarget))
				result[indexTarget] = result[indexTarget] + 1;
		}
		return result;
	}

	/**
	 * Processes the picked items for all players in the 
	 * specified round, using the specified event.
	 * 
	 * @param stats
	 * 		Stats object used to get players.
	 * @param event
	 * 		Data used to process the scores.
	 * @return
	 * 		An array containing the scores.
	 */
	public long[] processItems(StatisticRound stats, StatisticEvent event)
	{	// init
		List<String> playersIds = stats.getPlayersIds();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.GATHER_ITEM)
		{	int index = playersIds.indexOf(event.getActorId());
			result[index] = result[index] + 1;
		}
		return result;
	}

	/**
	 * Processes the bombed opponents scores for all players in the 
	 * specified round, using the specified event.
	 * 
	 * @param stats
	 * 		Stats object used to get players.
	 * @param event
	 * 		Data used to process the scores.
	 * @return
	 * 		An array containing the scores.
	 */
	public long[] processBombings(StatisticRound stats, StatisticEvent event)
	{	// init
		List<String> playersIds = stats.getPlayersIds();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.BOMB_PLAYER)
		{	int indexActor = playersIds.indexOf(event.getActorId());
			int indexTarget = playersIds.indexOf(event.getTargetId());
			if(indexActor>=0 && indexActor!=indexTarget)
				result[indexActor] = result[indexActor] + 1;
		}
		return result;
	}

	/**
	 * Processes the self bomb scores for all players in the 
	 * specified round, using the specified event.
	 * 
	 * @param stats
	 * 		Stats object used to get players.
	 * @param event
	 * 		Data used to process the scores.
	 * @return
	 * 		An array containing the scores.
	 */
	public long[] processSelfBombings(StatisticRound stats, StatisticEvent event)
	{	// init
		List<String> playersIds = stats.getPlayersIds();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.BOMB_PLAYER)
		{	Integer indexActor = playersIds.indexOf(event.getActorId());
			Integer indexTarget = playersIds.indexOf(event.getTargetId());
			if(indexActor!=null && indexActor.equals(indexTarget))
				result[indexActor] = result[indexActor] + 1;
		}
		return result;
	}

	/**
	 * Processes the tiles painted scores for all players in the 
	 * specified round, using the specified event.
	 * 
	 * @param stats
	 * 		Stats object used to get players.
	 * @param event
	 * 		Data used to process the scores.
	 * @return
	 * 		An array containing the scores.
	 */
	public long[] processPaintings(StatisticRound stats, StatisticEvent event)
	{	// init
		List<String> playersIds = stats.getPlayersIds();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.WIN_TILE)
		{	// actor
			{	int index = playersIds.indexOf(event.getActorId());
				result[index] = result[index] + 1;
			}
			// target (can be null if the tile was neutral)
			if(event.getTargetId()!=null)
			{	int index = playersIds.indexOf(event.getTargetId());
				result[index] = result[index] - 1;
			}
		}
		return result;
	}

	/**
	 * Processes the time played scores for all players in the 
	 * specified round, using the specified event.
	 * 
	 * @param stats
	 * 		Stats object used to get players.
	 * @param event
	 * 		Data used to process the scores.
	 * @return
	 * 		An array containing the scores.
	 */
	public long[] processTime(StatisticRound stats, StatisticEvent event)
	{	long result[] = stats.getScores(this);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Express this score as a string.
	 * 
	 * @return
	 * 		A string representation of this score.
	 */
	public String stringFormat()
	{	StringBuffer result = new StringBuffer();
		String raw = this.toString(); 
		result.append(raw.substring(0,1));
		raw = raw.toLowerCase();
		result.append(raw.substring(1,raw.length()));
		return result.toString();
	}
}
