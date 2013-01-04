package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;


/**
 * @author Tugce Gergin
 * @author Seçil Özkanoğlu
 *
 */
public class ModeHandler extends AiModeHandler<GerginOzkanoglu>
{	

	/**
	 * If the hero's bomb count is greater than this amount, he has enough
	 * bombs.
	 */
	private final int BOMB_LIMIT = 3;
	/**
	 * If the hidden item count in the map drops to this number, collect may not
	 * be possible.
	 */
	private final int COLLECT_IMPOSSIBLE = 0;
	/**
	 *  If the hero's range is greater than this number, he has enough range.
	 */
	private final int BOMB_RANGE_LIMIT = 3;
	/**
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(GerginOzkanoglu ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		verbose = false;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
	return (this.ai.getZone().getOwnHero().getBombNumberMax() >= BOMB_LIMIT || this.ai.getZone().getOwnHero().getBombRange() >= BOMB_RANGE_LIMIT);
	
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();

	 return (this.ai.getZone().getHiddenItemsCount() > COLLECT_IMPOSSIBLE || !this.ai.getZone().getItems().isEmpty());
	
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
