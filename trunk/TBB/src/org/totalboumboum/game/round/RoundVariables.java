package org.totalboumboum.game.round;

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

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.stream.file.FileOutputGameStream;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.tools.GameData;
import org.xml.sax.SAXException;


public class RoundVariables
{
	/////////////////////////////////////////////////////////////////
	// INSTANCE PATH		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Instance instance;
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Level level;
	public static VisibleLoop loop;
	
	/////////////////////////////////////////////////////////////////
	// SCALE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static double zoomFactor;
	public static double toleranceCoefficient = 1;
	public static double scaledTileDimension;

	public static void setZoomFactor(double zoomFactor)
	{	RoundVariables.zoomFactor = zoomFactor;
		toleranceCoefficient = zoomFactor*GameData.TOLERANCE;
		scaledTileDimension = GameData.STANDARD_TILE_DIMENSION*zoomFactor;
	}

	/////////////////////////////////////////////////////////////////
	// REPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
//	public static Replay replay = null;
	
	public static void writeEvent(ReplayEvent event)
	{	if(replay!=null && event.getSendEvent())
			replay.writeEvent(event);
	}

	public static void writeZoomCoef(double zoomCoef) throws IOException
	{	if(replay!=null)
			replay.writeZoomCoef(zoomCoef);
	}

	public static ReplayEvent readEvent()
	{	ReplayEvent result = null;
		if(replay!=null)
			result = replay.readEvent();
		return result;
	}
	
	public static void setFilterEvents(boolean flag)
	{	if(replay!=null)
			replay.setFilterEvents(flag);
	}
	
	public static boolean getFilterEvents()
	{	boolean result = false;
		if(replay!=null)
			result = replay.getFilterEvents();
		return result;
	}
	
	public static void finishWriting(StatisticRound stats) throws IOException, ParserConfigurationException, SAXException
	{	if(replay!=null)
			finishWriting(stats);
	}

}
