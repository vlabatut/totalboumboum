package org.totalboumboum.ai.v201112.adapter.path.astar;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.SuccessorCalculator;

/**
 * Représente un noeud dans l'arbre de recherche développé par l'algorithme A* 
 * 
 * @author Vincent Labatut
 */
public final class AstarNode implements Comparable<AstarNode>
{	
	/**
	 * Constructeur créant un noeud racine non visité. 
	 * Les calculateurs passés en paramètres seront utilisés
	 * dans l'arbre entier (i.e. pour tous les autre noeuds)
	 * 
	 * @param location	
	 * 		Emplacement associé à ce noeud de recherche.
	 * @param costCalculator	
	 * 		Fonction de coût.
	 * @param heuristicCalculator	
	 * 		Fonction heuristique.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected AstarNode(ArtificialIntelligence ai, AiLocation location, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator, SuccessorCalculator successorCalculator) throws StopRequestException
	{	// agent
		this.ai = ai;
		
		// emplacement
		this.location = location;
		
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
		heuristic = heuristicCalculator.processHeuristic(location);
		
		// successeurs
		this.successorCalculator = successorCalculator;
	}

	/**
	 * Constructeur créant un noeud non visité, fils du noeud
	 * passé en paramètre. 
	 * 
	 * @param location	
	 * 		Emplacement associé à ce noeud de recherche.
	 * @param parent	
	 * 		Noeud de recherche parent de ce noeud.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public AstarNode(AiLocation location, AstarNode parent) throws StopRequestException
	{	// agent
		this.ai = parent.getAi();
		
		// case
		this.location = location;
		
		// hero
		this.hero = parent.getHero();
		
		// parent
		this.parent = parent;
		
		// profondeur
		depth = parent.getDepth() + 1;
		
		// coût
		costCalculator = parent.getCostCalculator();
		AiLocation previous = parent.getLocation();
//if(costCalculator==null)
//	System.out.println();
		double localCost = costCalculator.processCost(previous,location);
		cost = parent.getCost() + localCost;
		
		// heuristique
		heuristicCalculator = parent.getHeuristicCalculator();
		heuristic = heuristicCalculator.processHeuristic(location);
		
		// successeurs
		successorCalculator = parent.getSuccessorCalculator();
	}

	/**
	 * Constructeur créant un noeud non visité, fils du noeud
	 * passé en paramètre. L'emplacement est le même que le noeud précédent
	 * et l'action consiste à faire une pause (et non pas un déplacement).
	 * 
	 * @param wait	
	 * 		Pause associée à ce noeud de recherche.
	 * @param zone	
	 * 		Zone correspondante au noeud de recherche.
	 * @param parent	
	 * 		Noeud de recherche parent de ce noeud.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public AstarNode(long wait, AiZone zone, AstarNode parent) throws StopRequestException
	{	// agent
		this.ai = parent.getAi();
		
		// emplacement
		AiLocation location = parent.getLocation();
		double posX = location.getPosX();
		double posY = location.getPosY();
		this.location = new AiLocation(posX,posY,zone);
		
		// hero
		this.hero = parent.getHero();
		
		// parent
		this.parent = parent;
		
		// profondeur
		depth = parent.getDepth() + 1;
		
		// coût
		costCalculator = parent.getCostCalculator();
		cost = parent.getCost() + wait;
		
		// heuristique
		heuristicCalculator = parent.getHeuristicCalculator();
		heuristic = heuristicCalculator.processHeuristic(location);
		
		// successeurs
		successorCalculator = parent.getSuccessorCalculator();
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** agent ayant invoqué A* */
	private ArtificialIntelligence ai = null;
	
	/**
	 * renvoie l'agent qui a invoqué A*
	 * 
	 * @return
	 * 		la classe principale de l'agent ayant invoqué A*
	 */
	public ArtificialIntelligence getAi()
	{	return ai;	
	}
	
    /////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Emplacement associé au noeud */
	private AiLocation location = null;
	
	/**
	 * Renvoie l'emplacement associé au noeud de recherche.
	 * 
	 * @return	
	 * 		Une emplacement.
	 */
	public AiLocation getLocation()
	{	return location;
	}

    /////////////////////////////////////////////////////////////////
	// DEPTH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** profondeur du noeud dans l'arbre de recherche */
	private int depth = 0;
	
	/**
	 * Renvoie la profondeur du noeud de recherche 
	 * dans l'arbre de recherche. 
	 * 
	 * @return	
	 * 		un entier
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
	 * 
	 * @return	
	 * 		le coût
	 */
	public double getCost()
	{	return cost;
	}
	
	/**
	 * renvoie la fonction de cout de ce noeud
	 * 
	 * @return 
	 * 		la fonction de cout de ce noeud
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
	 * 
	 * @return	
	 * 		l'heuristique
	 */
	public double getHeuristic()
	{	return heuristic;
	}
	
	/**
	 * renvoie la fonction heuristique de ce noeud
	 * 
	 * @return 
	 * 		la fonction heuristique de ce noeud
	 */
	public HeuristicCalculator getHeuristicCalculator()
	{	return heuristicCalculator;		
	}
		
    /////////////////////////////////////////////////////////////////
	// PARENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** parent du noeud ({@code null} pour la racine) */
	private AstarNode parent = null;
	
	/**
	 * renvoie le parent de ce noeud de recherche
	 * 
	 * @return	
	 * 		un noeud de recherche correspondant au parent de ce noeud
	 */
	public AstarNode getParent()
	{	return parent;	
	}
	
	/**
	 * Détermine si la case passée en paramètre a déjà été traitée,
	 * i.e. si elle apparait dans les noeuds de recherche ancêtres.
	 * 
	 * @param tile	
	 * 		Case à tester
	 * @return	
	 * 		{@code true} ssi la case a déjà été traitée.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public boolean hasBeenExplored(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		
		boolean result = this.location.equals(tile);
		if(parent!=null && !result)
			result = parent.hasBeenExplored(tile);
		return result;
	}
	
	/**
	 * Détermine si la case passée en paramètre a déjà été traitée,
	 * i.e. si elle apparait dans les noeuds de recherche ancêtres.
	 * On s'arrête dès qu'on rencontre une pause : l'idée est
	 * qu'à la suite d'une pause, il est possible de re-visiter
	 * des cases par lesquelles on est déjà passé, car la pause
	 * correspond à l'attente de la disparition d'un obstacle
	 * tel qu'une explosion (ou un mur mobile, etc.).
	 * 
	 * @param tile	
	 * 		Case à tester
	 * @return	
	 * 		{@code true} ssi la case a déjà été traitée.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public boolean hasBeenExploredSincePause(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		
		boolean result = location.getTile().equals(tile);
		if(!result && parent!=null && !parent.getLocation().equals(location))
			result = parent.hasBeenExploredSincePause(tile);
		return result;
	}
	
	/**
	 * Détermine si la case contenue dans l'emplacement passé en 
	 * paramètre a déjà été traitée, i.e. si elle apparait dans les 
	 * noeuds de recherche ancêtres.
	 * 
	 * @param location
	 * 		Emplacement contenant la case à tester
	 * @return	
	 * 		{@code true} ssi la case a déjà été traitée.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
/*	private boolean hasBeenExplored(AstarLocation location) throws StopRequestException
	{	ai.checkInterruption();
	
		AiTile tile = location.getTile();
		boolean result = this.location.getTile().equals(tile);
		if(parent!=null && !result)
			result = parent.hasBeenExplored(tile);
		return result;
	}
*/	
    /////////////////////////////////////////////////////////////////
	// CHILDREN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** calculateur des successeurs */
	private SuccessorCalculator successorCalculator;
	
	/**
	 * renvoie la fonction successeur de ce noeud
	 * 
	 * @return 
	 * 		la fonction successeur de ce noeud
	 */
	public SuccessorCalculator getSuccessorCalculator()
	{	return successorCalculator;		
	}
	
	/**
	 * renvoie les fils de ce noeud de recherche.
	 * 
	 * @return	
	 * 		une liste contenant les fils de ce noeud
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public List<AstarNode> getChildren() throws StopRequestException
	{	ai.checkInterruption();
	
		List<AstarNode> result = successorCalculator.processSuccessors(this);
//children = result;		
		return result;
	}
//private List<AstarNode> children;	
	
	/////////////////////////////////////////////////////////////////
	// HERO				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le personnage considéré */
	private AiHero hero = null;
	
	/**
	 * Renvoie le personnage de référence pour cette recherche
	 * 
	 * @return	
	 * 		Le personnage de référence.
	 */
	public AiHero getHero()
	{	return hero;	
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object object)
	{	boolean result = false;
		if(object instanceof AstarNode)
			result = compareTo((AstarNode)object)==0;
		return result;
	}

	@Override
	public int compareTo(AstarNode node)
    {	Double f1 = cost + heuristic;
    	Double f2 = node.getCost() + node.getHeuristic();
    	int result = f1.compareTo(f2);
    	return result;
    }

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result;
		result = "<";
		result = result + "("+location+") ";
		result = result + depth + ";";
		result = result + cost + ";";
		result = result + heuristic + " ";
		result = result + ">";
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * permet de terminer proprement ce noeud une fois
	 * qu'il n'est plus utilisé
	 */
	protected void finish()
	{	ai = null;
		costCalculator = null;
		heuristicCalculator = null;
		successorCalculator = null;
		parent = null;
		hero = null;
		location = null;
	}
}
