package fr.free.totalboumboum.game.round;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.StatisticEvent;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.engine.container.level.LevelDescription;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.Player;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PlayerPoints;
import fr.free.totalboumboum.game.points.PointsProcessor;

public class Round
{
	public Round(Match match)
	{	this.match = match;
		configuration = match.getConfiguration();
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean roundOver = false;
	
	public void loadStepOver()
	{	panel.loadStepOver();		
	}
	
	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	stats.init(this);
		remainingPlayers = getProfiles().size();
		for(int i=0;i<remainingPlayers;i++)
			playersInGame.add(new Boolean(true));
	}
	
	public boolean isOver()
	{	return roundOver;
	}
	
	public void progress() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	if(!isOver())
		{	loop = new Loop(this);
			Thread animator = new Thread(loop);
			animator.start();
//			loop.init();
		}
	}
	
	public void finish()
	{	// loop
		loop.finish();
		loop = null;
		// level description
		levelDescription.finish();
		levelDescription = null;
		// misc
		configuration = null;
		levelDescription = null;
		match = null;
		panel = null;
		playersInGame.clear();
		stats = null;
		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
	}

	/////////////////////////////////////////////////////////////////
	// LOOP 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Loop loop = null;
	
	public Loop getLoop()
	{	return loop;
	}	
	
	public void loopOver()
	{	match.roundOver();
		panel.roundOver();
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticRound stats = new StatisticRound();

	public StatisticRound getStats()
	{	return stats;
	}

	/////////////////////////////////////////////////////////////////
	// PLAY MODE	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PlayMode playMode;
	
	public PlayMode getPlayMode()
	{	return playMode;	
	}
	public void setPlayMode(PlayMode playMode)
	{	this.playMode = playMode;	
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;
	
    private Configuration configuration;
	public Configuration getConfiguration()
	{	return configuration;	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int remainingPlayers;
	private final ArrayList<Boolean> playersInGame = new ArrayList<Boolean>();

	public ArrayList<Profile> getProfiles()
	{	return match.getProfiles();
	}	
	
	public void playerOut(int index)
	{	if(!roundOver)
		{	remainingPlayers --;
			playersInGame.set(index,new Boolean(false));
			if(remainingPlayers<2 /*&& getPlayMode()==PlayMode.SURVIVAL*/)
				closeGame();		
		}
	}
	public void updateTime(long time)
	{	if(!roundOver)
		{	stats.updateTime(time);			
//			if(getTimeLimit()>0 && time>=getTimeLimit()/getConfiguration().getSpeedCoeff())
			int limit = getLimits().testLimits(stats);
			if(limit>=0)
			{	// close game
				roundOver = true;
				stats.finish(loop.getTotalTime());
				stats.computePoints(getPointProcessor());
				if(limit>=0 && limit<getProfiles().size())
					stats.setWinner(limit);
				celebrate();		
			}
			else
				stats.computePoints(getPointProcessor());
		}
	}
	public void closeGame()
	{	roundOver = true;
		stats.finish(loop.getTotalTime());
		stats.computePoints(getPointProcessor());
		celebrate();		
	}
	private void celebrate()
	{	loop.initCelebrationDelay();
		ArrayList<PlayerPoints> winners = stats.getWinners();
		// indexes of the winners
		ArrayList<Integer> winnersIndex = new ArrayList<Integer>();
		{	Iterator<PlayerPoints> i = winners.iterator();
			while(i.hasNext())
			{	PlayerPoints pp = i.next();
				winnersIndex.add(new Integer(pp.getIndex()));
			}
		}
		// celebration time !
		for(int i=0;i<getProfiles().size();i++)
		{	if(winnersIndex.contains(new Integer(i)))	
				loop.reportVictory(i);
			else
				loop.reportDefeat(i);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RoundRenderPanel panel;
	
	public void setPanel(RoundRenderPanel panel)
	{	this.panel = panel;
	}
	public RoundRenderPanel getPanel()
	{	return panel;	
	}
	
	public void addStatisticEvent(StatisticEvent event)
	{	if(!isOver())
		{	stats.addEvent(event);
			//stats.computePoints(getPointProcessor());
		}
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
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits<RoundLimit> limits;

	public Limits<RoundLimit> getLimits()
	{	return limits;
	}
	public void setLimits(Limits<RoundLimit> limits)
	{	this.limits = limits;
	}
			
	/////////////////////////////////////////////////////////////////
	// LEVEL DESCRIPTION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelDescription levelDescription;
	
	public LevelDescription getLevelDescription()
	{	return levelDescription;	
	}
	public void setLevelDescription(LevelDescription levelDescription)
	{	this.levelDescription = levelDescription;	
	}

	
	public Round copy()
	{	Round result = new Round(match);
		result.setNotes(notes);
		result.setLimits(limits);
		result.setPlayMode(playMode);
		result.pointProcessor = pointProcessor;
		result.setLevelDescription(levelDescription);
		return result;
	}
	
	public void setMatch(Match match)
	{	this.match = match;	
	}
}
