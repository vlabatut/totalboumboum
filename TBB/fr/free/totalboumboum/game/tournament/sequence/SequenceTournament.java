package fr.free.totalboumboum.game.tournament.sequence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticMatch;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.game.match.statistics.MatchStatistics;

public class SequenceTournament extends AbstractTournament
{
	
	public SequenceTournament(Configuration configuration)
	{	this.configuration = configuration; 
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean tournamentOver = false;
	
	@Override
	public void init() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	begun = true;
		// NOTE vérifier si le nombre de joueurs sélectionnés correspond
		setProfiles(getConfiguration().getProfiles());
		iterator = matches.iterator();
		stats.init(this);
	}

	@Override
	public boolean isOver()
	{	return tournamentOver;
	}
	
	@Override
	public void progress()
	{	if(!isOver())
		{	currentMatch = iterator.next();
			currentMatch.init(profiles);
		}
	}

	@Override
	public void finish()
	{	
	}
/*
	@Override
	public boolean hasBegun()
	{	boolean result = stats.getSize()>0;
		return result;
	}
*/
	@Override
	public boolean isReady()
	{	boolean result = true;
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Match> matches = new ArrayList<Match>();
	private Match currentMatch;
	private Iterator<Match> iterator;

	public void addMatch(Match match)
	{	matches.add(match);
	}

	@Override
	public Match getCurrentMatch()
	{	return currentMatch;	
	}
	@Override
	public void matchOver()
	{	StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		panel.matchOver();
		//
		if(!iterator.hasNext())
		{	stats.computePoints(pointProcessor);
			tournamentOver = true;
			panel.tournamentOver();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	@Override
	public void updatePlayerNumber()
	{	
		// TODO charger partiellement tous les matches 
		// pour déterminer le nombre de joueurs nécessaire
	}

	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointProcessor pointProcessor;

	public void setPointProcessor(PointProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}
	public PointProcessor getPointProcessor()
	{	return pointProcessor;
	}

}
