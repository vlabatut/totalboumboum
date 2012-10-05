package org.totalboumboum.ai.v200708.ais.ciritmutlu.tree;

import org.totalboumboum.ai.v200708.ais.ciritmutlu.problem.State;


/**
 * Represente un noeud dans un arbre de recherche, caracterise par :
 * un etat du probleme, une profondeur, un cout, un etat de visite 
 * et un numero unique d'iteration (qui indique e quelle iteration
 * ce noeud a ete visite par l'algorithme). 
 */
public class SearchNode
{	// etat du probleme associe au noeud
	private State state;
	// cout du noeud (calcule depuis la racine)
	private double cost;
	// iteration de visite (ou -1 si le noeud n'a pas encore ete visite)
	private int iteration;

	/**
	 * Constructeur creant un noeud non visite, d'iteration -1, caracterise par
	 * les donnees passees en parametres. 
	 * @param state	etat du probleme associe au noeud de recherche
	 * @param cost	coet calcule depuis la racine
	 */
	public SearchNode(State state,double cost)
	{	this.state = state;
		this.cost = cost;
		this.iteration = -1;
	}

	/**
	 * Renvoie l'etat associe au noeud de recherche.
	 * @return	l'etat
	 */
	public State getState()
	{	return state;
	}

	/**
	 * Renvoie le coet du noeud calcule depuis la racine. 
	 * @return	le coet
	 */
	public double getCost()
	{	return cost;
	}
	

	/**
	 * Renvoie l'iteration a laquelle le noeud a ete visite
	 * (ou -1 s'il n'a jamais ete visite). 
	 * @return	l'iteration de visite
	 */
	public int getIteration()
	{	return iteration;
	}
	
	public boolean equals(Object object)
	{	return object == this;		
	}
}
