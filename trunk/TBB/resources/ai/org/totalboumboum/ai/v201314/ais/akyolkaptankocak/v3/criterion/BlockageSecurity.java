package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class represents the BlockageSecurity criterion. It determines whether
 * we can apply blockage strategy on this tile or not
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
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
            AiHero enemy = checkEnemy(neighbor, ownHero);

            /* If there is an enemy nearby */
            if(enemy != null)
            {
                AiTile enemyTile = enemy.getTile();
                Direction d = zone.getDirection(tile, enemyTile);
                AiTile n = enemyTile.getNeighbor(d);
                if(n.getBombs().size() > 0)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether there is an enemy and returns it
     * 
     * @param tile
     *            Current Tile
     * @param ownHero
     *            Own Hero
     * @return AiHero enemy if an enemy is present, else null
     */
    public AiHero checkEnemy(AiTile tile, AiHero ownHero)
    {
        ai.checkInterruption();

        List<AiHero> enemies = tile.getHeroes();

        int size = enemies.size();
        if(size == 0)
        {
            return null;
        }

        AiHero result = enemies.get(0);
        if(result != ownHero)
            return result;

        else if(size == 1)
            return null;

        else
            return enemies.get(1);
    }
}
