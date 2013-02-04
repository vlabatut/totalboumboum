package org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.BesniliKangal;

/**
 * Cette critere a été utilisé pour calculer le nombre des murs autour d'une
 * case.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
@SuppressWarnings("deprecation")
public class Bloque extends AiUtilityCriterionInteger<BesniliKangal>
{
	/** Nom de ce critère */
	public static final String NAME = "Bloque";
	/** Valeur maximale pour ce critère */
	public static final int BLOCKING_LIMIT = 4;

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Bloque( BesniliKangal ai ) throws StopRequestException
	{
		super( ai, NAME, 0, 4 );
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		int result = 0;
		if ( !tile.getHeroes().isEmpty() )
		{
			for ( AiTile neighborsTile : tile.getNeighbors() )
			{
				ai.checkInterruption();
				if ( !neighborsTile.isCrossableBy( tile.getHeroes().get( 0 ) ) )
					result++;
			}
		}
		if ( result > BLOCKING_LIMIT )
			return BLOCKING_LIMIT;
		return result;
	}
}
