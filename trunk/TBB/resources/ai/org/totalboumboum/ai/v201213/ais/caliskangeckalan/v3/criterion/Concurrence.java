package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion;

import java.util.Iterator;
import java.util.List;

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
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.CaliskanGeckalan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
@SuppressWarnings("deprecation")
public class Concurrence extends AiUtilityCriterionBoolean<CaliskanGeckalan>
{	/** Nom de ce critère */
	public static final String NAME = "ConcurrenceCollecteForBonus";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/** initialize the zone	 */
	protected AiZone zone = null;
	/** initialize the ownHero	 */
	protected AiHero ownHero = null;
	/** initialize the tile of ownHero	 */
	protected AiTile currentTile = null;
	/** initialize the astar for direct path */
	protected Astar astarPrecise = null;
	/** initialize the astar for indirect path */
	protected Astar astarApproximation = null;
	/** initialize the dijkstra for all tiles*/
	protected Dijkstra dijkstra = null;
	/** initialize a path for indirect*/
	protected AiPath indirectPath = null;
	/** initialize a tile for escape*/
	protected AiTile safeDestination = null;
	/** initialize a boolean for dropping bomb*/
	protected boolean secondaryBombing = false;
	
	/**
	 * @param ai
	 * 		information manquante !?	
	 * @throws StopRequestException
	 * 		information manquante !?	
	 */
	public Concurrence(CaliskanGeckalan ai) throws StopRequestException
	{	// init nom
		super(ai,NAME);
		
		// init agent
		ai.checkInterruption();
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
		List<AiHero> listHeroes = zone.getRemainingHeroes();
		AiPath path = null;
		int lengthOfPathOwn = 0;
		int lenghtOfPathOthers = 0;
		double vitesseOwn = ownHero.getWalkingSpeed();
		double dureeOwn = 0D;
		double dureeOthers = Double.MAX_VALUE;
		CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
		costCalculator.setOpponentCost(1000); 
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
		Astar astar = new Astar(ai, ai.getZone().getOwnHero(), costCalculator, heuristicCalculator, successorCalculator);
		try {
			path = astar.startProcess(new AiLocation(currentTile), tile);
		} catch (LimitReachedException e) {//
		}
		if(path!=null) {
			lengthOfPathOwn = path.getLength();
		}
		dureeOwn = ((double)lengthOfPathOwn)/vitesseOwn;
		Iterator<AiHero> heroesIt = listHeroes.iterator();
		while(heroesIt.hasNext() && dureeOwn<dureeOthers) {
			ai.checkInterruption();
			AiHero heroesNext = heroesIt.next();
			AiPath pathOthers = null;
			if(!heroesNext.equals(ownHero)) {
				double vitesseNextHeroes = heroesNext.getWalkingSpeed();
				CostCalculator costCalculatorOthers = new TimeCostCalculator(ai,heroesNext);
				costCalculator.setOpponentCost(1000); 
				HeuristicCalculator heuristicCalculatorOthers = new TimeHeuristicCalculator(ai,heroesNext);
				SuccessorCalculator successorCalculatorOthers = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
				Astar astarOthers = new Astar(ai, heroesNext, costCalculatorOthers, heuristicCalculatorOthers, successorCalculatorOthers);
				try {
					pathOthers = astarOthers.startProcess(new AiLocation(heroesNext), tile);
				} catch (LimitReachedException e) {//
				}
				if(pathOthers!=null) {
					lenghtOfPathOthers = pathOthers.getLength();
					dureeOthers = ((double)lenghtOfPathOthers)/vitesseNextHeroes;
				}
			}
		}
		if(dureeOthers>dureeOwn)
			result = true;
	return result;
	}
}
