package fr.free.totalboumboum.ai.adapter200910.path.astar;

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
import java.util.PriorityQueue;

import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;

/**
 * Implémentation de l'algorithme A* classique (http://fr.wikipedia.org/wiki/Algorithme_A*)
 * Cet algorithme permet (entre autres) de trouver le chemin le plus court entre deux cases,
 * en considérant les obstacles. Il a besoin de trois paramètres :
 * 		- le personnage qui doit effectuer le trajet entre les deux cases
 * 		- une fonction de cout, qui permet de définir combien coute une action (ici : le fait de passer d'une case à l'autre)
 * 		- une fonction heuristique, qui permet d'estimer le cout du chemin restant à parcourir
 * A noter qu'il s'agit d'une implémentation non-déterministe de l'algorithme.
 * Cela signifie que la méthode renverra toujours le chemin optimal (i.e. le plus court par
 * rapport au cout défini), mais s'il existe plusieurs solutions optimales, l'algorithme ne
 * renverra pas forcément toujours la même (il en choisira une au hasard).
 * Le but est d'introduire une part de hasard dans les IA, de manière à les rendre moins prévisibles.
 */
public class Astar
{	private static boolean verbose = false;

	public Astar(AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator)
	{	this.hero = hero;
		this.costCalculator = costCalculator;
		this.heuristicCalculator = heuristicCalculator;
	}

    /////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** fonction de cout */
	private CostCalculator costCalculator = null;
	/** fonctin heuristique */
	private HeuristicCalculator heuristicCalculator = null;
	/** racine de l'arbre de recherche */
	private AstarNode root = null;
	/** personnage de référence */
	private AiHero hero = null;

	/////////////////////////////////////////////////////////////////
	// LIMITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de hauteur (négatif = pas de limite) */
	private int maxHeight = -1;
	/** limite de coût (négatif = pas de limite) */
	private int maxCost = -1;
	/** limite de nombre de noeuds (négatif = pas de limite), pas configurable */
	private int maxNodes = 10000;
	
	/**
	 * limite l'arbre de recherche à une hauteur de maxHeight,
	 * i.e. quand le noeud courant a une profondeur correspondant à maxHeight,
	 * l'algorithme se termine et ne renvoie pas de solution (échec).
	 * (sinon l'arbre peut avoir une hauteur infinie, et l'algorithme
	 * peut ne jamais s'arrêter)
	 * 
	 * @param maxHeight
	 */
	public void setMaxHeight(int maxHeight)
	{	this.maxHeight = maxHeight;	
	}
		
	/**
	 * limite l'arbre de recherche à un certain cout maxCost, i.e. dès que le
	 * noeud courant atteint ce cout maximal, l'algorithme se termine et ne
	 * renvoie pas de solution (échec)
	 * (ceci permet d'éviter que l'algorithme ne s'arrête jamais quand il n'y
	 * a pas de solution)
	 * 
	 * @param maxCost	le cout maximal que le noeud courant peut atteindre
	 */
	public void setMaxCost(int maxCost)
	{	this.maxCost = maxCost;
	}
		
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AiPath processShortestPath(AiTile startTile, AiTile endTile)
	{	if(verbose)
			System.out.println("A*: from "+startTile+" to "+endTile);
		
		// initialisation
		AiPath result = new AiPath();
		heuristicCalculator.setEndTile(endTile);
		root = new AstarNode(startTile,hero,costCalculator,heuristicCalculator);
		PriorityQueue<AstarNode> queue = new PriorityQueue<AstarNode>(1);
		queue.offer(root);
		AstarNode finalNode = null;
		boolean stop = false;

		// traitement
		do
		{	// on prend le noeud situé en tête de file
			AstarNode currentNode = queue.poll();
			if(verbose)
			{	System.out.println("Visited : "+currentNode.toString());
				System.out.println("Queue length: "+queue.size());
			}
			// on teste si on est arrivé à la fin de la recherche
			if(currentNode.getTile()==endTile)
			{	// si oui on garde le dernier noeud pour ensuite pouvoir reconstruire le chemin solution
				finalNode = currentNode;
				stop = true;
			}
			// si l'arbre a atteint la hauteur maximale, on s'arrête
			else if(maxHeight>0 && currentNode.getDepth()>=maxHeight)
				stop = true;
			// si le noeud courant a atteint le cout maximal, on s'arrête
			else if(maxCost>0 && currentNode.getCost()>=maxCost)
				stop = true;
			// si le nombre de noeuds dans la file est trop grand, on s'arrête
			else if(maxNodes>0 && queue.size()>=maxNodes)
				stop = true;
			else
			{	// sinon on récupère les noeuds suivants
				ArrayList<AstarNode> successors = new ArrayList<AstarNode>(currentNode.getChildren());
				// on introduit du hasard en permuttant aléatoirement les noeuds suivants
				// pour cette raison, cette implémentation d'A* ne renverra pas forcément toujours le même résultat :
				// si plusieurs chemins sont optimaux, elle renverra un de ces chemins (pas toujours le même)
				Collections.shuffle(successors);
				// puis on les rajoute dans la file de priorité
				for(AstarNode node: successors)
					queue.offer(node);
			}
		}
		while(!queue.isEmpty() && !stop);
		
		// build solution path
		while(finalNode!=null)
		{	AiTile tile = finalNode.getTile();
			result.addTile(0,tile);
			finalNode = finalNode.getParent();
		}
		if(verbose)
		{	System.out.print("Path: [");
			for(AiTile t: result.getTiles())
				System.out.print(" "+t);
			System.out.println(" ]");
		}
		
		return result;
	}
}
