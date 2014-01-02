package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent. En particulier,
 * elle implémente la méthode update de l'algorithme général.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre. Calculer les
	 * preferences, selection des cases
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * Réinitialise les structures de données modifiées à chaque itération. Cette méthode concerne seulement 
	 *les structures de données créées par le concepteur : les structures imposées sont réinitialisées par 
	 * resetData(). 
	 *Cette méthode est appelée automatiquement, vous (le concepteur de l'agent) n'avez pas besoin de 
	 *l'appeler.
	 */
	protected void resetCustomData() {
		ai.checkInterruption();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	/**
	 * Réinitialise les structures de données modifiées à chaque itération. Cette méthode concerne seulement 
	les structures de données créées par le concepteur : les structures imposées sont réinitialisées par 
	resetData(). 
	Cette méthode est appelée automatiquement, vous (le concepteur de l'agent) n'avez pas besoin de 
	l'appeler.
	 * @return set AiTile
	 */
	protected Set<AiTile> selectTiles() {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();

		AiHero ourhero = this.ai.getZone().getOwnHero();
		AiTile ourtile = ourhero.getTile();
		AiTile aitile = ourtile;

		Queue<AiTile> qe = new LinkedList<AiTile>();
		qe.add(aitile);
		while (!qe.isEmpty()) {
			ai.checkInterruption();
			aitile = qe.poll();
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if (aitile.getNeighbor(direction).getBombs().isEmpty()
						&& aitile.getNeighbor(direction).getBlocks().isEmpty()
						&& !qe.contains(aitile.getNeighbor(direction))
						&& !result.contains(aitile.getNeighbor(direction))
						&& !aitile.getNeighbor(direction).equals(ourtile)) {
					qe.add(aitile.getNeighbor(direction));
				}
			}
			if (!qe.isEmpty()) {

				aitile = qe.peek();

				result.add(aitile);

			} else {
				break;
			}

			result.add(ourtile);

		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// CATEGORY /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Nom de la 1ère catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_1 = "Attack";

	/**
	 * Nom de la 2ème catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_2 = "Run";
	/**
	 * Nom de la 3ème catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_3 = "Collect";

	@Override
	/**
	 * Réinitialise les structures de données modifiées à chaque itération. Cette méthode concerne seulement 
	les structures de données créées par le concepteur : les structures imposées sont réinitialisées par 
	resetData(). 
	Cette méthode est appelée automatiquement, vous (le concepteur de l'agent) n'avez pas besoin de 
	l'appeler.
	 * @return category
	 */
	protected AiCategory identifyCategory(AiTile tile) {
		ai.checkInterruption();
		AiCategory result = getCategory(CAT_NAME_1);
		AiModeHandler<Agent> modeHandler = ai.getModeHandler();
		AiMode mode = modeHandler.getMode();
		// int distance = 0;
		// List<AiBomb> bombs = ai.getZone().getBombs();
		if (mode == AiMode.COLLECTING) {
			result = getCategory(CAT_NAME_3);
		}

		if (mode == AiMode.ATTACKING) {

			// if(ai.tileHandler.checkDangerTiles(ownTile)==false)
			if (ai.dangerTiles.size() > 25) {
				result = getCategory(CAT_NAME_2);
			}
			// else result = getCategory(CAT_NAME_1);

		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant les données de ce gestionnaire. 
	Ici, on affiche la valeur numérique de la préférence dans chaque case, et on colorie la case en fonction de 
	cette valeur : la couleur dépend du mode (bleu pour la collecte, rouge pour l'attaque) et son intensité 
	dépend de la préférence (clair pour une préférence faible, foncé pour une préférence élevée). 
	Cette méthode peut être surchargée si vous voulez afficher les informations différemment, ou d'autres 
	informations. A noter que cette méthode n'est pas appelée automatiquement : elle doit l'être par la 
	fonction surchargeant ArtificialIntelligence.updateOutput() si vous désirez l'utiliser. 
	 */
	public void updateOutput() {
		ai.checkInterruption();

		super.updateOutput();

	}
}
