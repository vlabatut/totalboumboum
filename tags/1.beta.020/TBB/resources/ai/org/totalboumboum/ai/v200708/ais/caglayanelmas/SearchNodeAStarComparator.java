package org.totalboumboum.ai.v200708.ais.caglayanelmas;

import java.util.Comparator;

import org.totalboumboum.ai.v200708.ais.caglayanelmas.SearchNode;




/**
 * 
 * Compare deux noeuds de recherche en fonction de leur heuristique et de leur cout.
 * 
 * @author Ozan Caglayan
 *
 */
public class SearchNodeAStarComparator implements Comparator<SearchNode>
{	
	public int compare(SearchNode n1, SearchNode n2)
	{
		double r1 = n1.getHeuristic() + n1.getCost();
		double r2 = n2.getHeuristic() + n2.getCost();
		int result = (int)(r1 - r2);
		if (result == 0)
			return -1;
		else
			return result;
	}
}
