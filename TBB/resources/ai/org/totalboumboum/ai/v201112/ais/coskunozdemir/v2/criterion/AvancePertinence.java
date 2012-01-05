package org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.CoskunOzdemir;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.TileOperation;

/**
 * The criteria that will evaluate the tile for suitability to advance through a
 * non-accessible enemy.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class AvancePertinence extends AiUtilityCriterionBoolean
{
	public static final String	NAME	= "AvancePertinence";

	public AvancePertinence( CoskunOzdemir ai ) throws StopRequestException
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

		TileOperation to = new TileOperation( this.ai );
		try
		{
			if ( to.getClosestAccDesWalltoEnemy().getNeighbors().contains( tile ) )
			{
				result = true;
			}
		}
		catch ( NullPointerException e )
		{
			result = false;
		}
		return result;
	}
}
