package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.AlcarKayaYildirim;

/**
 * The criteria that will evaluate the tile for suitability to advance through a
 * non-accessible enemy.
 * 
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
@SuppressWarnings("deprecation")
public class AvancePertinence extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** */
	public static final String NAME = "AVANCE_PERTINENCE";

	/**
	 * 
	 * @param ai
	 * 		?	
	 * @throws StopRequestException
	 * 		?	
	 */
	public AvancePertinence( AlcarKayaYildirim ai ) throws StopRequestException
	{
		super(ai,NAME );
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		boolean result = false;

		try
		{
			if ( this.ai.getClosestAccDesWalltoEnemy().getNeighbors().contains( tile ) )
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
