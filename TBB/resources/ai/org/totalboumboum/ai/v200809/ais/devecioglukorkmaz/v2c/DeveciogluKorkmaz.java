package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2c;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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
 * @author Eser Devecioğlu
 * @author lev Korkmaz
 *
 */
@SuppressWarnings("deprecation")
public class DeveciogluKorkmaz extends ArtificialIntelligence {

	AiHero ownHero;
	Collection<AiHero> enemies = new ArrayList<AiHero>();
	Collection<AiBomb> bombs;
	private AiTile currentTile;
	AiZone zone;
	Collection<AiTile> dangerousTiles;
	AiTile safeTile;
	AiTile nearestDestructible;
	AiTile nearestItem;
	AiTile nearestHero;
	private LinkedList<Noeud> path;
	Noeud cible;
	int nombreHeroes;

	public AiAction processAction() throws StopRequestException {
		checkInterruption();
		AiZone zone = getPercepts();
		ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);
		if (ownHero != null) {
			currentTile = ownHero.getTile();
			this.zone = zone;
			// updating enemies
			Iterator<AiHero> k = zone.getHeroes().iterator();
			AiHero temp;
			enemies = new ArrayList<AiHero>();
			while (k.hasNext()) {
				checkInterruption();
				temp = k.next();
				if (!temp.equals(ownHero)
						&& !temp.getState().getName().name().equals("BURNING"))
					enemies.add(temp);
			}
			// updating bombs
			bombs = zone.getBombs();
			// updating dangerous tiles
			dangerousTiles = setDangerousTiles();
			safeTile = setNearestSafeTile();
			nearestDestructible = setNearestDestructible();
			nearestItem = setNearestItem();
			nearestHero = setNearestHero();
			Direction direction = null;
			int myBombs = 0;
			boolean dontBomb = false;
			Iterator<AiTile> iterator = getPercepts().getNeighborTiles(
					currentTile).iterator();
			while (iterator.hasNext() && !dontBomb) {
				checkInterruption();
				AiTile temp4 = iterator.next();
				if (!temp4.getBombs().isEmpty()
						|| dangerousTiles.contains(temp4))
					dontBomb = true;
			}
			if (bombs.size() > 0) {
				Iterator<AiBomb> i = bombs.iterator();
				while (i.hasNext()) {
					checkInterruption();
					AiBomb temp3 = i.next();
					if (temp3.getColor().equals(ownHero.getColor())) {
						myBombs++;
					}
				}
			}
			k = enemies.iterator();
			boolean kill = false;
			nombreHeroes = 0;
			while (k.hasNext()) {
				checkInterruption();
				nombreHeroes++;
				AiTile temp2 = k.next().getTile();
				if (getDistance(temp2, currentTile) == 1)
					kill = true;
			}
			boolean danger = false;
			AiTile nextSafe = setPathNearestSafeTile(safeTile);
			AiTile nextItem = setPath(nearestItem);
			AiTile nextDestructible = setPath(nearestDestructible);
			AiTile nextHero = setPathNearestSafeTile(nearestHero);
			AiTile nextTile = null;
			if (dangerousTiles.contains(currentTile) && nextSafe != null
					&& safeTile != null) {
				direction = getPercepts().getDirection(currentTile, nextSafe);
				nextTile = nextSafe;
				danger = true;
			} else if (nearestItem != null && nextItem != null) {
				direction = getPercepts().getDirection(currentTile, nextItem);
				nextTile = nextItem;
			} else if (myBombs > 0) {
				result = new AiAction(AiActionName.NONE);
			} else if (kill && myBombs <= 2 && !dontBomb) {
				result = new AiAction(AiActionName.DROP_BOMB);
			} else if (nearestDestructible != null
					&& getDistance(currentTile, nearestDestructible) == 0
					&& !dontBomb) {
				result = new AiAction(AiActionName.DROP_BOMB);
			} else if (nearestDestructible != null && nextDestructible != null) {
				direction = getPercepts().getDirection(currentTile,
						nextDestructible);
				nextTile = nextDestructible;
			} else if (nearestHero != null && nextHero != null) {
				direction = getPercepts().getDirection(currentTile, nextHero);
				nextTile = nextHero;
			}
			if (!danger && dangerousTiles.contains(nextTile)) {
				result = new AiAction(AiActionName.NONE);
			}
			// last check
			if (nextTile != null && nextTile.getBombs().isEmpty()
					&& nextTile.getFires().isEmpty())
				result = new AiAction(AiActionName.MOVE, direction);
		}
		return result;
	}

	private AiTile setPath(AiTile target) throws StopRequestException {
		checkInterruption();
		Noeud cible = new Noeud(target,this);
		path = new LinkedList<Noeud>();
		Noeud courant = new Noeud(currentTile,this);
		Tree tree = new Tree(courant,this);
		NoeudComparator comparator = new NoeudComparator(courant, this);
		PriorityQueue<Noeud> frange = new PriorityQueue<Noeud>(1000, comparator);
		LinkedList<Noeud> open = new LinkedList<Noeud>();
		LinkedList<Noeud> closed = new LinkedList<Noeud>();
		Noeud temp = new Noeud(currentTile,this);
		boolean found = false;
		frange.offer(courant);
		open.add(courant);
		while (!found && !frange.isEmpty()) {
			checkInterruption();
			temp = frange.poll();
			open.remove(open.indexOf(temp));
			closed.add(temp);
			if (temp.getTile().equals(cible.getTile())) {
				found = true;
			} else {
				AiTile t = temp.getTile();
				Collection<AiTile> neighbors = getClearNeighbors(t);
				neighbors.removeAll(dangerousTiles);
				Iterator<AiTile> i = neighbors.iterator();
				while (i.hasNext()) {
					checkInterruption();
					AiTile temp2 = i.next();
					Noeud noeud = new Noeud(temp2,this);
					if (!closed.contains(noeud) && !open.contains(noeud)) {
						open.add(noeud);
						tree.addNoeud(temp, noeud);
						frange.offer(noeud);
					}
				}
			}
		}
		if (found)
			path = tree.getPath(temp);
		frange = null;
		tree = null;
		AiTile result;
		if (path.size() > 1)
			result = path.get(path.size() - 2).getTile();
		else
			result = null;
		return result;
	}

	private AiTile setPathNearestSafeTile(AiTile target)
			throws StopRequestException {
		checkInterruption();
		Noeud cible = new Noeud(target,this);
		path = new LinkedList<Noeud>();
		Noeud courant = new Noeud(currentTile,this);
		Tree tree = new Tree(courant,this);
		NoeudComparator comparator = new NoeudComparator(courant, this);
		PriorityQueue<Noeud> frange = new PriorityQueue<Noeud>(1000, comparator);
		LinkedList<Noeud> open = new LinkedList<Noeud>();
		LinkedList<Noeud> closed = new LinkedList<Noeud>();
		Noeud temp = new Noeud(currentTile,this);
		boolean found = false;
		frange.offer(courant);
		open.add(courant);
		while (!found && !frange.isEmpty()) {
			checkInterruption();
			temp = frange.poll();
			open.remove(open.indexOf(temp));
			closed.add(temp);
			if (temp.getTile().equals(cible.getTile())) {
				found = true;
			} else {
				AiTile t = temp.getTile();
				Collection<AiTile> neighbors = getClearNeighbors(t);
				Iterator<AiTile> i = neighbors.iterator();
				while (i.hasNext()) {
					checkInterruption();
					AiTile temp2 = i.next();
					Noeud noeud = new Noeud(temp2,this);
					if (!closed.contains(noeud) && !open.contains(noeud)) {
						open.add(noeud);
						tree.addNoeud(temp, noeud);
						frange.offer(noeud);
					}
				}
			}
		}
		if (found)
			path = tree.getPath(temp);
		frange = null;
		tree = null;
		AiTile result;
		if (path.size() > 1)
			result = path.get(path.size() - 2).getTile();
		else
			result = null;
		return result;
	}

	private AiTile setNearestItem() throws StopRequestException {
		checkInterruption();
		AiTile solution = null;
		PriorityQueue<AiTile> fifo = new PriorityQueue<AiTile>(1000,
				new AiTileComparator(currentTile, this));
		Iterator<AiTile> i;
		Collection<AiTile> visited = new ArrayList<AiTile>();
		fifo.offer(currentTile);
		while (!fifo.isEmpty() && solution == null) {
			checkInterruption();
			AiTile node = fifo.poll();
			if (node.getItem() != null) {
				if (!node.getItem().getState().getName().name().equals(
						"BURNING"))
					solution = node;
			} else {
				visited.add(node);
				i = getClearNeighbors(node).iterator();
				while (i.hasNext()) {
					checkInterruption();
					AiTile temp = i.next();
					if (!dangerousTiles.contains(temp)
							&& !visited.contains(temp))
						fifo.offer(temp);
				}
			}
		}
		return solution;
	}

	private AiTile setNearestDestructible() throws StopRequestException {
		checkInterruption();
		AiTile solution = null;
		PriorityQueue<AiTile> fifo = new PriorityQueue<AiTile>(1000,
				new AiTileComparator(currentTile, this));
		Iterator<AiTile> i;
		Collection<AiTile> visited = new ArrayList<AiTile>();
		boolean found = false;
		AiTile temp;
		fifo.offer(currentTile);
		while (!fifo.isEmpty() && solution == null) {
			checkInterruption();
			AiTile node = fifo.poll();
			visited.add(node);
			i = getPercepts().getNeighborTiles(node).iterator();
			while (i.hasNext()) {
				checkInterruption();
				temp = i.next();
				if (temp.getBlock() != null) {
					if (temp.getBlock().isDestructible())
						found = true;
				}
			}
			if (found) {
				solution = node;
			} else {
				i = getClearNeighbors(node).iterator();
				while (i.hasNext()) {
					checkInterruption();
					temp = i.next();
					if (!dangerousTiles.contains(temp)
							&& !visited.contains(temp))
						fifo.offer(temp);
				}
			}
		}
		return solution;
	}

	private AiTile setNearestSafeTile() throws StopRequestException {
		checkInterruption();
		AiTile solution = null;
		PriorityQueue<AiTile> fifo = new PriorityQueue<AiTile>(1000,
				new AiTileComparator(currentTile, this));
		Iterator<AiTile> i;
		Collection<AiTile> visited = new ArrayList<AiTile>();
		fifo.offer(currentTile);
		while (!fifo.isEmpty() && solution == null) {
			checkInterruption();
			AiTile node = fifo.poll();
			if (!dangerousTiles.contains(node)) {
				solution = node;
			} else {
				visited.add(node);
				i = getClearNeighbors(node).iterator();
				while (i.hasNext()) {
					checkInterruption();
					AiTile temp = i.next();
					if (!visited.contains(temp))
						fifo.offer(temp);
				}
			}
		}
		return solution;
	}

	private Collection<AiTile> setDangerousTiles() throws StopRequestException {
		checkInterruption();
		Collection<AiTile> neigbourTiles;
		AiBomb temp1;
		AiTile temp2;
		Iterator<AiBomb> i = bombs.iterator();
		Iterator<AiTile> it;
		int k = 0;
		Collection<AiTile> result = new ArrayList<AiTile>();
		if (bombs.size() > 0) {
			while (i.hasNext()) {
				checkInterruption();
				temp1 = i.next();
				int range = temp1.getRange();
				if (!result.contains(temp1.getTile()))
					result.add(temp1.getTile());
				neigbourTiles = getPercepts()
						.getNeighborTiles(temp1.getTile());
				it = neigbourTiles.iterator();
				while (it.hasNext()) {
					checkInterruption();
					temp2 = it.next();
					Direction d = zone.getDirection(temp1.getTile(), temp2);
					boolean continuer = true;
					while (isClear(temp2) || temp2.getFires().size() != 0
							&& k <= range && continuer) {
						checkInterruption();
						if (!result.contains(temp2))
							result.add(temp2);
						if (temp2.getItem() != null)
							continuer = false;
						temp2 = zone.getNeighborTile(temp2, d);
						k++;
					}
				}
			}
		}
		Collection<AiFire> fires = zone.getFires();
		Iterator<AiFire> iterator = fires.iterator();
		while (iterator.hasNext()) {
			checkInterruption();
			AiTile temp3 = iterator.next().getTile();
			if (!result.contains(temp3))
				result.add(temp3);
		}
		return result;
	}

	private AiTile setNearestHero() throws StopRequestException {
		checkInterruption();
		AiTile solution = null;
		PriorityQueue<AiTile> fifo = new PriorityQueue<AiTile>(1000,
				new AiTileComparator(currentTile, this));
		Iterator<AiTile> i;
		Collection<AiTile> visited = new ArrayList<AiTile>();
		fifo.offer(currentTile);
		while (!fifo.isEmpty() && solution == null) {
			checkInterruption();
			AiTile node = fifo.poll();
			Collection<AiHero> heroes = new ArrayList<AiHero>();
			if (!node.equals(currentTile))
				heroes = node.getHeroes();
			if (!heroes.isEmpty()) {
				solution = node;
			} else {
				visited.add(node);
				i = getClearNeighbors(node).iterator();
				while (i.hasNext()) {
					checkInterruption();
					AiTile temp = i.next();
					if (!visited.contains(temp)
							&& !dangerousTiles.contains(temp))
						fifo.offer(temp);
				}
			}
		}
		return solution;
	}

	public double getDistance(AiTile a, AiTile b) throws StopRequestException {
		checkInterruption();
		int vertical = Math.abs(a.getCol() - b.getCol());
		int horizontal = Math.abs(a.getLine() - b.getLine());
		return vertical + horizontal;
		// return Math.hypot(vertical, horizontal);
	}

	private List<AiTile> getClearNeighbors(AiTile tile)
			throws StopRequestException {
		checkInterruption();
		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(tile);
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while (it.hasNext()) {
			checkInterruption();
			AiTile t = it.next();
			if (t.getBlock() == null && t.getBombs().isEmpty()
					&& t.getFires().isEmpty())
				result.add(t);
		}
		return result;
	}

	private boolean isClear(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean result = false;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		if (block == null && bombs.size() == 0 && fires.size() == 0)
			result = true;
		return result;
	}

}
