package org.totalboumboum.ai.v201112.ais.balcetin.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;

import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Mode handler class to choose the right mode for the AI.
 * 
 * 
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */

public class ModeHandler extends AiModeHandler<BalCetin> {


	protected ModeHandler(BalCetin ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		//no text output on the console
		verbose = false;
		zone = ai.getZone();
		ownHero = zone.getOwnHero();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	private AiZone zone;
	private AiHero ownHero;

	protected boolean hasEnoughItems() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		//the criteria is 2 bombs and 3 ranges.It's enough to pass to attack mode.
		if (ownHero.getBombNumberMax() >= 2 && ownHero.getBombRange() >= 3)
			result = true;
		else
			result = false;

		return result;
	}

	@Override
	protected boolean isCollectPossible() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;

		int hiddenitems = zone.getHiddenItemsCount();
		// if there is at least 1 hidden item and the remaining time is higher
		// than a half.
		if (hiddenitems > 0
				&& (zone.getTotalTime() < (0.5) * zone.getLimitTime()))
			result = true;
		else
			result = false;

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();

	}
}
