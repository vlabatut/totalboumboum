package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.Agent;

/**
 * Cette critere est pour savoir que si une case est dangereuse ou pas. on
 * calcule les valeurs du critere avec les methodes dangerousTiles qui prend les
 * cases qui contiennent de flamme ou de bombe, et les met dans une liste, et
 * isMalus qui nous donne la situation d'item qui se trouve dans la case qu'on
 * examine cette critere nous donne 1 si la case est dangeruese et 0 sinon
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
@SuppressWarnings("deprecation")
public class Danger extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "DANGER";

	/**
	 * Crée un critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Danger(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		for (AiItem item : tile.getItems()) {
			ai.checkInterruption();

			if (ai.itemHandler.isMalus(item))
				return true;
		}

	/*	for (AiTile enemyTile : ai.enemyHandler.enemyTiles()) {
			ai.checkInterruption();

			if (ai.enemyHandler.dangerEnemy != null)
				if (!ai.enemyHandler.enemyTiles().contains(ownTile)) {
					if (ai.tileHandler.simpleTileDistance(ownTile,
							ai.enemyHandler.dangerEnemy) < ai.tileHandler
							.simpleTileDistance(ownTile, tile))
						return true;
				}
		}*/
		
//		if (ai.tileHandler.isTileBehindEnemy(tile))
//			return true;

		if (!(this.ai.tileHandler.dangerousTiles().isEmpty())
				&& (this.ai.tileHandler.dangerousTiles().contains(tile))) {
			return true;
		}

		return false;
	}
}
