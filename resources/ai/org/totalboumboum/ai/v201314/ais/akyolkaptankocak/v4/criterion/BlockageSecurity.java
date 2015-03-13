package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.ZoneHandler.TileProperty;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.Agent;

/**
 * This class represents the BlockageSecurity criterion. It determines whether
 * we can apply blockage strategy on this tile or not
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
@SuppressWarnings("deprecation")
public class BlockageSecurity extends AiCriterionBoolean<Agent>
{
    /** Name of the criterion */
    public static final String NAME = "BLOCKAGE_SECURITY";
    /**
     * Blockage Security criterion, binary
     * 
     * @param ai
     *            Our agent (black).
     */
    public BlockageSecurity(Agent ai)
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

        if(ownHero.getBombNumberMax() == ownHero.getBombNumberCurrent())
            return false;

        if(tile.getBombs().size() > 0)
            return false;

        for(AiTile neighbor : tile.getNeighbors())
        {
            ai.checkInterruption();
            AiHero enemy = ai.criterionHandler.fetchEnemy(neighbor, ownHero);

            /* If there is an enemy nearby */
            if(enemy != null)
            {
                AiTile enemyTile = enemy.getTile();

                TileProperty tp = ai.zoneHandler.map.get(enemyTile);
                if(tp!=null && tp.walls == 3)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
