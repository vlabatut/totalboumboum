package org.totalboumboum.ai.v201011.adapter.data.actual;

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

import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;

/**
 * cette classe permet de simuler les sprites du jeu,
 * et un nombre restreint de leurs propriétés.
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AiSimSprite implements AiSprite
{	
	/**
	 * crée une nouvelle simulation d'un sprite
	 * 
	 * @param tile	simulation de la case contenant le sprite
	 */
	protected AiSimSprite(AiSimTile tile, double posX, double posY, double posZ)
	{	// general
		this.tile = tile;
		state = new AiSimState();
		
		// location
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	/**
	 * construit une simulation du sprite passé en paramètre
	 * 
	 * @param sprite	sprite à simuler
	 * @param tile	simulation de la case contenant le sprite
	 */
	protected AiSimSprite(AiSprite sprite, AiSimTile tile)
	{	// general
		this.tile = tile;
		state = new AiSimState(sprite);
		
		// location
		this.posX = sprite.getPosX();
		this.posY = sprite.getPosY();
		this.posZ = sprite.getPosZ();
	}

	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** état dans lequel se trouve ce sprite */
	protected AiSimState state;

	/** 
	 * renvoie l'état dans lequel se trouve ce sprite
	 * (ie: quelle action il est en train d'effectuer ou de subir)
	 * 
	 * @return	l'état du sprite
	 */
	@Override
	public AiSimState getState()
	{	return state;
	}
	
	/**
	 * renvoie vrai si ce sprite a été éliminé du jeu
	 * @return	vrai si le sprite n'est plus en jeu
	 */
	@Override
	public boolean hasEnded()
	{	return state.getName()==AiStateName.ENDED;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** simulation de la case contenant ce sprite */
	protected AiSimTile tile;
	
	/** 
	 * renvoie la simulation de la case contenant ce sprite 
	 */
	@Override
	public AiSimTile getTile()
	{	return tile;
	}
	
	/** 
	 * renvoie le numéro de la ligne contenant ce sprite 
	 * 
	 * @return	le numéro de la ligne du sprite
	 */
	@Override
	public int getLine()
	{	return tile.getLine();	
	}
	/** 
	 * renvoie le numéro de la colonne contenant ce sprite
	 * 
	 * @return	le numéro de la colonne du sprite
	 */
	@Override
	public int getCol()
	{	return tile.getCol();	
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse de ce sprite en pixels */
	private double posX;
	/** ordonnée de ce sprite en pixels */
	private double posY;
	/** altitude de ce sprite en pixels */
	private double posZ;

	/** 
	 * renvoie l'abscisse de ce sprite en pixels 
	 * 
	 * @return	l'abscisse du sprite
	 */
	@Override
	public double getPosX()
	{	return posX;
	}
	
	/** 
	 * renvoie l'ordonnée de ce sprite en pixels 
	 * 
	 * @return	l'ordonnée du sprite
	 */
	@Override
	public double getPosY()
	{	return posY;
	}
	
	/** 
	 * renvoie l'altitude de ce sprite en pixels 
	 * 
	 * @return	l'altitude du sprite
	 */
	@Override
	public double getPosZ()
	{	return posZ;
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiSimSprite)
		{	
//			AiSprite<?> s = (AiSprite<?>)o;	
//			result = sprite==s.sprite;
			result = this==o;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(" ("+getTile().getLine()+";"+getTile().getCol()+")");
		result.append(" - state: "+state.toString());
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement ce sprite et libère les ressources qu'il occupait
	 */
	protected void finish()
	{	// state
		state.finish();
		state = null;
		
		// misc
		tile = null;
	}
}
