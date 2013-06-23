/**
 * Cette classe est responsable de calculer tout sur les cases (trouver les voisins, les distances etc.)
 */
package org.totalboumboum.ai.v200809.ais.akpolatsener.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.ai.v200809.ais.akpolatsener.v2.comparators.ComparatorByHypotenuse;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Cem Akpolat
 * @author Emre Sener
 *
 */
public class TileControl {
	/** la classe principale de notre IA */
	AkpolatSener as;

	public TileControl(AkpolatSener as) throws StopRequestException {
		as.checkInterruption();
		this.as = as;
	}

	/**
	 * renvoie la liste des tous les voisins
	 * 
	 * @return liste des voisins sans condition
	 * @throws StopRequestException
	 */
	public List<AiTile> findAllNeighbors(AiTile tile)
			throws StopRequestException {
		as.checkInterruption();

		Iterator<AiTile> iter = as.zone.getNeighborTiles(tile).iterator();

		List<AiTile> tiles = new ArrayList<AiTile>();

		while (iter.hasNext()) {
			as.checkInterruption();

			tiles.add(iter.next());
		}
		return tiles;
	}

	/**
	 * filtre la liste des tous voisins d'apres les parametres
	 * 
	 * @param bombDanger
	 *            true quand on veut filtrer les voisins qui est sous danger
	 * @param bomb
	 *            true quand on veut filtrer les voisins qui a des bombes
	 * @param fire
	 *            true quand on veut filtrer les voisins qui a des feux
	 * @param block
	 *            true quand on veut filtrer les voisins qui a de bloc
	 * @param enemy
	 *            true quand on veut filtrer les voisins qui a des enemies
	 * @param softWall
	 *            true quand on veut filtrer les voisins qui a de bloc
	 *            destructible
	 * @param bonus
	 *            true quand on veut filtrer les voisins qui a des bonuses
	 * @return liste des voisins filtré
	 * @throws StopRequestException
	 */
	public List<AiTile> filterNeighbors(AiTile tile, boolean bombDanger,
			boolean bomb, boolean fire, boolean block, boolean enemy,
			boolean softwall, boolean bonus) throws StopRequestException {
		as.checkInterruption();

		List<AiTile> tiles = findAllNeighbors(tile);
		List<AiTile> filteredTiles = new ArrayList<AiTile>();

		for (AiTile tempTile : tiles) {
			as.checkInterruption();

			if (!checkTile(tempTile, bombDanger, bomb, fire, block, enemy,
					softwall, bonus)) {
				filteredTiles.add(tempTile);
			}
		}

		return filteredTiles;
	}

	/**
	 * controler une case
	 * 
	 * @param bombDanger
	 *            true quand on veut le controler s'il existe
	 * @param bomb
	 *            true quand on veut le controler s'il existe
	 * @param fire
	 *            true quand on veut le controler s'il existe
	 * @param block
	 *            true quand on veut le controler s'il existe
	 * @param enemy
	 *            true quand on veut le controler s'il existe
	 * @param softWall
	 *            true quand on veut le controler s'il existe
	 * @param bonus
	 *            true quand on veut le controler s'il existe
	 * @throws StopRequestException
	 */
	public boolean checkTile(AiTile tile, boolean bombDanger, boolean bomb,
			boolean fire, boolean block, boolean enemy, boolean softwall,
			boolean bonus) throws StopRequestException {
		as.checkInterruption();

		boolean result = false;

		if (bombDanger)
			result |= isInBombRange(tile);

		if (bomb)
			result |= !tile.getBombs().isEmpty();
		if (fire)
			result |= !tile.getFires().isEmpty();
		if (block)
			result |= (tile.getBlock() != null && !tile.getBlock()
					.isDestructible());
		if (enemy)
			result |= (tile.getHeroes().size() - 1 > 0);
		if (softwall)
			result |= (tile.getBlock() != null && tile.getBlock()
					.isDestructible());
		if (bonus)
			result |= tile.getItem() != null;

		return result;
	}

	/**
	 * le nombre des voisins de la case donnée
	 * 
	 * @param tile
	 * @return le nombre des voisins de la case donnée
	 * @throws StopRequestException
	 */
	public int getNeighborsCount(AiTile tile) throws StopRequestException {
		as.checkInterruption();

		List<AiTile> tiles = this.filterNeighbors(tile, false, false,
				false, false, false, false, false);

		return tiles.size();
	}

	/**
	 * trier les cases d'apres la parametre
	 * 
	 * @param tiles
	 * @param targetOrDanger
	 *            true si c'est pour la cible, false sinon.
	 * @return la liste des cases triés
	 * @throws StopRequestException
	 */
	List<AiTile> sortTiles(List<AiTile> tiles, boolean targetOrDanger)
			throws StopRequestException {
		as.checkInterruption();

		ComparatorByHypotenuse comp = new ComparatorByHypotenuse();

		as.danger = new Danger(as);
		if (targetOrDanger)
			comp.addTarget(as, as.target);
		else
			comp.addDanger(as, as.danger);

		PriorityQueue<AiTile> sortedTiles = new PriorityQueue<AiTile>(1, comp);

		for (AiTile tile : tiles)
			sortedTiles.offer(tile);

		tiles.clear();

		while (sortedTiles.size() > 0)
			tiles.add(sortedTiles.poll());

		return tiles;

	}

	/**
	 * donne la distance directe entre deux cases
	 * 
	 * @param origin
	 * @param target
	 * @return distance entre ces 2 cases
	 * @throws StopRequestException
	 */
	public double getHypotenuseTo(AiTile origin, AiTile target)
			throws StopRequestException {
		as.checkInterruption();

		int X = origin.getCol() - target.getCol();
		int Y = origin.getLine() - target.getLine();
		double distance = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));

		return distance;
	}

	/**
	 * renvoie le nombre de Manhattan entre deux cases..
	 * 
	 * @param origin
	 * @param target
	 * @return Manhattan distance
	 * @throws StopRequestException
	 */
	public int getManhattanTo(AiTile origin, AiTile target)
			throws StopRequestException {
		as.checkInterruption();

		// verticale
		int col = target.getCol() - origin.getCol();

		// horizontale
		int line = target.getLine() - origin.getLine();

		return Math.abs(col + line);
	}

	/**
	 * determine si une case est dans la portée d'une bombe
	 * 
	 * @param tile
	 * @param bombRange
	 * @return true si elle est, false sinon
	 * @throws StopRequestException
	 */
	public boolean isInBombRange(AiTile tile) throws StopRequestException {
		as.checkInterruption();

		boolean result = false;
		// on teste la risque d'une bombe de gauche à droite de la case//
		result = result || !checkTilesInDirection(tile, Direction.LEFT);

		result = result || !checkTilesInDirection(tile, Direction.RIGHT);
		// on teste la risque d'une bombe de haut à bas de la case//
		result = result || !checkTilesInDirection(tile, Direction.UP);
		result = result || !checkTilesInDirection(tile, Direction.DOWN);

		return result;

	}

	/**
	 * controle la case dans une direction donnés
	 * 
	 * @param tile
	 * @return true s'il n'ya pas de danger dans cette direction
	 * @throws StopRequestException
	 */
	boolean checkTilesInDirection(AiTile tile, Direction dir)
			throws StopRequestException {
		as.checkInterruption();

		boolean result = true;

		while (true) {
			if (checkTile(tile, false, true, true, false, false, false, false)) {
				result = false;
				break;
			} else if (checkTile(tile, false, false, false, true, true, true,
					false)) {
				result = true;
				break;
			} else
				tile = tile.getNeighbor(dir);

		}

		return result;

	}

	/**
	 * controle s'il y a de danger derrier à un coin
	 * 
	 * @param tile
	 * @param dir
	 * @return
	 * @throws StopRequestException
	 */
	public boolean checkDangerBehindCorner(AiTile tile, Direction dir)
			throws StopRequestException {

		as.checkInterruption();

		boolean result = false;

		as.danger = new Danger(as);

		AiBomb bomb = as.danger.findClosestBomb();

		if (bomb != null) {
			int bombLine = bomb.getLine();
			int bombCol = bomb.getCol();

			if (isInBombRange(as.zone
					.getTile(bombLine, as.currentTile.getCol()))
					|| isInBombRange(as.zone.getTile(as.currentTile.getLine(),
							bombCol))) {
				result = true;

			}
		}

		return result;
	}

	/**
	 * donne la direction le mieux pour arriver à une case cible.
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public Direction prisonBreak(AiTile tile) throws StopRequestException {
		as.checkInterruption();

		Direction result = null;

		List<AiTile> tiles = filterNeighbors(tile, true, true, true,
				true, false, true, false);
		if (tiles.size() == 0)
			tiles = filterNeighbors(tile, false, true, true, true, false,
					true, false);

		for (AiTile neigh : tiles) {
			as.checkInterruption();
			Direction dir = null;
			List<AiTile> neighbors = null;
			AiTile tempTile = neigh;
			while ((neighbors = filterNeighbors(tempTile, false, true, true,
					true, false, true, false)).size() > 0) {

				as.checkInterruption();
				Iterator<AiTile> iter = neighbors.iterator();

				while (iter.hasNext()) {
					as.checkInterruption();
					AiTile temp = iter.next();
					if (temp.getCol() == tile.getCol()
							|| temp.getLine() == tile.getLine())
						iter.remove();
				}

				if (neighbors.size() > 0) {
					// neighbors = sortTiles(neighbors, false);
					tempTile = neighbors.get(0);
					result = as.zone.getDirection(tile, neigh);

					break;
				} else {
					dir = as.zone.getDirection(tile, neigh);
					tempTile = tempTile.getNeighbor(dir);
				}
			}

		}

		if (result == null) {
			result = as.zone.getDirection(tile, tiles.get(0));
		}

		return result;
	}

}
