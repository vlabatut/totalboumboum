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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v201112.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.SuccessorCalculator;

/**
 * implément de l'algorithme A* (http://fr.wikipedia.org/wiki/Algorithme_A*) adapté au
 * cas où on a le choix entre plusieurs objectifs alternatifs. S'il y a un seul objectif, 
 * cette implément correspond à peu près à un A* classique. Il y a quand même une modification,
 * puisque les noeuds d'état apparaissant déjà dans des noeuds de recherche anc�tre sont
 * �cart�s lorsqu'un noeud de recherche est développ�. En d'autres termes, l'algorithme �vite
 * de chercher des chemins qui passent plusieurs fois par la même case, ce qui l'emp�che de
 * boucler à l'infini.</br>
 * 
 * Cette implément trouve donc le chemin le plus court entre deux cases,
 * en consid�rant les obstacles. Elle a besoin de quatre paramètres :<ul>
 * 		<li> Le personnage qui doit effectuer le trajet entre les deux cases (nécessaire afin de tester la traversabilit� des cases).</li>
 * 		<li> Une fonction successeur, qui définit les actions possibles à partir d'un état donn�. Dans le cas prèsent, il s'agit de 
 * 			 restreindre les déplacement possibles en consid�rant des facteurs supplémentaires par rapport à la simple traversabilit� courrante.</li>
 * 		<li> Une fonction de coût, qui permet de définir combien coûte une action (ici : le fait de passer d'une case à l'autre).</li>
 * 		<li> Une fonction heuristique, qui permet d'estimer le coût du chemin restant à parcourir.</li></ul>
 * 
 * A noter qu'il s'agit d'une implément non-déterministe de l'algorithme.
 * Cela signifie que la méthode renverra toujours le chemin optimal (i.e. le plus court par
 * rapport au cout défini), mais s'il existe plusieurs solutions optimales, l'algorithme ne
 * renverra pas forcément toujours la même (il en choisira une au hasard).
 * Le but est d'introduire une part de hasard dans les IA, de mani�re à les rendre moins pr�visibles.
 * 
 * @author Vincent Labatut
 *
 */
public final class Astar
{	private static boolean verbose = false;

	/**
	 * construit un objet permettant d'appliquer l'algorithme A*
	 * en utilisant la fonction successeur définie par défaut.
	 * 
	 * @param ai
	 * 		l'AI invoquant A*
	 * @param hero
	 * 		le personnage à consid�rer pour les déplacements
	 * @param costCalculator
	 * 		la fonction de coût
	 * @param heuristicCalculator
	 * 		la fonction heuristique
	 */
	public Astar(ArtificialIntelligence ai, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator)
	{	this(ai,hero,costCalculator,heuristicCalculator,new BasicSuccessorCalculator());
	}
	
	/**
	 * construit un objet permettant d'appliquer l'algorithme A*.
	 * 
	 * @param ai
	 * 		l'AI invoquant A*
	 * @param hero
	 * 		le personnage à consid�rer pour les déplacements
	 * @param costCalculator
	 * 		la fonction de coût
	 * @param heuristicCalculator
	 * 		la fonction heuristique
	 * @param successorCalculator
	 * 		la fonction successeur
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
	/** l'ai qui a r�alis� l'appel */
	private ArtificialIntelligence ai = null;

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de hauteur (n�gatif = pas de limite) */
	private int maxHeight = -1;
	/** limite de coût (n�gatif = pas de limite) */
	private double maxCost = -1;
	/** limite de nombre de noeuds (n�gatif = pas de limite), pas configurable */
	private int maxNodes = 10000;
	
	/**
	 * limite l'arbre de recherche à une hauteur de maxHeight,
	 * i.e. quand le noeud courant a une profondeur correspondant à maxHeight,
	 * l'algorithme se termine et ne renvoie pas de solution (�chec).
	 * Dans des cas extr�mes, l'arbre peut avoir une hauteur consid�rable,
	 * ce qui peut provoquer un d�passement m�moire. Ce paramètre permet d'�viter
	 * de d�clencher ce type d'exception. A noter qu'un paramètre non-configurable
	 * limite déjà le nombre de noeuds dans l'arbre.
	 * 
	 * @param maxHeight
	 */
	public void setMaxHeight(int maxHeight)
	{	this.maxHeight = maxHeight;	
	}
		
	/**
	 * limite l'arbre de recherche à un certain cout maxCost, i.e. d�s que le
	 * noeud courant atteint ce cout maximal, l'algorithme se termine et ne
	 * renvoie pas de solution (�chec)
	 * Dans des cas extr�mes, l'arbre peut avoir une hauteur consid�rable,
	 * ce qui peut provoquer un d�passement m�moire. Ce paramètre permet d'�viter
	 * de d�clencher ce type d'exception. A noter qu'un paramètre non-configurable
	 * limite déjà le nombre de noeuds dans l'arbre.
	 * 
	 * @param maxCost	
	 * 		le cout maximal que le noeud courant peut atteindre
	 */
	public void setMaxCost(int maxCost)
	{	this.maxCost = maxCost;
	}
		
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * calcule le plus court chemin pour aller de la case startTile à 
	 * la case endTile, en utilisant l'algorithme A*. Si jamais aucun
	 * chemin n'est trouv�, alors un chemin vide est renvoy�. Si jamais
	 * l'algorithme atteint une limite de cout/taille, la valeur null est
	 * renvoy�e. Dans ce cas l�, c'est qu'il y a généralement un probl�me
	 * dans le fa�on dont A* est employ� (mauvaise fonction de cout, par
	 * exemple). 
	 * 
	 * @param startTile	
	 * 		la case de départ
	 * @param endTile	
	 * 		la case d'arrivée
	 * @return 
	 * 		un chemin pour aller de startTile à endTile, ou un chemin vide, ou la valeur null
	 * @throws StopRequestException 
	 * @throws LimitReachedException 
	 */
	public AiPath processShortestPath(AiTile startTile, AiTile endTile) throws StopRequestException, LimitReachedException
	{	List<AiTile> endTiles = new ArrayList<AiTile>();
		endTiles.add(endTile);
		AiPath result = processShortestPath(startTile,endTiles);
		return result;
	}
	
	/**
	 * calcule le plus court chemin pour aller de la case startTile à 
	 * une des cases contenues dans la liste endTiles (n'importe laquelle),
	 * en utilisant l'algorithme A*. Si jamais aucun chemin n'est trouv� 
	 * alors un chemin vide est renvoy�. Si jamais l'algorithme atteint 
	 * une limite de cout/taille, la valeur null est renvoy�e. Dans ce 
	 * cas-l�, c'est qu'il y a généralement un probl�me dans le fa�on 
	 * dont A* est employ� (mauvaise fonction de cout, par exemple).
	 * La fonction renvoie �galement null si la liste endTiles est vide.
	 * 
	 * @param startTile	
	 * 		la case de départ
	 * @param endTiles	
	 * 		la liste des cases d'arrivée possibles
	 * @return 
	 * 		un chemin pour aller de startTile à une des cases de endTiles, ou un chemin vide, ou la valeur null
	 * @throws StopRequestException 
	 * @throws LimitReachedException 
	 */
	public AiPath processShortestPath(AiTile startTile, List<AiTile> endTiles) throws StopRequestException, LimitReachedException
	{	if(verbose)
		{	System.out.print("A*: from "+startTile+" to [");
			for(AiTile tile: endTiles)
				System.out.print(" "+tile);
			System.out.println(" ]");
		}		
		int maxh = 0;
		double maxc = 0;
		int maxn = 0;

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
				// on prend le noeud situ� en t�te de file
				AstarNode currentNode = queue.poll();
				if(verbose)
				{	System.out.println("Visited : "+currentNode.toString());
					System.out.println("Queue length: "+queue.size());
				}
				// on teste si on est arriv� à la fin de la recherche
				if(endTiles.contains(currentNode.getTile()))
				{	// si oui on garde le dernier noeud pour ensuite pouvoir reconstruire le chemin solution
					finalNode = currentNode;
					found = true;
				}
				// si l'arbre a atteint la hauteur maximale, on s'arr�te
				else if(maxHeight>0 && currentNode.getDepth()>=maxHeight)
					limitReached = true;
				// si le noeud courant a atteint le cout maximal, on s'arr�te
				else if(maxCost>0 && currentNode.getCost()>=maxCost)
					limitReached = true;
				// si le nombre de noeuds dans la file est trop grand, on s'arr�te
				else if(maxNodes>0 && queue.size()>=maxNodes)
					limitReached = true;
				else
				{	// sinon on r�cup�re les noeuds suivants
					List<AstarNode> successors = new ArrayList<AstarNode>(currentNode.getChildren());
					// on introduit du hasard en permuttant al�atoirement les noeuds suivants
					// pour cette raison, cette implément d'A* ne renverra pas forcément toujours le même r�sultat :
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
				System.out.print(" trgt="+endTiles.get(endTiles.size()-1));
			if(result!=null) 
				System.out.print(" result="+result);
			System.out.println();
		}

		finish();
		if(limitReached)
			throw new LimitReachedException(startTile,endTiles,maxh,maxc,maxn,maxCost,maxHeight,maxNodes);
		else if(endTiles.isEmpty())
			throw new IllegalArgumentException("endTiles list must not be empty");
		
		// finish path
		if(startTile.equals(hero.getTile()))
		{	double startX = hero.getPosX();
			double startY = hero.getPosY();
			result.setStart(startX,startY);
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement cet objet quand il n'est plus utilisé
	 */
	private void finish()
	{	if(root!=null)
		{	root.finish();
			root = null;
		}		
	}
}
