package fr.free.totalboumboum.data.game;

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

public class GameConfiguration
{

	/////////////////////////////////////////////////////////////////
	// CURRENT TOURNAMENT	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament = null;

	public AbstractTournament getTournament()
	{	return tournament;	
	}
	
	/////////////////////////////////////////////////////////////////
	// LAST TOURNAMENT		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String lastTournamentName = null;
	
	public String getLastTournamentName()
	{	return lastTournamentName;	
	}
	
	public void setLastTournamentName(String lastTournamentName)
	{	this.lastTournamentName = lastTournamentName;
	}
	
	public void loadLastTournament() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	tournament = TournamentLoader.loadTournamentFromName(lastTournamentName,this);			
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
		SingleTournament quickmatch = new SingleTournament(this);
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
		SingleTournament quickstart = new SingleTournament(this);
		// one round match
		Match match = new Match(quickstart);
		{	// notes
			ArrayList<String> notes = new ArrayList<String>();
			notes.add("auto-generated notes");
			match.setNotes(notes);
		}
		{	// limits
			Limits<MatchLimit> limits = new Limits<MatchLimit>();
			MatchLimit limit = new LimitConfrontation(1);
			limits.addLimit(limit);
			match.setLimits(limits);
		}
		{	// points processor
			PointsProcessor pointProcessor = new PointsTotal();
			match.setPointProcessor(pointProcessor);
		}
		quickstart.setMatch(match);
		// round
		Round round = RoundLoader.loadRoundFromName(quickstartName,match);
		match.addRound(round);
		// 
		tournament = quickstart;
	}

}
