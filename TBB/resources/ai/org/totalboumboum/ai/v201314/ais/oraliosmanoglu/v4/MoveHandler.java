package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v4;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent. Cf. la documentation de
 * {@link AiMoveHandler} pour plus de détails.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 */
public class MoveHandler extends AiMoveHandler<Agent> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */

	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		{
			TimeCostCalculator costCalculator = new TimeCostCalculator(ai,
					ownHero);
			TimeHeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
					ai, ownHero);
			TimePartialSuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
					ai, SearchMode.MODE_NOTREE);
			astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator,
					successorCalculator);
		}
		{
			CostCalculator costCalculator = new ApproximateCostCalculator(ai,
					ownHero);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
					ai, ownHero);
			SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(
					ai);
			astarApproximation = new Astar(ai, ownHero, costCalculator,
					heuristicCalculator, successorCalculator);
		}
	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null;
	/**
	 * A titre d'exemple, je stocke le sprite controlé par cet agent, car on en
	 * a aussi souvent besoin
	 */
	private AiHero ownHero = null;
	/**
	 * Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le
	 * re-créer à chaque itération
	 */
	private Astar astar;
	/** represente case de destination suivante. */
	AiTile nextTile;
	/**
	 * Il est nécessaire de stocker l'objet astarApproximation, si on ne veut
	 * pas devoir le re-créer à chaque itération
	 */
	private Astar astarApproximation;
	/** represente case de destination */
	private AiTile destination = null;

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * Calcule l'objectif courant de l'agent, c'est à dire la case dans laquelle il veut aller
	 * @return tile destination
	 */
	protected AiTile processCurrentDestination() throws StopRequestException {
		ai.checkInterruption();
		AiTile result = null;
		AiTile ourtile = ai.getZone().getOwnHero().getTile();
		int distance = 0;
		int tamponDistance = 0;

		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();

		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();

		int minPref = Collections.min(preferences.keySet());// min oref in int
															// degeri
		if (!preferences.isEmpty()) {
			List<AiTile> tiles = preferences.get(minPref);// min preflerin
															// koordinatları
			result = tiles.get(0);
			distance = ai.getZone().getTileDistance(tiles.get(0), ourtile);

			for (int j = 0; j < tiles.size(); j++) {
				ai.checkInterruption();
				tamponDistance = ai.getZone().getTileDistance(tiles.get(j),
						ourtile);
				if (distance > tamponDistance) {
					distance = tamponDistance;
					result = tiles.get(j);
				}
			}
			if (destination == null) {
				destination = result;
			} else {
				if (!tiles.contains(destination))
					destination = result;
			}

		}
		return destination;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * Calcule le chemin courant de l'agent, c'est à dire la séquence de cases à parcourir pour atteindre (directement ou indirectement) la case objectif. 
	 * @return chemin 
	 */
	protected AiPath processCurrentPath() throws StopRequestException {
		ai.checkInterruption();
		AiPath result = null;
		boolean bombControl = ai.getZone().getBombsByColor(ownHero.getColor())
				.size() > 0;
		if (!bombControl
				&& !ai.isEnnemyAccesible
				&& ai.tileHandler.getClosestEnnemy().getTile() != ownHero
						.getTile()
				&& !ai.tileHandler.fourWall(ai.tileHandler.getClosestEnnemy())) {
			AiLocation startLocation = new AiLocation(ownHero);
			try {
				result = astarApproximation.startProcess(startLocation,
						ai.tileHandler.getClosestEnnemy().getTile());
			} catch (LimitReachedException e) {
				// result = new AiPath();
			}

		} else {
			AiLocation startLocation = new AiLocation(ownHero);
			AiTile endTile = getCurrentDestination();
			try {
				result = astar.startProcess(startLocation, endTile);
			} catch (LimitReachedException e) {
				// result = new AiPath();
			}

		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * Calcule la direction courante suivie par l'agent. 
	 * @return direction
	 */
	protected Direction processCurrentDirection() throws StopRequestException {
		ai.checkInterruption();
		Direction result = Direction.NONE;

		AiPath path = getCurrentPath();
		if (path == null || path.getLength() < 2)
			// cas où le chemin est vide, ou bien ne contient que la case
			// courante
			result = Direction.NONE;
		else {
			AiLocation nextLocation = path.getLocation(1);
			nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);
			// List<AiFire> dangerTiles=ai.getZone().getFires();

			if (!nextLocation.getTile().getFires().isEmpty())
				result = Direction.NONE;
			if (nextTile == null || nextTile.equals(currentTile))
				result = Direction.NONE;

			// ici, j'utilise une méthode de l'API pour calculer la direction
			// pour aller de la 1ère vers la 2nde case
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant les données de ce gestionnaire.
	 */
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		super.updateOutput();

	}
}
