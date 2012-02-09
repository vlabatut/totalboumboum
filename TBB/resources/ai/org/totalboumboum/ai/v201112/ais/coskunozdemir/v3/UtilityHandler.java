package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion.AttaPertinence;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion.AvanceItem;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion.AvancePertinence;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion.BestPertinence;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion.ColPertinence;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion.Duree;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion.NombreDeMurs;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion.NonConcurrence;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion.Securite;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Our utility handler class.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<CoskunOzdemir>
{
	/**
	 * The hero count at the initialisation.
	 */
	private final int	HERO_COUNT					= 0;

	/**
	 * If this number of enemies are accessible, tile may get QuartierEnnemi
	 * case.
	 */
	private final int	NEAR_ENEMY_HERO_COUNT_LIMIT	= 1;

	/**
	 * Constructs a handler for the agent passed as a parameter.
	 * 
	 * @param ai
	 *            The agent that the class will handle.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected UtilityHandler( CoskunOzdemir ai ) throws StopRequestException
	{
		super( ai );
		ai.checkInterruption();
		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	private final String	ItemVisible			= "ItemVisible";
	private final String	VoisinageMurDest	= "VoisinageMurDest";
	private final String	QuartierEnnemi		= "QuartierEnnemi";
	private final String	cNull				= "cNull";
	private final String	AvanceEnnemi		= "AvanceEnnemi";

	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();

		result.addAll( this.ai.getTo().getAccessibleTiles() );

		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException
	{
		ai.checkInterruption();

		ColPertinence colPertinence = new ColPertinence( ai );
		Duree duree = new Duree( ai );
		Securite securite = new Securite( ai );
		NonConcurrence nonConcurrence = new NonConcurrence( ai );
		NombreDeMurs nombreDeMurs = new NombreDeMurs( ai );
		AttaPertinence attaPertinence = new AttaPertinence( ai );
		AvancePertinence avancePertinence = new AvancePertinence( ai );
		BestPertinence bestPertinence = new BestPertinence( ai );
		AvanceItem avanceItem = new AvanceItem( ai );

		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add( colPertinence );
		criteria.add( duree );
		criteria.add( securite );
		criteria.add( nonConcurrence );
		AiUtilityCase CasItemVisible = new AiUtilityCase( ItemVisible, criteria );

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add( duree );
		criteria.add( securite );
		criteria.add( nombreDeMurs );
		AiUtilityCase CasVoisinageMurDest = new AiUtilityCase( VoisinageMurDest, criteria );

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add( bestPertinence );
		criteria.add( attaPertinence );
		criteria.add( securite );
		AiUtilityCase CasQuartierEnnemi = new AiUtilityCase( QuartierEnnemi, criteria );

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add( nombreDeMurs );
		AiUtilityCase CasNull = new AiUtilityCase( cNull, criteria );

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add( avanceItem );
		criteria.add( avancePertinence );
		criteria.add( securite );
		AiUtilityCase CasAvanceEnnemi = new AiUtilityCase( AvanceEnnemi, criteria );

		cases.put( ItemVisible, CasItemVisible );
		cases.put( VoisinageMurDest, CasVoisinageMurDest );
		cases.put( QuartierEnnemi, CasQuartierEnnemi );
		cases.put( cNull, CasNull );
		cases.put( AvanceEnnemi, CasAvanceEnnemi );

		// //////// Collect List //////////////////////////////

		AiUtilityCombination combiC;

		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, false );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nonConcurrence, false );
			referenceUtilities.put( combiC, 2 );

		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, true );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nonConcurrence, false );
			referenceUtilities.put( combiC, 3 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, false );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nonConcurrence, false );
			referenceUtilities.put( combiC, 6 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, false );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nonConcurrence, true );
			referenceUtilities.put( combiC, 7 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, true );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nonConcurrence, true );
			referenceUtilities.put( combiC, 10 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, false );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nonConcurrence, true );
			referenceUtilities.put( combiC, 12 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, true );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nonConcurrence, false );
			referenceUtilities.put( combiC, 13 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, true );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nonConcurrence, true );
			referenceUtilities.put( combiC, 15 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, false );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nonConcurrence, false );
			referenceUtilities.put( combiC, 16 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, false );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nonConcurrence, true );
			referenceUtilities.put( combiC, 17 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, false );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nonConcurrence, false );
			referenceUtilities.put( combiC, 20 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, true );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nonConcurrence, false );
			referenceUtilities.put( combiC, 21 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, false );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nonConcurrence, true );
			referenceUtilities.put( combiC, 24 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, true );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nonConcurrence, false );
			referenceUtilities.put( combiC, 26 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, true );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nonConcurrence, true );
			referenceUtilities.put( combiC, 27 );
		}
		{
			combiC = new AiUtilityCombination( CasItemVisible );
			combiC.setCriterionValue( colPertinence, true );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nonConcurrence, true );
			referenceUtilities.put( combiC, 28 );
		}
		// -----------------------------------------------------------------
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nombreDeMurs, 1 );
			referenceUtilities.put( combiC, 1 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nombreDeMurs, 1 );
			referenceUtilities.put( combiC, 4 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nombreDeMurs, 1 );
			referenceUtilities.put( combiC, 5 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nombreDeMurs, 1 );
			referenceUtilities.put( combiC, 8 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nombreDeMurs, 2 );
			referenceUtilities.put( combiC, 9 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nombreDeMurs, 3 );
			referenceUtilities.put( combiC, 11 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nombreDeMurs, 2 );
			referenceUtilities.put( combiC, 14 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, false );
			combiC.setCriterionValue( nombreDeMurs, 3 );
			referenceUtilities.put( combiC, 18 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nombreDeMurs, 2 );
			referenceUtilities.put( combiC, 19 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nombreDeMurs, 2 );
			referenceUtilities.put( combiC, 22 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, false );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nombreDeMurs, 3 );
			referenceUtilities.put( combiC, 23 );
		}
		{
			combiC = new AiUtilityCombination( CasVoisinageMurDest );
			combiC.setCriterionValue( duree, true );
			combiC.setCriterionValue( securite, true );
			combiC.setCriterionValue( nombreDeMurs, 3 );
			referenceUtilities.put( combiC, 25 );
		}

		{
			combiC = new AiUtilityCombination( CasNull );
			combiC.setCriterionValue( nombreDeMurs, 1 );
			referenceUtilities.put( combiC, 0 );
		}
		{
			combiC = new AiUtilityCombination( CasNull );
			combiC.setCriterionValue( nombreDeMurs, 2 );
			referenceUtilities.put( combiC, 0 );
		}
		{
			combiC = new AiUtilityCombination( CasNull );
			combiC.setCriterionValue( nombreDeMurs, 3 );
			referenceUtilities.put( combiC, 0 );
		}

		AiUtilityCombination combiA;

		{
			combiA = new AiUtilityCombination( CasQuartierEnnemi );
			combiA.setCriterionValue( bestPertinence, false );
			combiA.setCriterionValue( attaPertinence, false );
			combiA.setCriterionValue( securite, false );
			referenceUtilities.put( combiA, 9 );
		}
		{
			combiA = new AiUtilityCombination( CasQuartierEnnemi );
			combiA.setCriterionValue( bestPertinence, false );
			combiA.setCriterionValue( attaPertinence, true );
			combiA.setCriterionValue( securite, false );
			referenceUtilities.put( combiA, 10 );
		}
		{
			combiA = new AiUtilityCombination( CasQuartierEnnemi );
			combiA.setCriterionValue( bestPertinence, true );
			combiA.setCriterionValue( attaPertinence, false );
			combiA.setCriterionValue( securite, false );
			referenceUtilities.put( combiA, 11 );
		}
		{
			combiA = new AiUtilityCombination( CasQuartierEnnemi );
			combiA.setCriterionValue( bestPertinence, true );
			combiA.setCriterionValue( attaPertinence, true );
			combiA.setCriterionValue( securite, false );
			referenceUtilities.put( combiA, 12 );
		}
		{
			combiA = new AiUtilityCombination( CasQuartierEnnemi );
			combiA.setCriterionValue( bestPertinence, false );
			combiA.setCriterionValue( attaPertinence, false );
			combiA.setCriterionValue( securite, true );
			referenceUtilities.put( combiA, 13 );
		}
		{
			combiA = new AiUtilityCombination( CasQuartierEnnemi );
			combiA.setCriterionValue( bestPertinence, false );
			combiA.setCriterionValue( attaPertinence, true );
			combiA.setCriterionValue( securite, true );
			referenceUtilities.put( combiA, 15 );
		}
		{
			combiA = new AiUtilityCombination( CasQuartierEnnemi );
			combiA.setCriterionValue( bestPertinence, true );
			combiA.setCriterionValue( attaPertinence, false );
			combiA.setCriterionValue( securite, true );
			referenceUtilities.put( combiA, 14 );
		}
		{
			combiA = new AiUtilityCombination( CasQuartierEnnemi );
			combiA.setCriterionValue( bestPertinence, true );
			combiA.setCriterionValue( attaPertinence, true );
			combiA.setCriterionValue( securite, true );
			referenceUtilities.put( combiA, 16 );
		}
		// b8 , b6 switch
		// b9 , b6 switch
		{
			combiA = new AiUtilityCombination( CasAvanceEnnemi );
			combiA.setCriterionValue( avanceItem, false );
			combiA.setCriterionValue( avancePertinence, false );
			combiA.setCriterionValue( securite, false );
			referenceUtilities.put( combiA, 1 );
		}
		{
			combiA = new AiUtilityCombination( CasAvanceEnnemi );
			combiA.setCriterionValue( avanceItem, true );
			combiA.setCriterionValue( avancePertinence, false );
			combiA.setCriterionValue( securite, false );
			referenceUtilities.put( combiA, 2 );
		}
		{
			combiA = new AiUtilityCombination( CasAvanceEnnemi );
			combiA.setCriterionValue( avanceItem, false );
			combiA.setCriterionValue( avancePertinence, true );
			combiA.setCriterionValue( securite, false );
			referenceUtilities.put( combiA, 3 );
		}
		{
			combiA = new AiUtilityCombination( CasAvanceEnnemi );
			combiA.setCriterionValue( avanceItem, true );
			combiA.setCriterionValue( avancePertinence, true );
			combiA.setCriterionValue( securite, false );
			referenceUtilities.put( combiA, 4 );
		}
		{
			combiA = new AiUtilityCombination( CasAvanceEnnemi );
			combiA.setCriterionValue( avanceItem, false );
			combiA.setCriterionValue( avancePertinence, false );
			combiA.setCriterionValue( securite, true );
			referenceUtilities.put( combiA, 5 );
		}
		{
			combiA = new AiUtilityCombination( CasAvanceEnnemi );
			combiA.setCriterionValue( avanceItem, false );
			combiA.setCriterionValue( avancePertinence, true );
			combiA.setCriterionValue( securite, true );
			referenceUtilities.put( combiA, 6 );
		}
		{
			combiA = new AiUtilityCombination( CasAvanceEnnemi );
			combiA.setCriterionValue( avanceItem, true );
			combiA.setCriterionValue( avancePertinence, false );
			combiA.setCriterionValue( securite, true );
			referenceUtilities.put( combiA, 7 );
		}
		{
			combiA = new AiUtilityCombination( CasAvanceEnnemi );
			combiA.setCriterionValue( avanceItem, true );
			combiA.setCriterionValue( avancePertinence, true );
			combiA.setCriterionValue( securite, true );
			referenceUtilities.put( combiA, 8 );
		}

	}

	@Override
	protected AiUtilityCase identifyCase( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		AiUtilityCase result = null;
		int heroCount = 0;
		ColPertinence colPertinence = new ColPertinence( ai );
		Duree duree = new Duree( ai );
		Securite securite = new Securite( ai );
		NonConcurrence nonConcurrence = new NonConcurrence( ai );
		NombreDeMurs nombreDeMurs = new NombreDeMurs( ai );
		AttaPertinence attaPertinence = new AttaPertinence( ai );
		AvancePertinence avancePertinence = new AvancePertinence( ai );
		BestPertinence bestPertinence = new BestPertinence( ai );
		AvanceItem avanceItem = new AvanceItem( ai );

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

		// mode is collecting
		if ( this.ai.getModeHandler().getMode().equals( AiMode.COLLECTING ) )
		{
			if ( !tile.getItems().isEmpty() )
			{
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add( colPertinence );
				criteria.add( duree );
				criteria.add( securite );
				criteria.add( nonConcurrence );
				result = new AiUtilityCase( ItemVisible, criteria );
			}

			else if ( count != HERO_COUNT )
			{
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add( duree );
				criteria.add( securite );
				criteria.add( nombreDeMurs );
				result = new AiUtilityCase( VoisinageMurDest, criteria );
			}
			else
			{
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add( nombreDeMurs );
				result = new AiUtilityCase( cNull, criteria );

			}

		}
		else
		// mode is attacking
		{

			for ( AiTile tt : this.ai.getTo().getAccessibleTiles() )
			{
				ai.checkInterruption();
				if ( !tt.getHeroes().isEmpty() ) heroCount++;
			}

			if ( heroCount > NEAR_ENEMY_HERO_COUNT_LIMIT )
			{

				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add( bestPertinence );
				criteria.add( attaPertinence );
				criteria.add( securite );
				result = new AiUtilityCase( QuartierEnnemi, criteria );
			}
			else
			{
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add( securite );
				criteria.add( avancePertinence );
				criteria.add( avanceItem );
				result = new AiUtilityCase( AvanceEnnemi, criteria );

			}

		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{
		ai.checkInterruption();
	}
}
