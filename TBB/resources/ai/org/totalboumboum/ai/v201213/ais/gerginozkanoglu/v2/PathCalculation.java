package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2;


import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
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
public class PathCalculation {
	
	/**
	 * agent
	 */
	private GerginOzkanoglu ai;
	/**
	 * our hero
	 */
	private AiHero ourHero;
	/**
	 * Maximum cost for costCalculator
	 */
	private static double MAX_COST = 1000;
	/**
	 * Represents the next tile's index in an array-list etc.
	 */
	private static int	NEXT_TILE_INDEX	= 1;
	/**
	 * limit to access
	 */
	private static double LIMIT_TO_ACCESS = 2.5;

	
	/**
	 * constructor
	 * @param ai
	 * @throws StopRequestException 
	 */
	public PathCalculation(GerginOzkanoglu ai) throws StopRequestException
	{
		ai.checkInterruption();
		this.ai = ai;
		this.ourHero = this.ai.getZone().getOwnHero();
	}
    /**
     * 
     * @param agent
     * @param target
     * @return path
     *        this method returns the best path to the target tile by using A*.
     * @throws StopRequestException
     */
    public AiPath bestPath(AiHero agent, AiTile target) throws StopRequestException
    {
    	ai.checkInterruption();
    	AiPath path;
    	CostCalculator costCalculator = new TimeCostCalculator(ai,ourHero );
    	costCalculator.setOpponentCost(MAX_COST);
    	HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(this.ai, this.ourHero);
    	SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(this.ai, TimePartialSuccessorCalculator.MODE_NOTREE);
    	Astar astarAlgo = new Astar(this.ai, this.ourHero, costCalculator, heuristicCalculator, successorCalculator);
    	AiLocation ourLocation = new AiLocation(this.ourHero);
    	try
    	{
    		path = astarAlgo.startProcess(ourLocation, target);
    
    	}
    	catch(LimitReachedException e)
    	{
    		path = new AiPath();
    		path.addLocation(new AiLocation(this.ourHero.getTile()));
    	}
    	catch(NullPointerException e)
    	{
    		path = new AiPath();
    		path.addLocation(new AiLocation(this.ourHero.getTile()));
    	}
    	return path;
		
    }
    
    /**
     * this method will control if best path is safe or not
     * @param path
     * @return boolean 
     * @throws StopRequestException
     */
    protected boolean isPathSafe(AiPath path) throws StopRequestException
    {
    	ai.checkInterruption();
    	TileCalculation calculation = new TileCalculation(this.ai);
    	
    	for(AiLocation location :path.getLocations())
    	{
    		ai.checkInterruption();
    		AiTile tile = location.getTile();
    		if(calculation.isDangerous(tile))
    			return false;
    	}
    	return true;
    }
    /**
     * returns the next tile on path.
     * @param endTile
     * @return nextTileOnPath
     * @throws StopRequestException
     */
    protected AiTile getNextTileOnPath( AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		if ( this.ai.getZone().getOwnHero().getTile().equals( endTile ) ) 
			return this.ai.getZone().getOwnHero().getTile();
		try
		{
			return this.bestPath(this.ai.getZone().getOwnHero(), endTile).getLocation( NEXT_TILE_INDEX ).getTile();
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
     * @return nextDirectionOnPath
     * @throws StopRequestException
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
     */
    protected double timeToAccessToNextTile() throws StopRequestException
    {
    	ai.checkInterruption();
    	AiHero ourHero = this.ai.getZone().getOwnHero();
    	return ourHero.getTile().getSize()/ ourHero.getWalkingSpeed();
    	
    }
    /**
     * Calculates Time Remaining For Explosion
     * @param bomb
     * @return long 
     * @throws StopRequestException
     */
    protected long timeRemainingForExplosion(AiBomb bomb) throws StopRequestException
    {
    	ai.checkInterruption();
    	return  bomb.getNormalDuration() - bomb.getElapsedTime();
    }
    /**
     * Calculates Shortest Time Remaining For Explosion
     * @param tile
     * @return long 
     * @throws StopRequestException
     */
    protected long shortestTimeRemainingForExplosion(AiTile tile) throws StopRequestException
    {
    	ai.checkInterruption();
    	TileCalculation calculate = new TileCalculation(this.ai);
    	long shortestTime = Long.MAX_VALUE;
    	for(AiBomb bomb : this.ai.getZone().getBombs())
    	{
    		ai.checkInterruption();
    		if(calculate.rangeOfBombCrossableByHero(bomb.getTile(), bomb.getRange()).contains(tile))
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
     * @return boolean
     * @throws StopRequestException
     */
    protected boolean isNextTileOnPathSafe (AiTile nextTile) throws StopRequestException
    {
    	ai.checkInterruption();
    	if(!nextTile.getFires().isEmpty())
    		return false;
    	else{
    		if(this.shortestTimeRemainingForExplosion(nextTile) > this.timeToAccessToNextTile()*LIMIT_TO_ACCESS)
				return true;
    	return false;
    	}
    }
}
