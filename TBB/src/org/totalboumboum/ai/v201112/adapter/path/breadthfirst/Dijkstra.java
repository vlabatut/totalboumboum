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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiAbstractSearchAlgorithm;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.SuccessorCalculator;

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
	 * 		L'AI invoquant A*.
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
	
	/**
	 * - on utilise une hashmap de matrices pour marquer les cases explorées, et ne garder que les meilleures
	 * - à la fin, on intègrera cette matrice pour obtenir le meilleur de chaque case (on garde l'arbre de recherche aussi)
	 * - on a besoin d'une fonction getAncestor dans les noeuds de recherche pour les utiliser comme clés de la map
	 * 
	 * - dans djikstra, pour chaque noeud fils on teste si y a pas déjà un noeud correspondant dans la matrice
	 * - si oui, ça veut dire qu'y a déjà mieux pour cette tranche temporelle, donc on laisse tomber
	 * - on s'arrête quand toutes les branches temporelles ont été explorées
	 *   >> au final, le niveau entier sera exploré autant de fois qu'il y a d'explosions possibles
	 *   
	 * - p-ê intéressant de limiter le nombre d'explosions considérées (horizon) pour accélérer le traitement
	 * 
	 * 
	 * TODO désactiver le 'chemin parcouru?' dès qu'y a pu de bombes
	 * TODO ne pas oublier de prendre une liste de priorité pr tenir cpte du coût dans dijkstra
	 */
	
	
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
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
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public HashMap<AiTile,AiSearchNode> startProcess(AiLocation startLocation) throws StopRequestException, LimitReachedException
	{	// on réinitialise la case de départ
		this.startLocation = startLocation;
		root = new AiSearchNode(ai,startLocation,hero,costCalculator,heuristicCalculator,successorCalculator);
		successorCalculator.init(root);
		
		HashMap<AiTile,AiSearchNode> result = startProcess();
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
	 * à partir d'un arbre existant avec {@link #BreadthFirst(AiSearchNode)},
	 * ou bien quand la méthode {@link #setRoot(AiSearchNode)} a été utilisée.
	 * 
	 * @param endTiles	
	 * 		L'ensemble des cases d'arrivée possibles.
	 * @return 
	 * 		Une map associant un noeud de recherche à chaque case accessible.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public HashMap<AiTile,AiSearchNode> startProcess() throws StopRequestException, LimitReachedException
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
		HashMap<AiTile,AiSearchNode> result = continueProcess();
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
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public HashMap<AiTile,AiSearchNode> continueProcess() throws StopRequestException, LimitReachedException
	{	long before = print("      >> Starting/resuming Dijkstra +++++++++++++++++++++");
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
		
		HashMap<AiTile,AiSearchNode> result = integrateMaps();
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
	private HashMap<AiTile,AiSearchNode> integrateMaps()
	{	HashMap<AiSearchNode,HashMap<AiTile,AiSearchNode>> processedTiles = successorCalculator.getProcessedTiles();
		HashMap<AiTile,AiSearchNode> result = new HashMap<AiTile,AiSearchNode>();
		for(HashMap<AiTile,AiSearchNode> map: processedTiles.values())
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
