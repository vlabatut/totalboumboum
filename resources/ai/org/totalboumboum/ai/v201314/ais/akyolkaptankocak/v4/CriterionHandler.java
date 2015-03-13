/**
 *
 */
package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * This class helps criterion selection
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
@SuppressWarnings("deprecation")
public class CriterionHandler extends AiAbstractHandler<Agent>
{

    /** Zone */
    public AiZone zone;

    /** Own Hero */
    public AiHero ownHero;

    /**
     * Initialize variables
     * @param ai Agent
     */
    protected CriterionHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();

        zone = ai.getZone();

        ownHero = zone.getOwnHero();
    }

    

    /**
     * Check which tiles are dangerous. A tile is dangerous if - It is in range
     * of a bomb - It is burning - It has a bad bonus (malus)
     * 
     * @return ArrayList<AiTile> dangerousTiles list of (dangerous) tiles
     */
    public List<AiTile> getDangerousTiles()
    {
        ai.checkInterruption();

        // Create a list for dangerous tiles
        List<AiTile> dangerousTiles = new ArrayList<AiTile>();

        // Check for each bomb
        for(AiBomb currentBomb : zone.getBombs())
        {
            ai.checkInterruption();
            // Add each bomb's range
            dangerousTiles.addAll(currentBomb.getBlast());
        }

        // Check for every flame
        for(AiFire currentFire : zone.getFires())
        {
            ai.checkInterruption();
            dangerousTiles.add(currentFire.getTile());
        }

        // Check for every malus
        for(AiItem currentItem : this.ai.getZone().getItems())
        {
            ai.checkInterruption();

            switch (currentItem.getType())
            {
                case ANTI_BOMB:
                case ANTI_FLAME:
                case ANTI_SPEED:
                case NO_BOMB:
                case NO_FLAME:
                case NO_SPEED:
                case RANDOM_NONE:
                    dangerousTiles.add(currentItem.getTile());
                    break;
            }
        }

        for(AiSuddenDeathEvent e : zone.getAllSuddenDeathEvents())
        {
            ai.checkInterruption();
            if(zone.getTotalTime() + 1250 >= e.getTime())
                dangerousTiles.addAll(e.getTiles());
        }

        return dangerousTiles;
    }
    
    /**
     * This method checks whether a hero is on the specified tile. If there is
     * none, it returns false. If there is more than 1, it returns true, because
     * that means it definitely has an enemy. If there is only one hero, it
     * could be ours. So we check whether that is our hero or an enemy.
     *
     * @param tile
     *            Current tile
     * @param ownHero
     *            Own hero
     * @return Is there any enemy in this case? false : if there isn't any true
     *         : if there is one or more
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

    /**
     * Checks whether there is an enemy and returns it
     *
     * @param tile
     *            Current Tile
     * @param ownHero
     *            Own Hero
     * @return AiHero enemy if an enemy is present, else null
     */
    public AiHero fetchEnemy(AiTile tile, AiHero ownHero)
    {
        ai.checkInterruption();

        List<AiHero> enemies = tile.getHeroes();

        int size = enemies.size();
        if(size == 0)
            return null;

        AiHero result = enemies.get(0);
        if(result != ownHero)
            return result;

        else if(size == 1)
            return null;

        else
            return enemies.get(1);
    }

    /**
     * Checks the given tile has an item we might interest.
     * @param tile Given tile
     * @return true for there's an item we might interest. else not.
     */
    public Boolean isItemNeeded(AiTile tile)
    {
        ai.checkInterruption();
        Boolean result = false;

        List<AiItem> items = tile.getItems();
        if(items.isEmpty())
            return false;

        switch (items.get(0).getType())
        {
            case EXTRA_SPEED:
            case GOLDEN_SPEED:
                // if we have max speed, don't want that item
                if(ownHero.getWalkingSpeedIndex() != 8)
                    result = true;
                break;
            case EXTRA_FLAME:
            case GOLDEN_FLAME:
                // if we have max range, don't want that item
                if(ownHero.getBombRange() != ownHero.getBombRangeLimit())
                    result = true;
                break;
            case EXTRA_BOMB:
            case GOLDEN_BOMB:
                // if we have max flame, don't want that item
                if(ownHero.getBombNumberMax() != ownHero.getBombNumberLimit())
                    result = true;
                break;
            case RANDOM_EXTRA:
                result = true;
                break;
        }
        return result;

    }

}
