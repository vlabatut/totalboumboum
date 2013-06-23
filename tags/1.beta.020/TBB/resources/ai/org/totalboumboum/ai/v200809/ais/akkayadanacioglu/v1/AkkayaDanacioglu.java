package org.totalboumboum.ai.v200809.ais.akkayadanacioglu.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Huseyin Akkaya
 * @author Hayko Danacioglu
 *
 */
public class AkkayaDanacioglu extends ArtificialIntelligence {
	private AiTile currentTile = null;
	private AiTile nextTile = null;
	private AiTile previousTile = null;

	public AiAction processAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);
		if (ownHero != null) {
			currentTile = ownHero.getTile();

			if (nextTile == null)
				init();

			if (currentTile == nextTile)
				pickNextTile();
			else if (previousTile != currentTile) {
				previousTile = currentTile;
				pickNextTile();
			} else {
				checkNextTile();
			}

			int i = -1;
			while ((!isClear(nextTile) || isInDanger(nextTile)) && ++i < 4) {
				checkInterruption(); // APPEL OBLIGATOIRE
				pickNextTile();
			}

			Direction direction = getPercepts().getDirection(currentTile,
					nextTile);

			if (direction != Direction.NONE) {
				Collection<AiHero> heroes = zone.getHeroes();
				boolean foundEnemy = false;
				Iterator<AiHero> it = heroes.iterator();
				while (it.hasNext() && !foundEnemy) {
					checkInterruption(); // APPEL OBLIGATOIRE
					AiHero hero = it.next();
					if (hero.getTile().equals(nextTile)) {
						result = new AiAction(AiActionName.DROP_BOMB);
						foundEnemy = true;
					}
				}

				if (!foundEnemy) {
					result = new AiAction(AiActionName.MOVE, direction);
				}
			}

		}
		return result;
	}

	private void init() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		nextTile = currentTile;
		previousTile = currentTile;
	}

	private void pickNextTile() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		List<AiTile> tiles = getClearNeighbors(currentTile);
		boolean canGoBack = false;
		if (tiles.contains(previousTile)) {
			tiles.remove(previousTile);
			canGoBack = true;
		}
		if (tiles.size() > 0) {
			AiTile tempTile = getPercepts().getNeighborTile(currentTile,
					Direction.NONE);
			Direction dir = getPercepts().getDirection(previousTile,
					currentTile);
			if (dir != Direction.NONE) {
				tempTile = getPercepts().getNeighborTile(currentTile, dir);
				if (tiles.contains(tempTile))
					tiles.remove(tempTile);
			}
			if (tiles.size() > 0) {
				dir = getPercepts().getDirection(currentTile,
						selectTargetTile());
				dir = getNewDir(dir);
				nextTile = getPercepts().getNeighborTile(currentTile, dir);
				if (!isClear(nextTile) || isInDanger(nextTile)) {
					List<AiTile> nTiles = getClearNeighbors(currentTile);
					if (nTiles.size() == 0) {
						nextTile = currentTile;
					} else {
						for (AiTile tile : nTiles) {
							if (isClear(tile) && !isInDanger(tile)) {
								nextTile = tile;
								break;
							}
						}
					}
				}
				previousTile = currentTile;
			} else {
				nextTile = tempTile;
				previousTile = currentTile;
			}
		} else {
			if (canGoBack) {
				nextTile = previousTile;
				previousTile = currentTile;
			}
		}
	}

	private List<AiTile> getClearNeighbors(AiTile tile)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(tile);
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while (it.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE

			AiTile t = it.next();
			if (isClear(t))
				result.add(t);
		}
		result.add(tile);
		return result;
	}

	private boolean isClear(AiTile tile) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = block == null && bombs.size() == 0 && fires.size() == 0;
		return result;
	}

	private boolean isInDanger(AiTile tile) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		boolean result = false;
		int currentCol = tile.getCol();
		int currentLine = tile.getLine();
		AiZone zone = getPercepts();

		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while (it.hasNext() && !result) {
			checkInterruption(); // APPEL OBLIGATOIRE
			AiBomb bomb = it.next();
			int boundryColMin = bomb.getCol() - bomb.getRange();
			int boundryColMax = bomb.getCol() + bomb.getRange();
			int boundryLineMin = bomb.getLine() - bomb.getRange();
			int boundryLineMax = bomb.getLine() + bomb.getRange();

			if (((currentCol <= boundryColMax && currentCol >= boundryColMin && currentLine == bomb
					.getLine()) || (currentLine <= boundryLineMax
					&& currentLine >= boundryLineMin && currentCol == bomb
					.getCol()))
					&& !isThereBlockBetween(bomb.getTile(), tile, zone)) {
				result = true;
			}
		}

		return result;
	}

	private boolean isThereBlockBetween(AiTile tile1, AiTile tile2, AiZone zone)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		int col1 = tile1.getCol();
		int line1 = tile1.getLine();
		int col2 = tile2.getCol();
		int line2 = tile2.getLine();

		if (line1 == line2) {
			if (col1 < col2) {
				for (int i = col2 - 1; i >= col1 + 1; i--) {
					checkInterruption(); // APPEL OBLIGATOIRE
					if (zone.getTile(line1, i).getBlock() != null) {
						return true;
					}
				}
			} else {
				for (int i = col2 + 1; i <= col1 - 1; i++) {
					checkInterruption(); // APPEL OBLIGATOIRE
					if (zone.getTile(line1, i).getBlock() != null) {
						return true;
					}
				}
			}
		} else if (col1 == col2) {
			if (line1 < line2) {
				for (int i = line2 - 1; i >= line1 + 1; i--) {
					checkInterruption(); // APPEL OBLIGATOIRE
					if (zone.getTile(i, col1).getBlock() != null) {
						return true;
					}
				}
			} else {
				for (int i = line2 + 1; i <= line1 - 1; i++) {
					checkInterruption(); // APPEL OBLIGATOIRE
					if (zone.getTile(i, col1).getBlock() != null) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private void checkNextTile() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		if (!isClear(nextTile)) {
			List<AiTile> tiles = getClearNeighbors(currentTile);

			if (tiles.contains(nextTile))
				tiles.remove(nextTile);
			if (tiles.size() > 0) {
				double p = Math.random() * tiles.size();
				int index = (int) p;
				nextTile = tiles.get(index);
				previousTile = currentTile;
			}
		}
	}

	private AiTile selectTargetTile() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		AiZone zone = getPercepts();
		Collection<AiHero> heroes = zone.getHeroes();
		AiHero ownHero = zone.getOwnHero();
		AiHero selectedHero = null;
		int min = -1;
		Iterator<AiHero> it = heroes.iterator();
		while (it.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			AiHero hero = it.next();
			if (hero.equals(ownHero)) {
				continue;
			} else if (Math.abs(hero.getCol() - ownHero.getCol())
					+ Math.abs(hero.getLine() - ownHero.getLine()) < min
					|| min == -1) {
				selectedHero = hero;
				min = Math.abs(hero.getCol() - ownHero.getCol())
				+ Math.abs(hero.getLine() - ownHero.getLine());
			}
		}

		if (selectedHero != null) {
			return selectedHero.getTile();
		} else {
			return ownHero.getTile();
		}
	}

	private Direction getNewDir(Direction dir) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (dir.equals(Direction.DOWNRIGHT)) {
			AiTile n = getPercepts().getNeighborTile(currentTile,
					Direction.RIGHT);
			if ((isClear(n) || !isInDanger(n)) && n.getBlock() == null) {
				dir = Direction.RIGHT;
			} else {
				dir = Direction.DOWN;
			}
		} else if (dir.equals(Direction.DOWNLEFT)) {
			AiTile n = getPercepts().getNeighborTile(currentTile,
					Direction.LEFT);
			if ((isClear(n) || !isInDanger(n)) && n.getBlock() == null) {
				dir = Direction.LEFT;
			} else {
				dir = Direction.DOWN;
			}
		} else if (dir.equals(Direction.UPLEFT)) {
			AiTile n = getPercepts().getNeighborTile(currentTile,
					Direction.LEFT);
			if ((isClear(n) || !isInDanger(n)) && n.getBlock() == null) {
				dir = Direction.LEFT;
			} else {
				dir = Direction.UP;
			}
		} else if (dir.equals(Direction.UPRIGHT)) {
			AiTile n = getPercepts().getNeighborTile(currentTile,
					Direction.RIGHT);
			if ((isClear(n) || !isInDanger(n)) && n.getBlock() == null) {
				dir = Direction.RIGHT;
			} else {
				dir = Direction.UP;
			}
		}

		return dir;
	}
}