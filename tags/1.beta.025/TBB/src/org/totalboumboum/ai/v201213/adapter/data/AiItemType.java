package org.totalboumboum.ai.v201213.adapter.data;

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
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public enum AiItemType
{
	////////////////////////
	// BOMBES
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
	// PORTÉE
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
	// VITESSE
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
	// EFFET ALÉATOIRE
	////////////////////////
	/** Effet négatif sur le joueur (ralentissement, constipation, etc.) */
	RANDOM_NONE(false),
	
	/** Effet positif sur le joueur (bombe supplémentaire, portée étendue, etc.) */
	RANDOM_EXTRA(true),
	

	////////////////////////
	// AUTRES
	////////////////////////
	/** Le joueur peut frapper dans une bombe et ainsi l'envoyer plus loin */
	// (pas utilisé)
	PUNCH(true),
	
	/** Autre type d'item */
	OTHER(false);
	
	/**
	 * Constructeur standard.
	 * 
	 * @param bonus
	 * 		Indique si le type correspond à un bonus.
	 */
	AiItemType(boolean bonus)
	{	this.bonus = bonus;
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
	
    /**
	 * calcule l'AiItemType correspondant au nom d'item passé en paramètre
	 * 
	 * @param name	
	 * 		nom de l'item à traiter
	 * @return	
	 * 		symbole représentant ce type d'item
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
}
