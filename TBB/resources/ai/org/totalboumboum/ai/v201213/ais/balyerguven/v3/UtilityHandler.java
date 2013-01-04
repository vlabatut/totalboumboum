package org.totalboumboum.ai.v201213.ais.balyerguven.v3;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion.Adversaire;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion.Duree;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion.MalusMenace;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion.Menace;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion.MurVoiAdv;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion.NbrMurDest;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion.Pertinence;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion.PertinentCaseVide;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * our utility handler class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class UtilityHandler extends AiUtilityHandler<BalyerGuven>
{	
	/** verbose
	 * @param ai
	 * @throws StopRequestException
	 */
	protected UtilityHandler(BalyerGuven ai) throws StopRequestException
	{	
		
		super(ai);
		ai.checkInterruption();
		verbose = false;
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	@Override
	protected void resetCustomData() throws StopRequestException
	{	
		ai.checkInterruption();
	}
	
	/**
	 * game zone
	 */
	AiZone gameZone = ai.getZone();
	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * list of possible tiles
	 */
	public List<AiTile> possibleTiles= new ArrayList <AiTile> (ai.getZone().getTiles()); 
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	
		ai.checkInterruption();
		
		possibleTiles.clear();
		AiHero ourhero = this.ai.getZone().getOwnHero();
		AiTile ourtile = ourhero.getTile();
		Set<AiTile> result = new TreeSet<AiTile>();
		AiTile aitile = ourtile;

		Queue<AiTile> qe = new LinkedList<AiTile>();
		qe.add(aitile);
		while (!qe.isEmpty()) {
			ai.checkInterruption();
			aitile = qe.poll();
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if (aitile.getNeighbor(direction).getBombs().isEmpty()
						&& aitile.getNeighbor(direction).getBlocks().isEmpty()
						&& !qe.contains(aitile.getNeighbor(direction))
						&& !result.contains(aitile.getNeighbor(direction))
						&& !aitile.getNeighbor(direction).equals(ourtile)) {
					qe.add(aitile.getNeighbor(direction));
				}
			}
			if (!qe.isEmpty()) {

				aitile = qe.peek();

				result.add(aitile);

			} else {
				break;
			}
		}
		
		
		result.add(ourtile);
		
		
		for(AiTile dangerTiles :ai.getCurrentDangerousTiles() ){
			ai.checkInterruption();
			if(result.contains(dangerTiles)){
				result.remove(dangerTiles);
			}
		
		}
		
		possibleTiles.addAll(result);

		return result;

	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();
		
		new MurVoiAdv(ai);
		new Duree(ai);
		new Pertinence(ai);
		new NbrMurDest(ai);
		new Menace(ai);
	    new Adversaire(ai);	
	    new PertinentCaseVide(ai);
	    new MalusMenace(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms du premier cas*/
	private final String itemVisibleBonus = "itemVisibleBonus";
	/** noms du deuxième cas*/
	private final String murDest = "murDest";
	/** noms du troisieme cas*/
	private final String caseVide = "caseVide";
	/** noms du quatriemment cas*/
	private final String casVoiAdv = "casVoiAdv";
	/*** noms du cinquieme cas */
	private final String itemVisibleMalus = "itemVisibleMalus";

	
	
	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;

		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Menace.NAME));
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(Pertinence.NAME));
		criteria.add(criterionMap.get(Adversaire.NAME));		
		AiUtilityCase caseitemVisibleBonus = new AiUtilityCase(ai,itemVisibleBonus,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(MalusMenace.NAME));
		AiUtilityCase caseitemVisibleMalus = new AiUtilityCase(ai,itemVisibleMalus,criteria);
	
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Pertinence.NAME));
		criteria.add(criterionMap.get(Menace.NAME));
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(NbrMurDest.NAME));
		AiUtilityCase casemurDest = new AiUtilityCase(ai,murDest,criteria);

		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Menace.NAME));
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(MurVoiAdv.NAME));
		AiUtilityCase casecasVoiAdv = new AiUtilityCase(ai,casVoiAdv,criteria);

		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(PertinentCaseVide.NAME));
		AiUtilityCase caseCaseVide = new AiUtilityCase(ai,caseVide,criteria);
		


		caseMap.put( itemVisibleBonus, caseitemVisibleBonus);
		caseMap.put( murDest, casemurDest );
		caseMap.put( caseVide, caseCaseVide );
		caseMap.put( casVoiAdv, casecasVoiAdv );
		caseMap.put( itemVisibleMalus, caseitemVisibleMalus);


	}

	
	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		AiUtilityCase result = null;
		
		AiMode mode = this.ai.modeHandler.getMode();
		
		//Tile identification for collect mode

		if (mode.equals(AiMode.COLLECTING)) {
			
			// Item visible identification
			for(AiItem item:tile.getItems()){
				ai.checkInterruption();
			
				if(item.getType().equals(AiItemType.EXTRA_BOMB) ||
						item.getType().equals(AiItemType.EXTRA_FLAME) ||
						item.getType().equals(AiItemType.EXTRA_SPEED) ||
						item.getType().equals(AiItemType.GOLDEN_BOMB) ||
						item.getType().equals(AiItemType.GOLDEN_FLAME) ||
						item.getType().equals(AiItemType.GOLDEN_SPEED) ||
						item.getType().equals(AiItemType.RANDOM_EXTRA)){
					result = caseMap.get(itemVisibleBonus);
					return result;
					}
				else if (item.getType().equals(AiItemType.NO_BOMB) ||
					item.getType().equals(AiItemType.NO_FLAME) ||
					item.getType().equals(AiItemType.NO_SPEED) ||
					item.getType().equals(AiItemType.ANTI_BOMB) ||
					item.getType().equals(AiItemType.ANTI_FLAME) ||
					item.getType().equals(AiItemType.ANTI_SPEED) ||
					item.getType().equals(AiItemType.RANDOM_NONE)){
					
					result = caseMap.get(itemVisibleMalus);
					return result;
				}

			}	
	
			//Mur Destructible identification
			
			for (Direction direction : Direction.getPrimaryValues()) {
				
				ai.checkInterruption();
				
				for (AiBlock currentBlock : tile.getNeighbor(direction).getBlocks()) {
					
					ai.checkInterruption();
					// Destructible wall identification
					
					if (currentBlock.isDestructible()) {
						
						result = caseMap.get(murDest);
						return result;

					}
				}
			}
		
		}
		
		
		//Mode Attacking
		else {
			AiZone zone=ai.getZone();
			AiHero myHero=zone.getOwnHero();
			
			// Case Voisin Adversaire identification
			
			if (!zone.getRemainingOpponents().isEmpty()) {
			
				for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				
				for(int i=0;i<myHero.getBombRange();i++){
					ai.checkInterruption();
					AiHero enemy=this.ai.getNearestEnemy();
					if(tile.getNeighbor(direction).equals(enemy)){
						
						result = caseMap.get(casVoiAdv);
						
						return result;
						
					}
				}
				
				}
				
			}
			
			//Mur Destructible identification
			
			for (Direction direction : Direction.getPrimaryValues()) {
				
				ai.checkInterruption();
				
				for (AiBlock currentBlock : tile.getNeighbor(direction).getBlocks()) {
					
					ai.checkInterruption();
					// Destructible wall identification
					
					if (currentBlock.isDestructible()) {
						
						result = caseMap.get(murDest);
						return result;

					}
				}
			}
			}
		result = caseMap.get(caseVide);
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
		
			{
				combi = new AiUtilityCombination(caseMap.get(itemVisibleMalus));
				combi.setCriterionValue((MalusMenace)criterionMap.get(MalusMenace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, -1);
			}
		{	combi = new AiUtilityCombination(caseMap.get(caseVide));
		combi.setCriterionValue((PertinentCaseVide)criterionMap.get(PertinentCaseVide.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
		{	combi = new AiUtilityCombination(caseMap.get(caseVide));
		combi.setCriterionValue((PertinentCaseVide)criterionMap.get(PertinentCaseVide.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
		{	combi = new AiUtilityCombination(caseMap.get(caseVide));
		combi.setCriterionValue((PertinentCaseVide)criterionMap.get(PertinentCaseVide.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
		{	combi = new AiUtilityCombination(caseMap.get(caseVide));
		combi.setCriterionValue((PertinentCaseVide)criterionMap.get(PertinentCaseVide.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
		
	}
		
		{
			combi = new AiUtilityCombination(caseMap.get(itemVisibleMalus));
			combi.setCriterionValue((MalusMenace)criterionMap.get(MalusMenace.NAME),Boolean.FALSE);
		}
		
		
		
	
		{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
}

	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
}
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
		combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
}
		
		//item visible
	{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}

{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	
	combi = new AiUtilityCombination(caseMap.get(itemVisibleBonus));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
	// on rajoute la combinaison dans la map, avec son utilité
	defineUtilityValue(mode, combi, utility);
	// on incrémente l'utilité pour la combinaison suivante
	utility++;
}
	
	// le deuxième cas a un critère boolean [1,3] 
	// et un critère chaine de caractères à 5 valeurs
	// possibles, donc ça fait 15 combinaisons au total.
	// la définition de l'utilité de ces combinaisons
	// se fait de la même façon que ci dessus
	

	// ......
	// etc. pour les 14 autres combinaisons (qui doivent toutes être définies
	// afin de leur associer une valeur d'utilité à chacune)
}

// on traite maintenant le mode attaque
{	mode = AiMode.ATTACKING;
	utility = 1;
	// pour simplifier, on ne met qu'un seul cas : le troisième
	// il n'a qu'un seul critère, défini sur un domaine de 5 valeurs
	
	{
		combi = new AiUtilityCombination(caseMap.get(itemVisibleMalus));
		combi.setCriterionValue((MalusMenace)criterionMap.get(MalusMenace.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, -1);
	}
	//case vide
	{	combi = new AiUtilityCombination(caseMap.get(caseVide));
	combi.setCriterionValue((PertinentCaseVide)criterionMap.get(PertinentCaseVide.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
	{	combi = new AiUtilityCombination(caseMap.get(caseVide));
	combi.setCriterionValue((PertinentCaseVide)criterionMap.get(PertinentCaseVide.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
	{	combi = new AiUtilityCombination(caseMap.get(caseVide));
	combi.setCriterionValue((PertinentCaseVide)criterionMap.get(PertinentCaseVide.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
	{	combi = new AiUtilityCombination(caseMap.get(caseVide));
	combi.setCriterionValue((PertinentCaseVide)criterionMap.get(PertinentCaseVide.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
	

	{
		combi = new AiUtilityCombination(caseMap.get(itemVisibleMalus));
		combi.setCriterionValue((MalusMenace)criterionMap.get(MalusMenace.NAME),Boolean.FALSE);
	}
	
	
	{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}

{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
{	combi = new AiUtilityCombination(caseMap.get(murDest));
	combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
	combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
	combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
	combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
	defineUtilityValue(mode, combi, utility);
	utility++;
}
	
	{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
		combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.FALSE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
		defineUtilityValue(mode, combi, utility);
		utility++;
	}
	{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
		combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
		combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.TRUE);
		combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
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
	{	
		ai.checkInterruption();
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	}
}
