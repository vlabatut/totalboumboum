package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class BombHandler extends AiBombHandler<Agent> {

	/** */
	private AiHero ownHero = null;

	/** */
	private AiZone zone = null;

	/** */
	private Astar astarApproximation = null;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		CostCalculator costCalculator = new ApproximateCostCalculator(ai,
				ownHero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
				ai, ownHero);
		SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(
				ai);
		astarApproximation = new Astar(ai, ownHero, costCalculator,
				heuristicCalculator, successorCalculator);

	}

	/**
	 * Calcul la posibilite de faire chaineReaction
	 * 
	 * @return true si possible sinon false
	 */
	public boolean chaineReactionPossibility() {
		ai.checkInterruption();
		int distance = 0;
		if (ownHero.getBombNumberCurrent() == 1
				&& ownHero.getBombNumberMax() >= 3) {
			for (AiBomb bomb : ai.getZone().getBombsByColor(ownHero.getColor())) {
				ai.checkInterruption();
				distance = ai.getZone().getTileDistance(ownHero.getTile(),
						bomb.getTile());
				if (bomb.getBlast().contains(ownHero.getTile())
						&& distance == 2 && ownHero.getBombNumberCurrent() < 3) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Calcul la case de deuxième meilleur valeur de préférence
	 * 
	 * @return tiles La liste des cases ayant la seconde meilleur valeur de
	 *         préférence
	 */
	public List<AiTile> secondMinPrefTilesList() {
		ai.checkInterruption();
		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();
		TreeSet<Integer> values = new TreeSet<Integer>(preferences.keySet());
		values.remove(values.first());
		List<AiTile> tiles = preferences.get(values.first());
		return tiles;
	}

	/**
	 * Calcul les cases en danger à cause du dépôt d'une de nos bombes
	 * 
	 * @return getDangerousTiles La liste des cases d'explosion de nos bombes
	 */
	public ArrayList<AiTile> dangerZoneForBomb() {
		ai.checkInterruption();
		ArrayList<AiTile> getDangerousTiles = ai.getDangerousTiles();
		AiBomb bomb = ownHero.getBombPrototype();
		List<AiTile> ownBlast = bomb.getBlast();
		getDangerousTiles.addAll(ownBlast);
		return getDangerousTiles;
	}

	/**
	 * Calcule la possibilité de tuer un ennemi en posant une bombe sur la case
	 * ou se trouve notre agent
	 * 
	 * @return true si on peut tuer un ennemi false sinon
	 * 
	 */
	public boolean ennemyInOurBlast() {
		ai.checkInterruption();
		for (AiHero hero : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			if (ownHero.getBombPrototype().getBlast().contains(hero.getTile()))
				return true;
		}
		return false;
	}

	/**
	 * Calcul les cases en dangers
	 * 
	 * @param tile
	 *            case concernée
	 * @param range
	 *            portée donnée
	 * @return getDangerousTilesWhileBombExist La liste des cases en danger
	 */
	public ArrayList<AiTile> getDangerousTilesWhileBombExist(AiTile tile,
			int range) {
		ai.checkInterruption();
		ArrayList<AiTile> getDangerousTilesWhileBombExist = new ArrayList<AiTile>();
		AiHero ownHero = ai.getZone().getOwnHero();

		if (tile.isCrossableBy(ownHero) && (range > 0)) {
			getDangerousTilesWhileBombExist.add(tile);

			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				AiTile currentTile = tile.getNeighbor(direction);
				int distance = range;
				while (currentTile.isCrossableBy(ownHero) && (distance > 0)) {
					ai.checkInterruption();
					getDangerousTilesWhileBombExist.add(currentTile);
					currentTile = currentTile.getNeighbor(direction);
					distance--;
				}
			}
		}

		return getDangerousTilesWhileBombExist;
	}

	/**
	 * Calcule le temps qui va rester pour faire une explosion en enchainement
	 * sur sur la case concernée
	 * 
	 * @param tile
	 *            case concernée
	 * @return result Le temps qui nous restera si l'enchainement est possible 0
	 *         si l'enchainement n'est pas possble
	 */
	public long chainReactionTime(AiTile tile) {
		ai.checkInterruption();
		long result = 1000000;
		Map<AiBomb, Long> bomb_time = ai.getZone().getDelaysByBombs();
		if (ai.getDangerousTiles().contains(tile)) {
			if (!bomb_time.isEmpty()) {
				for (AiBomb bomb : ai.getZone().getBombs()) {
					ai.checkInterruption();
					if (bomb.getBlast().contains(tile)) {
						long explosion_time = bomb_time.get(bomb)
								- bomb.getElapsedTime();
						if (explosion_time < result)
							result = explosion_time;
					}
				}

			}
		} else
			result = 0;

		return result;
	}

	/**
	 * Calcule le temps d'explosion de la bombe
	 * 
	 * @param tile
	 *            case concernée
	 * @return result le temps d'explosion de la bombe
	 */
	public long bombExplosionTime(AiTile tile) {
		ai.checkInterruption();
		long result = 0;

		if (ai.getDangerousTiles().contains(tile)) {
			result = chainReactionTime(tile);
		} else
			result = ai.getZone().getOwnHero().getBombDuration();

		return result;
	}

	/**
	 * Calcule l'existence de case hors de danger
	 * 
	 * @param hero
	 *            l'agent concernée
	 * @return true si il existe au moins une case hors de danger false sinon
	 */
	protected boolean isThereAnySafeTile(AiHero hero) {
		ai.checkInterruption();

		int counter = ai.preferenceHandler.selectTiles().size();
		for (AiTile currentTile : ai.getAccesibleTiles()) {
			ai.checkInterruption();
			if (getDangerousTilesWhileBombExist(hero.getTile(),
					hero.getBombRange()).contains(currentTile)
					|| ai.getDangerousTiles().contains(currentTile)) {
				counter--;
			}

		}
		if (counter > 0)
			return true;

		return false;
	}

	/**
	 * Controle si on a au moins deux cases hors de danger accessible
	 * 
	 * @param givenHero
	 *            l'agent concernée
	 * @return true si il existe deux cases à échapper false sinon
	 */
	public boolean canReachSafetyAstar(AiHero givenHero) {
		ai.checkInterruption();

		ArrayList<AiTile> dangerousTilesOnBombing = getDangerousTilesWhileBombExist(
				givenHero.getTile(), givenHero.getBombRange());

		AiLocation location = new AiLocation(givenHero);
		AiPath path = new AiPath();

		double explosion_time = bombExplosionTime(givenHero.getTile());

		for (AiTile tile : ai.getAccesibleTiles()) {
			ai.checkInterruption();
			if (!ai.getDangerousTiles().contains(tile)
					&& !(dangerousTilesOnBombing.contains(tile))) {
				try {
					path = astarApproximation.startProcess(location, tile);
				} catch (LimitReachedException e) {
					return false;
				}
				if (path != null) {
					int distance = astarApproximation.getTreeHeight();

					long x = ai.getTotalPauseTime(path.getPauses());
					double limit = givenHero.getWalkingSpeed()
							* (explosion_time / 1000 - x);

					if ((path.getFirstLocation() != null)) {
						if ((distance * AiTile.getSize()) < limit)
							return true;
					}
				}
			}
		}

		return false;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de déterminer si l'agent doit poser une bombe ou pas.
	 * 
	 * @return true si l'agent peut poser une bombe false sinon
	 */
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		AiTile myTile = ownHero.getTile();
		AiModeHandler<Agent> modeHandler = ai.getModeHandler();
		AiMode mode = modeHandler.getMode();
		boolean needAstar = false;
		if (ownHero.getBombRange() > 1) {
			needAstar = true;
		}
		if ((ownHero.getBombNumberMax() - ownHero.getBombNumberCurrent()) != 0
				&& (myTile.getBombs().isEmpty())) {
			if (mode == AiMode.COLLECTING) {
				if (!needAstar) {
					if (myTile == ai.endTile && !ai.usefulItemsAccesibility()) {
						if (isThereAnySafeTile(ownHero)) {
							return true;
						}
					}
				} else {
					if (myTile == ai.endTile && !ai.usefulItemsAccesibility()) {
						if (canReachSafetyAstar(ownHero)) {
							return true;
						}
					}
				}
			} else {
				if (!needAstar) {
					if (ai.hasEnoughEnnemyAccesible()) {
						if (ennemyInOurBlast() || chaineReactionPossibility()) {
							if (isThereAnySafeTile(ownHero)) {
								return true;
							}
						}
					}
					if (ai.accesibleDestWallExistence()) {
						if (myTile == ai.endTile) {
							if (isThereAnySafeTile(ownHero)) {
								return true;
							}
						}
					}
				} else {
					if (ai.hasEnoughEnnemyAccesible()) {
						if (ennemyInOurBlast() || chaineReactionPossibility()) {
							if (canReachSafetyAstar(ownHero)) {
								return true;
							}
						}
					}
					if (ai.accesibleDestWallExistence()) {
						if (myTile == ai.endTile) {
							if (canReachSafetyAstar(ownHero)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();

	}
}
