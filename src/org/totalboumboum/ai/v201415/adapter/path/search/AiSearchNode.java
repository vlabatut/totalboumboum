package org.totalboumboum.ai.v201415.adapter.path.search;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.Collections;
import java.util.List;

import org.totalboumboum.ai.v201415.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201415.adapter.data.AiHero;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.ai.v201415.adapter.data.AiZone;
import org.totalboumboum.ai.v201415.adapter.path.AiLocation;
import org.totalboumboum.ai.v201415.adapter.path.AiPath;
import org.totalboumboum.ai.v201415.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201415.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201415.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Représente un noeud dans l'arbre de recherche développé par 
 * un algorithme de recherche dans l'espace d'états, tel
 * que A*. 
 * 
 * @author Vincent Labatut
 */
public final class AiSearchNode// implements Comparable<AiSearchNode>
{	
	/**
	 * Constructeur créant un noeud racine non visité. 
	 * Les calculateurs passés en paramètres seront utilisés
	 * dans l'arbre entier (i.e. pour tous les autre noeuds)
	 * 
	 * @param ai	
	 * 		Agent utilisant ce noeud de recherche.
	 * @param location	
	 * 		Emplacement associé à ce noeud de recherche.
	 * @param hero	
	 * 		Personnage dont on veut déterminer le déplacement.
	 * @param costCalculator	
	 * 		Fonction de coût.
	 * @param heuristicCalculator	
	 * 		Fonction heuristique.
	 * @param successorCalculator 
	 * 		Fonction successeur.
	 */
	public AiSearchNode(ArtificialIntelligence ai, AiLocation location, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator, SuccessorCalculator successorCalculator)
	{	// agent
		this.ai = ai;
		
		// emplacement
		this.location = location;
		
		// direction utilisée
		this.direction = Direction.NONE;
		
		// hero
		this.hero = hero;
		
		// parent
		parent = null;
		
		// racine locale
		localRoot = this;
		
		// noeuds explorés
//		exploredNodes = new HashMap<AiTile, AiSearchNode>();
//		exploredNodes.put(location.getTile(),this);
		
		// profondeur
		depth = 0;
		
		// coût
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
	 * @param direction
	 * 		Direction de déplacement pour aller du père au fils
	 * 		(peut être {@link Direction#NONE} en cas d'attente.
	 */
	public AiSearchNode(AiLocation location, AiSearchNode parent, Direction direction)
	{	// agent
		this.ai = parent.getAi();
		
		// emplacement
		this.location = location;
		
		// direction utilisée
		this.direction = direction;
		
		// hero
		this.hero = parent.getHero();
		
		// parent
		this.parent = parent;
		
		// racine locale
		localRoot = parent.getLocalRoot();
		
		// noeuds explorés
//		exploredNodes = new HashMap<AiTile, AiSearchNode>(parent.exploredNodes);
//		exploredNodes.put(location.getTile(),this);
		
		// profondeur
		depth = parent.getDepth() + 1;
//if(depth>ai.getZone().getWidth()*ai.getZone().getHeight())
//	System.out.print("");
		
		// coût
		costCalculator = parent.getCostCalculator();
		double localCost = costCalculator.processCost(parent,location);
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
	 */
	public AiSearchNode(long wait, AiZone zone, AiSearchNode parent)
	{	// agent
		this.ai = parent.getAi();
		
		// emplacement
		AiLocation location = parent.getLocation();
		double posX = location.getPosX();
		double posY = location.getPosY();
		this.location = new AiLocation(posX,posY,zone);
		
		// direction utilisée
		this.direction = Direction.NONE;

		// hero
		this.hero = parent.getHero();
		
		// parent
		this.parent = parent;
		
		// racine locale
		localRoot = this;
		
		// noeuds explorés
//		exploredNodes = new HashMap<AiTile, AiSearchNode>();
//		exploredNodes.put(location.getTile(),this);
		
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
	/** Agent ayant invoqué l'algorithme de recherche */
	private ArtificialIntelligence ai = null;
	
	/**
	 * Renvoie l'agent qui a invoqué l'algorithme de recherche.
	 * 
	 * @return
	 * 		La classe principale de l'agent ayant invoqué l'algorithme de recherche.
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
	 * 		Un emplacement.
	 */
	public AiLocation getLocation()
	{	return location;
	}

    /////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Direction utilisée pour aller du père à ce fils */
	private Direction direction = null;
	
	/**
	 * Renvoie la direction utilisée pour 
	 * aller du noeud de recherche père à ce
	 * noeud de recherche fils. Il est possible
	 * que la direction soit  {@link Direction#NONE}
	 * en cas d'attente.
	 * 
	 * @return	
	 * 		la direction associée à ce noeud de recherche.
	 */
	public Direction getDirection()
	{	return direction;
	}

    /////////////////////////////////////////////////////////////////
	// DEPTH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Profondeur du noeud dans l'arbre de recherche */
	private int depth = 0;
	
	/**
	 * Renvoie la profondeur du noeud de recherche 
	 * dans l'arbre de recherche. 
	 * 
	 * @return	
	 * 		Un entier.
	 */
	public int getDepth()
	{	return depth;
	}

	/////////////////////////////////////////////////////////////////
	// COST				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Coût du noeud (calculé depuis la racine) */
	private double cost = 0;
	/** Calculateur de coût */
	private CostCalculator costCalculator;
	
	/**
	 * Renvoie le coût du noeud calculé depuis la racine. 
	 * 
	 * @return	
	 * 		Le coût.
	 */
	public double getCost()
	{	return cost;
	}
	
	/**
	 * Renvoie la fonction de coût de ce noeud.
	 * 
	 * @return 
	 * 		La fonction de cout de ce noeud.
	 */
	public CostCalculator getCostCalculator()
	{	return costCalculator;		
	}
	
	/////////////////////////////////////////////////////////////////
	// HEURISTIC		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Heuristique du noeud */
	private double heuristic = 0;
	/** Calculateur de l'heuristique */
	private HeuristicCalculator heuristicCalculator;
	
	/**
	 * Renvoie l'heuristique du noeud 
	 * 
	 * @return	
	 * 		L'heuristique.
	 */
	public double getHeuristic()
	{	return heuristic;
	}
	
	/**
	 * Renvoie la fonction heuristique de ce noeud.
	 * 
	 * @return 
	 * 		La fonction heuristique de ce noeud.
	 */
	public HeuristicCalculator getHeuristicCalculator()
	{	return heuristicCalculator;		
	}

	/**
	 * Renvoie la case la plus proche parmi les
	 * destinations spécifiées avant le début
	 * de la recherche.
	 * <br/>
	 * La fonction heuristique est utilisée
	 * pour determiner cette proximité. Cette
	 * fonction peut être utile quand l'algorithme
	 * de recherche s'est arrêté avant de trouver une
	 * solution (par exemple si l'arbre de recherche
	 * a atteint la hauteur maximale). Elle permet
	 * de savoir vers quelle case se dirigerait le
	 * chemin en cours de développement, et ainsi
	 * à l'utiliser même s'il n'est pas complet.
	 * 
	 * @return
	 * 		La case de destination la plus proche.
	 */
	public AiTile getClosestDestination()
	{	AiTile result = heuristicCalculator.getClosestEndTile(location);
		return result;
	}
	
	/**
	 * Met à jour toutes les heuristiques (méthode
	 * utilisée en cas de changement de destination).
	 */
	public void updateHeuristic()
	{	// this node
		heuristic = heuristicCalculator.processHeuristic(location);

		// its children
		if(children!=null)
		{	for(AiSearchNode child: children)
				child.updateHeuristic();
		}
	}
		
    /////////////////////////////////////////////////////////////////
	// PARENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Parent du noeud ({@code null} pour la racine) */
	private AiSearchNode parent = null;
	
	/**
	 * Renvoie le parent de ce noeud de recherche.
	 * 
	 * @return	
	 * 		Un noeud de recherche correspondant au parent de ce noeud.
	 */
	public AiSearchNode getParent()
	{	return parent;	
	}
	
    /////////////////////////////////////////////////////////////////
	// LOCAL ROOT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Racine locale de la branche ({@code null} pour la racine locale) */
	private AiSearchNode localRoot = null;
	
	/**
	 * Renvoie la racine locale de la branche
	 * à laquelle appartient ce noeud de recherche.
	 * 
	 * @return	
	 * 		Un noeud de recherche correspondant à la racine locale de ce noeud.
	 */
	public AiSearchNode getLocalRoot()
	{	return localRoot;	
	}
	
    /////////////////////////////////////////////////////////////////
	// INSERTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ordre d'insertion du noeud dans la frange */
	private int insertionOrder = -1;
	
	/**
	 * Renvoie le numéro d'insertion
	 * dans la frange.
	 * <br/>
	 * <b>Note :</b> cette méthode est destinée
	 * à être utilisée par un algorithme de recherche,
	 * et non pas directement par un agent. 
	 * 
	 * @return
	 * 		Ordre dans lequel ce noeud
	 * 		a été inséré dans la frange.
	 */
	public int getInsertionOrder() {
		return insertionOrder;
	}

	/**
	 * Change le numéro d'insertion
	 * dans la frange.
	 * <br/>
	 * <b>Note :</b> cette méthode est destinée
	 * à être utilisée par un algorithme de recherche,
	 * et non pas directement par un agent. 
	 * 
	 * @param insertionOrder
	 * 		Ordre dans lequel ce noeud
	 * 		a été inséré dans la frange.
	 */
	public void setInsertionOrder(int insertionOrder) {
		this.insertionOrder = insertionOrder;
	}	
	
   /////////////////////////////////////////////////////////////////
	// CHILDREN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Calculateur des successeurs */
	private SuccessorCalculator successorCalculator;
	/** Indique si les enfants de ce noeud ont déjà été calculés */ 
	private boolean childrenProcessed = false;
	/** Liste des enfants de ce noeud */
	private final List<AiSearchNode> children = new ArrayList<AiSearchNode>();
	/** Version immuable de la liste des enfants de ce noeud */
	private final List<AiSearchNode> externalChildren = Collections.unmodifiableList(children);
	
	/**
	 * Renvoie la fonction successeur de ce noeud.
	 * 
	 * @return 
	 * 		La fonction successeur de ce noeud.
	 */
	public SuccessorCalculator getSuccessorCalculator()
	{	return successorCalculator;		
	}
	
	/**
	 * Renvoie les fils de ce noeud de recherche.
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Une liste contenant les fils de ce noeud.
	 */
	public List<AiSearchNode> getChildren()
	{	ai.checkInterruption();
		
		if(!childrenProcessed)
		{	childrenProcessed = true;
			List<AiSearchNode> temp = successorCalculator.processSuccessors(this);
			children.addAll(temp);
		}
		
		return externalChildren;
	}
	
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
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	@Override
	public boolean equals(Object object)
	{	boolean result = false;
		if(object instanceof AiSearchNode)
			result = compareTo((AiSearchNode)object)==0;
		return result;
	}
*/
/*	@Override
	public int compareTo(AiSearchNode node)
    {	Double f1 = cost + heuristic;
    	Double f2 = node.getCost() + node.getHeuristic();
    	int result = f1.compareTo(f2);
    	return result;
    }
*/
	/**
	 * Compare ce noeud avec celui passé
	 * en paramètre.
	 * 
	 * @param node
	 * 		Le noeud comparé. 
	 * @return
	 * 		Une valeur positive si ce noeud est considéré comme "supérieur",
	 * 		une valeur négative s'il est considéré comme inférieur, et
	 * 		zéro si les noeuds sont considérés comme "égaux". 
	 * 
	 */
	public int compareScoreTo(AiSearchNode node)
    {	Double f1 = cost + heuristic;
    	Double f2 = node.getCost() + node.getHeuristic();
    	int result = f1.compareTo(f2);
    	return result;
    }

	/////////////////////////////////////////////////////////////////
	// PATH				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Construit, à partir de ce noeud de recherche,
	 * le chemin permettant d'atteindre la case correspondante
	 * à partir de l'emplacement de départ.
	 * 
	 * @return
	 * 		Le chemin permettant d'atteindre ce noeud, dans cet arbre.
	 */
	public AiPath processPath()
	{	AiSearchNode node = this;
		AiSearchNode previousNode = null;
		AiPath result = new AiPath();
	
		while(node!=null)
		{	AiLocation location = node.getLocation();
			AiTile tile = location.getTile();
			
			// case différente
			if(previousNode==null || !tile.equals(previousNode.getLocation().getTile()))
				result.addLocation(0,location);
			
			// même case
			else
			{	long pause = (long)(previousNode.getCost() - node.getCost());
				pause = pause + result.getPause(0);
				result.setPause(0,pause);
			}
			
			// noeud suivant
			previousNode = node;
			node = node.getParent();
		}

//if(result.getLength()>1 && result.getLocation(0).equals(result.getLocation(1)))
//	System.out.print("");
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("<");
		result.append("(");
		result.append(location);
		result.append(") ");
		result.append(depth);
		result.append(";");
		result.append(cost);
		result.append("+");
		result.append(heuristic);
		result.append("=");
		result.append(cost+heuristic);
		result.append(" ");
		result.append(direction);
		result.append(" ");
		result.append(">");
		return result.toString();
	}
	
	/**
	 * Renvoie une représentation textuelle de la
	 * zone associée à ce noeud de recherche.
	 * 
	 * @return
	 * 		Une chaîne de caractères représentant la zone associée à ce noeud.
	 */
	public String getZoneRepresentation()
	{	String result = successorCalculator.getZoneRepresentation(this);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Permet de terminer proprement ce noeud une fois
	 * qu'il n'est plus utilisé.
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
