package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
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
        CostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
        HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
                ai, ownHero);
        SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
                ai, SearchMode.MODE_NOBRANCH);
        astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator,
                successorCalculator);
    }

    // ///////////////////////////////////////////////////////////////
    // DATA /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    /** Store current zone */
    private AiZone zone = null;
    /** Store own hero */
    private AiHero ownHero = null;
    /** Store astar */
    private Astar astar = null;

    // ///////////////////////////////////////////////////////////////
    // DESTINATION /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected AiTile processCurrentDestination()
    {
        ai.checkInterruption();

        AiPreferenceHandler<Agent> preferenceHandler = ai
                .getPreferenceHandler();
        Map<Integer, List<AiTile>> preferences = preferenceHandler
                .getPreferencesByValue();

        int minPref = Collections.min(preferences.keySet());
        List<AiTile> tiles = preferences.get(minPref);

        AiTile previousDestination = getCurrentDestination();

        if(previousDestination == ownHero.getTile())
        {
            return tiles.get(0);
        }
        else if(tiles.size() == 1)
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
        AiLocation startLocation = new AiLocation(ownHero);
        
        AiTile endTile = getCurrentDestination(); 
        try
        { 
            result = astar.startProcess(startLocation, endTile);
        }
        catch (LimitReachedException e)
        { 
            // Not implemented
            result = new AiPath();
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

        if(ai.bombHandler.considerBombing())
        {
            return zone.getDirection(ownHero.getTile(), path.getLocation(1)
                    .getTile());
        }
        else if(path == null || path.getLength() < 2
                || path.getFirstPause() != 0)
        {
            result = Direction.NONE;
        }
        else
        {
            AiLocation nextLocation = path.getLocation(1);

            AiTile nextTile = nextLocation.getTile();
            AiTile currentTile = ownHero.getTile();
            result = zone.getDirection(currentTile, nextTile);
        }

        return result;
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
