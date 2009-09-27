package fr.free.totalboumboum.ai.adapter200910.path;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import java.util.ArrayList;

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;

/**
 * Repr�sente un noeud dans l'arbre de recherche A* 
 */
public class AstarNode
{	
	/**
	 * Constructeur cr�ant un noeud racine non visit�. 
	 * Les calculateurs pass�s en param�tres seront utilis�s
	 * dans l'arbre entier (i.e. pour tous les autre noeuds)
	 * 
	 * @param tile	case associ�e � ce noeud de recherche
	 * @param costCalculator	fonction de cout
	 * @param heuristicCalculator	fonction heuristique
	 */
	public AstarNode(AiTile tile, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator)
	{	// case
		this.tile = tile;
		// parent
		parent = null;
		// profondeur
		depth = 0;
		// cout
		this.costCalculator = costCalculator;
		cost = 0;
		// heuristique
		this.heuristicCalculator = heuristicCalculator;
		heuristic = heuristicCalculator.processHeuristic(tile);
		
	}

	/**
	 * Constructeur cr�ant un noeud non visit�, fils du noeud
	 * pass� en param�tre. 
	 * @param tile	case associ�e � ce noeud de recherche
	 * @param parent	noeud de recherche parent de ce noeud
	 */
	public AstarNode(AiTile tile, AstarNode parent)
	{	// case
		this.tile = tile;
		// parent
		this.parent = parent;
		// profondeur
		depth = parent.getDepth() + 1;
		// cout
		costCalculator = parent.getCostCalculator();
		double localCost = costCalculator.processCost(parent.getTile(),tile);
		cost = parent.getCost() + localCost;
		// heuristique
		heuristicCalculator = parent.getHeuristicCalculator();
		heuristic = heuristicCalculator.processHeuristic(tile);
		
	}

    /////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case associ�e au noeud */
	private AiTile tile = null;
	
	/**
	 * Renvoie la case associ�e au noeud de recherche.
	 * @return	une case
	 */
	public AiTile getTile()
	{	return tile;
	}

    /////////////////////////////////////////////////////////////////
	// DEPTH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** profondeur du noeud dans l'arbre de recherche */
	private int depth = 0;
	
	/**
	 * Renvoie la profondeur du noeud de recherche dans l'arbre de recherche. 
	 * @return	un entier
	 */
	public int getDepth()
	{	return depth;
	}

	/////////////////////////////////////////////////////////////////
	// COST				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** co�t du noeud (calcul� depuis la racine) */
	private double cost = 0;
	/** calculateur de co�t */
	private CostCalculator costCalculator;
	
	/**
	 * Renvoie le co�t du noeud calcul� depuis la racine. 
	 * @return	le co�t
	 */
	public double getCost()
	{	return cost;
	}
	
	public CostCalculator getCostCalculator()
	{	return costCalculator;		
	}
	
	/////////////////////////////////////////////////////////////////
	// HEURISTIC		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** heuristique du noeud */
	private double heuristic = 0;
	/** calculateur de l'heuristique */
	private HeuristicCalculator heuristicCalculator;
	
	/**
	 * Renvoie l'heuristique du noeud 
	 * @return	l'heuristique
	 */
	public double getHeuristic()
	{	return heuristic;
	}
	
	public HeuristicCalculator getHeuristicCalculator()
	{	return heuristicCalculator;		
	}
		
	/////////////////////////////////////////////////////////////////
	// VISITED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** �tat de visite */
	private boolean visited = false;
	
	/**
	 * Marque le noeud comme ayant �t� visit�
	 */
	protected void markVisited()
	{	visited = true;	
	}
	
	/**
	 * Renvoie l'�tat de visite du noeud
	 * @return	vrai si le noeud a �t� visit�.
	 */
	public boolean wasVisited()
	{	return visited;	
	}

    /////////////////////////////////////////////////////////////////
	// PARENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** parent du noeud (null pour la racine) */
	private AstarNode parent = null;
	
	public AstarNode getParent()
	{	return parent;	
	}
	
    /////////////////////////////////////////////////////////////////
	// CHILDREN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** fils du noeud */
	private final ArrayList<AstarNode> children = new ArrayList<AstarNode>();
	
	public ArrayList<AstarNode> getChildren()
	{	return children;
	}
	
    /////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object object)
	{	return object == this;		
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie une repr�sentation textuelle du noeud. 
	 * @return	la repr�sentation textuelle du noeud
	 */
	public String toString()
	{	String result;
		result = "<";
		result = result + "("+tile.getLine()+","+tile.getCol()+") ";
		result = result + depth + ";";
		result = result + cost + ";";
		result = result + heuristic + " ";
		result = result + ">";
		return result;
	}
}
