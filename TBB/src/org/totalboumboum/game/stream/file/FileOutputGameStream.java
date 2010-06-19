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
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.stream.OutputServerStream;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

public class FileOutputGameStream extends OutputServerStream
{	
	public FileOutputGameStream(Round round) throws IOException
	{	super(round);
	
		// init file-related data
		initSaveDate();
		initFolder();
		
		// init recording
		initStream();
		initRound();
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * close the replay output stream (if it was previously opened)
	 */
	@Override
	public void finishRound(StatisticRound stats) throws IOException, ParserConfigurationException, SAXException
	{	super.finishRound(stats);
		close();
		
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
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	private void initStream() throws IOException
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
		ObjectOutputStream o = new ObjectOutputStream(outBuff);
		outs.add(o);
	}
	
	@Override
	protected void write(Object object) throws IOException
	{	for(ObjectOutputStream o: outs)
			o.writeObject(object);
	}

	private void close() throws IOException
	{	for(ObjectOutputStream o: outs)
			o.close();
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
}
