package org.totalboumboum.ai.v201314.ais.saglamturgut.v4.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v4.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Destroys wall criterion. Checks if a bomb is put to the given tile, 
 * can it destroy a destructible wall.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class DestroysWall extends AiCriterionBoolean<Agent> {
	/** Name of the criterion */
	public static final String NAME = "DESTROYS_WALL";

	/** Constructs the criterion.
	 * @param ai Ai to process.
	 */
	public DestroysWall(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Checks if a hypotetical bomb on the given tile can destroy a wall.
	 * @param tile Tile to check.
	 * @return true if the bomb can destroy a wall on a direction, false otherwise.
	 */
	@Override
	protected Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		for (Direction direction : Direction.getPrimaryValues()) {
			ai.checkInterruption();
			if (canDestroy(tile, direction)) return true;
		}

		return false;
	}

	/**
	 * Checks if a bomb put to the given tile by the agent can destroy a tile on the given direction.
	 *
	 * @param tileToCheck Tile to process.
	 * @param direction   Direction to process.
	 * @return true if a bomb put to the given tile by the agent can destroy a tile on the given direction,
	 *         false otherwise.
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
