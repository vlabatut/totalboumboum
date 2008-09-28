package fr.free.totalboumboum.data.statistics;

import java.util.ArrayList;


public enum Score
{	BOMBS, CROWNS, DEATHS, ITEMS, KILLS, PAINTINGS, TIME;
	
	public long[] process(StatisticRound stats, StatisticEvent event)
	{	long[] result = new long[stats.getPlayers().size()];
		switch(this)
		{	case BOMBS:
				result = processBombs(stats,event);
				break;
			case CROWNS:
				result = processCrowns(stats,event);
				break;
			case DEATHS:
				result = processDeaths(stats,event);
				break;
			case ITEMS:
				result = processItems(stats,event);
				break;
			case KILLS:
				result = processKills(stats,event);
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

	public long[] processDeaths(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.KILL_PLAYER)
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

	public long[] processKills(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		if(event.getAction() == StatisticAction.KILL_PLAYER)
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

//TODO trouver un moyen de gérer TIME en temps réel...
	public long[] processTime(StatisticRound stats, StatisticEvent event)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		long result[] = stats.getScores(this);
		// processing
		//NOTE pour les differents playModes, il faudra surement distinguer mort du joueur (burn, shrink ou autre) et élimination définitive (plus aucun controle ou vengence)
		if(event.getAction() == StatisticAction.KILL_PLAYER)
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
