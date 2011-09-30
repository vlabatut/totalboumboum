package org.totalboumboum.ai.v201112.adapter.path.astar;

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

import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Représente un noeud dans l'arbre de recherche développé par l'algorithme A* 
 * 
 * @author Vincent Labatut
 *
 */
public final class AstarLocation
{	
	/**
	 * Définit une nouvelle position à partir
	 * d'une case et de deux coordonnées réelles.
	 * Les coordonnées réelles sont supposées appartenir
	 * à la case.
	 * 
	 * @param tile	
	 * 		case associée à ce noeud de recherche
	 * @param costCalculator	
	 * 		fonction de cout
	 * @param heuristicCalculator	
	 * 		fonction heuristique
	 */
	private AstarLocation(double posX, double posY, AiTile tile)
	{	this.tile = tile;
		this.posX = posX;
		this.posY = posY;
	}

	public AstarLocation(double posX, double posY, AiZone zone)
	{	this(posX,posY,zone.getTile(posX,posY));
	}
	
	public AstarLocation(AiTile tile)
	{	this(tile.getPosX(),tile.getPosY(),tile);
	}
	
    /////////////////////////////////////////////////////////////////
	// TILE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case associée à cette position */
	private AiTile tile;
	
	/**
	 * Renvoie la case contenant cette position.
	 * 
	 * @return
	 * 		La case associée à cette position.
	 */
    public AiTile getTile()
    {	return tile;
	}

    /**
     * Renvoie la zone dans laquelle
     * cet emplacement est défini.
     * 
     * @return
     * 		La zone contenant cet emplacement.
     */
    public AiZone getZone()
    {	return tile.getZone();
    }
    
	/////////////////////////////////////////////////////////////////
	// COORDINATES				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse associée à cette position */
	private double posX;
	/** ordonnée associée à cette position */
	private double posY;
	
	/**
	 * Renvoie l'abscisse de cette position
	 * (exprimée en pixels).
	 * 
	 * @return
	 * 		Un réel représentant l'abscisse de cette position exprimée en pixels.
	 */
	public double getPosX()
	{	return posX;
	}
	
	/**
	 * Renvoie l'ordonnée de cette position
	 * (exprimée en pixels).
	 * 
	 * @return
	 * 		Un réel représentant l'ordonnée de cette position exprimée en pixels.
	 */
	public double getPosY()
	{	return posY;
	}

	/////////////////////////////////////////////////////////////////
	// MISC						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AstarLocation)
		{	AstarLocation location = (AstarLocation)o;	
			result = tile.getZone().hasSamePixelPosition(this,location);
		}
		return result;
	}
	
	@Override
    public int hashCode()
    {	AiZone zone = tile.getZone();
		double height = zone.getPixelHeight();
		int result = (int)(posY + height*posX);
    	return result;
    }
	
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("("+tile.getRow()+";"+tile.getCol()+" - "+posX+";"+posY+")");
		return result.toString();
	}
}
