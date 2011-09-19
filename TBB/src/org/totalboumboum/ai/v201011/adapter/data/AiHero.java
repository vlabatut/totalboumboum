package org.totalboumboum.ai.v201011.adapter.data;

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

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * repr�sente un personnage du jeu, ie un sprite contr�l� par un joueur
 * humain ou une IA.
 * 
 * @author Vincent Labatut
 *
 */
public interface AiHero extends AiSprite
{
	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie un exemple de bombe que ce personnage peut poser
	 * 
	 * @return	
	 * 		une repr�sentation de la bombe
	 */
	public AiBomb getBombPrototype();

	/**
	 * renvoie la port�e actuelle des bombes du personnage
	 * 
	 * @return	
	 * 		la port�e des bombes
	 */
	public int getBombRange();
	
	/**
	 * renvoie la dur�e actuelle des bombes du personnage
	 * (valide seulement pour les bombes à retardement)
	 * 
	 * @return	
	 * 		la dur�e de vie des bombes (i.e. temps entre la pose et l'explosion)
	 */
	public long getBombDuration();
	
	/**
	 * renvoie la dur�e actuelle des explosions des bombes du personnage
	 * 
	 * @return	
	 * 		la dur�e de l'explosion des bombes
	 */
	public long getExplosionDuration();
	
	/**
	 * renvoie le nombre de bombes que le personnage peut poser simultan�ment,
	 * à ce moment du jeu.
	 * Ce nombre correspond à la somme du nombre de bombes actuellement d�j� 
	 * pos�es (getBombNumberCurrent) plus le nombre de bombes que le joueur peut encore poser. 
	 * 
	 * @return	
	 * 		le nombre de bombes simultan�ment posables (en g�n�ral)
	 */
	public int getBombNumberMax();
	
	/**
	 * renvoie le nombre de bombes pos�es par le personnage à ce moment-l�.
	 * Ce nombre est limit� par la valeur renvoy�e par getBombNumberMax,
	 * i.e. il ne peut pas �tre plus grand puisque getBombNumberMax renvoie
	 * le nombre de bombes maximal que le joueur peut poser en même temps. 
	 * 
	 * @return	
	 * 		nombre de bombes pos�es en ce moment
	 */
	public int getBombNumberCurrent();
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la couleur de ce personnage (et de ses bombes)
	 * 
	 * @return 
	 * 		un symbole de type PredefinedColor repr�sentant une couleur
	 */
	public PredefinedColor getColor();
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la vitesse de déplacement au sol de ce personnage,
	 * exprim�e en pixel/seconde. il ne s'agit pas de la vitesse 
	 * de déplacement courante, il s'agit de la vitesse du personnage
	 * quand il marche. Cette vitesse peut �tre modifi�e par certains items.
	 * 
	 * @return	
	 * 		la vitesse de déplacement de ce personnage
	 */
	public double getWalkingSpeed();
	
	/////////////////////////////////////////////////////////////////
	// RANKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le classement de ce joueur, pour la manche en cours.
	 * Ce classement est susceptible d'�voluer d'ici la fin de la manche actuellement jou�e, 
	 * par exemple si ce joueur est �limin�.
	 * 
	 * @return	
	 * 		le classement de ce joueur dans la manche en cours
	 */
	public int getRoundRank();
	
	/**
	 * Renvoie le classement de ce joueur, pour la rencontre en cours.
	 * Ce classement n'�volue pas pendant la manche actuellement jou�e.
	 * 
	 * @return	
	 * 		le classement de ce joueur dans la rencontre en cours
	 */
	public int getMatchRank();
	
	/**
	 * Renvoie le classement de ce joueur, dans le classement g�n�ral du jeu (Glicko-2)
	 * Ce classement n'�volue pas pendant la manche actuellement jou�e.
	 * 
	 * @return	
	 * 		le classement g�n�ral (Glicko-2) de ce joueur
	 */
	public int getStatsRank();

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * teste si ce personnage est capable de passer à travers les (certains) murs
	 * <b>ATTENTION :</b> cette méthode ne devrait pas �tre utilisée directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le personnage traverse les murs
	 */
	public boolean hasThroughBlocks();

	/**
	 * teste si ce personnage est capable de passer à travers les bombes
	 * <b>ATTENTION :</b> cette méthode ne devrait pas �tre utilisée directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le personnage traverse les bombes
	 */
	public boolean hasThroughBombs();

	/**
	 * teste si ce personnage est capable de passer à travers le feu sans br�ler
	 * <b>ATTENTION :</b> cette méthode ne devrait pas �tre utilisée directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le personnage r�siste au feu
	 */
	public boolean hasThroughFires();
}
