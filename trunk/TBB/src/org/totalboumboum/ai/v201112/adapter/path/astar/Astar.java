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
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.SuccessorCalculator;

/**
 * Implémentation de l'algorithme A* (http://fr.wikipedia.org/wiki/Algorithme_A*) adapté au
 * cas où on a le choix entre plusieurs objectifs alternatifs. S'il y a un seul objectif, 
 * cette implémentation correspond à peu près à un A* classique. Il y a quand même une modification,
 * puisque les noeuds d'état apparaissant déjà dans des noeuds de recherche ancêtre sont
 * écartés lorsqu'un noeud de recherche est développé. En d'autres termes, l'algorithme évite
 * de chercher des chemins qui passent plusieurs fois par la même case, ce qui l'empêche de
 * boucler à l'infini.</br>
 * 
 * Cette implémentation trouve donc le chemin le plus court entre deux cases,
 * en considérant les obstacles. Elle a besoin de quatre paramètres :
 * <ul>	
 * 		<li> Le personnage qui doit effectuer le trajet entre les deux cases (nécessaire afin de tester la traversabilité des cases).</li>
 * 		<li> Une fonction successeur, qui définit les actions possibles à partir d'un état donné. Dans le cas prèsent, il s'agit de 
 * 			 restreindre les déplacement possibles en considérant des facteurs supplémentaires par rapport à la simple traversabilité courrante.</li>
 * 		<li> Une fonction de coût, qui permet de définir combien coûte une action (ici : le fait de passer d'une case à l'autre).</li>
 * 		<li> Une fonction heuristique, qui permet d'estimer le coût du chemin restant à parcourir.</li>
 * </ul>
 * 
 * A noter qu'il s'agit d'une implémentation non-déterministe de l'algorithme.
 * Cela signifie que la méthode renverra toujours le chemin optimal (i.e. le plus court par
 * rapport au coût défini), mais s'il existe plusieurs solutions optimales, l'algorithme ne
 * renverra pas forcément toujours la même (il en choisira une au hasard).
 * Le but est d'introduire une part de hasard dans les agents, de manière à les rendre moins prévisibles.
 * 
 * @author Vincent Labatut
 */
public final class Astar
{	/** permet d'activer/désactiver la sortie texte lors du débogage */
	public static boolean verbose = false;

	/**
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
	public Astar(ArtificialIntelligence ai, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator, SuccessorCalculator successorCalculator)
	{	this.ai = ai;
		this.hero = hero;
		this.costCalculator = costCalculator;
		this.heuristicCalculator = heuristicCalculator;
		this.successorCalculator = successorCalculator;
		
		// hauteur limite de l'arbre par défaut
		AiZone zone = ai.getZone();
		// bien que possible, c'est vraiment peu probable d'avoir 
		// besoin d'un chemin aussi long que ça...
		// (traverser la zone en entier diagonalement)
		maxHeight = zone.getWidth() + zone.getHeight();
	}

    /////////////////////////////////////////////////////////////////
	// CALCULATORS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Fonction de coût */
	private CostCalculator costCalculator = null;
	/** Fonction heuristique */
	private HeuristicCalculator heuristicCalculator = null;
	/** Fonction successeur */
	private SuccessorCalculator successorCalculator = null;

    /////////////////////////////////////////////////////////////////
	// A* STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Racine de l'arbre de recherche */
	private AiSearchNode root = null;
	/** Personnage de référence */
	private AiHero hero = null;
	/** L'IA qui a réalisé l'appel */
	private ArtificialIntelligence ai = null;
	/** Emplacement de départ pour la recherche de chemin */
	private AiLocation startLocation = null;
	/** Cases d'arrivée pour la recherche de chemin */
	private Set<AiTile> endTiles = new TreeSet<AiTile>();
	/** Frange courante */
	private PriorityQueue<AiSearchNode> queue = null;
	/** Indique si l'algorithme a atteint une des 3 limites définies (hauteur/cout/taille) */
	private boolean limitReached = false;
	/** Indique la hauteur courante de l'arbre de recherche */
	private int treeHeight = 0;
	/** Indique le coût maximal courant des chemins contenus dans l'arbre de recherche */
	private double treeCost = 0;
	/** Indique la taille courante (en noeuds) de l'arbre de recherche */
	private int treeSize = 0;
	
	/**
	 * Renvoie la frange courante de l'algorithme
	 * de recherche. Cette variable permet
	 * entres autres de reprendre le traitement
	 * après avoir trouver un premier résultat.
	 * Par conséquent, cette frange ne doit surtout pas
	 * être modifiée par l'utilisateur.
	 * 
	 * @return
	 * 		La frange courante de l'algorithme.
	 */
	public PriorityQueue<AiSearchNode> getQueue()
	{	return queue;
	}

	/**
	 * Indique si l'algorithme a atteint 
	 * une des 3 limites définies (hauteur/cout/taille).
	 * 
	 * @return
	 * 		{@code true} ssi l'algorithme a atteint une des 3 limites.
	 */
	public boolean isLimitReached()
	{	return limitReached;
	}

	/** 
	 * Renvoie la hauteur courante de l'arbre de recherche.
	 * 
	 * @return
	 * 		La hauteur courante de l'arbre de recherche.
	 */
	public int getTreeHeight()
	{	return treeHeight;
	}

	/** 
	 * Renvoie le coût maximal courant des chemins 
	 * contenus dans l'arbre de recherche
	 * 
	 * @return
	 * 		Le coût maximal courant.
	 */
	public double getTreeCost()
	{	return treeCost;
	}

	/** 
	 * Renvoie la taille courante (en noeuds) 
	 * de l'arbre de recherche.
	 * 
	 * @return
	 * 		La taille courante (en noeuds) de l'arbre de recherche.
	 */
	public int getTreeSize()
	{	return treeSize;
	}

	/////////////////////////////////////////////////////////////////
	// LAST RESEARCH NODE	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le dernier noeud de recherche traité (si disponible) */
	private AiSearchNode lastSearchNode = null;

	/**
	 * Renvoie le dernier noeud de recherche exploré par A*.
	 * Il sera utilisé en cas de demande de poursuite de
	 * l'algorithme, donc il ne faut surtout pas le modifier.
	 * 
	 * @return
	 * 		Le dernier noeud de recherche exploré par A*.
	 */
	public AiSearchNode getLastSearchNode()
	{	return lastSearchNode;
	}
	
	/**
	 * Renvoie la zone correspondant au
	 * dernier noeud exploré par A*.
	 * 
	 * @return
	 * 		La zone associée au dernier noeud parcouru.
	 */
	public AiZone getLastZone()
	{	AiZone result = null;
		if(lastSearchNode!=null)
			result = lastSearchNode.getLocation().getTile().getZone();
		return result;
	}
	
	/**
	 * Renvoie l'emplacement correspondant au
	 * dernier noeud exploré par A*.
	 * 
	 * @return
	 * 		L'emplacement associé au dernier noeud parcouru.
	 */
	public AiLocation getLastLocation()
	{	AiLocation result = null;
		if(lastSearchNode!=null)
			result = lastSearchNode.getLocation();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de hauteur (négatif = pas de limite) */
	private int maxHeight = -1;
	/** limite de coût (négatif = pas de limite) */
	private double maxCost = -1;
	/** limite de nombre de noeuds (négatif = pas de limite), pas configurable */
	private int maxNodes = 10000;
	
	/**
	 * Limite l'arbre de recherche à une hauteur de {@code maxHeight},
	 * i.e. quand le noeud courant a une profondeur correspondant à maxHeight,
	 * l'algorithme se termine et ne renvoie pas de solution (échec).<br/>
	 * Dans des cas extrêmes, l'arbre peut avoir une hauteur considérable,
	 * ce qui peut provoquer un dépassement mémoire. Ce paramètre permet d'éviter
	 * de déclencher ce type d'exception. A noter qu'un paramètre non-configurable
	 * limite déjà le nombre de noeuds dans l'arbre.<br/>
	 * Par défaut, ce paramètre est initialisé avec la valeur {@code hauteur+largeur},
	 * où {@code hauteur} et {@code largeur} sont les dimensions de la zone. En effet,
	 * bien que ça soit possible, il est très peu probable d'avoir besoin d'un
	 * chemin qui traverserait l'intégralité de la zone, en diagonale (ici la longueur
	 * du chemin est exprimée en distance de Manhattan).
	 * 
	 * @param maxHeight
	 * 		Hauteur maximale de l'arbre de recherche.
	 */
	public void setMaxHeight(int maxHeight)
	{	this.maxHeight = maxHeight;	
	}
		
	/**
	 * Limite l'arbre de recherche à un certain coût {@code maxCost}, i.e. dès que le
	 * noeud courant atteint ce cout maximal, l'algorithme se termine et ne
	 * renvoie pas de solution (échec). <br/>
	 * Dans des cas extrêmes, l'arbre peut avoir une hauteur considérable,
	 * ce qui peut provoquer un dépassement mémoire. Ce paramètre permet d'éviter
	 * de déclencher ce type d'exception. A noter qu'un paramètre non-configurable
	 * limite déjà le nombre de noeuds dans l'arbre.
	 * 
	 * @param maxCost	
	 * 		Le coût maximal que le noeud courant peut atteindre.
	 */
	public void setMaxCost(int maxCost)
	{	this.maxCost = maxCost;
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
		this.endTiles = endTiles;
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
	 * pour trouver un autre chemin. <br/>
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
		
		long before = System.currentTimeMillis();
		if(verbose)
		{	System.out.println(before+"++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.print("A*: from "+startLocation+" to [");
			for(AiTile tile: endTiles)
				System.out.print(" "+tile);
			System.out.println(" ]");
			System.out.println(before+"++++++++++++++++++++++++++++++++++++++++++++++");
		}		
		
		boolean found = false;
		// traitement
		if(!endTiles.isEmpty())
		{	do
			{	ai.checkInterruption();
				long before1 = System.currentTimeMillis();
				// on prend le noeud situé en tête de file
				lastSearchNode = queue.poll();
				if(verbose)
				{	System.out.println("Visited : "+lastSearchNode.toString());
					System.out.println("Queue length: "+queue.size());
					System.out.println("Zone:\n"+lastSearchNode.getLocation().getTile().getZone());
				}
				// on teste si on est arrivé à la fin de la recherche
				if(endTiles.contains(lastSearchNode.getLocation().getTile()))
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
				else
				{	// sinon on récupére les noeuds suivants
					long before2 = System.currentTimeMillis();
					List<AiSearchNode> successors = lastSearchNode.getChildren();
					long after2 = System.currentTimeMillis();
					long elapsed2 = after2 - before2;
					if(verbose)
						System.out.println("Child development: "+elapsed2+" ms");
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
				if(verbose)
					System.out.println("["+elapsed1+"]==============================================");
			}
			while(!queue.isEmpty() && !found && !limitReached);
		
			// build solution path
			if(found)
				result = finalNode.processPath();
		}
		
		long after = System.currentTimeMillis();
		long elapsed = after - before;
		if(verbose)
		{	System.out.print(after+"Path: [");
			if(limitReached)
				System.out.println(" limit reached");
			else if(found)
			{	for(AiLocation loc: result.getLocations())
					System.out.print(" "+loc);
			}
			else if(endTiles.isEmpty())
				System.out.print(" endTiles parameter empty");
			else 
				System.out.print(" no solution found");
			System.out.println(" ]");
			System.out.println("Elapsed time: "+elapsed+" ms");
			//
			System.out.print("height="+treeHeight+" cost="+treeCost+" size="+treeSize);
			System.out.print(" src="+root.getLocation());
			System.out.print(" trgt=");
			for(AiTile tile: endTiles) 
				System.out.print(" "+tile);
			System.out.println();
			if(result!=null) 
				System.out.print(" result="+result);
			System.out.println();
		}

		finish();
		if(limitReached)
			throw new LimitReachedException(startLocation,endTiles,treeHeight,treeCost,treeSize,maxCost,maxHeight,maxNodes,queue);
		else if(endTiles.isEmpty())
			throw new IllegalArgumentException("endTiles list must not be empty");
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement cet objet quand il n'est plus utilisé
	 */
	private void finish()
	{	
//		if(root!=null)
//		{	root.finish();
//			root = null;
//		}
//		ai = null;
//		costCalculator = null;
//		heuristicCalculator = null;
//		successorCalculator = null;
		root = null;
	}
}
