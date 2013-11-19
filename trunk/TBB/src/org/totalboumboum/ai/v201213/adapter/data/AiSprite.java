package org.totalboumboum.ai.v201213.adapter.data;

import java.io.Serializable;

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

/**
 * Cette interface permet de représenter les sprites manipulés par le jeu,
 * et un nombre restreint de leurs propriétés, rendues ainsi accessible à l'agent.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public interface AiSprite extends Comparable<AiSprite>, Serializable
{	
	/////////////////////////////////////////////////////////////////
	// ID				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie le numéro unique du sprite dans le jeu.
	 * <br/>
	 * <b>Attention :</b> cette méthode n'est pas destinée 
	 * à la programmation des agents.
	 * 
	 * @return	
	 * 		l'id du sprite
	 */
	public int getId();
	
	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie l'état dans lequel se trouve ce sprite
	 * (i.e. : quelle action il est en train d'effectuer ou de subir).
	 * 
	 * @return	
	 * 		L'état du sprite.
	 */
	public AiState getState();
	
	/**
	 * Renvoie vrai si ce sprite a été éliminé du jeu.
	 * 
	 * @return	
	 * 		{@code true} ssi le sprite n'est plus en jeu.
	 */
	public boolean hasEnded();
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie la représentation de la case contenant ce sprite.
	 * 
	 *  @return
	 *  	La case contenant ce sprite.
	 */
	public AiTile getTile();
	
	/** 
	 * Renvoie le numéro de la ligne contenant ce sprite. 
	 * 
	 * @return	
	 * 		Le numéro de la ligne du sprite.
	 */
	public int getRow();

	/** 
	 * Renvoie le numéro de la colonne contenant ce sprite.
	 * 
	 * @return	
	 * 		Le numéro de la colonne du sprite.
	 */
	public int getCol();
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie l'abscisse de ce sprite en pixels. 
	 * 
	 * @return	
	 * 		L'abscisse du sprite.
	 */
	public double getPosX();
	
	/** 
	 * Renvoie l'ordonnée de ce sprite en pixels. 
	 * 
	 * @return	
	 * 		L'ordonnée du sprite.
	 */
	public double getPosY();
	
	/** 
	 * Renvoie l'altitude de ce sprite en pixels. 
	 * 
	 * @return	
	 * 		L'altitude du sprite.
	 */
	public double getPosZ();
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la vitesse de déplacement courante du sprite exprimée en pixel/seconde.
	 * Si le sprite ne bouge pas, elle est de zéro.
	 * 
	 * @return	
	 * 		Vitesse de déplacement du sprite en pixels/seconde.
	 */
	public double getCurrentSpeed();
	
	/////////////////////////////////////////////////////////////////
	// COLLISION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Teste si le sprite passé en paramètre est capable de traverser
	 * la case de ce sprite.
	 * 
	 *  @param sprite
	 *  	le sprite à tester.
	 *  @return	
	 *  	{@code true} ssi ce sprite le laisse passer par sa case.
	 */
	public boolean isCrossableBy(AiSprite sprite);

	/////////////////////////////////////////////////////////////////
	// BURN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le temps que va mettre ce sprite à brûler s'il est touché
	 * par du feu, exprimé en ms. Si ce sprite ne peut pas brûler, ce
	 * temps est égal à -1.
	 * <br/>
	 * <b>Attention :</b> si ce sprite est une bombe, ce temps correspond
	 * à la durée mise par la bombe pour disparaître, et non pas à la durée
	 * de l'explosion. La durée de l'explosion correspond à la valeur
	 * renvoyée par {@code getBurningDuration} pour le sprite de feu produit
	 * par l'explosion de la bombe, ou aussi à la durée renvoyée par 
	 * {@code getExplosionDuration} pour la bombe.
	 * 
	 * @return	
	 * 		Le temps que ce sprite va mettre à brûler, en ms.
	 */
	public long getBurningDuration();

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o);

	@Override
    public int hashCode();
 	
	@Override
	public int compareTo(AiSprite sprite);
}
