package fr.free.totalboumboum.data.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.point.PointProcessor;

public class StatisticMatch implements Serializable, StatisticBase
{
	private static final long serialVersionUID = 1L;

	private final ArrayList<String> players = new ArrayList<String>();
	private final ArrayList<StatisticRound> rounds = new ArrayList<StatisticRound>();
	private final HashMap<Score,long[]> scores = new HashMap<Score,long[]>();
	private float[] points;
	private float[] partialPoints;
	
	public void init(Match match)
	{	// players
		ArrayList<Profile> profiles = match.getProfiles();
		Iterator<Profile> it = profiles.iterator();
		while(it.hasNext())
		{	Profile temp = it.next();
			String name = temp.getName();
			players.add(name);
		}
		// points
		points = new float[players.size()];
		for(int j=0;j<points.length;j++)
			points[j] = 0;
		// partial points
		partialPoints = new float[players.size()];
		for(int j=0;j<partialPoints.length;j++)
			partialPoints[j] = 0;
		// scores
		for (Score score : Score.values())
		{	long[] sc = new long[players.size()];
			for(int i=0;i<sc.length;i++)
				sc[i] = 0;
			scores.put(score,sc);
		}
	}
	
	public void addPlayer(String player)
	{	players.add(player);
	}
	public ArrayList<String> getPlayers()
	{	return players;
	}

	public void addStatisticRound(StatisticRound round)
	{	// round stats
		rounds.add(round);
		// scores
		for (Score score : Score.values())
		{	long[] currentScores = scores.get(score);
			long[] roundScores = round.getScores(score);
			for(int i=0;i<roundScores.length;i++)
				currentScores[i] = currentScores[i] + roundScores[i];
		}
		// partial points
		float[] roundPoints = round.getPoints();
		for(int i=0;i<players.size();i++)
			partialPoints[i] = partialPoints[i] + roundPoints[i];
	}
	
	public void computePoints(PointProcessor pointProcessor)
	{	points = pointProcessor.process(this);
	}
	
	public int getSize()
	{	return rounds.size();
	}

	public long[] getScores(Score score)
	{	long[] result;
		result = scores.get(score);
		return result;	
	}
	
	/** renvoie les points calculés a posteriori pour ce match*/
	public float[] getPoints()
	{	return points;		
	}

	/** 
	 * renvoie la somme des points obtenus lors des rounds de ce match,
	 * qui sera utilisée pour calculer les points obtenus lors de ce match  
	 */
	public float[] getPartialPoints()
	{	return partialPoints;
	}

	public ArrayList<StatisticRound> getStatRounds()
	{	return rounds;
	}

	@Override
	public int getConfrontationCount()
	{	int result = rounds.size();
		return result;
	}
}