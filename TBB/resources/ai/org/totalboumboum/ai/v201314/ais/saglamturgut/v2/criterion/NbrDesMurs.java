package org.totalboumboum.ai.v201314.ais.saglamturgut.v2.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saglamturgut.v2.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Number of walls criterion.
 * @author Neşe Güneş
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class NbrDesMurs extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "NMB_DE_MURS";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public NbrDesMurs(Agent ai) {
		super(ai, NAME, 1, 3);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int destructibleCount = 1;
		List<AiBlock> destructibles = ai.getZone().getDestructibleBlocks();
		List<AiTile> tiles = new ArrayList<AiTile>();
		for (AiBlock destructible : destructibles) {
			ai.checkInterruption();
			tiles.add(destructible.getTile());
		}

		if (tiles.contains(tile.getNeighbor(Direction.UP)))
			destructibleCount++;
		if (tiles.contains(tile.getNeighbor(Direction.DOWN)))
			destructibleCount++;
		if (tiles.contains(tile.getNeighbor(Direction.LEFT)))
			destructibleCount++;
		if (tiles.contains(tile.getNeighbor(Direction.RIGHT)))
			destructibleCount++;

		if (destructibleCount > 3)
			destructibleCount = 3;

		return destructibleCount;
	}
}
