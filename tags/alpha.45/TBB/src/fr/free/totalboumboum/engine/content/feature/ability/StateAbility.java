package fr.free.totalboumboum.engine.content.feature.ability;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.loop.Loop;

public class StateAbility extends AbstractAbility
{	
	/*************************
	 * STATES ABILITIES
	 *************************/
	/** 
	 * sprite complètement plat, sans relief 
	 */
	public static final String SPRITE_FLAT = "sprite-flat";
	/** 
	 * sprite situé en hauteur, a dessiner par dessus tous les autres graphismes 
	 */
	public static final String SPRITE_ABOVE= "sprite-above";
	/** 
	 * capacité à traverser les bombes
	 */
	public static final String SPRITE_TRAVERSE_BOMB = "sprite-traverse-bomb";
	/** 
	 * capacité à traverser les bombes
	 */
	public static final String SPRITE_TRAVERSE_ITEM = "sprite-traverse-item";
	/** 
	 * capacité à traverser les murs
	 */
	public static final String SPRITE_TRAVERSE_WALL = "sprite-traverse-wall";
	/** 
	 * capacité à repousser pour un bloc
	 */
	public static final String BLOCK_SPAWN = "block-spawn";
	/** 
	 * définit la durée que met une bombe à exploser quand elle est consumée par une
	 * explosion extérieure (pour les réactions en chaîne). 
	 */
	public static final String BOMB_EXPLOSION_LATENCY = "bomb-explosion-latency";
	/** 
	 * durée maximale de la panne de la bombe 
	 */
	public static final String BOMB_FAILURE_MAXDURATION = "bomb-failure-maxduration";
	/** 
	 * durée minimale de la panne de la bombe 
	 */
	public static final String BOMB_FAILURE_MINDURATION = "bomb-failure-minduration";
	/** 
	 * probabilité qu'une bombe tombe en panne quand elle reçoit un ordre d'explosion :
	 * que ce soit temps, contact ou télécommande (ou autre) 
	 */
	public static final String BOMB_FAILURE_PROBABILITY = "bomb-failure-probability";
	/** 
	 * durée pendant laquelle la bombe doit être poussée avant de se mettre à glisser 
	 */
	public static final String BOMB_PUSH_LATENCY = "bomb-push-latency";
	/** 
	 * bombe déclenchée par une collision 
	 */
	public static final String BOMB_TRIGGER_COLLISION = "bomb-trigger-collision";
	/** 
	 * bombe déclenchée à la combustion 
	 */
	public static final String BOMB_TRIGGER_COMBUSTION = "bomb-trigger-combustion";
	/** 
	 * bombe télécommandée par le joueur 
	 */
	public static final String BOMB_TRIGGER_CONTROL = "bomb-trigger-control";
	/** 
	 * bombe à retardement, l'explosion est déclenchée au bout d'un certain temps 
	 */
	public static final String BOMB_TRIGGER_TIMER = "bomb-trigger-timer";
	/** 
	 * nombre de bombes pouvant être posées en même temps 
	 */
	public static final String BOMB_NUMBER = "bomb-number";
	/** 
	 * portée des bombes 
	 */
	public static final String BOMB_RANGE = "bomb-range";
	/** 
	 * durée pendant laquelle le hero doit pleurer/exulter à la fin du round 
	 */
	public static final String HERO_CELEBRATION_DURATION = "hero-celebration-duration";
	/** 
	 * durée de l'animation d'entrée du héros au début du round 
	 */
	public static final String HERO_ENTRY_DURATION = "hero-entry-duration";
	/** 
	 * durée du coup de poing donné par le hero 
	 */
	public static final String HERO_PUNCH_DURATION = "hero-punch-duration";
	/** 
	 * durée pendant laquelle le hero doit être inactif avant de passer en gesture waiting 
	 */
	public static final String HERO_WAIT_DURATION = "hero-wait-duration";
	
	
	/** 
	 * name of the ability 
	 */
	private String name;
	
	public StateAbility(String name, Level level)
	{	super(level);
		this.name = name;
	}
	
	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}

	public boolean equals(Object ability)
	{	boolean result = false;
		if(ability instanceof StateAbility)
		{	StateAbility ab = (StateAbility)ability;
			// name
			result = name.equalsIgnoreCase(ab.getName());
		}
		return result;
	}
	
	public AbstractAbility copy()
	{	StateAbility result;
		result = new StateAbility(name,level);
		result.setStrength(strength);
		result.setUses(uses);
		result.setTime(time);
		return result;
	}

	public String toString()
	{	String result = name+"("+")";
		result = result+"["+strength+"]";
		return result;
	}
	
	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}

}
