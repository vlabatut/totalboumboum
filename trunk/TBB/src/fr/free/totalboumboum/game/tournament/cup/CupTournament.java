package fr.free.totalboumboum.game.tournament.cup;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.statistics.StatisticMatch;
import fr.free.totalboumboum.game.statistics.StatisticTournament;
import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class CupTournament extends AbstractTournament
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
	
		// are players in random order ?
		if(randomizePlayers)
			randomizePlayers();
		
		// are legs in random order ?
		if(randomizeLegs)
			randomizeLegs();
		
		// NOTE vérifier si le nombre de joueurs sélectionnés correspond
		currentIndex = 0;
		currentLeg = legs.get(currentIndex);
		currentLeg.init();
		
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(currentLeg.isOver())
		{	currentIndex++;
			currentLeg = legs.get(currentIndex);
			currentLeg.init();
		}
		else
			currentLeg.progress();
	}
	
	@Override
	public void finish()
	{	// legs
//		for(CupLeg leg:legs)
//			leg.finish();
		legs.clear();
		currentLeg = null;
	}
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean randomizePlayers;

	public boolean isRandomizePlayers()
	{	return randomizePlayers;
	}

	public void setRandomizePlayers(boolean randomizePlayers)
	{	this.randomizePlayers = randomizePlayers;
	}

	private void randomizePlayers()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(players,random);
	}

	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	Set<Integer> result = new TreeSet<Integer>();
	
		ArrayList<Set<Integer>> ap = new ArrayList<Set<Integer>>();
		CupLeg leg = legs.get(0);
		ArrayList<CupPart> parts = leg.getParts();
		// only one leg
		if(legs.size()==1)
		{	for(CupPart part: parts)
			{	Set<Integer> temp = part.getMatch().getAllowedPlayerNumbers();
				int max = part.getPlayers().size();
				for(int i=GameConstants.MAX_PROFILES_COUNT;i>max;i--)
					temp.remove(i);
				ap.add(temp);
			}
		}
		// several legs
		else //if(legs.size()>1)
		{	for(CupPart part: parts)
			{	Set<Integer> temp = part.getMatch().getAllowedPlayerNumbers();
				int max = part.getPlayers().size();
				for(int i=GameConstants.MAX_PROFILES_COUNT;i>max;i--)
					temp.remove(i);
				ap.add(temp);
			}
			// players needed in next legs
			int[] counts = new int[parts.size()];
			Arrays.fill(counts,0);
			
				
		}
		
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	@Override
	public int[] getOrderedPlayers()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/////////////////////////////////////////////////////////////////
	// LEGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<CupLeg> legs = new ArrayList<CupLeg>();
	private boolean randomizeLegs;
	private int currentIndex;
	private CupLeg currentLeg;
	
	public ArrayList<CupLeg> getLegs()
	{	return legs;	
	}
	
	public CupLeg getLeg(int index)
	{	return legs.get(index);	
	}
	
	public void addLeg(CupLeg leg)
	{	legs.add(leg);	
	}
	
	public CupLeg getCurrentLeg()
	{	return currentLeg;	
	}
	
	private void randomizeLegs()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(legs,random);
	}

	public boolean getRandomizeLegs()
	{	return randomizeLegs;
	}
	public void setRandomizeLegs(boolean randomizeLegs)
	{	this.randomizeLegs = randomizeLegs;
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Match getCurrentMatch()
	{	CupPart currentPart = currentLeg.getCurrentPart();
		Match match = currentPart.getCurrentMatch();
		return match;
	}

	@Override
	public void matchOver()
	{	// stats
		Match currentMatch = getCurrentMatch();
		StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		
		currentLeg.matchOver();
		if(currentLeg.isOver() && currentIndex==legs.size()-1)
		{	setOver(true);
			panel.tournamentOver();
			stats.initEndDate();
		}
		else
		{	panel.matchOver();		
		}
	}
}
