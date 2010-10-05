package org.totalboumboum.ai.v200910.ais.dereligeckalan.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiItemType;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;



/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui définit son comportement. n'hésitez pas à
 * décomposer le traitement en plusieurs classes, plus votre programme est
 * modulaire et plus il sera facile à débugger, modifier, relire, comprendre,
 * etc.
 */
public class DereliGeckalan extends ArtificialIntelligence {

	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */

	private int matris[][] = null;
	private AiTile nextTile;
	private AiTile currentTile, targetTile;

	private LinkedList<AiTile> path;
	Direction direction = Direction.NONE;

	public AiAction processAction() throws StopRequestException { // avant tout
		// : test
		// d'interruption

		checkInterruption();
		matris = new int[getPercepts().getHeigh()][getPercepts().getWidth()];
		AiZone zone = getPercepts();

		@SuppressWarnings("unused")
		AiHero ownHero = zone.getOwnHero();
		currentTile = zone.getOwnHero().getTile();
		@SuppressWarnings("unused")
		int line = currentTile.getLine();
		@SuppressWarnings("unused")
		int col = currentTile.getCol();

		targetTile = currentTile;
		AiAction result = new AiAction(AiActionName.NONE);
		initMatrice();
		// String s = toString();
		// System.out.println(s);
		if (dangerZone().contains(currentTile)) {
			// System.out.println("1");
			pickNextTile(findNext());
			if (danger() == false) {
				/*
				 * String s = toTileString(nextTile); String s1 =
				 * toTileString(currentTile); String s2 =
				 * toTileString(targetTile); System.out.println("next" + s1);
				 * System.out.println("current" + s);
				 * System.out.println("target" + s2);
				 */

				direction = zone.getDirection(currentTile, nextTile);
				result = new AiAction(AiActionName.MOVE, direction);
			} else {
				result = new AiAction(AiActionName.NONE);
			}

		}

		else {
			 pickNextTile(findNextbonus());
			 direction = zone.getDirection(currentTile, nextTile);
			 result = new AiAction(AiActionName.MOVE,direction);
			
		}

		// AiAction result = new AiAction(AiActionName.MOVE);
		return result;
	}

	/**
	 * une méthode bidon pour l'exemple
	 * 
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private String toTileString(AiTile tile) {
		String res = "";
		int x = tile.getLine();
		int y = tile.getCol();
		res = res + String.valueOf(x) + "," + String.valueOf(y);
		return res;
	}

	private void initMatrice() throws StopRequestException {
		// LinkedList<AiTile> danger = dangerZone();
		AiZone zone = getPercepts();

		for (int i = 0; i < zone.getHeigh(); i++) {
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++) {
				checkInterruption();
				matris[i][j] = 0;
			}
		}
		Collection<AiBlock> blocks = zone.getBlocks();
		LinkedList<AiTile> blokTile = new LinkedList<AiTile>();
		Iterator<AiBlock> it_blocks = blocks.iterator();

		while (it_blocks.hasNext()) {
			checkInterruption();
			AiBlock blok = it_blocks.next();
			AiTile tile = blok.getTile();
			blokTile.add(tile);
		}
		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		LinkedList<AiTile> safeZone = safeZone();
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> itBomb = bombs.iterator();
		while (itBomb.hasNext()) {
			AiBomb bomb = itBomb.next();
			double temp = bomb.getNormalDuration() - bomb.getTime();
			AiTile tile2 = bomb.getTile();
			int x = tile2.getCol();
			int y = tile2.getLine();
			// System.out.println("eeeee");
			matris[y][x] = (int) (bomb.getNormalDuration());

			int k = bomb.getRange();
			int i = 0;
			AiTile tile1 = tile2;

			while (i < k && !blocks.contains(tile2)) {
				checkInterruption();
				AiTile tile = tile2.getNeighbor(Direction.DOWN);
				// System.out.println("Asagi");
				tile2 = tile;
				if (!blokTile.contains(tile)) {
					// System.out.println("-1");
					dangerZone.add(tile);
					int a = tile.getLine();
					int b = tile.getCol();
					matris[a][b] = (int) (temp);
				} else
					break;
				i++;
			}
			i = 0;
			tile2 = tile1;

			while (i < k && !blocks.contains(tile2)) {
				checkInterruption();
				AiTile tile = tile2.getNeighbor(Direction.UP);
				// System.out.println("yukarý");
				tile2 = tile;
				if (!blokTile.contains(tile)) {
					// System.out.println("-2");
					dangerZone.add(tile);
					int a = tile.getLine();
					int b = tile.getCol();
					matris[a][b] = (int) (temp);
				} else
					break;
				i++;
			}
			i = 0;
			tile2 = tile1;
			while (i < k && !blocks.contains(tile2)) {
				checkInterruption();
				AiTile tile = tile2.getNeighbor(Direction.RIGHT);
				// System.out.println("sagai");
				tile2 = tile;
				if (!blokTile.contains(tile)) {
					// System.out.println("-3");
					dangerZone.add(tile);
					int a = tile.getLine();
					int b = tile.getCol();
					matris[a][b] = (int) (temp);
				} else
					break;
				i++;
			}
			i = 0;
			tile2 = tile1;
			while (i < k && !blocks.contains(tile2)) {
				checkInterruption();
				AiTile tile = tile2.getNeighbor(Direction.LEFT);
				// System.out.println("sola");
				tile2 = tile;
				if (!blokTile.contains(tile)) {
					// System.out.println("-4");
					dangerZone.add(tile);
					int a = tile.getLine();
					int b = tile.getCol();
					matris[a][b] = (int) (temp);
				} else
					break;
				i++;
			}
			i = 0;
			tile2 = tile1;
		}

		Iterator<AiTile> it2 = safeZone.iterator();
		while (it2.hasNext()) {
			AiTile temp = it2.next();
			int x = temp.getLine();
			int y = temp.getCol();
			matris[x][y] = -1;
		}

	}

	public String toString() {
		String result = "";
		for (int i = 0; i < getPercepts().getHeigh(); i++) {
			for (int j = 0; j < getPercepts().getWidth(); j++)
				result += "(" + i + "," + j + ")" + matris[i][j] + "   ";
			result += "\n";
		}

		return result;
	}

	private AiTile findNextbonus() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> escapeTiles = new LinkedList<AiTile>();
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		int min = 1000;
		AiTile escapeTileTemp;
		escapeTiles = bonusZone();

		Iterator<AiTile> itEscape = escapeTiles.iterator();

		while (itEscape.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			escapeTileTemp = itEscape.next();

			PathFinder pathFind = new PathFinder(this.getPercepts(),
					escapeTileTemp, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			if ((min > temp.size() && min > 0 && !temp.isEmpty())
					|| escapeTileTemp.equals(getPercepts().getOwnHero()
							.getTile())) {
				min = temp.size();

				targetTile = escapeTileTemp;
			}
		}

		return targetTile;
	}

	private AiTile findNext() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> escapeTiles = new LinkedList<AiTile>();
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		int min = Integer.MAX_VALUE;
		AiTile escapeTileTemp;
		escapeTiles = safeZone();

		Iterator<AiTile> itEscape = escapeTiles.iterator();

		while (itEscape.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			escapeTileTemp = itEscape.next();

			PathFinder pathFind = new PathFinder(this.getPercepts(),
					escapeTileTemp, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			String ss = String.valueOf(temp.size());
			System.out.println(ss);
			if ((min > temp.size() && !temp.isEmpty()) || escapeTileTemp.equals(getPercepts().getOwnHero().getTile())) {
				min = temp.size();
				// System.out.printf(String.valueOf(min));
				// System.out.println(s);
				targetTile = escapeTileTemp;
			}
		}

		return targetTile;
	}

	@SuppressWarnings("unused")
	private AiTile escapeTile() throws StopRequestException {
		AiTile resultat = currentTile;
		List<Integer> times = new ArrayList<Integer>();
		Collection<AiBomb> bombs = getPercepts().getBombs();
		LinkedList<AiTile> bombTiles = new LinkedList<AiTile>();
		Iterator<AiBomb> it1 = bombs.iterator();
		while (it1.hasNext()) {

			if (it1.next().getBlast().contains(currentTile)
					|| it1.next().getTile() == currentTile) {
				AiTile temp = it1.next().getTile();
				bombTiles.add(temp);
				int time = (int) (it1.next().getNormalDuration() - it1.next()
						.getTime());
				times.add(time);
			}
		}

		return resultat;
	}

	@SuppressWarnings("unused")
	private LinkedList<AiTile> bombRange(AiBomb bomb)
			throws StopRequestException {
		LinkedList<AiTile> resultat = new LinkedList<AiTile>();

		return resultat;
	}

	private boolean danger() throws StopRequestException {
		boolean danger = false;
		LinkedList<AiTile> dangerTiles = dangerZone();
		if (dangerTiles.contains(currentTile)
				&& dangerTiles.contains(targetTile)) {
			danger = true;
		}
		return danger;
	}

	private void pickNextTile(AiTile targettile) throws StopRequestException {
		checkInterruption();
		AiZone zone = getPercepts();
		// AiHero ownHero = zone.getOwnHero();

		// previousTile = currentTile;
		if (targettile != currentTile) {
			PathFinder tempPath = new PathFinder(zone, targettile, this);

			this.path = tempPath.getPath();
			if (path != null && !path.isEmpty()) {
				nextTile = path.poll();
				if (nextTile == currentTile) {
					if (path != null)
						nextTile = path.poll();
				}
			}
		} else {
			nextTile = currentTile;
		}

	}

	@SuppressWarnings("unused")
	private List<Double> bombTimes() throws StopRequestException {
		List<Double> bombTimes = new ArrayList<Double>();
		Collection<AiBomb> bombs = getPercepts().getBombs();
		Iterator<AiBomb> it1 = bombs.iterator();
		while (it1.hasNext()) {
			double temp = it1.next().getNormalDuration();
			bombTimes.add(temp);
		}
		return bombTimes;

	}

	private LinkedList<AiTile> safeZone() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		LinkedList<AiTile> safeZone = new LinkedList<AiTile>();
		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();

		dangerZone = dangerZone();
		// System.out.println("danger"+dangerZone);
		blocks = getPercepts().getBlocks();
		Iterator<AiBlock> it1 = blocks.iterator();
		LinkedList<AiTile> blok = new LinkedList<AiTile>();
		while (it1.hasNext()) {
			AiTile temp = it1.next().getTile();
			blok.add(temp);
		}
		for (int i = 0; i < getPercepts().getWidth(); i++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < getPercepts().getHeigh(); j++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = getPercepts().getTile(j, i);
				if (!dangerZone.contains(tile) && !blok.contains(tile))
					safeZone.add(tile);
			}
		}
		// System.out.println(safeZone);
		return safeZone;

	}

	@SuppressWarnings("unused")
	private boolean isSur(AiTile tile) throws StopRequestException {
		boolean res = true;
		AiZone zone = getPercepts();
		Collection<AiBlock> blok = zone.getBlocks();

		Iterator<AiBlock> blokTile = blok.iterator();
		LinkedList<AiTile> tiles = new LinkedList<AiTile>();
		while (blokTile.hasNext()) {
			AiTile temp = blokTile.next().getTile();
			tiles.add(temp);
		}
		int count = 0;
		AiTile up = tile.getNeighbor(Direction.UP);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		if (tiles.contains(down))
			count++;
		if (tiles.contains(up))
			count++;
		if (tiles.contains(left))
			count++;
		if (tiles.contains(right))
			count++;
		if (count < 5)
			res = false;
		return res;
	}

	@SuppressWarnings("unused")
	private boolean isSafe(AiTile tile) throws StopRequestException {
		checkInterruption();

		boolean x = true;
		AiZone zone = getPercepts();
		// AiHero ownHero = zone.getOwnHero();

		if (dangerZone().contains(tile))
			;
		x = false;
		return x;
	}

	private LinkedList<AiTile> dangerZone() throws StopRequestException {

		checkInterruption();
		AiZone zone = getPercepts();

		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBomb> bombs = zone.getBombs();
		Collection<AiFire> fires = zone.getFires();
		Collection<AiBlock> blocks = zone.getBlocks();
		LinkedList<AiTile> blokTile = new LinkedList<AiTile>();
		Iterator<AiBlock> it_blocks = blocks.iterator();
		Iterator<AiFire> itfires = fires.iterator();
		while (it_blocks.hasNext()) {
			checkInterruption();
			AiBlock blok = it_blocks.next();
			AiTile tile = blok.getTile();
			blokTile.add(tile);
		}
		if (fires.size() > 0) {
			while (itfires.hasNext()) {
				AiFire fire = itfires.next();
				AiTile temp = fire.getTile();
				dangerZone.add(temp);
			}
		}
		Iterator<AiBomb> it1 = bombs.iterator();
		if (bombs.size() > 0) {

			while (it1.hasNext()) {
				checkInterruption();

				AiBomb bomb = it1.next();

				int k = bomb.getRange();
				int x = bomb.getCol();
				int y = bomb.getLine();
				// System.out.printf(Integer.toString(x), Integer.toString(y));
				AiTile tempTile = zone.getTile(y, x);
				dangerZone.add(tempTile);
				AiTile tile1 = tempTile;
				int i = 0;
				while (i < k && !blocks.contains(tempTile)) {
					checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.DOWN);
					// System.out.println("Asagi");
					tempTile = tile;
					if (!blokTile.contains(tile)) {
						// System.out.println("-1");
						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;

				while (i < k && !blocks.contains(tempTile)) {
					checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.UP);
					// System.out.println("yukarý");
					tempTile = tile;
					if (!blokTile.contains(tile)) {
						// System.out.println("-2");
						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
				while (i < k && !blocks.contains(tempTile)) {
					checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.RIGHT);
					// System.out.println("sagai");
					tempTile = tile;
					if (!blokTile.contains(tile)) {
						// System.out.println("-3");
						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
				while (i < k && !blocks.contains(tempTile)) {
					checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.LEFT);
					// System.out.println("sola");
					tempTile = tile;
					if (!blokTile.contains(tile)) {
						// System.out.println("-4");
						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
			}

		}
		return dangerZone;
	}

	private LinkedList<AiTile> bonusZone() throws StopRequestException {
		LinkedList<AiTile> bonusZone = new LinkedList<AiTile>();
		Collection<AiItem> bonus = getPercepts().getItems();
		Iterator<AiItem> it1 = bonus.iterator();
		while (it1.hasNext()) {
			AiTile temp = it1.next().getTile();
			bonusZone.add(temp);
		}
		return bonusZone;

	}

	@SuppressWarnings("unused")
	private AiTile findNextbon() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> bonusTile = new LinkedList<AiTile>();
		Collection<AiItem> items = getPercepts().getItems();
		Iterator<AiItem> it1 = items.iterator();
		boolean number = false;
		boolean range = false;
		if (getPercepts().getOwnHero().getBombNumber() < 5) {
			while (it1.hasNext()) {
				checkInterruption();
				if (it1.next().getType() == AiItemType.EXTRA_BOMB) {
					AiTile temp = it1.next().getTile();
					bonusTile.add(temp);
				}
			}
		}
		LinkedList<AiTile> bonuslar = bonusTile;
		LinkedList<AiTile> temp = new LinkedList<AiTile>();

		int min = 1000;
		Iterator<AiTile> goTile = bonuslar.iterator();
		while (goTile.hasNext()) {
			AiTile escapeTileTemp = goTile.next();
			PathFinder pathFind = new PathFinder(this.getPercepts(),
					escapeTileTemp, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			if ((min > temp.size() && min > 0 && !temp.isEmpty())
					|| escapeTileTemp.equals(getPercepts().getOwnHero()
							.getTile())) {
				min = temp.size();

				targetTile = escapeTileTemp;
			}
		}
		return targetTile;
	}

}
