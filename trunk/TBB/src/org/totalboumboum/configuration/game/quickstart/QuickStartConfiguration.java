package org.totalboumboum.configuration.game.quickstart;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.game.limit.Comparisons;
import org.totalboumboum.game.limit.LimitConfrontation;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.MatchLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.points.PointsProcessor;
import org.totalboumboum.game.points.PointsTotal;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundLoader;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class QuickStartConfiguration
{
	public QuickStartConfiguration copy()
	{	QuickStartConfiguration result = new QuickStartConfiguration();

		// quickstart
		ProfilesSelection quickstartCopy = profilesSelection.copy();
		result.setProfilesSelection(quickstartCopy);
		result.setRoundName(new StringBuffer(roundName));
		result.setAllowedPlayers(new TreeSet<Integer>(allowedPlayers));
		return result;
	}
	
	public boolean hasChanged(QuickStartConfiguration copy)
	{	boolean result = false;
		// round
		if(!result)
		{	StringBuffer copyRound = copy.getRoundName();
			result = !roundName.equals(copyRound);
		}
		// profiles selection
		if(!result)
		{	ProfilesSelection copyProfiles = copy.getProfilesSelection();
			result = !profilesSelection.equals(copyProfiles);
		}
		//
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ProfilesSelection profilesSelection = new ProfilesSelection();

	public ProfilesSelection getProfilesSelection()
	{	return profilesSelection;	
	}	
	public void setProfilesSelection(ProfilesSelection profilesSelection)
	{	this.profilesSelection = profilesSelection;	
	}	

	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StringBuffer roundName = null;
	private Set<Integer> allowedPlayers = new TreeSet<Integer>();
	
	public Set<Integer> getAllowedPlayers()
	{	return allowedPlayers;
	}
	public void setAllowedPlayers(Set<Integer> allowedPlayers)
	{	this.allowedPlayers = allowedPlayers;
	}

	public StringBuffer getRoundName()
	{	return roundName;	
	}	
	public void setRoundName(StringBuffer roundName)
	{	this.roundName = roundName;
	}
	
	public SingleTournament loadQuickstart() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// single tournament
		SingleTournament result = new SingleTournament();
		result.setAuthor("quick start");
		result.setName("quick start");
		
		// one round match
		Match match = new Match(result);
		match.setAuthor("quick start");
		{	// notes
			List<String> notes = new ArrayList<String>();
			notes.add("auto-generated notes");
			match.setNotes(notes);
		}
		{	// limits
			PointsProcessor pointProcessor = new PointsTotal();
			Limits<MatchLimit> limits = new Limits<MatchLimit>();
			MatchLimit limit = new LimitConfrontation(1,Comparisons.GREATEREQ,pointProcessor);
			limits.addLimit(limit);
			match.setLimits(limits);
		}
		result.setMatch(match);
		
		// round
		Round round = RoundLoader.loadRoundFromName(roundName.toString(),match);
		match.addRound(round);
		// 
		return result;
	}
}
