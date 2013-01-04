package org.totalboumboum.ai.v201213.ais.balyerguven.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v2.BalyerGuven;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class NbrMurDest extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** Nom de ce critère */
	public static final String NAME = "NbrMurDest";
	
	/**
	 * Crée un nouveau critère binaire.
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
