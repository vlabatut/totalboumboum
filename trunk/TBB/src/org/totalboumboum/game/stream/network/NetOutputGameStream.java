package org.totalboumboum.game.stream.network;

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

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.stream.OutputGameStream;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.xml.sax.SAXException;

public class NetOutputGameStream extends OutputGameStream
{	
	public NetOutputGameStream(Round round) throws IOException
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
//TODO		initFolder();
		
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
	// NETWORK				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	private void initRecording() throws IOException
	{	
//TODO		out = new ObjectOutputStream(outBuff);
	}
	
	/**
	 * close the replay output stream (if it was previously opened)
	 */
	public void finishWriting(StatisticRound stats) throws IOException, ParserConfigurationException, SAXException
	{	super.finishWriting(stats);
		
//TODO
	}
}
