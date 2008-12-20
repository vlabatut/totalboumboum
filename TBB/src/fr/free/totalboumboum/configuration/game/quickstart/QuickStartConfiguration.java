package fr.free.totalboumboum.configuration.game.quickstart;

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

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.game.limit.ComparatorCode;
import fr.free.totalboumboum.game.limit.LimitConfrontation;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsTotal;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundLoader;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;

public class QuickStartConfiguration
{
	public QuickStartConfiguration copy()
	{	QuickStartConfiguration result = new QuickStartConfiguration();

		// quickstart
		ProfilesSelection quickstartCopy = profilesSelection.copy();
		result.setQuickStartSelected(quickstartCopy);
		result.setRoundName(roundName);

		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// QUICKSTART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ProfilesSelection profilesSelection = new ProfilesSelection();
	private String roundName = null;
	
	public String getRoundName()
	{	return roundName;	
	}	
	public void setRoundName(String roundName)
	{	this.roundName = roundName;
	}
	
	public ProfilesSelection getProfilesSelection()
	{	return profilesSelection;	
	}	
	public void setQuickStartSelected(ProfilesSelection profilesSelection)
	{	this.profilesSelection = profilesSelection;	
	}	

	public SingleTournament loadQuickstart() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// single tournament
		SingleTournament result = new SingleTournament();
		// one round match
		Match match = new Match(result);
		{	// notes
			ArrayList<String> notes = new ArrayList<String>();
			notes.add("auto-generated notes");
			match.setNotes(notes);
		}
		{	// limits
			PointsProcessor pointProcessor = new PointsTotal();
			Limits<MatchLimit> limits = new Limits<MatchLimit>();
			MatchLimit limit = new LimitConfrontation(1,ComparatorCode.GREATEREQ,pointProcessor);
			limits.addLimit(limit);
			match.setLimits(limits);
		}
		result.setMatch(match);
		// round
		Round round = RoundLoader.loadRoundFromName(roundName,match);
		match.addRound(round);
		// 
		return result;
	}
}
