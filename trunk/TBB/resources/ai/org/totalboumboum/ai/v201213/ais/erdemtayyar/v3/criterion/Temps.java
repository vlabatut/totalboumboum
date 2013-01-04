package org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.ErdemTayyar;

/**
 * The criteria that will evaluate the tile for the time to reach it. The time
 * is directly proportional to its distance, so the criteria will process the
 * distance.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
@SuppressWarnings("rawtypes")
public class Temps extends AiUtilityCriterionBoolean {

	/**
	 * We affect the name of out criteria
	 */
	public static final String NAME = "Temps";

	/**
	 * Distance radius limit to check to determine the tile's state.
	 */
	private final int limit = 6;

	//Constructor
	/**
	 * C'est un critere binaire.
	 * On initialise la valeur dont la domaine de définition est  TRUE et FALSE.
	 * Si la distance est plus petit  ou egale a 6 la sa valeur est TRUE sinon FALSE.
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unchecked")
	public Temps(ErdemTayyar ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();

	}


	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	
	
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		if (this.ai.getZone()
				.getTileDistance(((ErdemTayyar) this.ai).getHero().getTile(), tile) <= limit)
			result = true;

		return result;
	}
}
