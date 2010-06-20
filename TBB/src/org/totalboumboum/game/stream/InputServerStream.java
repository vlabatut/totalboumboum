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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;

public abstract class InputServerStream
{	private static final boolean VERBOSE = false;
	
	/////////////////////////////////////////////////////////////////
	// CONTROL SETTINGS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final List<ControlSettings> controlSettings = new ArrayList<ControlSettings>();
	
	public List<ControlSettings> getControlSettings()
	{	return controlSettings;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * reads an event in the currently open stream.
	 */
	public RemotePlayerControlEvent readEvent(int index)
	{	RemotePlayerControlEvent result = null;
		
		try
		{	Object object = ins[index].readObject();
			if(object instanceof RemotePlayerControlEvent)
			{	result = (RemotePlayerControlEvent) object;
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
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * reads remote player-related information in the open streams
	 */
	public void initRound() throws IOException, ClassNotFoundException
	{	for(int i=0;i<ins.length;i++)
		{	ObjectInputStream in = ins[i];
			if(in!=null)
			{	ControlSettings cs = (ControlSettings) ins[i].readObject();
				controlSettings.add(cs);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// STREAMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ObjectInputStream[] ins;
			
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
