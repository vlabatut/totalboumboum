package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.ZoneHandler.TileProperty;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.criterion.BlockageSecurity;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.criterion.EnemyDistance;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.criterion.Security;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class handles the bombing decision. It decides whether our agent should
 * put a bomb or not.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
@SuppressWarnings("deprecation")
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

    /** Security criterion */
    public Security securityCriterion;

    /** Blockage criterion */
    public BlockageSecurity blockageCriterion;

    /** Own Hero */
    public AiHero ownHero;

    /** Zone */
    public AiZone zone;

    /** Calculates length of a corridor */
    public int step = 0;

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

        // If consider flag has been raised
        if(consider)
        {
            if(ai.pathHandler.canReachSafety())
            {
                ai.moveHandler.endCollect = ownHero.getTile();
                consider = false;
                return true;
            }
        }

        if(ai.modeHandler.getMode() == AiMode.ATTACKING)
        {

            // Apply blockage strategy
            if(considerForBlockage(ownTile))
            {
                ai.pathHandler.timeSuccessor.setConsiderOpponents(false);
                return true;
            }

            // Apply corridor strategy
            else
                return considerForCorridor(ownTile);
        }

        return false;
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
        if(ai.zoneHandler.nearestEnemy == null)
            return false;

        boolean blockage = blockageCriterion.fetchValue(ownTile);

        if(blockage)
        {
            if(ai.pathHandler.canReachSafety())
            {
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

        if(ai.zoneHandler.nearestEnemy == null)
            return false;

        /* Get bomb range */
        List<AiTile> bombRange = ownHero.getBombPrototype().getBlast();
        AiTile deadEnd = null;
        for(AiHero opponent : zone.getRemainingOpponents())
        {
            ai.checkInterruption();

            AiTile enemyTile = opponent.getTile();

            TileProperty enemyProperty = ai.zoneHandler.map.get(enemyTile);

            if(enemyProperty != null
                    && enemyProperty.distance == Integer.MAX_VALUE)
                continue;

            /* If we are in the same tile do not put a bomb */
            if(enemyTile == ownTile)
                continue;

            /* If enemy is not in a corridor, do not put a bomb */
            int walls = enemyProperty.walls;
            if(walls == 0)
                continue;

            else if(walls == 3)
            {
                deadEnd = enemyTile;
            }

            /* If our bomb wont reach to enemy */
            if(!bombRange.contains(enemyTile))
            {
                enemyTile = getBombIntersection(ownTile, enemyTile, bombRange);
                if(enemyTile == null)
                {
                    continue;
                }
            }

            /* If enemy is in a deadEnd */
            else if(bombRange.contains(enemyTile))
            {
                boolean reachSafety = ai.pathHandler.canReachSafety();
                if(deadEnd != null)
                {
                    if(reachSafety)
                    {
                        ai.pathHandler.timeSuccessor.setConsiderOpponents(true);
                        return true;
                    }
                }
                if(!ai.preferenceHandler.hardEnemy)
                {
                    if(reachSafety)
                    {
                        ai.pathHandler.timeSuccessor.setConsiderOpponents(true);
                        return true;
                    }
                }
            }

            step = 0;
            deadEnd = findDeadEnd(ownTile, enemyTile);

            /* If we haven't found a deadEnd */
            if(deadEnd == null)
            {
                int enemyEscape = step
                        - ai.zoneHandler.map
                                .get(ai.zoneHandler.nearestEnemyTile).distance
                        - 2;
                double enemySpeed = ai.zoneHandler.nearestEnemy
                        .getWalkingSpeed() / AiTile.getSize();

                double bombDuration = (double) (ownHero.getBombDuration() / 1000.0);

                if(enemyEscape > 0
                        && (double) (enemyEscape / enemySpeed) >= bombDuration - 0.1)
                {
                    if(ai.pathHandler.canReachSafety())
                    {
                        ai.pathHandler.timeSuccessor.setConsiderOpponents(true);
                        return true;
                    }
                }

            }
            else if(bombRange.contains(deadEnd))
            {
                if(ai.pathHandler.canReachSafety())
                {
                    ai.pathHandler.timeSuccessor.setConsiderOpponents(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Search a dead end for an enemy A dead end could be the end of a corridor.
     * If enemy is in a corridor and it has a dead end, we can put a bomb
     * 
     * @param ownTile
     *            Our hero's tile
     * @param enemyTile
     *            Enemy's tile
     * @return deadEnd if found, null else
     */
    public AiTile findDeadEnd(AiTile ownTile, AiTile enemyTile)
    {
        ai.checkInterruption();

        Direction direction = ai.pathHandler
                .simpleDirection(ownTile, enemyTile);

        Direction nextPrimary = direction.getNextPrimary();
        Direction previousPrimary = direction.getPreviousPrimary();

        AiTile current = ownTile;
        AiTile neighbor = current.getNeighbor(direction);

        AiTile deadEnd = null;

        boolean exit = false;
        while (!exit
                && step++ < Math.max(ai.zoneHandler.mapHeight,
                        ai.zoneHandler.mapWidth))
        {
            ai.checkInterruption();

            if(neighbor.getBombs().size() > 0 || neighbor.getFires().size() > 0)
            {
                deadEnd = current;
                break;
            }

            TileProperty tp = ai.zoneHandler.map.get(neighbor);

            /*
             * If there is no mapping, this is a non-destructible tile and there
             * is no deadEnd
             */
            if(tp == null)
            {
                break;
            }

            try
            {
                switch (tp.walls)
                {
                    case 0:
                    {
                        AiTile forward = neighbor.getNeighbor(direction);
                        AiTile next = neighbor.getNeighbor(nextPrimary);
                        AiTile previous = neighbor.getNeighbor(previousPrimary);

                        int count = 3;
                        boolean resume = false;

                        if(!securityCriterion.fetchValue(next))
                        {
                            count--;
                        }

                        if(!securityCriterion.fetchValue(previous))
                        {
                            count--;
                        }

                        tp = ai.zoneHandler.map.get(forward);
                        if(!securityCriterion.fetchValue(forward))
                        {
                            count--;
                        }
                        else if(count == 1 && tp.walls == 2)
                        {
                            resume = true;
                        }

                        if(count == 0)
                        {
                            deadEnd = neighbor;
                            exit = true;
                        }
                        else if(resume)
                        {
                            /* If walls == 2, this is a corridor */
                            neighbor = neighbor.getNeighbor(direction);
                            current = current.getNeighbor(direction);
                            exit = false;
                        }
                        else
                        {
                            exit = true;
                        }
                    }
                        break;

                    case 1:
                    {
                        AiTile forward = neighbor.getNeighbor(direction);
                        AiTile next = neighbor.getNeighbor(nextPrimary);
                        AiTile previous = neighbor.getNeighbor(previousPrimary);

                        /* One sides are open */
                        int count = 2;
                        boolean resume = false;

                        if(isCrossable(next))
                        {
                            if(!securityCriterion.fetchValue(next))
                                count--;
                        }

                        if(isCrossable(previous))
                        {
                            if(!securityCriterion.fetchValue(previous))
                                count--;
                        }

                        tp = ai.zoneHandler.map.get(forward);
                        if(isCrossable(forward))
                        {
                            if(!securityCriterion.fetchValue(forward))
                                count--;
                            else if(count == 1 && tp.walls == 2)
                                resume = true;
                        }

                        if(count == 0)
                        {
                            deadEnd = neighbor;
                            exit = true;
                        }
                        else if(resume)
                        {
                            /* If walls == 2, this is a corridor */
                            neighbor = neighbor.getNeighbor(direction);
                            current = current.getNeighbor(direction);
                            exit = false;
                        }
                        else
                        {
                            exit = true;
                        }

                    }
                        break;
                    case 2:
                    {
                        /* If walls == 2, this is a corridor */
                        neighbor = neighbor.getNeighbor(direction);
                        current = current.getNeighbor(direction);

                        /* Can enemy escape from sides */
                        if(!isCrossable(neighbor))
                        {
                            /* Direction is full, enemy could escape? */
                            AiTile next = current.getNeighbor(nextPrimary);
                            AiTile previous = current
                                    .getNeighbor(previousPrimary);

                            /* One side is open */
                            int count = 1;

                            if(isCrossable(next))
                            {
                                if(!securityCriterion.fetchValue(next))
                                    count--;
                            }

                            if(isCrossable(previous))
                            {
                                if(!securityCriterion.fetchValue(previous))
                                    count--;
                            }

                            if(count == 0)
                            {
                                deadEnd = current;
                                exit = true;
                            }
                        }
                    }
                        break;
                    case 3:
                    {
                        /* We have found a deadEnd */
                        deadEnd = neighbor;
                        exit = true;
                    }
                        break;

                    case 4:
                    {
                        /*
                         * We have exited or find a wall, maybe previous tile
                         * was a deadEnd?
                         */
                        AiTile next = current.getNeighbor(nextPrimary);
                        AiTile previous = current.getNeighbor(previousPrimary);

                        int count = 1;

                        if(isCrossable(next))
                        {
                            if(!securityCriterion.fetchValue(next))
                                count--;
                        }

                        if(count > 0 && isCrossable(previous))
                        {
                            if(!securityCriterion.fetchValue(previous))
                                count--;
                        }

                        if(count == 0)
                        {
                            deadEnd = neighbor;
                        }

                        exit = true;
                    }
                        break;
                }
            }
            catch (NullPointerException n)
            {
                ai.checkInterruption();

                deadEnd = null;
                exit = true;
            }
        }

        return deadEnd;
    }

    /**
     * An intersection is the contact point with enemy and our hero
     * 
     * @param ownTile
     *            Our hero's tile
     * @param enemy
     *            Enemy's tile
     * @param bombRange
     *            Our bomb's range
     * @return intersection tile if found, null else
     */
    public AiTile getBombIntersection(AiTile ownTile, AiTile enemy,
            List<AiTile> bombRange)
    {
        ai.checkInterruption();

        if(ownTile.getCol() != enemy.getCol()
                && ownTile.getRow() != enemy.getRow())
        {
            AiTile contact1 = zone.getTile(ownTile.getRow(), enemy.getCol());
            AiTile contact2 = zone.getTile(enemy.getRow(), ownTile.getCol());
            TileProperty cp1 = ai.zoneHandler.map.get(contact1);
            TileProperty cp2 = ai.zoneHandler.map.get(contact2);

            if(cp1 == null && cp2 == null)
            {
                return null;
            }
            else if(cp1 == null)
            {
                int manDist = ai.pathHandler.simpleTileDistance(ownTile,
                        contact2);
                if(manDist == cp2.distance && bombRange.contains(contact2))
                {
                    Direction to = ai.pathHandler.simpleDirection(enemy,
                            contact2);
                    AiTile start = enemy.getNeighbor(to);
                    while (start != contact2)
                    {
                        ai.checkInterruption();

                        if(!isCrossable(start))
                            return null;
                        start = start.getNeighbor(to);
                    }
                    return contact2;
                }
            }
            else if(cp2 == null)
            {
                int manDist = ai.pathHandler.simpleTileDistance(ownTile,
                        contact1);
                if(manDist == cp1.distance && bombRange.contains(contact1))
                {
                    Direction to = ai.pathHandler.simpleDirection(enemy,
                            contact1);
                    AiTile start = enemy.getNeighbor(to);
                    while (start != contact1)
                    {
                        ai.checkInterruption();

                        if(!isCrossable(start))
                            return null;
                        start = start.getNeighbor(to);
                    }
                    return contact1;
                }
            }
            else
            {
                int manDist1 = ai.pathHandler.simpleTileDistance(ownTile,
                        contact1);
                int manDist2 = ai.pathHandler.simpleTileDistance(ownTile,
                        contact2);

                if(cp1.distance != manDist1 && cp2.distance != manDist2)
                {
                    return null;
                }

                if(cp1.distance != manDist1)
                {
                    if(bombRange.contains(contact2))
                    {
                        Direction to = ai.pathHandler.simpleDirection(enemy,
                                contact2);
                        AiTile start = enemy.getNeighbor(to);
                        while (start != contact2)
                        {
                            ai.checkInterruption();

                            if(!isCrossable(start))
                                return null;
                            start = start.getNeighbor(to);
                        }
                        return contact2;
                    }
                    else
                        return null;
                }

                else if(cp2.distance != manDist2)
                {
                    if(bombRange.contains(contact1))
                    {
                        Direction to = ai.pathHandler.simpleDirection(enemy,
                                contact1);
                        AiTile start = enemy.getNeighbor(to);
                        while (start != contact1)
                        {
                            ai.checkInterruption();

                            if(!isCrossable(start))
                                return null;
                            start = start.getNeighbor(to);
                        }
                        return contact1;
                    }
                    else
                        return null;
                }

                else
                // if(!bombRange.contains(contact1) &&
                // !bombRange.contains(contact2))
                {
                    if(cp1.distance <= cp2.distance)
                    {
                        Direction to = ai.pathHandler.simpleDirection(enemy,
                                contact1);
                        AiTile start = enemy.getNeighbor(to);
                        while (start != contact1)
                        {
                            ai.checkInterruption();

                            if(!isCrossable(start))
                                return null;
                            start = start.getNeighbor(to);
                        }
                        return contact1;
                    }
                    else if(cp2.distance < cp1.distance)
                    {
                        Direction to = ai.pathHandler.simpleDirection(enemy,
                                contact2);
                        AiTile start = enemy.getNeighbor(to);
                        while (start != contact2)
                        {
                            ai.checkInterruption();

                            if(!isCrossable(start))
                                return null;
                            start = start.getNeighbor(to);
                        }
                        return contact2;
                    }
                }
            }
        }
        else
        {
            if(!securityCriterion.fetchValue(enemy))
                return enemy;
        }

        return null;
    }

    /**
     * Our isCrossable function
     * 
     * @param tile
     *            Tile
     * @return boolean isCrossable
     */
    public boolean isCrossable(AiTile tile)
    {
        ai.checkInterruption();
        if(tile.getBlocks().size() > 0 || tile.getBombs().size() > 0
                || tile.getFires().size() > 0)
            return false;

        return true;
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
