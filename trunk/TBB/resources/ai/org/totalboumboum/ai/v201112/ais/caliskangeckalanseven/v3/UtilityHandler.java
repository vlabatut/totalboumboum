package org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201112.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.criterion.DistanceTemps;
import org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.criterion.Menace;
import org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.criterion.PertinentItems;
import org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.criterion.PertinentMurs;
import org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.criterion.Rivals;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent. Cf. la
 * documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 * @author Cihan Seven
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<CaliskanGeckalanSeven> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(CaliskanGeckalanSeven ai)
			throws StopRequestException {
		super(ai);
		ai.checkInterruption();


		
		verbose = false;
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		calculCommun = new CalculCommun(ai);
		{ CostCalculator costCalculator = new TileCostCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		dijkstra = new Dijkstra(ai,ownHero,costCalculator,successorCalculator);
		}
		{	CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
		costCalculator.setOpponentCost(1000); // on assimile la traversée d'un adversaire à un détour de 1 seconde
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
		astarPrecise = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
		}
		{	CostCalculator costCalculator = new ApproximateCostCalculator(ai,ownHero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
		SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
		astarApproximation = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
	}
	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	private final String caseName1 = "CAS1";
	/** */
	private final String caseName2 = "CAS2";

	/** */
	protected AiZone zone = null;
	/** */
	protected AiHero ownHero = null;
	/** */
	protected AiTile currentTile = null;
	/** */
	private Dijkstra dijkstra = null;
	/** */
	protected Astar astarPrecise = null;
	/** L'objet a* utilisé pour le calcul des chemins indirects */
	protected Astar astarApproximation = null;
	/** Les méthodes communes */
	/** Indique pour chaque case traitée si on veut y poser une bombe ou pas */
	protected CalculCommun calculCommun = null;
	/** */
	protected HashMap<AiTile,Boolean> bombTiles = new HashMap<AiTile,Boolean>();
	/** */
	protected HashMap<AiTile,Boolean> collectTiles = new HashMap<AiTile,Boolean>();
	
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		if(calculCommun.danger()) {
			result = calculCommun.safeZone();		
		}
		else {

				List<AiItem> items = zone.getItems();
				if(!items.isEmpty())
				{	for(AiItem item: items)
					{	ai.checkInterruption();	
						
						AiTile tile = item.getTile();
						result.add(tile);
					}
				}
				AiLocation startLocation = new AiLocation(ownHero);
				try
				{	HashMap<AiTile,AiSearchNode> map = dijkstra.startProcess(startLocation);
					result.addAll(map.keySet());
				}
				catch (LimitReachedException e)
				{	
				}	
			
	
		}
		return result;
	}
	@Override
	protected void initCriteria() throws StopRequestException {
		ai.checkInterruption();
		Menace menace = new Menace(ai);
		PertinentMurs pertinentMurs = new PertinentMurs(ai);
		PertinentItems pertinentItems = new PertinentItems(ai);
		DistanceTemps distance = new DistanceTemps(ai);
		Rivals rivals = new Rivals(ai);

		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(menace);
		criteria.add(pertinentMurs);
		criteria.add(distance);
		criteria.add(pertinentItems);
		criteria.add(rivals);
		AiUtilityCase case1 = new AiUtilityCase(caseName1, criteria);

		Set<AiUtilityCriterion<?>> criteria2 = new TreeSet<AiUtilityCriterion<?>>();
		criteria2.add(menace);
		criteria2.add(pertinentItems);
		criteria2.add(pertinentMurs);
		AiUtilityCase case2 = new AiUtilityCase(caseName2, criteria2);

		
		
		cases.put(caseName1,case1);
		cases.put(caseName2,case2);

		int utility = 1;
		AiUtilityCombination combi;

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, false);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentMurs, true);
			combi.setCriterionValue(distance, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(rivals, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(pertinentMurs, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(pertinentMurs, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(pertinentMurs, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(menace, false);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(pertinentMurs, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(pertinentMurs, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentItems, false);
			combi.setCriterionValue(pertinentMurs, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(pertinentMurs, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(menace, true);
			combi.setCriterionValue(pertinentItems, true);
			combi.setCriterionValue(pertinentMurs, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		AiUtilityCase result = null;
		AiMode mode = ai.modeHandler.getMode();
		if(mode.equals(AiMode.ATTACKING)) {
			result = cases.get(caseName1);
			bombTiles.put(tile, true);
		}
		else {
			result = cases.get(caseName2);
			collectTiles.put(tile, true);
		}
		return result;
	}
	


	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();
		
	}
}
