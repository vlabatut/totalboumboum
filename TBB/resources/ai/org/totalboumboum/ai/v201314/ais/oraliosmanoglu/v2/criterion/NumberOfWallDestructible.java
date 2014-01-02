package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

import org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v2.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * cette critere nous envoyer la nombre de murs (integer (0,1,2 ou
 * 3))destructibles autour de case.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * 
 * 
 * 
 */
public class NumberOfWallDestructible extends AiCriterionInteger<Agent> {
	/**
	 * description de nom de critere.
	 */
	public static final String NAME = "NumberOfWallDestructible";

	/**
	 * cette critere nous envoyer la nombre de murs destructibles autour de
	 * case.
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
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();

		// initialisation de result
		int result = 0;
		// on prend les tiles pour les voisins de cette tile(droit ,gauche,
		// haut, bas)
		AiTile up = tile.getNeighbor(Direction.UP);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		// loop pour les murs destructibles autour de notre agent.
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
