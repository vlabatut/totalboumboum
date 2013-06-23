package org.totalboumboum.ai.v200708.ais.baydarcamci;

import java.util.List;

/**
 * A PathFinder can find a path between two indexed nodes in a Map.
 * 
 * @author Ayca Selva Baydar
 * @author Ayfer Camci
 *
 */
public interface PathFinder {

	/**
	 * Find the best path through the Map from a start node to an end node.
	 * After calling findPath() you need to call nextStep() repeatedly until
	 * nextStep() returns SEARCH_STATE_SUCCEEDED or SEARCH_STATE_FAILED.
	 * 
	 * The path found will be returned in path.
	 * 
	 * @param start The start node state
	 * @param end The end node state
	 * @param path Contains the path through the nodes
	 */
	public void findPath(int[] start, int [] end, List<int[]> path);
	
	/**
	 * Perform the next step in the search.
	 * @return SEARCH_STATE_SUCCEEDED if the search has found a path
	 * @return SEARCH_STATE_FAILED if no path could be found
	 * @return SEARCH_STATE_INVALID if the search is not complete yet
	 * @return SEARCH_STATE_NOT_INITIALIZED if you forgot to call findPath()
	 */
	public int nextStep();
	
	// Search states
	public static final int SEARCH_STATE_SUCCEEDED = 1;
	public static final int SEARCH_STATE_FAILED = 2;
	public static final int SEARCH_STATE_NOT_INITIALIZED = 3;
	public static final int SEARCH_STATE_INVALID = 4;
	public static final int SEARCH_STATE_CANCELLED = 5;
	public static final int SEARCH_STATE_SEARCHING = 6;

}