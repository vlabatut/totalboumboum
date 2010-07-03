package org.totalboumboum.ai.v200910.ais.calisirguner.v5c;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

/**
 * cest un class Astar que lon a trouve par un site dintenet de forum dadressse http://x86.sun.com/thread.jspa?messageID=10084929
 * 
 * @version 5.c
 * 
 * @author Emre Calisir
 * @author Burak Ozgen Guner
 *
 */
public final class Astar {
	private static class CostComparator implements Comparator<Node> {
		//
		public int compare(Node nodeA, Node nodeB) {

			return (nodeA.gcost + nodeA.hcost) - (nodeB.gcost + nodeB.hcost);
		}
	}

	class Node {
		final int x;
		final int y;

		Node parent;
		int gcost;
		int hcost;
		ArtificialIntelligence ai;
		
		// on cree les noeuds
		public Node(int x, int y, ArtificialIntelligence ai) throws StopRequestException {
			ai.checkInterruption();
			this.ai = ai;
			assert x >= 0 && x < map.width : "x = " + x;
			assert y >= 0 && y < map.height : "y = " + y;

			this.x = x;
			this.y = y;
		}

		public void calculateHeuristic() throws StopRequestException {
			ai.checkInterruption();
			hcost = (Math.abs(x - destination.x) + Math.abs(y - destination.y))
					* (VERTICAL_COST + HORIZONTAL_COST) / 2;
		}

		public void setParent(Node parent) throws StopRequestException {
			ai.checkInterruption();
			this.parent = parent;
			// on se profite de matrice de risque qui envoie le danger des cases
			// ou
			int DANGER_COST = map.return_risque(parent.x, parent.y);

			if (parent.x == x) {

				gcost = parent.gcost + HORIZONTAL_COST + DANGER_COST;
			} else if (parent.y == y) {
				gcost = parent.gcost + VERTICAL_COST + DANGER_COST;
			} else {
				gcost = parent.gcost + DIAGONAL_COST + DANGER_COST;
			}
		}

		//
		public String toString() {
			return "(" + x + ", " + y + " : " + super.toString() + ')';
		}
	}

	private static final CostComparator COST_CMP = new CostComparator();

	private final int VERTICAL_COST = 10;

	private final int HORIZONTAL_COST = 10;

	private final int DIAGONAL_COST = (int) Math.rint(Math.sqrt(VERTICAL_COST
			* VERTICAL_COST + HORIZONTAL_COST * HORIZONTAL_COST));

	private final Map map;
	private final Node origin;
	private final Node destination;

	private final Queue<Node> open;
	private final Queue<Node> openreach;

	private final int[] closed;
	ArtificialIntelligence ai;
	
	public Astar(Map map, int originX, int originY, int destinationX,
			int destinationY, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		assert map != null : "map = " + map;

		this.map = map;
		destination = new Node(destinationX, destinationY,ai);
		origin = new Node(originX, originY,ai);

		open = new PriorityQueue<Node>(Math.max(map.width, map.height) * 2,
				COST_CMP);
		openreach = new PriorityQueue<Node>(
				Math.max(map.width, map.height) * 2, COST_CMP);

		closed = new int[(map.width * map.height >> 5) + 1];

	}

	/**
	 * Adds the node at {@code x}, {@code y} to the open list, using {@code
	 * parent} as the parent.
	 * 
	 * <p>
	 * If the node was already added to the open list, the old value is either
	 * kept or replaced, depending on whether the old one is closer to the
	 * origin or not.
	 * </p>
	 * 
	 * @param x
	 * @param y
	 * @param parent
	 * @throws StopRequestException 
	 */
	private void addToOpen(int x, int y, Node parent) throws StopRequestException {
		ai.checkInterruption();
		Node openNode = new Node(x, y, ai);
		openNode.setParent(parent);

		replacing: for (Iterator<Node> i = open.iterator(); i.hasNext();) {
			ai.checkInterruption();
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

	private void addToOpenreach(int x, int y, Node parent) throws StopRequestException {
		ai.checkInterruption();
		Node openNode = new Node(x, y,ai);
		openNode.setParent(parent);

		replacing: for (Iterator<Node> i = open.iterator(); i.hasNext();) {
			ai.checkInterruption();
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
		openreach.add(openNode);
	}

	/**
	 * Starts the algorithm and returns true if a valid path was found.
	 * 
	 * @return
	 * @throws StopRequestException 
	 */
	public boolean findPath() throws StopRequestException {
		ai.checkInterruption();
		Node current = origin;
		while (current != null
				&& (current.x != destination.x || current.y != destination.y)) {
			ai.checkInterruption();
			process(current);
			current = open.poll();
		}

		if (current != null) {
			destination.setParent(current.parent);
		}

		return current != null;
		// return true;
	}

	public Deque<Integer> getPath() throws StopRequestException {
		ai.checkInterruption();
		assert destination.parent != null
				|| (destination.x == origin.x && destination.y == origin.y);

		Deque<Integer> path = new LinkedList<Integer>();
		Node current = destination;

		while (current != null) {
			ai.checkInterruption();
			path.addFirst(current.y);
			path.addFirst(current.x);
			current = current.parent;
		}

		return path;
	}

	public boolean findPathreach() throws StopRequestException {
		ai.checkInterruption();
		Node current = origin;
		while (current != null
				&& (current.x != destination.x || current.y != destination.y)) {
			ai.checkInterruption();
			process_reach(current);
			current = openreach.poll();
		}

		if (current != null) {
			destination.setParent(current.parent);
		}

		return current != null;
		// return true;
	}

	/**
	 * Checks whether a node was already processed.
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @throws StopRequestException 
	 */
	private boolean isClosed(int x, int y) throws StopRequestException {
		ai.checkInterruption();
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
	 * @throws StopRequestException 
	 */
	private void process(Node node) throws StopRequestException {
		ai.checkInterruption();
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
			ai.checkInterruption();
			for (int y = lY; y <= uY; ++y) {
				ai.checkInterruption();
				if (!isClosed(x, y) && map.isWalkable(x, y)
						&& (c == x || cy == y)) {
					addToOpen(x, y, node);
				}
			}
		}
	}

	private void process_reach(Node node) throws StopRequestException {
		ai.checkInterruption();
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
			ai.checkInterruption();
			for (int y = lY; y <= uY; ++y) {
				ai.checkInterruption();
				if (!isClosed(x, y) && map.isReachable(x, y)
						&& (c == x || cy == y)) {
					addToOpenreach(x, y, node);
				}
			}
		}
	}

	/**
	 * Sets the node at {@code x}, {@code y} to "already been processed".
	 * 
	 * @param x
	 * @param y
	 * @throws StopRequestException 
	 */
	private void setClosed(int x, int y) throws StopRequestException {
		ai.checkInterruption();
		int i = map.width * y + x;
		closed[i >> 5] |= (1 << (i & 31));
	}
}
