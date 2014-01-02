package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		// verbose = true;

	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() {
		ai.checkInterruption();

		// si nécessaire, pour réinitialiser certaines

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();

		List<AiTile> tiles = ai.getZone().getTiles();
		tiles = new ArrayList<AiTile>(tiles);
		// Collections.shuffle(tiles);
		// result.addAll(tiles.subList(0,Math.min(tiles.size(),10)));
		result.addAll(this.ai.tileHandler.selectedTiles);

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// CATEGORY /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Nom de la 1ère catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_1 = "CORRIDOR";
	/**
	 * Nom de la 2ème catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_2 = "SEARCH";
	/**
	 * Nom de la 3ème catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_3 = "HURRYUP";
	/**
	 * Nom de la 4ème catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_4 = "DEMITRIANGLE";

	
	
	@Override
	protected AiCategory identifyCategory(AiTile tile) {
		ai.checkInterruption();
		AiCategory result = getCategory(CAT_NAME_1);
		AiModeHandler<Agent> modeHandler = ai.getModeHandler();
		AiMode mode = modeHandler.getMode();
		AiHero ownHero = ai.getZone().getOwnHero();
		AiHero enemy = ai.enemyHandler.getNearestEnemy();
		
		int bombLeft = ownHero.getBombNumberMax()
				- ownHero.getBombNumberCurrent();

		
		if (mode == AiMode.ATTACKING) {
			if (ai.getZone().getBombs().size() > 9
					&& ownHero.getBombRange() == 1)
				result = getCategory(CAT_NAME_3);
			else if (enemy != null && ownHero.getBombRange() > 1
					&& bombLeft > 1) {
				if (ai.enemyHandler.triangle(enemy)
						&& ai.tileHandler.simpleTileDistance(enemy.getTile(),
								ownHero.getTile()) < 6) {

					result = getCategory(CAT_NAME_4);

				}
				if (!ai.enemyHandler.enemyInTiles.isEmpty()) {
					if (ai.enemyHandler.enemyInTiles.contains(enemy.getTile())) {
						result = getCategory(CAT_NAME_4);
					} else {
						ai.enemyHandler.enemyInTiles.clear();
						result = getCategory(CAT_NAME_1);
					}
				}
				if (ai.tileHandler.controlTriangleBombe > 1) {
					ai.enemyHandler.enemyInTiles.clear();
					result = getCategory(CAT_NAME_1);
				}

			} else
				result = getCategory(CAT_NAME_1);
		}

		else {
			result = getCategory(CAT_NAME_2);
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();

	}
}
