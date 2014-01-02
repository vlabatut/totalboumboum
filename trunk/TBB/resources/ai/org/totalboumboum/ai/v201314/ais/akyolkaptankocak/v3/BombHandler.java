package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.criterion.BlockageSecurity;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.criterion.Corridor;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.criterion.EnemyDistance;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.criterion.Security;

/**
 * This class handles the bombing decision. It decides whether our agent should
 * put a bomb or not.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */

public class BombHandler extends AiBombHandler<Agent>
{
    /**
     * Constructs a handler for the agent which passed by parameter. Initializes
     * variables
     * 
     * @param ai
     *            the agent that this class should handle
     */
    protected BombHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();

        /* Initialize zone */
        zone = ai.getZone();

        /* Initialize hero */
        ownHero = zone.getOwnHero();

        /* Initialize criterion */
        // Get EnemyDistance Criterion
        enemyCriterion = (EnemyDistance) ai.preferenceHandler
                .getCriterion("ENEMY_DISTANCE");

        // Get Corridor Criterion
        corridorCriteriton = (Corridor) ai.preferenceHandler
                .getCriterion("CORRIDOR");

        // Get Security Criterion
        securityCriterion = (Security) ai.preferenceHandler
                .getCriterion("SECURITY");

        blockageCriterion = (BlockageSecurity) ai.preferenceHandler
                .getCriterion("BLOCKAGE_SECURITY");
    }

    /** Will be raised by move handler in order to reduce calculation time */
    public boolean consider;

    /** Enemy criterion */
    public EnemyDistance enemyCriterion;

    /** Corridor criterion */
    public Corridor corridorCriteriton;

    /** Security criterion */
    public Security securityCriterion;

    /** Blockage criterion */
    public BlockageSecurity blockageCriterion;

    /** Own Hero */
    public AiHero ownHero;

    /** Zone */
    public AiZone zone;

    // ///////////////////////////////////////////////////////////////
    // PROCESSING /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected boolean considerBombing()
    {
        ai.checkInterruption();

        // Own Tile
        AiTile ownTile = ownHero.getTile();

        // If we haven't got any bombs to put, do not consider bombing
        if(ownHero.getBombNumberMax() <= ownHero.getBombNumberCurrent())
            return false;

        // If there is already a bomb, do not consider bombing
        if(ownTile.getBombs().size() > 0)
            return false;

        // If we have 8 seconds left
        if(zone.getLimitTime() - zone.getTotalTime() < 8000)
        {
            // Is enemy in our bomb's range and if we can reach safety
            if(ownHero.getBombPrototype().getBlast()
                    .contains(ai.preferenceHandler.target.getTile())
                    && ai.moveHandler.canReachSafety())
            {
                // Run away
                ai.moveHandler.processEscape = true;
                return true;
            }
        }

        // If consider flag has been raised
        if(consider)
        {
            consider = false;
            return true;
        }

        if(ai.modeHandler.getMode() == AiMode.ATTACKING)
        {
            // If our enemy is hard, apply blockage strategy
            if(ai.preferenceHandler.hardEnemy)
                return considerForBlockage(ownTile);

            // Apply corridor strategy
            else
                return considerForCorridor(ownTile);
        }

        else
        {
            return considerForCollect(ownTile);
        }
    }

    /**
     * Decision of considering when using blockage strategy
     * 
     * @param ownTile
     *            Our heros current tile
     * @return Decision of putting a bomb
     */
    private boolean considerForBlockage(AiTile ownTile)
    {
        ai.checkInterruption();

        // If there isn't enemy nearby
        if(ai.preferenceHandler.nearestEnemy == null)
            return false;

        AiTile enemyTile = ai.preferenceHandler.nearestEnemy.getTile();

        boolean corridor = corridorCriteriton.fetchValue(enemyTile);

        // If enemy is not in a corridor, do not consider bombing
        if(!corridor)
            return false;

        boolean blockage = blockageCriterion.fetchValue(ownTile);

        if(blockage)
        {
            if(ai.moveHandler.canReachSafety())
            {
                // Run away
                ai.moveHandler.processEscape = true;
                return true;
            }
        }

        return false;
    }

    /**
     * Decision of considering when using corridor strategy
     * 
     * @param ownTile
     *            Own Tile
     * @return true if considering, false else
     */
    private boolean considerForCorridor(AiTile ownTile)
    {
        ai.checkInterruption();

        if(ai.preferenceHandler.nearestEnemy == null)
            return false;

        if(ownHero.getBombPrototype().getBlast()
                .contains(ai.preferenceHandler.nearestEnemy.getTile()))
        {
            if(ai.moveHandler.canReachSafety())
            {
                // Run away
                ai.moveHandler.processEscape = true;
                return true;
            }
        }
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
        return false;
    }

    // ///////////////////////////////////////////////////////////////
    // OUTPUT /////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    /**
     * Update graphical output.
     */
    protected void updateOutput()
    {
        ai.checkInterruption();

    }
}
