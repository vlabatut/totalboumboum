package org.totalboumboum.ai.v200910.adapter.data;

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

import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * cette classe permet de repr�senter les sprites manipul�s par le jeu,
 * et un nombre restreint de leurs propri�t�s, rendues ainsi accessible à l'IA.
 * Le param�tre T d�termine le type de sprite repr�sent� : bloc, bombe,
 * feu, sol, personnage ou item. 
 * 
 * @author Vincent Labatut
 *
 * @param <T>	type de sprite repr�sent�
 */

public abstract class AiSprite<T extends Sprite>
{	
	/**
	 * construit une repr�sentation du sprite pass� en param�tre
	 * 
	 * @param tile	repr�sentation de la case contenant le sprite
	 * @param sprite	sprite à repr�senter
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
	 * met à jour cette repr�sentation du sprite
	 * 
	 * @param tile	la nouvelle case contenant cette repr�sentation
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
	/** sprite repr�sent� par cette classe */ 
	private T sprite;

	/**
	 * teste si cette repr�sentation correspond au sprite pass� en param�tre
	 * 
	 * @param sprite	le sprite dont on veut la repr�sentation
	 * @return	vrai si cette repr�sentation correspond à ce sprite
	 */
	boolean isSprite(T sprite)
	{	return this.sprite == sprite;
	}
	
	/**
	 * renvoie le sprite correspondant à cette repr�sentation
	 * 
	 * @return	le sprite correspondant à cette repr�sentation
	 */
	T getSprite()
	{	return sprite;	
	}

	/////////////////////////////////////////////////////////////////
	// CHECK			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** marquage du sprite (permet de d�tecter quels sprites ont disparu lors de la mise à jour */
	private boolean checked;

	/**
	 * teste si sprite est marqu� ou pas
	 * 
	 * @return	vrai si ce sprite est marqu�
	 */
	boolean isChecked()
	{	return checked;	
	}
	
	/**
	 * d�marque ce sprite (action r�alis�e avant la mise à jour de la zone)
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
	 * renvoie l'�tat dans lequel se trouve ce sprite
	 * (ie: quelle action il est en train d'effectuer ou de subir)
	 * 
	 * @return	l'�tat du sprite
	 */
	public AiState getState()
	{	return state;
	}
	
	/** 
	 * initialise l'�tat dans lequel se trouve ce sprite
	 */
	private void updateState()
	{	state.update();
	}
	
	/**
	 * indique que le sprite a �t� �limin� du jeu
	 */
	void setEnded()
	{	state.setEnded();		
	}
	
	/**
	 * renvoie vrai si ce sprite a �t� �limin� du jeu
	 * @return	vrai si le sprite n'est plus en jeu
	 */
	public boolean hasEnded()
	{	return state.getName()==AiStateName.ENDED;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** repr�sentation de la case contenant ce sprite */
	private AiTile tile;
	
	/** 
	 * renvoie la repr�sentation de la case contenant ce sprite 
	 */
	public AiTile getTile()
	{	return tile;
	}
	
	/** 
	 * renvoie le num�ro de la ligne contenant ce sprite 
	 * 
	 * @return	le num�ro de la ligne du sprite
	 */
	public int getLine()
	{	return tile.getLine();	
	}
	/** 
	 * renvoie le num�ro de la colonne contenant ce sprite
	 * 
	 * @return	le num�ro de la colonne du sprite
	 */
	public int getCol()
	{	return tile.getCol();	
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse de ce sprite en pixels */
	private double posX;
	/** ordonn�e de ce sprite en pixels */
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
	 * renvoie l'ordonn�e de ce sprite en pixels 
	 * 
	 * @return	l'ordonn�e du sprite
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
	 * Teste si le sprite pass� en param�tre est capable de traverser
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
	 * termine proprement ce sprite et lib�re les ressources qu'il occupait
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
