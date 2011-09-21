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
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiState;
import org.totalboumboum.ai.v201112.adapter.data.AiStateName;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.AiModel;
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
	protected DybrefNode(ArtificialIntelligence ai, AiHero hero) throws StopRequestException
	{	// ia
		this.ai = ai;
		// hero
		this.hero = hero;
		
		// case courante
		this.tile = hero.getTile();
		// zone courante
		this.zone = tile.getZone();
		
		// parent
		parent = null;
		// profondeur
		depth = 0;
		
		// durée
		duration = 0;
		// durée totale
		totalDuration = 0;
		// matrice des temps d'accès
		matrix = new DybrefMatrix(hero);
	}

	/**
	 * Constructeur créant un noeud fils du noeud passé en paramètre. 
	 * 
	 * @param tile	
	 * 		case associée à ce noeud de recherche
	 * @param parent	
	 * 		noeud de recherche parent de ce noeud
	 */
	protected DybrefNode(AiTile tile, long duration, DybrefNode parent) throws StopRequestException
	{	// ia
		this.ai = parent.getAi();
		// hero
		this.hero = parent.getHero();
		
		// zone
		this.zone = tile.getZone();
		// case
		this.tile = tile;
		
		// parent
		this.parent = parent;
		// profondeur
		depth = parent.getDepth() + 1;
		
		// durée
		this.duration = duration; 
		// durée totale
		totalDuration = parent.getTotalDuration() + duration;
		// matrice des temps d'accès
		matrix = parent.getMatrix();
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
	// DURATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** durée nécessaire pour passer du parent à ce noeud */
	private long duration = 0;
	/** durée totale nécessaire pour atteindre ce noeud */
	private long totalDuration = 0;
	
	/**
	 * Renvoie la ddurée nécessaire pour passer du parent 
	 * à ce noeud de recherche. 
	 * 
	 * @return	
	 * 		Un entier représentant une durée exprimée en ms.
	 */
	public long getDuration()
	{	return duration;
	}

	/**
	 * Renvoie la durée totale nécessaire pour atteindre 
	 * ce noeud de recherche. 
	 * 
	 * @return	
	 * 		Un entier représentant une durée exprimée en ms.
	 */
	public long getTotalDuration()
	{	return totalDuration;
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
	// SAFETY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** état courant du noeud */
	private boolean safe = false;
	
	public void reportSafety(DybrefNode child)
	{	// update processed children count
		processedChildren++;
		
		// update safety
		if(child.isSafe())
			safe = true;
		
		// possibly report to parent (when all children have been processed)
		if(processedChildren==children.size())
		{	matrix.update(this);
			reportSafety(this);
		}
		
		// remove unsafe child
		if(!child.isSafe())
			children.remove(child);
	}

	/**
	 * Indique si ce noeud est sûr, i.e.
	 * si le personnage peut y aller sans danger.
	 * 
	 * @return
	 * 		Renvoie {@code true} si ce noeud est sûr.
	 */
	public boolean isSafe()
	{	return safe;
	}
	
    /////////////////////////////////////////////////////////////////
	// TIME MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice contenant les chemins les plus courts pour accéder à chaque cases */
	private DybrefMatrix matrix = null;
	
	/**
	 * Renvoie la matrice contenant les temps d'accès
	 * aux cases.
	 * 
	 * @return	
	 * 		Une matrice de {@code DybrefNode}.
	 */
	public DybrefMatrix getMatrix()
	{	return matrix;
	}

    /////////////////////////////////////////////////////////////////
	// CHILDREN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste des fils du noeud */
	private List<DybrefNode> children = null;
	/** nombre de fils ayant été traités */
	private int processedChildren = 0;
	
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
		
System.out.println("ORIGINAL ("+depth+")");
System.out.println(zone);
	
	
		children = new ArrayList<DybrefNode>();
//		if(tile.isCrossableBy(hero))
		{	List<Direction> directions = Direction.getPrimaryValues();
			directions.add(Direction.NONE);
			
			// pour chaque déplacement possible (y compris l'attente)
			for(Direction direction: directions)
			{	// on récupère la case cible
				AiTile targetTile = tile.getNeighbor(direction);
				
				// on applique le modèle pour obtenir la zone résultant de l'action
				AiModel model = new AiModel(zone);
				model.applyChangeHeroDirection(hero,direction);
				boolean safe = true;
				long duration = 0;
				if(direction==Direction.NONE)
				{	// on attend simplement jusqu'à la prochaine explosion
					// car c'est généralement l'évènement bloquant
					// de plus, on n'attend que si une des cases voisines est menacée par une explosion
					long waitDuration = getWaitDuration();
					if(waitDuration>0 && waitDuration<Long.MAX_VALUE)
					{	//model.simulateUntilFire();
						model.simulate(waitDuration);
						duration = model.getDuration();
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
				}
				else
				{	// on ne considère le retour en arrière que si on vient d'attendre 
					// (à l'action précédente) i.e. : si cette case et celle de son parent sont les mêmes.
					if(parent==null || !targetTile.equals(parent.getTile()))
					{	// on ne considère les cases déjà visitées que s'il reste des bombes ou du feu en jeu
						// ou sinon, si le temps courant permet d'en améliorer le chemin.
						if(!zone.getFires().isEmpty() || !zone.getBombs().isEmpty()
							|| matrix.getTime(targetTile)>totalDuration+hero.getWalkingSpeed()*tile.getSize()/1000)
						{	// on simule jusqu'au changement d'état du personnage : 
							// soit le changement de case, soit l'élimination
							safe = model.simulate(hero);
							duration = model.getDuration();
						}
					}
				}
	
				// si le joueur est encore vivant dans cette zone et si l'action a bien eu lieu
				if(safe && duration>0)
				{	// on récupère la nouvelle case occupée par le personnage
					AiZone futureZone = model.getCurrentZone();
					AiHero futureHero = futureZone.getHeroByColor(hero.getColor());
					AiTile futureTile = futureHero.getTile();
					
					// on teste si l'action a bien réussi : s'agit-il de la bonne case ?
					if(futureTile.equals(targetTile))
					{	// on crée le noeud fils correspondant (qui sera traité plus tard)
						DybrefNode node = new DybrefNode(futureTile,duration,this);
// TODO adapter les coms de l'exception
// TODO revoir tous les coms des classes du package dybref
						children.add(node);
System.out.println("DIRECTION: "+direction+" >> added");
					}
					// si la case n'est pas la bonne : 
					// la case ciblée n'était pas traversable et l'action est à ignorer
System.out.println("DIRECTION: "+direction);
System.out.println(futureZone);
				}
				// si le joueur n'est plus vivant dans la zone obtenue : 
				// la case ciblée n'est pas sûre et est ignorée
			}
			
			// s'il n'y a aucun enfant pour ce noeud (i.e. on ne peut même pas y rester)
			// alors la case courante n'est pas sûre et on en informe le noeud parent.
			if(children.isEmpty())
				parent.reportSafety(this);
		}
System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
	}
	
	private long getWaitDuration()
	{	// init
		long result = Long.MAX_VALUE;
		List<AiTile> neighbors = tile.getNeighbors();
		
		// is there a fire in the neighbor tiles?
		List<AiFire> fires = zone.getFires();
		for(AiFire fire: fires)
		{	AiTile fireTile = fire.getTile();
			if(neighbors.contains(fireTile))
			{	long duration = fire.getBurningDuration() - fire.getState().getTime();
				if(duration<result)
					result = duration;
			}
		}
		
		// is one of the neighbor tiles within the range of some bomb?
		if(fires.isEmpty())
		{	List<AiBomb> bombs = zone.getBombs();
			for(AiBomb bomb: bombs)
			{	List<AiTile> blast = bomb.getBlast();
				List<AiTile> neigh = new ArrayList<AiTile>(neighbors);
				neigh.retainAll(blast);
				if(!neigh.isEmpty())
				{	AiStateName stateName = bomb.getState().getName();
					long normalDuration = bomb.getNormalDuration();
					// we can ignore burning bombs, since there's already fire in the tile
					if(stateName.equals(AiStateName.STANDING)
					// we can also ignore non-time bombs, since there's no mean to predict when they're going to explode
						&& normalDuration>0)
					{	long duration = normalDuration - bomb.getState().getTime() + bomb.getExplosionDuration();
						if(duration<result)
							result = duration;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Détermine si la case anciennement occupée est menacée par
	 * du feu ou une bombe.
	 * 
	 * @return
	 * 		{@code true} si la case parent est menacée.
	 */
	@SuppressWarnings("unused")
	private boolean isParentTileThreatened()
	{	// init
		boolean result = false;
		AiTile parentTile = parent.getTile();
		
		// is there a fire in the parent tile?
		{	List<AiFire> fires = zone.getFires();
			Iterator<AiFire> it = fires.iterator();
			while(it.hasNext() && !result)
			{	AiFire fire = it.next();
				AiTile fireTile = fire.getTile();
				result = parentTile.equals(fireTile);
			}
		}
		
		// is the parent tile within the range of some bomb?
		if(!result)
		{	List<AiBomb> bombs = zone.getBombs();
			Iterator<AiBomb> it = bombs.iterator();
			while(it.hasNext() && !result)
			{	AiBomb bomb = it.next();
				List<AiTile> blast = bomb.getBlast();
				result = blast.contains(parentTile);
			}
		}
		
		return result;
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
		long duration2 = node.getDuration();
    	if(duration>duration2)
    		result = +1;
    	else if(duration<duration2)
    		result = -1;
    	else // if(duration==duration2)
    		result = depth - node.getDepth();
    	return result;
    }

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result;
		result = "<";
		result = result + "("+tile.getRow()+","+tile.getCol()+") ";
		result = result + depth + ";";
		result = result + duration + ";";
		result = result + totalDuration + " ";
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
		parent = null;
		hero = null;
		tile = null;
		zone = null;
	}
}
