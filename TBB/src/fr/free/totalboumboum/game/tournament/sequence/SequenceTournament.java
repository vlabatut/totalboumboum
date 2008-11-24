package fr.free.totalboumboum.game.tournament.sequence;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.TournamentLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.statistics.StatisticMatch;
import fr.free.totalboumboum.game.statistics.StatisticTournament;
import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class SequenceTournament extends AbstractTournament
{

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits<TournamentLimit> limits;

	public Limits<TournamentLimit> getLimits()
	{	return limits;
	}
	public void setLimits(Limits<TournamentLimit> limits)
	{	this.limits = limits;
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean tournamentOver = false;
	
	@Override
	public void init() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	begun = true;
		
		// are matches in random order ?
		if(randomOrder)
		{	Calendar cal = new GregorianCalendar();
			long seed = cal.getTimeInMillis();
			Random random = new Random(seed);
			Collections.shuffle(matches,random);
		}
		
		// NOTE vérifier si le nombre de joueurs sélectionnés correspond
		setProfiles(Configuration.getProfilesConfiguration().getSelected());
		iterator = matches.iterator();
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public boolean isOver()
	{	return tournamentOver;
	}
	
	@Override
	public void progress()
	{	if(!isOver())
		{	Match match = iterator.next();
			currentMatch = match.copy();
			currentMatch.init(profiles);
		}
	}

	@Override
	public void finish()
	{	
		// limits
		limits.finish();
		limits = null;
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
	private boolean randomOrder;
	private ArrayList<Match> matches = new ArrayList<Match>();
	private Match currentMatch;
	private Iterator<Match> iterator;

	public boolean getRandomOrder()
	{	return randomOrder;
	}
	public void setRandomOrder(boolean randomOrder)
	{	this.randomOrder = randomOrder;
	}

	public void addMatch(Match match)
	{	matches.add(match);
	}

	@Override
	public Match getCurrentMatch()
	{	return currentMatch;	
	}
	@Override
	public void matchOver()
	{	// stats
		StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		float[] points = limits.processPoints(this);
		stats.setPoints(points);
		// iterator
		if(!iterator.hasNext())
			iterator = matches.iterator();
		// limits
		if(getLimits().testLimit(this))
		{	tournamentOver = true;
			panel.tournamentOver();
			stats.initEndDate();
		}
		else
		{	panel.matchOver();		
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
}
