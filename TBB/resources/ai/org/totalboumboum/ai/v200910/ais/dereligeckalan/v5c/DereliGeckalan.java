package org.totalboumboum.ai.v200910.ais.dereligeckalan.v5c;

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
 * @author Merih Inal Dereli
 * @author Gökhan Geçkalan
 */
@SuppressWarnings("deprecation")
public class DereliGeckalan extends ArtificialIntelligence {

	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	private int matris[][] = null;
	/** */
	private AiTile nextTile;
	/** */
	private AiTile currentTile;
	/** */
	private AiTile targetTile;

	/** */
	private LinkedList<AiTile> path;
	/** */
	private boolean bonus = false;
	/** */
	@SuppressWarnings("unused")
	private AiTile murDestructible;
	/** */
	Direction direction = Direction.NONE;
	/** */
	private boolean target = false;
	/** */
	private AiTile tarr;
	/** */
	public AiTile bombTile;

	@Override
	public AiAction processAction() throws StopRequestException {
		// avant tout
		// : test
		// d'interruption

		checkInterruption();
		matris = new int[getPercepts().getHeight()][getPercepts().getWidth()];
		AiZone zone = getPercepts();

		currentTile = zone.getOwnHero().getTile();

		targetTile = currentTile;
		murDestructible = currentTile;
		ZoneDangereux zone1;
		AiAction result = new AiAction(AiActionName.NONE);
		double matrice[][] = new double[getPercepts().getHeight()][getPercepts()
				.getWidth()];

		if (danger()) {
			// if the agent is in danger
			zone1 = new ZoneDangereux(getPercepts(), this);
			matrice = zone1.getZoneArray();
			pickNextTile(findNext());
			zone1 = new ZoneDangereux(getPercepts(), this);
			matrice = zone1.getZoneArray();

			direction = zone.getDirection(currentTile, nextTile);
			result = new AiAction(AiActionName.MOVE, direction);
		} else if ((getPercepts().getOwnHero().getBombNumber() < 5)
				&& bonus == true) {
			// checks if the agent has enough attack power
			// if not finds the bonus on the zone and the goes to that tile
			pickNextTile(findNextbonus());
			direction = zone.getDirection(currentTile, nextTile);
			result = new AiAction(AiActionName.MOVE, direction);
		} else {
			// if the agent is not in danger and has enough attack power
			// drops bombs according to the values of the matrix
			zone1 = new ZoneDangereux(getPercepts(), this);
			matrice = zone1.getZoneArray();
			pickNextTile(findNext2());
			PathFinder path2 = new PathFinder(this.getPercepts(), tarr, this);
			LinkedList<AiTile> tempList = path2.getPath();
			if (surPath(tempList)) {
				int x = tarr.getCol();
				int y = tarr.getLine();
				zone1 = new ZoneDangereux(getPercepts(), this);
				matrice = zone1.getZoneArray();
				direction = zone.getDirection(currentTile, nextTile);
				if (matrice[y][x] < 0 && tarr == currentTile && target == true) {
					if (matrice[y][x] != -10) {
						result = new AiAction(AiActionName.DROP_BOMB);
						bombTile = currentTile;
					}
				} else if (tarr != currentTile) {
					result = new AiAction(AiActionName.MOVE, direction);
				}
			}

		}
		bonus = false;
		target = false;
		return result;
	}

	/**
	 * une méthode bidon pour l'exemple
	 * @param source 
	 * @param target 
	 * @return ?
	 * 
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private int distance(AiTile source, AiTile target)
			throws StopRequestException {
		checkInterruption();
		int res = 0;
		int x_1 = source.getCol();
		int y_1 = source.getLine();
		int x_2 = target.getCol();
		int y_2 = target.getLine();
		res = Math.abs(x_1 - x_2) + Math.abs(y_1 - y_2);
		return res + 1;
	}

	/**
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private String toTileString(AiTile tile) throws StopRequestException {
		checkInterruption();
		String res = "";
		int x = tile.getLine();
		int y = tile.getCol();
		res = res + String.valueOf(x) + "," + String.valueOf(y);
		return res;
	}

	/**
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private String toTileString2(LinkedList<AiTile> tile)
			throws StopRequestException {
		checkInterruption();
		String res = "";
		Iterator<AiTile> it1 = tile.iterator();
		while (it1.hasNext()) {
			checkInterruption();
			AiTile temp = it1.next();
			System.out.println(temp.toString() + "\n");

		}
		return res;
	}

	/**
	 * 
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private void initMatrice() throws StopRequestException {
		checkInterruption();
		// LinkedList<AiTile> danger = dangerZone();
		AiZone zone = getPercepts();

		for (int i = 0; i < zone.getHeight(); i++) {
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
			checkInterruption();
			AiBomb bomb = itBomb.next();
			double temp = bomb.getNormalDuration() - bomb.getTime();
			AiTile tile2 = bomb.getTile();
			int x = tile2.getCol();
			int y = tile2.getLine();

			matris[y][x] = (int) (bomb.getNormalDuration());

			int k = bomb.getRange();
			int i = 0;
			AiTile tile1 = tile2;

			while (i < k && !blocks.contains(tile2)) {
				checkInterruption();
				AiTile tile = tile2.getNeighbor(Direction.DOWN);

				tile2 = tile;
				if (!blokTile.contains(tile)) {

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

				tile2 = tile;
				if (!blokTile.contains(tile)) {

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

				tile2 = tile;
				if (!blokTile.contains(tile)) {

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

				tile2 = tile;
				if (!blokTile.contains(tile)) {

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
			checkInterruption();
			AiTile temp = it2.next();
			int x = temp.getLine();
			int y = temp.getCol();
			matris[x][y] = -1;
		}
		LinkedList<AiTile> bonusZone = bonusZone();
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < getPercepts().getHeight(); i++) {
			for (int j = 0; j < getPercepts().getWidth(); j++)
				result += "(" + i + "," + j + ")" + matris[i][j] + "   ";
			result += "\n";
		}

		return result;
	}

	/** finds the next tile that the agent can go (controls (uses the matrix) if the tile is safe and close to the agent)
	 *  
	 * @return ?
	 * @throws StopRequestException
	 */
	private AiTile findNext2() throws StopRequestException {
		checkInterruption();

		LinkedList<AiTile> temp = new LinkedList<AiTile>();

		ZoneDangereux zone1 = new ZoneDangereux(this.getPercepts(), this);
		double matris[][] = new double[this.getPercepts().getHeight()][this
				.getPercepts().getWidth()];
		matris = zone1.getZoneArray();
		AiTile t = null;
		for (int i = 0; i < getPercepts().getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < getPercepts().getWidth(); j++) {
				checkInterruption();
				t = getPercepts().getTile(i, j);
				if (!blokZone().contains(t))
					temp.add(t);
			}
		}
		Iterator<AiTile> it1 = temp.iterator();

		double min = Integer.MAX_VALUE;
		LinkedList<AiTile> res = new LinkedList<AiTile>();
		while (it1.hasNext()) {
			checkInterruption();
			AiTile tt = it1.next();
			LinkedList<AiTile> temp1 = new LinkedList<AiTile>();

			PathFinder path = new PathFinder(this.getPercepts(), tt, this);
			temp1 = path.getPath();
			if ((!temp1.isEmpty())) {

				res.add(tt);

			}

		}
		Iterator<AiTile> it2 = res.iterator();
		while (it2.hasNext()) {
			checkInterruption();
			AiTile temp3 = it2.next();

			int x = temp3.getLine();
			int y = temp3.getCol();

			if (matris[x][y] < min && !dangerZone().contains(temp3)
					&& safeZone().contains(temp3) && matris[x][y] < 0) {
				min = matris[x][y];

				targetTile = temp3;
				if (min == matris[currentTile.getLine()][currentTile.getCol()])
					targetTile = currentTile;

			}

		}
		LinkedList<AiTile> escapeTile = safeZone();
		int x = getPercepts().getOwnHero().getBombRange();
		int a = 0;
		AiTile up = targetTile.getNeighbor(Direction.UP);
		AiTile down = targetTile.getNeighbor(Direction.DOWN);
		AiTile left = targetTile.getNeighbor(Direction.LEFT);
		AiTile right = targetTile.getNeighbor(Direction.RIGHT);
		while (a < x && !blokZone().contains(up)) {
			checkInterruption();
			escapeTile.remove(up);
			up = up.getNeighbor(Direction.UP);
			a++;
		}
		a = 0;
		while (a < x && !blokZone().contains(down)) {
			checkInterruption();
			escapeTile.remove(down);
			down = down.getNeighbor(Direction.DOWN);
			a++;
		}
		a = 0;
		while (a < x && !blokZone().contains(left)) {
			checkInterruption();
			escapeTile.remove(left);
			left = left.getNeighbor(Direction.LEFT);
			a++;
		}
		a = 0;
		while (a < x && !blokZone().contains(right)) {
			checkInterruption();
			escapeTile.remove(right);
			right = right.getNeighbor(Direction.RIGHT);
			a++;
		}

		Iterator<AiTile> it4 = escapeTile.iterator();
		int min5 = Integer.MAX_VALUE;
		while (it4.hasNext()) {
			checkInterruption();
			AiTile temp5 = it4.next();
			PathFinder path = new PathFinder(this.getPercepts(), temp5, this);
			LinkedList<AiTile> temp6 = path.getPath();
			if (temp6.size() < min5 && !temp6.isEmpty() && temp6.size() > 1
					&& surPath(temp6)) {
				min5 = temp6.size();

			}
			if (min5 < 10) {
				target = true;
			} else {
				target = false;
			}
		}

		tarr = targetTile;
		return targetTile;
	}

	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private LinkedList<AiTile> surr() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> res = new LinkedList<AiTile>();
		LinkedList<AiTile> safeZone = safeZone();
		Iterator<AiTile> it1 = safeZone.iterator();
		while (it1.hasNext()) {
			checkInterruption();
			AiTile temp = it1.next();

			PathFinder pathFind = new PathFinder(this.getPercepts(), temp, this);
			LinkedList<AiTile> tempList = new LinkedList<AiTile>();
			tempList = pathFind.getPath();
			if (tempList.size() > 0 && !tempList.isEmpty()) {
				res.add(temp);
			}
		}
		return res;
	}

	/** checks if there's tile which is not safe on the path
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean surPath(LinkedList<AiTile> tile)
			throws StopRequestException {
		checkInterruption();
		boolean res = true;
		Iterator<AiTile> it1 = tile.iterator();
		while (it1.hasNext()) {
			checkInterruption();
			AiTile temp = it1.next();
			if (isSafe(temp) == true)
				res = true;
			else {
				res = false;
				break;
			}
		}
		return res;
	}

	/** finds the closest bonus to the agent
	 * 
	 * @return  ?
	 * @throws StopRequestException
	 */
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

			temp = (LinkedList<AiTile>) pathFind.getPath();

			if ((min > temp.size() && !temp.isEmpty() && surPath(temp) == true)
					|| escapeTileTemp.equals(getPercepts().getOwnHero()
							.getTile())) {
				min = temp.size();

				targetTile = escapeTileTemp;
				bonus = true;
			}
		}

		return targetTile;
	}

	/**
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private int nombreMur(AiTile tile) throws StopRequestException {
		checkInterruption();
		int res = 0;
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();

		int x = ownHero.getBombRange();

		int i = 0;
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile up = tile.getNeighbor(Direction.UP);
		boolean mur = false;
		while (i < x) {
			checkInterruption();

			while (!blokZone().contains(right)) {
				checkInterruption();
				i++;
				right = right.getNeighbor(Direction.RIGHT);
				System.out.println("3");
			}
			if (i != x) {
				res++;
			}
			i = 0;
			while (!blokZone().contains(left)) {
				checkInterruption();
				i++;
				left = left.getNeighbor(Direction.LEFT);
				System.out.println("4");
			}
			if (i != x) {
				res++;
			}
			i = 0;
			while (!blokZone().contains(down)) {
				checkInterruption();
				i++;
				down = down.getNeighbor(Direction.DOWN);
				System.out.println("5");
			}
			if (i != x) {
				res++;
			}
			i = 0;
			while (!blokZone().contains(up)) {
				checkInterruption();
				i++;
				up = up.getNeighbor(Direction.UP);
				System.out.println("6");
			}
			if (i != x) {
				res++;
			}
			i = 0;
		}
		i = 0;
		return res;
	}

	/** returns the tiles of the blocks on the zone
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private LinkedList<AiTile> blokZone() throws StopRequestException {
		checkInterruption();
		Collection<AiBlock> blokZone1 = getPercepts().getBlocks();
		LinkedList<AiTile> res = new LinkedList<AiTile>();
		Iterator<AiBlock> it1 = blokZone1.iterator();
		while (it1.hasNext()) {
			checkInterruption();
			AiBlock temp = it1.next();
			AiTile tempTile = temp.getTile();
			res.add(tempTile);
		}
		return res;
	}

	/** finds reachable closest tile that is safe
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
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

			temp = (LinkedList<AiTile>) pathFind.getPath();
			if ((min > temp.size() && !temp.isEmpty())
					|| escapeTileTemp.equals(getPercepts().getOwnHero()
							.getTile())) {
				min = temp.size();
				targetTile = escapeTileTemp;
			}
		}

		return targetTile;
	}

	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private AiTile escapeTile() throws StopRequestException {
		checkInterruption();
		AiTile resultat = currentTile;
		List<Integer> times = new ArrayList<Integer>();
		Collection<AiBomb> bombs = getPercepts().getBombs();
		LinkedList<AiTile> bombTiles = new LinkedList<AiTile>();
		Iterator<AiBomb> it1 = bombs.iterator();
		while (it1.hasNext()) {
			checkInterruption();
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

	/**
	 * 
	 * @param bomb
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private LinkedList<AiTile> bombRange(AiBomb bomb)
			throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> resultat = new LinkedList<AiTile>();

		return resultat;
	}

	/** checks if the agent is in danger
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean danger() throws StopRequestException {
		checkInterruption();
		boolean danger = false;
		LinkedList<AiTile> dangerTiles = dangerZone();
		if (dangerTiles.contains(currentTile)
				&& dangerTiles.contains(targetTile)) {
			danger = true;
		}
		return danger;
	}

	/** finds a path to the given target tile
	 * 
	 * @param targettile
	 * @throws StopRequestException
	 */
	private void pickNextTile(AiTile targettile) throws StopRequestException {
		checkInterruption();
		AiZone zone = getPercepts();
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

	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private List<Double> bombTimes() throws StopRequestException {
		checkInterruption();
		List<Double> bombTimes = new ArrayList<Double>();
		Collection<AiBomb> bombs = getPercepts().getBombs();
		Iterator<AiBomb> it1 = bombs.iterator();
		while (it1.hasNext()) {
			checkInterruption();
			double temp = it1.next().getNormalDuration();
			bombTimes.add(temp);
		}
		return bombTimes;

	}

	
	/** gives all the safe tiles on the zone
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private LinkedList<AiTile> safeZone() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		LinkedList<AiTile> safeZone = new LinkedList<AiTile>();
		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();
		dangerZone = dangerZone();
		blocks = getPercepts().getBlocks();
		Iterator<AiBlock> it1 = blocks.iterator();
		LinkedList<AiTile> blok = new LinkedList<AiTile>();
		while (it1.hasNext()) {
			checkInterruption();
			AiTile temp = it1.next().getTile();
			blok.add(temp);
		}
		for (int i = 0; i < getPercepts().getWidth(); i++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < getPercepts().getHeight(); j++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = getPercepts().getTile(j, i);
				if (!dangerZone.contains(tile) && !blok.contains(tile))
					safeZone.add(tile);
			}
		}
		return safeZone;

	}

	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private LinkedList<AiTile> safeZone2() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		LinkedList<AiTile> safeZone = new LinkedList<AiTile>();
		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();

		dangerZone = dangerZone();

		blocks = getPercepts().getBlocks();
		Iterator<AiBlock> it1 = blocks.iterator();
		LinkedList<AiTile> blok = new LinkedList<AiTile>();
		while (it1.hasNext()) {
			checkInterruption();
			AiTile temp = it1.next().getTile();
			blok.add(temp);
		}
		for (int i = 0; i < getPercepts().getWidth(); i++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < getPercepts().getHeight(); j++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = getPercepts().getTile(j, i);
				if (!dangerZone.contains(tile) && !blok.contains(tile))
					safeZone.add(tile);
			}
		}

		return safeZone;

	}

	/**
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private boolean isSur(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean res = true;
		AiZone zone = getPercepts();
		Collection<AiBlock> blok = zone.getBlocks();

		Iterator<AiBlock> blokTile = blok.iterator();
		LinkedList<AiTile> tiles = new LinkedList<AiTile>();
		while (blokTile.hasNext()) {
			checkInterruption();
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

	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private LinkedList<AiTile> murZone() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> res = new LinkedList<AiTile>();
		AiZone zone = getPercepts();
		Collection<AiBlock> blok = zone.getBlocks();
		Iterator<AiBlock> it1 = blok.iterator();
		while (it1.hasNext()) {
			checkInterruption();
			AiBlock temp1 = it1.next();
			AiTile temp2 = temp1.getTile();
			if (temp1.isDestructible()) {
				res.add(temp2);
			}
		}

		return res;
	}

	/** checks if the given tile is safe or not
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isSafe(AiTile tile) throws StopRequestException {
		checkInterruption();

		boolean x = true;
		if (dangerZone().contains(tile))

			x = false;
		return x;
	}

	/** gives the tiles that are dangereous
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
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
				checkInterruption();
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

				AiTile tempTile = zone.getTile(y, x);
				dangerZone.add(tempTile);
				AiTile tile1 = tempTile;
				int i = 0;
				while (i < k && !blocks.contains(tempTile)) {
					checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.DOWN);

					tempTile = tile;
					if (!blokTile.contains(tile)) {

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

					tempTile = tile;
					if (!blokTile.contains(tile)) {

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

					tempTile = tile;
					if (!blokTile.contains(tile)) {

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

					tempTile = tile;
					if (!blokTile.contains(tile)) {

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

	/**
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private LinkedList<AiTile> dangerZone1(AiTile tile)
			throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> res = dangerZone();
		int x = getPercepts().getOwnHero().getBombRange();
		int i = 0;
		AiTile up = tile.getNeighbor(Direction.UP);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		if (!blokZone().contains(up))
			res.add(up);
		if (!blokZone().contains(down))
			res.add(down);
		if (!blokZone().contains(left))
			res.add(left);
		if (!blokZone().contains(right))
			res.add(right);
		while (i < x && !blokZone().contains(right)) {
			checkInterruption();
			right = right.getNeighbor(Direction.RIGHT);
			if (!blokZone().contains(right)) {
				res.add(right);
			} else
				break;
			i++;
		}
		i = 0;
		while (i < x && !blokZone().contains(left)) {
			checkInterruption();

			left = left.getNeighbor(Direction.LEFT);
			if (!blokZone().contains(left)) {
				res.add(left);
			} else
				break;
			i++;
		}
		i = 0;
		while (i < x && !blokZone().contains(down)) {
			checkInterruption();
			down = down.getNeighbor(Direction.DOWN);
			if (!blokZone().contains(down)) {
				res.add(down);
			} else
				break;
			i++;
		}
		i = 0;
		while (i < x && !blokZone().contains(up)) {
			checkInterruption();
			right = right.getNeighbor(Direction.UP);
			if (!blokZone().contains(up)) {
				res.add(up);
			} else
				break;
			i++;
		}
		i = 0;
		return res;
	}
	
	/** gives the tiles of all bonus on the zone
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private LinkedList<AiTile> bonusZone() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> bonusZone = new LinkedList<AiTile>();
		Collection<AiItem> bonus = getPercepts().getItems();
		Iterator<AiItem> it1 = bonus.iterator();
		while (it1.hasNext()) {
			checkInterruption();
			AiTile temp = it1.next().getTile();
			bonusZone.add(temp);
		}
		return bonusZone;

	}

	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
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
			checkInterruption();
			AiTile escapeTileTemp = goTile.next();
			PathFinder pathFind = new PathFinder(this.getPercepts(),
					escapeTileTemp, this);

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

	/**
	 * 
	 * @param target
	 * @return ?
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private boolean canGo(AiTile target) throws StopRequestException {
		checkInterruption();
		boolean res = true;
		PathFinder path = new PathFinder(this.getPercepts(), target, this);
		LinkedList<AiTile> temp = path.getPath();
		int y = vitesse(target);
		Iterator<AiTile> it1 = temp.iterator();
		ZoneDangereux zonedanger = new ZoneDangereux(this.getPercepts(), this);
		double matris[][] = zonedanger.getZoneArray();

		AiTile temp1;
		while (it1.hasNext()) {
			checkInterruption();
			temp1 = it1.next();
			int i = temp1.getLine();
			int j = temp1.getCol();
			if (y < matris[i][j]) {
				res = false;
				break;
			} else
				res = true;

		}
		return res;
	}

	/** gives the time (in seconds) to get a given target
	 *  
	 * @param target
	 * @return ?
	 * @throws StopRequestException
	 */
	private int vitesse(AiTile target) throws StopRequestException {
		checkInterruption();
		int res = 0;
		PathFinder path = new PathFinder(this.getPercepts(), target, this);
		LinkedList<AiTile> temp = path.getPath();
		int distance = temp.size();
		double y = target.getSize();

		res = (int) (distance * (int) y)
				/ (int) (getPercepts().getOwnHero().getWalkingSpeed());

		return res;
	}
}
