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


/**
 * Représente un item du jeu, ie un bonus ou un malus que le joueur peut ramasser.
 * un item est caractérisé par son type, représentant le pouvoir apporté (ou enlevé)
 * par l'item. Ce type est représentée par une valeur de type {@link AiItemType}.
 * 
 * @author Vincent Labatut
 */
public interface AiItem extends AiSprite
{	
	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le type de l'item représenté.
	 * 
	 * @return	
	 * 		Le type de l'item.
	 */
	public AiItemType getType();
	
	/////////////////////////////////////////////////////////////////
	// STRENGTH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la force de cet item.
	 * <br/>
	 * Cette méthode est destinée à un usage interne, vous
	 * (le concepteur de l'agent) ne devez pas l'utiliser.
	 * Servez-vous plutot de la méthode {@link AiHero#getEffect(AiItem)}.
	 * 
	 * @return
	 * 		Valeur réelle représentant la force de cet item.
	 */
	public double getStrength();

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indique si cet item arrête les explosions.
	 * <br/>
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'agent,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * Utilisez plutot {@link #isCrossableBy}.
	 * 
	 * @return	
	 * 		Une valeur AiStopType indiquant si cet item arrête le feu.
	 */
	public AiStopType hasStopFires();

	/**
	 * Indique si cet item arrête les bombes.
	 * <br/>
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'agent,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * Utilisez plutot {@link #isCrossableBy}.
	 * 
	 * @return	
	 * 		Une valeur AiStopType indiquant si cet item arrête les bombes.
	 */
	public AiStopType hasStopBombs();

	/////////////////////////////////////////////////////////////////
	// CONTAGION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indique si l'effet de cet item est transmis par contagion.
	 * 
	 * @return
	 * 		{@code true} ssi l'effet de l'item est contagieux.
	 */
	public boolean isContagious();

	/////////////////////////////////////////////////////////////////
	// DURATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indique si cet item a un effet limité dans le temps.
	 * 
	 * @return
	 * 		{@code true} ssi cet item a un effet limité.
	 */
	public boolean hasLimitedDuration();
	
	/**
	 * Renvoie le délai normal avant la fin de l'effet de l'item.
	 * Ce délai ne tient pas compte des réinitialisations éventuelles.
	 * <br/>
	 * <b>Attention :</b> Ce délai n'est pas défini pour tous les types 
	 * d'items. Il faut utiliser la méthode {@link #hasLimitedDuration}
	 * pour s'en assurer.
	 * 
	 * @return	
	 * 		Le délai normal avant la fin de l'effet de cet item, 
	 * 		exprimé en millisecondes.
	 */
	public long getNormalDuration();

	/**
	 * Renvoie le temps écoulé depuis que l'item a commencé à faire
	 * son effet, exprimé en millisecondes. Bien sûr ceci
	 * n'est valide que pour les items dont l'effet est limité dans le
	 * temps.
	 * 
	 * @return	
	 * 		Temps d'effet exprimé en ms.
	 */
	// généralisation : Certaines actions sont susceptibles de réinitialiser le temps d'effet.
	public long getElapsedTime();
}
