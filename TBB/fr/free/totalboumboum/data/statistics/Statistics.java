package fr.free.totalboumboum.data.statistics;

import java.io.Serializable;
import java.util.ArrayList;

public class Statistics implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final ArrayList<StatisticTournament> tournaments = new ArrayList<StatisticTournament>();
	
	public void addGame(StatisticTournament tournament)
	{	tournaments.add(tournament);
	}
}
