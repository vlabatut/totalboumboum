package org.totalboumboum.ai.v201011.adapter.data.actual;

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

import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * repr�sente un personnage du jeu, ie un sprite contr�l� par un joueur
 * humain ou une IA.
 * 
 * @author Vincent Labatut
 *
 */
final class AiDataHero extends AiDataSprite<Hero> implements AiHero
{
	/**
	 * cr�e une repr�sentation du joueur pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite � repr�senter
	 */
	protected AiDataHero(AiDataTile tile, Hero sprite)
	{	super(tile,sprite);
		initColor();
		updateBombParam();
		updateWalkingSpeed();
		updateCollisions();
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update(AiDataTile tile)
	{	super.update(tile);
		updateBombParam();
		updateWalkingSpeed();
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** port�e des bombes du personnage */
	private int bombRange;
	/** nombre de bombes que le personnage peut poser simultan�ment (en g�n�ral) */
	private int bombNumber;
	/** nombre de bombes que le personnage a actuellement pos�es */
	private int bombCount;
	
	/**
	 * renvoie la port�e actuelle des bombes du personnage
	 * 
	 * @return	la port�e des bombes
	 */
	@Override
	public int getBombRange()
	{	return bombRange;
	}
	
	/**
	 * renvoie le nombre de bombes que le personnage peut poser simultan�ment,
	 * � ce moment du jeu.
	 * Ce nombre correspond � la somme du nombre de bombes actuellement d�j� 
	 * pos�es (getBombCount) plus le nombre de bombes que le joueur peut encore poser. 
	 * 
	 * @return	le nombre de bombes simultan�ment posables (en g�n�ral)
	 */
	@Override
	public int getBombNumber()
	{	return bombNumber;
	}
	
	/**
	 * renvoie le nombre de bombes pos�es par le personnage � ce moment-l�.
	 * Ce nombre est limit� par la valeur renvoy�e par getBombCount()
	 * 
	 * @return	nombre de bombes pos�es
	 */
	@Override
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
    	
    	//System.out.println(getSprite().getColor()+": bombRange="+bombRange+" bombNumber="+bombNumber+" bombCount="+bombCount);    	
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
	@Override
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

	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** vitesse de d�placement au sol du personnage, exprim�e en pixel/seconde */
	private double walkingSpeed;
	
	/**
	 * renvoie la vitesse de d�placement au sol de ce personnage,
	 * exprim�e en pixel/seconde. il ne s'agit pas de la vitesse 
	 * de d�placement courante, il s'agit de la vitesse du personnage
	 * quand il marche. Cette vitesse peut �tre modifi�e par certains items.
	 * 
	 * @return	la vitesse de d�placement de ce personnage
	 */
	@Override
	public double getWalkingSpeed()
	{	return walkingSpeed;	
	}
	
	/**
	 * met � jour la vitesses de d�placement de ce personnage 
	 */
	private void updateWalkingSpeed()
	{	Sprite sprite = getSprite();
		double speedCoeff = sprite.getSpeedCoeff();
		Gesture walking = getSprite().getGesturePack().getGesture(GestureName.WALKING);
		double basicSpeed = walking.getTrajectoryDirection(Direction.RIGHT).getXInteraction();
		walkingSpeed = speedCoeff*basicSpeed;
		//System.out.println(getSprite().getColor()+": walkingSpeed="+walkingSpeed);
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage peut traverser les murs */
	private boolean throughBlocks;
	/** indique si le personnage peut traverser les bombes */
	private boolean throughBombs;
	/** indique si le personnage peut traverser le feu (sans br�ler) */
	private boolean throughFires;
	
	/**
	 * teste si ce personnage est capable de passer � travers les (certains) murs
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si le personnage traverse les murs
	 */
	protected boolean hasThroughBlocks()
	{	return throughBlocks;	
	}

	/**
	 * teste si ce personnage est capable de passer � travers les bombes
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si le personnage traverse les bombes
	 */
	protected boolean hasThroughBombs()
	{	return throughBombs;	
	}

	/**
	 * teste si ce personnage est capable de passer � travers le feu sans br�ler
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si le personnage r�siste au feu
	 */
	protected boolean hasThroughFires()
	{	return throughFires;	
	}

	/**
	 * met � jour les divers pouvoirs du personnage
	 */
	private void updateCollisions()
	{	Sprite sprite = getSprite();
		StateAbility ability;
		// traverser les murs
		ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TRAVERSE_WALL);
		throughBlocks = ability.isActive();
		// traverser les bombes
		ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TRAVERSE_BOMB);
		throughBombs = ability.isActive();
		// traverser le feu
		ability = sprite.modulateStateAbility(StateAbilityName.HERO_FIRE_PROTECTION);
		throughFires = ability.isActive();
	}
	
	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	boolean result = false;
		if(sprite instanceof AiDataFire)
			result = true;
		else if(sprite instanceof AiDataHero)
			result = true;
		else
			result = sprite.isCrossableBy(this);		
		return result;
	}

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
	@Override
	public int getRoundRank()
	{	AiDataZone zone = getTile().getZone();
		int result = zone.getRoundRank(this);
		return result;
	}
	
	/**
	 * Renvoie le classement de ce joueur, pour la rencontre en cours.
	 * Ce classement n'�volue pas pendant la manche actuellement jou�e.
	 * 
	 * @return	le classement de ce joueur dans la rencontre en cours
	 */
	@Override
	public int getMatchRank()
	{	AiDataZone zone = getTile().getZone();
		int result = zone.getMatchRank(this);
		return result;
	}
	
	/**
	 * Renvoie le classement de ce joueur, dans le classement g�n�ral du jeu (Glicko-2)
	 * Ce classement n'�volue pas pendant la manche actuellement jou�e.
	 * 
	 * @return	le classement g�n�ral (Glicko-2) de ce joueur
	 */
	@Override
	public int getStatsRank()
	{	AiDataZone zone = getTile().getZone();
		int result = zone.getStatsRank(this);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void finish()
	{	super.finish();
	}
}
