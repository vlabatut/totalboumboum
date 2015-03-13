package org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.BalyerGuven;

/**
 * our NbrMurDest class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
@SuppressWarnings("deprecation")
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
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		
		int c = 0;
		for (AiTile neighbor : tile.getNeighbors()) {
			ai.checkInterruption();
			if (!neighbor.getBlocks().isEmpty() && neighbor.getBlocks().get(0).isDestructible() )
			{
				c++;
			}
		}
		if(c>0)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
}
