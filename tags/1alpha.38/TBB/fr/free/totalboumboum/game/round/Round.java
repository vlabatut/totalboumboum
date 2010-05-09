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
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.Player;
import fr.free.totalboumboum.game.match.LevelDescription;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.ranking.PlayerPoints;

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
	
	public void init(LevelDescription levelDescription) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	this.levelDescription = levelDescription;
		stats.init(this);
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
	{	// misc
		configuration = null;
		levelDescription = null;
		match = null;
		panel = null;
		playersInGame.clear();
		stats = null;
	}

	/////////////////////////////////////////////////////////////////
	// LOOP 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Loop loop = null;
	
	public Loop getLoop()
	{	return loop;
	}	
	
	public void loopOver()
	{	match.currentRoundOver();
		panel.roundOver();
		// loop
		loop = null;
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticRound stats = new StatisticRound();

	public StatisticRound getStats()
	{	return stats;
	}

	/////////////////////////////////////////////////////////////////
	// LEVEL DESCRIPTION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelDescription levelDescription;
	
	public PointProcessor getPointProcessor()
	{	return levelDescription.getPointProcessor();	
	}
	public PlayMode getPlayMode()
	{	return levelDescription.getPlayMode();	
	}
	public LevelDescription getLevelDescription()
	{	return levelDescription;	
	}
	public long getTimeLimit()
	{	return levelDescription.getTimeLimit();	
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
			{	roundOver = true;
				stats.finish(loop.getTotalTime());
				stats.computePoints(getPointProcessor());
				celebrate();
			}
		}
	}
	public void updateTime(long time)
	{	if(!roundOver)
		{	stats.updateTime(time);			
//			if(getTimeLimit()>0 && time>=getTimeLimit()/getConfiguration().getSpeedCoeff())
			if(getTimeLimit()>0 && time>=getTimeLimit())
			{	roundOver = true;
				stats.finish(loop.getTotalTime());
				stats.computePoints(getPointProcessor());
				celebrate();
			}
			else
				stats.computePoints(getPointProcessor());
		}
	}
	private void celebrate()
	{	ArrayList<PlayerPoints> winners = stats.getWinners();
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
}
