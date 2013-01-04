package org.totalboumboum.ai.v201213.ais.erdemtayyar.v4;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;

import org.totalboumboum.ai.v201213.adapter.data.AiTile;

import org.totalboumboum.ai.v201213.adapter.path.AiLocation;

import org.totalboumboum.ai.v201213.adapter.path.AiPath;

import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;

import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;

import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;

import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;

import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;

import org.totalboumboum.ai.v201213.adapter.path.search.Astar;

import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;

import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import java.awt.Color;


import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * This class is aimed to contain useful path operations. This class's methods
 * 
 * need an AI to operate (get zone etc.).
 * 
 * 
 * 
 * @author Banu Erdem
 * 
 * @author Zübeyir Tayyar
 */

public class Paths {

	/**
	 * 
	 * The supplementary cost to an enemy.
	 */

	private final int ASTAR_COST = 2000;

	/**
	 * 
	 * Represents the next tile's index in an array-list etc.
	 */

	private final int NEXT_TILE_INDEX = 1;

	/**
	 * 
	 * our IA
	 */

	private ErdemTayyar ai;

	
	/**
	 * 
	 */
	public AiPath path;
	/**
	 * 
	 * This method is for finding our path
	 * 
	 * @param ai
	 * 
	 * @throws StopRequestException
	 */

	public Paths(ErdemTayyar ai) throws StopRequestException

	{

		ai.checkInterruption();

		this.ai = ai;

	}

	/**
	 * 
	 * Method to get the direction to go to follow the path that calculated by
	 * 
	 * A* from this AI's current tile and a given end tile.
	 * 
	 * 
	 * 
	 * @param endTile
	 * 
	 *            Destination tile.
	 * 
	 * @return Direction to go.
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	protected Direction getNextDirectionOnPath(AiTile endTile)
			throws StopRequestException

	{

		ai.checkInterruption();
        
		return this.ai.getZone().getDirection(this.ai.getHero().getTile(),
				this.getNextTileOnPath(endTile));

	}

	/**
	 * 
	 * 
	 * 
	 * Method to get the next tile to move using A* to calculate the shortest
	 * 
	 * path between this AI's current tile and a given end tile. If the end tile
	 * 
	 * is this AI's tile, this tile will be returned, else it will be a neighbor
	 * 
	 * of this AI's tile.
	 * 
	 * 
	 * 
	 * @param endTile
	 * 
	 *            Destination tile.
	 * 
	 * 
	 * 
	 * @return Next tile on path.
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	protected AiTile getNextTileOnPath(AiTile endTile)
			throws StopRequestException

	{

		ai.checkInterruption();

		return this.getNextTileOnPath(this.ai.getHero(), endTile);

	}

	/**
	 * 
	 * Method to get the next tile to move using A* to calculate the shortest
	 * 
	 * path between a given starting tile and an end tile. If the end tile is
	 * 
	 * starting tile, this tile will be returned, else it will be a neighbor of
	 * 
	 * the start tile.
	 * 
	 * @param hero
	 * 
	 * 
	 * 
	 * @param endTile
	 * 
	 *            Destination tile.
	 * 
	 * @return Next tile on path.
	 * 
	 * @throws StopRequestException
	 */
	
	protected AiTile getNextTileOnPath(AiHero hero, AiTile endTile)
			throws StopRequestException

	{

		ai.checkInterruption();

		if (hero.getTile().equals(endTile))
			return hero.getTile();
		try

		{
            
			return this.getPath(hero,endTile).getLocation(NEXT_TILE_INDEX).getTile();

		}

		catch (NullPointerException e)

		{

			return hero.getTile();

		}
		catch (IndexOutOfBoundsException e)

		{

			return hero.getTile();

		}
	}
	
	
	/**
	 * 
	 * Uses built-in A* method to calculate the shortest path from a given start
	 * 
	 * tile to a given destination tile.
	 * 
	 * @param hero
	 * 
	 * 
	 * 
	 * @param endTile
	 * 
	 *            Destination tile.
	 * @return Returns path which is calculated by A*
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */
	protected AiPath getPath (AiHero hero, AiTile endTile) throws StopRequestException{
		ai.checkInterruption();
		CostCalculator costCalculator = new TimeCostCalculator(ai,
				this.ai.getHero());

		costCalculator.setOpponentCost(ASTAR_COST);
        costCalculator.setMalusCost(1000);
        
        HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
				ai, this.ai.getHero());

		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
				ai, TimePartialSuccessorCalculator.MODE_NOTREE);
		Astar astarPrecise = new Astar(ai, this.ai.getHero(), costCalculator,
				heuristicCalculator, successorCalculator);
		 
			AiLocation loc = new AiLocation(hero.getTile());
			try

			{

				path = astarPrecise.startProcess(loc, endTile);

			}

			catch (LimitReachedException e)

			{

				path = new AiPath();

				path.addLocation(new AiLocation(this.ai.getHero().getTile()));

			}
			Color color = null;
	        AiMode mode=ai.getModeHandler().getMode();

			if (mode==AiMode.ATTACKING)
				color = Color.RED;
			else if(mode==AiMode.COLLECTING)
				color =Color.BLUE;
			AiOutput output = ai.getOutput();
			output.addPath(path,color);
			return path;		
	}

}