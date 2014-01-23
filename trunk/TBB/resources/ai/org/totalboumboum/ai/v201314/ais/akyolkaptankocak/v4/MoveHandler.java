package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.ZoneHandler.TileProperty;
import org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v4.criterion.EnemyDistance;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class handles the destination, path and direction choices.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<Agent>
{
    /**
     * Constructs a handler for the agent which passed by parameter.
     * 
     * @param ai
     *            the agent that this class should handle
     */
    protected MoveHandler(Agent ai)
    {
        super(ai);
        ai.checkInterruption();

        zone = ai.getZone();
        ownHero = zone.getOwnHero();

        enemyCriterion = (EnemyDistance) ai.preferenceHandler
                .getCriterion("ENEMY_DISTANCE");
    }

    // ///////////////////////////////////////////////////////////////
    // DATA /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    /** Store current zone */
    private AiZone zone = null;
    /** Store own hero */
    private AiHero ownHero = null;
    /** Store start location */
    public AiLocation startLocation;

    /** Enemy criterion */
    public EnemyDistance enemyCriterion;

    /** Category name of the endTile */
    public String endCategory;

    /** Keep old destination, we will return there after collection */
    public AiTile endCollect = null;

    /** Start to collec or not */
    public boolean processCollection = false;

    /** Which tile we will go for collecting */
    public AiTile bonusTile = null;

    /** Should we return to our old destination */
    public boolean returnStart = false;

    /** Our path is approximate */
    public boolean approxPath = false;

    /**
     * Compares two tiles according to their properties
     */
    public class TileDistanceComparator implements Comparator<AiTile>
    {

        /** ZoneHandler's map */
        private HashMap<AiTile, TileProperty> map;

        /**
         * @param map
         *            ZoneHandler's map
         */
        public TileDistanceComparator(HashMap<AiTile, TileProperty> map)
        {
            ai.checkInterruption();
            this.map = map;
        }

        @Override
        public int compare(AiTile t1, AiTile t2)
        {	ai.checkInterruption();
        	
        	if(t1!=null && t2!=null)
        	{	TileProperty tp1 = map.get(t1);
	            TileProperty tp2 = map.get(t2);
	
	            if(tp1!=null && tp2!=null)
	            {	int d1 = tp1.distance;
		            int d2 = tp2.distance;
		
		            if(d1 == d2)
		            {
		                int b1 = tp1.bonus;
		                int b2 = tp2.bonus;
		
		                if(b1 == b2)
		                {
		                    int w1 = tp1.walls;
		                    int w2 = tp2.walls;
		
		                    return w1 - w2;
		                }
		                return b2 - b1;
		            }
		            return d1 - d2;
	            }
	            else
	            	return 0;
        	}
        	return 0;
        }
    }

    // ///////////////////////////////////////////////////////////////
    // DESTINATION /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected AiTile processCurrentDestination()
    {
        ai.checkInterruption();

        /* Reset consider flag */
        ai.bombHandler.consider = false;

        Map<Integer, List<AiTile>> preferences = ai.preferenceHandler
                .getPreferencesByValue();

        int minPref = Collections.min(preferences.keySet());

        List<AiTile> tiles = preferences.get(minPref);

        AiTile previousDestination = getCurrentDestination();

        if(tiles.size() == 1)
        {
            return tiles.get(0);
        }
        else if(tiles.contains(previousDestination))
        {
            return previousDestination;
        }
        else
        {
            PriorityQueue<AiTile> queue = new PriorityQueue<AiTile>(1,
                    new TileDistanceComparator(ai.zoneHandler.map));

            queue.addAll(tiles);

            return queue.poll();
        }
    }

    // ///////////////////////////////////////////////////////////////
    // PATH /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected AiPath processCurrentPath()
    {
        ai.checkInterruption();

        AiPath result = null;
        startLocation = new AiLocation(ownHero);
        /* Get minimum preference tile */
        AiTile endTile = getCurrentDestination();

        /*
         * Decide to collect one bonus or attack with astarApprox or utilize
         * dijkstra
         */
        int collect = decideCollect(endTile);

        /* If there is an enemy, do not collect */
        if(!ai.zoneHandler.bonusFlag)
        {
            returnStart = false;
            processCollection = false;
        }

        if(returnStart || (ownHero.getTile() == bonusTile && processCollection))
        {
            /* We have reached first bonus */
            processCollection = false;
            bonusTile = null;

            /* If there is no bonus left to collect */
            if(!ai.zoneHandler.bonusFlag)
            {
                returnStart = true;
            }
            /* If our bomb has exploded, return immediately */
            else if(!ai.zoneHandler.bombFlag)
            {
                returnStart = true;
            }
            else
            {
                /* This means there are other bonuses, continue to collect */
                endTile = getCurrentDestination();
                processCollection = true;
                returnStart = false;
                collect = 1;
            }

            if(returnStart)
            {
                /* If we haven't reached to our origin tile */
                if(ownHero.getTile() != endCollect)
                {
                    result = ai.pathHandler.findPath(startLocation, endCollect);
                    approxPath = false;
                    return result;
                }
                /* If we have reached our origin tile */
                else
                {
                    endCollect = null;
                    returnStart = false;
                    processCollection = false;
                }
            }
        }

        /* If preference value is 0 or 1, apply astarApprox */
        else if(collect == 0)
        {
            /* Try for approx based astar */
            result = ai.pathHandler.findApproxPath(startLocation, endTile);
            approxPath = true;
            return result;
        }

        /*
         * There is a bonus that we can collect, and we have enough time to
         * collect it
         */
        else if(collect == 1)
        {
            /* Raise processCollection flag */
            if(!processCollection)
            {
                processCollection = true;
            }

            /* This is the actual path */
            try
            {
                result = ai.pathHandler.astarTime.startProcess(startLocation,
                        endTile);
                if(result != null)
                    bonusTile = result.getLastLocation().getTile();
            }
            catch (LimitReachedException eD)
            {
                result = null;
            }

            approxPath = false;
            return result;
        }

        if(result == null)
        {	TileProperty tp = ai.zoneHandler.map.get(ai.zoneHandler.nearestEnemyTile);
            /* If there isn't a bonus, go to shortest possible secure tile */
            if((ai.zoneHandler.nearestEnemy == null
                    || zone.getRemainingOpponents().size() == 1
                    || (tp!=null && tp.distance > 4) || !ai.bombHandler.securityCriterion
                        .fetchValue(ai.zoneHandler.nearestEnemyTile)))
            {
                /* Try dijkstra */
                try
                {
                    result = ai.pathHandler.dijkstra
                            .processEscapePath(startLocation);

                    if(result != null && result.getLength() > 1)
                    {
                        approxPath = false;
                        return result;
                    }
                }
                catch (LimitReachedException eD)
                {
                    result = null;
                }
            }

            /* Find a path to destination */
            result = ai.pathHandler.findPath(startLocation, endTile);
            approxPath = false;
        }

        return result;
    }

    // ///////////////////////////////////////////////////////////////
    // DIRECTION /////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected Direction processCurrentDirection()
    {
        ai.checkInterruption();
        Direction result = Direction.NONE;

        AiPath path = getCurrentPath();

        if(path == null)
        {
            try
            {
                ai.pathHandler.timeSuccessor.setConsiderOpponents(false);
                path = ai.pathHandler.dijkstra.processEscapePath(startLocation);

                if(path != null && path.getLength() > 1)
                {
                    return zone.getDirection(ownHero.getTile(), path
                            .getLocation(1).getTile());
                }
            }
            catch (LimitReachedException eD)
            {
                result = null;
            }

            ai.bombHandler.consider = true;
            return zone
                    .getDirection(ownHero.getTile(), getCurrentDestination());
        }

        if(path.getLength() < 2 || path.getFirstPause() != 0)
        {
            return Direction.NONE;
        }

        AiTile nextTile = path.getLocation(1).getTile();
        /* If we do not have to plant a bomb here */
        if(approxPath)
        {
            AiBomb ownBomb = ownHero.getBombPrototype();

            if(ai.zoneHandler.bonusFlag)
            {
                for(AiLocation location : path.getLocations())
                {
                    ai.checkInterruption();

                    AiTile tile = location.getTile();

                    if(!tile.getBlocks().isEmpty())
                    {
                        if(ownBomb.getBlast().contains(tile))
                        {
                            if(ai.pathHandler.canReachSafety())
                            {
                                ai.bombHandler.consider = true;
                                break;
                            }
                        }
                    }
                    else if(ai.zoneHandler.map.get(tile).bonus < 0)
                    {
                        if(ownBomb.getBlast().contains(tile))
                        {
                            if(ai.pathHandler.canReachSafety())
                            {
                                ai.bombHandler.consider = true;
                                break;
                            }
                        }
                    }
                }
            }
            else if(!ai.zoneHandler.bombFlag)
            {	TileProperty tp = ai.zoneHandler.map.get(nextTile);
        		
                /* If we have put a bomb, do not put another one */
                /* If next tile is a destructible block, put a bomb here */
                if(!nextTile.getBlocks().isEmpty() || tp!=null && tp.bonus<0)
                {	/* If simulated path can found, we can put a bomb */

                    // endCollect = ownHero.getTile();
                    ai.bombHandler.consider = true;
                }
            }
        }

        if(nextTile.getFires().isEmpty())
            return zone.getDirection(ownHero.getTile(), nextTile);

        return result;
    }

    /**
     * @param endTile
     *            Last tile of the path
     * @return decision of collect
     */
    public int decideCollect(AiTile endTile)
    {
        ai.checkInterruption();

        int result = -1;

        endCategory = ai.preferenceHandler.getCombinationForTile(endTile)
                .getCategory().getName();

        /* If 0 or 1 and we haven't put a bomb, apply astarApprox to enemy */
        if((endCategory.equals("PASSIVE_CORRIDOR")) && !ai.zoneHandler.bombFlag)
        {
            result = 0;
        }

        /* If 2 or 3, start to collect */
        else if(endCategory.equals("COLLECT"))
        {
            result = 1;
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
