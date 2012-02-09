package org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.BalCetin;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.HeroProcess;

/**
 * The criterion binary to decide the tile of the most relevant hero for us
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class AttackMostRelevantHero extends AiUtilityCriterionBoolean {
	public static final String NAME = "AttackMostRelevantHero";


	public AttackMostRelevantHero(BalCetin ai) throws StopRequestException { // init
																				// nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected BalCetin ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		HeroProcess hp = new HeroProcess(ai);
		// checks if this tile is the tile of the most relevant hero for us.
		if (tile == hp.mostRelevantHero().getTile())
			result = true;

		return result;
	}
}
