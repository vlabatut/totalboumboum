package fr.free.totalboumboum.configuration;

import java.io.File;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

public class GameVariables
{
	/////////////////////////////////////////////////////////////////
	// INSTANCE PATH		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static String instanceName;
	public static String instancePath;
	
    public static void setInstanceName(String instanceName)
    {	GameVariables.instanceName = instanceName;
    	GameVariables.instancePath = FileTools.getInstancesPath()+File.separator+instanceName;
    }

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Level level;
	public static Loop loop;
	
	public static void setLoop(Loop loop)
	{	GameVariables.loop = loop;
		GameVariables.level = loop.getLevel();		
	}
	
	/////////////////////////////////////////////////////////////////
	// SCALE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static double zoomFactor;
	public static double toleranceCoefficient = 1;
	public static double scaledTileDimension;

	public static void setZoomFactor(double zoomFactor)
	{	GameVariables.zoomFactor = zoomFactor;
		toleranceCoefficient = zoomFactor*GameConstants.TOLERANCE;
		scaledTileDimension = GameConstants.STANDARD_TILE_DIMENSION*zoomFactor;
	}

	
}
