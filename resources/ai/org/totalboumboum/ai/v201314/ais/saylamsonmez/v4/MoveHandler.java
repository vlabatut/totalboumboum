package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4;

import java.util.Collections;
import java.util.Iterator;
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
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.TileCalculationHandler;

/**
 * Classe gérant le déplacement de l'agent.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<Agent> {

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** On stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null;
	/**
	 * On stocke le sprite controlé par cet agent, car on en
	 * a aussi souvent besoin
	 */
	private AiHero ownHero = null;
	/**
	 * Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le
	 * re-créer à chaque itération
	 */
	/** represents A* precise */
	protected Astar astar1 = null;
	/** represents A* approximation */
	protected Astar astar2 = null;
	/** represents A* precise */
	protected Astar astar3 = null;
	/**
	 * control pour la bombage
	 */
	public boolean control = false;

	/**
	 * represente les cases secure pour la destination
	 */
	public AiTile secureDest = null;

	/** pour acceder aux methodes de EnemyHandler */
	EnemyHandler enemyHandler;
	/** pour acceder aux methodes de TileCalculationHandler */
	TileCalculationHandler tileCalculationHandler;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// dans cette classe, on aura généralement besoin d'un objet de type
		// Astar.
		zone = ai.getZone();
		ownHero = ai.getZone().getOwnHero();

		{
			CostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
			costCalculator.setOpponentCost(0);
			costCalculator.setMalusCost(1000000000);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
					ai, ownHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
					this.ai, SearchMode.MODE_NOTREE);
			astar1 = new Astar(ai, ownHero, costCalculator,
					heuristicCalculator, successorCalculator);
		}

		{
			CostCalculator costCalculator1 = new ApproximateCostCalculator(ai,
					ownHero);
			HeuristicCalculator heuristicCalculator1 = new TimeHeuristicCalculator(
					ai, ownHero);
			SuccessorCalculator successorCalculator1 = new ApproximateSuccessorCalculator(
					ai);
			astar2 = new Astar(ai, ownHero, costCalculator1,
					heuristicCalculator1, successorCalculator1);
		}

		{
			CostCalculator costCalculator2 = new TimeCostCalculator(ai, ownHero);
			costCalculator2.setOpponentCost(1000000000);
			costCalculator2.setMalusCost(1000000000);
			HeuristicCalculator heuristicCalculator2 = new TimeHeuristicCalculator(
					ai, ownHero);
			SuccessorCalculator successorCalculator2 = new TimePartialSuccessorCalculator(
					this.ai, SearchMode.MODE_NOTREE);
			astar3 = new Astar(ai, ownHero, costCalculator2,
					heuristicCalculator2, successorCalculator2);
		}

	}

	/**
	 * Initialisation de gestionnaire
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected void initHandler(Agent ai) {
		ai.checkInterruption();
		this.ai = ai;

		tileCalculationHandler = ai.tileCalculationHandler;
		enemyHandler = ai.enemyHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();
		AiTile currentDestination = null;
		AiTile lastDestination = null;

		// ici, on se contente de prendre la case dont la
		// préférence est minimale

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

		if (tiles.contains(lastDestination)) {
			currentDestination = lastDestination;
		} else if (!tiles.contains(lastDestination)) {
			currentDestination = tiles
					.get((int) (Math.random() * (tiles.size() - 1)));
		}

		return currentDestination;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath() {
		ai.checkInterruption();
		AiPath currentPath = null;
		AiLocation firstLocation = new AiLocation(ownHero);
		AiTile endTile = getCurrentDestination();

		AiHero enemy = enemyHandler.selectEnemy();
		control = false;

		if (tileCalculationHandler.getReachableTiles(
				ai.getZone().getOwnHero().getTile()).contains(endTile)) {
			try {
				if (ai.bombHandler.bombTile == null) {
					currentPath = astar1.startProcess(firstLocation, endTile);
				} else if (ai.bombHandler.bombTile.getBombs().isEmpty()) {
					currentPath = astar1.startProcess(firstLocation, endTile);
				} else if (!ai.bombHandler.bombTile.getBombs().isEmpty()
						&& enemy.getBombNumberMax() != 0) {
					currentPath = astar3.startProcess(firstLocation, endTile);
				}

			} catch (LimitReachedException e) {
				currentPath = new AiPath();
				currentPath.addLocation(new AiLocation(ownHero.getTile()));
			} catch (NullPointerException e) {
				currentPath = new AiPath();
				currentPath.addLocation(new AiLocation(ownHero.getTile()));

			}
		} else {

			try {
				currentPath = astar2.startProcess(firstLocation, endTile);

				if (currentPath != null) {
					Iterator<AiLocation> iterator = currentPath.getLocations()
							.iterator();
					AiTile blockTile = null;
					AiTile previousTile = null;
					while (iterator.hasNext() && blockTile == null) {
						ai.checkInterruption();
						AiTile tile = iterator.next().getTile();
						if (!tile.getBlocks().isEmpty()) {
							blockTile = previousTile;
						}
						previousTile = tile;
						if (!tile.getBombs().isEmpty()) {
							AiTile secureTile = tileCalculationHandler
									.getSecureTile();
							return astar1.startProcess(firstLocation,
									secureTile);
						}

						if (blockTile != null && blockTile.getBombs().isEmpty()) {
							if (blockTile.equals(ownHero.getTile())) {
								control = true;
							}
						} else if (blockTile != null
								&& !blockTile.getBombs().isEmpty()) {
							AiTile secureTile = tileCalculationHandler
									.getSecureTile();
							return astar1.startProcess(firstLocation,
									secureTile);
						}
					}
					return currentPath;
				}
			} catch (LimitReachedException e) {
				currentPath = new AiPath();
				currentPath.addLocation(new AiLocation(ownHero.getTile()));
			} catch (NullPointerException e) {
				currentPath = new AiPath();
				currentPath.addLocation(new AiLocation(ownHero.getTile()));

			}
		}
		return currentPath;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction processCurrentDirection() {
		ai.checkInterruption();
		Direction result = Direction.NONE;

		AiPath path = getCurrentPath();
		// cas où le chemin est vide,
		// ou bien ne contient que
		// la case courante
		if (path == null || path.getLength() < 2)
			result = Direction.NONE;
		

		else {
			AiLocation nextLocation = path.getLocation(1);
			AiTile nextTile = nextLocation.getTile();
			long wait = path.getFirstPause();
			if (wait > 0) {
				result = Direction.NONE;
			}

			else {
				AiTile currentTile = ownHero.getTile();
				List<AiTile> dangerousTiles = tileCalculationHandler
						.getCurrentDangerousTiles();
				if (dangerousTiles.contains(currentTile)
						|| !dangerousTiles.contains(nextTile))
					result = zone.getDirection(currentTile, nextTile);
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
