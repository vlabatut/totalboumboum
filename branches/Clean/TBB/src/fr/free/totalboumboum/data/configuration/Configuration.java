package fr.free.totalboumboum.data.configuration;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
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
import fr.free.totalboumboum.tools.XmlTools;

public class Configuration
{	
	public Configuration() throws ParserConfigurationException, SAXException, IOException
	{	// engine
		setFps(50);
		setSpeedCoeff(1);
		// display
		setBorderColor(Color.BLACK);
		setSmoothGraphics(true);
		// panel
		setPanelDimension(750,600);
		// profiles
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int fps;
	private long milliPeriod;
	private long nanoPeriod;
	//NOTE speedcoeff à descendre au niveau de loop, car il peut dépendre du level
	private double speedCoeff;

	public void setFps(int fps)
	{	this.fps = fps;
		milliPeriod = (long) 1000.0 / fps;
		nanoPeriod = milliPeriod * 1000000L;
	}
	public int getFps()
	{	return fps;
	}
	public long getMilliPeriod()
	{	return milliPeriod;
	}
	public long getNanoPeriod()
	{	return nanoPeriod;
	}

	public double getSpeedCoeff()
	{	return speedCoeff;
	}
	public void setSpeedCoeff(double speedCoeff)
	{	this.speedCoeff = speedCoeff;
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Color borderColor;
	private boolean smoothGraphics;

	public void setSmoothGraphics(boolean smoothGraphics)
	{	this.smoothGraphics = smoothGraphics;		
	}
	public boolean getSmoothGraphics()
	{	return smoothGraphics;		
	}
	
	public Color getBorderColor()
	{	return borderColor;
	}
	public void setBorderColor(Color borderColor)
	{	this.borderColor = borderColor;
	}
	
	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Dimension panelDimension;
	
	public void setPanelDimension(int width, int height)
	{	panelDimension = new Dimension(width,height);
	}
	public Dimension getPanelDimension()
	{	return panelDimension;	
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<String> profiles = new ArrayList<String>();
	
	public ArrayList<String> getProfiles()
	{	return profiles;	
	}
	
	public void addProfile(String profile)
	{	if(!profiles.contains(profile))
			profiles.add(profile);
	}

	
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
