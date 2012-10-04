package org.totalboumboum.ai.v201112.ais.demireloz.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v2.DemirelOz;

/**
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class Time extends AiUtilityCriterionBoolean {
	/** */
	public static final String NAME = "Time";
	/** */
	public static final int TIME_TRUE = 3;

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public Time(DemirelOz ai) throws StopRequestException {
		super(NAME);
		ai.checkInterruption();

		this.ai = ai;
	}

	protected DemirelOz ai;

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {

		// If we are close by 2 tiles to the given tile this function will return true
		ai.checkInterruption();
		AiTile ourtile = this.ai.getZone().getOwnHero().getTile();
		boolean result = false;

		if (this.ai.getDist(ourtile, tile) <= TIME_TRUE) {
			result = true;
		}
		return result;

	}

}
