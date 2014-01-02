package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v4.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/** Good potential tile criterion. Checks how many tiles a bomb in the given tile can destroy.
 */
public class GoodPotentialTile extends AiCriterionInteger<Agent> {
	/** Name of the criterion */
	public static final String NAME = "GP_TILE";

	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public GoodPotentialTile(Agent ai) {
		super(ai, NAME, 0, 4);
		ai.checkInterruption();
	}

	/**
	 * Returns the number of blocks will be destroyed if a bomb is put on the given tile.
	 * @param tile Tile to check.
	 * @return The wall count.
	 */
	@Override
	protected Integer processValue(AiTile tile) {
		ai.checkInterruption();

		// the question is: if i put a bomb here, how many tiles i can destroy?
		int count = 0;
		// look up, down, left and right
		if (canDestroy(tile, Direction.UP)) count++;
		if (canDestroy(tile, Direction.DOWN)) count++;
		if (canDestroy(tile, Direction.LEFT)) count++;
		if (canDestroy(tile, Direction.RIGHT)) count++;
		return count;
	}


	/** Checks if a bomb is put on the given tile can destroy a block towards the given direction.
	 * @param tileToCheck The tile to process.
	 * @param direction The direction to check.
	 * @return true if a bomb put on the given tile can destroy a block towards the given direction,
	 * false otherwise.
	 */
	private boolean canDestroy(AiTile tileToCheck, Direction direction) {
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		int myBombRange = ownHero.getBombRange();
		int distance = 0;
		AiFire firePrototype = ownHero.getBombPrototype().getFirePrototype();
		AiTile currentTile = tileToCheck;
		while (currentTile.isCrossableBy(firePrototype)) {
			ai.checkInterruption();
			distance++;
			currentTile = currentTile.getNeighbor(direction);
		}
		// now check
		for (AiBlock aiBlock : currentTile.getBlocks()) {
			ai.checkInterruption();
			if (aiBlock.isDestructible() && distance < myBombRange) return true;
		}
		return false;
	}
}
