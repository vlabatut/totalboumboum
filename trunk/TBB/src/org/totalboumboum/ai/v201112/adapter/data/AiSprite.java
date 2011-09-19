package org.totalboumboum.ai.v201112.adapter.data;

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

/**
 * cette interface permet de représenter les sprites manipul�s par le jeu,
 * et un nombre restreint de leurs propriétés, rendues ainsi accessible à l'IA.
 * 
 * @author Vincent Labatut
 *
 */
public interface AiSprite
{	
	/////////////////////////////////////////////////////////////////
	// ID				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie le num�ro unique du sprite dans le jeu.<br/>
	 * <b>Attention :</b> cette méthode n'est pas destin�e à la programmation des IA 
	 * 
	 * @return	
	 * 		l'id du sprite
	 */
	public int getId();
	
	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie l'�tat dans lequel se trouve ce sprite
	 * (ie: quelle action il est en train d'effectuer ou de subir)
	 * 
	 * @return	
	 * 		l'�tat du sprite
	 */
	public AiState getState();
	
	/**
	 * renvoie vrai si ce sprite a �t� �limin� du jeu
	 * 
	 * @return	
	 * 		vrai si le sprite n'est plus en jeu
	 */
	public boolean hasEnded();
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la représentation de la case contenant ce sprite
	 * 
	 *  @return
	 *  	la case contenant ce sprite
	 */
	public AiTile getTile();
	
	/** 
	 * renvoie le num�ro de la ligne contenant ce sprite 
	 * 
	 * @return	
	 * 		le num�ro de la ligne du sprite
	 */
	public int getLine();

	/** 
	 * renvoie le num�ro de la colonne contenant ce sprite
	 * 
	 * @return	
	 * 		le num�ro de la colonne du sprite
	 */
	public int getCol();
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie l'abscisse de ce sprite en pixels 
	 * 
	 * @return	
	 * 		l'abscisse du sprite
	 */
	public double getPosX();
	
	/** 
	 * renvoie l'ordonnée de ce sprite en pixels 
	 * 
	 * @return	
	 * 		l'ordonnée du sprite
	 */
	public double getPosY();
	
	/** 
	 * renvoie l'altitude de ce sprite en pixels 
	 * 
	 * @return	
	 * 		l'altitude du sprite
	 */
	public double getPosZ();
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la vitesse de déplacement courante du sprite exprimée en pixel/seconde.
	 * si le sprite ne bouge pas, elle est de z�ro.
	 * 
	 * @return	
	 * 		vitesse de déplacement du sprite en pixels/seconde
	 */
	public double getCurrentSpeed();
	
	/////////////////////////////////////////////////////////////////
	// COLLISION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Teste si le sprite passé en paramètre est capable de traverser
	 * la case de ce sprite
	 * 
	 *  @param sprite
	 *  	le sprite à tester
	 *  @return	
	 *  	vrai si ce sprite le laisser passer par sa case 
	 */
	public boolean isCrossableBy(AiSprite sprite);

	/////////////////////////////////////////////////////////////////
	// BURN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le temps que va mettre ce sprite à br�ler s'il est touch�
	 * par du feu, exprimé en ms. Si ce sprite ne peut pas br�ler, ce
	 * temps est �gal à -1.
	 * <b>Attention :</b> si ce sprite est une bombe, ce temps correspond
	 * à la dur�e mise par la bombe pour dispara�tre, et non pas à la dur�e
	 * de l'explosion. La dur�e de l'explosion correspond à la valeur
	 * renvoy�e par getBurningDuration pour le sprite de feu produit
	 * par l'explosion de la bombe, ou aussi à la dur�e renvoy�e par 
	 * getExplosionDuration pour la bombe.
	 * 
	 * @return	
	 * 		le temps que ce sprite va mettre à br�ler, en ms
	 */
	public long getBurningDuration();
}
