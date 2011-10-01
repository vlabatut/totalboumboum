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
	{	// agent
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
		// cases visitées dans cette branche
		initVisits(false);
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
	{	// agent
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
		// cases visitées dans cette branche
		visits = parent.visits;
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
	// VISITS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si les cases ont été visitées, dans la branche courante */
	private boolean[][] visits;
	
	private void initVisits(boolean safeMode)
	{	int height = zone.getHeight();
		int width = zone.getWidth();
		visits = new boolean[height][width];

		if(safeMode)
		{	for(int i=0;i<height;i++)
				for(int j=0;j<width;j++)
				{	AiTile tile = zone.getTile(i,j);
					visits[i][j] = !tile.isCrossableBy(hero);
				}
		}
		else
		{	for(int i=0;i<height;i++)
				for(int j=0;j<width;j++)
					visits[i][j] = false;
		}
		visits[tile.getRow()][tile.getCol()] = true;
	}
	
/*	private boolean isAllVisited()
	{	boolean result = true;
		int height = zone.getHeight();
		int width = zone.getWidth();
		int i = 0;
		while(result && i<height)
		{	int j=0;
			while(result && j<width)
			{	result = visits[tile.getRow()][tile.getCol()];
				j++;
			}
			i++;
		}
		return result;
	}
*/	
	private boolean hasBeenVisited(AiTile tile)
	{	boolean result = visits[tile.getRow()][tile.getCol()];
		return result;
	}
	
    /////////////////////////////////////////////////////////////////
	// SAFETY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** état courant du noeud */
	private boolean safe = false;
	
	/**
	 * Méthode appelée par un noeud
	 * dont la sûreté vient juste d'être fixée.
	 * Cette méthode se charge de transmettre
	 * l'information au parent.
	 */
	public void reportSafety()
	{	// mise à jour de la matrice si nécessaire
		if(safe)
		{	matrix.update(this);
			visits[tile.getRow()][tile.getCol()] = true;
		}
		// transmission au noeud parent
		if(parent!=null)
			parent.safetyReported(this);
	}

	/**
	 * methode appellée par un fils qui veut
	 * avertir son parent que sa sûreté a été
	 * fixée.
	 * 
	 * @param child
	 */
	private void safetyReported(DybrefNode child)
	{	// safe child
		if(child.isSafe())
		{	// node previously unsafe
			if(!safe)
			{	safe = true;
				matrix.update(this);
				visits[tile.getRow()][tile.getCol()] = true;
				if(parent!=null)
					parent.safetyReported(this);
			}
		}
		
		// unsafe child last and node still unsafe
		else
		{	// remove unsafe child
			children.remove(child);
			// last child and node already unsafe
			if(parent!=null && children.isEmpty() && !safe)
				parent.safetyReported(this);
		}
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
	 * teste si le perso est toujours vivant
	 * @param hero
	 * @return
	 */
	private boolean isHeroAlive(AiHero hero)
	{	boolean result = hero!=null;
		if(result)
		{	AiStateName name = hero.getState().getName();
			result = !name.equals(AiStateName.BURNING) && !name.equals(AiStateName.ENDED);
		}
		return result;
	}
	
	/**
	 * utilise la fonction successeur pour calculer les enfants de ce noeud de recherche,
	 * i.e. pour déterminer quelles sont les cases que l'on peut atteindre à partir
	 * de la case courante.
	 * 
	 * @throws StopRequestException 
	 */

//TODO adapter les coms de l'exception
//TODO revoir tous les coms des classes du package dybref
//TODO pb en fait : il est possible de devoir repasser sur la même case, dans la même branche
//si par ex on s'est réfugié dans un coin en attendant qu'une bombe explose... ah ben non, si on attend c'est ok (nvelle branche)
//mais le recoin peut être un cul de sac : on peut quand même y aller, or l'algo va dire que non...

// TODO bidouiller une IA bidon dans le seul but d'afficher le temps trouvés par le nouveau pathfinding
	/**
	 * NOTE on fait l'hypothèse que pas de mur mobile
	 */
	private void developNodeSafeMode() throws StopRequestException
	{	ai.checkInterruption();
		
		// on modifie la sûreté et on valide auprès du père
		safe = true;
		reportSafety();
		
		// init
		children = new ArrayList<DybrefNode>();
		// on ne considère que les déplacement (pas besoin d'attente en mode sûr)
		List<Direction> directions = Direction.getPrimaryValues();
		
		// pour chaque déplacement possible
		for(Direction direction: directions)
		{	ai.checkInterruption();
			// on récupère la case cible
			AiTile targetTile = tile.getNeighbor(direction);
			
			// la case cible doit être traversable
			// NOTE : à modifier si on veut tenir compte de murs mobiles
			if(targetTile.isCrossableBy(hero) 
					// et également ne pas avoir déjà été visitée dans cette branche
					&& !hasBeenVisited(targetTile))
			{	// on applique le modèle pour obtenir la zone résultant de l'action
				AiModel model = new AiModel(zone);
				model.applyChangeHeroDirection(hero,direction);
				model.simulate(hero);
				long duration = model.getDuration();
				
				// on récupère la nouvelle case occupée par le personnage
				AiZone futureZone = model.getCurrentZone();
				AiHero futureHero = futureZone.getHeroByColor(hero.getColor());
				// on teste si le perso est toujours en vie (sinon > bug)
				if(!isHeroAlive(futureHero))
					System.err.println("ERREUR: le perso ne devrait pas pouvoir mourir.");
				AiTile futureTile = futureHero.getTile();
				// on teste si la case occupée est bien celle visée (sinon > bug)
				if(!futureTile.equals(targetTile))
					System.err.println("ERREUR: la case devrait être la même.");
				
				// on ne crée le noeud que si le déplacement n'est pas un retour en arrière
				// >> le fait de considérer la matrice de visite épargne ce test
//				if(parent==null || !targetTile.equals(parent.getTile()))
				{	// temps nécessaire pour aller dans la case cible
//					long alternativeTime = totalDuration + duration;
					// temps déjà présent dans la matrice
//					long existingTime = matrix.getTime(targetTile);
					// on ne crée le noeud que si ce temps est meilleur que le temps existant
					// >> le fait de considérer la matrice de visite épargne ce test
//					if(alternativeTime<existingTime)
					// pb >> on se retrouve dans l'impossibilité de refaire un chemin déjà parcouru
					// alors que parfois c'est bien pratique, quand on doit revenir sur ses pas pour éviter une explosion
					{	// ce temps sera màj dans la matrice lors du développement du noeud
						// on crée le noeud fils correspondant (qui sera traité plus tard)
						DybrefNode node = new DybrefNode(futureTile,duration,this);
						children.add(node);
//System.out.println("DIRECTION: "+direction+" >> added");
					}
				}
				
//System.out.println("DIRECTION: "+direction);
//System.out.println(futureZone);
			}
		}
	}
	
	private void developNodeUnsafeMode() throws StopRequestException
	{	ai.checkInterruption();
		
		// init
		children = new ArrayList<DybrefNode>();
		// on considère les déplacement, mais aussi l'attente
		List<Direction> directions = Direction.getPrimaryValues();
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
			// simulation pour l'attente
			if(direction==Direction.NONE)
			{	// on attend simplement jusqu'à la prochaine explosion car c'est généralement l'évènement bloquant
				// de plus, on n'attend que si une des cases voisines est menacée par une explosion
				long waitDuration = getWaitDuration();
				if(waitDuration>0 && waitDuration<Long.MAX_VALUE)
				{	model.simulate(waitDuration);
					duration = model.getDuration();
					AiZone futureZone = model.getCurrentZone();
					AiHero futureHero = futureZone.getHeroByColor(hero.getColor());
					// on teste si le perso est toujours en vie
					safe = isHeroAlive(futureHero);
				}
			}
			// simulation pour le déplacement
			else
			{	// restriction : on ne considère le retour en arrière que si on vient d'attendre 
				// (à l'action précédente) i.e. : la case cible et celle de son parent doivent être différentes
				// >> le fait de considérer la matrice de visite épargne ce test
//				if(parent==null || !targetTile.equals(parent.getTile()))
				// la case ne doit pas avoir déjà été visitée dans cette branche
				if(!hasBeenVisited(targetTile))
				{	// on simule jusqu'au changement d'état du personnage : 
					// soit le changement de case, soit l'élimination
					safe = model.simulate(hero);
					duration = model.getDuration();
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
					children.add(node);
					// en cas d'attente, on crée une nouvelle branche
					if(direction==Direction.NONE)
					{	boolean safeMode = futureZone.getFires().isEmpty() && futureZone.getBombs().isEmpty();
						node.initVisits(safeMode);
					}
//System.out.println("DIRECTION: "+direction+" >> added");
				}
				// si la case n'est pas la bonne : 
				// la case ciblée n'était pas traversable et l'action est à ignorer
				
//System.out.println("DIRECTION: "+direction);
//System.out.println(futureZone);
			}
			// si le joueur n'est plus vivant dans la zone obtenue : 
			// la case ciblée n'est pas sûre et est ignorée
			
			// s'il n'y a aucun enfant pour ce noeud, on en informe le noeud parent
			if(children.isEmpty())
				reportSafety();
		}
	}
	
	private void developNode() throws StopRequestException
	{	ai.checkInterruption();
		
		// safe mode: quand il ne reste ni bombe ni feu dans la zone, le traitement peut être simplifié
		boolean safeMode = zone.getFires().isEmpty() && zone.getBombs().isEmpty();
//System.out.println("++++++ ORIGINAL (d="+depth+", mode="+safeMode+", t="+totalDuration+"ms) ++++++++++++++++++++++++++++++++++++++++++");
//System.out.println(matrix);
//System.out.println(zone);
		if(safeMode)
			developNodeSafeMode();
		else
			developNodeUnsafeMode();
	}
	
	private long getWaitDuration() throws StopRequestException
	{	ai.checkInterruption();
		
		// init
		long result = Long.MAX_VALUE;
		List<AiTile> neighbors = tile.getNeighbors();
		
		// is there a fire in the neighbor tiles?
		List<AiFire> fires = zone.getFires();
		for(AiFire fire: fires)
		{	ai.checkInterruption();
			AiTile fireTile = fire.getTile();
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
			{	ai.checkInterruption();
				List<AiTile> blast = bomb.getBlast();
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
	 * @throws StopRequestException 
	 */
	@SuppressWarnings("unused")
	private boolean isParentTileThreatened() throws StopRequestException
	{	ai.checkInterruption();
		
		// init
		boolean result = false;
		AiTile parentTile = parent.getTile();
		
		// is there a fire in the parent tile?
		{	List<AiFire> fires = zone.getFires();
			Iterator<AiFire> it = fires.iterator();
			while(it.hasNext() && !result)
			{	ai.checkInterruption();
				AiFire fire = it.next();
				AiTile fireTile = fire.getTile();
				result = parentTile.equals(fireTile);
			}
		}
		
		// is the parent tile within the range of some bomb?
		if(!result)
		{	List<AiBomb> bombs = zone.getBombs();
			Iterator<AiBomb> it = bombs.iterator();
			while(it.hasNext() && !result)
			{	ai.checkInterruption();
				AiBomb bomb = it.next();
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
