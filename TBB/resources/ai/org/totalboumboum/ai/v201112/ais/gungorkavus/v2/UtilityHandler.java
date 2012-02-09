package org.totalboumboum.ai.v201112.ais.gungorkavus.v2;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.CaseVidePertinent;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.ItemVisibleConcurrence;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.ItemVisibleDistance;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.ItemVisibleMenace;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.ItemVisiblePertinent;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.VDAdvConcurrence;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.VDAdvDistance;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.VDAdvPertinent;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.VDMDestDistance;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.VDMDestIntersection;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.VDMDestMPBConcurrence;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.VDMDestMPBDistance;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.VDMDestMPBPertinent;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion.VDMDestPertinent;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<GungorKavus>
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
	protected UtilityHandler(GungorKavus ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		verbose = false;
	
	
		
		
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	private final String caseName1 = "itemVisible";
	private final String caseName2 = "vDMDestructible";
	private final String caseName3 = "vDMDestMenaceParUneBombe";
	private final String caseName4 = "vDAdversaires";
	private final String caseName5 = "caseVide";
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	
		ai.checkInterruption();
		
		Set<AiTile> result = new TreeSet<AiTile>();
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		List<AiTile> listTiles = zone.getTiles();
		AiTile tile = null;
		for(int i=0;i<listTiles.size();i++){
			ai.checkInterruption();
			tile = listTiles.get(i);
			if(tile.isCrossableBy(ownHero, false, false, false, false, true, true)){
				result.add(tile);
			}
		}
		
		
		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();
		
		//Les criteres du cas d'Item visible
		ItemVisiblePertinent IVpertinent = new ItemVisiblePertinent(ai);
		ItemVisibleDistance IVdistance = new ItemVisibleDistance(ai);
		ItemVisibleConcurrence IVconcurrence = new ItemVisibleConcurrence(ai);
		ItemVisibleMenace IVmenace = new ItemVisibleMenace(ai);
		
		
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(IVpertinent);
		criteria.add(IVdistance);
		criteria.add(IVconcurrence);
		criteria.add(IVmenace);
		AiUtilityCase case1 = new AiUtilityCase(caseName1,criteria);
			
		//Les criteres du cas des Voisinages des murs destructibles
		VDMDestPertinent VDMDestpertinent = new VDMDestPertinent(ai);
		VDMDestDistance VDMDestdistance = new VDMDestDistance(ai);
		VDMDestIntersection VDMDestintersection = new VDMDestIntersection(ai);
 		
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		
		criteria.add(VDMDestpertinent);
		criteria.add(VDMDestdistance);
		criteria.add(VDMDestintersection);
		AiUtilityCase case2 = new AiUtilityCase(caseName2,criteria);

		//Les criteres du cas des Voisinages des murs destructibles menace par une bombe
		VDMDestMPBPertinent VDMDestMPBpertinent = new VDMDestMPBPertinent(ai);
		VDMDestMPBDistance VDMDestMPBdistance = new VDMDestMPBDistance(ai);
		VDMDestMPBConcurrence VDMDestMPBconcurrence = new VDMDestMPBConcurrence(ai);
 		
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		
		criteria.add(VDMDestMPBpertinent);
		criteria.add(VDMDestMPBdistance);
		criteria.add(VDMDestMPBconcurrence);
		
		AiUtilityCase case3 = new AiUtilityCase(caseName3,criteria);
		
		//Les criteres du cas des Voisinages des adversaires.
		
		VDAdvPertinent VDAdvpertinent = new VDAdvPertinent(ai);
		VDAdvDistance VDAdvdistance = new VDAdvDistance(ai);
		VDAdvConcurrence VDAdvconcurrence = new VDAdvConcurrence(ai);
 		
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		
		criteria.add(VDAdvpertinent);
		criteria.add(VDAdvdistance);
		criteria.add(VDAdvconcurrence);
		
		AiUtilityCase case4 = new AiUtilityCase(caseName4,criteria);
		
		//case vide
		CaseVidePertinent caseVidePertinent = new CaseVidePertinent(ai);
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		
		criteria.add(caseVidePertinent);
		
		
		AiUtilityCase case5 = new AiUtilityCase(caseName5,criteria);
		
		
		cases.put(caseName1,case1);
		cases.put(caseName2,case2);
		cases.put(caseName3,case3);
		cases.put(caseName4,case4);
		cases.put(caseName5,case5);	
		
		int utility = 1;
		AiUtilityCombination combi;
		
		
		/*
		 * 
		 * L'orde des combinaisons du mode Collecte
		 * 
		 */
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);			
			combi.setCriterionValue(IVpertinent,true);
			combi.setCriterionValue(IVdistance,true);
			combi.setCriterionValue(IVconcurrence,false);
			combi.setCriterionValue(IVmenace,false);		
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,false);
			combi.setCriterionValue(IVdistance,true);
			combi.setCriterionValue(IVconcurrence,false);
			combi.setCriterionValue(IVmenace,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,true);
			combi.setCriterionValue(IVdistance,false);
			combi.setCriterionValue(IVconcurrence,false);
			combi.setCriterionValue(IVmenace,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(VDMDestpertinent,true);
			combi.setCriterionValue(VDMDestdistance,true);
			combi.setCriterionValue(VDMDestintersection,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(VDMDestpertinent,true);
			combi.setCriterionValue(VDMDestdistance,true);
			combi.setCriterionValue(VDMDestintersection,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles menace par une bombe
		{	
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(VDMDestMPBpertinent,true);
			combi.setCriterionValue(VDMDestMPBdistance,true);
			combi.setCriterionValue(VDMDestMPBconcurrence,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(VDMDestpertinent,true);
			combi.setCriterionValue(VDMDestdistance,false);
			combi.setCriterionValue(VDMDestintersection,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(VDMDestpertinent,true);
			combi.setCriterionValue(VDMDestdistance,false);
			combi.setCriterionValue(VDMDestintersection,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles menace par une bombe
		{	
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(VDMDestMPBpertinent,true);
			combi.setCriterionValue(VDMDestMPBdistance,false);
			combi.setCriterionValue(VDMDestMPBconcurrence,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(VDMDestpertinent,false);
			combi.setCriterionValue(VDMDestdistance,true);
			combi.setCriterionValue(VDMDestintersection,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(VDMDestpertinent,false);
			combi.setCriterionValue(VDMDestdistance,true);
			combi.setCriterionValue(VDMDestintersection,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(VDMDestpertinent,false);
			combi.setCriterionValue(VDMDestdistance,false);
			combi.setCriterionValue(VDMDestintersection,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		//La combi du cas des voisinages des murs detructibles menace par une bombe
		{	
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(VDMDestMPBpertinent,false);
			combi.setCriterionValue(VDMDestMPBdistance,true);
			combi.setCriterionValue(VDMDestMPBconcurrence,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(VDMDestpertinent,false);
			combi.setCriterionValue(VDMDestdistance,false);
			combi.setCriterionValue(VDMDestintersection,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible 
		{	
			combi = new AiUtilityCombination(case1);			
			combi.setCriterionValue(IVpertinent,true);
			combi.setCriterionValue(IVdistance,true);
			combi.setCriterionValue(IVconcurrence,true);
			combi.setCriterionValue(IVmenace,false);		
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,true);
			combi.setCriterionValue(IVdistance,false);
			combi.setCriterionValue(IVconcurrence,true);
			combi.setCriterionValue(IVmenace,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,false);
			combi.setCriterionValue(IVdistance,true);
			combi.setCriterionValue(IVconcurrence,true);
			combi.setCriterionValue(IVmenace,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles menace par une bombe
		{	
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(VDMDestMPBpertinent,true);
			combi.setCriterionValue(VDMDestMPBdistance,true);
			combi.setCriterionValue(VDMDestMPBconcurrence,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles menace par une bombe
		{	
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(VDMDestMPBpertinent,true);
			combi.setCriterionValue(VDMDestMPBdistance,false);
			combi.setCriterionValue(VDMDestMPBconcurrence,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,true);
			combi.setCriterionValue(IVdistance,true);
			combi.setCriterionValue(IVconcurrence,false);
			combi.setCriterionValue(IVmenace,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,true);
			combi.setCriterionValue(IVdistance,false);
			combi.setCriterionValue(IVconcurrence,false);
			combi.setCriterionValue(IVmenace,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,false);
			combi.setCriterionValue(IVdistance,false);
			combi.setCriterionValue(IVconcurrence,false);
			combi.setCriterionValue(IVmenace,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,true);
			combi.setCriterionValue(IVdistance,true);
			combi.setCriterionValue(IVconcurrence,true);
			combi.setCriterionValue(IVmenace,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,true);
			combi.setCriterionValue(IVdistance,false);
			combi.setCriterionValue(IVconcurrence,true);
			combi.setCriterionValue(IVmenace,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles menace par une bombe
		{	
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(VDMDestMPBpertinent,false);
			combi.setCriterionValue(VDMDestMPBdistance,true);
			combi.setCriterionValue(VDMDestMPBconcurrence,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,false);
			combi.setCriterionValue(IVdistance,true);
			combi.setCriterionValue(IVconcurrence,false);
			combi.setCriterionValue(IVmenace,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,false);
			combi.setCriterionValue(IVdistance,true);
			combi.setCriterionValue(IVconcurrence,true);
			combi.setCriterionValue(IVmenace,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas des voisinages des murs detructibles menace par une bombe
		{	
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(VDMDestMPBpertinent,false);
			combi.setCriterionValue(VDMDestMPBdistance,false);
			combi.setCriterionValue(VDMDestMPBconcurrence,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(VDMDestMPBpertinent,false);
			combi.setCriterionValue(VDMDestMPBdistance,false);
			combi.setCriterionValue(VDMDestMPBconcurrence,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,false);
			combi.setCriterionValue(IVdistance,false);
			combi.setCriterionValue(IVconcurrence,false);
			combi.setCriterionValue(IVmenace,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,false);
			combi.setCriterionValue(IVdistance,false);
			combi.setCriterionValue(IVconcurrence,true);
			combi.setCriterionValue(IVmenace,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		//La combi du cas d'item visible
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(IVpertinent,false);
			combi.setCriterionValue(IVdistance,false);
			combi.setCriterionValue(IVconcurrence,false);
			combi.setCriterionValue(IVmenace,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		
		{	
			combi = new AiUtilityCombination(case5);
			combi.setCriterionValue(caseVidePertinent,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case5);
			combi.setCriterionValue(caseVidePertinent,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		maxUtilities.put(AiMode.COLLECTING,utility-1);

		
		/*
		 * 
		 * L'ordre de combinaison de mode attaque
		 * 
		 * 
		 */
		
		{	
			combi = new AiUtilityCombination(case4);
			combi.setCriterionValue(VDAdvpertinent,true);
			combi.setCriterionValue(VDAdvdistance,true);
			combi.setCriterionValue(VDAdvconcurrence,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case4);
			combi.setCriterionValue(VDAdvpertinent,true);
			combi.setCriterionValue(VDAdvdistance,true);
			combi.setCriterionValue(VDAdvconcurrence,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case4);
			combi.setCriterionValue(VDAdvpertinent,true);
			combi.setCriterionValue(VDAdvdistance,false);
			combi.setCriterionValue(VDAdvconcurrence,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case4);
			combi.setCriterionValue(VDAdvpertinent,false);
			combi.setCriterionValue(VDAdvdistance,false);
			combi.setCriterionValue(VDAdvconcurrence,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case4);
			combi.setCriterionValue(VDAdvpertinent,true);
			combi.setCriterionValue(VDAdvdistance,false);
			combi.setCriterionValue(VDAdvconcurrence,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case4);
			combi.setCriterionValue(VDAdvpertinent,false);
			combi.setCriterionValue(VDAdvdistance,true);
			combi.setCriterionValue(VDAdvconcurrence,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case4);
			combi.setCriterionValue(VDAdvpertinent,false);
			combi.setCriterionValue(VDAdvdistance,false);
			combi.setCriterionValue(VDAdvconcurrence,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case4);
			combi.setCriterionValue(VDAdvpertinent,false);
			combi.setCriterionValue(VDAdvdistance,false);
			combi.setCriterionValue(VDAdvconcurrence,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case4);
			combi.setCriterionValue(VDAdvpertinent,false);
			combi.setCriterionValue(VDAdvdistance,true);
			combi.setCriterionValue(VDAdvconcurrence,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		
		//case vide
		{	
			combi = new AiUtilityCombination(case5);
			combi.setCriterionValue(caseVidePertinent,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	
			combi = new AiUtilityCombination(case5);
			combi.setCriterionValue(caseVidePertinent,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
	
		
		maxUtilities.put(AiMode.ATTACKING,utility-1);
		
	}


	
	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	
	ai.checkInterruption();
	
	AiUtilityCase result = null;
	AiZone zone = ai.getZone();
	AiHero ownHero = zone.getOwnHero();
	Set<AiTile> destBlockTileL = selectTiles();
	destBlockTileL.clear();
	boolean destKont = false;
	boolean menaceDestKont = false;
	List<AiBlock> destBlockL = zone.getDestructibleBlocks();
	
	for(int i=0;i<destBlockL.size();i++){
		ai.checkInterruption();
		destBlockTileL.add(destBlockL.get(i).getTile());
	}
	
	List<AiTile> neigBL = tile.getNeighbors();
	for(int i = 0;i<neigBL.size();i++){
		ai.checkInterruption();
		if(destBlockTileL.contains(neigBL.get(i))){
				destKont = true;
				break;
		}
	}
	
	List<AiBomb> bombL = zone.getBombs();
	for(int i = 0;i<bombL.size();i++){
		ai.checkInterruption();
		for(int j = 0;j<neigBL.size();j++){
			ai.checkInterruption();
			if(bombL.get(i).getBlast().contains(neigBL.get(j))&&destKont){
				menaceDestKont = true;
			}
		}
	}
	
	if(ai.modeHandler.getMode()==AiMode.COLLECTING)
	{
		if(tile.getItems().size()>0)
		{	
			result = cases.get(caseName1);
		}
		else if(menaceDestKont){
			result = cases.get(caseName3);
		}
		else if(destKont){	
			result = cases.get(caseName2);
		}
		else 
			result = cases.get(caseName5);
	}
	else if(ai.modeHandler.getMode()==AiMode.ATTACKING)
	{
		if((tile.getHeroes().size()==1 && !tile.getHeroes().contains(ownHero)) || tile.getHeroes().size()>1)
			result = cases.get(caseName4);
		else
			result = cases.get(caseName5);
	}	
	
		return result;
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
