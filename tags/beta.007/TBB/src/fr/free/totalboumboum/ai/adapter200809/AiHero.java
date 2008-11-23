package fr.free.totalboumboum.ai.adapter200809;

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

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;

/**
 * repr�sente un personnage du jeu, ie un sprite contr�l� par un joueur
 * humain ou une IA.
 * 
 * @author Vincent
 *
 */
public class AiHero extends AiSprite<Hero>
{
	/**
	 * cr�e une repr�sentation du joueur pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite � repr�senter
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

	/////////////////////////////////////////////////////////////////
	// BOMB PARAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** port�e des bombes du personnage */
	private int bombRange;
	/** nombre de bombes que le personnage peut poser simultan�ment */
	private int bombNumber;
	/** nombre de bombes que le personnage a actuellement pos�es */
	private int bombCount;
	
	/**
	 * renvoie la port�e actuelle des bombes du personnage
	 * 
	 * @return	la port�e des bombes
	 */
	public int getBombRange()
	{	return bombRange;
	}
	
	/**
	 * renvoie le nombre de bombes que le personnage peut poser simultan�ment,
	 * � ce moment du jeu.
	 * 
	 * @return	le nombre de bombes simultan�ment posables
	 */
	public int getBombNumber()
	{	return bombNumber;
	}
	
	/**
	 * renvoie le nombre de bombes pos�es par le personnage � ce moment-l�
	 * 
	 * @return	nombre de bombes pos�es
	 */
	public int getBombCount()
	{	return bombCount;
	}
	
	private void updateBombParam()
	{	Hero sprite = getSprite();
		// bomb range
		StateAbility ab = sprite.computeCapacity(StateAbility.BOMB_RANGE);
        bombRange = (int)ab.getStrength();
		// max number of simultaneous bombs
    	ab = sprite.computeCapacity(StateAbility.BOMB_NUMBER);
    	bombNumber = (int)ab.getStrength();
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
	 * @return un symbole de type PredefinedColor repr�sentant une couleur
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
