package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.Agent;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.ZoneHandler.TileProperty;

/**
 * This class represents the Security criterion. A tile is secure if: - There
 * isn't any malus on it - It isn't on a bomb's range - It hasn't got any flames
 * on it - It isn't going to be a indestructible block. This class checks
 * whether specified tile is secure or not. Thus this criterion is binary.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
@SuppressWarnings("deprecation")
public class Security extends AiCriterionBoolean<Agent>
{
    /** Name of the criterion */
    public static final String NAME = "SECURITY";

    /**
     * Security criterion, binary
     * 
     * @param ai
     *            Our agent (black).
     */
    public Security(Agent ai)
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

        // If this tile is dangerous
        if(ai.criterionHandler.getDangerousTiles().contains(tile))
            return false;

        boolean enemy = false, deadEnd = false;
        for(AiTile neighbor : tile.getNeighbors())
        {
            ai.checkInterruption();

            TileProperty tp = ai.zoneHandler.map.get(neighbor);

            if(tp != null && tp.walls != 4)
            {
                if(tp.walls == 3)
                    deadEnd = true;

                else if(ai.criterionHandler.checkEnemy(neighbor, ownHero))
                {
                    enemy = true;
                }
            }
        }

        if(enemy && deadEnd)
            return false;
        // If this tile is secure
        return true;
    }
}
