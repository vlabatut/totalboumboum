package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v2.criterion.*;

/**
 * This class handles the bombing decision.
 * It decides whether our agent should put a bomb or not.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */

public class BombHandler extends AiBombHandler<Agent>
{
    /**
     * Constructs a handler for the agent which passed by parameter.
     * 
     * @param ai
     *            the agent that this class should handle
     */
    protected BombHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();

    }

    // ///////////////////////////////////////////////////////////////
    // PROCESSING /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected boolean considerBombing()
    {
        ai.checkInterruption();

        // Get own tile
        AiTile ownTile = ai.getZone().getOwnHero().getTile();

        // If there is already a bomb, consider bombing
        if(ownTile.getBombs().size() > 0)
            return false;

        if(ai.modeHandler.getMode() == AiMode.ATTACKING)
        {
            return considerForAttack(ownTile);
        }

        else
        {
            return considerForCollect(ownTile);
        }
    }

    /**
     * If we are in the attacking mode, this function will be used for
     * calculating considerBombing. First, check whether an enemy is near to us
     * or not. Then, check whether our tile is a corridor or not. Finally, check
     * whether our tile is secure or not and calculate the result according to
     * strategy.
     * 
     * @param ownTile
     *            Our heros current tile
     * @return Decision of putting a bomb
     */
    private boolean considerForAttack(AiTile ownTile)
    {
        ai.checkInterruption();

        // Get EnemyDistance Criterion for own tile
        EnemyDistance enemyCriterion = (EnemyDistance) ai.preferenceHandler
                .getCriterion("ENEMY_DISTANCE");
        boolean enemy = enemyCriterion.fetchValue(ownTile).equals(2);

        // If enemy is far away, don't consider bombing
        if(enemy)
            return false;

        // Get Corridor Criterion for own tile
        Corridor corridorCriteriton = (Corridor) ai.preferenceHandler
                .getCriterion("CORRIDOR");
        boolean corridor = corridorCriteriton.fetchValue(ownTile);

        // If we are in the beginning of a corridor, consider bombing
        if(!corridor)
            return true;

        // Get Security Criterion for own tile
        Security securityCriterion = (Security) ai.preferenceHandler
                .getCriterion("SECURITY");
        boolean security = securityCriterion.fetchValue(ownTile);

        // If corridor is secure, consider bombing
        if(security)
            return true;

        return false;
    }

    /**
     * If we are in the collect mode, this function will be used for calculating
     * considerBombing.
     * 
     * -Not implemented yet
     * 
     * @param ownTile
     *            Our heros current tile
     * @return Decision of putting a bomb
     */
    private boolean considerForCollect(AiTile ownTile)
    {
        ai.checkInterruption();
        // Not implemented yet
        return false;
    }

    // ///////////////////////////////////////////////////////////////
    // OUTPUT /////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    /**
     * Met à jour la sortie graphique.
     */
    protected void updateOutput()
    {
        ai.checkInterruption();

    }
}
