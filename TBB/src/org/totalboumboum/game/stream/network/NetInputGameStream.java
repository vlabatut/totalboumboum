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

import org.totalboumboum.game.stream.InputGameStream;

public class NetInputGameStream extends InputGameStream
{	
	public NetInputGameStream()
	{	
	}

	/////////////////////////////////////////////////////////////////
	// TOURNEMENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void initTournament()
	{
		// TODO
	}
	
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	@Override
	public void initRound() throws IOException, ClassNotFoundException
	{	super.initRound();
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
