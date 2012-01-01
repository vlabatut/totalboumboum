package org.totalboumboum.ai.v201112.adapter.path;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.PriorityQueue;
import java.util.Set;

import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * Exception levée par un algorithme de recherche de chemin 
 * quand il atteint une des limites fixées au préalable.
 * <br/>
 * Pour A*, on ne peut donc pas conclure quant à l'existence d'un chemin solution.
 * En d'autres termes : cette exception indique qu'A* a exploré un arbre bien trop
 * grand, sans pourtant trouver la solution. On ne peut donc pas savoir s'il existe
 * un état final quelque part dans la partie non-explorée de l'arbre, ou bien
 * s'il n'existe pas de solution du tout.
 * <br/>
 * Pour Dybref, cela signifie que l'algorithme n'a pas pu explorer tous les
 * chemins existant. Il est toutefois possible qu'un chemin soit affecté à chaque
 * case, car cet algorithme considère les pauses, et donc est susceptible de trouver
 * plusieurs chemins pour aller à une même case.
 * 
 * @author Vincent Labatut
 */

public final class LimitReachedException extends Exception
{	private static final long serialVersionUID = 1L;

	/**
	 * Crée une exception représentant le fait qu'un algorithme de recherche
	 * a atteint une des limites qui lui étaient fixées sans pour autant
	 * trouver de solution (i.e. de chemin)
	 * 
	 * @param startLocation
	 * 		Emplacment initial.
	 * @param endTiles
	 * 		Cases finales.
	 * @param height
	 * 		Hauteur maximale atteinte pour l'arbre de recherche.
	 * @param cost
	 * 		Coût maximal atteint pour les chemins contenus dans l'arbre de recherche.
	 * @param size
	 * 		Taille atteinte par l'arbre de recherche (exprimée en nombre de noeuds).
	 * @param maxCost
	 * 		Limite de coût fixée pour l'exploration.
	 * @param maxHeight
	 * 		Limite de hauteur fixée pour l'exploration.
	 * @param maxSize
	 * 		Limite de taille (exprimée en nombre de noeuds) fixée pour l'exploration.
	 * @param queue
	 * 		Frange lors de l'arrêt de l'algorithme. 
	 */
	public LimitReachedException(AiLocation startLocation, Set<AiTile> endTiles, int height, double cost, int size, double maxCost, int maxHeight, int maxSize, PriorityQueue<AiSearchNode> queue)
	{	super("The search algorithm developped a tree too costly/deep/large according to the predefined limits.");
		
		this.startLocation = startLocation;
		this.endTiles = endTiles;
		this.height = height;
		this.cost = cost;
		this.size = size;
		this.maxCost = maxCost;
		this.maxHeight = maxHeight;
		this.maxSize = maxSize;
		this.fringe = queue;
	}

	/////////////////////////////////////////////////////////////////
	// START TILES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** case de départ de la recherche (état initial) */
	private AiLocation startLocation;
	
	/**
	 * Renvoie l'emplacement de départ de la recherche.
	 * 
	 * @return
	 * 		Un emplacement de la zone.
	 */
    public AiLocation getStartLocation()
    {	return startLocation;
	}

	/////////////////////////////////////////////////////////////////
	// END TILES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** les cases d'arrivée de la recherche (états finaux) */
    private Set<AiTile> endTiles;
	
    /**
     * renvoie les cases d'arrivée de la recherche
     * (uniquement pour A*)
     * 
     * @return
	 * 		une case de la zone
     */
	public Set<AiTile> getEndTiles()
	{	return endTiles;
	}

	/////////////////////////////////////////////////////////////////
	// HEIGHT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de hauteur (négatif = pas de limite) */
	private int maxHeight;
	/** hauteur maximale atteinte */
	private int height;

	/**
	 * renvoie la limite de hauteur pour l'exploration de l'arbre de recherche.
	 * une valeur négative représente une absence de limite.
	 * 
	 * @return
	 * 		la limite de hauteur pour l'exploration de l'arbre de recherche
	 */
	public int getMaxHeight()
	{	return maxHeight;
	}

	/**
	 * renvoie la hauteur de l'arbre, i.e. la longueur
	 * du chemin le plus long développé lors de la recherche de solution
	 * 
	 * @return
	 * 		hauteur de l'arbre
	 */
	public int getHeight()
	{	return height;
	}

	/**
	 * Indique si l'algorithme a été arrêté parce qu'il
	 * a atteint un arbre de hauteur maximale.
	 * 
	 * @return
	 * 		{@code true} ssi l'arbre de recherche a atteint la hauteur maximale.
	 */
	public boolean hasReachedHeightLimit()
	{	return maxHeight == height;
	}

	/////////////////////////////////////////////////////////////////
	// COST				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de coût (négatif = pas de limite) */
	private double maxCost;
	/** coût maximal atteint */
	private double cost;

	/**
	 * renvoie la limite de coût pour l'exploration de l'arbre de recherche.
	 * une valeur négative représente une absence de limite.
     * (uniquement pour A*)
	 * 
	 * @return
	 * 		la limite de coût pour l'exploration de l'arbre de recherche
	 */
	public double getMaxCost()
	{	return maxCost;
	}

	/**
	 * renvoie le coût du chemin le plus coûteux développé lors de la
	 * recherche de solution par A*.
	 * 
	 * @return
	 * 		coût maximal atteint lors de la recherche de solution
	 */
	public double getCost()
	{	return cost;
	}

	/**
	 * Indique si l'algorithme a été arrêté parce qu'il
	 * a atteint un chemin de coût limite.
	 * 
	 * @return
	 * 		{@code true} ssi l'arbre de recherche a atteint le coût maximal.
	 */
	public boolean hasReachedCostLimit()
	{	return maxCost == cost;
	}

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de taille exprimée en nombre de noeuds (négatif = pas de limite) */
	private int maxSize;
	/** taille atteinte lors de l'exploration */
	private int size;

	/**
	 * renvoie la limite qui avait été fixée pour la taille
	 * de l'arbre de recherche exprimée en noeuds.
	 * 
	 * @return
	 * 		la limite de taille fixée pour l'arbre
	 */
	public int getMaxSize()
	{	return maxSize;
	}

	/**
	 * renvoie la taille de l'arbre développé par A* lors de son exploration,
	 * exprimée en nombre de noeuds.
	 * 
	 * @return
	 * 		la taille de l'arbre exploré
	 */
	public int getSize()
	{	return size;
	}

	/**
	 * Indique si l'algorithme a été arrêté parce qu'il
	 * a atteint un arbre contenant le nombre de noeuds limite.
	 * 
	 * @return
	 * 		{@code true} ssi l'arbre de recherche contient plus de noeuds que la limite autorisée.
	 */
	public boolean hasReachedSizeLimit()
	{	return maxSize == size;
	}

	/////////////////////////////////////////////////////////////////
	// FRINGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la frange à l'instant où l'exception est levée */
	private PriorityQueue<AiSearchNode> fringe;
	
	/**
	 * renvoie la frange de l'algorithme au moment où l'exception est levée.
	 * Cela permet notamment de récupérer la meilleure case du moment
	 * (en termes d'heuristique).
	 * 
	 * @return
	 * 		Une file de priorité correspondant à la frange de l'algorithme de recherche
	 * 		au moment de son interruption forcée.
	 */
	public PriorityQueue<AiSearchNode> getFringe()
	{	return fringe;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String getSummary()
	{	String result = "";
		result = result + "height=" + height + "(" + maxHeight + ") ";
		result = result + "cost="   + cost   + "(" + maxCost   + ") ";
		result = result + "size="   + size   + "(" + maxSize   + ") ";
		result = result + "src="    + startLocation + " ";
		result = result + "trgt=";
		for(AiTile tile: endTiles)
			result = result + tile + " ";
		result = result + "\n";
		return result;
	}
}
