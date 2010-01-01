package org.totalboumboum.ai.adapter200910.path.astar;

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
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import org.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import org.totalboumboum.ai.adapter200910.communication.StopRequestException;
import org.totalboumboum.ai.adapter200910.data.AiHero;
import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.path.AiPath;
import org.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.adapter200910.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.adapter200910.path.astar.successor.SuccessorCalculator;


/**
 * Impl�mentation de l'algorithme A* (http://fr.wikipedia.org/wiki/Algorithme_A*) adapt� au
 * cas o� on a le choix entre plusieurs objectifs alternatifs. S'il y a un seul objectif, 
 * cette impl�mentation correspond � peu pr�s � un A* classique. Il y a quand m�me une modification,
 * puisque les noeuds d'�tat apparaissant d�j� dans des noeuds de recherche anc�tre sont
 * �cart�s lorsqu'un noeud de recherche est d�velopp�. En d'autres termes, l'algorithme �vite
 * de chercher des chemins qui passent plusieurs fois par la m�me case, ce qui l'emp�che de
 * boucler � l'infini.</br>
 * 
 * Cette impl�mentation trouved donc le chemin le plus court entre deux cases,
 * en consid�rant les obstacles. Elle a besoin de trois param�tres :
 * 		- le personnage qui doit effectuer le trajet entre les deux cases
 * 		- une fonction de co�t, qui permet de d�finir combien coute une action (ici : le fait de passer d'une case � l'autre)
 * 		- une fonction heuristique, qui permet d'estimer le cout du chemin restant � parcourir</br>
 * 
 * A noter qu'il s'agit d'une impl�mentation non-d�terministe de l'algorithme.
 * Cela signifie que la m�thode renverra toujours le chemin optimal (i.e. le plus court par
 * rapport au cout d�fini), mais s'il existe plusieurs solutions optimales, l'algorithme ne
 * renverra pas forc�ment toujours la m�me (il en choisira une au hasard).
 * Le but est d'introduire une part de hasard dans les IA, de mani�re � les rendre moins pr�visibles.
 */
public class Astar
{	private static boolean verbose = false;

	public Astar(ArtificialIntelligence ai, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator)
	{	this(ai,hero,costCalculator,heuristicCalculator,new BasicSuccessorCalculator());
	}
	
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
	/** personnage de r�f�rence */
	private AiHero hero = null;
	/** l'ai qui a r�alis� l'appel */
	private ArtificialIntelligence ai = null;

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de hauteur (n�gatif = pas de limite) */
	private int maxHeight = -1;
	/** limite de co�t (n�gatif = pas de limite) */
	private int maxCost = -1;
	/** limite de nombre de noeuds (n�gatif = pas de limite), pas configurable */
	private int maxNodes = 10000;
	
	/**
	 * limite l'arbre de recherche � une hauteur de maxHeight,
	 * i.e. quand le noeud courant a une profondeur correspondant � maxHeight,
	 * l'algorithme se termine et ne renvoie pas de solution (�chec).
	 * Dans des cas extr�mes, l'arbre peut avoir une hauteur consid�rable,
	 * ce qui peut provoquer un d�passement m�moire. Ce param�tre permet d'�viter
	 * de d�clencher ce type d'exception. A noter qu'un param�tre non-configurable
	 * limite d�j� le nombre de noeuds dans l'arbre.
	 * 
	 * @param maxHeight
	 */
	public void setMaxHeight(int maxHeight)
	{	this.maxHeight = maxHeight;	
	}
		
	/**
	 * limite l'arbre de recherche � un certain cout maxCost, i.e. d�s que le
	 * noeud courant atteint ce cout maximal, l'algorithme se termine et ne
	 * renvoie pas de solution (�chec)
	 * Dans des cas extr�mes, l'arbre peut avoir une hauteur consid�rable,
	 * ce qui peut provoquer un d�passement m�moire. Ce param�tre permet d'�viter
	 * de d�clencher ce type d'exception. A noter qu'un param�tre non-configurable
	 * limite d�j� le nombre de noeuds dans l'arbre.
	 * 
	 * @param maxCost	le cout maximal que le noeud courant peut atteindre
	 */
	public void setMaxCost(int maxCost)
	{	this.maxCost = maxCost;
	}
		
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * calcule le plus court chemin pour aller de la case startTile � 
	 * la case endTile, en utilisant l'algorithme A*. Si jamais aucun
	 * chemin n'est trouv�, alors un chemin vide est renvoy�. Si jamais
	 * l'algorithme atteint une limite de cout/taille, la valeur null est
	 * renvoy�e. Dans ce cas l�, c'est qu'il y a g�n�ralement un probl�me
	 * dans le fa�on dont A* est employ� (mauvaise fonction de cout, par
	 * exemple). 
	 * 
	 * @param startTile	la case de d�part
	 * @param endTile	la case d'arriv�e
	 * @return un chemin pour aller de startTile � endTile, ou un chemin vide, ou la valeur null
	 * @throws StopRequestException 
	 */
	public AiPath processShortestPath(AiTile startTile, AiTile endTile) throws StopRequestException
	{	ArrayList<AiTile> endTiles = new ArrayList<AiTile>();
		endTiles.add(endTile);
		AiPath result = processShortestPath(startTile,endTiles);
		return result;
	}
	
	/**
	 * calcule le plus court chemin pour aller de la case startTile � 
	 * une des cases contenues dans la liste endTiles (n'importe laquelle),
	 * en utilisant l'algorithme A*. Si jamais aucun chemin n'est trouv� 
	 * alors un chemin vide est renvoy�. Si jamais l'algorithme atteint 
	 * une limite de cout/taille, la valeur null est renvoy�e. Dans ce 
	 * cas-l�, c'est qu'il y a g�n�ralement un probl�me dans le fa�on 
	 * dont A* est employ� (mauvaise fonction de cout, par exemple).
	 * La fonction renvoie �galement null si la liste endTiles est vide.
	 * 
	 * @param startTile	la case de d�part
	 * @param endTile	la liste des cases d'arriv�e possibles
	 * @return un chemin pour aller de startTile � une des cases de endTiles, ou un chemin vide, ou la valeur null
	 * @throws StopRequestException 
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
				// on teste si on est arriv� � la fin de la recherche
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
					ArrayList<AstarNode> successors = new ArrayList<AstarNode>(currentNode.getChildren());
					// on introduit du hasard en permuttant al�atoirement les noeuds suivants
					// pour cette raison, cette impl�mentation d'A* ne renverra pas forc�ment toujours le m�me r�sultat :
					// si plusieurs chemins sont optimaux, elle renverra un de ces chemins (pas toujours le m�me)
					Collections.shuffle(successors);
					// puis on les rajoute dans la file de priorit�
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
	private void finish()
	{	if(root!=null)
		{	root.finish();
			root = null;
		}		
	}
}
