package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v5;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

/**
 * cest un class Astar que lon a trouve par un site dintenet de forum dadressse
 * http://x86.sun.com/thread.jspa?messageID=10084929
 * 
 * @version 5
 * 
 * @author Erdem Bektaş
 * @author Nedim Mazilyah
 * 
 */
@SuppressWarnings("deprecation")
public final class Astar {
	/**
	 * 
	 *
	 */
	private static class CostComparator implements Comparator<Node> {
		@Override
		public int compare(Node nodeA, Node nodeB) {

			return (nodeA.gcost + nodeA.hcost) - (nodeB.gcost + nodeB.hcost);
		}
	}

	/**
	 * 
	 *
	 */
	class Node {
		/** */
		final int x;
		/** */
		final int y;

		/** */
		Node parent;
		/** */
		int gcost;
		/** */
		int hcost;

		/**
		 * on cree les noeuds
		 * 
		 * @param x
		 *            Description manquante !
		 * @param y
		 *            Description manquante !
		 */
		public Node(int x, int y) {
			assert x >= 0 && x < map.x : "x = " + x;
			assert y >= 0 && y < map.y : "y = " + y;

			this.x = x;
			this.y = y;
		}

		/**
		 * 
		 */
		public void calculateHeuristic() {

			hcost = (Math.abs(x - destination.x) + Math.abs(y - destination.y))
					* (VERTICAL_COST + HORIZONTAL_COST) / 2;
		}

		/**
		 * 
		 * @param parent
		 *            Description manquante !
		 */
		public void setParent(Node parent) {
			this.parent = parent;
			if (parent.x == x) {

				gcost = parent.gcost + HORIZONTAL_COST;
			} else if (parent.y == y) {
				gcost = parent.gcost + VERTICAL_COST;
			} else {
				gcost = parent.gcost + DIAGONAL_COST;
			}
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + " : " + super.toString() + ')';
		}
	}

	/** */
	private static final CostComparator COST_CMP = new CostComparator();

	/** */
	private final int VERTICAL_COST = 10;

	/** */
	private final int HORIZONTAL_COST = 10;

	/** */
	private final int DIAGONAL_COST = (int) Math.rint(Math.sqrt(VERTICAL_COST
			* VERTICAL_COST + HORIZONTAL_COST * HORIZONTAL_COST));

	/** */
	private final DangerZone map;
	/** */
	private final Node origin;
	/** */
	private final Node destination;

	/** */
	private final Queue<Node> open;

	/** */
	private final int[] closed;

	/**
	 * 
	 * @param map
	 *            Description manquante !
	 * @param originX
	 *            Description manquante !
	 * @param originY
	 *            Description manquante !
	 * @param destinationX
	 *            Description manquante !
	 * @param destinationY
	 *            Description manquante !
	 */
	public Astar(DangerZone map, int originX, int originY, int destinationX,
			int destinationY) {
		assert map != null : "map = " + map;

		this.map = map;
		destination = new Node(destinationX, destinationY);
		origin = new Node(originX, originY);

		open = new PriorityQueue<Node>(Math.max(map.x, map.y) * 2, COST_CMP);

		closed = new int[(map.x * map.y >> 5) + 1];

	}

	/**
	 * Adds the node at {@code x}, {@code y} to the open list, using
	 * {@code parent} as the parent.
	 * 
	 * <p>
	 * If the node was already added to the open list, the old value is either
	 * kept or replaced, depending on whether the old one is closer to the
	 * origin or not.
	 * </p>
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @param parent
	 *            Description manquante !
	 */

	private void addToOpen(int x, int y, Node parent) {
		Node openNode = new Node(x, y);
		openNode.setParent(parent);

		replacing: for (Iterator<Node> i = open.iterator(); i.hasNext();) {
			Node existing = i.next();
			if (existing.x == x && existing.y == y) {
				if (existing.gcost > openNode.gcost) {
					i.remove();
					break replacing;
				} else {
					return;
				}
			}
		}

		openNode.calculateHeuristic();
		open.add(openNode);
	}

	/**
	 * Starts the algorithm and returns true if a valid path was found.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean findPath() throws StopRequestException {
		Node current = origin;
		while (current != null
				&& (current.x != destination.x || current.y != destination.y)) {
			process(current);
			current = open.poll();
		}

		if (current != null) {
			destination.setParent(current.parent);
		}

		return current != null;
		// return true;
	}

	/**
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean findSecurePath() throws StopRequestException {
		Node current = origin;
		while (current != null
				&& (current.x != destination.x || current.y != destination.y)) {
			processBonus(current);
			current = open.poll();
		}

		if (current != null) {
			destination.setParent(current.parent);
		}

		return current != null;
		// return true;
	}

	/**
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean findPathSurWall() throws StopRequestException {
		Node current = origin;
		while (current != null
				&& (current.x != destination.x || current.y != destination.y)) {
			processWall(current);
			current = open.poll();
		}

		if (current != null) {
			destination.setParent(current.parent);
		}

		return current != null;
		// return true;
	}

	/**
	 * 
	 * @return ?
	 */
	public Deque<Integer> getPath() {
		assert destination.parent != null
				|| (destination.x == origin.x && destination.y == origin.y);

		Deque<Integer> path = new LinkedList<Integer>();
		Node current = destination;

		while (current != null) {
			path.addFirst(current.y);
			path.addFirst(current.x);
			current = current.parent;
		}

		return path;
	}

	/**
	 * Checks whether a node was already processed.
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @return ? Description manquante !
	 */
	private boolean isClosed(int x, int y) {
		int i = map.x * y + x;
		return (closed[i >> 5] & (1 << (i & 31))) != 0;
	}

	/**
	 * Processes the passed node.
	 * 
	 * <ul>
	 * <li>Adds it to the closed list.</li>
	 * <li>Adds all adjacent nodes to the open list, if it was not processed yet
	 * and is walkable.</li>
	 * </ul>
	 * 
	 * @param node
	 *            Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */

	private void process(Node node) throws StopRequestException {
		// no need to process it twice
		setClosed(node.x, node.y);
		int c = node.x;
		int cy = node.y;

		// respect the array bounds
		int lX = node.x == 0 ? 0 : node.x - 1;
		int uX = node.x >= map.x - 1 ? map.x - 1 : node.x + 1;
		int lY = node.y == 0 ? 0 : node.y - 1;
		int uY = node.y >= map.y - 1 ? map.y - 1 : node.y + 1;

		// check all the neighbors
		for (int x = lX; x <= uX; ++x) {
			for (int y = lY; y <= uY; ++y) {
				if (!isClosed(x, y) && map.isWalkable(x, y)
						&& (c == x || cy == y)) {
					addToOpen(x, y, node);
				}
			}
		}
	}

	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void processBonus(Node node) throws StopRequestException {
		// no need to process it twice
		setClosed(node.x, node.y);
		int c = node.x;
		int cy = node.y;

		// respect the array bounds
		int lX = node.x == 0 ? 0 : node.x - 1;
		int uX = node.x >= map.x - 1 ? map.x - 1 : node.x + 1;
		int lY = node.y == 0 ? 0 : node.y - 1;
		int uY = node.y >= map.y - 1 ? map.y - 1 : node.y + 1;

		// check all the neighbors
		for (int x = lX; x <= uX; ++x) {
			for (int y = lY; y <= uY; ++y) {
				if (!isClosed(x, y) && map.canGoToBonus(x, y)
						&& (c == x || cy == y)) {
					addToOpen(x, y, node);
				}
			}
		}
	}

	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void processWall(Node node) throws StopRequestException {
		// no need to process it twice
		setClosed(node.x, node.y);
		int c = node.x;
		int cy = node.y;

		// respect the array bounds
		int lX = node.x == 0 ? 0 : node.x - 1;
		int uX = node.x >= map.x - 1 ? map.x - 1 : node.x + 1;
		int lY = node.y == 0 ? 0 : node.y - 1;
		int uY = node.y >= map.y - 1 ? map.y - 1 : node.y + 1;

		// check all the neighbors
		for (int x = lX; x <= uX; ++x) {
			for (int y = lY; y <= uY; ++y) {
				if (!isClosed(x, y) && map.canGoRival(x, y)
						&& (c == x || cy == y)) {
					addToOpen(x, y, node);
				}
			}
		}
	}

	/**
	 * Sets the node at {@code x}, {@code y} to "already been processed".
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 */
	private void setClosed(int x, int y) {
		int i = map.x * y + x;
		closed[i >> 5] |= (1 << (i & 31));
	}
}
