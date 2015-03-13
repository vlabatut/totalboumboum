/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimTile;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class helps path calculations
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
@SuppressWarnings("deprecation")
public class PathHandler extends AiAbstractHandler<Agent>
{
    /** Store current zone */
    public AiZone zone = null;
    /** Store own hero */
    public AiHero ownHero = null;
    /** Store cost calculator */
    public CostCalculator timeCost;
    /** Store heuristic calculator */
    public HeuristicCalculator timeHeuristic;
    /** Store successor calculator */
    public SuccessorCalculator timeSuccessor;
    /** Store time based astar */
    public Astar astarTime = null;
    /** Store approximate based astar */
    public Astar astarApprox = null;
    /** Store Dijkstra */
    public Dijkstra dijkstra;

    /**
     * Initialize variables
     * @param ai Agent
     */
    protected PathHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();

        zone = ai.getZone();

        ownHero = zone.getOwnHero();

        timeCost = new TimeCostCalculator(ai, ownHero);
        timeCost.setMalusCost(1000);

        timeHeuristic = new TimeHeuristicCalculator(ai, ownHero);
        timeSuccessor = new TimePartialSuccessorCalculator(ai,
                SearchMode.MODE_NOBRANCH);
        astarTime = new Astar(ai, ownHero, timeCost, timeHeuristic,
                timeSuccessor);

        CostCalculator approxCost = new ApproximateCostCalculator(ai, ownHero);
        approxCost.setMalusCost(2000);

        HeuristicCalculator noHeuristic = new NoHeuristicCalculator(ai);
        SuccessorCalculator approxSuccessor = new ApproximateSuccessorCalculator(
                ai);

        astarApprox = new Astar(ai, ownHero, approxCost, noHeuristic,
                approxSuccessor);

        dijkstra = new Dijkstra(ai, ownHero, timeCost, timeSuccessor);
    }

    
    /**
     * Find path using astar
     * @param startLocation Start location 
     * @param endTile Finish tile
     * @return path
     */
    public AiPath findPath(AiLocation startLocation, AiTile endTile)
    {
        ai.checkInterruption();

        if(startLocation == null || endTile == null)
            return null;

        AiPath result = null;
        try
        {
            ai.checkInterruption();

            if(ownHero.getTile().getBombs().isEmpty())
            {
                timeSuccessor.setConsiderOpponents(false);
                timeCost.setOpponentCost(2000);
            }

            result = astarTime.startProcess(startLocation, endTile);
        }
        catch (LimitReachedException e)
        {
            result = null;
        }

        if(result == null)
        {
            /* Astar failed */
            /* Try dijkstra */
            try
            {
                result = dijkstra.processEscapePath(startLocation);
            }
            catch (LimitReachedException eD)
            {
                ai.bombHandler.consider = true;
                result = null;
            }
        }

        return result;
    }

    /**
     * Find path using approximately astar
     * @param startLocation Start location 
     * @param endTile Finish tile
     * @return path
     */
    public AiPath findApproxPath(AiLocation startLocation, AiTile endTile)
    {
        ai.checkInterruption();

        AiPath result = null;

        if(startLocation == null || endTile == null)
            return null;

        try
        {
            result = astarApprox.startProcess(startLocation, endTile);
        }
        catch (LimitReachedException eA)
        {
            result = null;
        }

        if(result == null)
        {
            /* Approx astar failed */
            /* Try dijkstra */
            try
            {
                result = dijkstra.processEscapePath(startLocation);
            }
            catch (LimitReachedException eD)
            {
                ai.bombHandler.consider = true;
                result = null;
            }
        }

        return result;
    }

    /**
     * Simulate Can reach safety
     * 
     * @return True if a path found, false else
     */
    public boolean canReachSafety()
    {
        ai.checkInterruption();

        /* Create new path */
        AiPath path = null;
        /* Create new SimZone */
        AiSimZone simZone = new AiSimZone(zone);
        /* Own Simulated hero */
        AiSimHero ownSimHero = simZone.getOwnHero();
        /* Simulate location */
        AiLocation start = new AiLocation(ownSimHero);
        /* Own simTile */
        AiSimTile ownTile = ownSimHero.getTile();
        /* Calculators */
        TimeCostCalculator simTimeCost = new TimeCostCalculator(ai, ownSimHero);
        SuccessorCalculator simTimeSuccessor = new TimePartialSuccessorCalculator(
                ai, SearchMode.MODE_NOBRANCH);

        simTimeSuccessor.setConsiderOpponents(true);

        /* Simulate a bomb on our location */
        simZone.createBomb(ownTile, ownSimHero);

        Dijkstra dijkstraSim = new Dijkstra(ai, ownSimHero, simTimeCost,
                simTimeSuccessor);

        // Astar loop = new Astar(ai, ownSimHero, simTimeCost, simTimeHeuristic,
        // simTimeSuccessor);

        try
        {
            path = dijkstraSim.processEscapePath(start);
            // path = loop.processLoopPath(start);
        }
        catch (LimitReachedException e)
        {
            path = null;
        }

        if(path != null && path.getLength() > 1)
        {
            /* Path found, can put a bomb */
            return true;
        }
        /* Do not put a bomb */
        return false;
    }

    /**
     * 
     * A simple method to calculate distance between two tiles.
     * 
     * @param tile1
     *            given tile 1
     * @param tile2
     *            given tile 2
     * @return distance between two tiles.
     */
    public int simpleTileDistance(AiTile tile1, AiTile tile2)
    {
        ai.checkInterruption();
        return Math.abs(tile1.getCol() - tile2.getCol())
                + Math.abs(tile1.getRow() - tile2.getRow());
    }

    /**
     * A simple method to find direction one tile to to another.
     * 
     * @param tile1 given tile 1
     * @param tile2 given tile 2
     * @return direction one tile to another.
     */
    public Direction simpleDirection(AiTile tile1, AiTile tile2)
    {
        ai.checkInterruption();
        
        /* Get direction from here to enemy */
        Direction direction = zone.getDirection(tile1, tile2);

        if(direction == Direction.UP || direction == Direction.DOWN)
        {
            if(simpleTileDistance(tile1, tile2) > ai.zoneHandler.mapHeight / 2)
                direction = direction.getOpposite();
        }
        else if(direction == Direction.RIGHT || direction == Direction.LEFT)
        {
            if(simpleTileDistance(tile1, tile2) > ai.zoneHandler.mapWidth / 2)
                direction = direction.getOpposite();
        }
        
        return direction;
    }
    
}
