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

import java.util.PriorityQueue;

import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;

/**
 * Cette classe sert de base pour implémenter
 * des algorithmes de recherche dans des espaces
 * d'état. L'arbre de recherche obtenu est
 * construit en utilisant des objets de classe
 * {@link AiSearchNode}.
 * 
 * @author Vincent Labatut
 */
public abstract class AiAbstractSearchAlgorithm
{	
	/**
	 * Construit un objet permettant d'appliquer l'algorithme de recherche.
	 * 
	 * @param ai
	 * 		L'AI invoquant l'algorithme de recherche.
	 * @param hero
	 * 		Le personnage à considérer pour les déplacements.
	 * @param costCalculator
	 * 		La fonction de coût.
	 * @param heuristicCalculator
	 * 		La fonction heuristique.
	 * @param successorCalculator
	 * 		La fonction successeur.
	 */
	public AiAbstractSearchAlgorithm(ArtificialIntelligence ai, AiHero hero, CostCalculator costCalculator, HeuristicCalculator heuristicCalculator, SuccessorCalculator successorCalculator)
	{	// champs de base
		this.ai = ai;
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
		
		// pour la sortie texte
		colorStr = zone.getOwnHero().getColor().toString();
	}
	
	/**
	 * Construit un objet permettant d'appliquer l'algorithme de recherche.
	 * Cet objet en utilisant un arbre de recherche existant.
	 * Le noeud de recherche reçu doit correspondre à la racine de cet
	 * arbre. Le fait d'utiliser un arbre existant peut accélérer
	 * grandement la recherche de chemin.
	 * 
	 * @param root
	 * 		La racine d'un arbre de recherche existant.
	 */
	public AiAbstractSearchAlgorithm(AiSearchNode root)
	{	// on crée l'objet de façon classique
		this(root.getAi(),root.getHero(),root.getCostCalculator(),root.getHeuristicCalculator(),root.getSuccessorCalculator());
		
		// on utilise l'arbre de recherche existant pour effectuer l'initialisation
		setRoot(root);
	}
	
    /////////////////////////////////////////////////////////////////
	// CALCULATORS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Fonction de coût */
	protected CostCalculator costCalculator = null;
	/** Fonction heuristique */
	protected HeuristicCalculator heuristicCalculator = null;
	/** Fonction successeur */
	protected SuccessorCalculator successorCalculator = null;

	/**
	 * Renvoie la fonction de coût affectée
	 * à cet algorithme de recherche.
	 * 
	 * @return
	 * 		La fonction de coût utilisée pour initialiser cet objet.
	 */
	public CostCalculator getCostCalculator()
	{	return costCalculator;
	}
	
	/**
	 * Renvoie la fonction heuristique affectée
	 * à cet algorithme de recherche.
	 * 
	 * @return
	 * 		La fonction heuristique utilisée pour initialiser cet objet.
	 */
	public HeuristicCalculator getHeuristicCalculator()
	{	return heuristicCalculator;
	}
	
	/**
	 * Renvoie la fonction successeur affectée
	 * à cet algorithme de recherche.
	 * 
	 * @return
	 * 		La fonction successeur utilisée pour initialiser cet objet.
	 */
	public SuccessorCalculator getSuccessorCalculator()
	{	return successorCalculator;
	}
	
    /////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Racine de l'arbre de recherche */
	protected AiSearchNode root = null;
	/** Personnage de référence */
	protected AiHero hero = null;
	/** L'IA qui a réalisé l'appel */
	protected ArtificialIntelligence ai = null;
	/** Emplacement de départ pour la recherche de chemin */
	protected AiLocation startLocation = null;
	/** Frange courante */
	protected PriorityQueue<AiSearchNode> queue = null;
	/** Indique si l'algorithme a atteint une des 3 limites définies (hauteur/coût/taille) */
	protected boolean limitReached = false;
	/** Indique la hauteur courante de l'arbre de recherche */
	protected int treeHeight = 0;
	/** Indique le coût maximal courant des chemins contenus dans l'arbre de recherche */
	protected double treeCost = 0;
	/** Indique la taille courante (en noeuds) de l'arbre de recherche */
	protected int treeSize = 0;
	
	/**
	 * Initialise cet objet en utilisant un arbre de recherche existant.
	 * Le noeud de recherche reçu doit correspondre à la racine de cet
	 * arbre. Le fait d'utiliser un arbre existant peut accélérer
	 * grandement la recherche de chemin.
	 * 
	 * @param root
	 * 		La racine d'un arbre de recherche existant.
	 */
	public void setRoot(AiSearchNode root)
	{	startLocation = root.getLocation();
		this.root = root;
	}
	
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
	 * l'une des 3 limites définies (hauteur/cout/taille).
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
	protected AiSearchNode lastSearchNode = null;

	/**
	 * Renvoie le dernier noeud de recherche exploré par l'algorithme.
	 * Il sera utilisé en cas de demande de poursuite de
	 * l'algorithme, donc il ne faut surtout pas le modifier.
	 * 
	 * @return
	 * 		Le dernier noeud de recherche exploré par l'algorithme.
	 */
	public AiSearchNode getLastSearchNode()
	{	return lastSearchNode;
	}
	
	/**
	 * Renvoie la zone correspondant au
	 * dernier noeud exploré par l'algorithme.
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
	 * dernier noeud exploré par l'algorithme.
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
	protected int maxHeight = -1;
	/** limite de coût (négatif = pas de limite) */
	protected double maxCost = -1;
	/** limite de nombre de noeuds (négatif = pas de limite), pas configurable */
	protected int maxNodes = 10000;
	
	/**
	 * Limite l'arbre de recherche à une hauteur de {@code maxHeight},
	 * i.e. quand le noeud courant a une profondeur correspondant à maxHeight,
	 * l'algorithme se termine et ne renvoie pas de solution (échec).
	 * <br/>
	 * Dans des cas extrêmes, l'arbre peut avoir une hauteur considérable,
	 * ce qui peut provoquer un dépassement mémoire. Ce paramètre permet d'éviter
	 * de déclencher ce type d'exception. A noter qu'un paramètre non-configurable
	 * limite déjà le nombre de noeuds dans l'arbre.
	 * <br/>
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
	 * renvoie pas de solution (échec).
	 * <br/>
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
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Préfixe affiché avant chaque message */
	private String colorStr;
	/** permet d'activer/désactiver la sortie texte lors du débogage */
	private boolean verbose = false;

	/**
	 * Permet d'activer/désactiver la sortie
	 * texte de cet algorithme (pour le débogage).
	 * 
	 * @param verbose
	 * 		La sortie est activée pour {@code true}.
	 */
	public void setVerbose(boolean verbose)
	{	this.verbose = verbose;
	}
	
	/**
	 * Cette méthode affiche à l'écran le message passé en paramètre,
	 * à condition que {@link #verbose} soit {@code true}. Elle
	 * préfixe automatiquement la couleur de l'agent et le moment
	 * de l'affichage. Utilisez-la pour tout affichage de message,
	 * car elle vous permet de désactiver tous vos messages simplement
	 * en faisant {@code verbose = false;}.
	 * 
	 * @param msg
	 * 		Le message à afficher dans la console.
	 * @return
	 * 		Le temps à l'instant de l'affichage.
	 */
	protected final long print(String msg)
	{	long time = System.currentTimeMillis();
		if(verbose)
		{	String prefix = "[" + time + ":" + colorStr + "]";
			String message = prefix + " " + msg;
			System.out.println(message);
		}
		return time;
	}

	/**
	 * L'affichage standard d'une file de priorité ne respecte pas
	 * l'ordre des éléments dans cette file. Cette méthode permet
	 * d'afficher la file dans l'ordre approprié.
	 *  
	 * @param prefix
	 * 		Le texte à afficher avant chaque élément de la file.
	 * @param queue
	 * 		La file à afficher.
	 */
	protected final void printQueue(String prefix, PriorityQueue<AiSearchNode> queue)
	{	if(verbose)
		{	PriorityQueue<AiSearchNode> q = new PriorityQueue<AiSearchNode>(queue);
			while(!q.isEmpty())
			{	AiSearchNode node = q.poll();
				print(prefix + node);	
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement cet objet quand il n'est plus utilisé
	 */
	protected void finish()
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
