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
 * et un nombre restreint de leurs propri�t�s.
 * 
 * @author Vincent Labatut
 *
 */
abstract class AiSimSprite implements AiSprite
{	
	/**
	 * cr�e une nouvelle simulation d'un sprite
	 * 
	 * @param tile	simulation de la case contenant le sprite
	 */
	protected AiSimSprite(int id, AiSimTile tile, double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	// general
		this.state = state;
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
	 * construit une simulation du sprite pass� en param�tre
	 * 
	 * @param sprite	sprite � simuler
	 * @param tile	simulation de la case contenant le sprite
	 */
	protected AiSimSprite(AiSprite sprite, AiSimTile tile)
	{	// general
		this.tile = tile;
		state = new AiSimState(sprite);
		burningDuration = sprite.getBurningDuration();
		this.id = sprite.getId();
		
		// location
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
	 * m�thode utilis�e uniquement lors des simulations.
	 * 
	 * @param
	 * 		id	nouvel id de ce sprite
	 */
	protected void setId(int id)
	{	this.id = id;
	}
	
	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** �tat dans lequel se trouve ce sprite */
	protected AiSimState state;

	@Override
	public AiSimState getState()
	{	return state;
	}
	
	/**
	 * modidifie l'�tat de ce sprites
	 * 
	 * @param 
	 * 		state	le nouvel �tat � affecter � ce sprite
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
	public int getLine()
	{	return tile.getLine();	
	}

	@Override
	public int getCol()
	{	return tile.getCol();	
	}
	
	/**
	 * permet de modifier la case occup�e par ce sprite.
	 * m�thode utilis�e uniquement lors des simulations.
	 * 
	 * @param
	 * 		tile	nouvelle case occup�e par ce sprite
	 */
	protected void setTile(AiSimTile tile)
	{	this.tile = tile;
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse de ce sprite en pixels */
	protected double posX;
	/** ordonn�e de ce sprite en pixels */
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
	 * permet de modifier les coordonn�es de ce sprite (exprim�es en pixels).
	 * m�thode utilis�e uniquement lors des simulations.
	 * 
	 * @param
	 * 		x	nouvelle abscisse
	 * 		y	nouvelle ordonn�e
	 * 		z	nouvelle hauteur
	 */
	protected void setPos(double x, double y, double z)
	{	posX = x;
		posY = y;
		posZ = z;
	}

	/////////////////////////////////////////////////////////////////
	// BURN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps n�cessaire au sprite pour br�ler (� condition qu'il puisse br�ler) */
	protected long burningDuration = 0;
	
	@Override
	public long getBurningDuration()
	{	return burningDuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** vitesse de d�placement courante en pixels/s (est nulle si le sprite ne bouge pas */
	protected double currentSpeed = 0;
	
	@Override
	public double getCurrentSpeed()
	{	return currentSpeed;
	}

	/**
	 * permet de modifier la vitesse de d�placement courante de ce sprite.
	 * m�thode utilis�e uniquement lors des simulations.
	 * 
	 * @param
	 * 		currentSpeed	nouvelle vitesse de d�placement
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
