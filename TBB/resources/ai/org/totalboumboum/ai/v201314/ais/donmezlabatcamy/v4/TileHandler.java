/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant les types de cases nécessaires pour l'agent.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
@SuppressWarnings("deprecation")
public class TileHandler extends AiAbstractHandler<Agent> {

	/** */
	private AiHero ownHero;
	/** */
	private Astar astar = null;

	/**
	 * @param ai
	 *            agent concerné
	 */
	public TileHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		ownHero = ai.getZone().getOwnHero();

		AiLocation location = new AiLocation(ownHero);
		CostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai, ownHero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai, SearchMode.MODE_NOTREE);
		heuristicCalculator.processHeuristic(location);
		astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator, successorCalculator);
	}

	/**
	 * Calcul les cases en dangers après un dépôt de bombe.
	 * 
	 * @param tile
	 *            case concernée
	 * @param range
	 *            portée donnée
	 * @return getDangerousTilesWhileBombExist La liste des cases en danger
	 */
	public ArrayList<AiTile> getDangerousTilesWhileBombExist(AiTile tile, int range) {
		ai.checkInterruption();

		ArrayList<AiTile> getDangerousTilesWhileBombExist = new ArrayList<AiTile>();

		AiHero ownHero = ai.getZone().getOwnHero();

		if ( tile.isCrossableBy(ownHero) && (range > 0) ) {
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
	 * Calcule l'existence de case hors de danger.
	 * 
	 * @param hero
	 *            l'agent concernée
	 * @return true si il existe au moins une case hors de danger false sinon
	 */
	protected boolean isThereAnySafeTile(AiHero hero) {
		ai.checkInterruption();

		int counter = ai.selectTiles.size();

		for (AiTile currentTile : ai.getSafeTilesWithoutEnnemyBlast) {
			ai.checkInterruption();

			int distance = ai.getCG().nonCyclicTileDistance(currentTile, hero.getTile());

			double limit = hero.getWalkingSpeed() * (ai.getCG().bombExplosionTime(hero.getTile()) / 1000);

			if ( ai.getDangerousTilesWhileBombExist.contains(currentTile) || ai.getDangerousTiles.contains(currentTile)
					|| (distance * AiTile.getSize()) >= limit || !ai.getCG().concurrencePossibility(currentTile) ) {
				counter--;
			}
		}
		if ( counter > 0 )
			return true;

		return false;
	}

	/**
	 * Vérifie si on a au moins une case hors de danger accessible avant l'ennemi.
	 * 
	 * @param givenHero
	 *            l'agent concernée
	 * @return true si il existe une case sûre false sinon
	 */
	public boolean IsThereAnyReachableSafeTileBeforeEnnemy(AiHero givenHero) {
		ai.checkInterruption();

		ArrayList<AiTile> getDangerousTilesWhileBombExist = ai.getDangerousTilesWhileBombExist;

		AiModeHandler<Agent> modeHandler = ai.getModeHandler();

		AiMode mode = modeHandler.getMode();

		AiLocation location = new AiLocation(givenHero);

		AiPath path = new AiPath();

		double explosionTime = ai.getCG().bombExplosionTime(givenHero.getTile());

		for (AiTile tile : ai.getSafeTilesWithoutEnnemyBlast) {
			ai.checkInterruption();

			if ( !ai.getDangerousTiles.contains(tile) && !(getDangerousTilesWhileBombExist.contains(tile)) ) {
				try {
					path = astar.startProcess(location, tile);
				} catch (LimitReachedException e) {
					return false;
				}
				if ( path != null ) {

					int distance = astar.getTreeHeight();

					long x = ai.getCG().getTotalPauseTime(path.getPauses());

					double limit = givenHero.getWalkingSpeed() * (explosionTime / 1000 - x);

					if ( (path.getFirstLocation() != null) ) {
						if ( mode == AiMode.COLLECTING ) {
							if ( (distance * AiTile.getSize()) < limit )
								return true;
						} else {
							if ( ai.ennemyAccesibility ) {
								if ( (distance * AiTile.getSize()) < limit && ai.getCG().concurrencePossibility(tile) )
									return true;
							} else {
								if ( (distance * AiTile.getSize()) < limit )
									return true;
							}

						}

					}
				}
			}
		}

		return false;
	}

	/**
	 * Calcule l'accessibilité de la case passée en paramètre.
	 * 
	 * @param tile
	 *            case concernée
	 * @return true si la case est accessible false sinon
	 */
	public boolean tileIsReachable(AiTile tile) {
		ai.checkInterruption();

		ArrayList<AiTile> getDangerousTilesWhileBombExist = ai.getDangerousTilesWhileBombExist;

		AiModeHandler<Agent> modeHandler = ai.getModeHandler();

		AiMode mode = modeHandler.getMode();

		AiLocation location = new AiLocation(ownHero);

		AiPath path = new AiPath();

		double explosionTime = ai.getCG().bombExplosionTime(ownHero.getTile());

		if ( !ai.getDangerousTiles.contains(tile) && !(getDangerousTilesWhileBombExist.contains(tile)) ) {
			try {
				path = astar.startProcess(location, tile);
			} catch (LimitReachedException e) {
				return false;
			}
			if ( path != null ) {

				int distance = astar.getTreeHeight();

				long x = ai.getCG().getTotalPauseTime(path.getPauses());

				double limit = ownHero.getWalkingSpeed() * (explosionTime / 1000 - x);

				if ( (path.getFirstLocation() != null) ) {
					if ( mode == AiMode.COLLECTING ) {
						if ( (distance * AiTile.getSize()) < limit )
							return true;
					} else {
						if ( (distance * AiTile.getSize()) < limit )
							return true;

					}

				}
			}
		}

		return false;
	}

	/**
	 * Calcule la case de deuxième meilleur valeur de préférence.
	 * 
	 * @return tiles La liste des cases ayant la seconde meilleur valeur de préférence
	 */
	public List<AiTile> secondMinPrefTilesList() {
		ai.checkInterruption();

		AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();

		Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();

		TreeSet<Integer> values = new TreeSet<Integer>(preferences.keySet());

		values.remove(values.first());

		List<AiTile> tiles = preferences.get(values.first());

		return tiles;
	}

	/**
	 * Calcule une liste de case contenant des ennemis.
	 * 
	 * @return ennemyTiles liste des cases contenant des ennemis
	 */
	public ArrayList<AiTile> ennemyTiles() {
		ai.checkInterruption();

		ArrayList<AiTile> ennemyTiles = new ArrayList<AiTile>();

		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();

			if ( ai.selectTiles.contains(ennemy.getTile()) )
				ennemyTiles.add(ennemy.getTile());
		}
		return ennemyTiles;
	}

	/**
	 * Calcule une liste de case contenant des bombes.
	 * 
	 * @return bombTiles liste des cases contenant des bombes
	 */
	public ArrayList<AiTile> bombTiles() {
		ai.checkInterruption();

		ArrayList<AiTile> bombTiles = new ArrayList<AiTile>();

		for (AiBomb bomb : ai.getZone().getBombs()) {
			ai.checkInterruption();
			bombTiles.add(bomb.getTile());
		}
		return bombTiles;
	}

	/**
	 * Calcule une liste des cases contenant les bombes de l'agent.
	 * 
	 * @return bombTileList liste des cases contenant les bombes
	 */
	public ArrayList<AiTile> myBombTileList() {
		ai.checkInterruption();

		ArrayList<AiTile> bombTileList = new ArrayList<AiTile>();

		for (AiBomb bomb : ai.getZone().getBombsByColor(ai.getZone().getOwnHero().getColor())) {
			ai.checkInterruption();

			bombTileList.add(bomb.getTile());
		}
		return bombTileList;

	}

	/**
	 * Calcule la case hors de danger la plus proche de notre agent parmis toute les cases de la zone de jeu.
	 * 
	 * @return result la case hors de danger la plus proche
	 */
	public AiTile getNearestSafeTiles() {
		ai.checkInterruption();

		int tmpDistance = 100;

		AiTile result = null;

		for (AiTile aiTile : ai.getSafeTiles) {
			ai.checkInterruption();

			int myDistance = ai.getCG().nonCyclicTileDistance(ai.getZone().getOwnHero().getTile(), aiTile);

			if ( tmpDistance > myDistance ) {
				tmpDistance = myDistance;
				result = aiTile;

			}
		}
		return result;
	}

	/**
	 * Calcule la case hors de danger la plus proche de notre agent parmis ses cases voisines.
	 * 
	 * @return result la case hors de danger la plus proche
	 */
	public AiTile getNearestSafeTiles2() {
		ai.checkInterruption();

		int tmpDistance = 100;

		AiTile result = null;

		for (AiTile aiTile : ai.getSecuretiles) {
			ai.checkInterruption();

			int myDistance = ai.getCG().nonCyclicTileDistance(ai.getZone().getOwnHero().getTile(), aiTile);

			if ( tmpDistance > myDistance ) {
				tmpDistance = myDistance;
				result = aiTile;
			}
		}
		return result;
	}

	/**
	 * Calcule les cases se trouvant en sécurité parmis les cases voisines de la case de l'agent.
	 * 
	 * @return secureTiles liste des cases en sécurité
	 */
	public ArrayList<AiTile> getSecuretiles() {
		ai.checkInterruption();

		ArrayList<AiTile> secureTiles = new ArrayList<AiTile>();

		for (Direction direction : Direction.getPrimaryValues()) {
			ai.checkInterruption();

			if ( ownHero.getTile().getNeighbor(direction).getBlocks().isEmpty()
					&& ownHero.getTile().getNeighbor(direction).getBombs().isEmpty()
					&& ai.selectTiles.contains(ownHero.getTile().getNeighbor(direction)) )
				secureTiles.add(ownHero.getTile().getNeighbor(direction));

			for (Direction direction2 : Direction.getPrimaryValues()) {
				ai.checkInterruption();

				if ( ownHero.getTile().getNeighbor(direction).getNeighbor(direction2).getBlocks().isEmpty()
						&& ownHero.getTile().getNeighbor(direction).getNeighbor(direction2).getBombs().isEmpty()
						&& ai.selectTiles.contains(ownHero.getTile().getNeighbor(direction).getNeighbor(direction2)) )
					secureTiles.add(ownHero.getTile().getNeighbor(direction).getNeighbor(direction2));
			}
		}

		secureTiles.add(ownHero.getTile());

		secureTiles.removeAll(ai.getDangerousTiles);

		return secureTiles;
	}

	/**
	 * Calcule les cases hors de danger parmis toutes les cases de la zone de jeu.
	 * 
	 * @return safeTiles La liste des cases hors de danger
	 */
	public Set<AiTile> getSafeTiles() {
		ai.checkInterruption();

		Set<AiTile> safeTiles = ai.accesibleTiles;

		safeTiles.removeAll(ai.getDangerousTiles);

		return safeTiles;
	}

	/**
	 * Calcule les cases hors de danger et ne contenant pas d'adversaires.
	 * 
	 * @return getSafeTilesWithoutEnnemyBlast liste des cases hors de danger et sans ennemis
	 */
	public Set<AiTile> getSafeTilesWithoutEnnemyBlast() {
		ai.checkInterruption();

		Set<AiTile> getSafeTilesWithoutEnnemyBlast = ai.getSafeTiles;

		getSafeTilesWithoutEnnemyBlast.removeAll(ai.dangerZoneForEnnemyBomb);

		return getSafeTilesWithoutEnnemyBlast;
	}

	/**
	 * Calcule la liste des cases accessibles; les cases surlesquelles il n'y a pas
     * d'explosion, d'ennemi ou de murs et la zone sera délimitée avec ces obstacles
	 * 
	 * @return selectedTiles La liste des cases accessibles
	 */
	public Set<AiTile> getAccesibleTiles() {
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

				if ( tmpTile.getNeighbor(direction).getBlocks().isEmpty() && tmpTile.getNeighbor(direction).getBombs().isEmpty()
						&& tmpTile.getNeighbor(direction).getHeroes().isEmpty() && !queueTile.contains(tmpTile.getNeighbor(direction))
						&& !selectedTiles.contains(tmpTile.getNeighbor(direction)) && !tmpTile.getNeighbor(direction).equals(myTile)
						&& tmpTile.getNeighbor(direction).getFires().isEmpty() ) {
					queueTile.add(tmpTile.getNeighbor(direction));
				}
			}
			if ( !queueTile.isEmpty() ) {
				tmpTile = queueTile.peek();
				selectedTiles.add(tmpTile);
			} else {
				break;
			}
			selectedTiles.add(myTile);
			for (AiHero heroes : ai.getZone().getHeroes()) {
				ai.checkInterruption();

				if ( selectedTiles.contains(heroes.getTile()) ) {
					for (AiBomb selectedBombs : ai.getZone().getBombsByColor((heroes.getColor()))) {
						ai.checkInterruption();

						selectedTiles.add(selectedBombs.getTile());

					}
				}

			}
		}
		return selectedTiles;
	}

	/**
	 * Calcule les cases qui sont en danger; celle qui contiennent une bombe,
     * celle qui sont dans la portée d'une bombe, ou qui contiennent une explosion.
	 * 
	 * @return dangerTiles Listes des cases en danger
	 */
	public ArrayList<AiTile> getDangerousTiles() {
		ai.checkInterruption();

		ArrayList<AiTile> dangerTiles = new ArrayList<AiTile>();

		for (AiBomb bomb : ai.getZone().getBombs()) {
			ai.checkInterruption();

			dangerTiles.add(bomb.getTile());

			for (AiTile tileBombBlast : bomb.getBlast()) {
				ai.checkInterruption();

				dangerTiles.add(tileBombBlast);
			}
		}
		for (AiFire fires : ai.getZone().getFires()) {
			ai.checkInterruption();

			dangerTiles.add(fires.getTile());
		}

		return dangerTiles;

	}

	/**
	 * Calcule une liste des cases situées dans la portée des bombes posées par les adversaires.
	 * 
	 * @return getDangerousTiles La liste des cases d'explosion des bombes des ennemis
	 */
	public ArrayList<AiTile> dangerZoneForEnnemyBombs() {
		ai.checkInterruption();

		ArrayList<AiTile> getDangerousTiles = ai.getDangerousTiles;

		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();

			AiBomb bomb = ennemy.getBombPrototype();

			List<AiTile> ennemyBlast = bomb.getBlast();

			getDangerousTiles.addAll(ennemyBlast);
		}

		return getDangerousTiles;
	}

	/**
	 * Calcule les cases d'explosions de la bombe de l'agent.
	 * 
	 * @return dangerZoneForBomb La liste des cases d'explosion de la bombe de l'agent
	 */
	public ArrayList<AiTile> dangerZoneForBomb() {
		ai.checkInterruption();

		ArrayList<AiTile> dangerZoneForBomb = new ArrayList<AiTile>();

		AiBomb bomb = ai.getZone().getOwnHero().getBombPrototype();

		List<AiTile> ownBlast = bomb.getBlast();

		dangerZoneForBomb.addAll(ownBlast);

		return dangerZoneForBomb;
	}

	/**
	 * Vérifie si toute les cases au voisinage de la case du héro concernée sont en danger.
	 * 
	 * @param hero
	 *            agent concernée
	 * @return true si les cases au voisinage sont en danger et si l'agent n'est pas sur une case de danger false sinon
	 */
	public boolean allNeighborsInDangerExceptHeroTile(AiHero hero) {
		ai.checkInterruption();

		int counter = 4;

		for (Direction direction : Direction.getPrimaryValues()) {
			ai.checkInterruption();

			if ( ai.getDangerousTiles.contains(hero.getTile().getNeighbor(direction))
					|| !hero.getTile().getNeighbor(direction).getBlocks().isEmpty() )
				counter--;
		}
		if ( counter == 0 && !ai.getDangerousTiles.contains(hero.getTile()) )
			return true;

		return false;
	}

	/**
	 * Vérifie si la direction suivie par l'agent est une case en danger.
	 * 
	 * @param hero
	 *            agent concerné
	 * @return true si la direction est une case dangereuse false sinon
	 */
	public boolean dangerInDirection(AiHero hero) {
		ai.checkInterruption();

		if ( ai.getDangerousTiles.contains(hero.getTile().getNeighbor(ai.moveHandler.getCurrentDirection()))
				&& !ai.getDangerousTiles.contains(hero.getTile()) )
			return true;

		return false;
	}

	/**
	 * Vérifie si la case passée en paramètres est une case se trouvant dans un corridor.
	 * 
	 * @param tile
	 *            case concernée
	 * @return true si la case est dans un corridor false sinon
	 */
	public boolean isTileInCorridor(AiTile tile) {
		ai.checkInterruption();
		int counter = 0;
		for (Direction direction : Direction.getPrimaryValues()) {
			ai.checkInterruption();
			if ( tile.getNeighbor(direction).getBlocks().isEmpty() ) {
				counter++;
			}
		}
		if ( counter == 2 || counter == 1 ) {
			// to be continued....
		}

		return false;
	}

	/**
	 * Calcule la case la plus proche entourée de trois murs.
	 * 
	 * @return tile la case la plus proche entourée de trois murs
	 */
	public AiTile getTilesWhichSurroundByThreeWall() {
		ai.checkInterruption();

		int distance = 0;

		AiTile tile = null;

		ArrayList<AiTile> tiles = new ArrayList<AiTile>();
		for (AiTile tilee : ai.selectTiles) {
			ai.checkInterruption();

			if ( ai.getCG().zoneWall(tilee) == 2 ) {
				tiles.add(tilee);
			}
		}

		int tmpDistance = 0;

		if ( !tiles.isEmpty() ) {
			for (int i = 0; i < tiles.size(); i++) {
				ai.checkInterruption();

				tmpDistance = ai.getCG().nonCyclicTileDistance(tiles.get(i), ownHero.getTile());

				if ( distance < tmpDistance ) {
					distance = tmpDistance;
					tile = tiles.get(i);
				}
			}
		}

		return tile;
	}

	/**
	 * Vérifie si l'agent n'est pas bloqué entre des murs et un adversaire.
	 * 
	 * @param tile
	 *            case concernée
	 * @return true si la l'adversaire sera bloqué entre des murs et un adversaire false sinon
	 */
	public boolean dontStayBetweenWallAndEnnemyControl(AiTile tile) {
		ai.checkInterruption();

		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();

			if ( ai.getCG().zoneWall(tile) == 1 && ai.getTilesWhichSurroundByThreeWall != null ) {
				if ( ai.getZone().getTileDistance(ai.getTilesWhichSurroundByThreeWall, tile) >= ai.getZone().getTileDistance(
						ai.getTilesWhichSurroundByThreeWall, ennemy.getTile()) )
					return false;

			} else if ( ai.getCG().zoneWall(tile) == 2 ) {
				if ( tile == ennemy.getTile() && tile != ai.getZone().getOwnHero().getTile() )
					return true;
				else
					return false;
			}
		}
		return true;
	}
}
