package org.totalboumboum.statistics.detailed;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

/**
 * 
 * @author Vincent Labatut
 *
 */
public enum Score
{	/** number of bombs dropped */
	BOMBS, 
	/** number of crowns picked up */
	CROWNS, 
	/** number of times bombed by other players */
	BOMBEDS,
	/** number of items picked up */
	ITEMS, 
	/** number of other players bombed */
	BOMBINGS,
	/** number of tiles painted */
	PAINTINGS, 
	/** number of times the player bombed himself */
	SELF_BOMBINGS, 
	/** time played */
	TIME;
	
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

	public long[] processTime(StatisticRound stats, StatisticEvent event)
	{	long result[] = stats.getScores(this);
		return result;
	}
	
	public String stringFormat()
	{	StringBuffer result = new StringBuffer();
		String raw = this.toString(); 
		result.append(raw.substring(0,1));
		raw = raw.toLowerCase();
		result.append(raw.substring(1,raw.length()));
		return result.toString();
	}
}
