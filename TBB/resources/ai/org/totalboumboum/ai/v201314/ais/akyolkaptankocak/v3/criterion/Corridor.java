package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.Agent;

/**
 * This class represents the Corridor criterion. A tile is a corridor if 2 or
 * more of its neighbors are blocks. This criterion checks whether the specified
 * tile is corridor or not. Thus its binary.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */

public class Corridor extends AiCriterionBoolean<Agent>
{
    /** Name of the criterion */
    public static final String NAME = "CORRIDOR";

    /**
     * Corridor criterion, binary
     * 
     * @param ai
     *            Our agent (black).
     */
    public Corridor(Agent ai)
    {
        super(ai, NAME);
        ai.checkInterruption();
        zone = ai.getZone();
        ownHero = zone.getOwnHero();
    }

    /** Zone */
    public AiZone zone;

    /** Own Hero */
    public AiHero ownHero;

    // ///////////////////////////////////////////////////////////////
    // PROCESS /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    public Boolean processValue(AiTile tile)
    {
        ai.checkInterruption();
        int count = 0;

        // Get all neighbors
        List<AiTile> neighbors = tile.getNeighbors();

        // Check corridors for blocks,fires,bombs
        for(AiTile aiTile : neighbors)
        {
            ai.checkInterruption();
            count += aiTile.getBlocks().size();
            count += aiTile.getBombs().size();
            count += aiTile.getFires().size();
        }

        // If a case has 2 or more block neighbors, it is a corridor
        return count > 1;
    }
}
