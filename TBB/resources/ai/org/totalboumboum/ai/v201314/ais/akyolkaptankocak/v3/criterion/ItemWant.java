package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3.Agent;

/**
 * This class represents the Item Want criterion If we need the item which
 * appears on a tile passed by parameter it returns true. If we don't need that
 * item, returns false. Thus, binary.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */

public class ItemWant extends AiCriterionBoolean<Agent>
{
    /** Name of the criterion */
    public static final String NAME = "ITEM_WANT";

    /** Double value of speed limit for the hero. */
    public static final double SPEED_LIMIT = 99;

    /** Integer value of limit of multiplied bomb range (number of bombs range). */
    public static final int MULTIPLIED_BOMB_RANGE_LIMIT = 4;

    /**
     * Item Want criterion, binary
     * 
     * @param ai
     *            Our agent (black).
     */
    public ItemWant(Agent ai)
    {
        super(ai, NAME);
        ai.checkInterruption();
    }

    // ///////////////////////////////////////////////////////////////
    // PROCESS /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    public Boolean processValue(AiTile tile)
    {
        ai.checkInterruption();
        Boolean result = false;
        AiZone zone = ai.getZone();
        AiHero hero = zone.getOwnHero();
        switch (tile.getItems().get(0).getType())
        {
            case EXTRA_SPEED:
            case GOLDEN_SPEED:
                if(hero.getWalkingSpeed() < SPEED_LIMIT)
                    result = true;
                break;
            case EXTRA_BOMB:
            case EXTRA_FLAME:
            case GOLDEN_BOMB:
            case GOLDEN_FLAME:
                if(hero.getBombNumberMax() * hero.getBombRange() < MULTIPLIED_BOMB_RANGE_LIMIT)
                    result = true;
                break;
            case RANDOM_EXTRA:
                result = true;
                break;
        }

        return result;

    }
}
