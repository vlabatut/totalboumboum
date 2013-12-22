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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimeFullSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Implémentation de l'<a href="http://fr.wikipedia.org/wiki/Algorithme_A*">algorithme A*</a> adapté au
 * cas où on a le choix entre plusieurs objectifs alternatifs. S'il y a un seul objectif, 
 * cette implémentation correspond à peu près à un A* classique.
 * <br>
 * Il y a quand même une modification implémentée dans les fonctions successeurs,
 * puisque les noeuds d'état apparaissant déjà dans des noeuds de recherche ancêtres sont
 * écartés lorsqu'un noeud de recherche est développé. En d'autres termes, l'algorithme évite
 * de chercher des chemins qui passent plusieurs fois par la même case, ce qui l'empêche de
 * boucler à l'infini. La notion de 'noeud déjà exploré' est spécifiée par la fonction successeur
 * utilisée.
 * <br/>
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
	/** Indique si on est en train d'essayer de revenir au point de départ */
	private boolean searchLoop = false;

    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Calcule le plus court chemin pour aller de la position {@code startLocation} 
	 * à la case {@code endTile}, en utilisant l'algorithme A*. La méthode
	 * peut avoir 3 comportement différents, en fonction du résultat de la recherche :
	 * <ul>
	 * 		<li>Si un chemin optimal peut être trouvé entre les cases de départ et de destination,
	 * 			alors ce chemin est renvoyé. Si plusieurs chemins optimaux existent, seulement l'un
	 * 			d'entre eux est renvoyé.</li>
	 * 		<li>S'il n'y a aucun chemin entre les cases de départ et de destination, alors
	 * 			c'est la valeur {@code null} qui est renvoyée.</li>
	 * 		<li>Si jamais l'algorithme atteint une limite de coût/taille, une {@link LimitReachedException}
	 * 			est levée. Cela ne signifie pas forcément qu'aucun chemin n'existe entre les cases.
	 * 			Cela veut simplement dire que l'algorithme n'a pas été capable de trouver ce chemin,
	 * 			ni de montrer qu'il n'en existait aucun. Il a été interrompu avant par un méchanisme
	 * 			déclenché quand l'algorithme consomme trop de mémoire. Cette surconsommation est
	 * 			généralement due à une mauvaise utilisation de A* (une mauvaise fonction de coût, par exemple).</li>
	 * </ul>
	 * Il est nécessaire de définir un traitement spécifique lorsque A* lève une {@code LimitReachedException}.
	 * En effet, dans ce cas là, il faut généralement relancer une recherche, en utilisant d'autres paramètres.
	 * 
	 * @param startLocation	
	 * 		La case de départ
	 * @param endTile	
	 * 		La case d'arrivée
	 * @return 
	 * 		Un chemin pour aller de {@code startTile} à {@code endTile}, ou la valeur {@code null}.
	 * 
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath startProcess(AiLocation startLocation, AiTile endTile) throws LimitReachedException
	{	Set<AiTile> endTiles = new TreeSet<AiTile>();
		endTiles.add(endTile);
		AiPath result = startProcess(startLocation,endTiles);
		return result;
	}
	
	/**
	 * Calcule le plus court chemin pour aller de la case {@code startTile} à 
	 * l'une des cases contenues dans la liste {@code endTiles} (n'importe laquelle),
	 * en utilisant l'algorithme A*. En raison du caractère optimal de la solution
	 * recherchée, il s'agira de la case la plus proche (ou rapide à atteindre,
	 * suivant la fonction de coût utilisée). 
	 * <br/>
	 * Si jamais aucun chemin n'est trouvé, alors c'est {@code null} qui est renvoyé. 
	 * Si jamais l'algorithme atteint une limite de coût/taille, une {@link LimitReachedException} 
	 * est levée. Dans ce cas-là, c'est qu'il y a généralement un problème dans le façon 
	 * dont A* est employé (mauvaise fonction de coût, par exemple). Voir la documentation
	 * de la méthode {@link #startProcess(AiLocation, AiTile)} pour plus de détails.
	 * A noter qu'une {@code IllegalArgumentException} est levée si la liste {@code endTiles} est vide.
	 * 
	 * @param startLocation	
	 * 		La case de départ.
	 * @param endTiles	
	 * 		L'ensemble des cases d'arrivée possibles.
	 * @return 
	 * 		un chemin pour aller de {@code startTile} à une des cases de {@code endTiles},
	 * 		ou un chemin vide, ou la valeur {@code null}.
	 * 
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés).
	 * @throws IllegalArgumentException
	 * 		Si la liste des cases de destination est vide.
	 */
	public AiPath startProcess(AiLocation startLocation, Set<AiTile> endTiles) throws LimitReachedException
	{	// on vérifie que les cases/emplacement sont bien définies dans les mêmes instances de zones
		AiZone zone1 = startLocation.getZone();
		for(AiTile endTile: endTiles)
		{	AiZone zone2 = endTile.getZone();
			if(zone1!=zone2)
				throw new IllegalArgumentException("The starting and ending points do not belong to the same zone instance (which they should): "+startLocation+" vs. "+endTile);
		}
				
		// on réinitialise la case de départ
		this.startLocation = startLocation;
		heuristicCalculator.setEndTiles(endTiles);
		successorCalculator.setEndTiles(endTiles);
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
	 * calculer plus vite le résultat de ce nouvel appel.
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
	 * 		Un chemin pour aller à {@code endTile}, ou la valeur {@code null}
	 * 		si aucun chemin n'a pu être trouvé.
	 * 
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 * TODO à appeler "restart"
	 */
	public AiPath startProcess(AiTile endTile) throws LimitReachedException
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
	 * 		ou la valeur {@code null}, si aucun chemin n'existe.
	 * 
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 * @throws IllegalArgumentException
	 * 		Si la liste des cases de destination est vide.
	 * TODO à appeler "restart"
	 */
	public AiPath startProcess(Set<AiTile> endTiles) throws LimitReachedException
	{	// on teste d'abord si l'algorithme a au moins été appliqué une fois,
		// sinon la case de départ n'est pas connue. Dans ce cas, on lève une NullPointerException.
		if(root==null)
			throw new NullPointerException("The algorithm must be called at least once with processShortestPath(AiLocation,...) for init purposes");
		
		// on vérifie que les cases/emplacements sont bien définies dans les mêmes instances de zones
		AiZone zone1 = startLocation.getZone();
		for(AiTile endTile: endTiles)
		{	AiZone zone2 = endTile.getZone();
			if(zone1!=zone2)
				throw new IllegalArgumentException("The starting and ending points do not belong to the same zone instance (which they should): "+startLocation+" vs. "+endTile);
		}
		
		// initialisation
		this.endTiles = endTiles;
		treeHeight = 0;
		treeCost = 0;
		fringeSize = 0;
		lastSearchNode = null;
		
		// heuristique
		Set<AiTile> et = heuristicCalculator.getEndTiles();
		if(!et.equals(endTiles))
		{	heuristicCalculator.setEndTiles(endTiles);
			root.updateHeuristic();
		}

		// frange et racine
		resetFringe();
		insertNodeInFringe(root);
		
		// traitement
		AiPath result = continueProcess();
		return result;
	}
	
	/**
	 * Calcule le plus court chemin partant de la position {@code startLocation} 
	 * et retournant à la même case, en utilisant l'algorithme A*. Comme pour les
	 * autres méthodes, on peut obtenir trois résultats différents : un chemin
	 * commençant et finissant par la case de départ, la valeur {@code null} si aucun
	 * chemin n'existe, et enfin la méthode peut lever une {@link LimitReachedException}
	 * si A* consomme trop de mémoire lors de la recherche. Cf. la documentation de
	 * {@link #startProcess(AiLocation, AiTile)} pour plus de détails.
	 * <br/>
	 * Cette méthode est particulièrement utile quand l'agent se trouve déjà dans sa case
	 * de destination préférée, mais que celle-ci est menacée. Il est alors nécessaire
	 * qu'il se déplace, selon un chemin qui lui permettra finalement de revenir dans
	 * cette case.
	 * <br/>
	 * A noter que la configuration de la fonction successeur a un rôle important, ici. 
	 * En effet, si vous sélectionnez par exemple {@link TimeFullSuccessorCalculator}
	 * ou {@link TimePartialSuccessorCalculator} avec un mode restrictif (MODE_NOTREE 
	 * ou MODE_NOBRANCH), il est peu probable que cette méthode trouve un chemin. En effet, 
	 * ces modes restrictifs interdisent la construction de chemins contenant plusieurs fois 
	 * la même case. Or, puisqu'on cherche ici à revenir sur la case de départ, il est 
	 * probable qu'on doive réutiliser certaines cases, ou au moins la toute première. La 
	 * même remarque s'applique aux successeurs qui ne tiennent pas compte du temps 
	 * ({@link ApproximateSuccessorCalculator} et {@link BasicSuccessorCalculator}).
	 * 
	 * @param startLocation	
	 * 		La position de départ
	 * @return 
	 * 		Un chemin pour sortir de la case de départ puis y revenir.
	 * 
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath processLoopPath(AiLocation startLocation) throws LimitReachedException
	{	// avertissements possibles
		SearchMode mode = null;
		if(successorCalculator instanceof TimeFullSuccessorCalculator)
			mode =  ((TimeFullSuccessorCalculator)successorCalculator).getSearchMode();
		else if(successorCalculator instanceof TimePartialSuccessorCalculator)
			mode = ((TimePartialSuccessorCalculator)successorCalculator).getSearchMode();
		if(mode!=null && mode!=SearchMode.MODE_ALL && mode!=SearchMode.MODE_ONEBRANCH)
			print("WARNING: a time-based successor function is used with the "+mode+" search mode, when looking for a loop path. These settings are most certainly not appropriate.");
		if(successorCalculator instanceof BasicSuccessorCalculator || successorCalculator instanceof ApproximateSuccessorCalculator)
			print("WARNING: a time-based successor function is necessary when looking for a loop path (so no ApproximateSuccessorCalculator or BasicSuccessorCalculator.");
		
		// on indique qu'on recherche un cycle
		searchLoop = true;
		
		// on recherche le chemin
		Set<AiTile> endTiles = new TreeSet<AiTile>();
		AiTile endTile = startLocation.getTile();
		endTiles.add(endTile);
		AiPath result = startProcess(startLocation,endTiles);
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
	 * @throws LimitReachedException
	 * 		L'algorithme a développé un arbre trop grand (il y a
	 * 		vraisemblablement un problème dans les paramètres/fonctions utilisés). 
	 */
	public AiPath continueProcess() throws LimitReachedException
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
		{	insertNodeInFringe(lastSearchNode);
			print("           Fringe length: "+fringe.size());
			printQueue("             + ",fringe);
		}
	
		// initialisation
		int it = 0;
		AiPath result = null;
		AiSearchNode finalNode = null;
		lastSearchNode = null;
		boolean found = false;
		limitReached = false;
		boolean firstIteration = true;

		// traitement
		if(!endTiles.isEmpty() && !fringe.isEmpty())
		{	do
			{	ai.checkInterruption();
				
				// verbose : iteration
				it ++;
				long before1 = print("         -- starting iteration #" + it + " --");
				
				// on prend le noeud situé en tête de file
				lastSearchNode = fringe.poll();
				// verbose : noeud courant
				print("           Zone:\n"+lastSearchNode.getZoneRepresentation());
				print("           Visiting : "+lastSearchNode);
				
				// mise à jour des données décrivant l'arbre
				//if(lastSearchNode.getDepth()>treeHeight)
					treeHeight = lastSearchNode.getDepth();
				//if(lastSearchNode.getCost()>treeCost)
					treeCost = lastSearchNode.getCost();
				//if(fringe.size()>fringeSize)
					fringeSize = fringe.size();
				
				// on teste si on est arrivé à la fin de la recherche
				if((!firstIteration || !searchLoop)	// cas particulier : premier noeud d'une recherche de cycle = noeud d'arrivée et de départ 
					&& endTiles.contains(lastSearchNode.getLocation().getTile()))
				{	// si oui on garde le dernier noeud pour ensuite pouvoir reconstruire le chemin solution
					finalNode = lastSearchNode;
					found = true;
				}
				
				// si l'arbre a atteint la hauteur maximale, on s'arrête
				else if(maxHeight>0 && treeHeight>=maxHeight)
					limitReached = true;
				// si le noeud courant a atteint le coût maximal, on s'arrête
				else if(maxCost>0 && treeCost>=maxCost)
					limitReached = true;
				// si le nombre de noeuds dans la file est trop grand, on s'arrête
				else if(maxNodes>0 && fringeSize>=maxNodes)
					limitReached = true;
				
				// sinon on récupére les noeuds suivants
				else
				{	// développement (on copie la liste des fils, qui est sinon immuable)
					long before2 = ai.getCurrentTime();
					List<AiSearchNode> successors = new ArrayList<AiSearchNode>(lastSearchNode.getChildren());
					
					// dans le cas de la première itération de la recherche d'un cycle,
					// on retire le fils correspondant à la même case (action d'attente)
					// afin de forcer la sortie de case.
					if(searchLoop && firstIteration)
					{	Iterator<AiSearchNode> itemp = successors.iterator();
						boolean fnd = false;
						while(!fnd && itemp.hasNext())
						{	AiSearchNode child = itemp.next();
							AiTile t = child.getLocation().getTile();
							if(endTiles.contains(t))
							{	itemp.remove();
								fnd = true;
							}
						}
					}
					
					// verbose : temps
					{	long after2 = ai.getCurrentTime();
						long elapsed2 = after2 - before2;
						print("           Child development: duration="+elapsed2+" ms");
						for(AiSearchNode c: successors)
							print("             + " + c);
					}
					
//					Collections.shuffle(successors);
					// on rajoute les fils dans la file de priorité
					for(AiSearchNode node: successors)
						insertNodeInFringe(node);
				}
				
				// verbose : file
				{	print("           Fringe length: "+fringeSize);
					printQueue("             + ",fringe);
				}
				//  verbose : iteration
				{	long after1 = ai.getCurrentTime();
					long elapsed1 = after1 - before1;
					print("         -- iteration #" + it + " finished, duration=" + elapsed1 + " --");
				}
				
				firstIteration = false;
			}
			while(!fringe.isEmpty() && !found && !limitReached);
		
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
		{	msg = "         height="+treeHeight+" cost="+treeCost+" size="+fringeSize;
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
		{	//if(result!=null) 
				print("         result="+result);
			print("      < A* finished +++++++++++++++++++++");
		}
		
		// exceptions
		if(limitReached)
			throw new LimitReachedException(startLocation,endTiles,treeHeight,treeCost,fringeSize,maxCost,maxHeight,maxNodes,fringe);
		else if(endTiles.isEmpty())
		{	PredefinedColor color = ai.getZone().getOwnHero().getColor();
			throw new IllegalArgumentException("endTiles list must not be empty ("+color+" player)");
		}
		
		return result;
	}
}
