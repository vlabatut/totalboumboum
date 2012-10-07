package org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.DemirelOz;

/**
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class Competition extends AiUtilityCriterionBoolean {
	/** */
	public static final String NAME = "Competition";
	/** */
	public static final int COMPETITITON_TRUE = 5;

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public Competition(DemirelOz ai) throws StopRequestException {
		super(NAME);
		ai.checkInterruption();
		this.ai = ai;
	}

	/** */
	protected DemirelOz ai;

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		AiZone zone = this.ai.getZone();
		boolean result = false;
	
		// If there is an enemy in a 5 tile range of the given tile then it
		// returns true.
		if (zone.getRemainingOpponents().isEmpty() != true) {
			for (AiHero currentopponent : zone.getRemainingOpponents()) {
				ai.checkInterruption();
				if (this.ai.getDist(currentopponent.getTile(), tile) <= COMPETITITON_TRUE) {
					result = true;
				} else
					result = false;
			}
		}

		return result;
	}
}