package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.Agent;

/**
 * This class represents the Enemy Distance criterion. It checks whether an
 * enemy is on the specified tile or on one of its neighbors or not. Thus, this
 * criterion is integer. It evaluates the tile with 0,1 or 2 respectively.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
@SuppressWarnings("deprecation")
public class EnemyDistance extends AiCriterionInteger<Agent>
{
    /** Name of the criterion */
    public static final String NAME = "ENEMY_DISTANCE";

    /**
     * Enemy Distance criterion, integer
     * 
     * @param ai
     *            Our agent (black).
     */
    public EnemyDistance(Agent ai)
    {
        super(ai, NAME, 0, 2);
        ai.checkInterruption();
    }

    // ///////////////////////////////////////////////////////////////
    // PROCESS /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    public Integer processValue(AiTile tile)
    {
        ai.checkInterruption();

        /* Get own hero */
        AiHero ownHero = ai.getZone().getOwnHero();

        /* If current case has an enemy, return 0 */
        if(ai.criterionHandler.checkEnemy(tile, ownHero))
            return 0;

        /* Check neighbors for enemy occurrence, if any return 1 */
        for(AiTile aiTile : tile.getNeighbors())
        {
            ai.checkInterruption();
            if(ai.criterionHandler.checkEnemy(aiTile, ownHero))
                return 1;
        }

        /* No enemy found */
        return 2;
    }
}
