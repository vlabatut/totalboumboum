package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3;

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

        zone = ai.getZone();
        ownHero = zone.getOwnHero();
    }

    /** Get Zone */
    public AiZone zone;
    /** Get Own Hero */
    public AiHero ownHero;
    /** Max Map Length */
    public int maxMapLenght = 32;
    /** Greed limit */
    public double greedLimit = 0.2;
    /** Integer value of hidden item limit. */

    public static final int HIDDEN_ITEM_LIMIT = 4;

    /** Integer value of limit of multiplied bomb range (number of bombs range). */
    public static final int MULTIPLIED_BOMB_RANGE_LIMIT = 4;

    /** Double value of evaluated probability limit for hidden items. */
    public static final double EVALUATED_PROBABILITY_LIMIT = 0.35;

    /** Double value of speed limit for the hero. */
    public static final double SPEED_LIMIT = 99;

    /**
     * Double value for golden items. It must be higher than 1, because normal
     * items values are 1.
     */
    public static final double GOLDEN_ITEM_VALUE = 1.5;

    // ///////////////////////////////////////////////////////////////
    // PROCESSING /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected boolean hasEnoughItems()
    {
        ai.checkInterruption();
        // if there's no enemy, no bomb, no flame around and match wont end
        // soon, so collect
        // dont forget to consider map size! that's also important.
        int mapLenght = zone.getMatrix().length + zone.getMatrix()[0].length;
        double mapRatio = mapLenght / (double) maxMapLenght;
        double elapsedTimeRatio = zone.getTotalTime()
                / (double) zone.getLimitTime();

        if(!ai.preferenceHandler.bombFlag
                && ai.preferenceHandler.nearestEnemy == null
                && elapsedTimeRatio < (greedLimit - 0.1 * mapRatio))
        {
            return false;
        }
        return true;
    }

    @Override
    protected boolean isCollectPossible()
    {
        ai.checkInterruption();

        if(ai.preferenceHandler.nearestEnemy != null)
        {
            return false;
        }

        // if there's an accessible bonus to get.
        for(AiTile tile : ai.preferenceHandler.getReachableTiles())
        {
            ai.checkInterruption();
            for(AiItem item : tile.getItems())
            {
                ai.checkInterruption();
                if(item.getType().isBonus())
                {
                    return true;
                }
            }
        }
        // if there's no block to be destroyed, collect is not possible.
        if(ai.preferenceHandler.blockFlag == false)
        {
            return false;
        }
        // if left item count is low
        if(zone.getHiddenItemsCount() < HIDDEN_ITEM_LIMIT)
            return false;
        else
        {

            double gs = .0, gb = .0, eb = .0, es = .0, gf = .0, ef = .0;
            Map<AiItemType, Double> map = zone.getHiddenItemsProbas();
            // get golden speed,golden extra, etc. probability
            if(map.containsKey(AiItemType.GOLDEN_SPEED))
                gs = map.get(AiItemType.GOLDEN_SPEED);
            if(map.containsKey(AiItemType.GOLDEN_BOMB))
                gb = map.get(AiItemType.GOLDEN_BOMB);
            if(map.containsKey(AiItemType.EXTRA_BOMB))
                eb = map.get(AiItemType.EXTRA_BOMB);
            if(map.containsKey(AiItemType.EXTRA_SPEED))
                es = map.get(AiItemType.EXTRA_SPEED);
            if(map.containsKey(AiItemType.GOLDEN_FLAME))
                gf = map.get(AiItemType.GOLDEN_FLAME);
            if(map.containsKey(AiItemType.EXTRA_FLAME))
                ef = map.get(AiItemType.EXTRA_FLAME);

            double wanted = 0;
            /*
             * If we need speed, we only consider the probability of speed
             * items, same as for bomb range and bomb number. If we need both,
             * we consider both. Golden items has more value than extra items,
             * so we consider them not as 1 item but their value defined in our
             * Utility class.
             */
            if(ownHero.getWalkingSpeed() < SPEED_LIMIT)
                wanted += gs * GOLDEN_ITEM_VALUE + es;

            if(ownHero.getBombRange() * ownHero.getBombNumberMax() < MULTIPLIED_BOMB_RANGE_LIMIT)
            {
                wanted += gf * GOLDEN_ITEM_VALUE + ef + gb
                        * GOLDEN_ITEM_VALUE + eb;
            }

            if(wanted < EVALUATED_PROBABILITY_LIMIT)
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
