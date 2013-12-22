package org.totalboumboum.ai.v201314.adapter.path.cost;

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

import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiSearchNode;
import org.totalboumboum.tools.images.PredefinedColor;

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
	 * utilisant l'agent passé en paramètre
	 * pour gérer les interruptions.
	 * 
	 * @param ai
	 * 		Agent de référence.
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
	/** L'agent associé */
	protected ArtificialIntelligence ai = null;
	
	/////////////////////////////////////////////////////////////////
	// OPPONENT COST	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le coût <i>supplémentaire</i> associé à une case contenant un personnage adverse */
	protected double opponentCost = 0;
	
	/**
	 * Change le côut <i>supplémentaire</i> associé à une 
	 * case contenant un joueur adverse.
	 * <br/>
	 * Utiliser un côut non-nul permet de préférer des chemins 
	 * qui ne contiennent pas d'adversaire, tout en conservant
	 * la possibilité d'explorer des chemins contenant des adversaires
	 * s'il n'y a pas d'autre possibilité.
	 * <br/>
	 * Le côut est nul par défaut. Il doit être exprimé dans
	 * exactement la même unité que celle manipulée par ce
	 * {@code CostCalculator}.
	 * 
	 * @param opponentCost
	 * 		Le coût <i>supplémentaire</i> associé à un joueur adverse.
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le coût passé en paramètre est négatif.
	 */
	public void setOpponentCost(double opponentCost)
	{	if(opponentCost<0)
		{	PredefinedColor color = ai.getZone().getOwnHero().getColor();
			throw new IllegalArgumentException("The opponent cost in CostCalculator cannot be negative ("+color+" player).");
		}
		this.opponentCost = opponentCost;
	}
	
	/////////////////////////////////////////////////////////////////
	// MALUS COST		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le coût <i>supplémentaire</i> associé à une case contenant un malus */
	protected double malusCost = 0;

	/**
	 * Change le côut <i>supplémentaire</i> associé à une 
	 * case contenant un malus.
	 * <br/>
	 * Utiliser un coût non-nul permet de préférer des chemins 
	 * qui ne contiennent pas de malus, tout en conservant
	 * la possibilité d'explorer des chemins contenant des malus
	 * s'il n'y a pas d'autre possibilité.
	 * <br/>
	 * Le coût est nul par défaut. Il doit être exprimé dans
	 * exactement la même unité que celle manipulée par ce
	 * {@code CostCalculator}.
	 * 
	 * @param malusCost
	 * 		Le coût <i>supplémentaire</i> associé à un malus.
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le coût passé en paramètre est négatif.
	 */
	public void setMalusCost(double malusCost)
	{	if(malusCost<0)
		{	PredefinedColor color = ai.getZone().getOwnHero().getColor();
			throw new IllegalArgumentException("The malus cost in CostCalculator cannot be negative ("+color+" player).");
		}
		this.malusCost = malusCost;
	}
	
	/////////////////////////////////////////////////////////////////
	// EXTRA COSTS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Matrice utilisée pour déterminer le coût d'une action */
	protected double tileCosts[][] = null;
	
	/**
	 * Change le côut <i>supplémentaire</i> associé à chaque case 
	 * de la zone de jeu. Ce coût s'additionne au coût des actions.
	 * <br/>
	 * Utiliser un côut non-nul permet de préférer des chemins 
	 * qui ne contiennent pas ces cases-là, tout en conservant
	 * la possibilité d'explorer des chemins qui les contiennent,
	 * s'il n'y a pas d'autre possibilité.
	 * <br/>
	 * Un côut nul signifie que seul le côut de l'action est considéré.
	 * Par défaut, la matrice vaut {@code null}, ce qui signifie qu'aucun
	 * coût supplémentaire n'est défini pour les cases. Ces côuts doivent 
	 * être exprimés dans exactement la même unité que celle manipulée par 
	 * ce {@code CostCalculator}.
	 * <br/>
	 * Tous les coûts supplémentaires (y compris ceux des adversaires et
	 * des malus), s'ils sont définis, s'ajoutent.
	 * 
	 * @param tileCosts
	 * 		Une matrice contenant les coûts <i>supplémentaire</i> associés à chaque case.
	 * 		Cette matrice doit avoir la même dimension que la zone de jeu.
	 * 
	 * @throws IllegalArgumentException
	 * 		Si de coûts sont négatifs, ou si la matrice n'a pas la bonne dimension.
	 */
	public void setTileCosts(double tileCosts[][])
	{	PredefinedColor color = ai.getZone().getOwnHero().getColor();
			
		// check matrix dimensions
		AiZone zone = ai.getZone();
		int rows = zone.getHeight();
		int mRows = tileCosts.length;
		if(mRows!=rows)
			throw new IllegalArgumentException("The tile cost matrix does not have the appropriate dimension: "+mRows+" rows instead of "+rows+" ("+color+" player).");
		int cols = zone.getWidth();
		int mCols = tileCosts[0].length;
		if(mRows!=rows)
			throw new IllegalArgumentException("The tile cost matrix does not have the appropriate dimension: "+mCols+" columns instead of "+cols+" ("+color+" player).");
		this.tileCosts = new double[rows][cols];
				
		// check matrix values
		for(int i=0;i<mRows;i++)
		{	for(int j=0;j<mCols;j++)
			{	if(tileCosts[i][j]<0)
					throw new IllegalArgumentException("The tile cost in CostCalculator cannot be negative ("+color+" player).");
				else
					this.tileCosts[i][j] = tileCosts[i][j];
			}
		}
	}

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
	 */
	public abstract double processCost(AiSearchNode currentNode, AiLocation nextLocation);
	
	/**
	 * Calcule le coût d'un chemin, i.e. la somme des coûts des actions
	 * consistant à passer d'un emplacement du chemin au suivant.
	 * 
	 * @param path
	 * 		Chemin à traiter
	 * @return
	 * 		Le coût de ce chemin.
	 */
/*	public double processCost(AiPath path)
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
