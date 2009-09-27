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
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;
import fr.free.totalboumboum.engine.content.feature.Direction;
/**
 * Représente un noeud dans l'arbre de recherche A* 
 */
public class AstarNode implements Comparable<AstarNode>
{	
	/**
	 * Constructeur créant un noeud racine non visité. 
	 * Les calculateurs passés en paramètres seront utilisés
	 * dans l'arbre entier (i.e. pour tous les autre noeuds)
	 * 
	 * @param tile	case associée à ce noeud de recherche
	 * @param costCalculator	fonction de cout
	 * @param heuristicCalculator	fonction heuristique
	 */
	public AstarNode(AiTile tile, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator)
	{	// case
		this.tile = tile;
		// hero
		this.hero = hero;
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
	 * Constructeur créant un noeud non visité, fils du noeud
	 * passé en paramètre. 
	 * @param tile	case associée à ce noeud de recherche
	 * @param parent	noeud de recherche parent de ce noeud
	 */
	public AstarNode(AiTile tile, AstarNode parent)
	{	// case
		this.tile = tile;
		// hero
		this.hero = parent.getHero();
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
	/** case associée au noeud */
	private AiTile tile = null;
	
	/**
	 * Renvoie la case associée au noeud de recherche.
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
	/** coût du noeud (calculé depuis la racine) */
	private double cost = 0;
	/** calculateur de coût */
	private CostCalculator costCalculator;
	
	/**
	 * Renvoie le coût du noeud calculé depuis la racine. 
	 * @return	le coût
	 */
	public double getCost()
	{	return cost;
	}
	
	/**
	 * renvoie la fonction de cout de ce noeud
	 * 
	 * @return la fonction de cout de ce noeud
	 */
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
	
	/**
	 * renvoie la fonction heuristique de ce noeud
	 * 
	 * @return la fonction heuristique de ce noeud
	 */
	public HeuristicCalculator getHeuristicCalculator()
	{	return heuristicCalculator;		
	}
		
    /////////////////////////////////////////////////////////////////
	// PARENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** parent du noeud (null pour la racine) */
	private AstarNode parent = null;
	
	/**
	 * renvoie le parent de ce noeud de recherche
	 * 
	 * @return	un noeud de recherche correspondant au parent de ce noeud
	 */
	public AstarNode getParent()
	{	return parent;	
	}
	
    /////////////////////////////////////////////////////////////////
	// CHILDREN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** fils du noeud */
	private ArrayList<AstarNode> children = null;
	
	/**
	 * renvoie les fils de ce noeud de recherche
	 * (ils sont éventuellement calculés si ce n'est pas déjà fait)
	 * 
	 * @return	une liste contenant les fils de ce noeud
	 */
	public List<AstarNode> getChildren()
	{	if(children==null)
			developNode();
		return children;
	}
	
	/**
	 * utilise la fonction successeur pour calculer les enfants de ce noeud de recherche,
	 * i.e. pour déterminer quelles sont les cases que l'on peut atteindre à partir
	 * de la case courante.
	 */
	private void developNode()
	{	children = new ArrayList<AstarNode>();
		for(Direction direction: Direction.getPrimaryValues())
		{	AiTile neighbor = tile.getNeighbor(direction);
			if(neighbor.isCrossableBy(hero))
			{	AstarNode node = new AstarNode(neighbor,this);
				children.add(node);			
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// HERO				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** personnage considéré */
	private AiHero hero = null;
	
	/**
	 * renvoie le personnage de référence pour cette recherche
	 * 
	 * @return	le personnage de référence
	 */
	public AiHero getHero()
	{	return hero;	
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object object)
	{	boolean result = false;
		if(object instanceof AstarNode)
			result = compareTo((AstarNode)object)==0;
		return result;
	}

    public int compareTo(AstarNode node)
    {	int result = 0;
		double f1 = cost+heuristic;
    	double f2 = node.getCost()+node.getHeuristic();
    	if(f1>f2)
    		result = +1;
    	else if(f1<f2)
    		result = -1;
    	return result;
    }

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
