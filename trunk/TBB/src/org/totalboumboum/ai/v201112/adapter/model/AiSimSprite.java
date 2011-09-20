package org.totalboumboum.ai.v201112.adapter.model;

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

import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.ai.v201112.adapter.data.AiStateName;

/**
 * cette classe permet de simuler les sprites du jeu,
 * et un nombre restreint de leurs propriétés.
 * 
 * @author Vincent Labatut
 *
 */
abstract class AiSimSprite implements AiSprite
{	
	/**
	 * crée une nouvelle simulation d'un sprite
	 * 
	 * @param id
	 * 		num�ro d'identification du sprite
	 * @param tile
	 * 		case contenant le sprite
	 * @param posX
	 * 		abscisse du sprite
	 * @param posY
	 * 		ordonnée du sprite
	 * @param posZ
	 * 		hauteur du sprite
	 * @param state
	 * 		état du sprite
	 * @param burningDuration
	 * 		durée de combustion du sprite
	 * @param currentSpeed
	 * 		vitesse courante de déplacement du sprite
	 */
	protected AiSimSprite(int id, AiSimTile tile, double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	// general
		this.state = new AiSimState(state);
		this.burningDuration = burningDuration;
		this.currentSpeed = currentSpeed;
		this.id = id;
		
		// location
		this.tile = tile;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	/**
	 * construit une simulation du sprite passé en paramètre
	 * 
	 * @param sprite	
	 * 		sprite à simuler
	 * @param tile	
	 * 		simulation de la case contenant le sprite
	 */
	protected AiSimSprite(AiSprite sprite, AiSimTile tile)
	{	// general
		state = new AiSimState(sprite);
		burningDuration = sprite.getBurningDuration();
		this.currentSpeed = sprite.getCurrentSpeed();
		this.id = sprite.getId();
		
		// location
		this.tile = tile;
		this.posX = sprite.getPosX();
		this.posY = sprite.getPosY();
		this.posZ = sprite.getPosZ();
	}

	/////////////////////////////////////////////////////////////////
	// ID				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** id du sprite dans le jeu */
	protected int id;
	
	@Override
	public int getId()
	{	return id;
	}
	
	/**
	 * permet de modifier l'id de ce sprite.
	 * méthode utilisée uniquement lors des simulations.
	 * 
	 * @param id
	 * 		nouvel id de ce sprite
	 */
	protected void setId(int id)
	{	this.id = id;
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
	
	/**
	 * modidifie l'état de ce sprites
	 * 
	 * @param state
	 * 		le nouvel état à affecter à ce sprite
	 */
	public void setState(AiSimState state)
	{	this.state = state;
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
	public int getRow()
	{	return tile.getRow();	
	}

	@Override
	public int getCol()
	{	return tile.getCol();	
	}
	
	/**
	 * permet de modifier la case occupée par ce sprite.
	 * méthode utilisée uniquement lors des simulations.
	 * 
	 * @param tile
	 * 		nouvelle case occupée par ce sprite
	 */
	protected void setTile(AiSimTile tile)
	{	this.tile = tile;
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse de ce sprite en pixels */
	protected double posX;
	/** ordonnée de ce sprite en pixels */
	protected double posY;
	/** altitude de ce sprite en pixels */
	protected double posZ;

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
	
	/**
	 * permet de modifier les coordonnées de ce sprite (exprimées en pixels).
	 * méthode utilisée uniquement lors des simulations.
	 * 
	 * @param x
	 * 		nouvelle abscisse
	 * @param y
	 * 		nouvelle ordonnée
	 * @param z
	 * 		nouvelle hauteur
	 */
	protected void setPos(double x, double y, double z)
	{	posX = x;
		posY = y;
		posZ = z;
	}

	/////////////////////////////////////////////////////////////////
	// BURN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps nécessaire au sprite pour brûler (� condition qu'il puisse brûler) */
	protected long burningDuration = 0;
	
	@Override
	public long getBurningDuration()
	{	return burningDuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** vitesse de déplacement courante en pixels/s (est nulle si le sprite ne bouge pas */
	protected double currentSpeed = 0;
	
	@Override
	public double getCurrentSpeed()
	{	return currentSpeed;
	}

	/**
	 * permet de modifier la vitesse de déplacement courante de ce sprite.
	 * méthode utilisée uniquement lors des simulations.
	 * 
	 * @param currentSpeed
	 * 		nouvelle vitesse de déplacement
	 */
	protected void setCurrentSpeed(double currentSpeed)
	{	this.currentSpeed = currentSpeed;
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiSprite)
		{	AiSprite s = (AiSprite)o;	
			result = id==s.getId();
		}
		return result;
	}

	@Override
    public int hashCode()
    {	return id;
    }
	
	/////////////////////////////////////////////////////////////////
	// TEXT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(" ("+getTile().getRow()+";"+getTile().getCol()+")");
		result.append(" - state: "+state.toString());
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement ce sprite et lib�re les ressources qu'il occupait
	 */
	protected void finish()
	{	// state
		state.finish();
		state = null;
		
		// misc
		tile = null;
	}
}
