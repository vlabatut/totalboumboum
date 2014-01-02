package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion;

import java.util.*;

import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe va nous permettre de traiter une case par rapport au critere
 * binaire "Nombre de sorties".
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class NombreDestructible extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "NUMBER_DESTRUCTIBLE";

	/**
	 * Crée le critère entier "Nombre de Destr".
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */

	public NombreDestructible(Agent ai) {
		super(ai, NAME, 0, 4);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Définit le traitement de la case par rapport a notre critere.
	 * 
	 * @param tile
	 *            la case concernée.
	 * @return result renvoie la valeur entier qui est obtenu par le traitement.
	 *         Ici, la valeur est le nombre de cases adjacentes a la case
	 *         concerné qui sont traversable par notre agent.
	 */
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException 
	{
		ai.checkInterruption();
		int result = 0;
		AiZone zone = ai.getZone();
		AiTile tile_up = tile.getNeighbor(Direction.UP);
		AiTile tile_down = tile.getNeighbor(Direction.DOWN);
		AiTile tile_left = tile.getNeighbor(Direction.LEFT);
		AiTile tile_right =tile.getNeighbor(Direction.RIGHT);
		int i = 1, bomb_range = zone.getOwnHero().getBombRange();
		// obstacles sont pour terminer les recherce de murs quand on se
		// rencontre des murs.
		boolean[] obstacle = { true, true, true, true, true };

		while (obstacle[4] && (i <= bomb_range)) {
			ai.checkInterruption();

			List<AiBlock> blocks;

			if (obstacle[0]) {
				blocks = tile_up.getBlocks();
				if (!tile_up.getItems().isEmpty())
					obstacle[0] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[0] = false;
					}
				}

				tile_up = tile_up.getNeighbor(Direction.UP);
			}
			if (obstacle[1]) {
				blocks = tile_down.getBlocks();
				if (!tile_down.getItems().isEmpty())
					obstacle[1] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[1] = false;
					}
				}
				tile_down = tile_down.getNeighbor(Direction.DOWN);
			}
			if (obstacle[2]) {
				blocks = tile_left.getBlocks();
				if (!tile_left.getItems().isEmpty())
					obstacle[2] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[2] = false;
					}
				}
				tile_left = tile_left.getNeighbor(Direction.LEFT);
			}
			if (obstacle[3]) {
				blocks = tile_right.getBlocks();
				if (!tile_right.getItems().isEmpty())
					obstacle[3] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[3] = false;
					}
				}
				tile_right = tile_right.getNeighbor(Direction.RIGHT);
			}
			if ((!obstacle[0]) && (!obstacle[1])
					&& (!obstacle[2] && (!obstacle[3])))
				obstacle[4] = false;

			i++;

		}
		if (result > 4)
			result = 4;
		return result;
	}
}
