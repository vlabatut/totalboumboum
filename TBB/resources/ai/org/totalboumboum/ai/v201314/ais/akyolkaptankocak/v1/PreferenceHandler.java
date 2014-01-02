package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * This class selects which tiles are we going to evaluate and
 * identifies categories on selected tiles.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{
    /**
     * Construit un gestionnaire pour l'agent passé en paramètre.
     *
     * @param ai
     *            l'agent que cette classe doit gérer.
     */
    protected PreferenceHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();

    }

    /////////////////////////////////////////////////////////////////
    // DATA                     /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    protected void resetCustomData()
    {
        ai.checkInterruption();

        // cf. la Javadoc dans AiPreferenceHandler pour une description de la
        // méthode
    }

    /////////////////////////////////////////////////////////////////
    // PROCESSING               /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    protected Set<AiTile> selectTiles()
    {
        /**
         * This method uses the "largeur d'abord" algorithm
         * to find accessible tiles considering the obstacles from our hero's tile.
         */
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
                    fringe.add(neighbor);
                    result.add(neighbor);
                }
            }
        }

        return result;
    }

    /////////////////////////////////////////////////////////////////
    // CATEGORY                 /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /** Name of the 1st category */
    private static String CAT_NAME_1 = "FATAL_ATTACK";
    /** Name of the 2nd category */
    private static String CAT_NAME_2 = "PASSIVE_ATTACK";

    @Override
    protected AiCategory identifyCategory(AiTile tile)
    {
        ai.checkInterruption();
        AiCategory result = null;

        if(getNearTiles(tile.getZone().getOwnHero().getTile(), 4)
                .contains(tile))
        {
            result = getCategory(CAT_NAME_1);
        }
        else
        {
            result = getCategory(CAT_NAME_2);
        }

        return result;
    }

    /**
	 * This method gives the accessible tiles from given tile in a certain distance
     * considering the obstacles using the "largeur d'abord" algorithm.
	 *
     * @param tile
     *            : Starting tile for measuring real distance.
     * @param distance
     *            : Integer value of the tile distance.
     * @return result : List which contains the tiles we can reach at (distance)
     *         steps.
     */
    public List<AiTile> getNearTiles(AiTile tile, int distance)
    {
        ai.checkInterruption();

        // List we will return
        List<AiTile> result = new ArrayList<AiTile>();
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
                if(neighbor.isCrossableBy(ownHero)
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
        return null;
    }

    /////////////////////////////////////////////////////////////////
    // OUTPUT           /////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    public void updateOutput()
    {
        ai.checkInterruption();

        // ici on se contente de faire le traitement par défaut
        super.updateOutput();

    }
}
