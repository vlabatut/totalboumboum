package org.totalboumboum.configuration.ai;

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.files.FileTools;

/**
 * This class handles all options regarding
 * the artificial intelligence aspects.
 * 
 * @author Vincent Labatut
 */
public class AisConfiguration
{
	/**
	 * Copy the current configuration,
	 * to be able to restore it later.
	 * 
	 * @return
	 * 		A copy of this object.
	 */
	public AisConfiguration copy()
	{	AisConfiguration result = new AisConfiguration();

		result.setAiUps(aiUps);
		
		result.setAutoAdvance(autoAdvance);
		result.setAutoAdvanceDelay(autoAdvanceDelay);
		result.setTournamentAutoAdvanceMode(tournamentAutoAdvanceMode);
		result.setTournamentAutoAdvancePack(tournamentAutoAdvancePack);
		
		result.setHideAllAis(hideAllAis);
		result.setBombUselessAis(bombUselessAis);
		
		result.setDisplayExceptions(displayExceptions);

		result.setLogExceptions(logExceptions);
		result.setLogExceptionsSeparately(logExceptionsSeparately);

		result.setRecordStats(recordStats);
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TIMING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** AI updates per second */
	private int aiUps = 50;
	/** AI update period */
	private long aiPeriod = (long)(1000.0/aiUps);
	/** AI yield period (experimental) */
	private long aiYieldPeriod = (long)50;

	/**
	 * Returns the number of AI updates per second.
	 * 
	 * @return
	 * 		AI updates per second
	 */
	public int getAiUps()
	{	return aiUps;
	}
	
	/**
	 * Changes the number of AI updates per second.
	 * 
	 * @param aiUps
	 * 		New number of AI updates per second.
	 */
	public void setAiUps(int aiUps)
	{	this.aiUps = aiUps;
		aiPeriod = (long) (1000.0/aiUps);
	}
	
	/***
	 * Returns the AI update period.
	 * 
	 * @return
	 * 		AI update period.
	 */
	public long getAiPeriod()
	{	return aiPeriod;
	}

	/**
	 * Returns the AI yield period (experimental).
	 * 
	 * @return
	 * 		AI yield period.
	 */
	public long getAiYieldPeriod()
	{	return aiYieldPeriod;
	}

	/////////////////////////////////////////////////////////////////
	// AUTO ADVANCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** During a tournament/match, automatically advances to the next match/round */
	private AutoAdvance autoAdvance = AutoAdvance.NONE;
	/** Delay (in ms) before the auto system advances to the next round */
	private long autoAdvanceDelay = 1000;
	/** Mode of the tournament auto-advance */
	private TournamentAutoAdvance tournamentAutoAdvanceMode = TournamentAutoAdvance.RANKS;
	/** Pack selected for the "pack" type of tournament auto-advance */
	private String tournamentAutoAdvancePack = "v200708";
	
	/**
	 * Represents the various possible
	 * values for the auto-advance option.
	 * 
	 * @author Vincent Labatut
	 */
	public enum AutoAdvance
	{	/** No auto-advance at all */
		NONE,
		/** Simply advance through the tournament */
		SIMPLE,
		/** Like simple, and additionally select players and repeat the tournament automatically */
		TOURNAMENT;
		
		/**
		 * Cycle through the various values
		 * of this enum type.
		 * 
		 * @return
		 * 		The next value.
		 */
		public AutoAdvance getNext()
		{	int index = (this.ordinal() + 1) % AutoAdvance.values().length;
			AutoAdvance result = AutoAdvance.values()[index];
			return result;
		}
		
		/**
		 * Cycle through the various values
		 * of this enum type.
		 * 
		 * @return
		 * 		The previous value.
		 */
		public AutoAdvance getPrevious()
		{	int index = (this.ordinal() - 1 + AutoAdvance.values().length) % AutoAdvance.values().length;
			AutoAdvance result = AutoAdvance.values()[index];
			return result;
		}
	}
	
	/**
	 * Represents the various possible
	 * values for the tournamanet auto-
	 * mode advance option.
	 * 
	 * @author Vincent Labatut
	 */
	public enum TournamentAutoAdvance
	{	/** Priority to players with less confrontations */
		CONFRONTATIONS,
		/** Keeps on using the currently selected players */
		CONSTANT,
		/** Priority to players of a specified pack */
		PACK,
		/** Players are picked randomly */
		RANDOM,
		/** Players of similar rank are iteratively selected */
		RANKS;
		
		/**
		 * Cycle through the various values
		 * of this enum type.
		 * 
		 * @return
		 * 		The next value.
		 */
		public TournamentAutoAdvance getNext()
		{	int index = (this.ordinal() + 1) % TournamentAutoAdvance.values().length;
			TournamentAutoAdvance result = TournamentAutoAdvance.values()[index];
			return result;
		}
		
		/**
		 * Cycle through the various values
		 * of this enum type.
		 * 
		 * @return
		 * 		The previous value.
		 */
		public TournamentAutoAdvance getPrevious()
		{	int index = (this.ordinal() - 1 + TournamentAutoAdvance.values().length) % TournamentAutoAdvance.values().length;
			TournamentAutoAdvance result = TournamentAutoAdvance.values()[index];
			return result;
		}
	}

	/**
	 * Returns the delay used for auto-advance.
	 * 
	 * @return
	 * 		Auto-advance delay in ms.
	 */
	public long getAutoAdvanceDelay()
	{	return autoAdvanceDelay;
	}

	/**
	 * Changes the delay used for auto-advance.
	 * 
	 * @param autoAdvanceDelay
	 * 		New auto-advance delay in ms.
	 */
	public void setAutoAdvanceDelay(long autoAdvanceDelay)
	{	this.autoAdvanceDelay = autoAdvanceDelay;
	}

	/**
	 * Returns the current auto-advance option.
	 * 
	 * @return
	 * 		Auto advance option (an enum value).
	 */
	public AutoAdvance getAutoAdvance()
	{	return autoAdvance;
	}

	/**
	 * Changes the current auto-advance option.
	 * 
	 * @param autoAdvance
	 * 		New auto-advance option (an enum value).
	 */
	public void setAutoAdvance(AutoAdvance autoAdvance)
	{	this.autoAdvance = autoAdvance;
	}

	/**
	 * Returns the mode set for tournament
	 * auto advance (enum value).
	 * 
	 * @return
	 * 		Mode set for tournament auto advance.
	 */
	public TournamentAutoAdvance getTournamentAutoAdvanceMode()
	{	return tournamentAutoAdvanceMode;
	}

	/**
	 * Changes the mode set for tournament
	 * auto advance (enum value).
	 * 
	 * @param tournamentAutoAdvanceMode
	 * 		New mode for tournament auto advance.
	 */
	public void setTournamentAutoAdvanceMode(TournamentAutoAdvance tournamentAutoAdvanceMode)
	{	this.tournamentAutoAdvanceMode = tournamentAutoAdvanceMode;
	}

	/**
	 * Returns the pack selected for the pack
	 * mode of tournament auto advance.
	 * 
	 * @return
	 * 		Selected pack.
	 */
	public String getTournamentAutoAdvancePack()
	{	return tournamentAutoAdvancePack;
	}

	/**
	 * Changes the pack selected for the pack
	 * mode of tournament auto advance.
	 * 
	 * @param tournamentAutoAdvancePack
	 * 		New pack.
	 */
	public void setTournamentAutoAdvancePack(String tournamentAutoAdvancePack)
	{	this.tournamentAutoAdvancePack = tournamentAutoAdvancePack;
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** During a tournament/match, only show rounds with at least a human player */
	private boolean hideAllAis = false;
	/** Drop a level bomb on players standing still (not doing anything) */
	private long bombUselessAis = -1;
	/** Time probability */
	
	/**
	 * Indicates if the AIs rounds should
	 * be displayed or only simulated.
	 *  
	 * @return
	 * 		{@code true} for simulating, {@code false} for showing.
	 */
	public boolean getHideAllAis()
	{	return hideAllAis;
	}

	/**
	 * Changes the AIs simulation option.
	 * 
	 * @param hideAllAis
	 * 		{@code true} for simulating, {@code false} for showing.
	 */
	public void setHideAllAis(boolean hideAllAis)
	{	this.hideAllAis = hideAllAis;
	}

	/**
	 * Indicates if idle AIs should be threaten
	 * by level bombs.
	 * 
	 * @return
	 * 		{@code true} iff the threatening option is enabled.
	 */
	public long getBombUselessAis()
	{	return bombUselessAis;
	}
	
	/**
	 * Change the AIs threatening option.
	 * 
	 * @param bombUselessPlayers
	 * 		{@code true} iff the threatening option is enabled.
	 */
	public void setBombUselessAis(long bombUselessPlayers)
	{	this.bombUselessAis = bombUselessPlayers;
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY EXCEPTIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Shows exceptions onscren during game */
	private boolean displayExceptions = true;
	
	/**
	 * Indicates if the exceptions thrown by agent threads
	 * should be shown in the console during game.
	 * 
	 * @return
	 * 		{@code true} iff the exceptions are shown.
	 */
	public boolean getDisplayExceptions()
	{	return displayExceptions;
	}

	/**
	 * Changes the option regarding agent exception
	 * display.
	 * 
	 * @param displayExceptions
	 * 		{@code true} to display the agent exceptions.
	 */
	public void setDisplayExceptions(boolean displayExceptions)
	{	this.displayExceptions = displayExceptions;
	}

	/////////////////////////////////////////////////////////////////
	// EXCEPTIONS LOG	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if agent exceptions should be logged in a file */
	private boolean logExceptions = false;
	/** Indicates if logged exceptions should be separated by agent */
	private boolean logExceptionsSeparately = false;
	/** Stream used for logging */
	private OutputStream exceptionsLogStream;

	/**
	 * Indicates if agent exceptions should be logged in a file.
	 * 
	 * @return
	 * 		{@code true} to log exceptions.
	 */
	public boolean getLogExceptions()
	{	return logExceptions;
	}
	
	/**
	 * Changes the option regarding agent exceptions logging.
	 * 
	 * @param logExceptions
	 * 		{@code true} to log exceptions.
	 */
	public void setLogExceptions(boolean logExceptions)
	{	this.logExceptions = logExceptions;
	}
	
	/**
	 * Changes the option regarding separate agent
	 * exception logging.
	 * 
	 * @param logExceptionsSeparately
	 * 		{@code true} to log exceptions separately for each agent.
	 */
	public void setLogExceptionsSeparately(boolean logExceptionsSeparately)
	{	this.logExceptionsSeparately = logExceptionsSeparately;
	}
	
	/**
	 * Creates a stream for exception logging.
	 * 
	 * @throws FileNotFoundException
	 * 		Problem while creating the stream.
	 */
	public void initExceptionsLogStream() throws FileNotFoundException
	{	// init path
		String path = FilePaths.getLogsPath()+File.separator;
		// put the date
		if(logExceptionsSeparately)
			path = path + FileTools.getFilenameCompatibleCurrentTime() + "_";
		// put name and extension
		path = path + FileNames.FILE_AI_EXCEPTIONS + FileNames.EXTENSION_LOG;
		// open file
		File logFile = new File(path);
		if(logFile.exists())
			logFile.delete();
		FileOutputStream fileOut = new FileOutputStream(logFile);
		exceptionsLogStream = new BufferedOutputStream(fileOut);
	}
	
	/**
	 * Close the exceptions stream.
	 * 
	 * @throws IOException
	 * 		Problem while closing the stream.
	 */
	public void closeExceptionsLogStream() throws IOException
	{	exceptionsLogStream.close();		
	}
	
	/**
	 * Returns the (previously opened) exception
	 * stream.
	 * 
	 * @return
	 * 		Stream used to log agent exceptions.
	 */
	public OutputStream getExceptionsLogOutput()
	{	return exceptionsLogStream;
	}

	/////////////////////////////////////////////////////////////////
	// RECORD STATS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if agent stats should be recorded in text files */
	private boolean recordStats = false;
	
	/**
	 * Indicates if stats should be record as text files.
	 * This allows chaining many tournaments automatically
	 * and then assessing results later.
	 * 
	 * @return
	 * 		{@code true} iff stats are recorded.
	 */
	public boolean getRecordStats()
	{	return recordStats;
	}

	/**
	 * Changes the option regarding agent stat recording.
	 * 
	 * @param recordStats
	 * 		{@code true} to record agent stats.
	 */
	public void setRecordStats(boolean recordStats)
	{	this.recordStats = recordStats;
	}
}
