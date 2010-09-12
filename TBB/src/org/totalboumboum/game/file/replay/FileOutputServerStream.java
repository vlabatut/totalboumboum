package org.totalboumboum.game.file.replay;

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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.loop.event.StreamedEvent;
import org.totalboumboum.engine.loop.event.replay.StopReplayEvent;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class FileOutputServerStream
{	private final boolean verbose = false;

	public FileOutputServerStream(Round round)
	{	this.round = round;
	
		initSaveDate();
		initFolder();
	}
	
	/////////////////////////////////////////////////////////////////
	// ZOOM					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void writeZoomCoef(double zoomCoef) throws IOException
	{	write(zoomCoef);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROFILES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeProfiles() throws IOException
	{	List<Profile> profiles = round.getProfiles();
		write(profiles);
	}

	/////////////////////////////////////////////////////////////////
	// INFO					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeLevelInfo() throws IOException
	{	LevelInfo leveInfo = round.getHollowLevel().getLevelInfo();
		out.writeObject(leveInfo);
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeLimits() throws IOException
	{	Limits<RoundLimit> limits = round.getLimits();
		write(limits);
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeItems() throws IOException
	{	HashMap<String,Integer> itemsCounts = round.getHollowLevel().getItemCount();
		write(itemsCounts);
	}

	/////////////////////////////////////////////////////////////////
	// STATS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeStats() throws IOException
	{	StatisticRound stats = round.getStats();
		write(stats);
	}

	/////////////////////////////////////////////////////////////////
	// FOLDER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String folder = null;

	private void initFolder()
	{	folder = "";
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(saveDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits(2);
		folder = year + "." + nf.format(month) + "." + nf.format(day) + ".";
		folder = folder + nf.format(hourOfDay) + "." + nf.format(minute) + "." + nf.format(second) + ".";
		folder = folder + getLevelPack() + "." + getLevelName();
	}
	
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

	/////////////////////////////////////////////////////////////////
	// DATE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Date saveDate;
	
	public Date getSaveDate()
	{	return saveDate;
	}

	private void initSaveDate()
	{	saveDate = GregorianCalendar.getInstance().getTime();
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public List<String> getPlayers()
	{	List<String> result = new ArrayList<String>();
		List<Profile> profiles = round.getProfiles();
		for(Profile profile: profiles)
		{	String name = profile.getName();
			result.add(name);
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// LEVEL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String getLevelName()
	{	LevelInfo levelInfo = round.getHollowLevel().getLevelInfo();
		String result = levelInfo.getFolder();
		return result;
	}
	
	public String getLevelPack()
	{	LevelInfo levelInfo = round.getHollowLevel().getLevelInfo();
		String result = levelInfo.getPackName();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void writeEvent(StreamedEvent event)
	{	try
		{	out.writeObject(event);
			if(verbose)
				System.out.println("recording: "+event);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Round round;

	public void initRound() throws IOException
	{	writeProfiles();
		writeLevelInfo();
		writeLimits();
		writeItems();
	}

	public void finishRound(StatisticRound stats) throws IOException, ParserConfigurationException, SAXException
	{	// put a stop event
		StopReplayEvent event = new StopReplayEvent();
		writeEvent(event);
		
		// record the stats
		writeStats();
		
		if(verbose)
			System.out.println("recording: stats");
		
//		close();
		
		// possibly record the preview
		if(preview!=null)
		{	String previewFilename = FilePaths.getReplaysPath() + File.separator + folder + File.separator + FileNames.FILE_PREVIEW + FileNames.EXTENSION_PNG;
			File file = new File(previewFilename);
			ImageIO.write(preview,"png",file);
		}
		
		// record the associated xml file
		ReplaySaver.saveReplay(this);
	}
	
	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectOutputStream out = null;

	public void initStream() throws IOException
	{	// open file
		String folderPath = FilePaths.getReplaysPath() + File.separator + folder;
		File file = new File(folderPath);
		file.mkdir();
		String filePath = folderPath + File.separator + FileNames.FILE_REPLAY + FileNames.EXTENSION_DATA;
		file = new File(filePath);
		FileOutputStream fileOut = new FileOutputStream(file);
		BufferedOutputStream outBuff = new BufferedOutputStream(fileOut);
//		ZipOutputStream outZip = new ZipOutputStream(outBuff);
//		out = new ObjectOutputStream(outZip);
		out = new ObjectOutputStream(outBuff);
	}
	
	protected void write(Object object) throws IOException
	{	out.writeObject(object);
	}

	public void close() throws IOException
	{	out.close();
	}

	/////////////////////////////////////////////////////////////////
	// FILTER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean filterEvents = true;

	public void setFilterEvents(boolean flag)
	{	filterEvents = flag;		
	}
	
	public boolean getFilterEvents()
	{	return filterEvents;		
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			
			out = null;
			round = null;
			
			folder = null;
			preview = null;
			saveDate = null;
		}
	}
}
