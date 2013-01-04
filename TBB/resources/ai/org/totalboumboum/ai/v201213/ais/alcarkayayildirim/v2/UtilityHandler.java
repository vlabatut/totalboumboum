package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2;

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
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.criterion.AttaquePertinence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.criterion.CollectePertinence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.criterion.Duree;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.criterion.NbrAdjacentMurs;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.criterion.NonConcurrence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.criterion.Securite;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.criterion.caseVidePertinent;
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
	private final int HERO_COUNT = 0;
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

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException
	{	
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	
		ai.checkInterruption();
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
		new caseVidePertinent(ai);
	    new AttaquePertinence(ai);	
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms du premier cas*/
	private final String ItemVisible = "ItemVisible";
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
		AiUtilityCase caseItemVisible = new AiUtilityCase(ai,ItemVisible,criteria);
			
		// on définit un deuxième cas 'Voisinage d'un mur destructible' utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(NbrAdjacentMurs.NAME));
		AiUtilityCase caseVoisinageMurDestructible = new AiUtilityCase(ai,VoisinageMurDestructible,criteria);

		// on définit un troisième cas 'case vide' utilisant les deux critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(caseVidePertinent.NAME));
		criteria.add(criterionMap.get(Duree.NAME));
		AiUtilityCase caseVide = new AiUtilityCase(ai,CaseVide,criteria);
		
//******************* Mode attaque ****************************************
		
		// on définit un quatrième cas 'Portee de l'ennemi' utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(AttaquePertinence.NAME));
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(Duree.NAME));		
		AiUtilityCase casePorteeEnnemi = new AiUtilityCase(ai, PorteeEnnemi, criteria);
		
		// on définit un cinqième cas utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(NbrAdjacentMurs.NAME));
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(Duree.NAME));	
		AiUtilityCase caseAdversaireInaccessible = new AiUtilityCase(ai, AdversaireInaccessible, criteria);

		caseMap.put( ItemVisible, caseItemVisible);
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
		AiHero ownHero = zone.getOwnHero();		
		int count = HERO_COUNT;

		for ( AiBlock currentBlock : tile.getNeighbor( Direction.UP ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.DOWN ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.LEFT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.RIGHT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}


		if(ai.modeHandler.getMode()==AiMode.COLLECTING)
		{
			if (!tile.getItems().isEmpty()){	
				
				result = caseMap.get(ItemVisible);
			}
			else if(count != HERO_COUNT){	
				result = caseMap.get(VoisinageMurDestructible);
			}
			else{
				result = caseMap.get(CaseVide);
			}
				
		}
		else if(ai.modeHandler.getMode()==AiMode.ATTACKING)
		{
			boolean voisinageDopponent = false;
			List<AiTile> tilesN = tile.getNeighbors();
			for(int i=0;i<tilesN.size();i++){
				ai.checkInterruption();
				if((tilesN.get(i).getHeroes().size()==1 && !tilesN.get(i).getHeroes().contains(ownHero)) || tilesN.get(i).getHeroes().size()>1){
					voisinageDopponent = true;
				}
			}

			if(voisinageDopponent){
				result = caseMap.get(PorteeEnnemi);
			}
			else{
				result = caseMap.get(AdversaireInaccessible);
			}
		}
		else{
			result = caseMap.get(CaseVide);
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
		//int utility;
		AiUtilityCombination combi;
		AiMode mode;
		
		// on commence avec le mode collecte
		{	mode = AiMode.COLLECTING;
			//Les combinaisons pour le cas 'Item Visible Bonus'
			//A1
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 32);
			}//A2
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 31);
			}//A3
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 30);
			}//A4
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 28);
			}//A5
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 25);
			}//A6
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 24);
			}//A7
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 21);
			}//A8
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 20);
			}//A9
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 19);
			}//A10
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 17);
			}//A11
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 16);
			}//A12
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 14);
			}//A13
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, 11);
			}//A14
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 10);
			}//A15
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 7);
			}//A16
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue((CollectePertinence)criterionMap.get(CollectePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NonConcurrence)criterionMap.get(NonConcurrence.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, 6);
			}
			//Les combinaisons pour le cas 'Voisinage d'un mur destructible'
			//B1
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, 29);
			}//B2
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, 27);
			}//B3
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, 26);
			}//B4
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, 23);
			}//B5
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, 22);
			}//B6
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, 18);
			}//B7
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, 15);
			}//B8
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, 13);
			}//B9
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, 12);
			}//B10
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, 9);
			}//B11
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, 8);
			}//B12
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, 5);
			}//B13
			/*{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),0);
				defineUtilityValue(mode, combi, 0);
			}//B14
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),0);
				defineUtilityValue(mode, combi, 0);
			}//B15
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),0);
				defineUtilityValue(mode, combi, 0);
			}//B16
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),0);
				defineUtilityValue(mode, combi, 0);
			}*/
			
			//Les combinaisons pour le cas 'case null/vide' dans le mode collecte
			//C1
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((caseVidePertinent) criterionMap.get(caseVidePertinent.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 4);
			}//C2
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((caseVidePertinent) criterionMap.get(caseVidePertinent.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 3);
			}//C3
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((caseVidePertinent) criterionMap.get(caseVidePertinent.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 2);
			}//C4
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((caseVidePertinent) criterionMap.get(caseVidePertinent.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 1);
			}

			//maxReferenceUtilities.put(AiMode.COLLECTING, utility-1);			
		}
		
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			//utility = 1;
			//Les combinaisons pour le cas 'Portee de l'ennemi'
			// D1
			{
				combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
				combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 24);
			}// D2
			{
				combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
				combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 23);
			}// D3
			{
				combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
				combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 22);
			}// D4
			{
				combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
				combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 21);
			}// D5
			{
				combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
				combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 20);
			}// D6
			{
				combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
				combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 19);
			}// D7
			{
				combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
				combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 18);
			}// D8
			{
				combi = new AiUtilityCombination(caseMap.get(PorteeEnnemi));
				combi.setCriterionValue((AttaquePertinence) criterionMap.get(AttaquePertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite) criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 17);
			}
			//Les combinaisons pour le cas 'Adversaire inaccessible'
			//E1
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 16);
			}//E2
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 15);
			}//E3
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 14);
			}//E4
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 13);
			}//E5
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 12);
			}//E6
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 11);
			}//E7
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 10);
			}//E8
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 9);
			}//E9
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 8);
			}//E10
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 7);
			}//E11
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 6);
			}//E12
			{	combi = new AiUtilityCombination(caseMap.get(AdversaireInaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree) criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 5);
			}//E13
			/*{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),0);
				defineUtilityValue(mode, combi, 0);
			}//E14
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),0);
				defineUtilityValue(mode, combi, 0);
			}//E15
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),0);
				defineUtilityValue(mode, combi, 0);
			}//E16
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDestructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),0);
				defineUtilityValue(mode, combi, 0);
			}*/			
			//Les combinaisons pour le cas 'case null/vide' dans le mode attaque
			//F1
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((caseVidePertinent) criterionMap.get(caseVidePertinent.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 4);
			}//F2
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((caseVidePertinent) criterionMap.get(caseVidePertinent.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 3);
			}//F3
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((caseVidePertinent) criterionMap.get(caseVidePertinent.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 2);
			}//F4
			{
				combi = new AiUtilityCombination(caseMap.get(CaseVide));
				combi.setCriterionValue((caseVidePertinent) criterionMap.get(caseVidePertinent.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 1);
			}
			
			//maxReferenceUtilities.put(AiMode.ATTACKING, utility-1);		
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
