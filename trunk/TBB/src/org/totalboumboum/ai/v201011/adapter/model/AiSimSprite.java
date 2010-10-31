package org.totalboumboum.ai.v201011.adapter.model;

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
	protected AiSimSprite(AiSimTile tile, double posX, double posY, double posZ, 
			long burningDuration)
	{	// general
		this.tile = tile;
		state = new AiSimState();
		this.burningDuration = burningDuration;
		
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
		burningDuration = sprite.getBurningDuration();
		
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

	@Override
	public AiSimState getState()
	{	return state;
	}
	
	@Override
	public boolean hasEnded()
	{	return state.getName()==AiStateName.ENDED;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** simulation de la case contenant ce sprite */
	protected AiSimTile tile;
	
	@Override
	public AiSimTile getTile()
	{	return tile;
	}
	
	@Override
	public int getLine()
	{	return tile.getLine();	
	}

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

	@Override
	public double getPosX()
	{	return posX;
	}
	
	@Override
	public double getPosY()
	{	return posY;
	}
	
	@Override
	public double getPosZ()
	{	return posZ;
	}

	/////////////////////////////////////////////////////////////////
	// BURN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long burningDuration = 0;
	
	@Override
	public long getBurningDuration()
	{	return burningDuration;
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
