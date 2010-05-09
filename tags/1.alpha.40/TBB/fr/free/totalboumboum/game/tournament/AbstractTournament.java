package fr.free.totalboumboum.game.tournament;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.profile.ProfileLoader;
import fr.free.totalboumboum.data.statistics.StatisticTournament;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchRenderPanel;
import fr.free.totalboumboum.game.point.PointProcessor;

public abstract class AbstractTournament
{	
	/////////////////////////////////////////////////////////////////
	// GAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean begun = false;
	
	public abstract void init() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException;
	public abstract void progress();
	public abstract void finish();
	public abstract boolean isOver();
	
	public abstract boolean isReady();
	
	public boolean hasBegun()
	{	return begun;	
	}

	/////////////////////////////////////////////////////////////////
	// MATCHES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract Match getCurrentMatch();
	public abstract void matchOver();

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Configuration configuration; //NOTE a virer quand on aura mis une référence vers de la GUI (?)
	
	public Configuration getConfiguration()
	{	return configuration;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ArrayList<Profile> profiles = new ArrayList<Profile>();
	protected int minPlayerNumber;
	protected int maxPlayerNumber;

	public void setProfiles(ArrayList<String> profilesNames) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Iterator<String> i = profilesNames.iterator();
		while(i.hasNext())
		{	String name = i.next();
			Profile profile = ProfileLoader.loadProfile(name);
			profiles.add(profile);
		}
	}
	public ArrayList<Profile> getProfiles()
	{	return profiles;	
	}

	public abstract void updatePlayerNumber();
	
	public int getMinPlayerNumber()
	{	return minPlayerNumber;			
	}
	public int getMaxPlayerNumber()
	{	return maxPlayerNumber;			
	}

	/////////////////////////////////////////////////////////////////
	// NAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String name = "N/A";

	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected StatisticTournament stats = new StatisticTournament();

	public StatisticTournament getStats()
	{	return stats;
	}

	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected TournamentRenderPanel panel;
	
	public void setPanel(TournamentRenderPanel panel)
	{	this.panel = panel;
	}
	public TournamentRenderPanel getPanel()
	{	return panel;	
	}
}
