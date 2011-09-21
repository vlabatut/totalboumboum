package org.totalboumboum.ai.v201112.adapter.path.dybref;

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

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;

/**
 * Représente un noeud dans l'arbre de recherche développé par l'algorithme A* 
 * 
 * @author Vincent Labatut
 *
 */
public final class DybrefMatrix
{	
	/**
	 * Constructeur créant un noeud racine non visité. 
	 * Les calculateurs passés en paramètres seront utilisés
	 * dans l'arbre entier (i.e. pour tous les autre noeuds)
	 * 
	 * @param tile	
	 * 		case associée à ce noeud de recherche
	 * @param costCalculator	
	 * 		fonction de cout
	 * @param heuristicCalculator	
	 * 		fonction heuristique
	 */
	protected DybrefMatrix(AiHero hero) throws StopRequestException
	{	// position exacte du personnage
		startX = hero.getPosX();
		startY = hero.getPosY();
		
		// initialisation de la matrice
		AiZone zone = hero.getTile().getZone();
		matrix = new DybrefNode[zone.getHeight()][zone.getWidth()];
		for(int i=0;i<zone.getHeight();i++)
			for(int j=0;j<zone.getWidth();j++)
				matrix[i][j] = null;
	}

    /////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice contenant les chemins les plus courts pour accéder à chaque case */
	private DybrefNode[][] matrix = null;
	
	/**
	 * Met à jour la matrice des temps d'accès
	 * en fonction du temps associé à cette case
	 * et des valeurs déjà contenues dans la matrice.
	 */
	public void update(DybrefNode node)
	{	if(node.isSafe())
		{	AiTile tile = node.getTile();
			int r = tile.getRow();
			int c = tile.getCol();
			DybrefNode node2 = matrix[r][c];
			if(node2==null || node.getTotalDuration()<node2.getTotalDuration())
				matrix[r][c] = node;
		}
	}
	
    /////////////////////////////////////////////////////////////////
	// PATH				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse de départ du personnage considéré */
	private double startX;
	/** ordonnée de départ du personnage considéré */
	private double startY;
	
	/**
	 * Part du noeud de recherche contenu dans la matrice
	 * (et dont la position est spécifiée en paramètres)
	 * pour reconstruire le chemin permettant d'aller
	 * du point de départ à cette case. A noter que
	 * le chemin renvoyé est dynamique, dans le sens où
	 * le temps est considéré. Il est susceptible de 
	 * contenir en particulier des pauses permettant
	 * d'éviter divers obstacles et/ou dangers. Ces
	 * pauses doivent être considérées lorsque le
	 * chemin est suivi.
	 * 
	 * @param row
	 * 		Ligne de la case d'arrivée.
	 * @param col
	 * 		Colonne de la case d'arrivée.
	 * @return
	 * 		Un chemin, ou {@code null} si aucun chemin vers la case spécifiée n'a été trouvé.
	 */
	public AiPath getPathFor(int row, int col)
	{	AiPath result;
	
		// init
		DybrefNode node = matrix[row][col];
		if(node==null)
			result = null;
		else
			result = new AiPath();
		DybrefNode previous = null;
		
		while(node!=null)
		{	AiTile tile = node.getTile();
			
			// different tile
			if(previous ==null || !tile.equals(previous.getTile()))
				result.addTile(0,tile);
			
			// same tile
			else
			{	long pause = previous.getDuration();
				pause = pause + result.getPause(0);
				result.setPause(0,pause);
			}
			
			// process next node
			node = node.getParent();
		}
		
		// init starting point
		result.setStart(startX,startY);
		
		return result;
	}

	/**
	 * Comme {@link #getPathFor(int, int}, mais prend
	 * directment la case d'arrivée en paramètre
	 * (au lieu de ses coordonnées).
	 * 
	 * @param tile
	 * 		La case d'arrivée.
	 * @return
	 * 		Le chemin vers la case d'arrivée, ou {@code null} s'il n'existe pas.
	 */
	public AiPath getPathFor(AiTile tile)
	{	int col = tile.getCol();
		int row = tile.getRow();
		return getPathFor(row,col);
	}

    /////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le temps nécessaire pour arriver à la case
	 * spécifiée. Si aucun chemin n'existe, c'est
	 * {@link Long#MAX_VALUE} qui est renvoyé.
	 * 
	 * @param row
	 * 		Ligne de la case d'arrivée.
	 * @param col
	 * 		Colonne de la case d'arrivée.
	 * @return
	 * 		La durée du chemin, ou {@link Long#MAX_VALUE} si aucun chemin vers la case spécifiée n'a été trouvé.
	 */
	public long getTime(int row, int col)
	{	long result = Long.MAX_VALUE;
		DybrefNode node = matrix[row][col];
		if(node!=null)
			result = node.getTotalDuration();
		return result;
	}
	
	/**
	 * Comme {@link #getPathFor(int,int)}, mais prend
	 * directment la case en paramètre
	 * (au lieu de ses coordonnées).
	 * 
	 * @param tile
	 * 		La case d'arrivée.
	 * @return
	 * 		La durée du chemin vers la case d'arrivée, ou {@link Long#MAX_VALUE} si ce chemin n'existe pas.
	 */
	public long getTime(AiTile tile)
	{	int col = tile.getCol();
		int row = tile.getRow();
		return getTime(row,col);
	}
	
	/**
	 * Renvoie une matrice contenant le temps nécessaire
	 * pour aller dans chacune des cases de l'aire de jeu.
	 * les cases inaccessibles, ou pour lesquelles aucun
	 * chemin n'a été trouvé, contiennent la valeur
	 * {@link Long#MAX_VALUE}.
	 * 
	 * @return
	 * 		Une matrice d'entiers correspondant à des durées.
	 */
	public long[][] getTimeMatrix()
	{	int height = matrix.length;
		int width = matrix[0].length;
		long[][] result = new long[height][width];
		for(int i=0;i<height;i++)
			for(int j=0;j<height;j++)
				result[i][j] = getTime(i,j);
		return result;
	}
}
