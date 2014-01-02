package org.totalboumboum.ai.v201314.ais.saylamsonmez.v3;

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

/**
 * Classe gérant le déplacement de l'agent. Cf. la documentation de
 * {@link AiMoveHandler} pour plus de détails.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class MoveHandler extends AiMoveHandler<Agent> {

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
	 * represente les cases secure
	 */
	public AiTile secureDest = null;

	/** min preference */
	public static int LAST_MIN_PREF;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		// verbose = true;

		// dans cette classe, on aura généralement besoin d'un objet de type
		// Astar.
		// à titre d'exemple, on construit ici un objet Astar très simple (pas
		// forcément très efficace)
		// pour des raisons de rapidité, il est recommandé de créer l'objet
		// Astar une seule fois,
		// et non pas à chaque itération. Cela permet aussi d'éviter certains
		// problèmes de mémoire.

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
			CostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
			costCalculator.setOpponentCost(1000000000);
			costCalculator.setMalusCost(1000000000);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
					ai, ownHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
					this.ai, SearchMode.MODE_NOTREE);
			astar3 = new Astar(ai, ownHero, costCalculator,
					heuristicCalculator, successorCalculator);
		}

	}

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();
		AiTile currentDestination = null;
		AiTile lastDestination = null;

		// ici, à titre d'exemple, on se contente de prendre la case dont la
		// préférence est maximale
		// c'est une approche simpliste, ce n'est pas forcément la meilleure
		// (sûrement pas, d'ailleurs)
		// c'est seulement pour montrer un exemple en termes de programmation
		// (et non pas de conception d'agent)
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
			print("LASTDESTINATION " + lastDestination);
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
		Enemy enmy = new Enemy(ai);
		AiHero enemy = enmy.selectEnemy();
		control = false;

		if (ai.getReachableTiles(ai.getZone().getOwnHero().getTile()).contains(
				endTile)) {
			try {
				print("BOMBER FLAGGGG " + ai.bombHandler.bombeTile);
				if (ai.bombHandler.bombeTile == null) {
					print("kaçamaya gerek yok" + firstLocation + endTile);
					currentPath = astar1.startProcess(firstLocation, endTile);
				} else if (ai.bombHandler.bombeTile.getBombs().isEmpty()) {
					print("kaçamaya gerek yok");
					currentPath = astar1.startProcess(firstLocation, endTile);
				} else if (!ai.bombHandler.bombeTile.getBombs().isEmpty()
						&& enemy.getBombNumberMax() != 0) {
					print("KAÇÇÇÇÇÇÇÇÇÇÇÇÇÇÇ");
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
							print("ENGEL VAR " + blockTile);
							blockTile = previousTile;
						}
						previousTile = tile;
						if (!tile.getBombs().isEmpty()) {
							AiTile secureTile = ai.getSecureTile();
							return astar1.startProcess(firstLocation,
									secureTile);
						}

						if (blockTile != null && blockTile.getBombs().isEmpty()) {
							if (blockTile.equals(ownHero.getTile())) {
								print("Bomba bırakıyorum");
								control = true;
							}
						} else if (blockTile != null
								&& !blockTile.getBombs().isEmpty()) {
							print("BOMBA BIRAKTIM GÜVENLİ BİR YER");
							AiTile secureTile = ai.getSecureTile();
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
		print("movehandlerdayım");

		if (path == null || path.getLength() < 2) // cas où le chemin est vide,
		// ou bien ne contient que
		// la case courante
			result = Direction.NONE;
		else {
			AiLocation nextLocation = path.getLocation(1);
			AiTile nextTile = nextLocation.getTile();
			long wait = path.getFirstPause();
			if (wait > 0)
				result = Direction.NONE;
			else {
				AiTile currentTile = ownHero.getTile();
				List<AiTile> dangerousTiles = ai.getCurrentDangerousTiles();
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
