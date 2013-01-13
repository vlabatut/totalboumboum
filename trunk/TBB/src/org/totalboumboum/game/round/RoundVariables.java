package org.totalboumboum.game.round;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.stream.file.replay.FileClientStream;
import org.totalboumboum.stream.file.replay.FileServerStream;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.GameData;

/**
 * Sets of common variables
 * used when handling the current round.
 *  
 * @author Vincent Labatut
 */
public class RoundVariables
{
	/////////////////////////////////////////////////////////////////
	// INSTANCE PATH		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Location of the round instance */
	public static Instance instance;
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Level used in this round */
	public static Level level;
	/** Engine used to play the round */
	public static VisibleLoop loop;
	
	/////////////////////////////////////////////////////////////////
	// SCALE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Zoom coefficient */
	public static double zoomFactor = 1;
	/** Tolerance value */
	public static double toleranceCoefficient = GameData.TOLERANCE;
	/** Dimension of a (scaled) tile */
	public static double scaledTileDimension = GameData.STANDARD_TILE_DIMENSION;

	/**
	 * Changes the zoom coefficient 
	 * for the current round.
	 * 
	 * @param zoomFactor
	 * 		New zoom coefficient.
	 */
	public static void setZoomFactor(double zoomFactor)
	{	RoundVariables.zoomFactor = zoomFactor;
		toleranceCoefficient = zoomFactor*GameData.TOLERANCE;
		scaledTileDimension = GameData.STANDARD_TILE_DIMENSION*zoomFactor;
	}

	/////////////////////////////////////////////////////////////////
	// INPUT STREAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** File input stream for the network mode */
	public static FileClientStream fileIn = null;

	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** File output stream for the network mode */
	public static FileServerStream fileOut = null;
	
	/**
	 * Write a replay event in the output stream.
	 * 
	 * @param event
	 * 		Replay event to be written.
	 */
	public static void writeEvent(ReplayEvent event)
	{	if(fileOut!=null && event.getSendEvent())
			fileOut.writeEvent(event);
	
		ServerGeneralConnection connection = Configuration.getConnectionsConfiguration().getServerConnection();
		if(connection!=null && event.getSendEvent())
		{	connection.sendReplay(event);
		}
	}

	/**
	 * Write a control event in the output stream.
	 * 
	 * @param event
	 * 		Control event to be written.
	 */
	public static void writeEvent(RemotePlayerControlEvent event)
	{	ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
		if(connection!=null)
			connection.sendControl(event);
	}
	
	/**
	 * Write the zoom coefficient in the output stream.
	 * 
	 * @param zoomCoef
	 * 		Zoom coefficient to be written.
	 * 
	 * @throws IOException
	 * 		Problem while writing the zoom coefficient in the output stream.
	 */
	public static void writeZoomCoef(double zoomCoef) throws IOException
	{	if(fileOut!=null)
			fileOut.writeZoomCoef(zoomCoef);
	
		ServerGeneralConnection connection = Configuration.getConnectionsConfiguration().getServerConnection();
		if(connection!=null)
		{	connection.updateZoomCoef(zoomCoef);
		}
	}
	
	/**
	 * Changes the way events are filtered
	 * before writing them in the output streams.
	 *  
	 * @param flag
	 * 		The new filter flag.
	 */
	public static void setFilterEvents(boolean flag)
	{	if(fileOut!=null)
			fileOut.setFilterEvents(flag);
//NOTE NET		if(netServerOut!=null)
//NOTE NET			netServerOut.setFilterEvents(flag);
	}

	/**
	 * Returns the way events are filtered
	 * before writing them in the output streams.
	 * 
	 * @return
	 * 		Current filter.
	 */
	public static boolean getFilterEvents()
	{	boolean result = false;
		if(fileOut!=null)
			result = fileOut.getFilterEvents();
//NOTE NET		if(netServerOut!=null)
//NOTE NET			result = netServerOut.getFilterEvents();
		return result;
	}
}
