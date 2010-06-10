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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.stream.OutputGameStream;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

public class FileOutputGameStream extends OutputGameStream
{	
	public FileOutputGameStream(Round round) throws IOException
	{	// level
		LevelInfo levelInfo = round.getHollowLevel().getLevelInfo();
		levelName = levelInfo.getFolder();
		levelPack = levelInfo.getPackName();
		
		// date
		saveDate = GregorianCalendar.getInstance().getTime();
		
		// players
		List<Profile> profiles = round.getProfiles();
		for(Profile profile: profiles)
		{	String name = profile.getName();
			addPlayer(name);
		}
		
		// paths
		initFolder();
		
		// init recording
		initRecording();
		
		// record round info
		out.writeObject(profiles);
		out.writeObject(levelInfo);
		Limits<RoundLimit> limits = round.getLimits();
		out.writeObject(limits);
		HashMap<String,Integer> itemsCounts = round.getHollowLevel().getItemCount();
		out.writeObject(itemsCounts);
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
		folder = folder + levelPack + "." + levelName;
	}
	
	public String getFolder()
	{	return folder;		
	}
	
	public void setFolder(String folder)
	{	this.folder = folder;
	}
		
	/////////////////////////////////////////////////////////////////
	// OUTPUT				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	private void initRecording() throws IOException
	{	String folderPath = FilePaths.getReplaysPath() + File.separator + folder;
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
	
	/**
	 * close the replay output stream (if it was previously opened)
	 */
	public void finishWriting(StatisticRound stats) throws IOException, ParserConfigurationException, SAXException
	{	super.finishWriting(stats);
		
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
	// PREVIEW				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage preview = null;
	
	public BufferedImage getPreview()
	{	return preview;		
	}
	
	public void setPreview(BufferedImage preview)
	{	this.preview = preview;		
	}
}
