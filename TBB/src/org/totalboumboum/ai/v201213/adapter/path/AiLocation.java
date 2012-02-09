package org.totalboumboum.ai.v201213.adapter.path;

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

import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 * Représente un noeud dans l'arbre de recherche développé par l'algorithme A* 
 * 
 * @author Vincent Labatut
 *
 */
public final class AiLocation implements Comparable<AiLocation>
{	
	/**
	 * Définit une nouvelle position à partir
	 * d'une case et de deux coordonnées réelles.
	 * Les coordonnées réelles sont supposées appartenir
	 * à la case.
	 * 
	 * @param posX
	 * 		Position de l'abscisse en pixels.
	 * @param posY
	 * 		Position de l'ordonnée en pixels.
	 * @param tile	
	 * 		Case contenant cette position dans la zone courante.
	 */
	private AiLocation(double posX, double posY, AiTile tile)
	{	this.tile = tile;
		this.posX = posX;
		this.posY = posY;
	}

	/**
	 * Définit une nouvelle position à partir
	 * de deux coordonnées réelles et d'une zone.
	 * La zone est utilisée pour déduire la case
	 * correspondante à partir des coordonées.
	 * 
	 * @param posX
	 * 		Position de l'abscisse en pixels.
	 * @param posY
	 * 		Position de l'ordonnée en pixels.
	 * @param zone	
	 * 		Zone contenant ces coordonnées.
	 */
	public AiLocation(double posX, double posY, AiZone zone)
	{	this(posX,posY,zone.getTile(posX,posY));
	}
	
	/**
	 * Définit une nouvelle position à partir
	 * de la case spécifiée. Les coordonnées
	 * réelles du centre de la case sont utilisées.
	 * 
	 * @param tile
	 * 		La case dont on veut la position.
	 */
	public AiLocation(AiTile tile)
	{	this(tile.getPosX(),tile.getPosY(),tile);
	}
	
	/**
	 * Définit une nouvelle position à partir
	 * du sprite spécifié. Les coordonnées
	 * réelles du sprite et la case qui le
	 * contient sont utilisées lors
	 * de l'initialisation.
	 * 
	 * @param sprite
	 * 		Le sprite dont on veut la position.
	 */
	public AiLocation(AiSprite sprite)
	{	this.tile = sprite.getTile();
		this.posX = sprite.getPosX();
		this.posY = sprite.getPosY();
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
	// COMPARISON				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiLocation)
		{	AiLocation location = (AiLocation)o;	
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
	public int compareTo(AiLocation location)
	{	Double x1 = new Double(posX);
		Double y1 = new Double(posY);
		Double x2 = new Double(location.posX);
		Double y2 = new Double(location.posY);
		int result = x1.compareTo(x2);
		if(result==0)
			result = y1.compareTo(y2);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("("+tile.getRow()+";"+tile.getCol()+" - "+posX+";"+posY+")");
		return result.toString();
	}
}
