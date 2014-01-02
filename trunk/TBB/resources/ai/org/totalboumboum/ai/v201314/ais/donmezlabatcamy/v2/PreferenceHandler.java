package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe s'occupe des séléctions des cases sur lesquelles les valeurs de
 * préférences vont être calculées et détérmine une catégorie pour chaque case
 * séléctionnée.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 * 
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

	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() {
		ai.checkInterruption();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Séléction des cases dont la valeur de préférence va être calculée.
	 * 
	 * @return result un arbre contenant les case séléctionnée.
	 */
	@Override
	public Set<AiTile> selectTiles() {
		ai.checkInterruption();
		Set<AiTile> selectedTiles = new TreeSet<AiTile>();
		AiHero myHero = ai.getZone().getOwnHero();
		AiTile myTile = myHero.getTile();
		AiTile tmpTile = myTile;
		Queue<AiTile> queueTile = new LinkedList<AiTile>();
		queueTile.add(tmpTile);

		while (!queueTile.isEmpty()) {
			ai.checkInterruption();
			tmpTile = queueTile.poll();
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if (tmpTile.getNeighbor(direction).getBlocks().isEmpty()
						&& tmpTile.getNeighbor(direction).getBombs().isEmpty()
						&& !queueTile.contains(tmpTile.getNeighbor(direction))
						&& !selectedTiles.contains(tmpTile
								.getNeighbor(direction))
						&& !tmpTile.getNeighbor(direction).equals(myTile)
						&& tmpTile.getNeighbor(direction).getFires().isEmpty()) {
					queueTile.add(tmpTile.getNeighbor(direction));
				}
			}
			if (!queueTile.isEmpty()) {
				tmpTile = queueTile.peek();
				selectedTiles.add(tmpTile);
			} else {
				break;
			}
			selectedTiles.add(myTile);
			for (AiHero heroes : ai.getZone().getHeroes()) {
				ai.checkInterruption();
				if (selectedTiles.contains(heroes.getTile())) {
					for (AiBomb selectedBombs : ai.getZone().getBombsByColor(
							(heroes.getColor()))) {
						ai.checkInterruption();
						selectedTiles.add(selectedBombs.getTile());

					}
				}

			}
		}
		return selectedTiles;
	}

	// ///////////////////////////////////////////////////////////////
	// CATEGORY /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Nom de la 1ère catégorie
	 */
	private static String CAT_NAME_1 = "CAT_ITEM";
	/**
	 * Nom de la 2ème catégorie
	 */
	private static String CAT_NAME_2 = "CAT_DEST_WALL";
	/**
	 * Nom de la 3ème catégorie
	 */
	private static String CAT_NAME_3 = "CAT_ENNEMY";

	/**
	 * Choix de la catégorie pour les cases séléctionnée.
	 * 
	 * @param tile
	 *            la case de départ.
	 * @return result la catégorie de la case séléctionnée.
	 */
	@Override
	protected AiCategory identifyCategory(AiTile tile) {
		ai.checkInterruption();
		AiCategory result = null;
		AiModeHandler<Agent> modeHandler = ai.getModeHandler();
		AiMode mode = modeHandler.getMode();
		// On choisit une categorie lorsqu'il n'y a pas d'adversaire pour
		// empecher l'exception NullPointerException d'etre leve pendant les
		// trois derniere seconde apres l'elimination du dernier adversaire
		if (ai.getZone().getRemainingOpponents().size() == 0)
			result = getCategory(CAT_NAME_3);

		if (mode == AiMode.ATTACKING) {
			if (ai.hasEnoughEnnemyAccesible()) {
				result = getCategory(CAT_NAME_3);
			} else {
				result = getCategory(CAT_NAME_2);
			}

		} else {
			if (ai.usefulItemsAccesibility()) {
				result = getCategory(CAT_NAME_1);
			} else {
				result = getCategory(CAT_NAME_2);
			}
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
