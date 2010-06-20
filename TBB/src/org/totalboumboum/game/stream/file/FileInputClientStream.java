package org.totalboumboum.game.stream.file;

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
import java.util.List;

import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.stream.InputClientStream;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;

public class FileInputClientStream extends InputClientStream
{	private static final boolean VERBOSE = false;
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	@Override
	public void initRound() throws IOException, ClassNotFoundException
	{	String folderPath = FilePaths.getReplaysPath() + File.separator + folder;
		String filePath = folderPath + File.separator + FileNames.FILE_REPLAY + FileNames.EXTENSION_DATA;
		File file = new File(filePath);
		FileInputStream fileIn = new FileInputStream(file);
		BufferedInputStream inBuff = new BufferedInputStream(fileIn);
//		ZipInputStream inZip = new ZipInputStream(inBuff);
//		in = new ObjectInputStream(inZip);
		in = new ObjectInputStream(inBuff);
		
		super.initRound();
	}

	@Override
	public void finishRound() throws IOException, ClassNotFoundException
	{	if(VERBOSE)
			System.out.println("reading: stats");
	
		super.finishRound();
		
		close();
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
	protected String levelName;
	protected String levelPack;
	
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
	protected Date saveDate;
	
	public void setSaveDate(Date save)
	{	this.saveDate = save;
	}
	
	public Date getSaveDate()
	{	return saveDate;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final List<String> players = new ArrayList<String>();
	
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
	@Override
	public List<ReplayEvent> readEvents()
	{	List<ReplayEvent> result = new ArrayList<ReplayEvent>();
		
		try
		{	Object object = in.readObject();
			if(object instanceof ReplayEvent)
			{	ReplayEvent event = (ReplayEvent) object;
				result.add(event);
				if(VERBOSE)
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
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			folder = null;
			preview = null;
			levelName = null;
			levelPack = null;
			saveDate = null;
		}
	}
}
