package org.totalboumboum.game.stream;

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
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;

public abstract class InputServerStream
{	protected final boolean verbose = false;
	
	public InputServerStream(HashMap<Integer,HashMap<Integer,Integer>> translation)
	{
	}

	/////////////////////////////////////////////////////////////////
	// TRANSLATION			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<Integer,HashMap<Integer,Integer>> translation;
	
	/////////////////////////////////////////////////////////////////
	// CONTROL SETTINGS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final List<List<ControlSettings>> controlSettings = new ArrayList<List<ControlSettings>>();
	
	public ControlSettings getControlSettings(Profile profile)
	{	ControlSettings result = null;
		int tempIndex = profile.getSocketNumber();
		int localIndex = profile.getLocalNumber();
		int index = translation.get(streamIndex).get(localIndex);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract RemotePlayerControlEvent readEvent(int index);
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * reads remote player-related information in the open streams
	 */
	@SuppressWarnings("unchecked")
	public void initRound() throws IOException, ClassNotFoundException
	{	for(int i=0;i<ins.length;i++)
		{	List<ControlSettings> cs = (List<ControlSettings>) ins[i].readObject();
			controlSettings.add(cs);
		}
	}

	/////////////////////////////////////////////////////////////////
	// STREAMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ObjectInputStream[] ins;
			
	public abstract void initStreams() throws IOException;

	public void close() throws IOException
	{	for(ObjectInputStream ooi: ins)
			ooi.close();
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	finished = true;
		
		controlSettings.clear();
	}
}
