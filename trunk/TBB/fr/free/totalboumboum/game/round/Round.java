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

public class Round
{
	/////////////////////////////////////////////////////////////////
	// GAME 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean roundOver = false;
	
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
			loop.init();
		}
	}
	
	public void finish()
	{	
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

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;

	public void setMatch(Match match)
	{	this.match = match;
	}
	
	public Configuration getConfiguration()
	{	return match.getConfiguration();	
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
	{	remainingPlayers --;
		if(remainingPlayers<2 && getPlayMode()==PlayMode.SURVIVAL)
		{	roundOver = true;
			playersInGame.set(index,new Boolean(false));
			for(int i=0;i<playersInGame.size();i++)
			{	if(playersInGame.get(i))	
				{	loop.reportVictory(i);
					//NOTE ici on pourrait calculer le temps de fin de partie dans les stats
				}
				else
					loop.reportDefeat(i);
			}
		}
		/* NOTE normalement ça devrait être beaucoup plus général :
		 * faudrait faire une fonction générale déterminant la fin de la partie, qui serait appelée quand un 
		 * héro meurt, quand le temps est terminé, quand toutes les couronnes sont ramassées...
		 * cette fonction détermine à qui il faut envoyer quoi (reportVictory ou Defeat)
		 */
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
			stats.computePoints(getPointProcessor());
		}
	}
}
