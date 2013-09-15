package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.criterion.CriterionConcurrence;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.criterion.CriterionDuree;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.criterion.CriterionItemPertinent;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.criterion.CriterionMenace;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.criterion.CriterionNombredeMur;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.criterion.CriterionPertinenceAdv;

/**
 * Utility Handler class de l'agent outremer
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<CiplakErakyol>
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
	protected UtilityHandler(CiplakErakyol ai) throws StopRequestException
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
		if ( ai.modeHandler.getMode() == AiMode.COLLECTING )
		{
			Set<AiTile> selectedTiles = getAccessibleSafeTiles();
			return selectedTiles;
		}
		else/* if ( ai.modeHandler.getMode() == AiMode.ATTACKING ) */
		{
			Set<AiTile> selectedTiles = new HashSet<AiTile>();
			for ( AiHero ennemy : ai.getZone().getRemainingOpponents() )
			{
				ai.checkInterruption();
				selectedTiles.add(ennemy.getTile());
			}
			return selectedTiles;
		}
	}

	/**
	 * Method calcul tiles sont dans un scope de bomb 
	 * @return safetile
	 * @throws StopRequestException 
	 * 		information manquante !?	
	 */
	private Set<AiTile> getAccessibleSafeTiles() throws StopRequestException
	{
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		Set<AiTile > safeTiles = ai.getAccessibleTiles(ai.getZone().getOwnHero().getTile());
		for ( AiBomb bomb : zone.getBombs() )
		{
			ai.checkInterruption();
			safeTiles.removeAll( bomb.getBlast() );
		}
		for ( AiFire fire : zone.getFires() )
		{
			ai.checkInterruption();
			safeTiles.remove( fire.getTile() );
		}
		return safeTiles;
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();
		// on définit les critères, qui sont automatiquement insérés dans criterionMap
		new CriterionNombredeMur(ai);
		new CriterionPertinenceAdv(ai);
		new CriterionDuree(ai);
		new CriterionMenace(ai);
		new CriterionItemPertinent(ai);
		new CriterionConcurrence(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * ItemVisible , utilisé dans {@link #initCases}
	 * */
	private final String itemVisible = "ItemVisible";
	/**
	 * VoisinageMurDest, utilisé dans {@link #initCases}
	 * */
	private final String voisinageMurDest = "VoisinageMurDest";
	/**
	 * VoisinageAdversaire, utilisé dans {@link #initCases}
	 * */
	private final String voisinageAdversaire = "VoisinageAdversaire";
	/**
	 * AdverseireLoin, utilisé dans {@link #initCases}
	 * */
	private final String adversaireLoin = "AdverseireLoin";
	

	@Override
	protected void initCases() throws StopRequestException {
		ai.checkInterruption();
		Set<AiUtilityCriterion<?, ?>> criteria;

		// on définit le cas "item visible"
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionMenace.NAME));
		criteria.add(criterionMap.get(CriterionItemPertinent.NAME));
		criteria.add(criterionMap.get(CriterionDuree.NAME));
		criteria.add(criterionMap.get(CriterionConcurrence.NAME));
		new AiUtilityCase(ai,itemVisible,criteria);
		
		// on définit le cas "Voisinage de murs dest."
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionMenace.NAME));
		criteria.add(criterionMap.get(CriterionDuree.NAME));
		criteria.add(criterionMap.get(CriterionNombredeMur.NAME));
		new AiUtilityCase(ai,voisinageMurDest,criteria);

		// on définit le cas "Voisinage de l'adversaire"
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionMenace.NAME));
		criteria.add(criterionMap.get(CriterionPertinenceAdv.NAME));
		new AiUtilityCase(ai,voisinageAdversaire,criteria);
		
		// on définit le cas "l'adversaire loin"
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionMenace.NAME));
		criteria.add(criterionMap.get(CriterionDuree.NAME));
		criteria.add(criterionMap.get(CriterionPertinenceAdv.NAME));
		new AiUtilityCase(ai,adversaireLoin,criteria);
		

	}
	
	

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		AiUtilityCase result = null;
//		Set<AiUtilityCriterion<?, ?>> criteria;

		// mode is collecting
		if ( ai.getModeHandler().getMode().equals( AiMode.COLLECTING ) )
		{
			if ( !tile.getItems().isEmpty() )
			{
//				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
//				criteria.add(criterionMap.get(CriterionMenace.NAME));
//				criteria.add(criterionMap.get(CriterionItemPertinent.NAME));
//				criteria.add(criterionMap.get(CriterionDuree.NAME));
//				criteria.add(criterionMap.get(CriterionConcurrence.NAME));
//				result = new AiUtilityCase(ai,ItemVisible,criteria);
				result = caseMap.get(itemVisible);
			}
			else
			{
//				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
//				criteria.add(criterionMap.get(CriterionMenace.NAME));
//				criteria.add(criterionMap.get(CriterionDuree.NAME));
//				criteria.add(criterionMap.get(CriterionNombredeMur.NAME));
//				result = new AiUtilityCase(ai,VoisinageMurDest,criteria);
				result = caseMap.get(voisinageMurDest);
			}
		}
		// mode is attacking
		else
		{
			boolean isAdversaireLoin = true;
			
			for( AiTile neighbor : ai.getZone().getOwnHero().getTile().getNeighbors() )
			{
				ai.checkInterruption();
				if( !neighbor.getHeroes().isEmpty() )
					isAdversaireLoin = false;
			}

			if( !isAdversaireLoin )
			{
//				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
//				criteria.add(criterionMap.get(CriterionMenace.NAME));
//				criteria.add(criterionMap.get(CriterionPertinenceAdv.NAME));
//				result = new AiUtilityCase(ai,VoisinageAdversaire,criteria);
				result = caseMap.get(voisinageAdversaire);
			}
			else
			{
//				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
//				criteria.add(criterionMap.get(CriterionMenace.NAME));
//				criteria.add(criterionMap.get(CriterionDuree.NAME));
//				criteria.add(criterionMap.get(CriterionPertinenceAdv.NAME));
//				result = new AiUtilityCase(ai,AdverseireLoin,criteria);
				result = caseMap.get(adversaireLoin);
			}
		}
		return result;
	}
	/////////////////////////////////////////////////////////////////
	// REFERENCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initReferenceUtilities() throws StopRequestException
	{	
		ai.checkInterruption();
		AiUtilityCombination combiA,combiB;
		AiMode mode;

		// on commence avec le mode collecte
		{	
			mode = AiMode.COLLECTING;

			// item visible
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 24);

			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 23);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 22);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 21);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 20);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 19);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 18);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 17);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 16);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 15);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 14);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 13);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 12);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 11);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 10);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 9);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 8);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 7);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 6);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 5);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 4);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 3);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 2);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(itemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 1);
			}
	
			// murdestructible
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiB, 28);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiB, 27);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiB, 26);
			}
			
			
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiB, 25);
			}
			
			
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiB, 24);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiB, 23);
			}
		
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiB, 22);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiB, 21);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiB, 20);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiB, 19);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiB, 18);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiB, 17);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiB, 16);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiB, 15);
			}
			
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),0);
				defineUtilityValue(mode, combiB, 14);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),0);
				defineUtilityValue(mode, combiB, 13);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),0);
				defineUtilityValue(mode, combiB, 12);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiB, 11);
			}
		
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiB, 10);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiB, 9);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiB, 8);
			}

				
					//null
			
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),0);
				defineUtilityValue(mode, combiB, 0);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),0);
				defineUtilityValue(mode, combiB, 0);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),0);
				defineUtilityValue(mode, combiB, 0);
			}
			
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),4);
				defineUtilityValue(mode, combiB, 0);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),4);
				defineUtilityValue(mode, combiB, 0);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),4);
				defineUtilityValue(mode, combiB, 0);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),4);
				defineUtilityValue(mode, combiB, 0);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),4);
				defineUtilityValue(mode, combiB, 0);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(voisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),4);
				defineUtilityValue(mode, combiB, 0);
			}
			
				
		}
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			
			// voisinage
			{	
				combiA = new AiUtilityCombination(caseMap.get(voisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 3);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(voisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 2);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(voisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, 1);
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(voisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, 0);
			}
			
			//AdversaireLoin
			
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiB, 12);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiB, 11);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiB, 10);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiB, 9);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiB, 8);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiB, 7);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiB, 6);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiB, 5);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiB, 4);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiB, 3);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiB, 2);
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(adversaireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiB, 1);
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
		super.updateOutput();
	
	}
}
