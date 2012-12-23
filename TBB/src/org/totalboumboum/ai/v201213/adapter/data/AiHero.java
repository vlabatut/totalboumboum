package org.totalboumboum.ai.v201213.adapter.data;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.util.List;
import java.util.Map;

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Représente un personnage du jeu, ie un sprite contrôlé par un joueur
 * humain ou une agent.
 * 
 * @author Vincent Labatut
 */
public interface AiHero extends AiSprite
{
	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie un exemple de bombe que ce personnage peut poser
	 * 
	 * @return	
	 * 		Une représentation de la bombe.
	 */
	public AiBomb getBombPrototype();

	/**
	 * Renvoie la portée actuelle des bombes du personnage
	 * 
	 * @return	
	 * 		La portée des bombes.
	 */
	public int getBombRange();

	/**
	 * Renvoie la portée maximale que les bombes
	 * du personnage peuvent atteindre, dans l'absolu
	 * (i.e. limite qu'on ne peut pas dépasser, quel
	 * que soit le nombre d'items ramassés).
	 * 
	 * @return
	 * 		La portée limite des bombes.
	 */
	public int getBombRangeLimit();
	
	/**
	 * Renvoie la durée actuelle des bombes du personnage
	 * (valide seulement pour les bombes à retardement).
	 * 
	 * @return	
	 * 		La durée de vie des bombes (i.e. temps entre la pose et l'explosion).
	 */
	public long getBombDuration();
	
	/**
	 * Renvoie la durée actuelle des explosions des bombes du personnage.
	 * 
	 * @return	
	 * 		La durée de l'explosion des bombes.
	 */
	public long getExplosionDuration();
	
	/**
	 * Renvoie le nombre de bombes que le personnage peut poser simultanément,
	 * à ce moment du jeu.
	 * <br/>
	 * Ce nombre correspond à la somme du nombre de bombes actuellement déjà 
	 * posées ({@link #getBombNumberCurrent()}) plus le nombre de bombes que 
	 * le joueur peut encore poser. 
	 * 
	 * @return	
	 * 		Le nombre de bombes simultanément posables (en général).
	 */
	public int getBombNumberMax();
	
	/**
	 * Renvoie le nombre de bombes posées par le personnage à ce moment-là.
	 * Ce nombre est limité par la valeur renvoyée par {@link #getBombNumberMax()},
	 * i.e. il ne peut pas être plus grand puisque getBombNumberMax renvoie
	 * le nombre de bombes maximal que le joueur peut poser en même temps. 
	 * 
	 * @return	
	 * 		Nombre de bombes posées en ce moment.
	 */
	public int getBombNumberCurrent();
	
	/**
	 * Renvoie le nombre maximal de bombes que le personnage peut poser,
	 * dans l'absolu (i.e. limite qu'on ne peut pas dépasser, quel
	 * que soit le nombre d'items ramassés).
	 * 
	 * @return
	 * 		Le nombre maximal de bombes que le personnage peut 
	 * 		poser, dans l'absolu.
	 */
	public int getBombNumberLimit();
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la couleur de ce personnage (et de ses bombes).
	 * 
	 * @return 
	 * 		Un symbole de type {@link PredefinedColor} représentant une couleur.
	 */
	public PredefinedColor getColor();
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie l'index de vitesse de déplacement de ce personnage.
	 * <br/>
	 * Cette information est à usage interne, vous (le concepteur
	 * de l'agent) n'en avez pas besoin.
	 * 
	 * @return
	 * 		Un entier indiquant l'index de vitesse courant du joueur.
	 */
	public int getWalkingSpeedIndex();
	
	/**
	 * Renvoie une map contenant les vitesses de déplacement possible
	 * pour ce joueur.
	 * <br/>
	 * <b>Note :</b> cette information est à usage interne, vous (le concepteur
	 * de l'agent) n'en avez pas besoin. La map renvoyées n'est pas modifiable.
	 * Toute tentative de modification provoquera une 
	 * {@link UnsupportedOperationException}. 
	 * 
	 * @return
	 * 		Une map contenant les vitesses de déplacement possible du joueur.
	 */
	public Map<Integer,Double> getWalkingSpeeds();
	
	/**
	 * Renvoie la vitesse de déplacement au sol de ce personnage,
	 * exprimée en pixel/seconde. Il ne s'agit pas de la vitesse 
	 * de déplacement courante, il s'agit de la vitesse du personnage
	 * quand il marche. Cette vitesse peut être modifiée par certains items.
	 * 
	 * @return	
	 * 		La vitesse de déplacement de ce personnage.
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
	 * Teste si ce personnage est capable de passer à travers les (certains) murs.
	 * <br/>
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'agent,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot {@link #isCrossableBy}.
	 * 
	 * @return	
	 * 		{@code true} ssi le personnage traverse les murs.
	 */
	public boolean hasThroughBlocks();

	/**
	 * Teste si ce personnage est capable de passer à travers les bombes.
	 * <br/>
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'agent,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * Utilisez plutot {@link #isCrossableBy}.
	 * 
	 * @return	
	 * 		{@code true} ssi le personnage traverse les bombes.
	 */
	public boolean hasThroughBombs();

	/**
	 * Teste si ce personnage est capable de passer à travers le feu sans brûler.
	 * <br/>
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'agent,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * Utilisez plutot {@link #isCrossableBy}.
	 * 
	 * @return	
	 * 		{@code true} ssi le personnage résiste au feu.
	 */
	public boolean hasThroughFires();

	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie une valeur numérique que l'item spécifié aurait sur ce
	 * joueur. L'interprétation de cette valeur dépend de l'item considéré. 
	 * Par exemple, si on obtient +5 pour {@link AiItemType#EXTRA_BOMB}, 
	 * cela signifie que le joueur pourra poser jusqu'à 5 bombes, <i>s'il 
	 * ramasse cet item</i>.
	 * <br/>
	 * Si l'effet de l'item n'est pas quantifiable (ex : {@link AiItemType#RANDOM_EXTRA},
	 * {@link AiItemType#OTHER}, etc.), alors la fonction renvoie -1.
	 * 
	 * @param item
	 * 		L'item à considérer.
	 *  @return
	 *  	Une valeur numérique représentant la propriété modifiée par l'item,
	 *  	telle qu'elle sera après que ce joueur aura ramassé l'item.
	 *  	Ou bien -1 si le type de l'item n'a pas un effet quantifiable. 
	 */
	public double getEffect(AiItem item);

	/////////////////////////////////////////////////////////////////
	// CONTAGION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indique si ce joueur possède des items à l'effet contagieux.
	 * 
	 * @return
	 * 		{@code true} ssi le joueur possède des items contagieux.
	 */
	public boolean isContagious();
	
	/**
	 * Renvoie la liste des items contagieux possédés
	 * par ce joueur. La liste est vide s'il ne possède
	 * aucun item contagieux actuellement actif.
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return
	 * 		Une liste d'item, qui peut être vide.
	 */
	public List<AiItem> getContagiousItems();
}
