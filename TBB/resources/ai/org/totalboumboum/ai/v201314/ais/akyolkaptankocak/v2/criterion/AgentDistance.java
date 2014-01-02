package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v2.Agent;

/**
 * This class represents the Agent Distance criterion.
 * It checks whether our hero is on the specified tile or
 * one of its neighbors, or not.
 * Thus, this criterion is binary.
 *
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
public class AgentDistance extends AiCriterionBoolean<Agent>
{
    /** Name of the criterion */
    public static final String NAME = "AGENT_DISTANCE";

    /**
     * Agent Distance criterion, binary
     *
     * @param ai
     *            Our agent (black).
     */
    public AgentDistance(Agent ai)
    {
        super(ai, NAME);
        ai.checkInterruption();
    }

    /////////////////////////////////////////////////////////////////
    // PROCESS                  /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    public Boolean processValue(AiTile tile)
    {
        ai.checkInterruption();

        // Get own hero
        AiHero ownHero = ai.getZone().getOwnHero();

        // Check if this tile has our hero
        if(tile.getHeroes().contains(ownHero))
            return true;

        // Check all neighbors to find our hero
        for(AiTile aiTile : tile.getNeighbors())
        {
            ai.checkInterruption();
            if(aiTile.getHeroes().contains(ownHero))
                return true;
        }

        // Our hero is far away
        return false;
    }
}
