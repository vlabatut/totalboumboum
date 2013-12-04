package org.totalboumboum.ai.v201314.adapter.data;

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
 * Types d'items reconnus par l'agent.
 * 	
 * @author Vincent Labatut
 *
 */
public enum AiItemType implements Serializable
{
	////////////////////////
	// BOMBS
	////////////////////////
	/** Le joueur ne peut plus poser de bombe du tout (malus temporaire) = constipation */
	NO_BOMB(false),

	/** Le joueur peut poser une bombe de moins (malus) */
	ANTI_BOMB(false),

	/** Le joueur peut poser une bombe de plus */
	EXTRA_BOMB(true),
	
	/** Donne le nombre de bombes maximal */
	GOLDEN_BOMB(true),
	
	////////////////////////
	// RANGE
	////////////////////////
	/** Les bombes ont une portée nulle (malus temporaire) */
	NO_FLAME(false),

	/** Diminue la portée des bombes */
	ANTI_FLAME(false),
	
	/** Augmente la portée des bombes */
	EXTRA_FLAME(true),
	
	/** Donne la portée maximale */
	GOLDEN_FLAME(true),
	
	////////////////////////
	// SPEED
	////////////////////////
	/** Le joueur se déplace très lentement (malus temporaire) */
	NO_SPEED(false),
	
	/** Le joueur se déplace plus lentement (malus) */
	ANTI_SPEED(false),
	
	/** Le joueur se déplace plus rapidement */
	EXTRA_SPEED(true),
	
	/** Donne la vitesse maximale */
	GOLDEN_SPEED(true),

	////////////////////////
	// RANDOM
	////////////////////////
	/** Effet négatif sur le joueur (ralentissement, constipation, etc.) */
	RANDOM_NONE(false),
	
	/** Effet positif sur le joueur (bombe supplémentaire, portée étendue, etc.) */
	RANDOM_EXTRA(true),
	

	////////////////////////
	// OTHERS
	////////////////////////
	/** Le joueur peut frapper dans une bombe et ainsi l'envoyer plus loin */
	// (pas utilisé)
	PUNCH(true),
	
	/** Autre type d'item */
	OTHER(false);
	
	////////////////////////
	// GENERAL
	////////////////////////
	/**
	 * Constructeur standard.
	 * 
	 * @param bonus
	 * 		Indique si le type correspond à un bonus.
	 */
	AiItemType(boolean bonus)
	{	this.bonus = bonus;
    }

	/**
	 * Calcule l'AiItemType correspondant au nom d'item passé en paramètre.
	 * 
	 * @param name	
	 * 		Nom de l'item à traiter.
	 * @return	
	 * 		Symbole représentant ce type d'item.
	 */
	public static AiItemType makeItemType(String name)
	{	AiItemType result;
	
		// bombes
		if(name.equalsIgnoreCase("nobomb"))
			result = NO_BOMB;
		else if(name.equalsIgnoreCase("antibomb"))
			result = ANTI_BOMB;
		else if(name.equalsIgnoreCase("extrabomb"))
			result = EXTRA_BOMB;
		else if(name.equalsIgnoreCase("goldenbomb"))
			result = GOLDEN_BOMB;
		
		// portée
		else if(name.equalsIgnoreCase("noflame"))
			result = NO_FLAME;
		else if(name.equalsIgnoreCase("antiflame"))
			result = ANTI_FLAME;
		else if(name.equalsIgnoreCase("extraflame"))
			result = EXTRA_FLAME;
		else if(name.equalsIgnoreCase("goldenflame"))
			result = GOLDEN_FLAME;
		
		// vitesse
		else if(name.equalsIgnoreCase("nospeed"))
			result = NO_SPEED;
		else if(name.equalsIgnoreCase("antispeed"))
			result = ANTI_SPEED;
		else if(name.equalsIgnoreCase("extraspeed")
				|| name.equalsIgnoreCase("speed"))
			result = EXTRA_SPEED;
		else if(name.equalsIgnoreCase("goldenspeed")
				|| name.equalsIgnoreCase("superspeed"))
			result = GOLDEN_SPEED;

		// aléatoires
		else if(name.equalsIgnoreCase("malus") 
				|| name.equalsIgnoreCase("disease")
				|| name.equalsIgnoreCase("randomnone"))
			result = RANDOM_NONE;
		else if(name.equalsIgnoreCase("question")
				|| name.equalsIgnoreCase("random")
				|| name.equalsIgnoreCase("randomextra"))
			result = RANDOM_EXTRA;

		// autres
		else if(name.equalsIgnoreCase("punch"))
			result = PUNCH;
		else
			result = OTHER;
		return result;
	}

	////////////////////////
	// BONUS/MALUS
	////////////////////////
	/** Variable vraie ssi l'item est un bonus */
	private final boolean bonus;

	/**
	 * Indique si ce type correspond à 
	 * un item bonus.
	 * 
	 * @return
	 * 		{@code true} ssi l'item est un bonus.
	 */
	public boolean isBonus()
	{	return bonus;
	}
	
	////////////////////////
	// KIND
	////////////////////////
	/**
	 * Détermine si ce type d'item
	 * concerne le nombre de bombes
	 * qu'un joueur peut poser.
	 * 
	 * @return
	 * 		{@code true} ssi ce type d'item
	 * 		concerne le nombre de bombes posables.
	 */
	public boolean isBombKind()
	{	boolean result = 
			this==ANTI_BOMB ||
			this==EXTRA_BOMB ||
			this==GOLDEN_BOMB ||
			this==NO_BOMB;
		return result;
	}

	/**
	 * Détermine si ce type d'item
	 * concerne la portée des bombes.
	 * 
	 * @return
	 * 		{@code true} ssi ce type d'item
	 * 		concerne la portée des bombes.
	 */
	public boolean isFlameKind()
	{	boolean result = 
			this==ANTI_FLAME ||
			this==EXTRA_FLAME ||
			this==GOLDEN_FLAME ||
			this==NO_FLAME;
		return result;
	}

	/**
	 * Détermine si ce type d'item
	 * concerne la vitesse de déplacement
	 * du joueur.
	 * 
	 * @return
	 * 		{@code true} ssi ce type d'item
	 * 		concerne la vitesse du joueur.
	 */
	public boolean isSpeedKind()
	{	boolean result = 
			this==ANTI_SPEED ||
			this==EXTRA_SPEED ||
			this==GOLDEN_SPEED ||
			this==NO_SPEED;
		return result;
	}

	/**
	 * Détermine si ce type d'item
	 * a un effet aléatoire.
	 * 
	 * @return
	 * 		{@code true} ssi ce type d'item
	 * 		a un effet aléatoire.
	 */
	public boolean isRandomKind()
	{	boolean result = 
			this==RANDOM_EXTRA ||
			this==RANDOM_NONE;
		return result;
	}

	/**
	 * Détermine si ce type d'item
	 * a un effet incrémental.
	 * 
	 * @return
	 * 		{@code true} ssi ce type d'item
	 * 		a un effet incrémental.
	 */
	public boolean isExtraKind()
	{	boolean result = 
			this==EXTRA_BOMB ||
			this==EXTRA_FLAME ||
			this==EXTRA_SPEED ||
			this==RANDOM_EXTRA;
		return result;
	}

	/**
	 * Détermine si ce type d'item
	 * a un effet décrémental.
	 * 
	 * @return
	 * 		{@code true} ssi ce type d'item
	 * 		a un effet décrémental.
	 */
	public boolean isAntiKind()
	{	boolean result = 
			this==ANTI_BOMB ||
			this==ANTI_FLAME ||
			this==ANTI_SPEED;
		return result;
	}

	/**
	 * Détermine si ce type d'item
	 * a un effet maximisant.
	 * 
	 * @return
	 * 		{@code true} ssi ce type d'item
	 * 		a un effet maximisant.
	 */
	public boolean isGoldenKind()
	{	boolean result = 
			this==GOLDEN_BOMB ||
			this==GOLDEN_FLAME ||
			this==GOLDEN_SPEED;
		return result;
	}

	/**
	 * Détermine si ce type d'item
	 * a un effet minimisant.
	 * 
	 * @return
	 * 		{@code true} ssi ce type d'item
	 * 		a un effet minimisant.
	 */
	public boolean isNoneKind()
	{	boolean result = 
			this==NO_BOMB ||
			this==NO_FLAME ||
			this==NO_SPEED ||
			this==RANDOM_NONE;
		return result;
	}
}
