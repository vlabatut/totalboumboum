package org.totalboumboum.ai.v201213.adapter.path.search;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;

/**
 * Implémentation de l'<a href="http://fr.wikipedia.org/wiki/Algorithme_A*">algorithme A*</a> adapté au
 * cas où on a le choix entre plusieurs objectifs alternatifs. S'il y a un seul objectif, 
 * cette implémentation correspond à peu près à un A* classique.
 * <br>
 * Il y a quand même une modification implémentée dans les fonctions successeurs,
 * puisque les noeuds d'état apparaissant déjà dans des noeuds de recherche ancêtre sont
 * écartés lorsqu'un noeud de recherche est développé. En d'autres termes, l'algorithme évite
 * de chercher des chemins qui passent plusieurs fois par la même case, ce qui l'empêche de
 * boucler à l'infini. La notion de 'noeud déjà exploré' est spécifiée par la fonction successeur
 * utilisée.
 * </br>
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
public final class Astar extends AiAbstractSearchAlgorithm
{	
	/**
	 * Construit un objet permettant d'appliquer l'algorithme A*,
	 * en utilisant les données et fonctions passées en paramètres.
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
	{	super(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
	}

	/**
	 * Construit un objet permettant d'appliquer l'algorithme A*,
	 * en utilisant l'arbre de recherche existant dont la racine
	 * est passée en paramètre. Cela revient à créer un objet
	 * en utilisant l'autre constructeur, puis à appliquer
	 * {@link #setRoot(AiSearchNode)}
	 * 
	 * @param root
	 * 		La racine de l'arbre de recherche existant.
	 */
	public Astar(AiSearchNode root)
	{	super(root);

		// on effectue le traitement propre à A*
		// > aucun pour l'instant
	}

    /////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Cases d'arrivée pour la recherche de chemin */
	private Set<AiTile> endTiles = new TreeSet<AiTile>();

    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Calcule le plus court chemin pour aller de la case {@code startTile} à 
	 * la case {@code endTile}, en utilisant l'algorithme A*. Si jamais aucun
	 * chemin n'est trouvé, alors un chemin vide est renvoyé. Si jamais
	 * l'algorithme atteint une limite de coût/taille, une {@link LimitReachedException}
	 * est levée. Dans ce cas là, c'est qu'il y a généralement un problème
	 * dans le façon dont A* est employé (mauvaise fonction de coût, par exemple). 
	 * 
	 * @param startLocation	
	 * 		La case de départ
	 * @param endTile	
	 * 		La case d'arrivée
	 * @return 
	 * 		Un chemin pour aller de {@code startTile} à {@code endTile}, ou un chemin vide, ou la valeur {@code null}.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath startProcess(AiLocation startLocation, AiTile endTile) throws StopRequestException, LimitReachedException
	{	Set<AiTile> endTiles = new TreeSet<AiTile>();
		endTiles.add(endTile);
		AiPath result = startProcess(startLocation,endTiles);
		return result;
	}
	
	/**
	 * Calcule le plus court chemin pour aller de la case {@code startTile} à 
	 * l'une des cases contenues dans la liste {@code endTiles} (n'importe laquelle),
	 * en utilisant l'algorithme A*. Si jamais aucun chemin n'est trouvé 
	 * alors un chemin vide est renvoyé. Si jamais l'algorithme atteint 
	 * une limite de coût/taille, une {@link LimitReachedException} est levée
	 * Dans ce cas-là, c'est qu'il y a généralement un problème dans le façon 
	 * dont A* est employé (mauvaise fonction de coût, par exemple).
	 * La fonction renvoie {@code null} si la liste {@code endTiles} est vide.
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
	public AiPath startProcess(AiLocation startLocation, Set<AiTile> endTiles) throws StopRequestException, LimitReachedException
	{	// on réinitialise la case de départ
		this.startLocation = startLocation;
		heuristicCalculator.setEndTiles(endTiles);
		root = new AiSearchNode(ai,startLocation,hero,costCalculator,heuristicCalculator,successorCalculator);
		costCalculator.init(root);
		heuristicCalculator.init(root);
		successorCalculator.init(root);
		
		// on lance le traitement
		AiPath result = startProcess(endTiles);
		return result;
	}
	
	/**
	 * Réalise le même traitement que {@link #startProcess(AiLocation, AiTile)},
	 * à la différence qu'ici on réutilise l'arbre de recherche déjà existant. 
	 * Autrement dit, on n'a pas besoin de préciser la case de départ, car elle 
	 * a déjà été initialisée précédemment. On va réutiliser ce travail fait
	 * lors d'un précédent appel (ou de plusieurs appels précédents) afin de 
	 * calculer plus vite le résultat.
	 * <br/>
	 * Cette méthode est également utilisable quand cet objet a été construit
	 * à partir d'un arbre existant avec {@link #Astar(AiSearchNode)},
	 * ou bien quand la méthode {@link #setRoot(AiSearchNode)} a été utilisée.
	 * <br/>
	 * <b>Attention </b>: avant d'appeler cette méthode, il est nécessaire que 
	 * l'arbre de recherche ait été initialisé avec l'une des méthodes citées 
	 * ci-dessus.
	 * 
	 * @param endTile	
	 * 		La case d'arrivée
	 * @return 
	 * 		Un chemin pour aller à {@code endTile}, 
	 * 		ou un chemin vide, ou la valeur {@code null}.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath startProcess(AiTile endTile) throws StopRequestException, LimitReachedException
	{	Set<AiTile> endTiles = new TreeSet<AiTile>();
		endTiles.add(endTile);
		AiPath result = startProcess(endTiles);
		return result;
	}
	
	/**
	 * Réalise le même traitement que {@link #startProcess(AiLocation, Set)},
	 * à la différence qu'ici on réutilise l'arbre de recherche déjà existant. 
	 * Autrement dit, on n'a pas besoin de préciser la case de départ, car elle 
	 * a déjà été initialisée précédemment. On va réutiliser ce travail fait
	 * lors d'un précédent appel (ou de plusieurs appels précédents) afin de 
	 * calculer plus vite le résultat.
	 * <br/>
	 * Cette méthode est également utilisable quand cet objet a été construit
	 * à partir d'un arbre existant avec {@link #Astar(AiSearchNode)},
	 * ou bien quand la méthode {@link #setRoot(AiSearchNode)} a été utilisée.
	 * <br/>
	 * <b>Attention </b>: avant d'appeler cette méthode, il est nécessaire que 
	 * l'arbre de recherche ait été initialisé avec l'une des méthodes citées 
	 * ci-dessus.
	 * 
	 * @param endTiles	
	 * 		L'ensemble des cases d'arrivée possibles.
	 * @return 
	 * 		Un chemin pour aller à l'une des cases de {@code endTiles},
	 * 		ou un chemin vide, ou la valeur {@code null}.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath startProcess(Set<AiTile> endTiles) throws StopRequestException, LimitReachedException
	{	// on teste d'abord si l'algorithme a au moins été appliqué une fois,
		// sinon la case de départ n'est pas connue. Dans ce cas, on lève une NullPointerException.
		if(root==null)
			throw new NullPointerException("The algorithm must be called at least once with processShortestPath(AiLocation,...) for init purposes");
		
		// initialisation
		this.endTiles = endTiles;
		treeHeight = 0;
		treeCost = 0;
		treeSize = 0;
		lastSearchNode = null;
		
		// heuristique
		Set<AiTile> et = heuristicCalculator.getEndTiles();
		if(!et.equals(endTiles))
		{	heuristicCalculator.setEndTiles(endTiles);
			root.updateHeuristic();
		}

		// file
		Comparator<AiSearchNode> comparator = new Comparator<AiSearchNode>()
		{	@Override
			public int compare(AiSearchNode o1, AiSearchNode o2)
			{	int result = o1.compareScoreTo(o2);
				return result;
			}
		};
		queue = new PriorityQueue<AiSearchNode>(1,comparator);
		queue.offer(root);
		
		// traitement
		AiPath result = continueProcess();
		return result;
	}
	
	/**
	 * Permet de continuer le traitement commencé par {@link #startProcess(AiLocation, AiTile) processShortestPath}.
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
	 * 		Un chemin différent de celui renvoyé par {@code processShortestPath}.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath continueProcess() throws StopRequestException, LimitReachedException
	{	//verbose
		long before = print("      > Starting/resuming A* +++++++++++++++++++++");
		String msg = "         searching paths from "+startLocation+" to [";
		for(AiTile tile: endTiles)
			msg = msg + " " + tile;
		msg = msg + " ]";
		print(msg);
		
		// on remet le dernier noeud (fautif) dans la file,
		// pour permettre éventuellement de continuer le traitement
		if(limitReached && lastSearchNode!=null)
		{	queue.offer(lastSearchNode);
			print("           Queue length: "+queue.size());
			printQueue("             + ",queue);
		}
	
		// initialisation
		int it = 0;
		AiPath result = null;
		AiSearchNode finalNode = null;
		lastSearchNode = null;
		boolean found = false;
		limitReached = false;

		// traitement
		if(!endTiles.isEmpty() && !queue.isEmpty())
		{	do
			{	ai.checkInterruption();
				
				// verbose : iteration
				it ++;
				long before1 = print("         -- starting iteration #" + it + " --");
				
				// on prend le noeud situé en tête de file
				lastSearchNode = queue.poll();
				// verbose : noeud courant
				AiZone zone = lastSearchNode.getLocation().getTile().getZone();
				print("           Zone:\n"+zone);
				print("           Visiting : "+lastSearchNode);
				
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
				
				// sinon on récupére les noeuds suivants
				else
				{	// développement
					long before2 = ai.getCurrentTime();
					List<AiSearchNode> successors = lastSearchNode.getChildren();
					
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
				
				// verbose : file
				{	print("           Queue length: "+queue.size());
					printQueue("             + ",queue);
				}
				//  verbose : iteration
				{	long after1 = ai.getCurrentTime();
					long elapsed1 = after1 - before1;
					print("         -- iteration #" + it + " finished, duration=" + elapsed1 + " --");
				}
			}
			while(!queue.isEmpty() && !found && !limitReached);
		
			// on construit le chemin solution
			if(found)
				result = finalNode.processPath();
		}
		
		// verbose : temps écoulé
		{	long after = ai.getCurrentTime();
			long elapsed = after - before;
			print("         Total elapsed time: "+elapsed+" ms");
		}
		// verbose : résultat
		{	msg = "         Path: [";
			if(limitReached)
				msg = msg + " limit reached";
			else if(found)
			{	for(AiLocation loc: result.getLocations())
					msg = msg + " " + loc;
			}
			else if(endTiles.isEmpty())
				msg = msg + " endTiles parameter empty";
			else
				msg = msg + " no solution found";
			msg = msg + " ]";
			print(msg);
		}
		// verbose : limites
		{	msg = "         height="+treeHeight+" cost="+treeCost+" size="+treeSize;
			msg = msg + " src="+root.getLocation();
			msg = msg + " trgt={";
			for(AiTile tile: endTiles) 
				msg = msg + " " + tile;
			msg = msg + " }";
			print(msg);
			if(limitReached)
				print("         maxHeight="+maxHeight+" maxCost="+maxCost+" maxSize="+maxNodes);
		}
		// verbose : fin
		{	if(result!=null) 
				print("         result="+result);
			print("      < A* finished +++++++++++++++++++++");
		}
		
		// exceptions
		if(limitReached)
			throw new LimitReachedException(startLocation,endTiles,treeHeight,treeCost,treeSize,maxCost,maxHeight,maxNodes,queue);
		else if(endTiles.isEmpty())
			throw new IllegalArgumentException("endTiles list must not be empty");
		
		return result;
	}
}
