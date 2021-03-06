package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3;

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class is aimed to contain useful path operations. This class's methods
 * need an AI to operate (get zone etc.).
 * 
 * @author Utku Ozdemir
 * @author Doruk Coskun
 */
@SuppressWarnings("deprecation")
public class PathOperation
{
	/**
	 * The supplementary cost to an enemy.
	 */
	private final int	ASTAR_COST		= 1000;
	/**
	 * Represents the next tile's index in an array-list etc.
	 */
	private final int	NEXT_TILE_INDEX	= 1;

	/** */
	CoskunOzdemir		ai;

	/**
	 * The AI must be given to this class to operate properly.
	 * 
	 * @param ai
	 *            The AI to give.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public PathOperation( CoskunOzdemir ai ) throws StopRequestException
	{
		ai.checkInterruption();
		this.ai = ai;
	}

	/**
	 * Uses built-in A* method to calculate the shortest path from this AI's
	 * tile to a given destination tile.
	 * 
	 * @param endTile
	 *            Destination tile.
	 * @return Shortest path from this AI's own tile to destination tile.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected AiPath getShortestPath( AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		return this.getShortestPath( this.ai.getHero(), endTile );
	}

	/**
	 * Uses built-in A* method to calculate the shortest path from a given start
	 * tile to a given destination tile.
	 * @param hero 
	 * 		description manquante !
	 * @param endTile
	 *            Destination tile.
	 * @return Shortest path from starting tile to destination tile.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected AiPath getShortestPath( AiHero hero, AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		AiPath path = new AiPath();
		CostCalculator costCalculator = new TimeCostCalculator( ai, this.ai.getHero() );
		costCalculator.setOpponentCost( ASTAR_COST );
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator( ai, this.ai.getHero() );
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator( ai, TimePartialSuccessorCalculator.MODE_NOTREE );
		Astar astarPrecise = new Astar( ai, this.ai.getHero(), costCalculator, heuristicCalculator, successorCalculator );

		AiLocation loc = new AiLocation( hero.getTile() );

		// Shortest path calculation
		if(endTile!=null)
		{	try
			{
				path = astarPrecise.processShortestPath( loc, endTile );
			}
			catch ( LimitReachedException e )
			{	
				path = new AiPath();
				path.addLocation( new AiLocation( this.ai.getHero().getTile() ) );
			}
		}
		return path;
	}

	/**
	 * 
	 * Method to get the next tile to move using A* to calculate the shortest
	 * path between this AI's current tile and a given end tile. If the end tile
	 * is this AI's tile, this tile will be returned, else it will be a neighbor
	 * of this AI's tile.
	 * 
	 * @param endTile
	 *            Destination tile.
	 * @return Next tile on path.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected AiTile getNextTileOnPath( AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		return this.getNextTileOnPath( this.ai.getHero(), endTile );
	}

	/**
	 * Method to get the next tile to move using A* to calculate the shortest
	 * path between a given starting tile and an end tile. If the end tile is
	 * starting tile, this tile will be returned, else it will be a neighbor of
	 * the start tile.
	 * @param hero 
	 * 		description manquante !
	 * @param endTile
	 *            Destination tile.
	 * @return Next tile on path.
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	protected AiTile getNextTileOnPath( AiHero hero, AiTile endTile ) throws StopRequestException
	{	ai.checkInterruption();
		
		AiTile result = hero.getTile();
		if ( !hero.getTile().equals( endTile ) ) 
		{	
			AiPath path = this.getShortestPath( hero, endTile );
			if(path!=null && path.getLength()>NEXT_TILE_INDEX)
			{	AiLocation location = path.getLocation( NEXT_TILE_INDEX );
				if(location!=null)
					result = location.getTile();
			}
		}
		
		return result;
	}

	/**
	 * Method to get the direction to go to follow the path that calculated by
	 * A* from this AI's current tile and a given end tile.
	 * 
	 * @param endTile
	 *            Destination tile.
	 * @return Direction to go.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected Direction getNextDirectionOnPath( AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		return this.ai.getZone().getDirection( this.ai.getHero().getTile(), this.getNextTileOnPath( endTile ) );
	}

	/**
	 * Method to get the direction to go to follow the path that calculated by
	 * A* from a given start tile and an end tile.
	 * @param hero 
	 * 		description manquante !
	 * @param endTile
	 *            Destination tile.
	 * @return Direction to go.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected Direction getNextDirectionOnPath( AiHero hero, AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		return this.ai.getZone().getDirection( hero, this.getNextTileOnPath( hero, endTile ) );
	}
}
