package org.totalboumboum.ai.v201112.adapter.path.dybref;

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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiState;
import org.totalboumboum.ai.v201112.adapter.data.AiStateName;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.AiModel;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Représente un noeud dans l'arbre de recherche développé par l'algorithme A* 
 * 
 * @author Vincent Labatut
 *
 */
public final class DybrefNode implements Comparable<DybrefNode>
{	
	/**
	 * Constructeur créant un noeud racine non visité. 
	 * Les calculateurs passés en paramètres seront utilisés
	 * dans l'arbre entier (i.e. pour tous les autre noeuds)
	 * 
	 * @param tile	
	 * 		case associée à ce noeud de recherche
	 * @param costCalculator	
	 * 		fonction de cout
	 * @param heuristicCalculator	
	 * 		fonction heuristique
	 */
	protected DybrefNode(ArtificialIntelligence ai, AiZone zone, AiTile tile, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator, SuccessorCalculator successorCalculator) throws StopRequestException
	{	// ia
		this.ai = ai;
		// hero
		this.hero = hero;
		
		// zone courante
		this.zone = zone;
		// case courante
		this.tile = tile;
		
		// parent
		parent = null;
		// profondeur
		depth = 0;
		// successeur
		this.successorCalculator = successorCalculator;
		
		// pause
		// durée totale
	}

	/**
	 * Constructeur créant un noeud fils du noeud passé en paramètre. 
	 * 
	 * @param tile	
	 * 		case associée à ce noeud de recherche
	 * @param parent	
	 * 		noeud de recherche parent de ce noeud
	 */
	protected DybrefNode(AiZone zone, AiTile tile, DybrefNode parent) throws StopRequestException
	{	// ia
		this.ai = parent.getAi();
		// hero
		this.hero = parent.getHero();
		
		// zone
		this.zone = zone;
		// case
		this.tile = tile;
		
		// parent
		this.parent = parent;
		// profondeur
		depth = parent.getDepth() + 1;
		// successeurs
		successorCalculator = parent.getSuccessorCalculator();
		
		// pause
		// durée totale
	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** IA ayant invoqué A* */
	private ArtificialIntelligence ai = null;
	
	/**
	 * renvoie l'IA qui a invoqué A*
	 * 
	 * @return
	 * 		la classe principale de l'IA ayant invoqué A*
	 */
	public ArtificialIntelligence getAi()
	{	return ai;	
	}
	
    /////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case associée au noeud */
	private AiTile tile = null;
	
	/**
	 * Renvoie la case associée au noeud de recherche.
	 * 
	 * @return	
	 * 		une case
	 */
	public AiTile getTile()
	{	return tile;
	}

    /////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** zone associée au noeud */
	private AiZone zone = null;
	
	/**
	 * Renvoie la zone associée au noeud de recherche.
	 * 
	 * @return	
	 * 		une zone
	 */
	public AiZone getZone()
	{	return zone;
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
	// PARENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** parent du noeud (null pour la racine) */
	private DybrefNode parent = null;
	
	/**
	 * renvoie le parent de ce noeud de recherche
	 * 
	 * @return	
	 * 		un noeud de recherche correspondant au parent de ce noeud
	 */
	public DybrefNode getParent()
	{	return parent;	
	}
	
    /////////////////////////////////////////////////////////////////
	// CHILDREN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** fils du noeud */
	private List<DybrefNode> children = null;
	/** calculateur des successeurs */
	private DybrefSuccessorCalculator successorCalculator;
	
	/**
	 * renvoie la fonction successeur de ce noeud
	 * 
	 * @return 
	 * 		la fonction successeur de ce noeud
	 */
	public DybrefSuccessorCalculator getSuccessorCalculator()
	{	return successorCalculator;		
	}
	
	/**
	 * renvoie les fils de ce noeud de recherche
	 * (ils sont éventuellement calculés si ce n'est pas déjà fait)
	 * 
	 * @return	
	 * 		une liste contenant les fils de ce noeud
	 * @throws StopRequestException 
	 */
	public List<DybrefNode> getChildren() throws StopRequestException
	{	if(children==null)
			developNode();
		return children;
	}
	
	/**
	 * utilise la fonction successeur pour calculer les enfants de ce noeud de recherche,
	 * i.e. pour déterminer quelles sont les cases que l'on peut atteindre à partir
	 * de la case courante.
	 * 
	 * @throws StopRequestException 
	 */
	private void developNode() throws StopRequestException
	{	ai.checkInterruption();
		
		children = new ArrayList<DybrefNode>();
		if(tile.isCrossableBy(hero))
		{	List<Direction> directions = Direction.getPrimaryValues();
			directions.add(Direction.NONE);
			
			// pour chaque déplacement possible (y compris l'attente)
			for(Direction direction: directions)
			{	// on récupère la case cible
				AiTile targetTile = tile.getNeighbor(direction);
				
				// on applique le modèle pour obtenir la zone résultant de l'action
				AiModel model = new AiModel(zone);
				model.applyChangeHeroDirection(hero,direction);
				boolean safe;
				if(direction==Direction.NONE)
				{	model.simulateUntilFire();
					AiZone futureZone = model.getCurrentZone();
					AiHero futureHero = futureZone.getHeroByColor(hero.getColor());
					if(futureHero==null)
						safe = false;
					else
					{	AiState futureState = futureHero.getState();
						AiStateName futureName = futureState.getName();
						safe = futureName==AiStateName.FLYING || futureName==AiStateName.MOVING || futureName==AiStateName.STANDING;
					}
				}
				else
					safe = model.simulate(hero);
				long duration = model.getDuration();
	
				// si le joueur est encore vivant dans cette zone et si l'action a bien eu lieu
				if(safe && duration>0)
				{	// on récupère la nouvelle case occupée par le personnage
					AiZone futureZone = model.getCurrentZone();
					AiHero futureHero = futureZone.getHeroByColor(hero.getColor());
					AiTile futureTile = futureHero.getTile();
					
					// on teste si l'action a bien réussi : s'agit-il de la bonne case ?
					if(futureTile.equals(targetTile))
					{	// on crée le noeud fils correspondant (qui sera traité plus tard)
						DybrefNode node = new DybrefNode(futureZone,futureTile,parent);
// TODO faut ajouter le temps. p-ê màj d'autres champs aussi (?)
// TODO tester si la limite temps==0 est prise en cpte dans simulation héro et simulation feu
// TODO optimisation >> on ne considère l'attente que s'il reste du feu ou des bombes dans la case...
						children.add(node);
					}
					// si la case n'est pas la bonne : 
					// la case ciblée n'était pas traversable et l'action est à ignorer
				}
				// si le joueur n'est plus vivant dans la zone obtenue : 
				// la case ciblée n'est pas sûre et est ignorée
			}
			
			// s'il n'y a aucun enfant pour ce noeud (i.e. on ne peut même pas y rester)
			// alors la case courante n'est pas sûre et on en informe le noeud parent.
			parent.childFailed(this);
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
	 * @return	
	 * 		le personnage de référence
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
		if(object instanceof DybrefNode)
			result = compareTo((DybrefNode)object)==0;
		return result;
	}

	@Override
	public int compareTo(DybrefNode node)
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
	@Override
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
	/**
	 * permet de terminer proprement ce noeud une fois
	 * qu'il n'est plus utilisé
	 */
	protected void finish()
	{	// children
		if(children!=null)
		{	while(!children.isEmpty())
			{	DybrefNode n = children.get(0);
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
