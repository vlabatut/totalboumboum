package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4;


import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * This class has all the calculations about path.
 * @author Secil Ozkanoglu
 * @author Tugce Gergin
 *
 */
@SuppressWarnings("deprecation")
public class PathCalculation {
	
	/**
	 * agent
	 */
	private GerginOzkanoglu ai;
	
	/**
	 * Maximum cost for costCalculator
	 */
	private static double MAX_COST = 1000;
	/**
	 * Minimum cost for costCalculator
	 */
	private static double MIN_COST = 0;
	/**
	 * Represents the next tile's index in an array-list etc.
	 */
	private static int	NEXT_TILE_INDEX	= 1;
	/**
	 * limit to access
	 */
	private static double LIMIT_TO_ACCESS = 2.5;
	/**
	 * limit to access 2
	 */
	private static int LIMIT_TO_ACCESS_2 = 2;
	/**
	 * static variable is used to convert second to milisecond.
	 */
	private static int CONVERSION_TO_MS = 1000;

	
	/**
	 * constructor
	 * @param ai
	 * 		description manquante !	
	 * @throws StopRequestException 
	 * 		description manquante !	
	 */
	public PathCalculation(GerginOzkanoglu ai) throws StopRequestException
	{
		ai.checkInterruption();
		this.ai = ai;
	}
    /**
     * 
     * @param agent
	 * 		description manquante !	
     * @param target
	 * 		description manquante !	
     * @return path
     *        this method returns the best path to the target tile by using A*.
     * @throws StopRequestException
	 * 		description manquante !	
     */
    public AiPath bestPath(AiHero agent, AiTile target) throws StopRequestException
    {
    	ai.checkInterruption();
    	AiPath path = new AiPath();
    	if(target!=null)
    	{	AiHero ourHero = this.ai.getZone().getOwnHero();
	    	CostCalculator costCalculator = new TimeCostCalculator(ai,ourHero);
	    	if(this.ai.getModeHandler().getMode().equals(AiMode.ATTACKING))
	    		costCalculator.setOpponentCost(MIN_COST);
	    	else
	    		costCalculator.setOpponentCost(MAX_COST);
	    	costCalculator.setMalusCost(MAX_COST);
	    	HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(this.ai,ourHero);
	    	SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(this.ai, TimePartialSuccessorCalculator.MODE_NOTREE);
	    	Astar astarAlgo = new Astar(this.ai,ourHero, costCalculator, heuristicCalculator, successorCalculator);
	    	AiLocation ourLocation = new AiLocation(ourHero);
	    	try
	    	{
	    		path = astarAlgo.startProcess(ourLocation, target);
	    
	    	}
	    	catch(LimitReachedException e)
	    	{
	    		path = new AiPath();
	    		path.addLocation(new AiLocation(ourHero.getTile()));
	    	}
	    	catch(NullPointerException e)
	    	{
	    		path = new AiPath();
	    		path.addLocation(new AiLocation(ourHero.getTile()));
	    	}
    	}
    	return path;
		
    }
    /**
     * this method will control if best path is safe or not for collect mode
     * @param path
	 * 		description manquante !	
     * @return boolean
     * @throws StopRequestException
	 * 		description manquante !	
     */
    protected boolean isPathSafe(AiPath path) throws StopRequestException
    {
    	ai.checkInterruption();
    	TileCalculation calculation = new TileCalculation(this.ai);
    	if(path!=null)
    	{	for(AiLocation location :path.getLocations())
	    	{
	    		ai.checkInterruption();
	    		AiTile tile = location.getTile();
	    		if(calculation.isDangerous(tile))
	    			return false;
	    	}
    	}
    	return true;
    }
    /**
     * returns the next tile on path.
     * @param endTile
	 * 		description manquante !	
     * @return nextTileOnPath
     * @throws StopRequestException
	 * 		description manquante !	
     */
    protected AiTile getNextTileOnPath( AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		if ( this.ai.getZone().getOwnHero().getTile().equals( endTile ) ) 
			return this.ai.getZone().getOwnHero().getTile();
		try
		{
			AiPath path = this.bestPath(this.ai.getZone().getOwnHero(), endTile);
			if(path!=null && !path.isEmpty())
				return path.getLocation( NEXT_TILE_INDEX ).getTile();
			else
				return this.ai.getZone().getOwnHero().getTile();
		}
		catch(IndexOutOfBoundsException e)
		{
			return this.ai.getZone().getOwnHero().getTile();
		}
		catch ( NullPointerException e )
		{
			return this.ai.getZone().getOwnHero().getTile();
		}
	}
    
    /**
     * returns the next direction on path
     * @param endTile
	 * 		description manquante !	
     * @return nextDirectionOnPath
     * @throws StopRequestException
	 * 		description manquante !	
     */
    protected Direction getNextDirectionOnPath( AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		AiTile nextTile =  this.getNextTileOnPath( endTile );
		if(this.isNextTileOnPathSafe(nextTile))
		return this.ai.getZone().getDirection( this.ai.getZone().getOwnHero().getTile(), nextTile);
		else
			return Direction.NONE;
	}
    /**
     * calculates time To Access To Next Tile
     * @return double
     * @throws StopRequestException 
	 * 		description manquante !	
     */
    protected long timeToAccessToNextTile() throws StopRequestException
    {
    	ai.checkInterruption();
    	AiHero ourHero = this.ai.getZone().getOwnHero();
    	return (long) Math.round(ourHero.getTile().getSize()/ ourHero.getWalkingSpeed()*CONVERSION_TO_MS);
    	
    }
    /**
     * Calculates Time Remaining For Explosion
     * @param bomb
	 * 		description manquante !	
     * @return long 
     * @throws StopRequestException
	 * 		description manquante !	
     */
    protected long timeRemainingForExplosion(AiBomb bomb) throws StopRequestException
    {
    	ai.checkInterruption();
    	return  bomb.getNormalDuration() - bomb.getElapsedTime();
    }
    /**
     * Calculates Shortest Time Remaining For Explosion
     * @param tile
	 * 		description manquante !	
     * @return long 
     * @throws StopRequestException
	 * 		description manquante !	
     */
    protected long shortestTimeRemainingForExplosion(AiTile tile) throws StopRequestException
    {
    	ai.checkInterruption();
    	long shortestTime = Long.MAX_VALUE;
    	for(AiBomb bomb : this.ai.getZone().getBombs())
    	{
    		ai.checkInterruption();
    		if(bomb.getBlast().contains(tile))
    		{
    			long timeRemaining = this.timeRemainingForExplosion(bomb);
    			if(shortestTime > timeRemaining)
    				shortestTime= timeRemaining;
    		}
    	}
    	return shortestTime;
    	
    }
    /**
     * controls if next tile on path safe or not via time calculations
     * @param nextTile
	 * 		description manquante !	
     * @return boolean
     * @throws StopRequestException
	 * 		description manquante !	
     */
    protected boolean isNextTileOnPathSafe (AiTile nextTile) throws StopRequestException
    {
    	ai.checkInterruption();
    	if(!nextTile.getFires().isEmpty())
    		return false;
    	else{
    		if(this.shortestTimeRemainingForExplosion(nextTile) > this.timeToAccessToNextTile()*LIMIT_TO_ACCESS && !this.isThereSuddenDeathEventInNextTile(nextTile))
				return true;
    	return false;
    	}
    }
    /**
     * checks if there is sudden death in next tile or not.
     * @param nextTile
	 * 		description manquante !	
     * @return boolean
     * @throws StopRequestException
	 * 		description manquante !	
     */
    public boolean isThereSuddenDeathEventInNextTile(AiTile nextTile) throws StopRequestException
	{
		ai.checkInterruption();
		AiSuddenDeathEvent event = this.ai.getZone().getNextSuddenDeathEvent();
		if(event!=null && event.getTiles().contains(nextTile))
		{
			long currentTime=this.ai.getZone().getTotalTime();
			
			if(this.timeToAccessToNextTile() <= (event.getTime()-currentTime)*LIMIT_TO_ACCESS && (event.getTime()-currentTime) <= this.timeToAccessToNextTile()*LIMIT_TO_ACCESS_2)
				return true;
			else 
				return false;
		}
		return false;
	}
    
}
