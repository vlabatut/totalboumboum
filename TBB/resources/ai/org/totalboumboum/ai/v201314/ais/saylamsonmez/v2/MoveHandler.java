package org.totalboumboum.ai.v201314.ais.saylamsonmez.v2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent. Cf. la documentation de
 * {@link AiMoveHandler} pour plus de détails.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class MoveHandler extends AiMoveHandler<Agent> {

	/**
	 * Minimum cost for costCalculator
	 */
	private static double MIN_COST = 0;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		/*
		 * // on règle la sortie texte pour ce gestionnaire verbose = false;
		 */

		// dans cette classe, on aura généralement besoin d'un objet de type
		// Astar.
		// pour des raisons de rapidité, il est recommandé de créer l'objet
		// Astar une seule fois,
		// et non pas à chaque itération. Cela permet aussi d'éviter certains
		// problèmes de mémoire.
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** On stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null;
	/**
	 * On stocke le sprite controlé par cet agent, car on en a aussi souvent
	 * besoin
	 */
	private AiHero ownHero = null;
	/**
	 * Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le
	 * re-créer à chaque itération
	 */
	private Astar astar = null;

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();
		AiTile currentDestination = null;
		AiTile lastDestination = null;
		// ici, on se contente de prendre la case dont la préférence est
		// minimale
		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet()); // ATTENTION : ici
																// il faudrait
																// tester qu'il
																// y a au moins
																// une valeur
																// dans la map
																// (sinon :
																// NullPointerException
																// !)
		List<AiTile> tiles = preferences.get(minPref); // on récupère la liste
														// de cases qui ont la
														// meilleure préférence
		lastDestination = getCurrentDestination();
		if (tiles.contains(lastDestination))
			currentDestination = lastDestination;
		else
			currentDestination = tiles
					.get((int) (Math.random() * (tiles.size() - 1)));

		return currentDestination;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath() {
		ai.checkInterruption();

		AiPath path = new AiPath();
		AiTile endTile = getCurrentDestination();
		if (endTile != null) {
			AiHero ourHero = this.ai.getZone().getOwnHero();
			TimeCostCalculator costCalculator = new TimeCostCalculator(ai,
					ourHero);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
					this.ai, ourHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
					this.ai, SearchMode.MODE_NOTREE);

			costCalculator.setOpponentCost(MIN_COST);

			astar = new Astar(this.ai, ourHero, costCalculator,
					heuristicCalculator, successorCalculator);
			AiLocation ourLocation = new AiLocation(ourHero);
			try {
				path = astar.startProcess(ourLocation, endTile);
			} catch (LimitReachedException e) {
				path = new AiPath();
				path.addLocation(new AiLocation(ourHero.getTile()));
			} catch (NullPointerException e) {
				path = new AiPath();
				path.addLocation(new AiLocation(ourHero.getTile()));

			}
		}
		return path;

	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction processCurrentDirection() {
		ai.checkInterruption();
		Direction result = Direction.NONE;

		// dans cette méthode, on doit utiliser les calculs précédents
		// (accessibles via
		// getCurrentDestination et getCurrentPath) pour choisir la prochaine
		// direction
		// à suivre pour notre agent.
		AiPath path = getCurrentPath();
		if (path == null || path.getLength() < 2) // cas où le chemin est vide,
			// ou bien ne contient que la case courante
			result = Direction.NONE;
		else {
			AiLocation nextLocation = path.getLocation(1);
			// un chemin est une séquence de AiLocation (position en pixel),
			// chaque
			// AiLocation contient la AiTile correspondant à la position.
			AiTile nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);
			// ici, j'utilise une méthode de l'API pour calculer la direction
			// pour aller de la 1ère vers la 2nde case
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
