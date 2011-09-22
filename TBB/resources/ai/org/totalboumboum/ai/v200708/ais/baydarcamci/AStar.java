package org.totalboumboum.ai.v200708.ais.baydarcamci;

import java.util.*;

/**
 * Implements the basic heuristic A* route finding algorithm
 * 
 * @author Ayça Selva Baydar
 * @author Ayfer Camcı
 *
 */
public class AStar implements PathFinder {

	/** The maximum number of nodes that this AStar can traverse. */
	@SuppressWarnings("unused")
	private final int maxNodes;

	/** The current list of open nodes */
	public final NodeList openList;

	/** The current list of closed nodes */
	private final NodeList closedList;
     
	int[][] zoneMatrix = new int[15][13];
	/** The map */
	
	/** A current list of neighbors. Optimized for 2D grids to begin with but it will adjust itself */
	private List<int[]> neighbors = new ArrayList<int[]>(8);

	/** The goal node */
	private Node end;
	
	/** The initial node */
	private Node start;

	/** The number of steps */
	private int steps;
	
	/** The current path */
	public List<int[]> path = new ArrayList<int[]>();
	
	private int[] ownPosition;
    
	/** The current search state */
	private int state  = SEARCH_STATE_NOT_INITIALIZED;
	/**
	 * A class containing a sorted list of nodes.
	 */
	private static class NodeList {
		final Node[] node;
		int count;
		boolean f_sorted;
//		HashMap stateMap = new HashMap();

		NodeList(int size) {
			node = new Node[size];
		}

		// Add a node. Returns false if there's no room.
		boolean addNode(Node newNode) {
			if (count == node.length)
				return false;
			f_sorted = false;
			node[count ++] = newNode;
			
			return true;
		}

		// Remove a node
		void removeNode(int [] ownPosition) {
			int index = findNode(ownPosition);
//			assert index != -1;
			f_sorted = false;
			node[index] = node[--count];
			node[count] = null;
		}

		// Find a node's position by its userstate. Returns -1 if node is not found.
		int findNode(int [] ownPosition) {
			for (int i = 0; i < count; i ++)
				if (node[i].userState.equals(ownPosition))
					return i;
					
			return -1;
		}
		
		// Return a node by its userstate (or null if not present)
		Node getNode(int [] ownPosition) {
			int index = findNode(ownPosition);
			if (index == -1)
				return null;
			else
				return node[index];
		}

		// Empty the list
		void clear() {
			for (int i = 0; i < count; i ++)
				node[i] = null;
			count = 0;
		}

		// How big is the list?
//		int size() {
//			return count;
//		}
		
		// Is the list empty?
		boolean isEmpty() {
			return count == 0;
		}
		
		// Pop and return the head of the list
		Node popHead() {
			if (count > 0 && !f_sorted) {
				Arrays.sort(node, 0, count);
				f_sorted = true;
			}
			Node ret = node[0];
			node[0] = node[--count];
			node[count] = null;
			return ret;
		}

	}

	/**
	 * Our nodes
	 */
	private static class Node {

		Node parent; // used during the search to record the parent of successor nodes
		Node child; // used after the search for the application to view the search in reverse

		int g; // cost of this node + it's predecessors
		@SuppressWarnings("unused")
		int h; // heuristic estimate of distance to goal
//		int f; // sum of cumulative cost of predecessors and self and heuristic

		int [] userState;

		Node(int [] userState, Node parent) {
			this.userState = userState;
			this.parent = parent;
		}

		public boolean equals(Object obj) {
			return ((Node) obj).userState.equals(this.userState);
		}

		/**
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
/*		public int compareTo(Object o) {
			Node n2 = (Node) o;
			if (this == n2)
				return 0;
			if (n2 == null)
				return 1;
			return f > n2.f ? 1 : f == n2.f ? 0 : -1;
		}
*/		
		/**
		 * Duplicate the incoming node
		 */
/*		void dup(Node src) {
			this.g = src.g;
			this.h = src.h;
			this.f = src.f;
		}
*/
	}

	

;
 
	
	
	
	/**
	 * Constructor for AStar.
	 * @param map The map to traverse
	 * @param maxNodes The maximum number of nodes allowed. (For a grid shaped map,
	 * use width * height)
	 */
	public AStar(int zoneMatrix[][], int maxNodes ) {
	   
		this.zoneMatrix = zoneMatrix;
		this.maxNodes = maxNodes;
		openList = new NodeList(maxNodes);
		closedList = new NodeList(maxNodes);
	
	    
	}
	
	public synchronized void findPath(int[] from, int[] to, List<int[]> path) {
		cleanup();
		steps = 0;
		this.path = path;
		state = SEARCH_STATE_SEARCHING;
		start = new Node(from, null);
		openList.addNode(start);
		start.g = 0;
		start.h = getDistance(from, to);
//		start.f = start.h;
		end = new Node(to, null);
		
		
	}

	/**
	 * Cancel the current search
	 */
	public synchronized void cancel() {
		state = SEARCH_STATE_CANCELLED;
		cleanup();
	}
	
	/**
	 * Clean up when a search is finished
	 */
	private void cleanup() {
		openList.clear();
		closedList.clear();
		neighbors.clear();
	}

	
	public synchronized int nextStep() {

		if (state == SEARCH_STATE_FAILED || state == SEARCH_STATE_SUCCEEDED || state == SEARCH_STATE_CANCELLED)
			return state;

//		assert state == SEARCH_STATE_SEARCHING;

		// Failure is defined as emptying the open list as there is nothing left to 
		// search...
		if (openList.isEmpty()) {
			state = SEARCH_STATE_FAILED;
			cleanup();
			
			return state;
		}

		// Incremement step count
		steps++;

		// Pop the best node (the one with the lowest f) : this is at the head of the list
		Node n = openList.popHead(); // get pointer to the node
		
		// Check for the goal, once we pop that we're done
		if (n.equals(end))
			goalFound(n);
		else
			goalNotFound(n);

		return state;
	}
	
	/**
	 * The goal has been found.
	 * @param n The current node
	 */
	private void goalFound(Node n) {			
		
		
		
		end.parent = n.parent;
		
		// A special case is that the goal was passed in as the start state
		// so handle that here
		path.clear();
		if (n != start) {
			// set the child pointers in each node (except Goal which has no child)
			Node nodeChild = n;
			Node nodeParent = n.parent;

			do {
				nodeParent.child = nodeChild;
				nodeChild = nodeParent;
				nodeParent = nodeParent.parent;

			} while (!nodeChild.equals(start)); // Start is always the first node by definition

			// Fill in the path
			n = start;
			do {
				n = n.child;
				if (n != null)
					path.add(n.userState);
				
				
			} while (!n.equals(end));
		}

		state = SEARCH_STATE_SUCCEEDED;
		cleanup();
	}
	
	/**
	 * The goal has not been found.
	 * @param n The current node
	 */
	private void goalNotFound(Node n) {

		// We now need to generate the neighbors of this node. We ask the map for the neighbors.
		// By passing in the parent of the current node we help the map to avoid returning a neighbor
		// that simply backtracks.
	    
		getNeighbors(n.userState, n.parent.userState , neighbors);

		int numNeighbors = neighbors.size();
		for (int i = 0; i < numNeighbors; i ++) {
			int []newState = (int []) neighbors.get(i);
			int newg = n.g + getCost(ownPosition, newState);
			
			// Now find the node on the open or closed lists. If it is on a list
			// already but the node that is already there has a better (lower) g
			// scrore then forget about this neighbor.
			
			// First the open list:
			Node foundOnOpenList = openList.getNode(newState);
			if (foundOnOpenList != null) {
				if (foundOnOpenList.g <= newg) {
					// Already got a node that's cheaper on the open list
					continue;
				}
			}
			
			// Then the closed list:
			Node foundOnClosedList = closedList.getNode(newState);
			if (foundOnClosedList != null) {
				if (foundOnClosedList.g <= newg) {
					// Already got a node that's cheaper on the closed list
					continue;
				}
			}
			
			// This node is the best so far with this particular state, so let's keep it

			// If it was on the closed list, remove it
			if (foundOnClosedList != null)
				closedList.removeNode(foundOnClosedList.userState);
			
			// If it was on the open list, remove it
			if (foundOnOpenList != null)
				openList.removeNode(foundOnOpenList.userState);
				
			// Now add a new node
			Node newNode = new Node(newState, n);
			newNode.g = newg;
			newNode.h = getDistance(newState, end.userState);
//			newNode.f = newNode.g + newNode.h;
			if (!openList.addNode(newNode)) {
				state = SEARCH_STATE_FAILED;
				cleanup();
				break;
			}
		}

		// push n onto Closed, as we have expanded it now
		if (!closedList.addNode(n)) {
			state = SEARCH_STATE_FAILED;
			cleanup();
		}

	}
   
	private int distance(int x1,int y1,int x2,int y2)
	{	int result = 0;
		result = result + Math.abs(x1-x2);
		result = result + Math.abs(y1-y2);
		return result;
	    
	}
   
	public int getDistance(int[] from, int[] to)
	{int distance = distance(from[0],from[1],to[0],to[1]);
	
	
	return distance	;
	}
   
	public int getCost(int[] from, int[] to)
	{  int x = Math.abs(from[0]-from[1]);
	   int y = Math.abs(to[0]-to[1]);
		
		int cost = 10*(x+y);
		
		return cost;
				

		
	}

	public void getNeighbors(int [] node, int [] parent, List<int[]> path)
	{   BaydarCamci bomber = new BaydarCamci();
	    node=ownPosition;
	    bomber.getPossibleMoves(ownPosition[0],ownPosition[1]);
	    path.add(ownPosition);
	    
	}
}
