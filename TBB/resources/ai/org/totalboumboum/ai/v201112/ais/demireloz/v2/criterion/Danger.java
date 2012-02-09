package org.totalboumboum.ai.v201112.ais.demireloz.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201112.ais.demireloz.v2.DemirelOz;

/**
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class Danger extends AiUtilityCriterionBoolean {
	public static final String NAME = "Danger";

	public Danger(DemirelOz ai) throws StopRequestException {
		super(NAME);
		ai.checkInterruption();
		this.ai = ai;
	}

	protected DemirelOz ai;

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		// Checks if we can put the nearest enemy in danger, if we put a bomb in
		// the given tile.
		ai.checkInterruption();
		boolean result = false;

		AiZone zone = this.ai.getZone();
		AiSimZone simzone = new AiSimZone(zone);
		simzone.createBomb(null, simzone.getOwnHero());

		if (/*this.ai.simDanger(this.ai.getNearestEnemy().getTile(), simzone)
				|| */this.ai.getSafeTiles(this.ai.getNearestEnemy(), null).isEmpty()
				|| this.ai.getSafeTiles(this.ai.getNearestEnemy(), null) == null) {
			return result = true;
		}

		return result;
	}
}
