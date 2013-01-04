package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v2.criterion;

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
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v2.CaliskanGeckalan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
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
		} catch (LimitReachedException e) {
			e.printStackTrace();
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
				} catch (LimitReachedException e) {
					e.printStackTrace();
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
