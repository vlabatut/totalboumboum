package org.totalboumboum.ai.v201011.adapter.data;

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

/**
 * cette interface permet de représenter les sprites manipulés par le jeu,
 * et un nombre restreint de leurs propriétés, rendues ainsi accessible à l'IA.
 * 
 * @author Vincent Labatut
 *
 */
public interface AiSprite
{	
	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie l'état dans lequel se trouve ce sprite
	 * (ie: quelle action il est en train d'effectuer ou de subir)
	 * 
	 * @return	l'état du sprite
	 */
	public AiState getState();
	
	/**
	 * renvoie vrai si ce sprite a été éliminé du jeu
	 * @return	vrai si le sprite n'est plus en jeu
	 */
	public boolean hasEnded();
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la représentation de la case contenant ce sprite 
	 */
	public AiTile getTile();
	
	/** 
	 * renvoie le numéro de la ligne contenant ce sprite 
	 * 
	 * @return	le numéro de la ligne du sprite
	 */
	public int getLine();

	/** 
	 * renvoie le numéro de la colonne contenant ce sprite
	 * 
	 * @return	le numéro de la colonne du sprite
	 */
	public int getCol();
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie l'abscisse de ce sprite en pixels 
	 * 
	 * @return	l'abscisse du sprite
	 */
	public double getPosX();
	
	/** 
	 * renvoie l'ordonnée de ce sprite en pixels 
	 * 
	 * @return	l'ordonnée du sprite
	 */
	public double getPosY();
	
	/** 
	 * renvoie l'altitude de ce sprite en pixels 
	 * 
	 * @return	l'altitude du sprite
	 */
	public double getPosZ();
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la vitesse de déplacement courante du sprite.
	 * si le sprite ne bouge pas, elle est de zéro.
	 * 
	 * @return	vitesse de déplacement du sprite en pixels/seconde
	 */
	public double getCurrentSpeed();
	
	/////////////////////////////////////////////////////////////////
	// COLLISION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Teste si le sprite passé en paramètre est capable de traverser
	 * la case de ce sprite
	 * 
	 *  @param sprite	le sprite à tester
	 *  @return	vrai si ce sprite le laisser passer par sa case 
	 */
	public boolean isCrossableBy(AiSprite sprite);

	/////////////////////////////////////////////////////////////////
	// BURN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le temps que va mettre ce sprite à brûler s'il est touché
	 * par du feu, exprimé en ms. Si ce sprite ne peut pas brûler, ce
	 * temps est égal à -1.
	 * 
	 * @return	le temps que ce sprite va mettre à brûler, en ms
	 */
	public long getBurningDuration();
}
