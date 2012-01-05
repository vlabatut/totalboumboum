package org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.CoskunOzdemir;

/**
 * The criteria that will evaluate the tile for the time to reach it. The time
 * is directly proportional to its distance, so the criteria will process the
 * distance.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class Duree extends AiUtilityCriterionBoolean
{
	public static final String	NAME					= "Duree";

	/**
	 * Distance radius limit to check to determine the tile's state.
	 */
	private final int			DISTANCE_UPPER_LIMIT	= 6;

	public Duree( CoskunOzdemir ai ) throws StopRequestException
	{
		super( NAME );
		ai.checkInterruption();
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected CoskunOzdemir	ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		boolean result = false;
		if ( this.ai.getZone().getTileDistance( this.ai.getZone().getOwnHero().getTile(), tile ) <= DISTANCE_UPPER_LIMIT ) result = true;

		return result;
	}
}
