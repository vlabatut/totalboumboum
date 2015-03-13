package org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.BesniliKangal;

/**
 * Cette classe représente le critère de menace envers l'adversaire. Il est
 * entier : la valeur comprise entre 1 et {@value #THREAT_LIMIT} représente la
 * distance entre la case et la cible.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
@SuppressWarnings("deprecation")
public class Threat extends AiUtilityCriterionInteger<BesniliKangal>
{
	/** Nom de ce critère */
	public static final String NAME = "THREAT";
	/** Valeur maximale pour ce critère */
	private final static int THREAT_LIMIT = 4;

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Threat( BesniliKangal ai ) throws StopRequestException
	{
		super( ai, NAME, 1, THREAT_LIMIT );
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		int distance = ai.tileOperation.getDistanceToTarget( tile );
		if ( distance < 1 )
			distance = THREAT_LIMIT;
		else if ( distance > THREAT_LIMIT )
			distance = THREAT_LIMIT;
		int result = THREAT_LIMIT - distance + 1;
		return result;
	}
}
