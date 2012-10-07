package org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.BalCetin;

/**
 * Criterion binary to decide if there is competition for a tile.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class Competition extends AiUtilityCriterionBoolean {
	
	/** */
	public static final String NAME = "Competition";
	
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public Competition(BalCetin ai) throws StopRequestException { // init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** */
	protected BalCetin ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();

		int ownHeroDistance = zone.getTileDistance(ownHero.getTile(), tile);
		boolean result = false;
		// for each hero, we check if he is closer than us to a tile.
		for (AiHero opponentHero : zone.getRemainingOpponents()) {
			ai.checkInterruption();
			if (zone.getTileDistance(opponentHero.getTile(), tile) < ownHeroDistance)
				result = true;
		}

		return result;
	}
}
