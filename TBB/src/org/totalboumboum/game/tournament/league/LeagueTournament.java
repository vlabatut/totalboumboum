package org.totalboumboum.game.tournament.league;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.points.AbstractPointsProcessor;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticMatch;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.computing.CombinatoricsTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * This class represents a tournament in which
 * all players must play against each other at
 * some point, possibly several times. It is
 * based on the league model used in most sports.
 * <br/>
 * One or several matches can be associated to the tournament,
 * and the number of times the same match is played depends
 * on both the total number of players involved in the tournament
 * and the numbers of players the match can handle. Once all
 * players have meet each other on one match, the process is
 * performed again on the next match. This is repeated again
 * all matches have been played.
 * <br/>
 * Concerning the number of confrontations, i.e. the number of
 * times the same match is repeated, it can be either the total
 * number of possible combinations of k (players in one match)
 * amongst n (total number of players for the whole tournament).
 * Sometimes, it is possible to use less than all these combinations,
 * though. For example, if we consider n=6 and k=3 (6 players in
 * the tournament, 3 in each match), then the number of combinations
 * is 20, but we can have all players play against all others in
 * only 10 matches.
 * 
 * @author Vincent Labatut
 */
public class LeagueTournament extends AbstractTournament
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new league tournament object.
	 * Possible reads the repetition maps from
	 * a file, if needed.
	 * 
	 * @throws IOException
	 * 		Problem while loading the repetition maps. 
	 * @throws SAXException 
	 * 		Problem while loading the repetition maps. 
	 */
	public LeagueTournament() throws SAXException, IOException
	{	loadRepetitionMaps();
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME		/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
	
		// players
		if(randomizePlayers)
			Collections.shuffle(profiles);
	
		// matches and tbeir repetitions
		initMatches();
		
		// stats
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(!isOver())
		{	// get players for next repetition (effective match)
			List<Set<Integer>> seq = repetitions.get(currentIndex);
			Set<Integer> players = seq.get(confCount);
			List<Profile> prof = new ArrayList<Profile>();
			for(Integer idx: players)
			{	Profile profile = profiles.get(idx);
				prof.add(profile);
			}
			
			// set up next repetition (effective match)
			Match match = matches.get(currentIndex);
			currentMatch = match.copy();
			currentMatch.init(prof);
			playedMatches.add(currentMatch);
			
			// update match/conf counts
			confCount++;
			if(confCount==seq.size())
			{	confCount = 0;
				currentIndex++;
			}
		}
	}

// TODO la manière employée dans les panels pour la coupe est elle compatible ?
	
	@Override
	public void matchOver()
	{	// update stats
		StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		
		// check limits
		if(currentIndex>=matches.size())
		{	float[] points;
			if(pointsProcessor!=null)
				points = pointsProcessor.process(this);
			else
				points = stats.getTotal();
			stats.setPoints(points);
			setOver(true);
			panel.tournamentOver();
			stats.initEndDate();
			
			// possibly record stats as text file
			if(hasAi())
			{	AisConfiguration config = Configuration.getAisConfiguration();
				if(config.getRecordStats())
				try
				{	recordStatsAsText();
				}
				catch (FileNotFoundException e)
				{	e.printStackTrace();
				}
				catch (UnsupportedEncodingException e)
				{	e.printStackTrace();
				}
			}

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
	private AbstractPointsProcessor pointsProcessor;
		
	/**
	 * Returns the point processor of this tournament.
	 * 
	 * @return
	 * 		Point processor of this tournament.
	 */
	public AbstractPointsProcessor getPointsProcessor()
	{	return pointsProcessor;
	}

	/**
	 * Changes the point processor of this tournament.
	 * By default, we just sum the points scored over
	 * all matches, so some ties can appear in the end. 
	 * 
	 * @param pointsProcessor
	 * 		New point processor of this tournament.
	 */
	public void setPointsProcessor(AbstractPointsProcessor pointsProcessor)
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

//	/**
//	 * Returns the allowed numbers of players
//	 * for the matches of this tournament.
//	 * 
//	 * @return
//	 * 		Set of allowed number of players.
//	 */
//	public Set<Integer> getMatchesAllowedPlayerNumbers()
//	{	Set<Integer> result = new TreeSet<Integer>();
//		
//		for(int i=2;i<=GameData.MAX_PROFILES_COUNT;i++)
//			result.add(i);
//		for(Match m:matches)
//		{	Set<Integer> temp = m.getAllowedPlayerNumbers();
//			result.retainAll(temp);			
//		}
//		
//		return result;			
//	}

	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	// we use the maximal value of the
		// minimal allowed numbers over matches
		int max = 0;
		for(Match match: matches)
		{	Set<Integer> temp = match.getAllowedPlayerNumbers();
			int min = Collections.min(temp);
			max = Math.max(max,min);
		}
		
		// anything larger is fine, since it can be broken down into smaller numbers
		Set<Integer> result = new TreeSet<Integer>();
		for(int i=max;i<=GameData.MAX_PROFILES_COUNT;i++)
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
	// STATS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void recordStatsAsText() throws FileNotFoundException, UnsupportedEncodingException
	{	// get data
		Ranks orderedPlayers = getOrderedPlayers();
		List<Profile> absoluteList = orderedPlayers.getAbsoluteOrderList();
		float points[] = stats.getPoints();
			
		// get file name
		String fileBase = stats.getFilePath();
		int confNbr = playedMatches.size();
		String filePath = fileBase + "." + FileNames.FILE_TOURNAMENT + FileNames.EXTENSION_TEXT;
			
		// open text stream
		FileOutputStream fileOut = new FileOutputStream(filePath);
		BufferedOutputStream outBuff = new BufferedOutputStream(fileOut);
		OutputStreamWriter outSW = new OutputStreamWriter(outBuff, "UTF-8");
		PrintWriter writer = new PrintWriter(outSW);
			
		// write general info
		writer.println("Tournament: "+getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); 
		Date startDate = stats.getStartDate();
		writer.println("Start: "+sdf.format(startDate));
		Date endDate = stats.getEndDate();
		writer.println("End: "+sdf.format(endDate));
		long duration = endDate.getTime() - startDate.getTime();
		String durationStr = TimeTools.formatTime(duration, TimeUnit.MINUTE, TimeUnit.MILLISECOND, false);
		writer.println("Duration: "+durationStr);
		writer.println();

		// write headers
		writer.print("Rank\t");
		writer.print("Name\t");
		writer.print("Color\t");
//			writer.print("Id\t");
		writer.print("Bombs\t");
		writer.print("Items\t");
		writer.print("Bombeds\t");
		writer.print("Selfies\t");
		writer.print("Bombings\t");
		writer.print("Played\t");
		writer.print("Lost\t");
		writer.print("Drawn\t");
		writer.print("Won\t");
		writer.print("Points\t");
		for(int i=0;i<confNbr;i++)
			writer.print("M"+(i+1)+"\t");
		writer.println();

		// write data
		for(int i=0;i<points.length;i++)
		{	// set profile stuff
			Profile profile = absoluteList.get(i);
			int profileIndex = profiles.indexOf(profile);

			// rank
			{	int rank = orderedPlayers.getRankForProfile(profile);
				writer.print(rank+".\t");
			}
			
			// name
			{	String name = profile.getName();
				writer.print(name+"\t");
			}
			
			// color
			{	PredefinedColor color = profile.getSpriteColor();
				writer.print(color+"\t");
			}
			
			// id
//				{	String id = playersIds.get(profileIndex);
//					writer.print(name+"\t");
//				}
			
			// bombs dropped
			{	long scores[] = stats.getScores(Score.BOMBS);
				long bombs = scores[profileIndex];
				writer.print(bombs+"\t");
			}
			
			// items pîcked
			{	long scores[] = stats.getScores(Score.ITEMS);
				long items = scores[profileIndex];
				writer.print(items+"\t");
			}
			
			// times bombed
			{	long scores[] = stats.getScores(Score.BOMBEDS);
				long bombeds = scores[profileIndex];
				writer.print(bombeds+"\t");
			}
			
			// self-bombings
			{	long scores[] = stats.getScores(Score.SELF_BOMBINGS);
				long selfies = scores[profileIndex];
				writer.print(selfies+"\t");
			}
			
			// players bombed
			{	long scores[] = stats.getScores(Score.BOMBINGS);
				long bombings = scores[profileIndex];
				writer.print(bombings+"\t");
			}
			
			// played
			{	int played = stats.getPlayed()[profileIndex];
				writer.print(played+"\t");
			}
			
			// lost
			{	int lost = stats.getLost()[profileIndex];
				writer.print(lost+"\t");
			}
			
			// drawn
			{	int drawn = stats.getDrawn()[profileIndex];
				writer.print(drawn+"\t");
			}
			
			// won
			{	int won = stats.getWon()[profileIndex];
				writer.print(won+"\t");
			}
			
			// points
			{	float total[] = stats.getTotal();
				float pts = total[profileIndex];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String ptsStr = nf.format(pts);
				writer.print(ptsStr+"\t");
			}
			
			// confrontations
			{	List<StatisticBase> statMatches = stats.getConfrontationStats();
				for(StatisticBase statMatch: statMatches)
				{	float pts = statMatch.getPoints()[profileIndex];
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(0);
					String ptsStr = nf.format(pts);
					writer.print(ptsStr+"\t");
				}
			}
			
			writer.println();
		}
		
		// close stream
		writer.close();
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
	/** Number of times the current match was played (with different players) */
	private int confCount;

	/**
	 * Initializes the matches of this tournament.
	 * It also processes the repetitions of these matches and their order.
	 */
	private void initMatches()
	{	// resets matches
		if(randomizeMatches)
			randomizeMatches();
		currentIndex = 0;
		playedMatches.clear();
		
		// repetitions
		repetitions = processRepetitions();
		
		// repetitions order
		if(repetitionOrder==RepetitionOrder.RANDOM)
			randomizeRepetitions();
		else if(repetitionOrder==RepetitionOrder.HOMOGENEOUS)
			homogenizeRepetitions();
		else if(repetitionOrder==RepetitionOrder.HETEROGENEOUS)
			heterogenizeRepetitions();
		
		confCount = 0;
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

	@Override
	public Integer getTotalMatchNumber()
	{	List<List<Set<Integer>>> rep = repetitions;
		if(rep == null)
			rep = processRepetitions();
		int result = 0;
		for(List<Set<Integer>> m: rep)
		{	int tmp = m.size();
			result = result + tmp;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// REPETITIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** How match repetitions should be ordered */
	private RepetitionOrder repetitionOrder;
	/** Whether we should try to minimize the number of times two players meet */
	private boolean minimizeRepetitions;
	/** List of repetitions (which players play each repetition of a match) */
	private List<List<Set<Integer>>> repetitions;
	
	/**
	 * Processes the repetition sequences for the whole tournament.
	 * 
	 * @return
	 * 		List of repetition sequences.
	 */
	private List<List<Set<Integer>>> processRepetitions()
	{	List<List<Set<Integer>>> result = new ArrayList<List<Set<Integer>>>();
		int n = profiles.size();
		for(Match match: matches)
		{	Set<Integer> ks = match.getAllowedPlayerNumbers();
			Iterator<Integer> it = ks.iterator();
			while(it.hasNext())
			{	int k = it.next();
				if(k>n)
					it.remove();
			}
			// try to minimize the number of matches
			if(minimizeRepetitions)
			{	List<Set<Integer>> temp = minimizeRepetitions(n,ks);
				result.add(temp);
			}
			
			// or choose all possible combinations (might be quite a long tournament !)
			else
			{	List<Set<Integer>> temp = combiRepetitions(n,ks);
				result.add(temp);
			}
		}
		return result;
	}
	
	/**
	 * Returns the way repetitions are ordered.
	 * 
	 * @return
	 * 		Repetition ordering type.
	 */
	public RepetitionOrder getRepetitionOrder()
	{	return repetitionOrder;
	}
	
	/**
	 * Changes the way repetitions are ordered.
	 * 
	 * @param repetitionOrder
	 * 		New repetition ordering type.
	 */
	public void setRepetitionOrder(RepetitionOrder repetitionOrder)
	{	this.repetitionOrder = repetitionOrder;
	}

	/**
	 * Indicates whether we should try to minimize 
	 * the number of times two players meet.
	 * 
	 * @return
	 * 		{@code true} iff the minization flag is on.
	 */
	public boolean getMinimizeRepetitions()
	{	return minimizeRepetitions;
	}
	
	/**
	 * Changes the flag indicating whether we should try to minimize 
	 * the number of times two players meet.
	 * 
	 * @param minimizeRepetitions
	 * 		New minimization flag value.
	 */
	public void setMinimizeRepetitions(boolean minimizeRepetitions)
	{	this.minimizeRepetitions = minimizeRepetitions;
	}
	
	/**
	 * Processes the repetition sequence of a given match,
	 * such that the number of repetitions (i.e. match played)
	 * is minimal. In the worst case, this corresponds to what
	 * is returned by method {@link #combiRepetitions(int, Set)}.
	 * 
	 * @param n
	 * 		Number of players in the tournament.
	 * @param ks
	 * 		Allowed numbers of players for the considered match.
	 * @return
	 * 		Repetition sequence with the minimal number of repetitions.
	 */
	private List<Set<Integer>> minimizeRepetitions(int n, Set<Integer> ks)
	{	List<Set<Integer>> result = new ArrayList<Set<Integer>>();
		int matchNbr = Integer.MAX_VALUE;
		Map<Integer,List<Set<Integer>>> trnmt = repetitionMaps.get(n);
		for(Integer k: ks)
		{	List<Set<Integer>> matches = trnmt.get(k);
			int tempMatchNbr = matches.size();
			if(tempMatchNbr<matchNbr)
			{	matchNbr = tempMatchNbr;
				result = matches;
			}
		}
		return result;
	}
	
	/**
	 * Returns the repetition sequence for all possible combinations
	 * of k players amongst n.
	 * 
	 * @param n
	 * 		Total number of players in the tournament.
	 * @param k
	 * 		Number of players in the match.
	 * @return
	 * 		Corresponding sequence of repetitions.
	 */
	private List<Set<Integer>> combiRepetitions(int n, int k)
	{	// retrieval all the combinations
		int combis[][] = CombinatoricsTools.getCombinations(k, n);
		
		// put them under the appropriate form
		List<Set<Integer>> result = new ArrayList<Set<Integer>>();
		for(int i=0;i<combis.length;i++)
		{	Set<Integer> match = new TreeSet<Integer>();
			for(int j=0;j<combis[i].length;j++)
				match.add(combis[i][j]);
			result.add(match);
		}
		
		return result;
	}
	
	/**
	 * Returns the repetition sequence for all possible combinations.
	 * We consider every k in ks and return the sequence containing
	 * the smallest number of repetitions.
	 * 
	 * @param n
	 * 		Total number of players in the tournament.
	 * @param ks
	 * 		Allowed numbers of players for the match of interest.
	 * @return
	 * 		Smallest sequence of repetitions.
	 */
	private List<Set<Integer>> combiRepetitions(int n, Set<Integer> ks)
	{	List<Set<Integer>> result = new ArrayList<Set<Integer>>();
		// we check each possible k and keep that with the smallest number of repetitions
		int matchNbr = Integer.MAX_VALUE;
		for(Integer k: ks)
		{	List<Set<Integer>> temp = combiRepetitions(n, k);
			if(temp.size()<matchNbr)
			{	matchNbr = temp.size();
				result = temp;
			}
		}
		return result;
	}
	
	/**
	 * Makes repetitions randomly distributed
	 * (consecutive match may or may not involve
	 * the same players).
	 */
	private void randomizeRepetitions()
	{	Collections.shuffle(repetitions);
	}

	/**
	 * Makes repetitions distributed
	 * in an heterogeneous way (consecutive
	 * matches do not involve the same players).
	 */
	private void heterogenizeRepetitions()
	{	// TODO	not implemenented yet
	}
	
	/**
	 * Makes repetitions distributed
	 * in an homogeneous way (consecutive
	 * matches involve roughly the same players).
	 */
	private void homogenizeRepetitions()
	{	// TODO	not implemenented yet
	}
	
	/**
	 * Represents the order of the repetitions.
	 * 
	 * @author Vincent Labatut
	 */
	public enum RepetitionOrder
	{	/** Keeps the order as defined by the designer */
		UNCHANGED,
		/** Randomizes the order */
		RANDOM,
		/** (Tries to) distribute repetitions homogeneously over matches */
		HOMOGENEOUS,
		/** (Tries to) distribute repetitions heterogeneously over matches */
		HETEROGENEOUS;
	}

	/////////////////////////////////////////////////////////////////
	// REPETITION MAP	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map of preprocessed repetitions (takes too long to process on demand) */
	private Map<Integer,Map<Integer,List<Set<Integer>>>> repetitionMaps = null;
	
	/**
	 * Loads the pre-processed repetition map, only if needed.
	 * 
	 * @throws SAXException
	 * 		Problem while loading the repetition maps. 
	 * @throws IOException
	 * 		Problem while loading the repetition maps. 
	 */
	private void loadRepetitionMaps() throws SAXException, IOException
	{	if(repetitionMaps==null)
		{	repetitionMaps = new HashMap<Integer, Map<Integer,List<Set<Integer>>>>();
			
			// open files
			String individualFolder = FilePaths.getMiscPath();
			File dataFile = new File(individualFolder+File.separator+FileNames.FILE_COMBIS+FileNames.EXTENSION_XML);
			String schemaFolder = FilePaths.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_COMBIS+FileNames.EXTENSION_SCHEMA);
			
			// retrieve and process the XML content
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			loadCombisElement(root);
			
			// complete with missing settings, using all possible combinations
			for(int n=2;n<=GameData.MAX_PROFILES_COUNT;n++)
			{	Map<Integer, List<Set<Integer>>> trnmt = repetitionMaps.get(n);
				if(trnmt==null)
				{	trnmt = new HashMap<Integer, List<Set<Integer>>>();
					repetitionMaps.put(n, trnmt);
				}
				for(int k=2;k<=n;k++)
				{	List<Set<Integer>> matches = trnmt.get(k);
					if(matches==null)
					{	matches = combiRepetitions(n, k);
						trnmt.put(k, matches);
					}
				}
			}
		}
	}
	
	/**
	 * Processes the XML content of the repetition maps file.
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
		repetitionMaps.put(players, result);
		
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
