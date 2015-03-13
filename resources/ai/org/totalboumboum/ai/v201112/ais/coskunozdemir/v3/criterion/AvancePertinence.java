package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.CoskunOzdemir;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.TileOperation;

/**
 * The criteria that will evaluate the tile for suitability to advance through a
 * non-accessible enemy.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class AvancePertinence extends AiUtilityCriterionBoolean
{	/** */
	public static final String	NAME	= "AvancePertinence";

	/**
	 * 
	 * @param ai
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public AvancePertinence( CoskunOzdemir ai ) throws StopRequestException
	{
		super( NAME );
		ai.checkInterruption();
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** */
	protected CoskunOzdemir	ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		boolean result = false;
		TileOperation to = this.ai.getTo();
		if(to!=null)
		{	AiTile cl = to.getClosestAccDesWalltoEnemy();
			if(cl!=null)
			{	List<AiTile> neighbors = cl.getNeighbors();
				if(neighbors!=null && neighbors.contains( tile ) )
				{	result = true;
				}
			}
		}
			
		return result;
	}
}
