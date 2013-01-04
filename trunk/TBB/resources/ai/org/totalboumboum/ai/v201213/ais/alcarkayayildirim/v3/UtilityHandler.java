package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3;

import java.util.HashMap;
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
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.AttaqueBestPertinence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.AttaqueItemVisible;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.AttaquePertinence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.AvancePertinence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.CaseVidePertinent;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.CollectePertinence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.Duree;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.NbrAdjacentMurs;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.NonConcurrence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.Securite;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion.malusDanger;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Our utility handler class.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class UtilityHandler extends AiUtilityHandler<AlcarKayaYildirim>
{	
	/**
	 * The hero count at the initialisation.
	 */
	//private final int HERO_COUNT = 1;
	/**
	 * If this number of enemies are accessible, tile may get QuartierEnnemi
	 * case.
	 */
	private final int NEAR_ENEMY_HERO_COUNT_LIMIT = 1;
	
	/**
	 * @param ai	
	 * @throws StopRequestException	
	 */
	protected UtilityHandler(AlcarKayaYildirim ai) throws StopRequestException
	{	
		
		super(ai);
		ai.checkInterruption();
		verbose = false;
	}

	/** Indique pour chaque case traitée si on veut y poser une bombe ou pas */
	protected HashMap<AiTile,Boolean> bombTiles = new HashMap<AiTile,Boolean>();
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException
	{	
		ai.checkInterruption();
		bombTiles.clear();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	
		ai.checkInterruption();
		
		Set<AiTile> result = new TreeSet<AiTile>();
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		List<AiTile> listTiles = zone.getTiles();
		AiTile tile = null;

		if(this.ai.getModeHandler().getMode() == AiMode.ATTACKING)
		{
			
			AiHero ourhero = this.ai.getZone().getOwnHero();
			AiTile ourtile = ourhero.getTile();
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

		}else
		{
			for(int i=0;i<listTiles.size();i++){
				ai.checkInterruption();
				tile = listTiles.get(i);
				if(tile.isCrossableBy(ownHero, false, false, false, false, true, true)){
					result.add(tile);
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
	{	
		ai.checkInterruption();
		
		new CollectePertinence(ai);
		new Duree(ai);
		new Securite(ai);
		new NonConcurrence(ai);
		new NbrAdjacentMurs(ai);
		new CaseVidePertinent(ai);
	    new AttaquePertinence(ai);	
	    new AttaqueItemVisible(ai);
	    new malusDanger(ai);
	    new AvancePertinence(ai);
	    new AttaqueBestPertinence(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms du premier cas*/
	private final String ItemVisibleBONUS = "ItemVisibleBONUS";
	/** noms du premier cas*/
	private final String ItemVisibleMALUS = "ItemVisibleMALUS";
	/** noms du deuxième cas*/
	private final String VoisinageMurDestructible = "MurDestructible";
	/** noms du troisieme cas*/
	private final String CaseVide = "CaseVide";
	/** noms du quatriemment cas*/
	private final String PorteeEnnemi = "PorteeEnnemi";
	/** noms du cinqiemment cas*/
	private final String AdversaireInaccessible = "AdversaireInaccessible";
	
	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
		
//******************* Mode Collecte ****************************************	
		// on définit un premier cas 'Item visible BONUS' utilisant les quatre critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CollectePertinence.NAME));
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(NonConcurrence.NAME));		
		AiUtilityCase caseItemVisibleBonus = new AiUtilityCase(ai,ItemVisibleBONUS,criteria);
		
		// on définit un deuxième cas 'Item visible MALUS' utilisant une critère
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(malusDanger.NAME));		
		AiUtilityCase caseItemVisibleMalus = new AiUtilityCase(ai,ItemVisibleMALUS,criteria);		
			
		// on définit un troisième cas 'Voisinage d'un mur destructible' utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(NbrAdjacentMurs.NAME));
		AiUtilityCase caseVoisinageMurDestructible = new AiUtilityCase(ai,VoisinageMurDestructible,criteria);

		// on définit un quatrième cas 'case vide' utilisant les deux critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CaseVidePertinent.NAME));
		criteria.add(criterionMap.get(Duree.NAME));
		AiUtilityCase caseVide = new AiUtilityCase(ai,CaseVide,criteria);
		
//******************* Mode attaque ****************************************
		
		// on définit un cinqième cas 'Portee de l'ennemi' utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(AttaquePertinence.NAME));
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(AttaqueBestPertinence.NAME));
		AiUtilityCase casePorteeEnnemi = new AiUtilityCase(ai, PorteeEnnemi, criteria);
		
		// on définit un sixième cas utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(AvancePertinence.NAME));
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(AttaqueItemVisible.NAME));
		AiUtilityCase caseAdversaireInaccessible = new AiUtilityCase(ai, AdversaireInaccessible, criteria);

		caseMap.put( ItemVisibleBONUS, caseItemVisibleBonus);
		caseMap.put( ItemVisibleMALUS, caseItemVisibleMalus);
		caseMap.put( VoisinageMurDestructible, caseVoisinageMurDestructible );
		caseMap.put( CaseVide, caseVide );
		caseMap.put( PorteeEnnemi, casePorteeEnnemi );
		caseMap.put( AdversaireInaccessible, caseAdversaireInaccessible );
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		
		AiUtilityCase result = null;
		AiZone zone = ai.getZone();
//		AiHero ownHero = zone.getOwnHero();
//		AiTile ownTile = ownHero.getTile();
//		int count = HERO_COUNT;
		int heroCount = 0;
		Set<AiTile> destBlockTileL = selectTiles();
		destBlockTileL.clear();
		boolean destKont = false;
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

//		for ( AiBlock currentBlock : tile.getNeighbor( Direction.UP ).getBlocks() )
//		{
//			ai.checkInterruption();
//			if ( currentBlock.isDestructible() ) count++;
//		}
//		for ( AiBlock currentBlock : tile.getNeighbor( Direction.DOWN ).getBlocks() )
//		{
//			ai.checkInterruption();
//			if ( currentBlock.isDestructible() ) count++;
//		}
//		for ( AiBlock currentBlock : tile.getNeighbor( Direction.LEFT ).getBlocks() )
//		{
//			ai.checkInterruption();
//			if ( currentBlock.isDestructible() ) count++;
//		}
//		for ( AiBlock currentBlock : tile.getNeighbor( Direction.RIGHT ).getBlocks() )
//		{
//			ai.checkInterruption();
//			if ( currentBlock.isDestructible() ) count++;
//		}


		if(ai.modeHandler.getMode()==AiMode.COLLECTING)
		{
			if (!tile.getItems().isEmpty()){	
				for(AiItem item : tile.getItems()){
					ai.checkInterruption();
					if(item.getType().equals(AiItemType.EXTRA_BOMB) ||
						item.getType().equals(AiItemType.EXTRA_FLAME) ||
						item.getType().equals(AiItemType.EXTRA_SPEED) ||
						item.getType().equals(AiItemType.GOLDEN_BOMB) ||
						item.getType().equals(AiItemType.GOLDEN_FLAME) ||
						item.getType().equals(AiItemType.GOLDEN_SPEED) ||
						item.getType().equals(AiItemType.RANDOM_EXTRA)){
					   result = caseMap.get(ItemVisibleBONUS);
					}
					else if(item.getType().equals(AiItemType.NO_BOMB) ||
							item.getType().equals(AiItemType.NO_FLAME) ||
							item.getType().equals(AiItemType.NO_SPEED) ||
							item.getType().equals(AiItemType.ANTI_BOMB) ||
							item.getType().equals(AiItemType.ANTI_FLAME) ||
							item.getType().equals(AiItemType.ANTI_SPEED) ||
							item.getType().equals(AiItemType.RANDOM_NONE)){
						result = caseMap.get(ItemVisibleMALUS);
					}
				}
				bombTiles.put(tile,false);
			}
			else if(destKont){              // count > HERO_COUNT){	
				result = caseMap.get(VoisinageMurDestructible);
				bombTiles.put(tile,true);
			}
			else{
				result = caseMap.get(CaseVide);
			}	
		}
		//mode attaque
		else if(ai.modeHandler.getMode()==AiMode.ATTACKING)
		{
			if(!bombTiles.containsKey(tile))
				bombTiles.put(tile,true);
			//boolean voisinageDopponent = false;	
			for ( AiTile tt : this.ai.getAccessibleTiles() )
			{
				ai.checkInterruption();
				if ( !tt.getHeroes().isEmpty() ) heroCount++;
			}
		//	List<AiTile> tilesN = this.ai.getTilesForRadius(ownTile, ownHero);
		//	List<AiTile> tilesN = tile.getNeighbors();
//			for(int i=0;i<tilesN.size();i++){
//				ai.checkInterruption();
//				if((tilesN.get(i).getHeroes().size()==1 && !tilesN.get(i).getHeroes().contains(ownHero)) || tilesN.get(i).getHeroes().size()>1){
//					voisinageDopponent = true;
//				}
//			}
			
			if ( heroCount > NEAR_ENEMY_HERO_COUNT_LIMIT ){ //if(voisinageDopponent){
				result = caseMap.get(PorteeEnnemi);
				
			}
			else{
				result = caseMap.get(AdversaireInaccessible);
			}
		
		}

//		for (int i = 0; i < 100; i++) {
//			System.out.println(this.ai.modeHandler.getMode());
//		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// REFERENCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initReferenceUtilities() throws StopRequestException
	{	ai.checkInterruption();
		
		// on affecte les valeurs d'utilité
		//int utility;
		AiUtilityCombination combi;
		AiMode mode;
		
		// on commence avec le mode collecte
		{	mode = AiMode.COLLECTING;
			//Les combinaisons pour le cas 'Item Visible Bonus'
			//A1
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 34);
			}//A2
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 33);
			}//A3
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 32);
			}//A4
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 30);
			}//A5
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 27);
			}//A6
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 26);
			}//A7
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 23);
			}//A8
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 22);
			}//A9
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 21);
			}//A10
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 19);
			}//A11
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 18);
			}//A12
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 16);
			}//A13
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 13);
			}//A14
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 12);
			}//A15
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 9);
			}//A16
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 8);
			}
			//Les combinaisons pour le cas 'Item Visible Malus'
			//B1
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleMALUS));
				combi.setCriterionValue((malusDanger)criterionMap.get(malusDanger.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 0);
			}//B2
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleMALUS));
				combi.setCriterionValue((malusDanger)criterionMap.get(malusDanger.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 5);
			}			
			//Les combinaisons pour le cas 'Voisinage d'un mur destructible'
			//C1
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, 31);
			}//C2
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, 29);
			}//C3
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, 28);
			}//C4
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, 25);
			}//C5
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, 24);
			}//C6
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, 15);
			}//C7
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, 17);
			}//C8
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, 14);
			}//C9
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, 11);
			}//C10
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, 10);
			}//C11
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, 7);
			}//C12
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, 6);
			}			
			//Les combinaisons pour le cas 'case null/vide' dans le mode collecte
			//D1
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((CaseVidePertinent) criterionMap.get(CaseVidePertinent.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 4);
			}//D2
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((CaseVidePertinent) criterionMap.get(CaseVidePertinent.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 3);
			}//D3
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((CaseVidePertinent) criterionMap.get(CaseVidePertinent.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 2);
			}//D4
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((CaseVidePertinent) criterionMap.get(CaseVidePertinent.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 1);
			}			
		}
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			//utility = 1;
			//Les combinaisons pour le cas 'Portee de l'ennemi'
					// E1
					{
						combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
						combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
						combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.TRUE);
						combi.setCriterionValue((AttaqueBestPertinence) criterionMap.get(AttaqueBestPertinence.NAME),Boolean.TRUE);
						defineUtilityValue(mode, combi, 16);
					}// E2
					{
						combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
						combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.FALSE);
						combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.TRUE);
						combi.setCriterionValue((AttaqueBestPertinence) criterionMap.get(AttaqueBestPertinence.NAME),Boolean.TRUE);
						defineUtilityValue(mode, combi, 15);
					}// E3
					{
						combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
						combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
						combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.TRUE);
						combi.setCriterionValue((AttaqueBestPertinence) criterionMap.get(AttaqueBestPertinence.NAME),Boolean.FALSE);
						defineUtilityValue(mode, combi, 14);
					}// E4
					{
						combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
						combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
						combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.FALSE);
						combi.setCriterionValue((AttaqueBestPertinence) criterionMap.get(AttaqueBestPertinence.NAME),Boolean.TRUE);
						defineUtilityValue(mode, combi, 13);
					}// E5
					{
						combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
						combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.FALSE);
						combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.FALSE);
						combi.setCriterionValue((AttaqueBestPertinence) criterionMap.get(AttaqueBestPertinence.NAME),Boolean.TRUE);
						defineUtilityValue(mode, combi, 12);
					}// E6
					{
						combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
						combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
						combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.FALSE);
						combi.setCriterionValue((AttaqueBestPertinence) criterionMap.get(AttaqueBestPertinence.NAME),Boolean.FALSE);
						defineUtilityValue(mode, combi, 11);
					}// E7
					{
						combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
						combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.FALSE);
						combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.TRUE);
						combi.setCriterionValue((AttaqueBestPertinence) criterionMap.get(AttaqueBestPertinence.NAME),Boolean.FALSE);
						defineUtilityValue(mode, combi, 10);
					}// E8
					{
						combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
						combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.FALSE);
						combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.FALSE);
						combi.setCriterionValue((AttaqueBestPertinence) criterionMap.get(AttaqueBestPertinence.NAME),Boolean.FALSE);
						defineUtilityValue(mode, combi, 9);
					}
			//Les combinaisons pour le cas 'Adversaire inaccessible'
			//F1
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((AvancePertinence) criterionMap.get(AvancePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((AttaqueItemVisible) criterionMap.get(AttaqueItemVisible.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 8);
			}//F2
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((AvancePertinence) criterionMap.get(AvancePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((AttaqueItemVisible) criterionMap.get(AttaqueItemVisible.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 7);
			}//F3
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((AvancePertinence) criterionMap.get(AvancePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((AttaqueItemVisible) criterionMap.get(AttaqueItemVisible.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 6);
			}//F4
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((AvancePertinence) criterionMap.get(AvancePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((AttaqueItemVisible) criterionMap.get(AttaqueItemVisible.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 5);
			}//F5
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((AvancePertinence) criterionMap.get(AvancePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((AttaqueItemVisible) criterionMap.get(AttaqueItemVisible.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 4);
			}//F6
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((AvancePertinence) criterionMap.get(AvancePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((AttaqueItemVisible) criterionMap.get(AttaqueItemVisible.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 3);
			}//F7
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((AvancePertinence) criterionMap.get(AvancePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((AttaqueItemVisible) criterionMap.get(AttaqueItemVisible.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 2);
			}//F8
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((AvancePertinence) criterionMap.get(AvancePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((AttaqueItemVisible) criterionMap.get(AttaqueItemVisible.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 1);
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
