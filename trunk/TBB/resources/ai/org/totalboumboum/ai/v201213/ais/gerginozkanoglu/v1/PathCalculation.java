package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1;


import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
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
	private static double MAX_COST = 500;
	
	/**
	 * @param ai
	 * @throws StopRequestException 
	 */
	
	protected PathCalculation(GerginOzkanoglu ai) throws StopRequestException
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
    protected AiPath bestPath(AiHero agent, AiTile target) throws StopRequestException
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
    		e.printStackTrace();
    		path = new AiPath();
    		path.addLocation(new AiLocation(this.ourHero.getTile()));
    	}
    	return path;
		
    }

}
