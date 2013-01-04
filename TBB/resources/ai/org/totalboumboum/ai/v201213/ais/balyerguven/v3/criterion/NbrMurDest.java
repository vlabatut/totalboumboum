package org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.BalyerGuven;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * our NbrMurDest class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class NbrMurDest extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** Number of destructible walls */
	public static final String NAME = "NbrMurDest";
	
	/**
	 * Crée un nouveau critère boolean.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */

	public NbrMurDest(BalyerGuven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai = ai;
	}
	


    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		
		int count = 0;
		boolean result = false;

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
	
		
	if (count == 0)
		{
			result = false;
		}
	else if (count >= 1)
		{
			result = true;
		}
	
	return result;
	
	}
}
