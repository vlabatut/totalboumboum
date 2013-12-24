package org.totalboumboum.game.tournament.league;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.points.PointsProcessor;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.StatisticMatch;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.computing.CombinatoricsTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * This class represents a tournament in which
 * all players must play against each other at
 * some point, possibly several times. It is
 * based on the league model used in most sports.
 * 
 * @author Vincent Labatut
 */
public class LeagueTournament extends AbstractTournament
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new league tournament object.
	 * Possible reads the confrontation maps from
	 * a file, if needed.
	 * 
	 * @throws IOException
	 * 		Problem while loading the confrontation maps. 
	 * @throws SAXException 
	 * 		Problem while loading the confrontation maps. 
	 */
	public LeagueTournament() throws SAXException, IOException
	{	loadConfrontationMaps();
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
	
		// players
		if(randomizePlayers)
			Collections.shuffle(profiles);
	
		// matches
		initMatches();
		
		// stats
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(!isOver())
		{	// confrontations
			Set<Integer> players = confrontations.get(matchCount);
			matchCount++;
			List<Profile> prof = new ArrayList<Profile>();
			for(Integer idx: players)
			{	Profile profile = profiles.get(idx);
				prof.add(profile);
			}
			
			// match
			Match match = matches.get(currentIndex);
			currentIndex++;
			currentMatch = match.copy();
			currentMatch.init(prof);
			playedMatches.add(currentMatch);
		}
	}

	@Override
	public void matchOver()
	{	// stats
		StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		
		// iterator
		if(currentIndex>=matches.size())
		{	if(randomizeMatches)
				randomizeMatches();
			currentIndex = 0;
		}
		
		// limits
		if(matchCount==confrontations.size()-1)
		{	float[] points;
			if(pointsProcessor!=null)
				points = pointsProcessor.process(this);
			else
				points = stats.getTotal();
			stats.setPoints(points);
			setOver(true);
			panel.tournamentOver();
			stats.initEndDate();
			
			// server connection
			ServerGeneralConnection serverConnection = Configuration.getConnectionsConfiguration().getServerConnection();
			if(serverConnection!=null)
				serverConnection.updateHostState(HostState.FINISHED);
		}
		else
		{	panel.matchOver();		
		}
	}
	
	@Override
	public void roundOver()
	{	panel.roundOver();
	}

	@Override
	public void finish()
	{	// points
//		pointsProcessor = null;
	}

	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Used to process points for this tournament */
	private PointsProcessor pointsProcessor;
		
	/**
	 * Returns the point processor of this tournament.
	 * 
	 * @return
	 * 		Point processor of this tournament.
	 */
	public PointsProcessor getPointsProcessor()
	{	return pointsProcessor;
	}

	/**
	 * Changes the point processor of this tournament.
	 * 
	 * @param pointsProcessor
	 * 		New point processor of this tournament.
	 */
	public void setPointsProcessor(PointsProcessor pointsProcessor)
	{	this.pointsProcessor = pointsProcessor;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the players should be used in a random order */ 
	private boolean randomizePlayers;
	
	/**
	 * Indicates if the player order
	 * should be random or not.
	 * 
	 * @return
	 * 		{@code true} iff players should be used in a random order.
	 */
	public boolean getRandomizePlayers()
	{	return randomizePlayers;
	}

	/**
	 * Changes the flag indicating if the player order
	 * should be random or not.
	 * 
	 * @param randomizePlayers
	 * 		If {@code true}, players should be used in a random order.
	 */
	public void setRandomizePlayers(boolean randomizePlayers)
	{	this.randomizePlayers = randomizePlayers;
	}

	/**
	 * Returns the allowed numbers of players
	 * for the matches of this tournament.
	 * 
	 * @return
	 * 		Set of allowed number of players.
	 */
	public Set<Integer> getMatchesAllowedPlayerNumbers()
	{	Set<Integer> result = new TreeSet<Integer>();
		
		for(int i=2;i<=GameData.MAX_PROFILES_COUNT;i++)
			result.add(i);
		for(Match m:matches)
		{	Set<Integer> temp = m.getAllowedPlayerNumbers();
			result.retainAll(temp);			
		}
		
		return result;			
	}

	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	Set<Integer> result = getMatchesAllowedPlayerNumbers();
		
		int min = Collections.min(result);
		min = Math.max(min,2);
		result = new TreeSet<Integer>();
		for(int i=min;i<=GameData.MAX_PROFILES_COUNT;i++)
			result.add(i);
		
		return result;			
	}

	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the ranks for this tournament.
	 * 
	 * @param pts
	 * 		Points scored by the players.
	 * @return
	 * 		Corresponding player ranks.
	 */
	private int[] getRanks(float[] pts)
	{	int[] result = new int[getProfiles().size()];
		for(int i=0;i<result.length;i++)
			result[i] = 1;

		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<result.length;j++)
			{	if(pts[i]<pts[j])
					result[i] = result[i] + 1;
				else if(pts[i]>pts[j])
					result[j] = result[j] + 1;
			}
		}	

		return result;
	}

	@Override
	public Ranks getOrderedPlayers()
	{	Ranks result = new Ranks();
		// points
		float[] points = stats.getPoints();
		float[] total = stats.getTotal();
		// ranks
		int ranks[];
		int ranks2[];
		if(isOver())
		{	ranks = getRanks(points);
			ranks2 = getRanks(total);
		}
		else
		{	ranks = getRanks(total);
			ranks2 = new int[ranks.length];
			Arrays.fill(ranks2,0);
		}
		// result
		for(int i=0;i<ranks.length;i++)
		{	int rank = ranks[i];
			int rank2 = ranks2[i];
			Profile profile = getProfiles().get(i);
			List<Profile> list = result.getProfilesFromRank(rank);
			int index = -1;
			// if no list yet : regular insertion
			if(list==null)
			{	result.addProfile(rank,profile);
				index = 0;
			}
			// if list : insert at right place considering total points
			else
			{	int j = 0;
				while(j<list.size() && index==-1)
				{	Profile profileB = list.get(j);
					int plrIdx = getProfiles().indexOf(profileB);
					int rank2B = ranks2[plrIdx];
					if(rank2<rank2B)
						index = j;
					else
						j++;
				}				
				if(index==-1)
					index = j;
				list.add(index,profile);
			}			
		}
			
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// MATCH ORDER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the matches should be played in a random order */ 
	private boolean randomizeMatches;

	/**
	 * Indicates if the match order
	 * should be random or not.
	 * 
	 * @return
	 * 		{@code true} iff matches should be played in a random order.
	 */
	public boolean getRandomizeMatches()
	{	return randomizeMatches;
	}
	
	/**
	 * Changes the flag indicating if the match order
	 * should be random or not.
	 * 
	 * @param randomizeMatches
	 * 		If {@code true}, matches should be played in a random order.
	 */
	public void setRandomizeMatches(boolean randomizeMatches)
	{	this.randomizeMatches = randomizeMatches;
	}

	/**
	 * Set matches in a random order.
	 */
	private void randomizeMatches()
	{	Collections.shuffle(matches);
	}

	/////////////////////////////////////////////////////////////////
	// MATCHES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Prototype matches for this tournament */ 
	private List<Match> matches = new ArrayList<Match>();
	/** Number of match played until now */
	private int matchCount;

	/**
	 * Initializes the matches of this tournament.
	 */
	private void initMatches()
	{	// matches
		if(randomizeMatches)
			randomizeMatches();
		currentIndex = 0;
		playedMatches.clear();
		
		// confrontations
		int n = profiles.size();
		List<Integer> ks = new ArrayList<Integer>(getMatchesAllowedPlayerNumbers());
		confrontations = null;
		// try to minimize the number of matches
		if(minimizeConfrontations)
		{	int matchNbr = Integer.MAX_VALUE;
			Map<Integer,List<Set<Integer>>> trnmt = confrontationMaps.get(n);
			for(Integer k: ks)
			{	List<Set<Integer>> matches = trnmt.get(k);
				int tempMatchNbr = matches.size();
				if(tempMatchNbr<matchNbr)
				{	matchNbr = tempMatchNbr;
					confrontations = matches;
				}
			}
		}
		// or choose all possible combinations (might be quite a long tournament !)
		else
		{	int matchNbr = Integer.MAX_VALUE;
			int combis[][] = null;
			for(Integer k: ks)
			{	int tempCombis[][] = CombinatoricsTools.getCombinations(k, n);
				int tempMatchNbr = tempCombis.length;
				if(tempMatchNbr<matchNbr)
				{	matchNbr = tempMatchNbr;
					combis = tempCombis;
				}
			}
			List<Set<Integer>> matches = new ArrayList<Set<Integer>>();
			for(int i=0;i<combis.length;i++)
			{	Set<Integer> match = new TreeSet<Integer>();
				for(int j=0;j<combis[i].length;j++)
					match.add(combis[i][j]);
				matches.add(match);
			}
		}
		if(confrontationOrder==ConfrontationOrder.RANDOM)
			randomizeConfrontations();
		else if(confrontationOrder==ConfrontationOrder.HOMOGENEOUS)
			homogenizeConfrontations();
		else if(confrontationOrder==ConfrontationOrder.HETEROGENEOUS)
			heterogenizeConfrontations();
		matchCount = 0;
	}

	/**
	 * Adds a match to this tournament.
	 * 
	 * @param match
	 * 		The additional match.
	 */
	public void addMatch(Match match)
	{	matches.add(match);
	}

	/////////////////////////////////////////////////////////////////
	// CONFRONTATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** How player confrontations affect match order */
	private ConfrontationOrder confrontationOrder;
	/** Whether we should try to minimize the number of times two players meet */
	private boolean minimizeConfrontations;
	/** List of confrontations */
	private List<Set<Integer>> confrontations;

	/**
	 * Returns the way confrontations
	 * affect match order.
	 * 
	 * @return
	 * 		Confrontation ordering type.
	 */
	public ConfrontationOrder getConfrontationOrder()
	{	return confrontationOrder;
	}
	
	/**
	 * Changes the way confrontations
	 * affect match order.
	 * 
	 * @param confrontationOrder
	 * 		New confrontation ordering type.
	 */
	public void setConfrontationOrder(ConfrontationOrder confrontationOrder)
	{	this.confrontationOrder = confrontationOrder;
	}

	/**
	 * Indicates whether we should try to minimize 
	 * the number of times two players meet.
	 * 
	 * @return
	 * 		{@code true} iff the minization flag is on.
	 */
	public boolean getMinimizeConfrontations()
	{	return minimizeConfrontations;
	}
	
	/**
	 * Changes the flag indicating whether we should try to minimize 
	 * the number of times two players meet.
	 * 
	 * @param minimizeConfrontations
	 * 		New minimization flag value.
	 */
	public void setMinimizeConfrontations(boolean minimizeConfrontations)
	{	this.minimizeConfrontations = minimizeConfrontations;
	}

	/**
	 * Makes confrontations randomly distributed
	 * (consecutive match may or may not involve
	 * the same players).
	 */
	private void randomizeConfrontations()
	{	Collections.shuffle(confrontations);
	}

	/**
	 * Makes confrontations distributed
	 * in an heterogeneous way (consecutive
	 * matches do not involve the same players).
	 */
	private void heterogenizeConfrontations()
	{	// TODO	not implemenented yet
	}
	
	/**
	 * Makes confrontations distributed
	 * in an homogeneous way (consecutive
	 * matches involve roughly the same players).
	 */
	private void homogenizeConfrontations()
	{	// TODO	not implemenented yet
	}
	
	/**
	 * Represents the order of the confrontations.
	 * 
	 * @author Vincent Labatut
	 */
	public enum ConfrontationOrder
	{	/** Keeps the order as defined by the designer */
		AS_IS,
		/** Randomizes the order */
		RANDOM,
		/** (Tries to) distribute confrontations homogeneously over matches */
		HOMOGENEOUS,
		/** (Tries to) distribute confrontations heterogeneously over matches */
		HETEROGENEOUS;
	}

	/////////////////////////////////////////////////////////////////
	// CONFRONTATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map of preprocessed confrontation (too long to process on demand) */
	private Map<Integer,Map<Integer,List<Set<Integer>>>> confrontationMaps = null;
	
	/**
	 * Loads the pre-processed confrontation map, only if needed.
	 * 
	 * @throws SAXException
	 * 		Problem while loading the confrontation maps. 
	 * @throws IOException
	 * 		Problem while loading the confrontation maps. 
	 */
	private void loadConfrontationMaps() throws SAXException, IOException
	{	if(confrontationMaps==null)
		{	// open files
			String individualFolder = FilePaths.getMiscPath();
			File dataFile = new File(individualFolder+File.separator+FileNames.FILE_COMBIS+FileNames.EXTENSION_XML);
			String schemaFolder = FilePaths.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_COMBIS+FileNames.EXTENSION_SCHEMA);
			
			// retrieve and process the XML content
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			loadCombisElement(root);
			
			// complete with missing settings, using all possible combinations
			for(int n=2;n<=GameData.MAX_PROFILES_COUNT;n++)
			{	Map<Integer, List<Set<Integer>>> trnmt = confrontationMaps.get(n);
				if(trnmt==null)
				{	trnmt = new HashMap<Integer, List<Set<Integer>>>();
					confrontationMaps.put(n, trnmt);
				}
				for(int k=2;k<=n;k++)
				{	List<Set<Integer>> matches = trnmt.get(k);
					if(matches==null)
					{	matches = new ArrayList<Set<Integer>>();
						trnmt.put(k, matches);
					}
					int combis[][] = CombinatoricsTools.getCombinations(k, n);
					for(int i=0;i<combis.length;i++)
					{	Set<Integer> match = new TreeSet<Integer>();
						for(int j=0;j<combis[i].length;j++)
							match.add(combis[i][j]);
						matches.add(match);
					}
				}
			}
		}
	}
	
	/**
	 * Processes the xml content of the confrontation maps file.
	 * 
	 * @param root
	 * 		Root of the XML document.
	 */
	@SuppressWarnings("unchecked")
	private void loadCombisElement(Element root)
	{	// process each element in the XML document
		List<Element> tournamentElts = root.getChildren(XmlNames.TOURNAMENT);
		for(Element tournamentElt: tournamentElts)
			loadTournamentElement(tournamentElt);
	}
	
	/**
	 * Processes each tournament element in the XML file.
	 * Each one corresponds to a fixed number of tournament players.
	 *   
	 * @param root
	 * 		Element of the tournament.
	 */
	@SuppressWarnings("unchecked")
	private void loadTournamentElement(Element root)
	{	// retrieve the player number
		String playersStr = root.getAttributeValue(XmlNames.PLAYERS);
		int players = Integer.parseInt(playersStr);
		Map<Integer,List<Set<Integer>>> result = new HashMap<Integer, List<Set<Integer>>>();
		confrontationMaps.put(players, result);
		
		// process each sub-element
		List<Element> matchesElts = root.getChildren(XmlNames.MATCHES);
		for(Element matchesElt: matchesElts)
			loadMatchesElement(matchesElt, result);
	}

	/**
	 * Processes each matches element in the XML file.
	 * Each one corresponds to a fixed number of match players.
	 *   
	 * @param root
	 * 		Element of the tournament.
	 * @param result
	 * 		Map to be completed during the process. 
	 */
	@SuppressWarnings("unchecked")
	private void loadMatchesElement(Element root, Map<Integer,List<Set<Integer>>> result)
	{	// retrieve the player number
		String playersStr = root.getAttributeValue(XmlNames.PLAYERS);
		int players = Integer.parseInt(playersStr);
		List<Set<Integer>> list = new ArrayList<Set<Integer>>();
		result.put(players, list);
		
		// process each sub-element
		List<Element> matchElts = root.getChildren(XmlNames.MATCH);
		for(Element matchElt: matchElts)
		{	String playerListStr = matchElt.getAttributeValue(XmlNames.PLAYERS);
			String temp[] = playerListStr.split(" ");
			Set<Integer> set = new TreeSet<Integer>();
			list.add(set);
			for(String tmp: temp)
			{	int player = Integer.parseInt(tmp);
				set.add(player);
			}
		}
	}
}
