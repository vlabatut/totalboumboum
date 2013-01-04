package org.totalboumboum.ai.v201213.ais.balyerguven.v3;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 * our mode handler class.
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class ModeHandler extends AiModeHandler<BalyerGuven>
{	
	/**
	 * represents the bomb number
	 */
	private final int	BOMB_NUMBER		= 0;
	
	/**
	 * represents the bomb range
	 */
	private final int	BOMB_RANGE		= 3;
	
	/**
	 * represents the count of collectible items
	 */
	private final int	NO_COLLECT_ITEM_COUNT	= 0;

	/**
	 * Constructs a handler for the agent passed as a parameter.
	 * 
	 * @param ai
	 *            The agent that the class will handle.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	
	protected ModeHandler(BalyerGuven ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
		AiZone zone = this.ai.getZone();
		AiHero myhero = zone.getOwnHero();
		boolean result = false;
		if (myhero.getBombNumberMax() >= BOMB_NUMBER && myhero.getBombRange() >= BOMB_RANGE)
		{
			result = true;
		}
		
		return result;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
		AiZone zone = this.ai.getZone();
		int item = zone.getHiddenItemsCount();
		boolean result = false;
		
		if(item > NO_COLLECT_ITEM_COUNT)
		{
			result = true;
		}
		
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
	}
}
