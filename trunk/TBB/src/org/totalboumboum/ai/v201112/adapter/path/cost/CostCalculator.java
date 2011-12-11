package org.totalboumboum.ai.v201112.adapter.path.cost;

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

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiSearchNode;

/**
 * Permet de définir une fonction de coût 
 * nécessaire aux algorithmes de recherche
 * du type A* ou Dijkstra.
 * 
 * @author Vincent Labatut
 */
public abstract class CostCalculator
{
	/**
	 * Construit une fonction de coût
	 * utilisant l'IA passée en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		IA de référence.
	 */
	public CostCalculator(ArtificialIntelligence ai)
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
	protected ArtificialIntelligence ai = null;
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Calcule le coût de l'action consistant à aller de l'emplacement
	 * contenu dans {@code start} à l'emplacement {@code end}, sachant 
	 * que les deux cases correspondantes doivent être voisines.
	 * <br/>
	 * Il est possible de définir des coûts évolués, en tenant compte par exemple des
	 * influences négatives dans ces cases (pour le joueur) comme la prèsence de bombes 
	 * à proximité, etc., et des influences positives telles que la prèsence de bonus.
	 * Si les deux cases ne sont pas voisines, le résultat est indéterminé.
	 * 
	 * @param currentNode
	 * 		Le noeud contenant l'emplacement de départ. 
	 * @param nextLocation
	 * 		L'emplacement d'arrivée (case voisine de la case courante).
	 * @return	
	 * 		Le coût du déplacement entre les deux emplacements.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public abstract double processCost(AiSearchNode currentNode, AiLocation nextLocation) throws StopRequestException;
	
	/**
	 * Calcule le coût d'un chemin, i.e. la somme des coûts des actions
	 * consistant à passer d'un emplacement du chemin au suivant.
	 * 
	 * @param path
	 * 		Chemin à traiter
	 * @return
	 * 		Le coût de ce chemin.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
/*	public double processCost(AiPath path) throws StopRequestException
	{	double result = 0;
		AiTile previous = null;
		
		for(AiTile tile: path.getLocations())
		{	if(previous==null)
				previous = tile;
			else
			{	double localCost = processCost(previous,tile);
				result = result + localCost;
				previous = tile;
			}			
		}
		return result;
	}
*/
}
