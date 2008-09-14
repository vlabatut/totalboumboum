package fr.free.totalboumboum.data.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.ranking.PlayerPoints;
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
		for(int j=0;j<points.length;j++)
			points[j] = 0;
		// scores
		for (Score score : Score.values())
		{	long[] sc = new long[players.size()];
			for(int i=0;i<sc.length;i++)
				sc[i] = 0;
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
	
	/** renvoie les points calculés a posteriori pour ce round */
	public float[] getPoints()
	{	return points;
	}

	/** inutile ici (on renvoie zéro) */
	public float[] getPartialPoints()
	{	int size = players.size();
		float[] result = new float[size];
		for(int i=0;i<size;i++)
			result[i] = 0;
		return result; 
	}
	
	public TreeSet<PlayerPoints> getOrderedPlayers()
	{	TreeSet<PlayerPoints> result = new TreeSet<PlayerPoints>();
		for(int i=0;i<points.length;i++)
		{	PlayerPoints pp = new PlayerPoints(players.get(i),i);
			pp.addPoint(points[i]);
			result.add(pp);
		}
		return result;
	}
	public ArrayList<PlayerPoints> getWinners()
	{	ArrayList<PlayerPoints> result = new ArrayList<PlayerPoints>();
		TreeSet<PlayerPoints> orderedPlayers = getOrderedPlayers();
		Iterator<PlayerPoints> i = orderedPlayers.descendingIterator();
		PlayerPoints first = orderedPlayers.last();
		boolean goOn = true;
		while(i.hasNext() && goOn)	
		{	PlayerPoints temp = i.next();
			if(temp.equalsPoints(first))
				result.add(temp);
			else
				goOn = false;
		}
		return result;
	}
	
	private long totalTime = 0;
	
	public void updateTime(long time)
	{	totalTime = time;
		long[] sc = scores.get(Score.TIME);
		for(int i=0;i<sc.length;i++)
		{	if(sc[i]>=0)
				sc[i] = time;
		}
	}
	public void finish(long time)
	{	updateTime(time+1);
		long[] sc = scores.get(Score.TIME);
		for(int i=0;i<sc.length;i++)
		{	if(sc[i]<0)
				sc[i] = -sc[i];
		}
	}
}
