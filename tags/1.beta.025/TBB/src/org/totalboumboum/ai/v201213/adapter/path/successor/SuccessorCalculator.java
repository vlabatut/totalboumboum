package org.totalboumboum.ai.v201213.adapter.path.successor;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiSearchNode;

/**
 * Permet de définir une fonction successeur utilisée par un algorithme
 * de recherche lors de la recherche d'un plus court chemin, pour 
 * développer un état.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
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
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Cases déjà traitées */
	protected final Map<AiSearchNode,Map<AiTile,AiSearchNode>> processedTiles = new HashMap<AiSearchNode,Map<AiTile,AiSearchNode>>();
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
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public abstract List<AiSearchNode> processSuccessors(AiSearchNode node) throws StopRequestException;
	
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
