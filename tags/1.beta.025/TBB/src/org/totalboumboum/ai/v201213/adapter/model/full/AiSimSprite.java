package org.totalboumboum.ai.v201213.adapter.model.full;

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

import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiStateName;

/**
 * Cette classe permet de simuler les sprites du jeu,
 * et un nombre restreint de leurs propriétés.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public abstract class AiSimSprite implements AiSprite
{	
	/**
	 * Crée une nouvelle simulation d'un sprite.
	 * 
	 * @param id
	 * 		Numéro d'identification du sprite.
	 * @param tile
	 * 		Case contenant le sprite.
	 * @param posX
	 * 		Abscisse du sprite.
	 * @param posY
	 * 		Ordonnée du sprite.
	 * @param posZ
	 * 		Hauteur du sprite.
	 * @param state
	 * 		État du sprite.
	 * @param burningDuration
	 * 		Durée de combustion du sprite.
	 * @param currentSpeed
	 * 		Vitesse courante de déplacement du sprite.
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
	 * Construit une simulation du sprite passé en paramètre.
	 * 
	 * @param tile	
	 * 		La simulation de la case contenant le sprite.
	 * @param sprite	
	 * 		Le sprite à simuler.
	 */
	protected AiSimSprite(AiSimTile tile, AiSprite sprite)
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
	/** Id du sprite dans le jeu */
	protected int id;
	
	@Override
	public int getId()
	{	return id;
	}
	
	/**
	 * Permet de modifier l'id de ce sprite.
	 * méthode utilisée uniquement lors des simulations.
	 * 
	 * @param id
	 * 		Nouvel id de ce sprite.
	 */
	protected void setId(int id)
	{	this.id = id;
	}
	
	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** État dans lequel se trouve ce sprite */
	protected AiSimState state;

	@Override
	public AiSimState getState()
	{	return state;
	}
	
	/**
	 * Modidifie l'état de ce sprites.
	 * 
	 * @param state
	 * 		Le nouvel état à affecter à ce sprite.
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
	/** Simulation de la case contenant ce sprite */
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
	 * Permet de modifier la case occupée par ce sprite.
	 * méthode utilisée uniquement lors des simulations.
	 * 
	 * @param tile
	 * 		Nouvelle case occupée par ce sprite.
	 */
	protected void setTile(AiSimTile tile)
	{	this.tile = tile;
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Abscisse de ce sprite en pixels */
	protected double posX;
	/** Ordonnée de ce sprite en pixels */
	protected double posY;
	/** Altitude de ce sprite en pixels */
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
	 * Permet de modifier les coordonnées de ce sprite (exprimées en pixels).
	 * méthode utilisée uniquement lors des simulations.
	 * 
	 * @param x
	 * 		Nouvelle abscisse.
	 * @param y
	 * 		Nouvelle ordonnée.
	 * @param z
	 * 		Nouvelle hauteur.
	 */
	protected void setPos(double x, double y, double z)
	{	posX = x;
		posY = y;
		posZ = z;
	}

	/////////////////////////////////////////////////////////////////
	// BURN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Temps nécessaire au sprite pour brûler (à condition qu'il puisse brûler) */
	protected long burningDuration = 0;
	
	@Override
	public long getBurningDuration()
	{	return burningDuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Vitesse de déplacement courante en pixels/s (est nulle si le sprite ne bouge pas */
	protected double currentSpeed = 0;
	
	@Override
	public double getCurrentSpeed()
	{	return currentSpeed;
	}

	/**
	 * Permet de modifier la vitesse de déplacement courante de ce sprite.
	 * méthode utilisée uniquement lors des simulations.
	 * 
	 * @param currentSpeed
	 * 		Nouvelle vitesse de déplacement.
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
	
	@Override
	public int compareTo(AiSprite sprite)
	{	int result = id - sprite.getId();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		String row = "x";
		String col = "x";
		if(tile!=null)
		{	row = Integer.toString(tile.getRow());
			col = Integer.toString(tile.getCol());
		}
		result.append(" ("+row+";"+col+")");
		result.append(" ("+posX+";"+posY+";"+posZ+")");
		result.append(" - state: "+state.toString());
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Termine proprement ce sprite et libère 
	 * les ressources qu'il occupait.
	 */
	protected void finish()
	{	// state
		state.finish();
		state = null;
		
		// misc
		tile = null;
	}
}
