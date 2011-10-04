package fr.free.totalboumboum.game.match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.StatisticMatch;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.points.PointsTotal;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.LevelDescription;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class Match
{	
	public Match(AbstractTournament tournament)
	{	this.tournament = tournament;
		configuration = tournament.getConfiguration();
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament;
	
    private Configuration configuration;
	public Configuration getConfiguration()
	{	return configuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean matchOver = false;
	
	public void init(ArrayList<Profile> profiles)
	{	// are rounds in random order ?
    	if(randomOrder)
    	{	Calendar cal = new GregorianCalendar();
			long seed = cal.getTimeInMillis();
			Random random = new Random(seed);
			Collections.shuffle(rounds,random);
    	}
    	
		
		// NOTE v�rifier si le nombre de joueurs s�lectionn�s correspond
		this.profiles.addAll(profiles); 
		iterator = rounds.iterator();
		stats.init(this);
	}

	public void progress() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	if(!isOver())
		{	Round round = iterator.next();
			currentRound = round.copy();
			currentRound.init();
		}
	}

	public boolean isOver()
	{	return matchOver;
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits<MatchLimit> limits;

	public Limits<MatchLimit> getLimits()
	{	return limits;
	}
	public void setLimits(Limits<MatchLimit> limits)
	{	this.limits = limits;
	}
			
	/////////////////////////////////////////////////////////////////
	// ROUNDS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean randomOrder;
	private ArrayList<Round> rounds = new ArrayList<Round>();

	public boolean getRandomOrder()
	{	return randomOrder;
	}
	public void setRandomOrder(boolean randomOrder)
	{	this.randomOrder = randomOrder;
	}
	
	public void addRound(Round round)
	{	rounds.add(round);		
	}
	public ArrayList<Round> getRound()
	{	return rounds;	
	}
	public void setRounds(ArrayList<Round> rounds)
	{	this.rounds.addAll(rounds);			
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<Profile> profiles = new ArrayList<Profile>();
	private int minPlayerNumber;
	private int maxPlayerNumber;

	public void addProfile(Profile profile)
	{	profiles.add(profile);
	}
	public ArrayList<Profile> getProfiles()
	{	return profiles;	
	}
	
	public void updateMinPlayerNumber()
	{	
		// TODO charger partiellement tous les matches 
		// pour d�terminer le nombre de joueurs n�cessaire
	}
	public int getMinPlayerNumber()
	{	return minPlayerNumber;			
	}
	public int getMaxPlayerNumber()
	{	return maxPlayerNumber;			
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUNDS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Round currentRound;
	private Iterator<Round> iterator;
	
	public Round getCurrentRound()
	{	return currentRound;	
	}
	public void roundOver()
	{	// stats
		StatisticRound statsRound = currentRound.getStats();
		stats.addStatisticRound(statsRound);
		stats.computePoints(pointProcessor);
		// iterator
		if(!iterator.hasNext())
			iterator = rounds.iterator();
		// limits
		int limit = limits.testLimits(stats);
		if(limit>=0)
		{	if(limit>=0 && limit<profiles.size())
				stats.setWinner(limit);
			matchOver = true;
			tournament.matchOver();
			panel.matchOver();
		}
		else
		{	//tournament.roundOver();
			panel.roundOver();
		}
	}
	
	public void finish()
	{	// rounds
		currentRound = null;
		iterator = null;
		rounds.clear();
		// limits
		limits.finish();
		limits = null;
		// misc
		configuration = null;
		panel = null;
		pointProcessor = null;
		profiles.clear();
		stats = null;
		tournament = null;
		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
	}
	
	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsProcessor pointProcessor;

	public void setPointProcessor(PointsProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}
	public PointsProcessor getPointProcessor()
	{	return pointProcessor;
	}

	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticMatch stats = new StatisticMatch();
	
	public StatisticMatch getStats()
	{	return stats;
	}
	
	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MatchRenderPanel panel;
	
	public void setPanel(MatchRenderPanel panel)
	{	this.panel = panel;
	}
	public MatchRenderPanel getPanel()
	{	return panel;	
	}

	/////////////////////////////////////////////////////////////////
	// NOTES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<String> notes = new ArrayList<String>();

	public void setNotes(ArrayList<String> notes)
	{	this.notes.addAll(notes);
	}
	public ArrayList<String> getNotes()
	{	return notes;
	}
	
	
	public Match copy()
	{	Match result = new Match(tournament);
		// rounds
		Iterator<Round> i = rounds.iterator();
		while (i.hasNext())
		{	Round round = i.next();
			Round copy = round.copy();
			copy.setMatch(result);
			result.addRound(copy);
		}
		// misc
		result.setNotes(notes);
		result.setLimits(limits);
		result.pointProcessor = pointProcessor;
		return result;
	}
}