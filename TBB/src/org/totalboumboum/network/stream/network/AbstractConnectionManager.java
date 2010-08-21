package org.totalboumboum.network.stream.network;

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
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractConnectionManager<T extends Object, U extends AbstractConnection<T>>
{	
	/////////////////////////////////////////////////////////////////
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	protected List<U> connections = new ArrayList<U>();
	
	protected abstract void addConnection(U connection);
	
	protected abstract void clearConnections();
	
	public void close() throws IOException
	{	for(U connection: connections)
			connection.close();
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final List<T> listeners = new ArrayList<T>();
	
	public boolean addListener(T listener)
	{	boolean result = false;
		if(!listeners.contains(listener))
			result = listeners.add(listener);
		return result;
	}
	
	public boolean removeListener(T listener)
	{	boolean result = listeners.remove(listener);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			
			listeners.clear();
			clearConnections();
		}
	}
}
