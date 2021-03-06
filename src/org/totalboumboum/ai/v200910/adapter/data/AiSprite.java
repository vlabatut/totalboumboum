package org.totalboumboum.ai.v200910.adapter.data;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.io.Serializable;

import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * cette classe permet de représenter les sprites manipulés par le jeu,
 * et un nombre restreint de leurs propriétés, rendues ainsi accessible à l'IA.
 * Le paramètre T détermine le type de sprite représenté : bloc, bombe,
 * feu, sol, personnage ou item. 
 * 
 * @param <T>	type de sprite représenté
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public abstract class AiSprite<T extends Sprite> implements Serializable
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * construit une représentation du sprite passé en paramètre
	 * 
	 * @param tile	représentation de la case contenant le sprite
	 * @param sprite	sprite à représenter
	 */
	AiSprite(AiTile tile, T sprite)
	{	this.tile = tile;
		this.sprite = sprite;
		state = new AiState(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour cette représentation du sprite
	 * 
	 * @param tile	la nouvelle case contenant cette représentation
	 */
	void update(AiTile tile)
	{	this.tile = tile;
		updateLocation();
		updateState();
		checked = true;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sprite représenté par cette classe */ 
	private transient T sprite;

	/**
	 * teste si cette représentation correspond au sprite passé en paramètre
	 * 
	 * @param sprite	le sprite dont on veut la représentation
	 * @return	vrai si cette représentation correspond à ce sprite
	 */
	boolean isSprite(T sprite)
	{	return this.sprite == sprite;
	}
	
	/**
	 * renvoie le sprite correspondant à cette représentation
	 * 
	 * @return	le sprite correspondant à cette représentation
	 */
	T getSprite()
	{	return sprite;	
	}

	/////////////////////////////////////////////////////////////////
	// CHECK			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** marquage du sprite (permet de détecter quels sprites ont disparu lors de la mise à jour */
	private boolean checked;

	/**
	 * teste si sprite est marqué ou pas
	 * 
	 * @return	vrai si ce sprite est marqué
	 */
	boolean isChecked()
	{	return checked;	
	}
	
	/**
	 * démarque ce sprite (action réalisée avant la mise à jour de la zone)
	 */
	void uncheck()
	{	checked = false; 
	}
	
	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** état dans lequel se trouve ce sprite */
	private AiState state;

	/** 
	 * renvoie l'état dans lequel se trouve ce sprite
	 * (ie: quelle action il est en train d'effectuer ou de subir)
	 * 
	 * @return	l'état du sprite
	 */
	public AiState getState()
	{	return state;
	}
	
	/** 
	 * initialise l'état dans lequel se trouve ce sprite
	 */
	private void updateState()
	{	state.update();
	}
	
	/**
	 * indique que le sprite a été éliminé du jeu
	 */
	void setEnded()
	{	state.setEnded();		
	}
	
	/**
	 * renvoie vrai si ce sprite a été éliminé du jeu
	 * @return	vrai si le sprite n'est plus en jeu
	 */
	public boolean hasEnded()
	{	return state.getName()==AiStateName.ENDED;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** représentation de la case contenant ce sprite */
	private AiTile tile;
	
	/** 
	 * renvoie la représentation de la case contenant ce sprite 
	 * @return
	 * 		La case. 
	 */
	public AiTile getTile()
	{	return tile;
	}
	
	/** 
	 * renvoie le numéro de la ligne contenant ce sprite 
	 * 
	 * @return	le numéro de la ligne du sprite
	 */
	public int getLine()
	{	return tile.getLine();	
	}
	/** 
	 * renvoie le numéro de la colonne contenant ce sprite
	 * 
	 * @return	le numéro de la colonne du sprite
	 */
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
	public double getPosX()
	{	return posX;
	}
	
	/** 
	 * renvoie l'ordonnée de ce sprite en pixels 
	 * 
	 * @return	l'ordonnée du sprite
	 */
	public double getPosY()
	{	return posY;
	}
	
	/** 
	 * renvoie l'altitude de ce sprite en pixels 
	 * 
	 * @return	l'altitude du sprite
	 */
	public double getPosZ()
	{	return posZ;
	}
	
	/** 
	 * initialise les positions (en pixels) de ce sprite 
	 */
	private void updateLocation()
	{	posX = sprite.getCurrentPosX();
		posY = sprite.getCurrentPosY();
		posZ = sprite.getCurrentPosZ();
		
//if(Double.isNaN(posX))
//	System.out.println("error");
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiSprite<?>)
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
		result.append(" ("+posX+";"+posY+";"+posZ+")");
		result.append(" - state: "+state.toString());
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Teste si le sprite passé en paramètre est capable de traverser
	 * la case de ce sprite
	 * 
	 *  @param sprite	le sprite à tester
	 *  @return	vrai si ce sprite le laisser passer par sa case 
	 */
	public abstract boolean isCrossableBy(AiSprite<?> sprite);
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement ce sprite et libère les ressources qu'il occupait
	 */
	void finish()
	{	// state
		state.finish();
		state = null;
		// misc
		tile = null;
		sprite = null;
		
	}
}
