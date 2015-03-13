package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Our mode handler class.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<CoskunOzdemir>
{
	/**
	 * If the hero's bomb count is greater than this amount, he has enough
	 * bombs.
	 */
	private final int	BOMB_NUMBER_LIMIT		= 3;
	/**
	 * If the hero's range is greater than this number, he has enough range.
	 */
	private final int	BOMB_RANGE_LIMIT		= 3;
	/**
	 * If the hidden item count in the map drops to this number, collect may not
	 * be possible.
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
	protected ModeHandler( CoskunOzdemir ai ) throws StopRequestException
	{
		super( ai );
		ai.checkInterruption();
		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{
		ai.checkInterruption();
		return ( ( this.ai.getHero().getBombNumberMax() >= BOMB_NUMBER_LIMIT || this.ai.getHero().getBombRange() >= BOMB_RANGE_LIMIT ) );
	}

	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{
		ai.checkInterruption();
		return ( this.ai.getZone().getHiddenItemsCount() > NO_COLLECT_ITEM_COUNT );
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	protected void updateOutput() throws StopRequestException
	{
		ai.checkInterruption();
	}
}
