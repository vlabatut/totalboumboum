package org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.CoskunOzdemir;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * The criteria that will evaluate the tile for the number of destructible walls
 * surrounding it.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class NombreDeMurs extends AiUtilityCriterionInteger
{
	public static final String	NAME	= "NombreDeMurs";

	/**
	 * Bottom value of the utility.
	 */
	private final int			BOTTOM	= 1;

	/**
	 * Middle value of the utility.
	 */
	private final int			MIDDLE	= 2;
	/**
	 * Top value of the utility.
	 */
	private final int			TOP		= 3;
	/**
	 * Symbolises empty.
	 */
	private final int			EMPTY	= 0;

	public NombreDeMurs( CoskunOzdemir ai ) throws StopRequestException
	{ // init nom + bornes du domaine de définition
		super( NAME, 1, 3 );
		ai.checkInterruption();
		// init agent
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
	public Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		int count = EMPTY, result = EMPTY;

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

		if ( count == EMPTY )
			result = BOTTOM;
		else if ( count == BOTTOM || count == MIDDLE )
			result = MIDDLE;
		else if ( count == TOP ) result = TOP;

		return result;
	}
}