package org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.DemirelOz;

/**
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class NbrAdjacentWall extends AiUtilityCriterionInteger {
	/** */
	public static final String NAME = "NbrAdjacentWall";

	/**
	 * 
	 * @param ai
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public NbrAdjacentWall(DemirelOz ai) throws StopRequestException {
		super(NAME, 0, 3);
		ai.checkInterruption();
		this.ai = ai;
	}

	/** */
	protected DemirelOz ai;

	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {

		ai.checkInterruption();
		int result = 0;

		AiMode mode = this.ai.getMode();

		if (mode == AiMode.COLLECTING) {
			// We basically count the number of destructible walls
			for (AiTile currentNeighbors : tile.getNeighbors()) {
				ai.checkInterruption();
				for (AiBlock currentblock : currentNeighbors.getBlocks()) {
					ai.checkInterruption();

					if (currentblock.isDestructible()) {
						result = result + 1;
					}
				}
			}
		}

		else {
			// In the attack mode we count the number of obstacles around the
			// given tile
			for (AiTile currentNeighbors : tile.getNeighbors()) {
				ai.checkInterruption();
				if (!currentNeighbors.getBlocks().isEmpty()) {
					result = result + 1;
				}
			}

		}

		return result;
	}

}
