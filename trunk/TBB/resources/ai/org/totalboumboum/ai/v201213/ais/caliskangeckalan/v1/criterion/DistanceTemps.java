package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1.CaliskanGeckalan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
public class DistanceTemps extends AiUtilityCriterionBoolean<CaliskanGeckalan>
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCETEMPS";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/** */
	protected AiZone zone = null;
	/** */
	protected AiHero ownHero = null;
	/** */
	protected AiTile currentTile = null;
	/** */
	protected Astar astarPrecise = null;
	/** */
	protected Astar astarApproximation = null;
	/** */
	protected Dijkstra dijkstra = null;
	/** */
	protected AiPath indirectPath = null;
	/** */
	protected AiTile safeDestination = null;
	/** */
	protected boolean secondaryBombing = false;
	/**
	 * @param ai
	 * @throws StopRequestException
	 */
	public DistanceTemps(CaliskanGeckalan ai) throws StopRequestException
	{	// init nom
		super(ai,NAME);
		ai.checkInterruption();
		// init agent
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	boolean result = false;
	
	AiPath path = null;
	CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
	costCalculator.setOpponentCost(1000); 
	HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
	SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
	Astar astar = new Astar(ai, ai.getZone().getOwnHero(), costCalculator, heuristicCalculator, successorCalculator);
	try {
		path = astar.startProcess(new AiLocation(currentTile), tile);
	} catch (LimitReachedException e) {
		e.printStackTrace();
	}
	if(path!=null && path.getLength()<4 ) {
		result = true;
	}
	return result;
	}
}
