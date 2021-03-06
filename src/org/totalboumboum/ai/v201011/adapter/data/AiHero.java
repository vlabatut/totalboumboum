package org.totalboumboum.ai.v201011.adapter.data;

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

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * représente un personnage du jeu, ie un sprite contrôlé par un joueur
 * humain ou une IA.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
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
	 * 		une représentation de la bombe
	 */
	public AiBomb getBombPrototype();

	/**
	 * renvoie la portée actuelle des bombes du personnage
	 * 
	 * @return	
	 * 		la portée des bombes
	 */
	public int getBombRange();
	
	/**
	 * renvoie la durée actuelle des bombes du personnage
	 * (valide seulement pour les bombes à retardement)
	 * 
	 * @return	
	 * 		la durée de vie des bombes (i.e. temps entre la pose et l'explosion)
	 */
	public long getBombDuration();
	
	/**
	 * renvoie la durée actuelle des explosions des bombes du personnage
	 * 
	 * @return	
	 * 		la durée de l'explosion des bombes
	 */
	public long getExplosionDuration();
	
	/**
	 * renvoie le nombre de bombes que le personnage peut poser simultanément,
	 * à ce moment du jeu.
	 * Ce nombre correspond à la somme du nombre de bombes actuellement déjà 
	 * posées (getBombNumberCurrent) plus le nombre de bombes que le joueur peut encore poser. 
	 * 
	 * @return	
	 * 		le nombre de bombes simultanément posables (en général)
	 */
	public int getBombNumberMax();
	
	/**
	 * renvoie le nombre de bombes posées par le personnage à ce moment-là.
	 * Ce nombre est limité par la valeur renvoyée par getBombNumberMax,
	 * i.e. il ne peut pas être plus grand puisque getBombNumberMax renvoie
	 * le nombre de bombes maximal que le joueur peut poser en même temps. 
	 * 
	 * @return	
	 * 		nombre de bombes posées en ce moment
	 */
	public int getBombNumberCurrent();
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la couleur de ce personnage (et de ses bombes)
	 * 
	 * @return 
	 * 		un symbole de type PredefinedColor représentant une couleur
	 */
	public PredefinedColor getColor();
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la vitesse de déplacement au sol de ce personnage,
	 * exprimée en pixel/seconde. il ne s'agit pas de la vitesse 
	 * de déplacement courante, il s'agit de la vitesse du personnage
	 * quand il marche. Cette vitesse peut être modifiée par certains items.
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
	 * Ce classement est susceptible d'évoluer d'ici la fin de la manche actuellement jouée, 
	 * par exemple si ce joueur est éliminé.
	 * 
	 * @return	
	 * 		le classement de ce joueur dans la manche en cours
	 */
	public int getRoundRank();
	
	/**
	 * Renvoie le classement de ce joueur, pour la rencontre en cours.
	 * Ce classement n'évolue pas pendant la manche actuellement jouée.
	 * 
	 * @return	
	 * 		le classement de ce joueur dans la rencontre en cours
	 */
	public int getMatchRank();
	
	/**
	 * Renvoie le classement de ce joueur, dans le classement général du jeu (Glicko-2)
	 * Ce classement n'évolue pas pendant la manche actuellement jouée.
	 * 
	 * @return	
	 * 		le classement général (Glicko-2) de ce joueur
	 */
	public int getStatsRank();

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * teste si ce personnage est capable de passer à travers les (certains) murs
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le personnage traverse les murs
	 */
	public boolean hasThroughBlocks();

	/**
	 * teste si ce personnage est capable de passer à travers les bombes
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le personnage traverse les bombes
	 */
	public boolean hasThroughBombs();

	/**
	 * teste si ce personnage est capable de passer à travers le feu sans brûler
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	
	 * 		vrai si le personnage résiste au feu
	 */
	public boolean hasThroughFires();
}
