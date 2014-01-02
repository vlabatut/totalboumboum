package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v1.Agent;

/**
 * This class represents th Enemy Distance criterion.
 * It checks whether an enemy is on the specified tile or
 * on one of its neighbors or not.
 * Thus, this criterion is integer. It evaluates the tile with 0,1
 * or 2 respectively.
 *
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
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

    /////////////////////////////////////////////////////////////////
    // PROCESS                  /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    public Integer processValue(AiTile tile)
    {
        ai.checkInterruption();

        /* Get own hero */
        AiHero ownHero = ai.getZone().getOwnHero();

        /* If current case has an enemy, return 0 */
        if(checkEnemy(tile, ownHero))
            return 0;

        /* Check neighbors for enemy occurrence, if any return 1 */
        for(AiTile aiTile : tile.getNeighbors())
        {
            ai.checkInterruption();
            if(checkEnemy(aiTile, ownHero))
                return 1;
        }

        /* No enemy found */
        return 2;
    }

    /**
     * This method checks whether a hero is on the specified tile.
     * If there is none, it returns false.
     * If there is more than 1, it returns true, because that means
     * it definitely has an enemy.
     * If there is only one hero, it could be ours. So we check whether
     * that is our hero or an enemy.
     *
     * @param tile
     *            Current tile
     * @param ownHero
     *            Own hero
     * @return
     *            Is there any enemy in this case?
     *            false : if there isn't any
     *            true : if there is one or more
     */
    public boolean checkEnemy(AiTile tile, AiHero ownHero)
    {
        ai.checkInterruption();

        List<AiHero> enemies = tile.getHeroes();

        /* If tile has 0 heroes */
        if(enemies.isEmpty())
            return false;
        /* If tile has more than 1 hero, that means it definitely has an enemy */
        else if(enemies.size() > 1)
            return true;
        /* If tile has one hero, check if it is ours */
        else
            return !enemies.contains(ownHero);
    }
}
