package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * This class is responsible from zone's tiles. It calculates every tile's
 * distance to us. It helps selection of tiles.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
@SuppressWarnings("deprecation")
public class ZoneHandler extends AiAbstractHandler<Agent>
{

    /** Zone */
    public AiZone zone;
    /** Own Hero */
    public AiHero ownHero;

    /** Whether a map has destructible tiles or not */
    public boolean hasDestructible = false;

    /** Store the nearest enemy */
    public AiHero nearestEnemy = null;

    /** Store the nearest enemys tile */
    public AiTile nearestEnemyTile = null;

    /** Reachable destructible block flag */
    public boolean blockFlag;
    /** heroFlag */
    public boolean heroFlag;
    /** bomb flag */
    public boolean bombFlag;

    /** Bonus flag */
    public boolean bonusFlag;

    /** Speed bonus is appeared */
    public boolean bonusSpeedFlag;

    /** Range bonus is appeared */
    public boolean bonusRangeFlag;

    /** Bomb bonus is appeared */
    public boolean bonusBombFlag;

    /** The map that keeps all tiles properties */
    public HashMap<AiTile, TileProperty> map;

    /** Store nearest bonus */
    public AiItem nearestBonus;

    /** Keep nearest enemy's distance */
    public int minEnemyDistance = Integer.MAX_VALUE;

    /** Store zone's heigth */
    public int mapHeight;
    /** Store zone's width */
    public int mapWidth;

    /**
     * Initializes variables
     * 
     * @param ai
     *            Agent
     * 
     */
    protected ZoneHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();

        zone = ai.getZone();
        ownHero = zone.getOwnHero();
        map = new HashMap<AiTile, TileProperty>();

        for(AiTile tile : zone.getTiles())
        {
            ai.checkInterruption();
            List<AiBlock> blocks = tile.getBlocks();
            if(blocks.isEmpty())
            {
                map.put(tile, new TileProperty());
            }
            else if(blocks.get(0).isDestructible())
            {
                hasDestructible = true;
                TileProperty tp = new TileProperty();
                tp.walls = 4;
                map.put(tile, tp);
            }
        }

        mapHeight = zone.getHeight();
        mapWidth = zone.getWidth();
    }

    /**
     * Class for storing a tile's values
     * 
     * @author Fırat Akyol
     * @author Mustafa Kaptan
     * @author Gökberk Koçak
     */
    public class TileProperty
    {
        /** Distance of a tile to us */
        public int distance = Integer.MAX_VALUE;
        /** Wall number of a tile */
        public int walls = 0;
        /** Bonus value of a tile */
        public int bonus = 0;
    }

    /**
     * This function helps tile selection It fills this.map
     * 
     * @return selected tiles
     */
    public Set<AiTile> tileSelection()
    {
        ai.checkInterruption();

        /* List we will return to selectTiles() */
        Set<AiTile> result = new HashSet<AiTile>();
        /* Our fringe */
        Queue<AiTile> fringe = new LinkedList<AiTile>();

        /* Reset flags */
        bonusFlag = false;
        heroFlag = false;
        bombFlag = false;
        nearestEnemy = null;
        bonusSpeedFlag = false;
        bonusRangeFlag = false;
        bonusBombFlag = false;

        /* Initialize both lists with our hero's current tile */
        AiTile ownTile = ownHero.getTile();
        result.add(ownTile);
        fringe.offer(ownTile);

        /* Our hero's current tile's distance is 0 */
        int distance = 0;
        map.get(ownTile).distance = distance;
        map.get(ownTile).bonus = 0;

        /* If we haven't find an enemy yet */

        List<AiHero> enemys = ownTile.getHeroes();
        if(enemys.size() > 1)
        {
            int i = 0;
            while ((nearestEnemy = enemys.get(i++)) != ownHero)
            {
                ai.checkInterruption();
            }
            nearestEnemyTile = nearestEnemy.getTile();
            heroFlag = true;
        }

        /* Increase distance (level) */
        distance++;
        int step = 1;

        /* Begin iteration */
        while (!fringe.isEmpty())
        {
            ai.checkInterruption();

            AiTile currentTile = fringe.poll();

            /* Reset walls to 0 */
            map.get(currentTile).walls = 0;

            for(AiTile neighbor : currentTile.getNeighbors())
            {
                ai.checkInterruption();

                /* If this neighbor is crossable by our hero */
                if(neighbor.isCrossableBy(ownHero))
                {
                    /* If it doesn't exist in return list, add it to both list */
                    if(!result.contains(neighbor))
                    {
                        result.add(neighbor);
                        fringe.offer(neighbor);

                        /* Set its distance */
                        map.get(neighbor).distance = distance;

                        /* Set its bonus */
                        List<AiItem> items = neighbor.getItems();
                        if(!items.isEmpty())
                        {
                            AiItem item = items.get(0);
                            boolean bon = item.getType().isBonus();

                            if(bon)
                            {
                                map.get(neighbor).bonus = 1;
                                // bonus type considaration
                                AiItemType itemType = item.getType();
                                if(itemType.isBombKind())
                                    bonusBombFlag = true;
                                else if(itemType.isFlameKind())
                                    bonusRangeFlag = true;
                                else if(itemType.isSpeedKind())
                                    bonusSpeedFlag = true;
                            }
                            else
                                map.get(neighbor).bonus = -1;

                            /* If we haven't find a bonus yet */
                            if(!bonusFlag && bon)
                            {
                                bonusFlag = true;
                                nearestBonus = item;
                            }
                        }
                        else
                        {
                            map.get(neighbor).bonus = 0;
                        }

                        /* If we haven't find an enemy yet */
                        if(!heroFlag)
                        {
                            enemys = neighbor.getHeroes();
                            if(!enemys.isEmpty())
                            {
                                nearestEnemy = enemys.get(0);
                                nearestEnemyTile = nearestEnemy.getTile();
                                heroFlag = true;
                            }
                        }
                    }
                }
                /* If this neighbor is NOT crossable by our hero */
                else
                {
                    if(neighbor.getFires().isEmpty())
                    {
                        /* Increase wall number of current tile */
                        map.get(currentTile).walls++;
                    }
                    /* Check if there is not a bomb yet */
                    if(!bombFlag)
                    {
                        if(!neighbor.getBombs().isEmpty()
                                || !neighbor.getFires().isEmpty())
                        {
                            bombFlag = true;
                        }

                    }

                    /* Check if there is a destructible block */
                    if(!blockFlag && hasDestructible)
                    {
                        List<AiBlock> blocks = neighbor.getBlocks();
                        if(!blocks.isEmpty() && blocks.get(0).isDestructible())
                        {
                            blockFlag = true;
                        }
                    }
                }
            }

            /* Check if there is not a bomb yet */
            if(!bombFlag)
            {
                if(!currentTile.getBombs().isEmpty())
                {
                    bombFlag = true;
                }
            }

            step--;
            if(step == 0)
            {
                step = fringe.size();
                /* Increase distance (level) */
                distance++;
            }
        }
        return result;
    }
}
