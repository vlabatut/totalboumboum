package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1;

import java.util.LinkedList;
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
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.criterion.AttaquePertinence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.criterion.Concurrence;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.criterion.Duree;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.criterion.Menace;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.criterion.NbrAdjacentMurs;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.criterion.Pertinence;
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
	{	ai.checkInterruption();
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
		
	    new AttaquePertinence(ai);
		new Concurrence(ai);
		new Duree(ai);
		new Menace(ai);
		new NbrAdjacentMurs(ai);
		new Pertinence(ai);
		//new Securite(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms du premier cas*/
	private final String Item_Visible = "CAS1";
	/** noms du deuxième cas*/
	private final String Mur_Destructible_Menace = "CAS2";
	/** noms du troisieme cas*/
	private final String Mur_Destructible = "CAS3";
	/** noms du quatriemment cas*/
	private final String Portee_Ennemi = "CAS4";
	/** noms du cinqiemment cas*/
	private final String Adversaire_Inaccessible = "CAS5";

	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
//******************* Mode Collecte ****************************************	
		// on définit un premier cas utilisant les quatre critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Pertinence.NAME));
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(Concurrence.NAME));
		criteria.add(criterionMap.get(Menace.NAME));
		AiUtilityCase caseItemVisible = new AiUtilityCase(ai,Item_Visible,criteria);
			
		// on définit un deuxième cas utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(Concurrence.NAME));
		criteria.add(criterionMap.get(Menace.NAME));
		AiUtilityCase caseMurDestructibleMenace = new AiUtilityCase(ai,Mur_Destructible_Menace,criteria);

		// on définit un troisième cas utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(Menace.NAME));
		criteria.add(criterionMap.get(NbrAdjacentMurs.NAME));
		AiUtilityCase caseMurDestructible = new AiUtilityCase(ai,Mur_Destructible,criteria);
		
//******************* Mode attaque ****************************************
		// on définit un quatrième cas utilisant les deux critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(AttaquePertinence.NAME));
		//criteria.add(criterionMap.get(Securite.NAME));
		AiUtilityCase casePorteeEnnemi = new AiUtilityCase(ai, Portee_Ennemi, criteria);
		
		// on définit un cinqième cas utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(NbrAdjacentMurs.NAME));
		//criteria.add(criterionMap.get(Securite.NAME));
		AiUtilityCase caseAdversaireInaccessible = new AiUtilityCase(ai, Adversaire_Inaccessible, criteria);

		caseMap.put( Item_Visible, caseItemVisible);
		caseMap.put( Mur_Destructible_Menace, caseMurDestructibleMenace );
		caseMap.put( Mur_Destructible, caseMurDestructible );
		caseMap.put( Portee_Ennemi, casePorteeEnnemi );
		caseMap.put( Adversaire_Inaccessible, caseAdversaireInaccessible );
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		
		AiUtilityCase result = null; 

		@SuppressWarnings("unused")
		boolean herocontrol = true;
		// We control if we have enemies in the list of tiles returned by
		// SelectTiles.

		if (!this.ai.getZone().getRemainingOpponents().isEmpty()) {
			for (AiHero aihero : this.ai.getZone().getRemainingOpponents()) {
				ai.checkInterruption();
				if (this.ai.utilityHandler.selectTiles().contains(aihero.getTile())) {
					herocontrol = false;
					break;
				}

			}
		}
		AiMode mode = this.ai.modeHandler.getMode();
		// Tile identification for collect mode

		if (mode.equals(AiMode.COLLECTING)) {
			// Visible item identification
			if (tile.getItems().contains(AiItemType.EXTRA_BOMB)
					|| tile.getItems().contains(AiItemType.EXTRA_FLAME)||tile.getItems().contains(AiItemType.EXTRA_SPEED)
					||tile.getItems().contains(AiItemType.GOLDEN_BOMB)) {
				Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
				criteria.add(criterionMap.get(Pertinence.NAME));
				criteria.add(criterionMap.get(Duree.NAME));
				criteria.add(criterionMap.get(Concurrence.NAME));
				criteria.add(criterionMap.get(Menace.NAME));
				result = new AiUtilityCase(ai,Item_Visible,criteria);
			}

			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				AiTile currentNeighbor = tile.getNeighbor(direction);

				boolean value = this.ai.getWallInDanger(currentNeighbor);
				
				// Destructible wall in danger identification
				if (value) {
					Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
					criteria = new TreeSet<AiUtilityCriterion<?,?>>();
					criteria.add(criterionMap.get(Duree.NAME));
					criteria.add(criterionMap.get(Concurrence.NAME));
					criteria.add(criterionMap.get(Menace.NAME));
					result = new AiUtilityCase(ai,Mur_Destructible_Menace,criteria);
				}

			}

			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				for (AiBlock currentBlock : tile.getNeighbor(direction)
						.getBlocks()) {
					ai.checkInterruption();
					// Destructible wall identification
					if (currentBlock.isDestructible()) {
						Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
						criteria = new TreeSet<AiUtilityCriterion<?,?>>();
						criteria.add(criterionMap.get(Duree.NAME));
						criteria.add(criterionMap.get(Menace.NAME));
						criteria.add(criterionMap.get(NbrAdjacentMurs.NAME));
						result = new AiUtilityCase(ai,Mur_Destructible,criteria);
					}
				}
			}
			
			// Tile not in range of an enemy identification
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if (!(this.ai.getAnEnemyInMyRange(tile, direction, 0))) {
					Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
					criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
					criteria.add(criterionMap.get(NbrAdjacentMurs.NAME));
					//criteria.add(criterionMap.get(Securite.NAME));
					result = new AiUtilityCase(ai, Adversaire_Inaccessible, criteria);
				}
			}

		}

		// Tile identification for attack mode
		if (mode == AiMode.ATTACKING) {
			// Tile in range of an enemy identification
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if (this.ai.getAnEnemyInMyRange(tile, direction, 0)) {
					Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
					criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
					criteria.add(criterionMap.get(AttaquePertinence.NAME));
					//criteria.add(criterionMap.get(Securite.NAME));
					result = new AiUtilityCase(ai, Portee_Ennemi, criteria);
				}
			}
			// Tile not in range of an enemy identification
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if (!(this.ai.getAnEnemyInMyRange(tile, direction, 0))) {
					Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
					criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
					criteria.add(criterionMap.get(NbrAdjacentMurs.NAME));
					//criteria.add(criterionMap.get(Securite.NAME));
					result = new AiUtilityCase(ai, Adversaire_Inaccessible, criteria);
				}
			}

			
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
			//C11
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C10
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C9
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C8
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C7
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C6
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//B7
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible_Menace));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//B6
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible_Menace));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//B5
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible_Menace));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//B4
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible_Menace));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A15
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A14
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A13
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A12
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A11
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A10
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A9
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A8
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A7
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A6
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A5
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C5
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C4
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C3
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C2
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//C1
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//B3
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible_Menace));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//B2
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible_Menace));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//B1
			{	combi = new AiUtilityCombination(caseMap.get(Mur_Destructible_Menace));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A4
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A3
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A2
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//A1
			{	combi = new AiUtilityCombination(caseMap.get(Item_Visible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			//E5
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//E4
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//E3
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//E2
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//E1
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//D3
				 
		}
		
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			utility = 1;
			//E5
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//E4
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//E3
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),1);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//E2
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),2);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//E1
			{	combi = new AiUtilityCombination(caseMap.get(Adversaire_Inaccessible));
				combi.setCriterionValue((NbrAdjacentMurs)criterionMap.get(NbrAdjacentMurs.NAME),3);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//D3
			{	combi = new AiUtilityCombination(caseMap.get(Portee_Ennemi));
				combi.setCriterionValue((AttaquePertinence)criterionMap.get(AttaquePertinence.NAME),Boolean.FALSE);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//D2
			{	combi = new AiUtilityCombination(caseMap.get(Portee_Ennemi));
				combi.setCriterionValue((AttaquePertinence)criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}//D1
			{	combi = new AiUtilityCombination(caseMap.get(Portee_Ennemi));
				combi.setCriterionValue((AttaquePertinence)criterionMap.get(AttaquePertinence.NAME),Boolean.TRUE);
				//combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
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
