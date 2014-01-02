package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
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
 * This class handles the destination, path and direction choices.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
public class MoveHandler extends AiMoveHandler<Agent>
{
    /**
     * Constructs a handler for the agent which passed by parameter.
     * 
     * @param ai
     *            the agent that this class should handle
     */
    protected MoveHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();

        zone = ai.getZone();
        ownHero = zone.getOwnHero();

        timeCost = new TimeCostCalculator(ai, ownHero);
        timeCost.setOpponentCost(1000);
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

    // ///////////////////////////////////////////////////////////////
    // DATA /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    /** Store current zone */
    private AiZone zone = null;
    /** Store own hero */
    private AiHero ownHero = null;
    /** Store cost calculator */
    private CostCalculator timeCost;
    /** Store heuristic calculator */
    private HeuristicCalculator timeHeuristic;
    /** Store successor calculator */
    private SuccessorCalculator timeSuccessor;
    /** Store time based astar */
    public Astar astarTime = null;
    /** Store approximate based astar */
    private Astar astarApprox = null;
    /** Store start location */
    private AiLocation startLocation;
    /** Store endTile */
    private AiTile endTile;
    /** Store Dijkstra */
    private Dijkstra dijkstra;
    /** If we put a bomb in blockage strategy, find a path immediately */
    public boolean processEscape;
    /** Malus flag for bombing the malus */
    public boolean malus;

    // ///////////////////////////////////////////////////////////////
    // DESTINATION /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected AiTile processCurrentDestination()
    {
        ai.checkInterruption();

        /* Reset consider flag */
        ai.bombHandler.consider = false;

        Map<Integer, List<AiTile>> preferences = ai.preferenceHandler
                .getPreferencesByValue();

        int minPref = Collections.min(preferences.keySet());
        List<AiTile> tiles = preferences.get(minPref);

        AiTile previousDestination = getCurrentDestination();

        if(tiles.size() == 1)
        {
            return tiles.get(0);
        }
        else if(tiles.contains(previousDestination))
        {
            return previousDestination;
        }
        else
        {
            return tiles.get(0);
        }
    }

    // ///////////////////////////////////////////////////////////////
    // PATH /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected AiPath processCurrentPath()
    {
        ai.checkInterruption();
        AiPath result = null;
        startLocation = new AiLocation(ownHero);

        if(processEscape)
        {
            try
            {
                result = dijkstra.processEscapePath(startLocation);
            }
            catch (LimitReachedException e)
            {
                result = null;
            }
        }

        if(result == null)
        {
            endTile = getCurrentDestination();
            /* Try for time based astar */
            try
            {
                result = astarTime.startProcess(startLocation, endTile);
            }
            catch (LimitReachedException e)
            {
                /* Time based astar failed */
                result = null;
            }

            if(result == null)
            {
                /* Try for approx based astar */
                try
                {
                    result = astarApprox.startProcess(startLocation, endTile);
                }
                catch (LimitReachedException e)
                {
                    result = new AiPath();
                }
            }
        }

        return result;
    }

    // ///////////////////////////////////////////////////////////////
    // DIRECTION /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected Direction processCurrentDirection()
    {
        ai.checkInterruption();
        Direction result = Direction.NONE;

        AiPath path = getCurrentPath();

        if(processEscape)
        {
            if(path != null && path.getLength() > 1)
            {
                AiTile next = path.getLocation(1).getTile();
                if(next.getFires().isEmpty())
                    return zone.getDirection(ownHero.getTile(), next);

            }
            else
            {
                processEscape = false;
            }
        }

        if(path == null || path.getLength() < 2 || path.getFirstPause() != 0)
        {
            result = Direction.NONE;
        }

        /* If we do not have to plant a bomb here */
        else
        {
            AiTile nextTile = path.getLocation(1).getTile();

            List<AiItem> items = nextTile.getItems();

            malus = false;
            if(!items.isEmpty())
            {
                AiItemType type = items.get(0).getType();
                if(type.isNoneKind() || type.isAntiKind())
                {
                    malus = true;
                }
            }

            /* If we have put a bomb, do not put another one */
            /* If next tile is a destructible block, put a bomb here */
            if(!nextTile.getBlocks().isEmpty() || malus)
            {
                /* If simulated path can found, we can put a bomb */
                if(canReachSafety())
                {
                    ai.bombHandler.consider = true;
                    processEscape = true;
                }
                else
                {
                    /* Do not put a bomb, do not move */
                    return Direction.NONE;
                }
            }
            if(nextTile.getFires().isEmpty())
                return zone.getDirection(ownHero.getTile(), nextTile);
        }
        /* Direction.NONE */
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
        // TimeHeuristicCalculator simTimeHeuristic = new
        // TimeHeuristicCalculator(
        // ai, ownSimHero);
        SuccessorCalculator simTimeSuccessor = new TimePartialSuccessorCalculator(
                ai, SearchMode.MODE_NOBRANCH);

        /* Simulate a bomb on our location */
        simZone.createBomb(ownTile, ownSimHero);

        Dijkstra dijkstraSim = new Dijkstra(ai, ownSimHero, simTimeCost,
                simTimeSuccessor);

        try
        {
            path = dijkstraSim.processEscapePath(start);
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

    // ///////////////////////////////////////////////////////////////
    // OUTPUT /////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    public void updateOutput()
    {
        ai.checkInterruption();

        // ici on se contente de faire le traitement par défaut
        super.updateOutput();
    }
}
