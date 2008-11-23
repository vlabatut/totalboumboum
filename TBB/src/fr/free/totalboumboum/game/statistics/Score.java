package fr.free.totalboumboum.game.statistics;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.util.ArrayList;


public enum Score
{	BOMBS, CROWNS, BOMBEDS, ITEMS, BOMBINGS, PAINTINGS, TIME;
	
	public long[] process(StatisticRound stats, StatisticEvent event)
	{	long[] result = new long[stats.getPlayers().size()];
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
			case TIME:
				result = processTime(stats,event);
				break;
		}
		return result;
	}

	public long[] processBombs(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.DROP_BOMB)
		{	int index = players.indexOf(event.getActor());
			result[index] = result[index] + 1;
		}
		return result;
	}

	public long[] processCrowns(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.GATHER_CROWN)
		{	int index = players.indexOf(event.getActor());
			result[index] = result[index] + 1;
		}
		else if(event.getAction() == StatisticAction.LOSE_CROWN)
		{	int index = players.indexOf(event.getActor());
			result[index] = result[index] - 1;
		}
		return result;
	}

	public long[] processBombeds(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.EXPLOSE_PLAYER)
		{	//target
			int index = players.indexOf(event.getTarget());
			result[index] = result[index] + 1;
		}
		return result;
	}

	public long[] processItems(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.GATHER_ITEM)
		{	int index = players.indexOf(event.getActor());
			result[index] = result[index] + 1;
		}
		return result;
	}

	public long[] processBombings(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.EXPLOSE_PLAYER)
		{	// actor (can be null, ie: level)
			if(event.getActor()!=null)
			{	int index = players.indexOf(event.getActor());
				result[index] = result[index] + 1;
			}
		}
		return result;
	}

	public long[] processPaintings(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.WIN_TILE)
		{	// actor
			{	int index = players.indexOf(event.getActor());
				result[index] = result[index] + 1;
			}
			// target (can be null if the tile was neutral)
			if(event.getTarget()!=null)
			{	int index = players.indexOf(event.getTarget());
				result[index] = result[index] - 1;
			}
		}
		return result;
	}

	public long[] processTime(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.ELIMINATE_PLAYER)
		{	int index = players.indexOf(event.getTarget());
			long time = event.getTime();
			result[index] = -time;
		}
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
