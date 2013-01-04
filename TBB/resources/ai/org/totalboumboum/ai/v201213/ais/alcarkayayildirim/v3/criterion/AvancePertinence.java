package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.AlcarKayaYildirim;



/**
 * The criteria that will evaluate the tile for suitability to advance through a
 * non-accessible enemy.
 * 
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class AvancePertinence extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** */
	public static final String NAME = "AVANCE_PERTINENCE";

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public AvancePertinence( AlcarKayaYildirim ai ) throws StopRequestException
	{
		super(ai,NAME );
		ai.checkInterruption();
		this.ai1 = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** */
	protected AlcarKayaYildirim	ai1;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai1.checkInterruption();
		boolean result = false;

		try
		{
			if ( this.ai1.getClosestAccDesWalltoEnemy().getNeighbors().contains( tile ) )
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
