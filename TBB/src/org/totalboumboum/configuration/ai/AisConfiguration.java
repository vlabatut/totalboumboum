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
 * 
 * @author Vincent Labatut
 *
 */
public class AisConfiguration
{
	public AisConfiguration copy()
	{	AisConfiguration result = new AisConfiguration();

		result.setAiUps(aiUps);
		
		result.setAutoAdvance(autoAdvance);
		result.setAutoAdvanceDelay(autoAdvanceDelay);
		
		result.setHideAllAis(hideAllAis);
		result.setBombUselessAis(bombUselessAis);
		
		result.setDisplayExceptions(displayExceptions);

		result.setLogExceptions(logExceptions);
		result.setLogExceptionsSeparately(logExceptionsSeparately);

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TIMING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** ai updates per second */
	private int aiUps = 50;
	/** ai update period */
	private long aiPeriod = (long)(1000.0/aiUps);
	/** ai yield period (experimental) */
	private long aiYieldPeriod = (long)50;

	public int getAiUps()
	{	return aiUps;
	}
	
	public void setAiUps(int aiUps)
	{	this.aiUps = aiUps;
		aiPeriod = (long) (1000.0/aiUps);
	}
	
	public long getAiPeriod()
	{	return aiPeriod;
	}

	public long getAiYieldPeriod()
	{	return aiYieldPeriod;
	}

	/////////////////////////////////////////////////////////////////
	// AUTO ADVANCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** during a tournament/match, automatically advances to the next match/round */
	private AutoAdvance autoAdvance = AutoAdvance.NONE;
	/** delay (in ms) before the auto system advances to the next round */
	private long autoAdvanceDelay = 1000;
	
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
	
	public long getAutoAdvanceDelay()
	{	return autoAdvanceDelay;
	}

	public void setAutoAdvanceDelay(long autoAdvanceDelay)
	{	this.autoAdvanceDelay = autoAdvanceDelay;
	}

	public AutoAdvance getAutoAdvance()
	{	return autoAdvance;
	}

	public void setAutoAdvance(AutoAdvance autoAdvance)
	{	this.autoAdvance = autoAdvance;
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** during a tournament/match, only show rounds with at least a human player */
	private boolean hideAllAis = false;
	/** drop a level bomb on players standing still (not doing anything) */
	private long bombUselessAis = -1;
	/** time probability */
	
	public boolean getHideAllAis()
	{	return hideAllAis;
	}

	public void setHideAllAis(boolean hideAllAis)
	{	this.hideAllAis = hideAllAis;
	}

	public long getBombUselessAis()
	{	return bombUselessAis;
	}

	public void setBombUselessAis(long bombUselessPlayers)
	{	this.bombUselessAis = bombUselessPlayers;
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY EXCEPTIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** show exceptions onscren during game */
	private boolean displayExceptions = true;
	
	public boolean getDisplayExceptions()
	{	return displayExceptions;
	}

	public void setDisplayExceptions(boolean displayExceptions)
	{	this.displayExceptions = displayExceptions;
	}

	/////////////////////////////////////////////////////////////////
	// EXCEPTIONS LOG	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean logExceptions = false;
	private boolean logExceptionsSeparately = false;
	private OutputStream exceptionsLogStream;

	public boolean getLogExceptions()
	{	return logExceptions;
	}
	
	public void setLogExceptions(boolean logExceptions)
	{	this.logExceptions = logExceptions;
	}
	
	public void setLogExceptionsSeparately(boolean logExceptionsSeparately)
	{	this.logExceptionsSeparately = logExceptionsSeparately;
	}
	
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
	
	public void closeExceptionsLogStream() throws IOException
	{	exceptionsLogStream.close();		
	}
	
	public OutputStream getExceptionsLogOutput()
	{	return exceptionsLogStream;
	}
}
