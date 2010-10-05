package org.totalboumboum.ai.v200809.adapter;

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

import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * représente un personnage du jeu, ie un sprite contrôlé par un joueur
 * humain ou une IA.
 * 
 * @author Vincent Labatut
 *
 */
public class AiHero extends AiSprite<Hero>
{
	/**
	 * crée une représentation du joueur passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à représenter
	 */
	AiHero(AiTile tile, Hero sprite)
	{	super(tile,sprite);
		initColor();
		updateBombParam();
	}
	
	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateBombParam();
	}

	@Override
	void finish()
	{	super.finish();
	}

	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Hero: [");
		result.append(super.toString());
		result.append(" - bmbCnt.: "+bombCount);
		result.append(" - bmbNbr.: "+bombNumber);
		result.append(" - bmbRge.: "+bombRange);
		result.append(" - color: "+color);
		result.append(" ]");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// BOMB PARAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** portée des bombes du personnage */
	private int bombRange;
	/** nombre de bombes que le personnage peut poser simultanément */
	private int bombNumber;
	/** nombre de bombes que le personnage a actuellement posées */
	private int bombCount;
	
	/**
	 * renvoie la portée actuelle des bombes du personnage
	 * 
	 * @return	la portée des bombes
	 */
	public int getBombRange()
	{	return bombRange;
	}
	
	/**
	 * renvoie le nombre de bombes que le personnage peut poser simultanément,
	 * à ce moment du jeu.
	 * 
	 * @return	le nombre de bombes simultanément posables
	 */
	public int getBombNumber()
	{	return bombNumber;
	}
	
	/**
	 * renvoie le nombre de bombes posées par le personnage à ce moment-là
	 * 
	 * @return	nombre de bombes posées
	 */
	public int getBombCount()
	{	return bombCount;
	}
	
	private void updateBombParam()
	{	Hero sprite = getSprite();
		
		// bomb range
		StateAbility ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE);
        bombRange = (int)ab.getStrength();
        ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_RANGE_MAX);
		if(ab.isActive())
		{	int limit = (int)ab.getStrength();
			if(bombRange>limit)
				bombRange = limit;
		}
		
        // max number of simultaneous bombs
    	ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER);
    	bombNumber = (int)ab.getStrength();
        ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER_MAX);
		if(ab.isActive())
		{	int limit = (int)ab.getStrength();
			if(bombNumber>limit)
				bombNumber = limit;
		}

		// number of bombs currently dropped
    	bombCount = sprite.getDroppedBombs().size();
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleur du personnage (et de ses bombes) */
	private PredefinedColor color;
	
	/**
	 * renvoie la couleur de ce personnage (et de ses bombes)
	 * 
	 * @return un symbole de type PredefinedColor représentant une couleur
	 */
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/**
	 * initialise la couleur du personnage
	 */
	private void initColor()
	{	Hero sprite = getSprite();
		color = sprite.getColor();	
	}
}
