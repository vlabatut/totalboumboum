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
public class Distance extends AiUtilityCriterionBoolean {
	/** */
	public static final String NAME = "Distance";
	/** */
	public static final int DISTANCE_TRUE = 4;

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public Distance(DemirelOz ai) throws StopRequestException {
		super(NAME);
		ai.checkInterruption();
		this.ai = ai;
	}

	protected DemirelOz ai;

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		// Simply calculates the non cyclic distance between the nearest enemy
		// and the given tile.
		ai.checkInterruption();
		boolean result = false;
		
		if (this.ai.getDist(tile, this.ai.getNearestEnemy().getTile()) <= DISTANCE_TRUE) {
			result = true;
		}

		return result;
	}

}
