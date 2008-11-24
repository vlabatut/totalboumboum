package fr.free.totalboumboum.configuration.game;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.limit.LimitConfrontation;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsTotal;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundLoader;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.TournamentLoader;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.tools.FileTools;

public class GameConfiguration
{
	/////////////////////////////////////////////////////////////////
	// CURRENT TOURNAMENT	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament = null;

	public void loadLastTournament() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String folderPath = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_TOURNAMENT;
		tournament = TournamentLoader.loadTournamentFromFolderPath(folderPath);			
	}

	public AbstractTournament getTournament()
	{	return tournament;	
	}

	/////////////////////////////////////////////////////////////////
	// QUICKMATCH		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String quickmatchName = null;
	
	public String getQuickmatchName()
	{	return quickmatchName;	
	}
	public void setQuickmatchName(String quickmatchName)
	{	this.quickmatchName = quickmatchName;
	}
	
	public void loadQuickmatch() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// single tournament
		SingleTournament quickmatch = new SingleTournament();
		// load match
		Match match = MatchLoader.loadMatchFromName(quickmatchName,quickmatch);
		quickmatch.setMatch(match);
		// 
		tournament = quickmatch;
	}
	
	/////////////////////////////////////////////////////////////////
	// QUICKSTART		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String quickstartName = null;
	
	public String getQuickstartName()
	{	return quickstartName;	
	}	
	public void setQuickstartName(String quickstartName)
	{	this.quickstartName = quickstartName;
	}
	
	public void loadQuickstart() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// single tournament
		SingleTournament quickstart = new SingleTournament();
		// one round match
		Match match = new Match(quickstart);
		{	// notes
			ArrayList<String> notes = new ArrayList<String>();
			notes.add("auto-generated notes");
			match.setNotes(notes);
		}
		{	// limits
			PointsProcessor pointProcessor = new PointsTotal();
			Limits<MatchLimit> limits = new Limits<MatchLimit>();
			MatchLimit limit = new LimitConfrontation(1,true,pointProcessor);
			limits.addLimit(limit);
			match.setLimits(limits);
		}
		quickstart.setMatch(match);
		// round
		Round round = RoundLoader.loadRoundFromName(quickstartName,match);
		match.addRound(round);
		// 
		tournament = quickstart;
	}
}
