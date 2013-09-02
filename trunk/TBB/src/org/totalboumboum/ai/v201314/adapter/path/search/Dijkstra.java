package org.totalboumboum.ai.v201314.adapter.path.search;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;

/**
 * Cette classe implémente l'<a href="http://fr.wikipedia.org/wiki/Algorithme_de_Dijkstra">algorithme 
 * de Dijkstra</a> (<a href="http://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">en</a>) adapté à
 * TBB. En gros, cet algorithme consiste à appliquer la recherche par coût uniforme, mais de façon
 * exhaustive. On va donc tenter de développer l'intégralité de l'arbre de recherche en développant
 * les noeuds de recherche en fonction de leur coût. 
 * <br/>
 * Le résultat n'est donc pas un simple chemin  allant d'une case à une autre, mais l'ensemble des 
 * chemins optimaux allant d'une case source à toutes les cases accessibles dans la zone de jeu. 
 * Bien sûr, ce résultat varie en fonction des fonctions successeur et de coût utilisées. Aucune 
 * fonction heuristique n'est nécessaire, puisqu'on réalise un parcours exhaustif de l'arbre de 
 * recherche. 
 * <br/>
 * Le résultat du traitement prend la forme d'une map associant un noeud de recherche à chaque 
 * case accessible. Ce noeud permet de retrouver le chemin optimal pour aller à cette case,
 * en utilisant sa méthode {@link AiSearchNode#processPath()}.
 * <br/>
 * A noter qu'il s'agit d'une implémentation non-déterministe de l'algorithme.
 * Cela signifie que la méthode renverra toujours le chemin optimal (i.e. le plus court par
 * rapport au coût défini), mais s'il existe plusieurs solutions optimales, l'algorithme ne
 * renverra pas forcément toujours la même (il en choisira une au hasard).
 * Le but est d'introduire une part de hasard dans les agents, de manière à les rendre moins prévisibles.
 * 
 * @author Vincent Labatut
 */
public final class Dijkstra extends AiAbstractSearchAlgorithm
{	
	/**
	 * Construit un objet permettant d'appliquer l'algorithme de Dijkstra
	 * en utilisant les données et fonctions passées en paramètre.
	 * <br/>
	 * La fonction heuristique n'est pas utilisée, elle est donc
	 * initialisée à {@link NoHeuristicCalculator}.
	 * 
	 * @param ai
	 * 		L'agent invoquant A*.
	 * @param hero
	 * 		Le personnage à considérer pour les déplacements.
	 * @param costCalculator
	 * 		La fonction de coût.
	 * @param successorCalculator
	 * 		La fonction successeur.
	 */
	public Dijkstra(ArtificialIntelligence ai, AiHero hero, CostCalculator costCalculator, SuccessorCalculator successorCalculator)
	{	super(ai,hero,costCalculator,new NoHeuristicCalculator(ai),successorCalculator);
	}
	
	/**
	 * Construit un objet permettant d'appliquer l'algorithme de Dijkstra
	 * en réutilisant l'arbre de recherche dont la racine est passée
	 * en paramètre.
	 * 
	 * @param root
	 * 		La racine de l'arbre de recherche à réutilise.
	 */
	public Dijkstra(AiSearchNode root)
	{	super(root);
	
		// on effectue le traitement propre à Dijkstra
		// > aucun pour l'instant
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Permet d'arrêter l'exploration dès qu'on trouve une case sûre (pour {@code processEscapePath}) */ 
	private boolean stopWhenSafe = false;
	
	/**
	 * Calcule tous les plus courts chemins pour aller de la case {@code startTile}
	 * à toutes les cases accessibles de la zone, en utilisant l'algorithme de
	 * Dijkstra. L'algorithme renvoie une map associant à chaque case accessible
	 * un noeud de recherche correspondant à la dernière étape du chemin permettant
	 * de se déplacer sur la case concernée. Ce chemin peut être récupéré en utilisant
	 * la méthode {@link AiSearchNode#processPath()}. Si jamais l'algorithme atteint 
	 * une limite de cout/taille, une exception de type {@link LimitReachedException} 
	 * est levée. Dans ce cas-là, c'est qu'il y a généralement un problème dans le façon 
	 * dont A* est employé (mauvaise fonction de coût, par exemple).
	 * 
	 * @param startLocation	
	 * 		La case de départ.
	 * @return 
	 * 		Une map associant un noeud de recherche à chaque case accessible.
	 * 
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public Map<AiTile,AiSearchNode> startProcess(AiLocation startLocation) throws LimitReachedException
	{	// on réinitialise la case de départ
		this.startLocation = startLocation;
		root = new AiSearchNode(ai,startLocation,hero,costCalculator,heuristicCalculator,successorCalculator);
		costCalculator.init(root);
		successorCalculator.init(root);
		
		Map<AiTile,AiSearchNode> result = startProcess();
		return result;
	}
	
	/**
	 * Réalise le même traitement que {@link #startProcess(AiLocation)},
	 * à la différence qu'ici on réutilise l'arbre de recherche déjà existant. 
	 * Autrement dit, on n'a pas besoin de préciser la case de départ, car elle 
	 * a déjà été initialisée précédemment. On va réutiliser ce travail fait
	 * lors d'un précédent appel (ou de plusieurs appels précédents) afin de 
	 * calculer plus vite le résultat.
	 * <br/>
	 * Cette méthode est également utilisable quand cet objet a été construit
	 * à partir d'un arbre existant avec {@link #Dijkstra(AiSearchNode)},
	 * ou bien quand la méthode {@link #setRoot(AiSearchNode)} a été utilisée.
	 * 
	 * @return 
	 * 		Une map associant un noeud de recherche à chaque case accessible.
	 * 
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public Map<AiTile,AiSearchNode> startProcess() throws LimitReachedException
	{	// on teste d'abord si l'algorithme a au moins été appliqué une fois,
		// sinon la case de départ n'est pas connue. Dans ce cas, on lève une NullPointerException.
		if(root==null)
			throw new NullPointerException("The algorithm must be called at least once with startProcess(AiLocation,...) for init purposes");
		
		// initialisation
		treeHeight = 0;
		treeCost = 0;
		treeSize = 0;
		lastSearchNode = null;
		
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
		Map<AiTile,AiSearchNode> result = continueProcess();
		return result;
	}
	
	/**
	 * Calcule un chemin de fuite.
	 * 
	 * @param startLocation
	 * 		Position de départ.
	 * @return
	 * 		Le chemin calculé.
	 * 
	 * @throws LimitReachedException
	 * 		L'algorithme de recherche a atteint les limites fixées.
	 */
	public AiPath processEscapePath(AiLocation startLocation) throws LimitReachedException
	{	// on indique la condition de fin pour l'exploration de la zone
		stopWhenSafe = true;
		
		// on applique dijkstra
		startProcess(startLocation);
		
		// on construit le chemin à partir du dernier noeud de recherche traité
		AiPath result = lastSearchNode.processPath();
		return result;
	}
	
	/**
	 * Permet de continuer le traitement commencé par {@link #startProcess(AiLocation) startProcess}.
	 * Par exemple, si {@code startProcess} a provoqué une exception 
	 * de type {@link LimitReachedException}, on peut augmenter la limite
	 * concernée puis faire appel à cette méthode afin de continuer le traitement
	 * avec une limite moins stricte.
	 * 
	 * @return 
	 * 		Une map associant un noeud de recherche à chaque case accessible.
	 * 
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public Map<AiTile,AiSearchNode> continueProcess() throws LimitReachedException
	{	long before = print("      > Starting/resuming Dijkstra +++++++++++++++++++++");
		print("         searching paths starting from "+startLocation);
		
		// on remet le dernier noeud (fautif) dans la file,
		// pour permettre éventuellement de continuer le traitement
		if(limitReached && lastSearchNode!=null)
		{	queue.offer(lastSearchNode);
			print("           Queue length: "+queue.size());
			printQueue("             + ",queue);
		}
	
		// initialisation
		int it = 0;
		lastSearchNode = null;
		limitReached = false;
		boolean safeStop = false;
			
		// traitement
		if(!queue.isEmpty())
		{	do
			{	ai.checkInterruption();
				
				// verbose : iteration
				it ++;
				long before1 = print("         -- starting iteration #" + it + " --");
				
				// on prend le noeud situé en tête de file
				lastSearchNode = queue.poll();
				// verbose : noeud courant
				print("           Zone:\n"+lastSearchNode.getZoneRepresentation());
				print("           Visiting : "+lastSearchNode);
				
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
				{	// développement (on copie la liste des enfants, qui est sinon immuable)
					long before2 = ai.getCurrentTime();
					List<AiSearchNode> successors = new ArrayList<AiSearchNode>(lastSearchNode.getChildren());

					// verbose : temps
					{	long after2 = ai.getCurrentTime();
						long elapsed2 = after2 - before2;
						print("           Child development: duration="+elapsed2+" ms");
						for(AiSearchNode c: successors)
							print("             + " + c);
					}
					
					// on introduit du hasard en permuttant aléatoirement les noeuds suivants
					// pour cette raison, cette implémentation d'A* ne renverra pas forcément toujours le même résultat :
					// si plusieurs chemins sont optimaux, elle renverra un de ces chemins (pas toujours le même)
					Collections.shuffle(successors);
					// puis on les rajoute dans la file de priorité
					for(AiSearchNode node: successors)
						queue.offer(node);
				}
				
				// mise à jour des données décrivant l'arbre
				if(lastSearchNode.getDepth()>treeHeight)
					treeHeight = lastSearchNode.getDepth();
				if(lastSearchNode.getCost()>treeCost)
					treeCost = lastSearchNode.getCost();
				if(queue.size()>treeSize)
					treeSize = queue.size();
				
				// mise à jour de la condition d'arrêt
				if(stopWhenSafe)
					safeStop = !successorCalculator.isThreatened(lastSearchNode);
				
				// verbose : file
				{	print("           Queue length: "+queue.size());
					printQueue("             + ",queue);
				}
				// verbose : itération
				{	long after1 = ai.getCurrentTime();
					long elapsed1 = after1 - before1;
					print("         -- iteration #" + it + " finished, duration=" + elapsed1 + " --");
				}
			}
			while(!queue.isEmpty() && !safeStop);
		}
		
		// verbose : temps
		{	long after = ai.getCurrentTime();
			long elapsed = after - before;
			print("         Total elapsed time: "+elapsed+" ms");
		}
		// verbose résultat
		{	if(limitReached)
				print("         Limit reached");
			else
				print("         Search finished");
		}
		// verbose : limites
		{	print("         height="+treeHeight+" cost="+treeCost+" size="+treeSize+" src="+root.getLocation());
			if(limitReached)
				print("         maxHeight="+maxHeight+" maxCost="+maxCost+" maxSize="+maxNodes);
		}
		// verbose : fin
		print("      < Dijkstra finished +++++++++++++++++++++");

		// exceptions
		if(limitReached)
			throw new LimitReachedException(startLocation,null,treeHeight,treeCost,treeSize,maxCost,maxHeight,maxNodes,queue);
		
		// on construit le résultat
		Map<AiTile,AiSearchNode> result = integrateMaps();
		return result;
	}

	/**
	 * Prend l'ensemble des chemins trouvés lors de la recherche,
	 * qui sont contenus dans les différentes maps de la map
	 * principale contenue dans la fonction successeur, et les utilise 
	 * pour créer une map finale contenant pour chaque case accessible
	 * le meilleur chemin trouvé en termes de coût.
	 *  
	 * @return
	 * 		Une map contenant tous les meilleurs chemins trouvés.
	 */
	private Map<AiTile,AiSearchNode> integrateMaps()
	{	Map<AiSearchNode,Map<AiTile,AiSearchNode>> processedTiles = successorCalculator.getProcessedTiles();
		Map<AiTile,AiSearchNode> result = new HashMap<AiTile,AiSearchNode>();
		for(Map<AiTile,AiSearchNode> map: processedTiles.values())
		{	for(Entry<AiTile,AiSearchNode> entry: map.entrySet())
			{	AiTile tile = entry.getKey();
				AiSearchNode node1 = result.get(tile);
				AiSearchNode node2 = entry.getValue();
				if(node1==null)
					result.put(tile,node2);
				else
				{	double cost1 = node1.getCost();
					double cost2 = node2.getCost();
					if(cost1>cost2)
						result.put(tile,node2);
				}
			}
		}
		return result;
	}
}
