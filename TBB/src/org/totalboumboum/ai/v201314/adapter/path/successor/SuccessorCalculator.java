package org.totalboumboum.ai.v201314.adapter.path.successor;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiState;
import org.totalboumboum.ai.v201314.adapter.data.AiStateName;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Permet de définir une fonction successeur utilisée par un algorithme
 * de recherche lors de la recherche d'un plus court chemin, pour 
 * développer un état.
 * 
 * @author Vincent Labatut
 */
public abstract class SuccessorCalculator
{	
	/**
	 * Construit une fonction successeur
	 * utilisant l'agent passé en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		Agent de référence.
	 */
	public SuccessorCalculator(ArtificialIntelligence ai)
	{	this.ai = ai;
	
		initDirectionOrder();
	}
	
	/**
	 * Réinitialise les structures internes de
	 * l'objet avant de commencer une nouvelle
	 * recherche.
	 * 
	 * @param root
	 * 		Le noeud de recherche racine de l'arbre de recherche.
	 */
	public void init(AiSearchNode root)
	{	// à surcharger si nécessaire
	}

	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Agent utilisant cette classe */
	protected ArtificialIntelligence ai = null;
	
	/////////////////////////////////////////////////////////////////
	// NEIGHBOR ORDER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ordre dans lequel les directions doivent être considérées */
	protected Direction orderedDirections[] = new Direction[4];
	
	/**
	 * Initialise l'ordre dans lequel les directions
	 * doivent être prises pour l'agent considéré.
	 * Cela permet de varier le comportement d'un
	 * agent à l'autre.
	 */
	private void initDirectionOrder()
	{	AiZone zone = ai.getZone();
		AiHero hero = zone.getOwnHero();
		String uuid = hero.getUuid();
		int value = Math.abs(uuid.hashCode());
		List<Direction> remainingDirs = new ArrayList<Direction>();
		for(Direction d: Direction.getPrimaryValues())
			remainingDirs.add(d);
		for(int i=0;i<4;i++)
		{	int idx = value % (4-i);
			orderedDirections[i] = remainingDirs.get(idx);
			remainingDirs.remove(idx);
		}
		
// forcing normal order		
orderedDirections[0] = Direction.DOWN;
orderedDirections[1] = Direction.LEFT;
orderedDirections[2] = Direction.RIGHT;
orderedDirections[3] = Direction.UP;
	}
	
	/////////////////////////////////////////////////////////////////
	// END TILES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ensemble des cases pouvant terminer le chemin */
	protected final Set<AiTile> endTiles = new TreeSet<AiTile>();	

	/**
	 * Initialise/modifie l'ensemble des cases 
	 * pouvant terminer le chemin recherché.
	 * Cette méthode est appelée automatiquement
	 * quand c'est nécessaire, depuis les algorithmes
	 * de recherche.
	 * 
	 * @param endTiles	
	 * 		Les cases terminant le chemin.
	 */
	public void setEndTiles(Set<AiTile> endTiles)
	{	this.endTiles.clear();
		this.endTiles.addAll(endTiles);
	}

	/**
	 * Renvoie l'ensemble des cases objectifs (i.e. les cases terminant
	 * le chemin recherché).
	 * 
	 * @return	
	 * 		L'ensemble des cases objectifs.
	 */
	public Set<AiTile> getEndTiles()
	{	return endTiles;	
	}

	/////////////////////////////////////////////////////////////////
	// OBSTACLES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Considérer les adversaires comme des obstacles */
	protected boolean considerOpponents = false;
	/** Considérer les malus comme des obstacles */
	protected boolean considerMaluses = false;
	
	/**
	 * Change le statut des joueurs adverses. Par défaut,
	 * ils ne sont pas considérés comme des obstacles
	 * infranchissables. Il est possible de changer ça
	 * grâce à cette méthode.
	 * <br/>
	 * Bien sûr, utiliser cette méthode en même temps que
	 * {@link CostCalculator#setOpponentCost(double)} n'aurait
	 * absolument aucun sens, puisque le coût supplémentaire
	 * serait ignoré.
	 * 
	 * @param considerOpponents
	 * 		{@@code true} pour considérer les adversaires comme des obstacles.
	 */
	public void setConsiderOpponents(boolean considerOpponents)
	{	this.considerOpponents = considerOpponents;
	}
	
	/**
	 * Indique si la case passée en paramètre contient
	 * au moins un adversaire pouvant être un obstacle.
	 * L'adversaire doit être encore en jeu.
	 * 
	 * @param tile
	 * 		Case à traiter.
	 * @return
	 * 		{@code true} ssi la case contient un adversaire encore en jeu.
	 */
	protected boolean containsOpponent(AiTile tile)
	{	boolean result = false;
		AiHero ownHero = ai.getZone().getOwnHero();
		List<AiHero> heroes = tile.getHeroes();
		Iterator<AiHero> it = heroes.iterator();
		while(!result && it.hasNext())
		{	AiHero hero = it.next();
			if(!hero.equals(ownHero))
			{	AiState state = hero.getState();
				AiStateName name = state.getName();
				result = name==AiStateName.STANDING || name==AiStateName.MOVING;
			}
		}
		return result;
	}
	
	/**
	 * Change le statut des items malus. Par défaut,
	 * ils ne sont pas considérés comme des obstacles
	 * infranchissables. Il est possible de changer ça
	 * grâce à cette méthode.
	 * <br/>
	 * Bien sûr, utiliser cette méthode en même temps que
	 * {@link CostCalculator#setMalusCost(double)} n'aurait
	 * absolument aucun sens, puisque le coût supplémentaire
	 * serait ignoré.
	 * 
	 * @param considerMaluses
	 * 		{@@code true} pour considérer les malus comme des obstacles.
	 */
	public void setConsiderMaluses(boolean considerMaluses)
	{	this.considerMaluses = considerMaluses;
	}
	
	/**
	 * Indique si la case passée en paramètre contient
	 * au moins un malus pouvant être un obstacle.
	 * Le malus doit être encore en jeu.
	 * 
	 * @param tile
	 * 		Case à traiter.
	 * @return
	 * 		{@code true} ssi la case contient un adversaire encore en jeu.
	 */
	protected boolean containsMalus(AiTile tile)
	{	boolean result = false;
		List<AiItem> items = tile.getItems();
		Iterator<AiItem> it = items.iterator();
		while(!result && it.hasNext())
		{	AiItem item = it.next();
			AiItemType type = item.getType();
			if(!type.isBonus())
			{	AiState state = item.getState();
				AiStateName name = state.getName();
				result =  name==AiStateName.STANDING || name==AiStateName.MOVING;
			}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Cases déjà traitées */
	protected final Map<AiSearchNode,Map<AiTile,AiSearchNode>> processedTiles = new HashMap<AiSearchNode,Map<AiTile,AiSearchNode>>();
	/** Nombre de fois qu'une case a été traitée */
	protected final Map<AiSearchNode,Map<AiTile,Integer>> processedTilesCounts = new HashMap<AiSearchNode,Map<AiTile,Integer>>();
	/** Version immuable de la map contenant les cases déjà traitées */
	protected final Map<AiSearchNode,Map<AiTile,AiSearchNode>> externalProcessedTiles = Collections.unmodifiableMap(processedTiles);

	/**
	 * Renvoie la map contenant les cases traitées lors de la recherche.
	 * Les clés sont les racines locales de l'arbre de recherche,
	 * et les valeurs sont des maps associant des cases à des noeuds de
	 * recherche (chaque noeud de recherche permet de récupérer le chemin
	 * optimal pour aller à la case considérée). Cette structure peut
	 * être utilisée après une recherche exhaustive, pour reconstruire
	 * tous les chemins possibles.
	 * <br/>
	 * A noter que si la notion de racine locale n'est pas pertinente
	 * (i.e. fonction successeur ne tenant pas compte du temps, et donc
	 * de l'attente), alors la map contient une seule entrée dont la clé
	 * est {@code null}.
	 * <br/>
	 * <b>Attention :</b> la map renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return
	 * 		Une map associant à chaque racine locale une map de couples cases/noeuds de recherche.
	 */
	public Map<AiSearchNode,Map<AiTile,AiSearchNode>> getProcessedTiles()
	{	return externalProcessedTiles;
	}
	
	/** 
	 * Calcule tous les états accessibles à partir du noeud de recherche
	 * passé en paramètre. On prend un noeud de recherche et non pas
	 * un état en paramètre, car le noeud de recherche contient des informations
	 * susceptibles d'éliminer certains successeurs potentiels.
	 * <br/>
	 * Par exemple, si le coût correspond au temps de déplacement, alors le coût du noeud
	 * de recherche courant correspond au temps nécessaire pour arriver à l'état
	 * correspondant. Certaines des cases accessibles depuis cet état peuvent être
	 * menacée par du feu, et le temps est une information cruciale pour déterminer
	 * si le personnage peut ou pas traverser une case avant qu'elle brûle.
	 * <br/>
	 * <b>Note :</b> la liste est générée à la demande, elle peut
	 * être modifiée sans causer de problème.
	 * 
	 * @param node	
	 * 		Le noeud de recherche courant.
	 * @return	
	 * 		La liste de noeuds fils obtenus.
	 */
	public abstract List<AiSearchNode> processSuccessors(AiSearchNode node);
	
	/**
	 * Détermine si la case du noeud passé en paramètre est menacé par une
	 * bombe. Cette méthode est utilisée pour rechercher un chemin vers
	 * une case non-menacée. Elle est placée dans {@link SuccessorCalculator},
	 * car ce dernier contient souvent un modèle utilisé pour la simulation.
	 * 
	 * @param node
	 * 		Le noeud de recherche à traiter.
	 * @return
	 * 		{@code true} ssi la case n'est pas menacée.
	 */
	public abstract boolean isThreatened(AiSearchNode node);

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie une représentation textuelle de la
	 * zone associée au noeud de recherche spécifié.
	 * 
	 * @param node
	 * 		Le noeud dont on veut la représentation textuelle de la zone.
	 * @return
	 * 		Une chaîne de caractères représentant la zone associée au noeud.
	 */
	public abstract String getZoneRepresentation(AiSearchNode node);
}
