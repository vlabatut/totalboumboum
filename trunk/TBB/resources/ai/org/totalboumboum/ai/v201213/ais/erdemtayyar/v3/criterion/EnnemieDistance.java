package org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.ErdemTayyar;


/**
 * The criteria that will evaluate the tile for the time to reach it. The time
 * is directly proportional to its distance, so the criteria will process the
 * distance.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */

public class EnnemieDistance extends AiUtilityCriterionBoolean<ErdemTayyar> {

	
	/**
	 * We affect the name of out criteria
	 */
	public static final String	NAME	= "EnnemieDistance";

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	
	public EnnemieDistance( ErdemTayyar ai ) throws StopRequestException
	{
		super(ai, NAME);
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
			if (this.ai.getTs().getClosestAccDesWalltoEnemy().getNeighbors().contains( tile ) )
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
