package org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.BalCetin;


/**
 * Criterion to decide if the tile is max. 6 tiles away.
 * 
 * @return true if the tile is max 6 tiles away. false otherwise.
 * @author adnan
 * 
 */
public class AcceptableDistance extends AiUtilityCriterionBoolean {

	public static final String NAME = "AcceptableDistance";

	public AcceptableDistance(BalCetin ai) throws StopRequestException { // init
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
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		boolean result = true;
		// the tile is in max. 6 tiles away to our Hero ?
		if (zone.getTileDistance(ownHero.getTile(), tile) >= 6)
			result = false;

		return result;
	}
}
