package org.totalboumboum.ai.v200708.ais.caglayanelmas;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200708.ais.caglayanelmas.SearchLink;
import org.totalboumboum.ai.v200708.ais.caglayanelmas.SearchNode;





/**
 * Module d'IA qui va trouver le plus court chemin
 * entre un case initial et un case final en consid�rant
 * les obstacles. On va utiliser A* avec une heuristique
 * �tant �gale à la distance de Manhattan entre les deux
 * cases.
 * 
 * @author Ozan Caglayan
 * @author Arif Can Elmas
 *
 */
public class PathFinder
{
	// L'objet qui représente notre IA. On utilise
	// cette r�f�rence pour pouvoir appeler
	// les méthodes isObstacle, isMovePossible,
	// applyAction.
	private CaglayanElmas ai;
	
	private Vector<SearchNode> nodes;
	private Vector<SearchLink> links;
	
	private int[] initialState;
	private int[] finalState;
	
	public PathFinder(CaglayanElmas ai)
	{
		this.ai = (CaglayanElmas) ai;
		
		this.nodes = new Vector<SearchNode>();
		this.links = new Vector<SearchLink>();
	}
	
	public void setStates(int[] initialState, int[] finalState)
	{
		this.initialState = initialState.clone();
		this.finalState = finalState.clone();
		
		this.nodes.add(new SearchNode(this.initialState, 0, 10.0, 
									  this.calculateHeuristic(this.initialState, this.finalState)));
	}
	
	public void markVisitedNode(SearchNode node, int iteration)
	{	
		node.markVisited(iteration);
	}
	
	public int calculateHeuristic(int[] s1, int[] s2)
	{
		return 10*(Math.abs(s1[0]-s2[0])+Math.abs(s1[1]-s2[1]));
	}

	private Vector<Integer> getPossibleMoves(int x, int y)
	{	
		Vector<Integer> result = new Vector<Integer>();
		
		for(int move = ArtificialIntelligence.AI_ACTION_GO_UP; move <= ArtificialIntelligence.AI_ACTION_GO_RIGHT; move++)
			if(ai.isMovePossible(x, y, move))
				result.add(move);
		return result;
	}
	
	public Vector<SearchLink> findShortestPath()
	{
		PriorityQueue<SearchNode> fringe = new PriorityQueue<SearchNode>(1, new SearchNodeAStarComparator());
		
		Iterator<SearchLink> sl = null;
		SearchNode sn = null;
		boolean condition = true;
		int iteration = 0;
		
		fringe.offer(nodes.get(0));

		while (condition && !fringe.isEmpty())
		{
			sn = fringe.poll();
			
			markVisitedNode(sn, iteration);
			
			if (!((sn.getState()[0] == finalState[0]) && (sn.getState()[1] == finalState[1])))
			{
				sl = developNode(sn);
				
				while (sl.hasNext())
				{
					SearchNode snt = sl.next().getTarget();
					fringe.offer(snt);
				}
				iteration++;
			}
			
			else
				condition = false;
		}
		return getPath(new SearchNode(finalState,0,0,0));
	}
	
	public boolean containsNode(SearchNode node)
	{	
		boolean result = false;
		Iterator<SearchNode> i = nodes.iterator();
		while(i.hasNext() && !result)
		{
			SearchNode snt = i.next();
			result = (node.getState()[0] == snt.getState()[0]) &&
					 (node.getState()[1] == snt.getState()[1]);
		}
		return result;
	}
	
	public Iterator<SearchLink> developNode(SearchNode node)
	{	
		Vector<SearchLink> result = new Vector<SearchLink>();
		if(!((node.getState()[0] == finalState[0]) && (node.getState()[1] == finalState[1])))
		{	
			Iterator<Integer> i = getPossibleMoves(node.getState()[0], node.getState()[1]).iterator();
			while(i.hasNext())
			{
				Integer action = i.next();
				int[] targetState = ai.applyAction(node.getState()[0], node.getState()[1], action);
				int targetDepth = node.getDepth() + 1;
				double targetCost = node.getCost() + 10;
				double targetHeuristic = this.calculateHeuristic(targetState, finalState);
				SearchNode target = new SearchNode(targetState, targetDepth, targetCost, targetHeuristic);
				
				if (!containsNode(target))
				{
					SearchLink link = new SearchLink(node, target, action);
					result.add(link);
					links.add(link);
					nodes.add(target);
				}
			}
		}
		return result.iterator();
	}
	
	public Vector<SearchLink> getPath(SearchNode node)
	{	
		Vector<SearchLink> result;
		SearchLink parentLink = null;

		if (node != nodes.get(0))
		{
			Iterator<SearchLink> i = links.iterator();
			while(i.hasNext() && parentLink == null)
			{	
				SearchLink temp = i.next();
				if(temp.getTarget().equals(node))
					parentLink = temp;
			}
		}
		
		if(parentLink == null)
			result = new Vector<SearchLink>();
		else
		{	
			result = getPath(parentLink.getOrigin());
			result.add(parentLink);
		}

		return result;
	}
	
}
