package org.totalboumboum.ai.v200708.ais.caglayanelmas;


/**
 * La classe qui représente un noeud de recherche.
 * 
 * @author Ozan Çağlayan
 * @author Arif Can Elmas
 *
 */
public class SearchNode
{	
	private int[] state;
	private int depth;
	private int iteration;
	private double cost;
	private double heuristic;
	private boolean visited;

	/**
	 * Constructeur de la classe SearchNode.
	 * 
	 * @param state		l'état du noeud
	 * @param depth		la profondeur du noeud
	 * @param cost		le cout du noeud
	 * @param heuristic	l'heuristic du noeud
	 */
	public SearchNode(int[] state, int depth, double cost, double heuristic)
	{	
		this.state = state;
		this.depth = depth;

		this.cost = cost;
		this.heuristic = heuristic;
		
		this.iteration = -1;
		visited = false;
	}

	/**
	 * @return l'état du noeud
	 */
	public int[] getState()
	{	
		return this.state;
	}
	
	/**
	 * @return l'heuristic du noeud
	 */
	public double getHeuristic()
	{
		return this.heuristic;
	}

	/**
	 * @return la profondeur du noeud
	 */
	public int getDepth()
	{	
		return depth;
	}

	/**
	 * @return le cout du noeud
	 */
	public double getCost()
	{	
		return cost;
	}

	/**
	 * Marque la visite pendant l'iteration "i"
	 * @param i 
	 */
	protected void markVisited(int i)
	{	
		visited = true;	
		this.iteration = i;
	}

	/**
	 * @return vrai si le noeud est déjà visité.
	 */
	public boolean isVisited()
	{	
		return visited;	
	}

	/**
	 * @return l'itération pendant laquelle le noeud est visité.
	 */
	public int getIteration()
	{	
		return iteration;
	}
	
	public boolean equals(Object object)
	{
		SearchNode sn = (SearchNode) object;
		return ((sn.getState()[0] == this.state[0]) && (sn.getState()[1] == this.state[1]));
	}
}
