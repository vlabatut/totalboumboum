package org.totalboumboum.statistics.overall;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * Represents the statistics of a player
 * not already handled by the Glicko-2 system.
 * Especially scores.
 * 
 * @author Vincent Labatut
 */
public class PlayerStats implements Serializable
{	/** Serial number */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new, empty stats object for
	 * the specified player.
	 * 
	 * @param playerID
	 * 		Id of the concerned player.
	 */
	public PlayerStats(String playerID)
	{	this.playerId = playerID;
		reset();
	}
	
	/**
	 * Reinitializes the stats of this player.
	 */
	public void reset()
	{	// scores
		for(Score score: Score.values())
			setScore(score,0);
		previousRank = -1;
		roundsPlayed = 0;
		roundsWon = 0;
		roundsDrawn = 0;
		roundsLost = 0;
		selectedColor = null;
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYER ID		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Id of the concerned player */
	private String playerId;

	/**
	 * Returns the id of the concerned player.
	 * 
	 * @return
	 * 		Id of the concerned player.
	 */
	public String getPlayerId()
	{	return playerId;
	}
	
	/**
	 * Changes the id of the concerned player.
	 * 
	 * @param id
	 * 		New id for the concerned player.
	 */
	public void setPlayerId(String id)
	{	playerId = id;
	}

	/////////////////////////////////////////////////////////////////
	// PREVIOUS RANK	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Rank of the player at the last Glicko-2 update */
	private int previousRank = -1;

	/**
	 * Returns the rank of the player at the last Glicko-2 update.
	 * 
	 * @return
	 * 		Previous rank of the player.
	 */
	public int getPreviousRank()
	{	return previousRank;
	}

	/**
	 * Changes the rank of the player at the last Glicko-2 update.
	 * 
	 * @param previousRank
	 * 		New previous rank.
	 */
	public void setPreviousRank(int previousRank)
	{	this.previousRank = previousRank;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS PLAYED	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of rounds played by the player until now */
	private long roundsPlayed = 0;

	/**
	 * Returns the number of rounds played by the player until now.
	 * 
	 * @return
	 * 		Number of rounds played.
	 */
	public long getRoundsPlayed()
	{	return roundsPlayed;
	}

	/**
	 * Changes the number of rounds played by the player until now
	 * 
	 * @param roundsPlayed
	 * 		New number of rounds played.
	 */
	public void setRoundsPlayed(long roundsPlayed)
	{	this.roundsPlayed = roundsPlayed;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS WON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of rounds won by the player until now */
	private long roundsWon = 0;

	/**
	 * Returns the number of rounds won by the player until now.
	 * 
	 * @return
	 * 		Number of rounds won.
	 */
	public long getRoundsWon()
	{	return roundsWon;
	}

	/**
	 * Changes the number of rounds won by the player until now
	 * 
	 * @param roundsWon
	 * 		New number of rounds won.
	 */
	public void setRoundsWon(long roundsWon)
	{	this.roundsWon = roundsWon;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS DRAWN		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of rounds drawn by the player until now */
	private long roundsDrawn = 0;

	/**
	 * Returns the number of rounds drawn by the player until now.
	 * 
	 * @return
	 * 		Number of rounds drawn.
	 */
	public long getRoundsDrawn()
	{	return roundsDrawn;
	}

	/**
	 * Changes the number of rounds drawn by the player until now
	 * 
	 * @param roundsDrawn
	 * 		New number of rounds drawn.
	 */
	public void setRoundsDrawn(long roundsDrawn)
	{	this.roundsDrawn = roundsDrawn;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS LOST		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of rounds lost by the player until now */
	private long roundsLost = 0;

	/**
	 * Returns the number of rounds lost by the player until now.
	 * 
	 * @return
	 * 		Number of rounds lost.
	 */
	public long getRoundsLost()
	{	return roundsLost;
	}

	/**
	 * Changes the number of rounds lost by the player until now
	 * 
	 * @param roundsLost
	 * 		New number of rounds lost.
	 */
	public void setRoundsLost(long roundsLost)
	{	this.roundsLost = roundsLost;
	}

	/////////////////////////////////////////////////////////////////
	// SCORES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current total scores of the player */
	private final HashMap<Score,Long> scores = new HashMap<Score, Long>();

	/**
	 * Returns the current total score of the player.
	 * 
	 * @param score
	 * 		Score of interest.
	 * @return
	 * 		Total value of the score.
	 */
	public long getScore(Score score)
	{	if(scores.get(score)==null)
			scores.put(score,0l);
		return scores.get(score);
	}

	/**
	 * Changes the current total score of the player.
	 * 
	 * @param score
	 * 		Score to be changed.
	 * @param value
	 * 		New total value of the score.
	 */
	public void setScore(Score score, long value)
	{	scores.put(score,value);
	}
	
	/////////////////////////////////////////////////////////////////
	// SELECTED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates the color used to display this player in plots, or {@code null} if the player is not selected */
	private PredefinedColor selectedColor = null;

	/**
	 * Checks whether this player is selected
	 * for plotting.
	 * 
	 * @return
	 * 		{@code true} iff the player is selected.
	 */
	public boolean isSelected()
	{	boolean result = selectedColor!=null;
		return result;
	}
	
	/**
	 * Changes the color associated to this
	 * player of stats plotting, or {@code null}
	 * if the player is not selected.
	 * 
	 * @param color
	 * 		The player color, of {@code null} if
	 * 		not selected.
	 */
	public void setSelectedColor(PredefinedColor color)
	{	selectedColor = color;
	}
	
	/**
	 * Changes the color associated to this player
	 * for stats plotting.
	 * 
	 * @return
	 * 		New color associated to the player.
	 */
	public PredefinedColor getSelectedColor()
	{	return selectedColor;
	}

	/////////////////////////////////////////////////////////////////
	// HISTORY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** All the values recorded for the player */
	private transient Map<Value,List<Float>> historyValues = null;
	/** Time stamps corresponding to each recorded round */
	private transient List<Date> historyDates = new ArrayList<Date>();
	
	/**
	 * This type represents the various types of values
	 * recorded as history for each player.
	 * 
	 * @author Vincent Labatut
	 */
	public enum Value
	{	/** Glicko-2 Rank */
		RANK,
		/** Glicko-2 mean score */
		MEAN,
		/** Glicko-2 standard deviation score */
		STDEV,
		
		/** Number of bombs dropped */
		BOMBS, 
		/** Number of crowns picked up */
		CROWNS, 
		/** Number of times bombed by other players */
		BOMBEDS,
		/** Number of items picked up */
		ITEMS, 
		/** Number of other players bombed */
		BOMBINGS,
		/** Number of tiles painted */
		PAINTINGS, 
		/** Number of times the player bombed himself */
		SELF_BOMBINGS, 
		
		/** Total number of rounds played */
		CONFR_TOTAL,
		/** Total number of rounds won */
		CONFR_WON,
		/** Total number of rounds drawn */
		CONFR_DRAW,
		/** Total number of rounds lost */
		CONFR_LOST,
		/** Total time played */
		TIME;
	}
	
	/**
	 * Returns the series of values
	 * corresponding to the specified type of value.
	 * 
	 * @param value
	 * 		Type of the desired values.
	 * @return
	 * 		List of the corresponding values.
	 */
	public List<Float> getHistoryValues(Value value)
	{	if(historyValues==null)
		{	// init
			historyDates = new ArrayList<Date>();
			historyValues = new HashMap<PlayerStats.Value, List<Float>>();
			for(Value name: Value.values())
			{	List<Float> list = new ArrayList<Float>();
				historyValues.put(name, list);
			}
			
			// fill
			try
			{	loadHistory();
			}
			catch (FileNotFoundException e)
			{	e.printStackTrace();
			}
			catch (ParseException e)
			{	e.printStackTrace();
			}
		}
		
		List<Float> result = historyValues.get(value);
		return result;
	}
	
	/**
	 * Returns the sequence of date corresponding
	 * to the values constituting the history of
	 * the player.
	 * 
	 * @return
	 * 		A list of dates.
	 */
	public List<Date> getHistoryDate()
	{	return historyDates;
	}
	
	/**
	 * Loads the complete history of values
	 * for this player, from a text file.
	 * If the history is empty, it is initialized
	 * with current stats.
	 * 
	 * @throws FileNotFoundException
	 * 		Problem while reading the history file. 
	 * @throws ParseException 
	 * 		Problem with the format of dates in the history file.
	 */
	private void loadHistory() throws FileNotFoundException, ParseException
	{	String historyFolder = FilePaths.getDetailedStatisticsPath();
		File dataFile = new File(historyFolder+File.separator+playerId+FileNames.EXTENSION_TEXT);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        
		// check for history existence
		if(dataFile.exists())
		{	// load history
			FileInputStream fileIn = new FileInputStream(dataFile);
			InputStreamReader reader = new InputStreamReader(fileIn);
			Scanner scanner = new Scanner(reader);
			scanner.useLocale(Locale.ENGLISH);
			while(scanner.hasNext())
			{	// round date
				String dateStr = scanner.next();
				Date date = formatter.parse(dateStr);
				historyDates.add(date);
				
				// round values
				for(Value name: Value.values())
				{	float value = scanner.nextFloat();
					List<Float> list = historyValues.get(name);
					list.add(value);
				}
			}
			scanner.close();
		}
	}
	
	/**
	 * Adds the specified values to the
	 * history of this player (in the dedicated file).
	 * If the history is empty, it is initialized
	 * with current stats.
	 *  
	 * @param date
	 * 		Date of the new round to be appended.
	 * @param values
	 * 		Values describing the round.
	 * 
	 * @throws FileNotFoundException 
	 * 		Problem while appending the history file. 
	 */
	public void appendToHistory(Date date, Map<Value,Float> values) throws FileNotFoundException
	{	recordHistory(date,values);
			
		// possibly update in-memory values (if already loaded)
		if(historyValues!=null)
		{	historyDates.add(date);
			for(Value name: Value.values())
			{	Float value = values.get(name);
				List<Float> list = historyValues.get(name);
				list.add(value);
			}
		}
	}
	
	/**
	 * Records the specified values
	 * at the end of the player history.
	 * 
	 * @param date
	 * 		Date of the new round to be appended.
	 * @param values
	 * 		Values describing the round.
	 * 
	 * @throws FileNotFoundException 
	 * 		Problem while appending the history file. 
	 */
	private void recordHistory(Date date, Map<Value,Float> values) throws FileNotFoundException
	{	
		// open file
		String historyFolder = FilePaths.getDetailedStatisticsPath();
		File dataFile = new File(historyFolder+File.separator+playerId+FileNames.EXTENSION_TEXT);
		FileOutputStream fileOut = new FileOutputStream(dataFile,true);
		OutputStreamWriter writer = new OutputStreamWriter(fileOut);
		PrintWriter printWriter = new PrintWriter(writer);
		
		// append date
    	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    	String line = dateFormatter.format(date);

    	// append values
		NumberFormat floatFormatter = NumberFormat.getInstance(Locale.ENGLISH);
		for(Value name: Value.values())
    	{	// record
    		Float value = values.get(name);
    		if(value==null)
    			value = 0f;
   			if(name==Value.TIME)	// don't need the ms precision for time
   				value = (float)Math.round(value/1000);
    		line = line + "\t" + floatFormatter.format(value);
    	}
		printWriter.println(line);
		
		// close file
		printWriter.close();
	}
	
//	/**
//	 * Initializes history using current
//	 * stats. This is used when the player
//	 * exists before the history system
//	 * is put in place
//	 * 
//	 * @throws FileNotFoundException 
//	 * 		Problem while recording the initialized values.
//	 */
//	private void initHistory() throws FileNotFoundException
//	{	// init date
//		Calendar cal = Calendar.getInstance();
//	   	Date date = cal.getTime();
//	   	Map<Value,Float> values = new HashMap<PlayerStats.Value, Float>();
//	   	
//	   	// init score values
//   		for(Score score: Score.values())
//   		{	Value name = score.getValue();
//   			float value = scores.get(score);
//   			if(score==Score.TIME)	// don't need the ms precision for time
//   				value = Math.round(value/1000);
//   			values.put(name,value);
//   		}
//   		
//   		// init confrontation values
//		values.put(Value.CONFR_TOTAL,(float)roundsPlayed);
//   		values.put(Value.CONFR_WON,(float)roundsWon);
//   		values.put(Value.CONFR_DRAW,(float)roundsDrawn);
//   		values.put(Value.CONFR_LOST,(float)roundsLost);
//		
//   		// init Glicko-2 values
//   		RankingService rankingService = GameStatistics.getRankingService();
//		PlayerRating rating = rankingService.getPlayerRating(playerId);
//		if(rating!=null)
//		{	float rank = rankingService.getPlayerRank(playerId);
//			values.put(Value.RANK,rank);
//			values.put(Value.MEAN,(float)rating.getRating());
//			values.put(Value.STDEV,(float)rating.getRatingDeviation());
//		}
//   		
//   		// record
//   		recordHistory(date, values);
//	}
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "";
		
		// misc
		result = result+" prevrk: "+previousRank;
		
		// rounds
		result = result+" played: "+roundsPlayed;
		result = result+" won: "+roundsWon;
		result = result+" drawn: "+roundsDrawn;
		result = result+" lost: "+roundsLost;
		
		// scores
		for(Score score: Score.values())
		{	String text = Long.toString(scores.get(score));
			if(score==Score.TIME)
				text = TimeTools.formatTime(scores.get(score),TimeUnit.HOUR,TimeUnit.MILLISECOND,false);
			result = result+" "+score.toString()+": "+text;
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT IMPORT/EXPORT	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Records the overall stats of the player
	 * under the form of a text file.
	 * 
	 * @param writer
	 * 		Object used to write in the text file.
	 */
	public void exportToText(PrintWriter writer)
	{	// misc
		writer.print(playerId);
		writer.print(";"+previousRank);
		
		// rounds
		writer.print(";"+roundsPlayed);
		writer.print(";"+roundsWon);
		writer.print(";"+roundsDrawn);
		writer.print(";"+roundsLost);
		
		// scores
		for(Score score: Score.values())
			writer.print(";"+scores.get(score));
		
		// selection
		writer.print(";"+selectedColor);
		
		writer.println();
	}

	/**
	 * Loads the overall stats of the player
	 * from a text file.
	 * 
	 * @param scanner
	 * 		Object used to read the text file.
	 */
	public void importFromText(Scanner scanner)
	{	String text = scanner.nextLine();
		String texts[] = text.split(";");
		int t = 0;

		// misc
		playerId = texts[t++];
		previousRank = Integer.parseInt(texts[t++]);
		
		// rounds
		roundsPlayed = Long.parseLong(texts[t++]);
		roundsWon = Long.parseLong(texts[t++]);
		roundsDrawn = Long.parseLong(texts[t++]);
		roundsLost = Long.parseLong(texts[t++]);
		
		// scores
		for(Score score: Score.values())
		{	long value = Long.parseLong(texts[t++]);
			scores.put(score,value);
		}
		
		// selection
		selectedColor = PredefinedColor.valueOf(texts[t++]);
	}
}
