package org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.UnluYildirim;

/**
 * Cette classe permet de savoir si le tile est accessible par notre IA.
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class CriterionAccessible extends AiUtilityCriterionBoolean
{	/** */
	public static final String NAME = "ACCESSIBLE";
	
	/**
	 * 
	 * @param ai
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public CriterionAccessible(UnluYildirim ai) throws StopRequestException
	{
		super(NAME);
		ai.checkInterruption();
	
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected UnluYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
	
	ai.checkInterruption();
	boolean result = true;
	
	AiHero myhero = null ;
	AiZone zone;
	
	zone=ai.getZone();
	myhero=zone.getOwnHero();
	
	AiPath path = null;
	Astar astar ;
	CostCalculator costCalculator = new TimeCostCalculator(ai,myhero);
	HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,myhero);
	SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
	astar = new Astar(ai,myhero, costCalculator, heuristicCalculator, successorCalculator);
    
	
	
	AiLocation startlocation= new AiLocation(myhero);
	
	//il essaie de trouver une chemin s'il existe
	try
	{	path = astar.processShortestPath(startlocation,tile);
	}
	catch (LimitReachedException e)
	{	e.printStackTrace();
	}
	
	if(path==null || path.isEmpty())
	{	//S'il n'y a pas une chemin , ça veut dire que le tile n'est pas accessible
     	result=false ;
	}
	else
	{	//le tile est accessible et il retourne true.
	    result=true;
	}

	
		return result;
	}
}
