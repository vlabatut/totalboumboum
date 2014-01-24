package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.Agent;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.ZoneHandler.TileProperty;
import org.totalboumboum.tools.images.PredefinedColor;

/**
  * This class represents the XXX criterion. It determines that given tile has a bonus that
  * we might interest and we have enough time for collect it.
  * 
  * @author Fırat Akyol
  * @author Mustafa Kaptan
  * @author Gökberk Koçak
  */
@SuppressWarnings("deprecation")
public class EnoughTime extends AiCriterionBoolean<Agent>
{
    /** Name of the criterion */
    public static final String NAME = "ENOUGH_TIME";

    /**
     * Enough Time criterion, binary
     * 
     * @param ai
     *            Our agent (black).
     */
    public EnoughTime(Agent ai)
    {
        super(ai, NAME);
        ai.checkInterruption();
        zone = ai.getZone();
        ownHero = zone.getOwnHero();
    }

    /** Zone */
    public AiZone zone;

    /** Own Hero */
    public AiHero ownHero;

    // ///////////////////////////////////////////////////////////////
    // PROCESS /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    public Boolean processValue(AiTile tile)
    {
        ai.checkInterruption();

        TileProperty tp = ai.zoneHandler.map.get(tile);

        if(tp.bonus <= 0 || !ai.criterionHandler.isItemNeeded(tile))
            return false;

        double walkingSpeed = ownHero.getWalkingSpeed() / AiTile.getSize();
        int distance = tp.distance;

        List<AiBomb> bombs = zone.getBombsByColor(PredefinedColor.BLACK);

        if(bombs.isEmpty())
        {
            if(ai.zoneHandler.nearestEnemy!=null && ai.zoneHandler.nearestBonus!=null)
            {	AiTile bTile = ai.zoneHandler.nearestBonus.getTile();
            	TileProperty bTp = ai.zoneHandler.map.get(bTile);
            	AiTile eTile = ai.zoneHandler.nearestEnemy.getTile();
            	TileProperty eTp = ai.zoneHandler.map.get(eTile);
            	if(bTp!=null && eTp!=null)
            		return bTp.distance * 3 < eTp.distance;
            }
            return true;
        }

        double bombTime = (ownHero.getBombDuration() - bombs.get(0)
                .getElapsedTime()) / 100;

        boolean result = (walkingSpeed * bombTime) > (distance * 3);
        
        return result;
    }
}
