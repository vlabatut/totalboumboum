package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * This class selects which tiles are we going to evaluate and identifies
 * categories on selected tiles.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{
    /**
     * Constructs a handler for the agent which passed by parameter.
     * 
     * @param ai
     *            the agent that this class should handle
     */
    protected PreferenceHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();

        zone = ai.getZone();
        ownHero = zone.getOwnHero();
    }

    /** Get Zone */
    public AiZone zone;
    /** Get Own Hero */
    public AiHero ownHero;
    /** Nearest Enemy */
    public AiHero nearestEnemy = null;
    /** Reachable destructible block flag */
    public boolean blockFlag;
    /** heroFlag */
    public boolean heroFlag;
    /** bomb flag */
    public boolean bombFlag;
    /** Enemy Ai situation */
    public boolean hardEnemy = true;
    /** Targeted enemy */
    public AiHero target = null;
    /** Enemy's bomb count */
    public int enemyBombCount = 0;
    /** Count flag */
    public boolean count = true;

    // ///////////////////////////////////////////////////////////////
    // DATA /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected void resetCustomData()
    {
        ai.checkInterruption();

        // cf. la Javadoc dans AiPreferenceHandler pour une description de la
        // méthode
    }

    // ///////////////////////////////////////////////////////////////
    // PROCESSING /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected Set<AiTile> selectTiles()
    {
        ai.checkInterruption();
        AiZone zone = ai.getZone();

        Set<AiTile> result = getReachableTiles();

        // Select enemy tiles
        if(!heroFlag && !bombFlag)
        {
            for(AiHero hero : zone.getRemainingOpponents())
            {
                ai.checkInterruption();
                AiTile heroTile = hero.getTile();
                result.add(heroTile);
            }
        }
        return result;
    }

    // ///////////////////////////////////////////////////////////////
    // CATEGORY /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    /** Name of the 1st category */
    private static String CAT_NAME_1 = "BLOCKAGE";
    /** Name of the 2nd category */
    private static String CAT_NAME_2 = "PASSIVE_BLOCKAGE";
    /** Name of the 3rd category */
    private static String CAT_NAME_3 = "BONUS";
    /** Name of the 4th category */
    private static String CAT_NAME_4 = "CORRIDOR";
    /** Name of the 5th category */
    private static String CAT_NAME_5 = "PASSIVE_CORRIDOR";

    @Override
    protected AiCategory identifyCategory(AiTile tile)
    {
        ai.checkInterruption();
        AiCategory result = null;
        AiHero ownHero = tile.getZone().getOwnHero();
        AiTile ownTile = ownHero.getTile();

        if(target == null)
        {
            List<AiHero> enemys = zone.getRemainingOpponents();
            if(enemys.size() == 1)
                target = enemys.get(0);
        }

        // If we are counting
        if(count)
        {
            // If 10sec is over don't count anymore
            if(zone.getTotalTime() > 10000)
                count = false;

            // If enemy hasn't put any bomb
            if(enemyBombCount == 0)
            {
                enemyBombCount += target.getBombNumberCurrent();
            }

            // If it has, don't count anymore
            if(enemyBombCount > 0)
            {
                count = false;
            }
        }

        // If we have done counting
        if(enemyBombCount == 0 && !count && hardEnemy)
        {
            hardEnemy = false;
        }

        // Attacking Mode
        if(ai.modeHandler.getMode() == AiMode.ATTACKING)
        {
            /* If we have max 3 bomb to put, apply blockage strategy */
            if(ownHero.getBombNumberMax() < 50)
            {
                if(hardEnemy)
                {
                    ArrayList<AiTile> nearTiles = getNearTiles(ownTile, 4);

                    if(nearTiles.size() < ownHero.getBombRange() + 2)
                        nearTiles = getNearTiles(ownTile,
                                ownHero.getBombRange() + 1);

                    if(nearTiles.contains(tile))
                    {
                        result = getCategory(CAT_NAME_1);
                    }
                    else
                    {
                        result = getCategory(CAT_NAME_2);
                    }
                }
                else
                {
                    if(getNearTiles(ownTile, ownHero.getBombRange() + 1)
                            .contains(tile))
                    {
                        result = getCategory(CAT_NAME_4);
                    }
                    else
                    {
                        result = getCategory(CAT_NAME_5);
                    }
                }
            }
        }

        // Collecting Mode
        else
        {
            List<AiItem> items = tile.getItems();

            // If tile has a bonus
            if(items.size() > 0 && items.get(0).getType().isBonus())
            {
                result = getCategory(CAT_NAME_3);
            }
            else
            {
                result = getCategory(CAT_NAME_5);
            }
        }

        return result;
    }

    /**
     * This method gives the accessible tiles from given tile in a certain
     * distance considering the obstacles using the "largeur d'abord" algorithm.
     * 
     * @param tile
     *            : Starting tile for measuring real distance.
     * @param distance
     *            : Integer value of the tile distance.
     * @return result : List which contains the tiles we can reach at (distance)
     *         steps.
     */
    public ArrayList<AiTile> getNearTiles(AiTile tile, int distance)
    {
        ai.checkInterruption();

        // List we will return
        ArrayList<AiTile> result = new ArrayList<AiTile>();
        // Our fringe
        Queue<AiTile> fringe = new LinkedList<AiTile>();

        // The step when comparing with distance
        int step = 0;

        // Get our Hero.
        AiHero ownHero = ai.getZone().getOwnHero();
        AiTile currentTile = tile;

        // Initialize both lists with our hero's current tile
        result.add(tile);
        fringe.add(tile);

        // Accessible tile counter array which depends how many steps we can
        // reach at it.
        int stepArray[] = new int[distance + 1];

        stepArray[0] = 1;
        for(int i = 1; i < stepArray.length; i++)
        {
            ai.checkInterruption();
            stepArray[i] = 0;
        }

        // heroFlag = false;
        // nearestEnemy = null;
        while ((currentTile = fringe.poll()) != null)
        {
            ai.checkInterruption();

            // Decrement counter for this step because we are processing
            // a tile at this level.
            stepArray[step]--;

            for(AiTile neighbor : currentTile.getNeighbors())
            {
                ai.checkInterruption();

                // If this neighbor is crossable by our hero and if doesn't
                // exist in return list, add it to both list
                if(neighbor.getItems().size() == 0
                        && neighbor.isCrossableBy(ownHero)
                        && !result.contains(neighbor))
                {
                    result.add(neighbor);
                    fringe.add(neighbor);

                    // Increment counter for the next step because we added a
                    // new tile at this level.
                    stepArray[step + 1]++;
                }
            }

            // if there isn't any tiles to proceed at this level, proceed to the
            // next level.
            if(stepArray[step] == 0)
            {
                step++;
            }

            // if we reach at final distance, return the list.
            if(step == distance)
            {
                return result;
            }
        }
        // Something is wrong, return an empty list
        return new ArrayList<AiTile>();
    }

    /**
     * This method uses the "largeur d'abord" algorithm to find accessible tiles
     * considering the obstacles from our hero's tile.
     * 
     * @return list contains the reachable tiles.
     */
    public Set<AiTile> getReachableTiles()
    {
        ai.checkInterruption();

        // List we will return
        Set<AiTile> result = new TreeSet<AiTile>();
        // Our fringe
        Queue<AiTile> fringe = new LinkedList<AiTile>();

        // Get own hero
        AiHero ownHero = ai.getZone().getOwnHero();
        AiTile currentTile = ownHero.getTile();

        // Initialize both lists with our hero's current tile
        result.add(currentTile);
        fringe.add(currentTile);

        // setting heroflag
        heroFlag = false;
        bombFlag = false;
        blockFlag = false;
        nearestEnemy = null;

        while ((currentTile = fringe.poll()) != null)
        {
            ai.checkInterruption();
            for(AiTile neighbor : currentTile.getNeighbors())
            {
                ai.checkInterruption();

                // If this neighbor is crossable by our hero and if doesn't
                // exist in return list, add it to both list
                if(neighbor.isCrossableBy(ownHero)
                        && !result.contains(neighbor))
                {
                    fringe.offer(neighbor);
                    result.add(neighbor);
                    if(!heroFlag)
                    {
                        List<AiHero> enemys = neighbor.getHeroes();
                        if(!enemys.isEmpty())
                        {
                            nearestEnemy = enemys.get(0);
                            heroFlag = true;
                        }
                    }
                }

                // If neighbor has a bomb or fire
                else if(!bombFlag)
                {
                    if(neighbor.getBombs().size() > 0
                            || neighbor.getFires().size() > 0)
                        bombFlag = true;
                }

                else if(!blockFlag)
                {
                    List<AiBlock> blocks = neighbor.getBlocks();
                    if(blocks.size() > 0 && blocks.get(0).isDestructible())
                    {
                        blockFlag = true;
                    }
                }
            }
        }
        return result;

    }

    // ///////////////////////////////////////////////////////////////
    // OUTPUT /////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    public void updateOutput()
    {
        ai.checkInterruption();

        // ici on se contente de faire le traitement par défaut
        super.updateOutput();

    }
}
