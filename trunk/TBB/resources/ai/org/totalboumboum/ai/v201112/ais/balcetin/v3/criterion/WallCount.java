package org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.BalCetin;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Criterion integer to decide how many wall neighbors a tile has.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
public class WallCount extends AiUtilityCriterionInteger {
	public static final String NAME = "WallCount";

	public WallCount(BalCetin ai) throws StopRequestException {
		// criterion domain
		super(NAME, 0, 3);
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
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		int count = 0;
		int result = 0;

		// we check each neighbor of the tile if there is a destructible wall.
		for (AiBlock Block : tile.getNeighbor(Direction.UP).getBlocks()) {
			ai.checkInterruption();
			if (Block.isDestructible())
				count++;
		}
		for (AiBlock Block : tile.getNeighbor(Direction.DOWN).getBlocks()) {
			ai.checkInterruption();
			if (Block.isDestructible())
				count++;
		}
		for (AiBlock Block : tile.getNeighbor(Direction.LEFT).getBlocks()) {
			ai.checkInterruption();
			if (Block.isDestructible())
				count++;
		}
		for (AiBlock Block : tile.getNeighbor(Direction.RIGHT).getBlocks()) {
			ai.checkInterruption();
			if (Block.isDestructible())
				count++;
		}

		if (count == 0)
			result = 0;
		else if (count == 1)
			result = 1;
		else if (count == 2)
			result = 2;
		else if (count == 3)
			result = 3;

		return result;
	}
}
