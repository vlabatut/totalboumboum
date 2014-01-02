package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v1.criterion;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v1.Agent;

/**
 * This class represents the Security criterion.
 * A tile is secure if:
 * - There isn't any malus on it
 * - It isn't on a bomb's range
 * - It hasn't got any flames on it.
 * This class checks whether specified tile is secure
 * or not.
 * Thus this criterion is binary.
 *
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
public class Security extends AiCriterionBoolean<Agent>
{
    /** Name of the criterion */
    public static final String NAME = "SECURITY";

    /**
     * Security criterion, binary
     *
     * @param ai
     *            Our agent (black).
     */
    public Security(Agent ai)
    {
        super(ai, NAME);
        ai.checkInterruption();
    }

    /////////////////////////////////////////////////////////////////
    // PROCESS                  /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    public Boolean processValue(AiTile tile)
    {
        ai.checkInterruption();

        // If this tile is dangerous
        if(getDangerousTiles().contains(tile))
            return false;

        // If this tile is secure
        return true;
    }

    /**
     * Check which tiles are dangerous.
     * A tile is dangerous if
     *    - It is in range of a bomb
     *    - It is burning
     *    - It has a bad bonus (malus)
     *
     * @return
     *          ArrayList<AiTile> dangerousTiles
     *          list of (dangerous) tiles
     */
    public ArrayList<AiTile> getDangerousTiles()
    {
        ai.checkInterruption();

        // Create a list for dangerous tiles
        ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();

        // Check for each bomb
        for(AiBomb currentBomb : ai.getZone().getBombs())
        {
            ai.checkInterruption();
            // Add each bomb's range
            for(AiTile currentTile : currentBomb.getBlast())
            {
                ai.checkInterruption();
                dangerousTiles.add(currentTile);
            }
        }

        // Check for every flame
        for(AiFire currentFire : this.ai.getZone().getFires())
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

        return dangerousTiles;
    }
}
