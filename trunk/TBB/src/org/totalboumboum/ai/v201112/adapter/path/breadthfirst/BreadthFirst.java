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
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.HeuristicCalculator;
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
	 * @param startLocation	
	 * 		La case de départ
	 * @param endTile	
	 * 		La case d'arrivée
	 * @return 
	 * 		Un chemin pour aller de startTile à endTile, ou un chemin vide, ou la valeur {@code null}.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath processShortestPath(AiLocation startLocation, AiTile endTile) throws StopRequestException, LimitReachedException
	{	Set<AiTile> endTiles = new TreeSet<AiTile>();
		endTiles.add(endTile);
		AiPath result = processShortestPath(startLocation,endTiles);
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
	public AiPath processShortestPath(AiLocation startLocation, Set<AiTile> endTiles) throws StopRequestException, LimitReachedException
	{	// initialisation
		this.startLocation = startLocation;
		this.remainingTiles = endTiles;
		treeHeight = 0;
		treeCost = 0;
		treeSize = 0;
		limitReached = false;
		heuristicCalculator.setEndTiles(endTiles);
		root = new AiSearchNode(ai,startLocation,hero,costCalculator,heuristicCalculator,successorCalculator);
		queue = new PriorityQueue<AiSearchNode>(1);
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
		
		long before = print(">>>>>>>> Starting/resuming A* +++++++++++++++++++++");
		String msg = "         searching paths from "+startLocation+" to [";
		for(AiTile tile: remainingTiles)
			msg = msg + " " + tile;
		msg = msg + " ]";
		print(msg);
		
		boolean found = false;
		// traitement
		if(!remainingTiles.isEmpty())
		{	do
			{	ai.checkInterruption();
				long before1 = print("---------- new iteration --");
				
				// on prend le noeud situé en tête de file
				lastSearchNode = queue.poll();
				print("           Visited : "+lastSearchNode.toString());
				print("           Queue length: "+queue.size());
				print("           Zone:\n"+lastSearchNode.getLocation().getTile().getZone());
				
				// on teste si on est arrivé à la fin de la recherche
				if(remainingTiles.contains(lastSearchNode.getLocation().getTile()))
				{	// si oui on garde le dernier noeud pour ensuite pouvoir reconstruire le chemin solution
					finalNode = lastSearchNode;
					found = true;
				}
				
				// si l'arbre a atteint la hauteur maximale, on s'arrête
				else if(maxHeight>0 && lastSearchNode.getDepth()>=maxHeight)
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
					Collections.shuffle(successors);
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
				print("---------- iteration duration="+elapsed1+" --");
			}
			while(!queue.isEmpty() && !found && !limitReached);
		
			// build solution path
			if(found)
				result = finalNode.processPath();
		}
		
		long after = System.currentTimeMillis();
		long elapsed = after - before;
		msg = "         Path: [";
		if(limitReached)
			msg = msg + " limit reached";
		else if(found)
		{	for(AiLocation loc: result.getLocations())
				msg = msg + " " + loc;
		}
		else if(remainingTiles.isEmpty())
			msg = msg + " endTiles parameter empty";
		else
			msg = msg + " no solution found";
		msg = msg + " ]";
		print(msg);
		print("         Elapsed time: "+elapsed+" ms");
		//
		msg = "         height="+treeHeight+" cost="+treeCost+" size="+treeSize;
		msg = msg + " src="+root.getLocation();
		msg = msg + " trgt=";
		for(AiTile tile: remainingTiles) 
			msg = msg + " " + tile;
		print(msg);
		if(result!=null) 
			print("         result="+result);

//		finish();
		if(limitReached)
			throw new LimitReachedException(startLocation,remainingTiles,treeHeight,treeCost,treeSize,maxCost,maxHeight,maxNodes,queue);
		else if(remainingTiles.isEmpty())
			throw new IllegalArgumentException("endTiles list must not be empty");
		
		return result;
	}
}
