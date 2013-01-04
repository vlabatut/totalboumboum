package org.totalboumboum.ai.v201213.ais.besnilikangal.v3;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion.Bloque;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion.Concurrence;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion.Duree;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion.NombreDesMurs;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion.Pertinence;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion.PlusFacile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion.PlusFaible;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion.Threat;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * 
 * @author Mustafa Besnili
 * @author Doruk Kangal
 */
public class UtilityHandler extends AiUtilityHandler<BesniliKangal>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler( BesniliKangal ai ) throws StopRequestException
	{
		super( ai );
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException
	{	// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	@SuppressWarnings( "serial" )
	protected Set<AiTile> selectTiles() throws StopRequestException
	{
		ai.checkInterruption();
		if ( ai.getModeHandler().getMode() == AiMode.COLLECTING )
		{
			return new HashSet<AiTile>() { {
				addAll( ai.tileOperation.getAccessibleSafeTiles() );
				addAll( ai.itemOperation.getGoodItemTiles() );
			} };
		}
		else
			return ai.tileOperation.getEnnemyTilesInBombRange( ai.ownHero.getBombRange() );
	}
	
	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException
	{
		ai.checkInterruption();
		// criterionMap
		new Duree( ai );
		new PlusFaible( ai );
		new PlusFacile( ai );
		new Bloque( ai );
		new Concurrence( ai );
		new Pertinence( ai );
		new NombreDesMurs( ai );
		new Threat( ai );
	}

	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**Le nom d'un cas qu'il correspond a un item visible en mode collecte */
	public final String ItemVisible = "ItemVisible";
	/**Le nom d'un cas qu'il correspond a une voisinage d'un mur destructible en mode attaque*/
	public final String VoisinageMurDest = "VoisinageMurDest";
	/**Le nom d'un cas qu'il correspond a une voisinage d'un ennemi en mode attaque */
	public final String VoisinageEnnemi = "VoisinageEnnemi";
	/** Le nom d'un cas qu'il correspond a il n'y a pas d'un ennemi pour un radius donné en mode attaque */
	public final String NonProcheEnnemi = "NonProcheEnnemi";
	/** Le nom de cas*/
	public String casName = null;

	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
		
		/////////////////////////////////////////////////////////////////
		// MODE COLLECTE	/////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////
		/******** ITEM VISIBLE **********/
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add( criterionMap.get( Pertinence.NAME ) );
		criteria.add( criterionMap.get( Concurrence.NAME ) );
		criteria.add( criterionMap.get( Duree.NAME ) );
		AiUtilityCase CaseItemVisible = new AiUtilityCase( ai, ItemVisible, criteria );

		/******** VOISINAGE D'UN MUR DESTRUCTIBLE ***********/
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add( criterionMap.get( NombreDesMurs.NAME ) );
		criteria.add( criterionMap.get( Duree.NAME ) );
		AiUtilityCase CaseVoisinageMurDest = new AiUtilityCase( ai, VoisinageMurDest, criteria );

		/////////////////////////////////////////////////////////////////
		// MODE ATTAQUE 	/////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////
		/******** VOISINAGE d'UN ENNEMI **********/
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add( criterionMap.get( Bloque.NAME ) );
		criteria.add( criterionMap.get( PlusFaible.NAME ) );
		criteria.add( criterionMap.get( Threat.NAME ) );
		AiUtilityCase CaseVoisinageEnnemi = new AiUtilityCase( ai, VoisinageEnnemi, criteria );

		/******** NON PROCHE d'UN ENNEMI **********/
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add( criterionMap.get( PlusFaible.NAME ) );
		criteria.add( criterionMap.get( PlusFacile.NAME ) );
		criteria.add( criterionMap.get( Duree.NAME ) );
		AiUtilityCase CaseNonProcheEnnemi = new AiUtilityCase( ai, NonProcheEnnemi, criteria );

		caseMap.put( ItemVisible, CaseItemVisible );
		caseMap.put( VoisinageMurDest, CaseVoisinageMurDest );
		caseMap.put( VoisinageEnnemi, CaseVoisinageEnnemi );
		caseMap.put( NonProcheEnnemi, CaseNonProcheEnnemi );
	}

	@Override
	protected AiUtilityCase identifyCase( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		AiUtilityCase result = null;
		AiMode mode = ai.modeHandler.getMode();
		if ( mode == AiMode.COLLECTING )
		{
			if ( !tile.getItems().isEmpty() )
			{
				Set<AiUtilityCriterion<?, ?>> criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
				criteria.add( criterionMap.get( Pertinence.NAME ) );
				criteria.add( criterionMap.get( Concurrence.NAME ) );
				criteria.add( criterionMap.get( Duree.NAME ) );
				result = new AiUtilityCase( ai, ItemVisible, criteria );
				casName = ItemVisible;
			}
			else
			{
				Set<AiUtilityCriterion<?, ?>> criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
				criteria.add( criterionMap.get( NombreDesMurs.NAME ) );
				criteria.add( criterionMap.get( Duree.NAME ) );
				result = new AiUtilityCase( ai, VoisinageMurDest, criteria );
				casName = VoisinageMurDest;
			}
		}
		else if ( mode == AiMode.ATTACKING )
		{
			if ( ai.heroOperation.isEnemyInRadius( tile ) )
			{
				Set<AiUtilityCriterion<?, ?>> criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
				criteria.add( criterionMap.get( Bloque.NAME ) );
				criteria.add( criterionMap.get( PlusFaible.NAME ) );
				criteria.add( criterionMap.get( Threat.NAME ) );
				result = new AiUtilityCase( ai, VoisinageEnnemi, criteria );
				casName = VoisinageEnnemi;
			}
			else
			{
				Set<AiUtilityCriterion<?, ?>> criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
				criteria.add( criterionMap.get( PlusFaible.NAME ) );
				criteria.add( criterionMap.get( PlusFacile.NAME ) );
				criteria.add( criterionMap.get( Duree.NAME ) );
				result = new AiUtilityCase( ai, NonProcheEnnemi, criteria );
				casName = NonProcheEnnemi;
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
		AiUtilityCombination combi;
		AiMode mode;
		{
			mode = AiMode.COLLECTING;
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 7 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 68 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 7 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 66 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 7 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 67 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 7 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 65 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 7 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 64 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 7 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 63 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 7 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 60 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 7 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 59 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 6 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 62 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 6 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 61 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 6 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 58 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 6 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 57 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 6 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 56 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 6 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 55 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 6 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 52 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 6 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 51 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 5 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 54 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 5 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 53 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 5 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 50 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 5 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 49 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 5 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 48 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 5 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 47 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 5 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 44 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 5 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 43 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 46 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 45 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 42 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 41 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 40 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 39 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 37 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 36 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 38 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 35 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 34 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 33 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 42 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 32 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 31 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 30 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 29 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 28 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 27 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 26 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 25 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 24 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 23 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 22 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 21 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 20 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 19 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 18 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 17 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 16 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 15 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( ItemVisible ) );
				combi.setCriterionValue( (Pertinence) criterionMap.get( Pertinence.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				combi.setCriterionValue( (Concurrence) criterionMap.get( Concurrence.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 14 );
			}
			// VoisinageMurDest
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				defineUtilityValue( mode, combi, 0 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				defineUtilityValue( mode, combi, 0 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				defineUtilityValue( mode, combi, 0 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 4 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				defineUtilityValue( mode, combi, 0 );
			}
			// VoisinageMurDest
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				defineUtilityValue( mode, combi, 13 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				defineUtilityValue( mode, combi, 12 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				defineUtilityValue( mode, combi, 11 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 3 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				defineUtilityValue( mode, combi, 10 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				defineUtilityValue( mode, combi, 9 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				defineUtilityValue( mode, combi, 8 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				defineUtilityValue( mode, combi, 7 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 2 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				defineUtilityValue( mode, combi, 6 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				defineUtilityValue( mode, combi, 5 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				defineUtilityValue( mode, combi, 4 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				defineUtilityValue( mode, combi, 3 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 1 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				defineUtilityValue( mode, combi, 2 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 0 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				defineUtilityValue( mode, combi, 1 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 0 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				defineUtilityValue( mode, combi, 1 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 0 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				defineUtilityValue( mode, combi, 1 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageMurDest ) );
				combi.setCriterionValue( (NombreDesMurs) criterionMap.get( NombreDesMurs.NAME ), 0 );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				defineUtilityValue( mode, combi, 1 );
			}
		}
		{
			// on traite maintenant le mode attaque
			mode = AiMode.ATTACKING;
			// pour simplifier, on ne met qu'un seul cas : le troisième
			// il n'a qu'un seul critère, défini sur un domaine de 5 valeurs
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 4 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 50 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 4 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 49 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 4 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 48 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 4 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 47 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 4 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 46 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 4 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 45 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 4 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 44 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 4 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 43 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 3 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 42 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 3 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 41 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 3 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 40 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 3 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 39 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 3 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 38 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 3 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 37 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 3 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 36 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 3 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 35 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 2 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 34 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 2 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 33 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 2 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 32 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 2 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 31 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 2 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 30 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 2 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 29 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 2 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 28 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 2 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 27 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 1 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 26 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 1 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 25 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 1 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 24 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 1 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 23 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 1 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 22 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 1 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 21 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 1 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 20 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 1 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 19 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 0 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 18 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 0 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 17 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 0 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 16 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 0 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				defineUtilityValue( mode, combi, 15 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 0 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 4 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 14 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 0 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 3 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 13 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 0 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 2 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 12 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( VoisinageEnnemi ) );
				combi.setCriterionValue( (Bloque) criterionMap.get( Bloque.NAME ), 0 );
				combi.setCriterionValue( (Threat) criterionMap.get( Threat.NAME ), 1 );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				defineUtilityValue( mode, combi, 11 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				defineUtilityValue( mode, combi, 7 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				defineUtilityValue( mode, combi, 12 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				defineUtilityValue( mode, combi, 16 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				defineUtilityValue( mode, combi, 6 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				defineUtilityValue( mode, combi, 11 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				defineUtilityValue( mode, combi, 15 );
			}

			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				defineUtilityValue( mode, combi, 3 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				defineUtilityValue( mode, combi, 5 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				defineUtilityValue( mode, combi, 10 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 3 );
				defineUtilityValue( mode, combi, 1 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 2 );
				defineUtilityValue( mode, combi, 2 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 1 );
				defineUtilityValue( mode, combi, 4 );
			}
			////////////////////////////////////////////////////////////////////////////////////
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				defineUtilityValue( mode, combi, 1 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				defineUtilityValue( mode, combi, 1 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.TRUE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				defineUtilityValue( mode, combi, 1 );
			}
			{
				combi = new AiUtilityCombination( caseMap.get( NonProcheEnnemi ) );
				combi.setCriterionValue( (PlusFaible) criterionMap.get( PlusFaible.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (PlusFacile) criterionMap.get( PlusFacile.NAME ), Boolean.FALSE );
				combi.setCriterionValue( (Duree) criterionMap.get( Duree.NAME ), 0 );
				defineUtilityValue( mode, combi, 1 );
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	}
}
