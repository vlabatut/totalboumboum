package fr.free.totalboumboum.data.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.score.Score;
import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class StatisticTournament implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Date date;
//	private PlayerId host;
	
	private final ArrayList<String> players = new ArrayList<String>();	
	private final ArrayList<StatisticMatch> matches = new ArrayList<StatisticMatch>();
	private final HashMap<Score,long[]> scores = new HashMap<Score,long[]>();
	private float[] points;
	
	//NOTE à généraliser à matches et round, puisqu'on peut sauver pour reprendre plus tard...
	public StatisticTournament()
	{	Calendar cal = new GregorianCalendar();
		date = cal.getTime();
	}
	
	public void init(AbstractTournament tournament)
	{	// players
		ArrayList<Profile> profiles = tournament.getProfiles();
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
	
	public void addStatisticMatch(StatisticMatch match)
	{	// matches stats
		matches.add(match);
		// scores
		for (Score score : Score.values())
		{	long[] currentScores = scores.get(score);
			long[] matchScores = match.getScores(score);
			for(int i=0;i<matchScores.length;i++)
				currentScores[i] = currentScores[i] + matchScores[i];
		}
		// points
		float[] matchPoints = match.getPoints();
		for(int i=0;i<players.size();i++)
			points[i] = points[i] + matchPoints[i];
	}
	
	public Date getDate()
	{	return date;
	}
	public void setDate(Date date)
	{	this.date = date;
	}
	
	public int getSize()
	{	return matches.size();
	}

	public long[] getScores(Score score)
	{	long[] result;
		result = scores.get(score);
		return result;	
	}

	public float[] getPoints()
	{	return points;		
	}

	public ArrayList<StatisticMatch> getStatMatches()
	{	return matches;
	}
}


