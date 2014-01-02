package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.Agent;

/**
 * This class represents the Corridor criterion. A tile is a corridor if 2 or
 * more of its neighbors are blocks. This criterion checks whether the specified
 * tile is corridor or not. Thus its binary.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */

public class Corridor extends AiCriterionInteger<Agent>
{
    /** Name of the criterion */
    public static final String NAME = "CORRIDOR";

    /**
     * Corridor criterion, integer
     * 
     * @param ai
     *            Our agent (black).
     */
    public Corridor(Agent ai)
    {
        super(ai, NAME, 0, 2);
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
    public Integer processValue(AiTile tile)
    {
        ai.checkInterruption();

        int count = ai.zoneHandler.map.get(tile).walls;

        if(count < 2)
            return 0;

        else if(count == 2)
            return 1;

        else
            return 2;
    }
}
