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
	 * renvoie la port�e actuelle des bombes du personnage
	 * 
	 * @return	la port�e des bombes
	 */
	public int getBombRange();
	
	/**
	 * renvoie le nombre de bombes que le personnage peut poser simultan�ment,
	 * � ce moment du jeu.
	 * Ce nombre correspond � la somme du nombre de bombes actuellement d�j� 
	 * pos�es (getBombCount) plus le nombre de bombes que le joueur peut encore poser. 
	 * 
	 * @return	le nombre de bombes simultan�ment posables (en g�n�ral)
	 */
	public int getBombNumber();
	
	/**
	 * renvoie le nombre de bombes pos�es par le personnage � ce moment-l�.
	 * Ce nombre est limit� par la valeur renvoy�e par getBombNumber(),
	 * i.e. il ne peut pas �tre plus grand puisque getBombNumber renvoie
	 * le nombre de bombes maximal que le joueur peut poser en m�me temps. 
	 * 
	 * @return	nombre de bombes pos�es en ce moment
	 */
	public int getBombCount();
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la couleur de ce personnage (et de ses bombes)
	 * 
	 * @return un symbole de type PredefinedColor repr�sentant une couleur
	 */
	public PredefinedColor getColor();
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la vitesse de d�placement au sol de ce personnage,
	 * exprim�e en pixel/seconde. il ne s'agit pas de la vitesse 
	 * de d�placement courante, il s'agit de la vitesse du personnage
	 * quand il marche. Cette vitesse peut �tre modifi�e par certains items.
	 * 
	 * @return	la vitesse de d�placement de ce personnage
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
	 * @return	le classement de ce joueur dans la manche en cours
	 */
	public int getRoundRank();
	
	/**
	 * Renvoie le classement de ce joueur, pour la rencontre en cours.
	 * Ce classement n'�volue pas pendant la manche actuellement jou�e.
	 * 
	 * @return	le classement de ce joueur dans la rencontre en cours
	 */
	public int getMatchRank();
	
	/**
	 * Renvoie le classement de ce joueur, dans le classement g�n�ral du jeu (Glicko-2)
	 * Ce classement n'�volue pas pendant la manche actuellement jou�e.
	 * 
	 * @return	le classement g�n�ral (Glicko-2) de ce joueur
	 */
	public int getStatsRank();

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * teste si ce personnage est capable de passer � travers les (certains) murs
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si le personnage traverse les murs
	 */
	public boolean hasThroughBlocks();

	/**
	 * teste si ce personnage est capable de passer � travers les bombes
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si le personnage traverse les bombes
	 */
	public boolean hasThroughBombs();

	/**
	 * teste si ce personnage est capable de passer � travers le feu sans br�ler
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si le personnage r�siste au feu
	 */
	public boolean hasThroughFires();
}
