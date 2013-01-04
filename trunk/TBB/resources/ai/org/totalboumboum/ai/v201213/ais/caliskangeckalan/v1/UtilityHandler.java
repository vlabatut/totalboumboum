package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.PixelCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1.criterion.Concurrence;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1.criterion.ConjonctionMurs;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1.criterion.DistanceTemps;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1.criterion.MenaceCollecte;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1.criterion.NombreMursAttack;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1.criterion.PertinentCollecteForBonus;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1.criterion.PuissanceRivals;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
public class UtilityHandler extends AiUtilityHandler<CaliskanGeckalan>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(CaliskanGeckalan ai) throws StopRequestException
    {	
		super(ai);
		ai.checkInterruption();
		verbose = false;
		calculCommun = new CalculCommun(ai);
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		{ //Djikstra
			CostCalculator costCalculator = new TileCostCalculator(ai);
			SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
			dijkstra = new Dijkstra(ai,ownHero,costCalculator,successorCalculator);
		}
		{ //Djikstra simple
			CostCalculator costCalculator = new PixelCostCalculator(ai);
			SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
			dijkstraSimple = new Dijkstra(ai,ownHero,costCalculator,successorCalculator);
			dijkstraSimple.setMaxHeight(2*(zone.getWidth()+zone.getHeight())); 
		}
		{	//AStar for direct path
			CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
			costCalculator.setOpponentCost(1000); 
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
			astarPrecise = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
		}
		{	//AStar for indirect path
			CostCalculator costCalculator = new ApproximateCostCalculator(ai,ownHero);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
			SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
			astarApproximation = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
		}
	}

		
		/**
		 * 
		 */
		protected AiZone zone = null;
		/** */
		protected AiHero ownHero = null;
		/** */
		protected AiTile currentTile = null;
		/** */
		private Dijkstra dijkstra = null;
		/** */
		private Dijkstra dijkstraSimple = null;
		/** */
		protected Astar astarPrecise = null;
		/** */
		protected Astar astarApproximation = null;
		/** */
		private CalculCommun calculCommun = null;
		/** */
		
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException
	{	ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		calculCommun = new CalculCommun(ai);
		calculCommun.update();
		{ 
			CostCalculator costCalculator = new TileCostCalculator(ai);
			SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
			dijkstra = new Dijkstra(ai,ownHero,costCalculator,successorCalculator);
		}
		{ 
			CostCalculator costCalculator = new PixelCostCalculator(ai);
			SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
			dijkstraSimple = new Dijkstra(ai,ownHero,costCalculator,successorCalculator);
			dijkstraSimple.setMaxHeight(2*(zone.getWidth()+zone.getHeight())); 
		}
		{	CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
			costCalculator.setOpponentCost(1000); 
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

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		AiMode mode = ai.modeHandler.getMode(); //getting the mode
		AiLocation startLocation = new AiLocation(ownHero);
		Map<AiTile, AiSearchNode> map = new HashMap<AiTile, AiSearchNode>();
		try { //find all tile that we can go
			map = dijkstra.startProcess(startLocation);
		}
		catch (LimitReachedException e1) {
		}
		if(map.isEmpty() || map.size()>8) { //if the result is empty or over maxlimit, get the tiles with djikstraSimple
			try {
				map = dijkstraSimple.startProcess(startLocation);
			} catch (LimitReachedException e) {
			}
		}
		if (calculCommun.isDanger(currentTile)) { //if we are in danger
			try {
				//find the safe tile with djikstra
				AiTile tile = dijkstra.processEscapePath(new AiLocation(currentTile)).getLastLocation().getTile();
				result.add(tile);
			} catch (LimitReachedException e) {
			}
		} else if (mode.equals(AiMode.COLLECTING)) { 
			result = calculCommun.getItemsAccessible(map); // get the items accessibles
			if (result.size() == 0) // if there is no items get the mur neighboors
				result = calculCommun.getMursNeighBoorsForCollecte(map);
		} else {
			result = calculCommun.getRivalsAccessible(map); //get the rivals accessible
			if (result.size() == 0) {
				result = calculCommun.getMursForRivals(); // if we cant find a rival, get the murs for rivals
			}
			if (result.size() == 0) { //if we cant find murs for rival, get the other murs.
				result = calculCommun.getMursNeighBoorsForCollecte(map);
			}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException
	{	ai.checkInterruption();

		// on définit les critères
		new DistanceTemps(ai);
		new Concurrence(ai);
		new PertinentCollecteForBonus(ai);
		new MenaceCollecte(ai);
		new ConjonctionMurs(ai);
		new PuissanceRivals(ai);
		new NombreMursAttack(ai);
	
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/** noms du premier cas, utilisé dans {@link #initCriteria} */
	private final String bonusModeCollecte = "BONUSCOLLECTE";
	/** noms du second cas, utilisé dans {@link #initCriteria} */
	private final String mursModeCollecte = "MURCOLLECTE";
	/** noms du troisieme cas, utilisé dans {@link #initCriteria} */
	private final String rivalsModeAttack = "RIVALATTACK";
	/** noms du quatrieme cas, utilisé dans {@link #initCriteria} */
	private final String mursModeAttack = "MURATTACK";
	/** */
	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
	
		Set<AiUtilityCriterion<?,?>> criteriaCollecteBonus = new TreeSet<AiUtilityCriterion<?,?>>();
		criteriaCollecteBonus.add(criterionMap.get(DistanceTemps.NAME));
		criteriaCollecteBonus.add(criterionMap.get(Concurrence.NAME));
		criteriaCollecteBonus.add(criterionMap.get(PertinentCollecteForBonus.NAME));
		criteriaCollecteBonus.add(criterionMap.get(MenaceCollecte.NAME));
		new AiUtilityCase(ai,bonusModeCollecte,criteriaCollecteBonus);
			
		Set<AiUtilityCriterion<?,?>> criteriaCollecteMurs = new TreeSet<AiUtilityCriterion<?,?>>();
		criteriaCollecteMurs.add(criterionMap.get(ConjonctionMurs.NAME));
		criteriaCollecteMurs.add(criterionMap.get(DistanceTemps.NAME));
		criteriaCollecteMurs.add(criterionMap.get(MenaceCollecte.NAME));
		new AiUtilityCase(ai,mursModeCollecte,criteriaCollecteMurs);

		Set<AiUtilityCriterion<?,?>> criteriaAttackRivals = new TreeSet<AiUtilityCriterion<?,?>>();
		criteriaAttackRivals.add(criterionMap.get(DistanceTemps.NAME));
		criteriaAttackRivals.add(criterionMap.get(Concurrence.NAME));
		criteriaAttackRivals.add(criterionMap.get(PuissanceRivals.NAME));
		criteriaAttackRivals.add(criterionMap.get(MenaceCollecte.NAME));
		new AiUtilityCase(ai,rivalsModeAttack,criteriaAttackRivals);
			
		Set<AiUtilityCriterion<?,?>> criteriaAttackMurs = new TreeSet<AiUtilityCriterion<?,?>>();
		criteriaAttackMurs.add(criterionMap.get(DistanceTemps.NAME));
		criteriaAttackMurs.add(criterionMap.get(NombreMursAttack.NAME));
		criteriaAttackMurs.add(criterionMap.get(PuissanceRivals.NAME));
		criteriaAttackMurs.add(criterionMap.get(MenaceCollecte.NAME));
		new AiUtilityCase(ai,mursModeAttack,criteriaAttackMurs);
		
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	AiUtilityCase result = null;
	AiMode mode = ai.modeHandler.getMode();
	AiLocation startLocation = new AiLocation(ownHero.getTile());
	
	Map<AiTile, AiSearchNode> map = new HashMap<AiTile, AiSearchNode>();
	try {
		map = dijkstra.startProcess(startLocation);
	} catch (LimitReachedException e1) {
	}
	if(map.isEmpty()) {
		try {
			map = dijkstraSimple.startProcess(startLocation);
		} catch (LimitReachedException e) {
		}		
	}
	if(mode.equals(AiMode.COLLECTING)) {
		if(calculCommun.getItemsAccessible(map).size()>0) {
			result = caseMap.get(bonusModeCollecte);
		}
		else
			result = caseMap.get(mursModeCollecte);
		
	}
	else {
			Set<AiTile> tiles = calculCommun.getRivalsAccessible(map);
			if(tiles != null && tiles.size()>0) 
				result = caseMap.get(rivalsModeAttack);
			else
				result = caseMap.get(mursModeAttack);
		}
	return result;
	}

	/////////////////////////////////////////////////////////////////
	// REFERENCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initReferenceUtilities() throws StopRequestException
	{	ai.checkInterruption();
	
		// on affecte les valeurs d'utilité
		int utility;
		AiUtilityCombination combi;
		AiMode mode;
		
		// on commence avec le mode collecte
		{	mode = AiMode.COLLECTING;
			utility = 1;
			
			{	// on crée la combinaison (vide pour l'instant)
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),Boolean.FALSE);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),Boolean.FALSE);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),Boolean.FALSE);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),Boolean.FALSE);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),Boolean.FALSE);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),Boolean.FALSE);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),Boolean.FALSE);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),Boolean.FALSE);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),Boolean.FALSE);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(bonusModeCollecte));
				combi.setCriterionValue((PertinentCollecteForBonus)criterionMap.get(PertinentCollecteForBonus.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			utility = 1;
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeCollecte));
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((ConjonctionMurs)criterionMap.get(ConjonctionMurs.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeCollecte));
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((ConjonctionMurs)criterionMap.get(ConjonctionMurs.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeCollecte));
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((ConjonctionMurs)criterionMap.get(ConjonctionMurs.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeCollecte));
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((ConjonctionMurs)criterionMap.get(ConjonctionMurs.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeCollecte));
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((ConjonctionMurs)criterionMap.get(ConjonctionMurs.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeCollecte));
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((ConjonctionMurs)criterionMap.get(ConjonctionMurs.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeCollecte));
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((ConjonctionMurs)criterionMap.get(ConjonctionMurs.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeCollecte));
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((ConjonctionMurs)criterionMap.get(ConjonctionMurs.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			
			
		}
		{
			mode = AiMode.ATTACKING;
			utility = 1;
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),false);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),false);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),false);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),false);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),false);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),false);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),false);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),true);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(rivalsModeAttack));
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),false);
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			utility = 1;
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),true);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),true);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),false);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),false);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(mursModeAttack));
				combi.setCriterionValue((PuissanceRivals)criterionMap.get(PuissanceRivals.NAME),false);
				combi.setCriterionValue((DistanceTemps)criterionMap.get(DistanceTemps.NAME),true);
				combi.setCriterionValue((NombreMursAttack)criterionMap.get(NombreMursAttack.NAME),true);
				combi.setCriterionValue((MenaceCollecte)criterionMap.get(MenaceCollecte.NAME),false);
				defineUtilityValue(mode, combi, utility);
				utility++;			
			}
		}
		
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		super.updateOutput();
	}
}
