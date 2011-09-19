package org.totalboumboum.ai.v201011.adapter.path.astar;

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

import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;


/**
 * exception lev�e par l'algorithme A* quand il atteint une des limites fix�es au pr�alable.
 * On ne peut donc pas conclure quant à l'existence d'un chemin solution.
 * En d'autres termes : cette exception indique qu'A* a explor� un arbre bien trop
 * grand, sans pourtant trouver la solution. On ne peut donc pas savoir s'il existe
 * un état final quelque part dans la partie non-explor�e de l'arbre, ou bien
 * s'il n'existe pas de solution du tout.  
 * 
 * @author Vincent Labatut
 */

public final class LimitReachedException extends Exception
{	private static final long serialVersionUID = 1L;

	/**
	 * crée une exception repr�sentant le fait que l'algorithme A*
	 * a atteint une des limites qui lui �taient fix�es sans pour autant
	 * trouver de solution (i.e. de chemin)
	 * 
	 * @param startTile
	 * 		case initiale
	 * @param endTiles
	 * 		cases finales
	 * @param height
	 * 		hauteur maximale atteinte pour l'arbre de recherche
	 * @param cost
	 * 		co�t maximal atteint pour les chemins contenus dans l'arbre de recherche
	 * @param size
	 * 		taille atteinte par l'arbre de recherche (exprim�e en nombre de noeuds)
	 * @param maxCost
	 * 		limite de co�t fix�e pour l'exploration
	 * @param maxHeight
	 * 		limite de hauteur fix�e pour l'exploration
	 * @param maxSize
	 * 		limite de taille (exprim�e en nombre de noeuds) fix�e pour l'exploration
	 */
	public LimitReachedException(AiTile startTile, List<AiTile> endTiles, int height, double cost, int size, double maxCost, int maxHeight, int maxSize)
	{	this.startTile = startTile;
		this.endTiles = endTiles;
		this.height = height;
		this.cost = cost;
		this.size = size;
		this.maxCost = maxCost;
		this.maxHeight = maxHeight;
		this.maxSize = maxSize;	
	}
	
    /////////////////////////////////////////////////////////////////
	// START TILES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** case de d�part de la recherche (�tat initial) */
	private AiTile startTile;
	
	/**
	 * renvoie la case de d�part de la recherche
	 * 
	 * @return
	 * 		une case de la zone
	 */
    public AiTile getStartTile()
    {	return startTile;
	}

	/////////////////////////////////////////////////////////////////
	// END TILES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** les cases d'arriv�e de la recherche (�tats finaux) */
    private List<AiTile> endTiles;
	
    /**
     * renvoie les cases d'arriv�e de la recherche
     * 
     * @return
	 * 		une case de la zone
     */
	public List<AiTile> getEndTiles()
	{	return endTiles;
	}

	/////////////////////////////////////////////////////////////////
	// HEIGHT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de hauteur (n�gatif = pas de limite) */
	private int maxHeight;
	/** hauteur maximale atteinte */
	private int height;

	/**
	 * renvoie la limite de hauteur pour l'exploration de l'arbre de recherche.
	 * une valeur n�gative repr�sente une absence de limite.
	 * 
	 * @return
	 * 		la limite de hauteur pour l'exploration de l'arbre de recherche
	 */
	public int getMaxHeight()
	{	return maxHeight;
	}

	/**
	 * renvoie la hauteur de l'arbre, i.e. la longueur
	 * du chemin le plus long d�velopp� lors de la recherche de solution
	 * 
	 * @return
	 * 		hauteur de l'arbre
	 */
	public int getHeight()
	{	return height;
	}

	/////////////////////////////////////////////////////////////////
	// COST				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de co�t (n�gatif = pas de limite) */
	private double maxCost;
	/** co�t maximal atteint */
	private double cost;

	/**
	 * renvoie la limite de co�t pour l'exploration de l'arbre de recherche.
	 * une valeur n�gative repr�sente une absence de limite.
	 * 
	 * @return
	 * 		la limite de co�t pour l'exploration de l'arbre de recherche
	 */
	public double getMaxCost()
	{	return maxCost;
	}

	/**
	 * renvoie le co�t du chemin le plus co�teux d�velopp� lors de la
	 * recherche de solution par A*.
	 * 
	 * @return
	 * 		co�t maximal atteint lors de la recherche de solution
	 */
	public double getCost()
	{	return cost;
	}

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** limite de taille exprim�e en nombre de noeuds (n�gatif = pas de limite) */
	private int maxSize;
	/** taille atteinte lors de l'exploration */
	private int size;

	/**
	 * renvoie la limite qui avait �t� fix�e pour la taille
	 * de l'arbre de recherche exprim�e en noeuds.
	 * 
	 * @return
	 * 		la limite de taille fix�e pour l'arbre
	 */
	public int getMaxSize()
	{	return maxSize;
	}

	/**
	 * renvoie la taille de l'arbre d�velopp� par A* lors de son exploration,
	 * exprim�e en nombre de noeuds.
	 * 
	 * @return
	 * 		la taille de l'arbre explor�
	 */
	public int getSize()
	{	return size;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "";
		result = result + "height=" + height + "(" + maxHeight + ") ";
		result = result + "cost="   + cost   + "(" + maxCost   + ") ";
		result = result + "size="   + size   + "(" + maxSize   + ") ";
		result = result + "src="    + startTile + " ";
		result = result + "trgt=";
		for(AiTile tile: endTiles)
			result = result + tile + " ";
		result = result + "\n";
		return result;
	}
}
