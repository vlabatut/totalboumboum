package org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.DemirelOz;

public class Convenience extends AiUtilityCriterionBoolean {

	public static final String NAME = "Convenience";
	public static final int BOMB_NUMBER = 2;
	public static final int RANGE_NUMBER = 3;

	public Convenience(DemirelOz ai) throws StopRequestException {
		super(NAME);
		ai.checkInterruption();
		this.ai = ai;
	}

	protected DemirelOz ai;

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		// Checks if a visible item in the given tile is convenient or not.
		ai.checkInterruption();
		AiHero ourhero = this.ai.getZone().getOwnHero();
		boolean result = false;

		if (ourhero.getBombNumberMax() < BOMB_NUMBER) {
			if (tile.getItems().contains(AiItemType.EXTRA_BOMB)) {
				result = true;
			}
		}
		if (ourhero.getBombRange() < RANGE_NUMBER) {
			if (tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
				result = true;
			}
		}
		return result;

	}
}
