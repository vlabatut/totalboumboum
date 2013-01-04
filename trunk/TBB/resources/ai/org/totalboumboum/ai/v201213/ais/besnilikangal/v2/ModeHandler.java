package org.totalboumboum.ai.v201213.ais.besnilikangal.v2;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;

/**
 * Classe gérant les déplacements de l'agent. 

 * @author Mustafa Besnili
 * @author Doruk Kangal
 */
public class ModeHandler extends AiModeHandler<BesniliKangal>
{	
	/** */
	private final int BOMB_RANGE_LIMIT = 3;
	/** */
	private final int BOMB_COUNT_LIMIT = 3;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(BesniliKangal ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{
		ai.checkInterruption();
		return ( ( ai.ownHero.getBombRange() >= BOMB_RANGE_LIMIT ) || ( ai.ownHero.getBombNumberMax() >= BOMB_COUNT_LIMIT ) );
	}

	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiItem itemVisible : ai.getZone().getItems() )
		{
			ai.checkInterruption();
			String itemName = itemVisible.getType().name();
			if ( itemName.contains( "EXTRA" ) || itemName.contains( "GOLDEN" ) )
				return true;
		}
		return ( ai.getZone().getHiddenItemsCount() > 0 );
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{
		ai.checkInterruption();
	}
}
