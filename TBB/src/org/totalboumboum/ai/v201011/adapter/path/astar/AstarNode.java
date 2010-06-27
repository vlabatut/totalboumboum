package org.totalboumboum.ai.v201011.adapter.path.astar;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;

/**
 * Repr�sente un noeud dans l'arbre de recherche d�velopp� par l'algorithme A* 
 * 
 * @author Vincent Labatut
 *
 */
public final class AstarNode implements Comparable<AstarNode>
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
	protected AstarNode(ArtificialIntelligence ai, AiTile tile, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator, SuccessorCalculator successorCalculator) throws StopRequestException
	{	// ia
		this.ai = ai;
		// case
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
		// successeurs
		this.successorCalculator = successorCalculator;
	}

	/**
	 * Constructeur cr�ant un noeud non visit�, fils du noeud
	 * pass� en param�tre. 
	 * @param tile	case associ�e � ce noeud de recherche
	 * @param parent	noeud de recherche parent de ce noeud
	 */
	protected AstarNode(AiTile tile, AstarNode parent) throws StopRequestException
	{	// ia
		this.ai = parent.getAi();
		// case
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
		// successeurs
		successorCalculator = parent.getSuccessorCalculator();
	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArtificialIntelligence ai = null;
	
	public ArtificialIntelligence getAi()
	{	return ai;	
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
	
	/**
	 * d�termine si la case pass�e en param�tre a d�j� �t� trait�e,
	 * i.e. si elle apparait dans les noeuds de recherche anc�tres
	 * 
	 * @param tile	case � tester
	 * @return	vrai si la case a d�j� �t� trait�e
	 * @throws StopRequestException 
	 */
	private boolean hasBeenExplored(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		
		boolean result = this.tile==tile;
		if(parent!=null && !result)
			result = parent.hasBeenExplored(tile);
		return result;
	}
	
    /////////////////////////////////////////////////////////////////
	// CHILDREN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** fils du noeud */
	private List<AstarNode> children = null;
	/** calculateur des successeurs */
	private SuccessorCalculator successorCalculator;
	
	/**
	 * renvoie la fonction successeur de ce noeud
	 * 
	 * @return la fonction successeur de ce noeud
	 */
	public SuccessorCalculator getSuccessorCalculator()
	{	return successorCalculator;		
	}
	
	/**
	 * renvoie les fils de ce noeud de recherche
	 * (ils sont �ventuellement calcul�s si ce n'est pas d�j� fait)
	 * 
	 * @return	une liste contenant les fils de ce noeud
	 * @throws StopRequestException 
	 */
	public List<AstarNode> getChildren() throws StopRequestException
	{	if(children==null)
			developNode();
		return children;
	}
	
	/**
	 * utilise la fonction successeur pour calculer les enfants de ce noeud de recherche,
	 * i.e. pour d�terminer quelles sont les cases que l'on peut atteindre � partir
	 * de la case courante.
	 * @throws StopRequestException 
	 */
	private void developNode() throws StopRequestException
	{	ai.checkInterruption();
	
		children = new ArrayList<AstarNode>();
		List<AiTile> neighbors = successorCalculator.processSuccessors(this);
		for(AiTile neighbor: neighbors)
		{	// on ne garde pas les �tats qui appartiennent d�j� au chemin contenant le noeud de recherche courant
			// i.e. les �tats qui apparaissent dans des noeuds anc�tres du noeud courant
			if(!hasBeenExplored(neighbor))
			{	AstarNode node = new AstarNode(neighbor,this);
				children.add(node);			
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// HERO				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** personnage consid�r� */
	private AiHero hero = null;
	
	/**
	 * renvoie le personnage de r�f�rence pour cette recherche
	 * 
	 * @return	le personnage de r�f�rence
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

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void finish()
	{	// children
		if(children!=null)
		{	while(!children.isEmpty())
			{	AstarNode n = children.get(0);
				n.finish();
				children.remove(0);
			}
			children = null;
		}
		
		// misc
		ai = null;
		costCalculator = null;
		heuristicCalculator = null;
		successorCalculator = null;
		parent = null;
		hero = null;
		tile = null;
	}
}
