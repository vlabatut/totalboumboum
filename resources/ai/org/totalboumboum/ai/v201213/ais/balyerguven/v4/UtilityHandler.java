package org.totalboumboum.ai.v201213.ais.balyerguven.v4;


import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion.Adversaire;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion.Duree;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion.MalusMenace;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion.Menace;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion.MurVoiAdv;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion.NbrMurDest;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion.Pertinence;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion.PertinentCaseVide;

/**
 * our utility handler class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<BalyerGuven>
{	
	/** verbose
	 * @param ai
	 * 		?	
	 * @throws StopRequestException
	 * 		?	
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
	 * represents game zone
	 */
	AiZone gameZone = ai.getZone();
	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		Set<AiTile> selectedTiles = new HashSet<AiTile>();
		
		if(ai.getModeHandler().getMode() == AiMode.COLLECTING)
		{
			selectedTiles.addAll(ai.getReachableTiles(ai.getZone().getOwnHero().getTile()));
		}
		else if(ai.getModeHandler().getMode() == AiMode.ATTACKING)
		{
			for(AiHero hero : zone.getRemainingOpponents())
			{
				ai.checkInterruption();
				selectedTiles.add(hero.getTile());
				for(AiTile neighbor : hero.getTile().getNeighbors())
				{
					ai.checkInterruption();
					if(neighbor.isCrossableBy(ai.getHero()))
					{
						selectedTiles.add(neighbor);
					}
				}
			}			
		}
		return selectedTiles;
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
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		AiUtilityCase result = null;
		AiMode mode = this.ai.modeHandler.getMode();

		// Tile identification for collect mode

		if (mode == AiMode.COLLECTING) {

			// Item visible identification
			if (!tile.getItems().isEmpty()) {
				AiItem item = tile.getItems().get(0);
				
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
				//  Item malus identification
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
			// Destructible walls identification
			else
			{
				result = caseMap.get(murDest);
				return result;
			}
		}

		// Mode Attacking
		else {
			AiZone zone = ai.getZone();
			// Case Voisin Adversaire identification
			if (!zone.getRemainingOpponents().isEmpty()) {
				result = caseMap.get(casVoiAdv);
				return result;
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
					
					// item malus
					{
						combi = new AiUtilityCombination(caseMap.get(itemVisibleMalus));
						combi.setCriterionValue((MalusMenace)criterionMap.get(MalusMenace.NAME),Boolean.TRUE);
						defineUtilityValue(mode, combi, -1);
					}
					
				// case vide
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
				
				// item malus
				
				{
					combi = new AiUtilityCombination(caseMap.get(itemVisibleMalus));
					combi.setCriterionValue((MalusMenace)criterionMap.get(MalusMenace.NAME),Boolean.FALSE);
				}
				
				
				// mur destructible
			
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
			
			// item malus
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
			
			// item malus
			{
				combi = new AiUtilityCombination(caseMap.get(itemVisibleMalus));
				combi.setCriterionValue((MalusMenace)criterionMap.get(MalusMenace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			// mur destructible
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
		
		// cas voisin de l'adversaire
			
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
