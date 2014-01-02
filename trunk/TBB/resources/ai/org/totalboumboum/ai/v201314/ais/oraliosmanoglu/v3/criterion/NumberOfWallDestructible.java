package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v3.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * 
 *         cette critere nous envoyer la nombre de murs (integer (0,1,2 ou
 *         3))destructibles autour de case.
 * 
 */
public class NumberOfWallDestructible extends AiCriterionInteger<Agent> {
	/**
	 * variable de nom cette critere
	 */
	public static final String NAME = "NumberOfWallDestructible";

	/**
	 * Ce critere concern les mur peuvent etre detrui en posant une bombe a
	 * cette case.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public NumberOfWallDestructible(Agent ai) {
		super(ai, NAME, 0, 3);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	/**
	 * cette critere nous envoyer la nombre de murs destructibles autour de case.
	 * @param tile notre cases dans selection des cases
	 * @return integer 0,1,2 ou 3
	 */
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 0;
		AiTile up = tile.getNeighbor(Direction.UP);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		for (AiBlock blocks : ai.getZone().getDestructibleBlocks()) {
			ai.checkInterruption();
			if (blocks.getTile() == right || blocks.getTile() == left
					|| blocks.getTile() == up || blocks.getTile() == down)
				result++;
		}
		if (result == 4) {
			result = 3;
		}

		return result;

	}
}
