package org.totalboumboum.ai.v200910.adapter.path.astar;

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
import java.util.PriorityQueue;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.successor.SuccessorCalculator;

/**
 * 
 * Implémentation de l'algorithme A* (http://fr.wikipedia.org/wiki/Algorithme_A*) adapté au
 * cas où on a le choix entre plusieurs objectifs alternatifs. S'il y a un seul objectif, 
 * cette implément correspond à peu près à un A* classique. Il y a quand même une modification,
 * puisque les noeuds d'état apparaissant déjà dans des noeuds de recherche ancêtre sont
 * écartés lorsqu'un noeud de recherche est développé. En d'autres termes, l'algorithme évite
 * de chercher des chemins qui passent plusieurs fois par la même case, ce qui l'empêche de
 * boucler à l'infini.
 * <br/>
 * Cette implément trouved donc le chemin le plus court entre deux cases,
 * en considérant les obstacles. Elle a besoin de trois paramètres :
 * 		- le personnage qui doit effectuer le trajet entre les deux cases
 * 		- une fonction de coût, qui permet de définir combien coute une action (ici : le fait de passer d'une case à l'autre)
 * 		- une fonction heuristique, qui permet d'estimer le cout du chemin restant à parcourir
 * <br/>
 * A noter qu'il s'agit d'une implément non-déterministe de l'algorithme.
 * Cela signifie que la méthode renverra toujours le chemin optimal (i.e. le plus court par
 * rapport au cout défini), mais s'il existe plusieurs solutions optimales, l'algorithme ne
 * renverra pas forcément toujours la même (il en choisira une au hasard).
 * Le but est d'introduire une part de hasard dans les IA, de manière à les rendre moins prévisibles.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class Astar
{	/** Numéro de série */	
	private static boolean verbose = false;

	/**
	 * ?
	 * 
	 * @param ai
	 * 		?	
	 * @param hero
	 * 		?	
	 * @param costCalculator
	 * 		?	
	 * @param heuristicCalculator
	 * 		?	
	 */
	public Astar(ArtificialIntelligence ai, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator)
	{	this(ai,hero,costCalculator,heuristicCalculator,new BasicSuccessorCalculator());
	}
	
	/**
	 * ?
	 * 
	 * @param ai
	 * 		?	
	 * @param hero
	 * 		?	
	 * @param costCalculator
	 * 		?	
	 * @param heuristicCalculator
	 * 		?	
	 * @param successorCalculator
	 * 		?	
	 */
	public Astar(ArtificialIntelligence ai, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator, SuccessorCalculator successorCalculator)
	{	this.ai = ai;
		this.hero = hero;
		this.costCalculator = costCalculator;
		this.heuristicCalculator = heuristicCalculator;
		this.successorCalculator = successorCalculator;
	}

    /////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** fonction de cout */
	private CostCalculator costCalculator = null;
	/** fonction heuristique */
	private HeuristicCalculator heuristicCalculator = null;
	/** fonction successeur */
	private SuccessorCalculator successorCalculator = null;
	/** racine de l'arbre de recherche */
	private AstarNode root = null;
	/** personnage de référence */
	private AiHero hero = null;
	/** l'ai qui a réalisé l'appel */
	private ArtificialIntelligence ai = null;

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de hauteur (négatif = pas de limite) */
	private int maxHeight = -1;
	/** limite de coût (négatif = pas de limite) */
	private int maxCost = -1;
	/** limite de nombre de noeuds (négatif = pas de limite), pas configurable */
	private int maxNodes = 10000;
	
	/**
	 * Limite l'arbre de recherche à une hauteur de maxHeight,
	 * i.e. quand le noeud courant a une profondeur correspondant à maxHeight,
	 * l'algorithme se termine et ne renvoie pas de solution (échec).
	 * Dans des cas extrêmes, l'arbre peut avoir une hauteur considérable,
	 * ce qui peut provoquer un dépassement mémoire. Ce paramètre permet d'éviter
	 * de déclencher ce type d'exception. A noter qu'un paramètre non-configurable
	 * limite déjà le nombre de noeuds dans l'arbre.
	 * 
	 * @param maxHeight
	 * 		?	
	 */
	public void setMaxHeight(int maxHeight)
	{	this.maxHeight = maxHeight;	
	}
		
	/**
	 * Limite l'arbre de recherche à un certain cout maxCost, i.e. Dès que le
	 * noeud courant atteint ce cout maximal, l'algorithme se termine et ne
	 * renvoie pas de solution (échec)
	 * Dans des cas extrêmes, l'arbre peut avoir une hauteur considérable,
	 * ce qui peut provoquer un dépassement mémoire. Ce paramètre permet d'éviter
	 * de déclencher ce type d'exception. A noter qu'un paramètre non-configurable
	 * limite déjà le nombre de noeuds dans l'arbre.
	 * 
	 * @param maxCost	le cout maximal que le noeud courant peut atteindre
	 */
	public void setMaxCost(int maxCost)
	{	this.maxCost = maxCost;
	}
		
	/**
	 * Limite l'arbre de recherche à un certain nombre de noeuds. Dès que le
	 * noeud courant atteint cette limite, l'algorithme se termine et ne
	 * renvoie pas de solution (échec).
	 * La valeur ne peut pas être supérieure à celle définie par défaut
	 * (10000), auquel cas une IllegalArgumentException est renvoyée.
	 * 
	 * @param maxNodes	le cout maximal que le noeud courant peut atteindre
	 */
	public void setMaxNodes(int maxNodes)
	{	if(maxNodes<=10000)
			this.maxNodes = maxNodes;
		else
			throw new IllegalArgumentException("It is forbidden to set a node number limit larger than the default value.");
	}
		
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Calcule le plus court chemin pour aller de la case startTile à 
	 * la case endTile, en utilisant l'algorithme A*. Si jamais aucun
	 * chemin n'est trouvé, alors un chemin vide est renvoyé. Si jamais
	 * l'algorithme atteint une limite de cout/taille, la valeur null est
	 * renvoyée. Dans ce cas là, c'est qu'il y a généralement un problème
	 * dans le façon dont A* est employé (mauvaise fonction de cout, par
	 * exemple). 
	 * 
	 * @param startTile	la case de départ
	 * @param endTile	la case d'arrivée
	 * @return un chemin pour aller de startTile à endTile, ou un chemin vide, ou la valeur null
	 * 
	 * @throws StopRequestException 
	 * 		?	
	 */
	public AiPath processShortestPath(AiTile startTile, AiTile endTile) throws StopRequestException
	{	List<AiTile> endTiles = new ArrayList<AiTile>();
		endTiles.add(endTile);
		AiPath result = processShortestPath(startTile,endTiles);
		return result;
	}
	
	/**
	 * Calcule le plus court chemin pour aller de la case startTile à 
	 * une des cases contenues dans la liste endTiles (n'importe laquelle),
	 * en utilisant l'algorithme A*. Si jamais aucun chemin n'est trouvé 
	 * alors un chemin vide est renvoyé. Si jamais l'algorithme atteint 
	 * une limite de cout/taille, la valeur null est renvoyée. Dans ce 
	 * cas-là, c'est qu'il y a généralement un problème dans le façon 
	 * dont A* est employé (mauvaise fonction de cout, par exemple).
	 * La fonction renvoie également null si la liste endTiles est vide.
	 * 
	 * @param startTile	la case de départ
	 * @param endTiles	la liste des cases d'arrivée possibles
	 * @return un chemin pour aller de startTile à une des cases de endTiles, ou un chemin vide, ou la valeur null
	 * 
	 * @throws StopRequestException 
	 * 		?	
	 */
	public AiPath processShortestPath(AiTile startTile, List<AiTile> endTiles) throws StopRequestException
	{	if(verbose)
		{	System.out.print("A*: from "+startTile+" to [");
			for(AiTile tile: endTiles)
				System.out.print(" "+tile);
			System.out.println(" ]");
		}		
		int maxh = 0;
		double maxc = 0;
		int maxn = 0;
		queueMaxSize = 0;
			
		// initialisation
		boolean found = false;
		boolean limitReached = false;
		AiPath result = new AiPath();
		heuristicCalculator.setEndTiles(endTiles);
		root = new AstarNode(ai,startTile,hero,costCalculator,heuristicCalculator,successorCalculator);
		PriorityQueue<AstarNode> queue = new PriorityQueue<AstarNode>(1);
		queue.offer(root);
		AstarNode finalNode = null;
	
		// traitement
		if(!endTiles.isEmpty())
		{	do
			{	ai.checkInterruption();
				queueMaxSize = Math.max(queueMaxSize,queue.size());
				// on prend le noeud situé en tête de file
				AstarNode currentNode = queue.poll();
				if(verbose)
				{	System.out.println("Visited : "+currentNode.toString());
					System.out.println("Queue length: "+queue.size());
				}
				// on teste si on est arrivé à la fin de la recherche
				if(endTiles.contains(currentNode.getTile()))
				{	// si oui on garde le dernier noeud pour ensuite pouvoir reconstruire le chemin solution
					finalNode = currentNode;
					found = true;
				}
				// si l'arbre a atteint la hauteur maximale, on s'arrête
				else if(maxHeight>0 && currentNode.getDepth()>=maxHeight)
					limitReached = true;
				// si le noeud courant a atteint le cout maximal, on s'arrête
				else if(maxCost>0 && currentNode.getCost()>=maxCost)
					limitReached = true;
				// si le nombre de noeuds dans la file est trop grand, on s'arrête
				else if(maxNodes>0 && queue.size()>=maxNodes)
					limitReached = true;
				else
				{	// sinon on récupére les noeuds suivants
					List<AstarNode> successors = new ArrayList<AstarNode>(currentNode.getChildren());
					// on introduit du hasard en permuttant aléatoirement les noeuds suivants
					// pour cette raison, cette implément d'A* ne renverra pas forcément toujours le même résultat :
					// si plusieurs chemins sont optimaux, elle renverra un de ces chemins (pas toujours le même)
					Collections.shuffle(successors);
					// puis on les rajoute dans la file de priorité
					for(AstarNode node: successors)
						queue.offer(node);
				}
				// verbose
				if(currentNode.getDepth()>maxh)
					maxh = currentNode.getDepth();
				if(currentNode.getCost()>maxc)
					maxc = currentNode.getCost();
				if(queue.size()>maxn)
					maxn = queue.size();
			}
			while(!queue.isEmpty() && !found && !limitReached);
		
			// build solution path
			if(found)
			{	while(finalNode!=null)
				{	AiTile tile = finalNode.getTile();
					result.addTile(0,tile);
					finalNode = finalNode.getParent();
				}
			}
		}
		
		if(verbose)
		{	System.out.print("Path: [");
			if(limitReached)
				System.out.println(" limit reached");
			else if(found)
			{	for(AiTile t: result.getTiles())
					System.out.print(" "+t);
			}
			else //if(endTiles.isEmpty())
				System.out.println(" endTiles parameter empty");
			System.out.println(" ]");
			//
			System.out.print("height="+maxh+" cost="+maxc+" size="+maxn);
			System.out.print(" src="+root.getTile());
			if(!endTiles.isEmpty()) 
				System.out.println(" trgt="+endTiles.get(endTiles.size()-1));
			if(result!=null) 
				System.out.print(" result="+result);
			System.out.println();
		}

		finish();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Maximal size of the queue during the last search */
	private int queueMaxSize = 0;
	
	/**
	 * Returns the maximal size of
	 * the queue during the last search.
	 * 
	 * @return
	 * 		Queue max size.
	 */
	public int getQueueMaxSize()
	{	return queueMaxSize;
	}

	/**
	 * 
	 */
	private void finish()
	{	if(root!=null)
		{	root.finish();
			root = null;
		}
	}
}
