package org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.DemirelOz;

/**
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class Time extends AiUtilityCriterionBoolean {
	/** */
	public static final String NAME = "Time";
	/** */
	public static final int TIME_COLLECT = 3;
	/** */
	public static final int TIME_ATTACK = 5;

	/**
	 * 
	 * @param ai
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public Time(DemirelOz ai) throws StopRequestException {
		super(NAME);
		ai.checkInterruption();

		this.ai = ai;
	}

	/** */
	protected DemirelOz ai;

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {

		// This function will return the closest tile in the selected tile list,
		// for a given tile
		ai.checkInterruption();
		AiTile ourtile = this.ai.getZone().getOwnHero().getTile();
		boolean result = false;

		if (tile.equals(this.ai.getClosestTile(ourtile))) {
			result = true;
		}

		return result;

	}

}
