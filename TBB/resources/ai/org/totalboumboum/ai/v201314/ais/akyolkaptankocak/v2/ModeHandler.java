package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v2;

import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * This class represents choosing and changing agent's mode depending 2
 * important decisions. First decision is sufficiency of items for the hero.
 * Depending the first decision (false), second one calculates the possibility
 * of collecting the items which are necessary.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
public class ModeHandler extends AiModeHandler<Agent>
{
    /**
     * Constructs a handler for the agent which passed by parameter.
     * 
     * @param ai
     *            the agent that this class should handle.
     */
    protected ModeHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();
    }

    // ///////////////////////////////////////////////////////////////
    // PROCESSING /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected boolean hasEnoughItems()
    {
        ai.checkInterruption();
        boolean result = true;
        AiZone zone = ai.getZone();
        AiHero hero = zone.getOwnHero();
        // number of bombs
        int bomb_nb = hero.getBombNumberMax();
        // bomb range
        int bomb_r = hero.getBombRange();
        // multiple bombs range
        int bomb_mbr = bomb_r * bomb_nb;
        double speed = hero.getWalkingSpeed();
        if(bomb_mbr < Utility.MULTIPLIED_BOMB_RANGE_LIMIT
                || speed < Utility.SPEED_LIMIT)
            result = false;
        else
            result = true;
        return result;
    }

    @Override
    protected boolean isCollectPossible()
    {
        ai.checkInterruption();
        AiZone zone = ai.getZone();
        AiHero hero = zone.getOwnHero();
        // if there's an accessible bonus to get.
        for(AiTile tile : ai.preferenceHandler.selectTiles())
        {
            ai.checkInterruption();
            for(AiItem item : tile.getItems())
            {
                ai.checkInterruption();
                if(item.getType().isBonus())
                    return true;
            }
        }
        // if left item count is low
        if(zone.getHiddenItemsCount() < Utility.HIDDEN_ITEM_LIMIT)
            return false;
        else
        {
            Map<AiItemType, Double> map = zone.getHiddenItemsProbas();
            // get golden speed,golden extra, etc. probability
            double gs = map.get(AiItemType.GOLDEN_SPEED);
            double gb = map.get(AiItemType.GOLDEN_BOMB);
            double eb = map.get(AiItemType.EXTRA_BOMB);
            double es = map.get(AiItemType.EXTRA_SPEED);
            double gf = map.get(AiItemType.GOLDEN_FLAME);
            double ef = map.get(AiItemType.EXTRA_FLAME);
            double wanted = 0;
            /*
             * If we need speed, we only consider the probability of speed
             * items, same as for bomb range and bomb number. If we need both,
             * we consider both. Golden items has more value than extra items,
             * so we consider them not as 1 item but their value defined in our
             * Utility class.
             */
            if(hero.getWalkingSpeed() < Utility.SPEED_LIMIT)
                wanted += gs * Utility.GOLDEN_ITEM_VALUE + es;

            if(hero.getBombRange() * hero.getBombNumberMax() < Utility.MULTIPLIED_BOMB_RANGE_LIMIT)
            {
                wanted += gf * Utility.GOLDEN_ITEM_VALUE + ef + gb
                        * Utility.GOLDEN_ITEM_VALUE + eb;
            }

            if(wanted < Utility.EVALUATED_PROBABILITY_LIMIT)
                return false;
            else
                return true;

        }
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
