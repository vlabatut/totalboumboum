package tournament200809.devecioglukorkmaz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;
import fr.free.totalboumboum.ai.adapter200809.AiAction;
import fr.free.totalboumboum.ai.adapter200809.AiActionName;
import fr.free.totalboumboum.ai.adapter200809.AiBlock;
import fr.free.totalboumboum.ai.adapter200809.AiBomb;
import fr.free.totalboumboum.ai.adapter200809.AiFire;
import fr.free.totalboumboum.ai.adapter200809.AiFloor;
import fr.free.totalboumboum.ai.adapter200809.AiHero;
import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.ai.adapter200809.AiZone;
import fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200809.StopRequestException;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class DeveciogluKorkmaz extends ArtificialIntelligence {

	AiHero enemy;
	Collection<AiBomb> bombs;
	private AiTile currentTile;
	AiZone zone;
	Collection<AiTile> dangerousTiles;
	Collection<AiTile> safeTiles;
	AiTile safeTile;

	public AiAction processAction() throws StopRequestException {
		checkInterruption();
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);
		if (ownHero != null) {
			currentTile = ownHero.getTile();
			this.zone = zone;
			Collection<AiHero> hereos = zone.getHeroes();
			Iterator<AiHero> k = hereos.iterator();
			AiHero temp;
			if (hereos != null) {
				while (k.hasNext()) {
					checkInterruption();
					temp = k.next();
					if (!temp.equals(ownHero))
						this.enemy = temp;
				}
			}
			bombs = zone.getBombs();
			safeTiles = setSafeTiles();
			dangerousTiles = setDangerousTiles();
			safeTile = setNearestSafeTile(currentTile);
			Direction direction;
			if (dangerousTiles.contains(currentTile)) {
				checkInterruption();
				direction = getPercepts().getDirection(currentTile,
						goToNearestSafeTile());
				result = new AiAction(AiActionName.MOVE, direction);
			} else if (getDistance(currentTile, enemy.getTile()) <= 1
					&& currentTile.getBombs().size() == 0) {
				result = new AiAction(AiActionName.DROP_BOMB);
				System.out.println("J'ai pose une bombe");
			} else {
				AiTile m = getCloseToEnemy();
				if (!dangerousTiles.contains(m)) {
					direction = getPercepts().getDirection(currentTile, m);
					result = new AiAction(AiActionName.MOVE, direction);
				}
			}
		}
		return result;
	}

	private Collection<AiTile> setSafeTiles() {
		Collection<AiFloor> floors = zone.getFloors();
		Iterator<AiFloor> i = floors.iterator();
		Collection<AiTile> tiles = new ArrayList<AiTile>();
		while (i.hasNext())
			tiles.add(i.next().getTile());
		tiles.removeAll(zone.getBlocks());
		if (dangerousTiles != null)
			tiles.removeAll(dangerousTiles);
		return tiles;

	}

	public AiTile setNearestSafeTile(AiTile t) throws StopRequestException {
		checkInterruption();
		AiTile solution = null;
		PriorityQueue<AiTile> fifo = new PriorityQueue<AiTile>(1000,
				new AiTileHeuristicComparator(currentTile, this));
		Iterator<AiTile> i;
		Collection<AiTile> visited = new ArrayList<AiTile>();
		fifo.offer(t);
		while (!fifo.isEmpty() && solution == null) {
			checkInterruption();
			AiTile node = fifo.poll();
			while (visited.contains(node)) {
				node = fifo.poll();
			}
			System.out.println(node.toString());
			if (!dangerousTiles.contains(node)) {
				solution = node;
			} else {
				visited.add(node);
				i = getClearNeighbours(node).iterator();
				while (i.hasNext()) {
					checkInterruption();
					AiTile temp = i.next();
					fifo.offer(temp);
				}
				checkInterruption();
			}
		}
		return solution;
	}

	private AiTile goToNearestSafeTile() throws StopRequestException {
		checkInterruption();
		Collection<AiTile> neighbours = new ArrayList<AiTile>();
		AiTile closest;
		AiTile temp;
		neighbours = getClearNeighbours(currentTile);
		Iterator<AiTile> i = neighbours.iterator();
		closest = i.next();
		while (i.hasNext()) {
			checkInterruption();
			temp = i.next();
			checkInterruption();
			if (getDistance(temp, safeTile) < getDistance(closest, safeTile)) {
				checkInterruption();
				closest = temp;
			}
		}
		return closest;
	}

	private AiTile getCloseToEnemy() throws StopRequestException {
		checkInterruption();
		AiTile close = null;
		Collection<AiTile> neighbours = new ArrayList<AiTile>();
		AiTile temp;
		neighbours = getClearNeighbours(currentTile);
		Iterator<AiTile> i = neighbours.iterator();
		close = i.next();
		while (i.hasNext()) {
			checkInterruption();
			temp = i.next();
			if (getDistance(temp, enemy.getTile()) < getDistance(close, enemy
					.getTile()))
				close = temp;
		}
		AiTile closest = close;
		PriorityQueue<AiTile> fifo = new PriorityQueue<AiTile>(1000,
				new AiTileHeuristicComparator(enemy.getTile(), this));
		Iterator<AiTile> it;
		Collection<AiTile> visited = new ArrayList<AiTile>();
		fifo.offer(close);
		while (!fifo.isEmpty() && closest == null) {
			checkInterruption();
			AiTile node = fifo.poll();
			while (visited.contains(node)) {
				node = fifo.poll();
			}
			System.out.println(node.toString());
			if (!dangerousTiles.contains(node)) {
				closest = node;
			} else {
				visited.add(node);
				it = getClearNeighbours(node).iterator();
				while (i.hasNext()) {
					checkInterruption();
					AiTile temp1 = it.next();
					fifo.offer(temp1);
				}
				checkInterruption();
			}
		}
		return closest;
	}

	public double getDistance(AiTile a, AiTile b) throws StopRequestException {
		checkInterruption();
		int vertical = Math.abs(a.getCol() - b.getCol());
		int horizontal = Math.abs(a.getLine() - b.getLine());
		return Math.hypot(vertical, horizontal);
	}

	private Collection<AiTile> setDangerousTiles() throws StopRequestException {
		checkInterruption();
		Collection<AiTile> neigbourTiles;
		AiTile temp1;
		AiTile temp2;
		Iterator<AiBomb> i = bombs.iterator();
		Iterator<AiTile> it;
		int k = 1;
		Collection<AiTile> result = new ArrayList<AiTile>();
		if (bombs.size() > 0) {
			while (i.hasNext()) {
				checkInterruption();
				temp1 = i.next().getTile();
				if (!result.contains(temp1))
					result.add(temp1);
				neigbourTiles = getClearNeighbours(temp1);
				it = neigbourTiles.iterator();
				while (it.hasNext()) {
					checkInterruption();
					temp2 = it.next();
					Direction d = zone.getDirection(temp1, temp2);
					while (isClear(temp2) || temp2.getFires().size() != 0
							&& k <= 5) {
						checkInterruption();
						if (!result.contains(temp2))
							result.add(temp2);
						temp2 = zone.getNeighbourTile(temp2, d);
						k++;
					}
				}
			}
		}
		System.out.println(result.toString());
		return result;
	}

	private ArrayList<AiTile> getClearNeighbours(AiTile tile)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		// liste des cases autour de la case de r�f�rence
		Collection<AiTile> neighbours = getPercepts().getNeighbourTiles(tile);
		// on garde les cases sans bloc ni bombe ni feu
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbours.iterator();
		while (it.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			AiTile t = it.next();
			if (isClear(t))
				result.add(t);
		}
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
}
