package org.totalboumboum.ai.v201112.adapter.path.breadthfirst;

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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiAbstractSearchAlgorithm;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.SuccessorCalculator;

/**
 * TODO
 * version sans limite de la recherche par coût uniforme, aussi appelée algorithme de Dijkstra
 * http://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 * http://fr.wikipedia.org/wiki/Algorithme_de_Dijkstra
 * 
 * 
 * 
 * @author Vincent Labatut
 */
public final class BreadthFirst extends AiAbstractSearchAlgorithm
{	
	/**
	 * TODO
	 * construit un objet permettant d'appliquer l'algorithme A*.
	 * 
	 * @param ai
	 * 		l'AI invoquant A*
	 * @param hero
	 * 		le personnage à considérer pour les déplacements
	 * @param costCalculator
	 * 		la fonction de coût
	 * @param heuristicCalculator
	 * 		la fonction heuristique
	 * @param successorCalculator
	 * 		la fonction successeur
	 */
	public BreadthFirst(ArtificialIntelligence ai, AiHero hero, CostCalculator costCalculator, SuccessorCalculator successorCalculator)
	{	super(ai,hero,costCalculator,new NoHeuristicCalculator(ai),successorCalculator);
	}

    /////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Cases restant à traiter */
	private Set<AiTile> remainingTiles = new TreeSet<AiTile>();
/* comment arrêter l'exploration pour les fct succ à base de modèle ?
 *   > soit utiliser une liste des cases restant à résoudre
 *		- mais comment les identifier avt le début de l'algo ?
 *		- faudrait faire un premier parcours en largeur (?)
 *	 > soit utiliser une liste des cases déjà résolues
 *		- pareil : comment savoir s'il en reste à résoudre ?
 *	 > utiliser une matrice pour représenter la zone
 *		- initialiser avec des valeurs indéterminées
 *		- à chaque développement, on regarde si on a trouvé un meilleur temps que celui déjà présent
 *		- mais même si oui, on peut pas s'arrêter, car la case peut permettre de trouver un meilleur chemin pour une autre case
 *	 > est-ce que le parcours ne revient pas en fait à parcourir autant de fois toute
 *	   la zone qu'il y a d'explosion ? (!)
 *
 * - autre pb : comment s'assurer que la première case trouvée a bien le temps optimal ?
 * 
 */
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Calcule le plus court chemin pour aller de la case {@code startTile} à 
	 * l'une des cases contenues dans la liste {@code endTiles} (n'importe laquelle),
	 * en utilisant l'algorithme A*. Si jamais aucun chemin n'est trouvé 
	 * alors un chemin vide est renvoyé. Si jamais l'algorithme atteint 
	 * une limite de cout/taille, la valeur {@code null} est renvoyée. Dans ce 
	 * cas-là, c'est qu'il y a généralement un problème dans le façon 
	 * dont A* est employé (mauvaise fonction de coût, par exemple).
	 * La fonction renvoie également {@code null} si la liste {@code endTiles} est vide.
	 * 
	 * @param startLocation	
	 * 		La case de départ.
	 * @param endTiles	
	 * 		L'ensemble des cases d'arrivée possibles.
	 * @return 
	 * 		un chemin pour aller de {@code startTile} à une des cases de {@code endTiles},
	 * 		ou un chemin vide, ou la valeur {@code null}.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath startProcess(AiLocation startLocation) throws StopRequestException, LimitReachedException
	{	// on réinitialise la case de départ
		this.startLocation = startLocation;
		root = new AiSearchNode(ai,startLocation,hero,costCalculator,heuristicCalculator,successorCalculator);
		successorCalculator.init(root);
		
		AiPath result = startProcess();
		return result;
	}
	
	/**
	 * Réalise le même traitement que {@link #processShortestPath(AiLocation, Set)},
	 * à la différence qu'ici on réutilise l'arbre de recherche déjà existant. 
	 * Autrement dit, on n'a pas besoin de préciser la case de départ, car elle 
	 * a déjà été initialisée précédemment. On va réutiliser ce travail fait
	 * lors d'un précédent appel (ou de plusieurs appels précédents) afin de 
	 * calculer plus vite le résultat.
	 * 
	 * @param endTiles	
	 * 		L'ensemble des cases d'arrivée possibles.
	 * @return 
	 * 		un chemin pour aller à l'une des cases de {@code endTiles},
	 * 		ou un chemin vide, ou la valeur {@code null}.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath startProcess() throws StopRequestException, LimitReachedException
	{	// on teste d'abord si l'algorithme a au moins été appliqué une fois,
		// sinon la case de départ n'est pas connue. Dans ce cas, on lève une NullPointerException.
		if(root==null)
			throw new NullPointerException("The algorithm must be called at least once with startProcess(AiLocation,...) for init purposes");
		
		// initialisation
		treeHeight = 0;
		treeCost = 0;
		treeSize = 0;
		limitReached = false;
		
		// queue
		Comparator<AiSearchNode> comparator = new Comparator<AiSearchNode>()
		{	@Override
			public int compare(AiSearchNode o1, AiSearchNode o2)
			{	int result = o1.compareScoreTo(o2);
				return result;
			}
		};
		queue = new PriorityQueue<AiSearchNode>(1,comparator);
		queue.offer(root);
		
		// process
		AiPath result = continueProcess();
		return result;
	}
	
	/**
	 * Permet de continuer le traitement commencé par {@link #processShortestPath(AiLocation, AiTile) processShortestPath}.
	 * Par exemple, si {@code processShortestPath} a trouvé un résultat qui ne
	 * parait pas adapté, l'appel à cette méthode permet de continuer le traitement
	 * pour trouver un autre chemin.
	 * <br/>
	 * <b>Attention :</b> par définition de A*, le chemin suivant ne
	 * sera pas forcément optimal en termes du coût défini. Bien sûr,
	 * si d'autres chemins optimaux existent, ils seront identifiés
	 * avant les chemin sous-optimaux.
	 * 
	 * @return 
	 * 		un chemin différent de celui renvoyé par {@code processShortestPath}.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath continueProcess() throws StopRequestException, LimitReachedException
	{	AiPath result = null;
		AiSearchNode finalNode = null;
		
		long before = print("      >> Starting/resuming Dijkstra +++++++++++++++++++++");
		print("         searching paths from "+startLocation);
		
		lastSearchNode = null;
		// traitement
		if(!queue.isEmpty())
		{	do
			{	ai.checkInterruption();
				long before1 = print("---------- new iteration --");
				
				// on prend le noeud situé en tête de file
				lastSearchNode = queue.poll();
				print("           Visited : "+lastSearchNode.toString());
				print("           Queue length: "+queue.size());
				AiZone zone = lastSearchNode.getLocation().getTile().getZone();
				print("           Zone:\n"+zone);
				
				// si l'arbre a atteint la hauteur maximale, on s'arrête
				if(maxHeight>0 && lastSearchNode.getDepth()>=maxHeight)
					limitReached = true;
				// si le noeud courant a atteint le coût maximal, on s'arrête
				else if(maxCost>0 && lastSearchNode.getCost()>=maxCost)
					limitReached = true;
				// si le nombre de noeuds dans la file est trop grand, on s'arrête
				else if(maxNodes>0 && queue.size()>=maxNodes)
					limitReached = true;
				
				// sinon on récupére les noeuds suivants
				else
				{	long before2 = System.currentTimeMillis();
					List<AiSearchNode> successors = lastSearchNode.getChildren();
					long after2 = System.currentTimeMillis();
					long elapsed2 = after2 - before2;
					print("           Child development: duration="+elapsed2+" ms");
					// on introduit du hasard en permuttant aléatoirement les noeuds suivants
					// pour cette raison, cette implémentation d'A* ne renverra pas forcément toujours le même résultat :
					// si plusieurs chemins sont optimaux, elle renverra un de ces chemins (pas toujours le même)
//TODO					Collections.shuffle(successors);
					// puis on les rajoute dans la file de priorité
					for(AiSearchNode node: successors)
						queue.offer(node);
				}
				
				// verbose
				if(lastSearchNode.getDepth()>treeHeight)
					treeHeight = lastSearchNode.getDepth();
				if(lastSearchNode.getCost()>treeCost)
					treeCost = lastSearchNode.getCost();
				if(queue.size()>treeSize)
					treeSize = queue.size();
				long after1 = System.currentTimeMillis();
				long elapsed1 = after1 - before1;
				print("        -- iteration duration="+elapsed1+" --");
			}
			while(!queue.isEmpty());
		}
		
		long after = System.currentTimeMillis();
		long elapsed = after - before;
		if(limitReached)
			print("         Limit reached");
		else
			print("         Search finished");
		print("         Elapsed time: "+elapsed+" ms");
		//
		print("         height="+treeHeight+" cost="+treeCost+" size="+treeSize+" src="+root.getLocation());
		print("      << Dijkstra finished +++++++++++++++++++++");

//		finish();
		if(limitReached)
			throw new LimitReachedException(startLocation,null,treeHeight,treeCost,treeSize,maxCost,maxHeight,maxNodes,queue);
		
		return result;
	}
}
