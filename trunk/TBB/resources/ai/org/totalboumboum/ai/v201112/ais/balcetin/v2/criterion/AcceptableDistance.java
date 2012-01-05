package org.totalboumboum.ai.v201112.ais.balcetin.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.BalCetin;

/**
 * The criterion binary to decide if a tile is in acceptable distance for our AI.
 * 
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */

public class AcceptableDistance extends AiUtilityCriterionBoolean {
	/** Nom de ce critère */
	public static final String NAME = "AcceptableDistance";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AcceptableDistance(BalCetin ai) throws StopRequestException { // init nom
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
		//the tile is in max. 8 tiles away to our Hero ?
		if (zone.getTileDistance(ownHero.getTile(), tile) >= 8)
			result = false;

		return result;
	}
}
