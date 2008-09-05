package fr.free.totalboumboum.data.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.round.PlayMode;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.score.Score;

public class StatisticRound implements Serializable, StatisticBase
{
	private static final long serialVersionUID = 1L;

	private final ArrayList<String> players = new ArrayList<String>();
	private final ArrayList<StatisticEvent> events = new ArrayList<StatisticEvent>();
	private final HashMap<Score,long[]> scores = new HashMap<Score,long[]>();
	private PlayMode playMode;
	private float[] points;
	
	public void init(Round round)
	{	// players
		ArrayList<Profile> profiles = round.getProfiles();
		Iterator<Profile> it = profiles.iterator();
		while(it.hasNext())
		{	Profile temp = it.next();
			String name = temp.getName();
			players.add(name);
		}
		// play mode
		playMode = round.getPlayMode();
		// points
		points = new float[players.size()];
		for(int i=0;i<points.length;i++)
			points[i] = 0;
		// scores
		for (Score score : Score.values())
		{	long[] sc = score.init(this);
			scores.put(score,sc);
		}
	}
	
	public PlayMode getPlayMode()
	{	return playMode;	
	}
	
	public void addPlayer(String player)
	{	players.add(player);
	}
	public ArrayList<String> getPlayers()
	{	return players;
	}
	
	public void addEvent(StatisticEvent event)
	{	// events
		events.add(event);
		// scores
		Iterator<Score> i = scores.keySet().iterator();
		while(i.hasNext())
		{	Score temp = i.next();
			temp.process(this, event);
		}
	}

	public void computePoints(PointProcessor pointProcessor)
	{	points = pointProcessor.process(this);
	}

	public long[] getScores(Score score)
	{	long[] result;
		result = scores.get(score);
		return result;	
	}

	public ArrayList<StatisticEvent> getEvents()
	{	return events;
	}
	
	/** renvoie les points calcul�s a posteriori pour ce round */
	public float[] getPoints()
	{	return points;
	}

	/** inutile ici (on renvoie z�ro) */
	public float[] getPartialPoints()
	{	int size = players.size();
		float[] result = new float[size];
		for(int i=0;i<size;i++)
			result[i] = 0;
		return result; 
	}
}
