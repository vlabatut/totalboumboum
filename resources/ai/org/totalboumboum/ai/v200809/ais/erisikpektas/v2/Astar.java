package org.totalboumboum.ai.v200809.ais.erisikpektas.v2;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * cest un class Astar que lon a trouve par un site dintenet de forum dadressse
 * http://x86.sun.com/thread.jspa?messageID=10084929
 * 
 * @author Doğa Erişik
 * @author Abdurrahman Pektaş
 * 
 */
public final class Astar {
	/** */
	private static class CostComparator implements Comparator<Node> {
		//
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
			assert x >= 0 && x < map.width : "x = " + x;
			assert y >= 0 && y < map.height : "y = " + y;

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
	private static final CostComparator COST_CMPdan = new CostComparator();
	/** */
	private static final CostComparator COST_CMPexp = new CostComparator();
	/** */
	private static final CostComparator COST_CMPcho = new CostComparator();

	/** */
	private final int VERTICAL_COST = 10;

	/** */
	private final int HORIZONTAL_COST = 10;

	/** */
	private final int DIAGONAL_COST = (int) Math.rint(Math.sqrt(VERTICAL_COST
			* VERTICAL_COST + HORIZONTAL_COST * HORIZONTAL_COST));

	/** */
	private final Map map;
	/** */
	private final Node origin;
	/** */
	private final Node destination;

	/** */
	private final Queue<Node> open;
	/** */
	private final Queue<Node> openchoix;
	/** */
	private final Queue<Node> openexp;
	/** */
	private final Queue<Node> opendang;

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
	public Astar(Map map, int originX, int originY, int destinationX,
			int destinationY) {
		assert map != null : "map = " + map;

		this.map = map;
		destination = new Node(destinationX, destinationY);
		origin = new Node(originX, originY);

		open = new PriorityQueue<Node>(Math.max(map.width, map.height) * 2,
				COST_CMP);
		openchoix = new PriorityQueue<Node>(
				Math.max(map.width, map.height) * 2, COST_CMPcho);
		openexp = new PriorityQueue<Node>(Math.max(map.width, map.height) * 2,
				COST_CMPexp);
		opendang = new PriorityQueue<Node>(Math.max(map.width, map.height) * 2,
				COST_CMPdan);

		closed = new int[(map.width * map.height >> 5) + 1];

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
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @param parent
	 *            Description manquante !
	 */
	private void addToOpenchoix(int x, int y, Node parent) {
		Node openNode = new Node(x, y);
		openNode.setParent(parent);

		replacing: for (Iterator<Node> i = openchoix.iterator(); i.hasNext();) {
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
		openchoix.add(openNode);
	}

	/**
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @param parent
	 *            Description manquante !
	 */
	private void addToOpenexp(int x, int y, Node parent) {
		Node openNode = new Node(x, y);
		openNode.setParent(parent);

		replacing: for (Iterator<Node> i = openexp.iterator(); i.hasNext();) {
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
		openexp.add(openNode);
	}

	/**
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @param parent
	 *            Description manquante !
	 */
	private void addToOpendang(int x, int y, Node parent) {
		Node openNode = new Node(x, y);
		openNode.setParent(parent);

		replacing: for (Iterator<Node> i = opendang.iterator(); i.hasNext();) {
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
		opendang.add(openNode);
	}

	/**
	 * Starts the algorithm and returns true if a valid path was found.
	 * 
	 * @return ?
	 */
	public boolean findPath() {
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
	 * @return ?
	 */
	public boolean findPathchoix() {
		Node current = origin;
		while (current != null
				&& (current.x != destination.x || current.y != destination.y)) {
			processchoix(current);
			current = openchoix.poll();
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
	public boolean findPathexp() {
		Node current = origin;
		while (current != null
				&& (current.x != destination.x || current.y != destination.y)) {
			processexp(current);
			current = openexp.poll();
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
	public boolean findPathdang() {
		Node current = origin;
		while (current != null
				&& (current.x != destination.x || current.y != destination.y)) {
			processdang(current);
			current = opendang.poll();
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
		int i = map.width * y + x;
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
	 */
	private void process(Node node) {
		// no need to process it twice
		setClosed(node.x, node.y);
		int c = node.x;
		int cy = node.y;

		// respect the array bounds
		int lX = node.x == 0 ? 0 : node.x - 1;
		int uX = node.x >= map.width - 1 ? map.width - 1 : node.x + 1;
		int lY = node.y == 0 ? 0 : node.y - 1;
		int uY = node.y >= map.height - 1 ? map.height - 1 : node.y + 1;

		// check all the neighbors
		for (int x = lX; x <= uX; ++x) {
			for (int y = lY; y <= uY; ++y) {
				if (!isClosed(x, y) && map.isRunnable(x, y)
						&& (c == x || cy == y)) {
					addToOpen(x, y, node);
				}
			}
		}
	}

	/**
	 * 
	 * @param node
	 *            Description manquante !
	 */
	private void processchoix(Node node) {
		// no need to process it twice
		setClosed(node.x, node.y);
		int c = node.x;
		int cy = node.y;

		// respect the array bounds
		int lX = node.x == 0 ? 0 : node.x - 1;
		int uX = node.x >= map.width - 1 ? map.width - 1 : node.x + 1;
		int lY = node.y == 0 ? 0 : node.y - 1;
		int uY = node.y >= map.height - 1 ? map.height - 1 : node.y + 1;

		// check all the neighbors
		for (int x = lX; x <= uX; ++x) {
			for (int y = lY; y <= uY; ++y) {
				// pour les directions composites
				if (!isClosed(x, y) && map.isWalkable(x, y)
						&& (c == x || cy == y)) {
					addToOpenchoix(x, y, node);
				}
			}
		}
	}

	/**
	 * 
	 * @param node
	 *            Description manquante !
	 */
	private void processexp(Node node) {
		// no need to process it twice
		setClosed(node.x, node.y);
		int c = node.x;
		int cy = node.y;

		// respect the array bounds
		int lX = node.x == 0 ? 0 : node.x - 1;
		int uX = node.x >= map.width - 1 ? map.width - 1 : node.x + 1;
		int lY = node.y == 0 ? 0 : node.y - 1;
		int uY = node.y >= map.height - 1 ? map.height - 1 : node.y + 1;

		// check all the neighbors
		for (int x = lX; x <= uX; ++x) {
			for (int y = lY; y <= uY; ++y) {
				if (!isClosed(x, y) && map.isReachable(x, y)
						&& (c == x || cy == y)) {
					addToOpenexp(x, y, node);
				}
			}
		}
	}

	/**
	 * 
	 * @param node
	 *            Description manquante !
	 */
	private void processdang(Node node) {
		// no need to process it twice
		setClosed(node.x, node.y);
		int c = node.x;
		int cy = node.y;

		// respect the array bounds
		int lX = node.x == 0 ? 0 : node.x - 1;
		int uX = node.x >= map.width - 1 ? map.width - 1 : node.x + 1;
		int lY = node.y == 0 ? 0 : node.y - 1;
		int uY = node.y >= map.height - 1 ? map.height - 1 : node.y + 1;

		// check all the neighbors
		for (int x = lX; x <= uX; ++x) {
			for (int y = lY; y <= uY; ++y) {
				if (!isClosed(x, y) && map.isNoWhereElse(x, y)
						&& (c == x || cy == y)) {
					addToOpendang(x, y, node);
				}
			}
		}
	}

	/**
	 * Sets the node at {@code x}, {@code y} to "already been processed".
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 */
	private void setClosed(int x, int y) {
		int i = map.width * y + x;
		closed[i >> 5] |= (1 << (i & 31));
	}
}
