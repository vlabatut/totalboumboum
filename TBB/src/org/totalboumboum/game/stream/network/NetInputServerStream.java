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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.game.stream.InputServerStream;

public class NetInputServerStream extends InputServerStream
{	
	public NetInputServerStream(List<Socket> sockets) throws IOException
	{	for(int i=0;i<sockets.size();i++)
		{	Socket socket = sockets.get(i);
			if(socket==null)
				ins[i] = null;
			else
			{	InputStream in = socket.getInputStream();
				ins[i] = new ObjectInputStream(in);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void initRound() throws IOException, ClassNotFoundException
	{	super.initRound();
	
		for(ObjectInputStream in: ins)
		{	ControlSettings cs = null;
			if(in!=null)
				cs = (ControlSettings) in.readObject();
			controlSettings.add(cs);
		}
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			//
		}
	}
}
