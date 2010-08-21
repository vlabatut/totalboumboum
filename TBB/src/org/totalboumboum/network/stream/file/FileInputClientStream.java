package org.totalboumboum.network.stream.file;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class FileInputClientStream
{	private final boolean verbose = false;
	
	public FileInputClientStream()
	{	
	}
	
	public FileInputClientStream(String folder)
	{	super();
		this.folder = folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// ZOOM					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Double zoomCoef = null;

	public double getZoomCoef()
	{	return zoomCoef;
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Profile> profiles = null;

	public List<Profile> getProfiles()
	{	return profiles;
	}

	/////////////////////////////////////////////////////////////////
	// INFO					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelInfo levelInfo = null;

	public LevelInfo getLevelInfo()
	{	return levelInfo;
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits<RoundLimit> roundLimits = null;

	public Limits<RoundLimit> getRoundLimits()
	{	return roundLimits;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,Integer> itemCounts = null;

	public HashMap<String,Integer> getItemCounts()
	{	return itemCounts;
	}

	/////////////////////////////////////////////////////////////////
	// STATS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticRound roundStats = null;
	
	public StatisticRound getRoundStats()
	{	return roundStats;
	}

	/////////////////////////////////////////////////////////////////
	// FOLDER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String folder = null;

	public String getFolder()
	{	return folder;		
	}
	
	public void setFolder(String folder)
	{	this.folder = folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// PREVIEW				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage preview = null;
	
	public BufferedImage getPreview()
	{	return preview;		
	}
	
	public void setPreview(BufferedImage preview)
	{	this.preview = preview;		
	}

	/////////////////////////////////////////////////////////////////
	// LEVEL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String levelName;
	private String levelPack;
	
	public void setLevelName(String name)
	{	this.levelName = name;
	}
	
	public String getLevelName()
	{	return levelName;
	}
	
	public void setLevelPack(String levelPack)
	{	this.levelPack = levelPack;
	}
	
	public String getLevelPack()
	{	return levelPack;
	}
	
	/////////////////////////////////////////////////////////////////
	// DATE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Date saveDate;
	
	public void setSaveDate(Date save)
	{	this.saveDate = save;
	}
	
	public Date getSaveDate()
	{	return saveDate;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> players = new ArrayList<String>();
	
	public void addPlayer(String player)
	{	players.add(player);
	}
	
	public List<String> getPlayers()
	{	return players;
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * reads an event in the currently open stream.
	 */
	public ReplayEvent readEvent()
	{	ReplayEvent result = null;
		
		try
		{	Object object = in.readObject();
			if(object instanceof ReplayEvent)
			{	result = (ReplayEvent) object;
				if(verbose)
					System.out.println("reading: "+result);
			}
		}
		catch (EOFException e) 
		{	//
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	public void initRound() throws IOException, ClassNotFoundException
	{	profiles = (List<Profile>) in.readObject();
		levelInfo = (LevelInfo) in.readObject();
		roundLimits = (Limits<RoundLimit>) in.readObject();		
		itemCounts = (HashMap<String,Integer>) in.readObject();		
		zoomCoef = (Double) in.readObject();
	}

	public void finishRound() throws IOException, ClassNotFoundException
	{	if(verbose)
			System.out.println("reading: stats");
		roundStats = (StatisticRound) in.readObject();
	}
	
	/////////////////////////////////////////////////////////////////
	// STREAMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectInputStream in = null;

	public void initStreams() throws IOException
	{	String folderPath = FilePaths.getReplaysPath() + File.separator + folder;
		String filePath = folderPath + File.separator + FileNames.FILE_REPLAY + FileNames.EXTENSION_DATA;
		File file = new File(filePath);
		FileInputStream fileIn = new FileInputStream(file);
		BufferedInputStream inBuff = new BufferedInputStream(fileIn);
//		ZipInputStream inZip = new ZipInputStream(inBuff);
//		in = new ObjectInputStream(inZip);
		in = new ObjectInputStream(inBuff);
	}

	public void close() throws IOException
	{	in.close();
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			
			in = null;
			
			itemCounts = null;
			levelInfo = null;
			profiles = null;
			roundLimits = null;
			roundStats = null;
			zoomCoef = null;
	
			folder = null;
			preview = null;
			levelName = null;
			levelPack = null;
			saveDate = null;
		}
	}
}
