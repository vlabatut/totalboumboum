package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4;

import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.ZoneHandler.TileProperty;

/**
 * This class selects which tiles are we going to evaluate and identifies
 * categories on selected tiles.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
@SuppressWarnings("deprecation")
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

        Set<AiTile> result = ai.zoneHandler.tileSelection();

        // Select enemy tiles
        if(!ai.zoneHandler.heroFlag)
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
    private static String CORRIDOR = "CORRIDOR";
    /** Name of the 2nd category */
    private static String PASSIVE_CORRIDOR = "PASSIVE_CORRIDOR";
    /** Name of the 3rd category */
    private static String COLLECT = "COLLECT";

    @Override
    protected AiCategory identifyCategory(AiTile tile)
    {
        ai.checkInterruption();
        AiCategory result = null;
        AiHero ownHero = tile.getZone().getOwnHero();

        if(target == null)
        {
            if(zone.getRemainingOpponents().size() > 1)
            {
                count = false;
                hardEnemy = true;
                enemyBombCount = 1;
                target = zone.getRemainingOpponents().get(0);
            }
            else
            {
                target = zone.getRemainingOpponents().get(0);
            }
        }

        // If we are counting
        if(count)
        {
            // If 10sec is over don't count anymore
            if(zone.getTotalTime() > 8000)
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
            int bombRange = ownHero.getBombRange();

            if(hardEnemy)
            {
                if(ai.zoneHandler.nearestEnemy != null)
                {
                    TileProperty ne = ai.zoneHandler.map
                            .get(ai.zoneHandler.nearestEnemyTile);
                    if(ne != null && ne.distance < bombRange + 1)
                    {
                        if(ai.zoneHandler.map.get(tile).distance < bombRange + 1)
                        {
                            result = getCategory(CORRIDOR);
                        }
                        else
                            result = getCategory(PASSIVE_CORRIDOR);
                    }
                    else
                        result = getCategory(PASSIVE_CORRIDOR);
                }
                else
                {
                    result = getCategory(PASSIVE_CORRIDOR);
                }
            }
            else
            {
                if(ai.zoneHandler.nearestEnemy != null)
                {
                    if(ai.zoneHandler.map.get(ai.zoneHandler.nearestEnemyTile).distance <= bombRange - 3)
                    {
                        result = getCategory(CORRIDOR);
                    }
                    else
                    {
                        result = getCategory(PASSIVE_CORRIDOR);
                    }

                }
                else
                {
                    result = getCategory(PASSIVE_CORRIDOR);
                }
            }

        }

        // Collecting Mode
        else
        {
            if(ai.zoneHandler.bonusFlag
                    && ai.zoneHandler.map.get(tile).bonus > 0)
            {
                result = getCategory(COLLECT);
            }
            else
            {
                result = getCategory(PASSIVE_CORRIDOR);
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
