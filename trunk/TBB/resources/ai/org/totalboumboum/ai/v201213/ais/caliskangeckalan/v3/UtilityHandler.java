package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
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
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion.Concurrence;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion.ConjonctionMurs;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion.DistanceTemps;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion.MenaceCollecte;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion.NombreMursAttack;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion.PertinentCollecteForBonus;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion.PuissanceRivals;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
@SuppressWarnings("deprecation")
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

		
		/**initialize the zone */
		protected AiZone zone = null;
		/** initialize of own Hero*/
		protected AiHero ownHero = null;
		/** initialize of the tile of own hero */
		protected AiTile currentTile = null;
		/** initialize a dijkstra for all tiles that we can go*/
		private Dijkstra dijkstra = null;
		/** initialize a simple  dijkstra for all tiles that we can go*/
		private Dijkstra dijkstraSimple = null;
		/** initialize an star for direct path */
		protected Astar astarPrecise = null;
		/** initialize an star for indirect path */
		protected Astar astarApproximation = null;
		/**initialize class of calculCommun that has functions commun  */
		private CalculCommun calculCommun = null;
		/** initialize of boolean that control for dropping a bomb for mur*/
		protected boolean murBombing = false;
		/** initialize of boolean that control for dropping a bomb for enemy*/
		protected boolean rivalBombing = false;
		
		/** initialize the tiles of mort subite  */
		Set<AiTile> deathTiles = new TreeSet<AiTile>();
		
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
		List<AiSuddenDeathEvent> suddenDeath = zone.getAllSuddenDeathEvents();
		if(suddenDeath != null && deathTiles == null) {
			deathTiles = new TreeSet<AiTile>();
			deathTiles = calculCommun.suddenDeathTiles();
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
		if(map.isEmpty()) { //if the result is empty or over maxlimit, get the tiles with djikstraSimple
			try {
				map = dijkstraSimple.startProcess(startLocation);
			} catch (LimitReachedException e) {
			}
		}
		if (mode.equals(AiMode.COLLECTING)) { 
			rivalBombing = false;
			result = calculCommun.getItemsAccessible(map,deathTiles); // get the items accessibles
			if (result.size() == 0) {// if there is no items get the mur neighboors {
				result = calculCommun.getMursNeighBoorsForCollecte(map,deathTiles);
				murBombing = true; //set the tile for mur bombing
			}
			else 
				murBombing = false;
		} else {
			result = calculCommun.getRivalsAccessible(map,deathTiles); //get the rivals accessible
			if(result.size() >0) {
				murBombing = false;
				rivalBombing = true;  //set the tile for rival bombing
			}
			if (result.size() == 0) {
				result = getMursForRivals(); // if we cant find a rival, get the murs for rivals
				if(result.size() > 0) {
					murBombing = true;  //set the tile for mur bombing
				}
				else
					murBombing = false;
			}
		}
		return result;
	}
	
	/**
	 * @return if we cant find a path for rivals, this method sets the mur tiles for the rivals using indirect path
	 * @throws StopRequestException
	 */
	public Set<AiTile> getMursForRivals() throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		Set<AiTile> resultTemp = new TreeSet<AiTile>();
		Set<AiTile> tilesCanGo = new TreeSet<AiTile>();
		HashMap<AiTile,Double> farList = new HashMap<AiTile,Double>();
		HashMap<Double,AiTile> farListReverse = new HashMap<Double,AiTile>();
		List<AiHero> rivals = zone.getRemainingHeroes();
		Iterator<AiHero> rivalsIt = rivals.iterator();
		Map<AiTile, AiSearchNode> map = new HashMap<AiTile, AiSearchNode>();
		try {
			map = dijkstraSimple.startProcess(new AiLocation(currentTile));
		} catch (LimitReachedException e) {
		}
		tilesCanGo = calculCommun.getMursNeighBoorsForCollecte(map,deathTiles); // get all mur neighboors that we can go
		while(rivalsIt.hasNext()) {
			ai.checkInterruption();
			AiHero rival = rivalsIt.next();
			if(!rival.equals(ownHero)) {
				AiTile tempTile = rival.getTile(); 
				AiPath tempPath;
				try {
					tempPath = astarApproximation.startProcess(new AiLocation(currentTile),tempTile); //Find the indirect path
					if(tempPath != null) {
						for(int i=0;i< tempPath.getLength();i++) {
							ai.checkInterruption();
							double far = 0D;
							AiTile tileNext = tempPath.getLocation(i).getTile();
							if(tilesCanGo.contains(tileNext)) {
								if(tileNext.getNeighbor(Direction.RIGHT).getBlocks().size()> 0 && tileNext.getNeighbor(Direction.RIGHT).getBlocks().get(0).isDestructible()) {
									resultTemp.add(tileNext);
									far = zone.getTileDistance(new AiLocation(tempTile), new AiLocation(tileNext));
									farList.put(tileNext,far);
								}
								if(tileNext.getNeighbor(Direction.LEFT).getBlocks().size() > 0 && tileNext.getNeighbor(Direction.LEFT).getBlocks().get(0).isDestructible()) {
									resultTemp.add(tileNext);
									far = zone.getTileDistance(new AiLocation(tempTile), new AiLocation(tileNext));
									farList.put(tileNext,far);
								}
								if(tileNext.getNeighbor(Direction.DOWN).getBlocks().size() >0 && tileNext.getNeighbor(Direction.DOWN).getBlocks().get(0).isDestructible()) {
									resultTemp.add(tileNext);
									far = zone.getTileDistance(new AiLocation(tempTile), new AiLocation(tileNext));
									farList.put(tileNext,far);
								}
								if(tileNext.getNeighbor(Direction.UP).getBlocks().size()> 0 &&  tileNext.getNeighbor(Direction.UP).getBlocks().get(0).isDestructible()) {
									resultTemp.add(tileNext);
									far = zone.getTileDistance(new AiLocation(tempTile), new AiLocation(tileNext));
									farList.put(tileNext,far);
								}
							}
						}
						TreeSet<AiTile> tiles = new TreeSet<AiTile>(farList.keySet());
						Iterator<AiTile> tilesIt = tiles.iterator();
						while(tilesIt.hasNext()) {
							ai.checkInterruption();
							AiTile tile = tilesIt.next();
							double value = farList.get(tile);
							farListReverse.put(value, tile);
						}
						TreeSet<Double> values = new TreeSet<Double>(farListReverse.keySet());
						Iterator<Double> valuesIt = values.descendingIterator();
						boolean goOn = true;
						double far = 1000D;
						while(valuesIt.hasNext() && goOn) {
							ai.checkInterruption();
							double valueTemp = valuesIt.next();
							if(valueTemp<far) {
								far = valueTemp;
								AiTile tileTemp = farListReverse.get(valueTemp);
								result = new TreeSet<AiTile>();
								result.add(tileTemp);
							}
						}
						
					}
				}
				
					
				catch (LimitReachedException e) {
				}	
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
		if(calculCommun.getItemsAccessible(map,deathTiles).size()>0) {
			result = caseMap.get(bonusModeCollecte);
		}
		else
			result = caseMap.get(mursModeCollecte);
	}
	else {
			Set<AiTile> tiles = calculCommun.getRivalsAccessible(map,deathTiles);
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
