package org.totalboumboum.ai.v201112.ais.unluyildirim.v3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion.CriterionAccessible;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion.CriterionBesoin;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion.CriterionConcurrence;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion.CriterionFacile;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion.CriterionFaible;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion.CriterionMenace;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion.CriterionTemps;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion.CriterionVoisin;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<UnluYildirim> {
	protected UtilityHandler(UnluYildirim ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	private final String caseName1 = "CAS1";
	private final String caseName2 = "CAS2";
	private final String caseName3 = "CAS3";
	private Boolean pasaccessible = false;
	private Boolean pasaccessible_bonus = false;
	/** */
	public AiZone zone = null;
	/** */
	public AiHero ownHero = null;

	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();

		AiMode mode;
		AiModeHandler<UnluYildirim> mode_handler = ai.getModeHandler();

		List<AiHero> zone_hero;
		List<AiItem> zone_item = null;

		mode = mode_handler.getMode();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		Dijkstra dijkstra = null;

		CostCalculator costCalculator = new TileCostCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		dijkstra = new Dijkstra(ai, ownHero, costCalculator,successorCalculator);
		CriterionAccessible accessible = new CriterionAccessible(ai);
		Set<AiTile> result2 = new TreeSet<AiTile>();// va etre utilisé pour
													// controler si le tile
													// demande est accessible ou
													// pas.

		Set<AiTile> result1 = new TreeSet<AiTile>();
		if (mode == AiMode.COLLECTING)// Si le mode est collecte donc l'agent va
										// choisir soit les items soit les murs
		{
			
			ai.checkInterruption();
			zone_item = zone.getItems();
			// les items de la zone sont stockÃ©s dans la variable zone_item
			if (zone_item.isEmpty())
			// S'il n 'y a pas des items overts dans la zone , l'agant va
			// choisir les murs
			{
				
				ai.checkInterruption();
				AiLocation startLocation = new AiLocation(ownHero);// il
																	// commence
																	// par
																	// location
																	// de notre
																	// hero
				CriterionVoisin criterionvoisin = new CriterionVoisin(ai);

				try {
					HashMap<AiTile, AiSearchNode> map = dijkstra.startProcess(startLocation);// essaie de trouver
															// tous les case
															// possbile d'aller
					Iterator<AiTile> itr = map.keySet().iterator();
					while (itr.hasNext()) {
						
						ai.checkInterruption();
						AiTile tile1 = itr.next();
						if (criterionvoisin.processValue(tile1) != 0)
							result.add(tile1);
					}

				} catch (LimitReachedException e) { // e.printStackTrace();
				}

			} else {// S'il y a des items ouverts dans la zone

				for (AiItem item : zone_item)
				// S'il y a des items encore , l'agent va choisir les items

				{
					ai.checkInterruption();
					if (!accessible.processValue(item.getTile()))
						{;
						result2.add(item.getTile());// on ajoute des éléments
						}
					else 
						{
						result1.add(item.getTile());// pas accessibles a result2
						}
				}
				
				if(!result1.isEmpty())
				{
					
					//ai.moveHandler.isBombing= false;
					result.addAll(result1);
				}
				else
				{
					
					//ai.moveHandler.isBombing= true;
					for(AiItem item : zone.getItems())
					{
						ai.checkInterruption();
						HashMap<AiTile, AiSearchNode> map =null;
						boolean retour = true;
						pasaccessible_bonus=true;
					try{
						
						
						AiLocation startLocation = new AiLocation(item.getTile());
						map = dijkstra.startProcess(startLocation);
						
					   }  catch (LimitReachedException e) {
						
						//e.printStackTrace();
					}
					ai.checkInterruption();
						Iterator<AiTile> itr = map.keySet().iterator();
						AiFire fire = ownHero.getBombPrototype().getFirePrototype();
						while (itr.hasNext() && retour) {
							
							ai.checkInterruption();
							AiTile tile1 = itr.next();
							
							for(AiTile tiles : tile1.getNeighbors())
							{
								ai.checkInterruption();
								
								if(!tiles.getBlocks().isEmpty())
								{
									
									if(tiles.getBlocks().get(0).isDestructible())
									{
										
										for(AiTile tile2 :  tiles.getNeighbors())
										{
											ai.checkInterruption();
											if(accessible.processValue(tile2) && tile2.isCrossableBy(fire))
											{
												result.add(tile2);
											}
										}
									
									}
								}
					      }
				}
				}
					
				}
			}
		}
		else {
			
			// Si le mode est mode attaque ; donc l'agent va choisir les murs
			// voisins des adversaires
			result1 = new TreeSet<AiTile>();
			result2 = new TreeSet<AiTile>();
			zone_item = zone.getItems();

			int range = ownHero.getBombRange();
			AiFire fire = ownHero.getBombPrototype().getFirePrototype();
			zone_hero = zone.getRemainingOpponents();

			for (AiHero hero : zone_hero)// pour chaque adversaire dans la zone
			{
				ai.checkInterruption();

				for (Direction d : Direction.getPrimaryValues()) {
					ai.checkInterruption();
					AiTile voisin = hero.getTile();
					int i = 1;
					boolean retour = false;
					while (i <= range && !retour) {
						ai.checkInterruption();
						voisin = voisin.getNeighbor(d);
						if (!accessible.processValue(voisin) && voisin.isCrossableBy(fire)) {
						
							result2.add(voisin);}
						if (accessible.processValue(voisin) && voisin.isCrossableBy(fire))
							result1.add(voisin);
						else
							retour = true;
						i++;
					}
				}// donne les result avec le tile qui se trouve au tour de
					// l'adversaire

			}
			
			//result.addAll(result1);
		
		if (!result1.isEmpty()) {
			
			//ai.moveHandler.isBombing=true;
			
			result.addAll(result1);
		}// si result2 est vide ça veut dire qu'il y a des objet accessible
		else {// Sinon l'agent va choisir les tiles proche de lui
			ai.checkInterruption();
			pasaccessible=true;
			for(AiTile tile : result2 )
			{
				ai.checkInterruption();
				
				try {
					ai.checkInterruption();
					boolean retour = true;
					AiLocation startLocation = new AiLocation(tile);
					HashMap<AiTile, AiSearchNode> map = dijkstra.startProcess(startLocation);
					
					Iterator<AiTile> itr = map.keySet().iterator();
					while (itr.hasNext() && retour) {
						ai.checkInterruption();
						
						AiTile tile1 = itr.next();
						
						for(AiTile tiles : tile1.getNeighbors())
						{
							ai.checkInterruption();
							
							if(!tiles.getBlocks().isEmpty())
							{
								
								if(tiles.getBlocks().get(0).isDestructible())
								{
									ai.checkInterruption();
									
									for(AiTile tile2 :  tiles.getNeighbors())
									{
										ai.checkInterruption();
										if(accessible.processValue(tile2) && tile2.isCrossableBy(fire))
										{
											result.add(tile2);
										}
									}
								
								}
							}
						
						}
						
					}
				
				} 
			
				catch (LimitReachedException e) {
					
					//e.printStackTrace();
				}
				
			}
		}
		}
		if (result.isEmpty()) {
			
			//ai.moveHandler.isBombing=false;
			AiLocation startLocation = new AiLocation(ownHero.getTile());
			HashMap<AiTile, AiSearchNode> map;
			AiFire fire = ownHero.getBombPrototype().getFirePrototype();
			
			boolean retour = true;
			try {
				ai.checkInterruption();
				map = dijkstra.startProcess(startLocation);
				result.addAll(map.keySet());
				Iterator<AiTile> itr = map.keySet().iterator();
				while (itr.hasNext() && retour) {
					
					ai.checkInterruption();
					AiTile tile1 = itr.next();
					for(AiTile tiles : tile1.getNeighbors())
					{
						ai.checkInterruption();
						
						if(!tiles.getBlocks().isEmpty())
						{
							
							if(tiles.getBlocks().get(0).isDestructible())
							{
							
								for(AiTile tile2 :  tiles.getNeighbors())
								{
									ai.checkInterruption();
									if(accessible.processValue(tile2) && tile2.isCrossableBy(fire))
									{
										result.add(tile2);
									}
								}
							
							}
						}
					}
					
				}
			} catch (LimitReachedException e) {
				
				//e.printStackTrace();
			}
			

		}
		

		return result;
	}

	
	
	
	
	
	
	@Override
	protected void initCriteria() throws StopRequestException {
		ai.checkInterruption();

		CriterionMenace criterionMenace = new CriterionMenace(ai);
		CriterionTemps criterionTemps = new CriterionTemps(ai);
		CriterionBesoin criterionBesoin = new CriterionBesoin(ai);
		CriterionConcurrence criterionConcurrence = new CriterionConcurrence(ai);
		CriterionAccessible criterionAccessible = new CriterionAccessible(ai);
		CriterionFaible criterionFaible = new CriterionFaible(ai);
		CriterionFacile criterionFacile = new CriterionFacile(ai);
		CriterionVoisin criterionVoisin = new CriterionVoisin(ai);

		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criterionMenace);
		criteria.add(criterionTemps);
		criteria.add(criterionBesoin);
		criteria.add(criterionConcurrence);
		criteria.add(criterionAccessible);
		AiUtilityCase case1 = new AiUtilityCase(caseName1, criteria);
		// CAS1 represente le mode collecte quand il y a des items ouvert dans
		// la zone.

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criterionMenace);
		criteria.add(criterionConcurrence);
		criteria.add(criterionAccessible);
		criteria.add(criterionVoisin);
		AiUtilityCase case2 = new AiUtilityCase(caseName2, criteria);
		// CAS2 represent les cas lorsqu'il n'y a pas des items visible mais des
		// items caches dans la zone.

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criterionMenace);
		criteria.add(criterionTemps);
		criteria.add(criterionFaible);
		criteria.add(criterionFacile);
		AiUtilityCase case3 = new AiUtilityCase(caseName3, criteria);
		// CAS3 represent les cas dans le mode attaque

		cases.put(caseName1, case1);
		cases.put(caseName2, case2);
		cases.put(caseName3, case3);

		int utility = 1;
		AiUtilityCombination combi;
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 1);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 1);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 1);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 1);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 2);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 2);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 2);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 2);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 2);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 2);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 2);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 2);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 3);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 3);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 3);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 3);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 3);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 3);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, false);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 3);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionVoisin, 3);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionConcurrence, true);
			combi.setCriterionValue(criterionAccessible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);

			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);
			utility++;
		}
		// 24
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);
			utility++;
		}
		// 25
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);

			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);

			utility++;
		}

		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);

			utility++;
		}

		// 29
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);

			utility++;
		}

		// 30
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		// 31
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		// 32
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		// 33
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		// 34
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		// 35
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		// 36
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		// 37
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, false);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, false);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, false);

			referenceUtilities.put(combi, utility);

			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);

			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionBesoin, true);
			combi.setCriterionValue(criterionAccessible, true);
			combi.setCriterionValue(criterionConcurrence, true);

			referenceUtilities.put(combi, utility);

			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionFacile, false);
			combi.setCriterionValue(criterionFaible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionFacile, false);
			combi.setCriterionValue(criterionFaible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionFacile, true);
			combi.setCriterionValue(criterionFaible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionFacile, true);
			combi.setCriterionValue(criterionFaible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionFacile, false);
			combi.setCriterionValue(criterionFaible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionFacile, false);
			combi.setCriterionValue(criterionFaible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionFacile, true);
			combi.setCriterionValue(criterionFaible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, false);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionFacile, true);
			combi.setCriterionValue(criterionFaible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionFacile, false);
			combi.setCriterionValue(criterionFaible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionFacile, false);
			combi.setCriterionValue(criterionFaible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionFacile, true);
			combi.setCriterionValue(criterionFaible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionMenace, false);
			combi.setCriterionValue(criterionFacile, true);
			combi.setCriterionValue(criterionFaible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionFacile, false);
			combi.setCriterionValue(criterionFaible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionFacile, false);
			combi.setCriterionValue(criterionFaible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionFacile, true);
			combi.setCriterionValue(criterionFaible, false);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case3);

			combi.setCriterionValue(criterionTemps, true);
			combi.setCriterionValue(criterionMenace, true);
			combi.setCriterionValue(criterionFacile, true);
			combi.setCriterionValue(criterionFaible, true);
			referenceUtilities.put(combi, utility);
			utility++;
		}

	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		AiUtilityCase result = null;

		AiMode mode;
		AiModeHandler<UnluYildirim> mode_handler = ai.getModeHandler();
		AiZone zone = ai.getZone();

		mode = mode_handler.getMode();

		if (mode == AiMode.COLLECTING)// Si le mode est collecte donc l'agent va
										// choisir soit les items soit les murs
		{
			if (zone.getItems().isEmpty() || pasaccessible) {
				result = cases.get(caseName2);
			} else if (!zone.getItems().isEmpty() || !pasaccessible)
				result = cases.get(caseName1);

		} else {

			if (pasaccessible) // Si les adversaire ne sont pas accessible ,
								// donc l'agent va faire le controle par les
								// criteres definis par le cas2
			{
				if (!zone.getItems().isEmpty() && !pasaccessible_bonus)
					result = cases.get(caseName1);

				else
					result = cases.get(caseName2);
			} else
				result = cases.get(caseName3);

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
