package org.totalboumboum.ai.v201213.ais.besnilikangal.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v1.BesniliKangal;

/**
 * Cette critere a été utilisé pour calculer le nombre des murs autour d'une case.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
public class Bloque extends AiUtilityCriterionInteger<BesniliKangal>
{
	/** Nom de ce critère */
	public static final String NAME = "Bloque";

	/**
	 * 
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Bloque( BesniliKangal ai ) throws StopRequestException
	{
		super( ai, NAME, 0, 4 );
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		int result = 0;
		for ( AiTile neighborsTile : tile.getNeighbors() )
		{
			ai.checkInterruption();
			if (!tile.getHeroes().isEmpty() && !neighborsTile.isCrossableBy( tile.getHeroes().get( 0 ) ) )
				result++;
		}
		return result;
	}
}
