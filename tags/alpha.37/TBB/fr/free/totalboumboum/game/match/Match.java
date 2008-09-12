package fr.free.totalboumboum.game.match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.StatisticMatch;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.point.PointPoints;
import fr.free.totalboumboum.game.point.PointProcessor;
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
	{	// NOTE vérifier si le nombre de joueurs sélectionnés correspond
		this.profiles.addAll(profiles); 
		iterator = levels.iterator();
		stats.init(this);
	}

	public void progress() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	if(!isOver())
		{	// init round
			LevelDescription levelDescription = iterator.next();
			currentRound = new Round(this);
			currentRound.init(levelDescription);
			rounds.add(currentRound);
		}
	}

	public boolean isOver()
	{	return matchOver;
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MatchLimit matchLimit;
	private int matchLimitValue;

	public MatchLimit getMatchLimit() {
		return matchLimit;
	}
	public void setMatchLimit(MatchLimit matchLimit)
	{	this.matchLimit = matchLimit;
	}
	
	public int getMatchLimitValue()
	{	return matchLimitValue;
	}
	public void setMatchLimitValue(int matchLimitValue)
	{	this.matchLimitValue = matchLimitValue;
	}
		
	/////////////////////////////////////////////////////////////////
	// LEVELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean randomOrder;
	private ArrayList<LevelDescription> levels = new ArrayList<LevelDescription>();

	public boolean getRandomOrder()
	{	return randomOrder;
	}
	public void setRandomOrder(boolean randomOrder)
	{	this.randomOrder = randomOrder;
	}
	
	public void addLevelDescription(LevelDescription level)
	{	levels.add(level);		
	}
	public ArrayList<LevelDescription> getLevelDescriptions()
	{	return levels;	
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
		// pour déterminer le nombre de joueurs nécessaire
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
	private ArrayList<Round> rounds = new ArrayList<Round>();
	private Round currentRound;
	private Iterator<LevelDescription> iterator;
	
	public Round getCurrentRound()
	{	return currentRound;	
	}
	public void currentRoundOver()
	{	StatisticRound statsRound = currentRound.getStats();
		stats.addStatisticRound(statsRound);
		//
		int played = rounds.size();
		switch(matchLimit)
		{	// all levels have to be played
			case ALL_LEVELS:
				int levelNbr = levels.size();
				matchOver = levelNbr == played;
				break;
			// a fixed number of rounds have to be played
			case ROUND_LIMIT:
				matchOver = played == matchLimitValue;
				break;
			// a fixed number of points have to be scored
			case POINTS_LIMIT:
				//NOTE à compléter
				matchOver = played == matchLimitValue;
				break;
		}
		if(matchOver)
		{	stats.computePoints(pointProcessor);
			tournament.currentMatchOver();
			panel.matchOver();
		}
		else
			panel.roundOver();
	}
	
	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointProcessor pointProcessor = new PointPoints();

	public void setPointProcessor(PointProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}
	public PointProcessor getPointProcessor()
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
}
