package tournament200910.suiveur;

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

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.BasicCostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;
import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * classe chargée d'implémenter un déplacement, 
 * en respectant un chemin donné
 */
public class Mover
{
	public Mover(Suiveur ai, AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		this.ai = ai;
		costCalculator = new BasicCostCalculator();
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai.getOwnHero(),costCalculator,heuristicCalculator);
		setDestination(destination);
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Suiveur ai;

	/////////////////////////////////////////////////////////////////
	// OBJECTIVE	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la case de destination */
	private AiTile destination;
	/** le chemin à suivre */
	private AiPath path;
	
	public void setDestination(AiTile destination)
	{	this.destination = destination;
	}

	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		result = result && destination.getPosX()==ai.getCurrentX();
		result = result && destination.getPosY()==ai.getCurrentY();
		return result;
	}
/*	
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiHero ownHero = ai.getOwnHero();
		boolean result = ai.getZone().hasSamePixelPosition(ownHero,destination);
		return result;
	}
*/
	
	/////////////////////////////////////////////////////////////////
	// A STAR					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Astar astar;
	private HeuristicCalculator heuristicCalculator;
	private CostCalculator costCalculator;

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public Direction update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	path = astar.processShortestPath(ai.getCurrentTile(),destination);
			if(path.getLength()>1)
			{	AiTile source = path.getTile(0);
				AiTile target = path.getTile(1); 
				result = ai.getZone().getDirection(source,target);
			}
		}
		return result;
	}
}
