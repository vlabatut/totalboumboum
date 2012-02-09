package org.totalboumboum.ai.v201213.adapter.path.cost;

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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.BasicSuccessorCalculator;

/**
 * Classe étendant la classe abstraite {@link CostCalculator} grâce à une matrice de coûts.
 * Ici, le coût pour passer d'une case à l'autre dépend uniquement de la case
 * de destination. Ce coût est égal à la valeur associée à la case dans la matrice
 * de cout fournie. Cette matrice doit faire la même taille que la zone de jeu.
 * En d'autres termes, le coût d'un déplacement dépend ici uniquement de la case de destination.
 * </br>
 * Cette classe est utile si on veut calculer des coûts plus fins qu'avec BasicCostCalculator,
 * qui considère seulement la distance. Par exemple, on peut donner un coup plus important
 * à l'action de passer dans une case qui est à portée d'une bombe susceptible d'exploser, ou bien 
 * un coût infini (avec {@link Double#POSITIVE_INFINITY}) à l'action de passer dans une case 
 * qu'on veut interdire au personnage parce qu'elle est trop dangereuse.
 * <br/>
 * La classe est compatible avec :
 * <ul>
 * 		<li>Fonction heuristiques :
 * 			<ul>
 * 				<li>{@link NoHeuristicCalculator}</li>
 * 				<li>{@link TileHeuristicCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonctions successeurs :
 * 			<ul>
 * 				<li>{@link BasicSuccessorCalculator}</li>
 * 			</ul>
 * 		</li> 
 * </ul>
 * 
 * @author Vincent Labatut
 */
public class MatrixCostCalculator extends CostCalculator
{
	/**
	 * Initialise la fonction de coût. On doit obligatoirement
	 * fournir la matrice de cout correspondante.
	 * <br/>
	 * <b>Attention :</b> cette matrice doit avoir la même taille que la zone de jeu.
	 * 
	 * @param ai
	 * 		IA de référence.
	 * @param costMatrix	
	 * 		la matrice de coût
	 */
	public MatrixCostCalculator(ArtificialIntelligence ai, double costMatrix[][])
	{	super(ai);
		setCostMatrix(costMatrix);			
	}
	
	/////////////////////////////////////////////////////////////////
	// COST MATRIX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice utilisée pour déterminer le coût d'une action */
	private double costMatrix[][];
	
	/**
	 * Initialise la matrice de coût.
	 * <br/>
	 * <b>Attention :</b>cette matrice doit avoir 
	 * la même taille que la zone de jeu.
	 * 
	 * @param costMatrix	
	 * 		la matrice de coût
	 */
	public void setCostMatrix(double costMatrix[][])
	{	this.costMatrix = costMatrix;		
	}
	
	/**
	 * Met à jour un coût dans la matrice.
	 * 
	 * @param row	
	 * 		Ligne de la case à mettre à jour.
	 * @param col	
	 * 		Colonne de la case à mettre à jour.
	 * @param cost	
	 * 		Nouveau coût à affecter.
	 */
	public void setCost(int row, int col, double cost) throws StopRequestException
	{	costMatrix[row][col] = cost;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * La case de départ n'est pas considérée, on renvoie seulement la valeur
	 * correspondant à la case d'arrivée dans la matrice de coût.
	 * <b>ATTENTION :</b> si la matrice de coût est trop petite, la valeur maximale
	 * possible est renvoyée (Double.POSITIVE_INFINITY), et un message 
	 * d'avertissement est affiché dans la sortie standard d'erreur.
	 * <b>Note :</b> seul l'emplacement de destination est important pour cette fonction
	 * de coût. 
	 * 
	 * @param currentNode
	 * 		Le noeud contenant l'emplacement de départ. 
	 * @param nextLocation
	 * 		L'emplacement d'arrivée (case voisine de la case courante).
	 * @return	
	 * 		Le coût correspondant à la case d'arrivée dans la matrice de coût.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */ 
	@Override
	public double processCost(AiSearchNode currentNode, AiLocation nextLocation) throws StopRequestException
	{	// on récupère le coût associé dans la matrice
		AiTile destination = nextLocation.getTile();
		int col = destination.getCol();
		int row = destination.getRow();
		double result = Double.POSITIVE_INFINITY;
		if(row<costMatrix.length && col<costMatrix[0].length)
			result = costMatrix[row][col];
		
		// on rajoute le coût supplémentaire si la case contient un adversaire
		if(opponentCost>0)
		{	AiZone zone = destination.getZone();
			List<AiHero> opponents = new ArrayList<AiHero>(zone.getRemainingOpponents());
			List<AiHero> heroes = destination.getHeroes();
			opponents.retainAll(heroes);
			if(!opponents.isEmpty())
				result = result + opponentCost;
		}
		
		return result;
	}
}
