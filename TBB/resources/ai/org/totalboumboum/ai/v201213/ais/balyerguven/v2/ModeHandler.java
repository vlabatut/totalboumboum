package org.totalboumboum.ai.v201213.ais.balyerguven.v2;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class ModeHandler extends AiModeHandler<BalyerGuven>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 */
	private final int	BOMB_NUMBER		= 3;
	
	/**
	 * 
	 */
	private final int	BOMB_RANGE		= 3;
	
	/**
	 * 
	 */
	private final int	NO_COLLECT_ITEM_COUNT	= 0;
	/**
	 * @param ai
	 * @throws StopRequestException
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
