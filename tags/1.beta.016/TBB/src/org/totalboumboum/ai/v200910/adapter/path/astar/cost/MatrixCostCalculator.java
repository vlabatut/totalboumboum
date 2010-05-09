package org.totalboumboum.ai.v200910.adapter.path.astar.cost;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * Classe �tendant la classe abstraite CostCalculator gr�ce � une matrice de co�ts.
 * Ici, le cout pour passer d'une case � l'autre d�pend uniquement de la case
 * de destination. Ce cout est �gal � la valeur associ�e � la case dans la matrice
 * de cout fournie. Cette matrice doit faire la m�me taille que la zone de jeu.
 * </br>
 * Cette classe est utilie si on veut calculer des couts plus fins qu'avec BasicCostCalculator,
 * qui consid�re seulement la distance. Par exemple, on peut donner un coup plus important
 * aux cases qui sont � port�e d'une bombe susceptibles d'exploser, ou bien 
 * un cout infini (avec Double.POSITIVE_INFINITY) aux cases qu'on veut interdire
 * au personnage parce qu'elles sont trop dangereuses. 
 */
public class MatrixCostCalculator extends CostCalculator
{
	/**
	 * initialise la fonction de cout. On doit obligatoirement
	 * fournir la matrice de cout correspondante.
	 * Attention : cette matrice doit avoir la m�me taille que la zone de jeu.
	 * 
	 * @param costMatrix	la matrice de cout
	 */
	public MatrixCostCalculator(double costMatrix[][]) throws StopRequestException
	{	setCostMatrix(costMatrix);			
	}
	
	/////////////////////////////////////////////////////////////////
	// COST MATRIX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice de cout utilis�e pour d�terminer le cout d'une action */
	private double costMatrix[][];
	
	/**
	 * initialise la matrice de cout. Attention : cette matrice doit avoir
	 * la m�me taille que la zone de jeu.
	 * 
	 * @param costMatrix	la matrice de cout
	 */
	public void setCostMatrix(double costMatrix[][]) throws StopRequestException
	{	this.costMatrix = costMatrix;		
	}
	
	/**
	 * met � jour un co�t dans la matrice
	 * 
	 * @param line	ligne de la case � mettre � jour
	 * @param col	colonne de la case � mettre � jour
	 * @param cost	nouveau co�t � affecter
	 */
	public void setCost(int line, int col, double cost) throws StopRequestException
	{	costMatrix[line][col] = cost;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * La case de d�part n'est pas consid�r�e, on renvoie seulement la valeur
	 * correspondant � la case d'arriv�e dans la matrice de cout.
	 * Attention : si la matrice de cout est trop petite, la valeur maximale
	 * possible est renvoy�e (Double.POSITIVE_INFINITY), et un message 
	 * d'avertissement est affich� dans la sortie standard d'erreur.
	 * 
	 * @param start	la case de d�part
	 * @param end	la case d'arriv�e
	 * @return le cout correspondant � la case d'arriv�e dans la matrice de cout
	 */ 
	@Override
	public double processCost(AiTile start, AiTile end) throws StopRequestException
	{	int col = end.getCol();
		int line = end.getLine();
		double result = Double.POSITIVE_INFINITY;
		if(line<costMatrix.length && col<costMatrix[0].length)
			result = costMatrix[line][col];
		return result;
	}
}
