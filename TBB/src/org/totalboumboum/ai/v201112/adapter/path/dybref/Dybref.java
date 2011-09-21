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

import java.util.List;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;

/**
 * implément de l'algorithme A* (http://fr.wikipedia.org/wiki/Algorithme_A*) adapté au
 * cas où on a le choix entre plusieurs objectifs alternatifs. S'il y a un seul objectif, 
 * cette implément correspond à peu près à un A* classique. Il y a quand même une modification,
 * puisque les noeuds d'état apparaissant déjà dans des noeuds de recherche anc�tre sont
 * écartés lorsqu'un noeud de recherche est développé. En d'autres termes, l'algorithme évite
 * de chercher des chemins qui passent plusieurs fois par la même case, ce qui l'empêche de
 * boucler à l'infini.</br>
 * 
 * Cette implément trouve donc le chemin le plus court entre deux cases,
 * en considérant les obstacles. Elle a besoin de quatre paramètres :<ul>
 * 		<li> Le personnage qui doit effectuer le trajet entre les deux cases (nécessaire afin de tester la traversabilité des cases).</li>
 * 		<li> Une fonction successeur, qui définit les actions possibles à partir d'un état donné. Dans le cas prèsent, il s'agit de 
 * 			 restreindre les déplacement possibles en considérant des facteurs supplémentaires par rapport à la simple traversabilité courrante.</li>
 * 		<li> Une fonction de coût, qui permet de définir combien coûte une action (ici : le fait de passer d'une case à l'autre).</li>
 * 		<li> Une fonction heuristique, qui permet d'estimer le coût du chemin restant à parcourir.</li></ul>
 * 
 * A noter qu'il s'agit d'une implément non-déterministe de l'algorithme.
 * Cela signifie que la méthode renverra toujours le chemin optimal (i.e. le plus court par
 * rapport au cout défini), mais s'il existe plusieurs solutions optimales, l'algorithme ne
 * renverra pas forcément toujours la même (il en choisira une au hasard).
 * Le but est d'introduire une part de hasard dans les IA, de manière à les rendre moins prévisibles.
 * 
 * @author Vincent Labatut
 *
 */
public final class Dybref
{	private static boolean verbose = false;

	/**
	 * construit un objet permettant d'appliquer l'algorithme A*
	 * en utilisant la fonction successeur définie par défaut.
	 * 
	 * @param ai
	 * 		l'AI invoquant A*
	 * @param hero
	 * 		le personnage à considérer pour les déplacements
	 * @param costCalculator
	 * 		la fonction de coût
	 * @param heuristicCalculator
	 * 		la fonction heuristique
	 */
	public Dybref(ArtificialIntelligence ai, AiHero hero)
	{	this.ai = ai;
		this.hero = hero;
	}

    /////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** racine de l'arbre de recherche */
	private DybrefNode root = null;
	/** personnage de référence */
	private AiHero hero = null;
	/** l'ai qui a réalisé l'appel */
	private ArtificialIntelligence ai = null;

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de hauteur (négatif = pas de limite) */
	private int maxHeight = -1;
	/** limite de nombre de noeuds (négatif = pas de limite), pas configurable */
	private int maxNodes = 10000;
	
	/**
	 * limite l'arbre de recherche à une hauteur de maxHeight,
	 * i.e. quand le noeud courant a une profondeur correspondant à maxHeight,
	 * l'algorithme se termine et ne renvoie pas de solution (échec).
	 * Dans des cas extrêmes, l'arbre peut avoir une hauteur considérable,
	 * ce qui peut provoquer un dépassement mémoire. Ce paramètre permet d'éviter
	 * de déclencher ce type d'exception. A noter qu'un paramètre non-configurable
	 * limite déjà le nombre de noeuds dans l'arbre.
	 * 
	 * @param maxHeight
	 */
	public void setMaxHeight(int maxHeight)
	{	this.maxHeight = maxHeight;	
	}
		
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * calcule le plus court chemin pour aller de la case startTile à 
	 * la case endTile, en utilisant l'algorithme A*. Si jamais aucun
	 * chemin n'est trouvé, alors un chemin vide est renvoyé. Si jamais
	 * l'algorithme atteint une limite de cout/taille, la valeur null est
	 * renvoyée. Dans ce cas là, c'est qu'il y a généralement un problème
	 * dans le façon dont A* est employé (mauvaise fonction de cout, par
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
	public DybrefMatrix processShortestPaths(AiZone zone) throws StopRequestException, LimitReachedException
	{	AiHero hero = zone.getHeroByColor(this.hero.getColor());
		AiTile startTile = hero.getTile();
		if(verbose)
			System.out.println("Dybref*: from "+startTile);
		int maxh = 0;
		int maxn = 0;

		// initialisation
		boolean limitReached = false;
		root = new DybrefNode(ai,hero);
		PriorityQueue<DybrefNode> queue = new PriorityQueue<DybrefNode>(1);
		queue.offer(root);
	
		// traitement
		do
		{	ai.checkInterruption();
			
			// on prend le noeud situé en tête de file
			DybrefNode currentNode = queue.poll();
			if(verbose)
			{	System.out.println("Visited : "+currentNode.toString());
				System.out.println("Queue length: "+queue.size());
			}
			
			// si l'arbre a atteint la hauteur maximale, on s'arrête
			else if(maxHeight>0 && currentNode.getDepth()>=maxHeight)
				limitReached = true;
			// si le nombre de noeuds dans la file est trop grand, on s'arrête
			else if(maxNodes>0 && queue.size()>=maxNodes)
				limitReached = true;
			// sinon on récupére les noeuds suivants et on les rajoute dans la file de priorité
			else
			{	List<DybrefNode> successors = currentNode.getChildren();
				for(DybrefNode node: successors)
					queue.offer(node);
			}
			
			// limits
			if(currentNode.getDepth()>maxh)
				maxh = currentNode.getDepth();
			if(queue.size()>maxn)
				maxn = queue.size();
		}
		while(!queue.isEmpty() && !limitReached);
	
		if(verbose)
		{	System.out.print("Process done: ");
			if(limitReached)
				System.out.println(" limit reached");
			else //if(queue.isEmpty())
				System.out.println(" nothing left to process");
			System.out.print("height="+maxh+" size="+maxn);
			System.out.print(" src="+root.getTile());
			System.out.println();
		}

		finish();
		if(limitReached)
			throw new LimitReachedException(startTile,maxh,maxn,maxHeight,maxNodes);
		
		DybrefMatrix result = root.getMatrix();
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
		
		ai = null;
		hero = null;
	}
}
