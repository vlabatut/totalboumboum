package org.totalboumboum.ai.v201011.adapter.data.internal;

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

import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.hero.Hero;
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
final class AiDataHero extends AiDataSprite<Hero> implements AiHero
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * crée une représentation du joueur passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		case contenant le sprite
	 * @param sprite
	 * 		sprite à représenter
	 */
	protected AiDataHero(AiDataTile tile, Hero sprite)
	{	super(tile,sprite);
		initColor();
		updateBombParam();
		updateSpeed();
		updateCollisions();
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update(AiDataTile tile, long elapsedTime)
	{	super.update(tile,elapsedTime);
		updateBombParam();
		updateSpeed();
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** exemple de bombe que le personnage peut poser */
	private AiDataBomb bombPrototype;
	/** nombre de bombes que le personnage peut poser simultanément (en général) */
	private int bombNumberMax;
	/** nombre de bombes que le personnage a actuellement posées */
	private int bombNumberCurrent;
	
	@Override
	public AiBomb getBombPrototype()
	{	return bombPrototype;
	}
	
	@Override
	public int getBombRange()
	{	return bombPrototype.getRange();
	}
	
	@Override
	public long getBombDuration()
	{	return bombPrototype.getNormalDuration();
	}
	
	@Override
	public long getExplosionDuration()
	{	return bombPrototype.getExplosionDuration();
	}
	
	@Override
	public int getBombNumberMax()
	{	return bombNumberMax;
	}
	
	@Override
	public int getBombNumberCurrent()
	{	return bombNumberCurrent;
	}
	
	/**
	 * met à jour les paramètres décrivant les bombes que ce personnage peut poser
	 */
	private void updateBombParam()
	{	Hero sprite = getSprite();
		
		// prototype bomb
		Bomb bomb = sprite.makeBomb();
		bombPrototype = new AiDataBomb(tile,bomb);
		bombPrototype.tile = null;
		bombPrototype.posX = 0;
		bombPrototype.posY = 0;
		bombPrototype.posZ = 0;
	
		// max number of simultaneous bombs
		StateAbility ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER);
    	bombNumberMax = (int)ab.getStrength();
        ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER_MAX);
		if(ab.isActive())
		{	int limit = (int)ab.getStrength();
			if(bombNumberMax>limit)
				bombNumberMax = limit;
		}

		// number of bombs currently dropped
    	bombNumberCurrent = sprite.getDroppedBombs().size();
    	
    	//System.out.println(getSprite().getColor()+": bombRange="+bombRange+" bombNumber="+bombNumber+" bombCount="+bombCount);    	
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleur du personnage (et de ses bombes) */
	private PredefinedColor color;
	
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
	/** vitesse de déplacement au sol du personnage, exprimée en pixel/seconde */
	private double walkingSpeed;
	
	@Override
	public double getWalkingSpeed()
	{	return walkingSpeed;	
	}
	
	@Override
	protected void updateSpeed()
	{	// current speed
		super.updateSpeed();
		
		// walking speed (in general)
		Sprite sprite = getSprite();
		double speedCoeff = sprite.getGroundSpeedCoeff()[0];
		Gesture walking = getSprite().getGesturePack().getGesture(GestureName.WALKING);
		double basicSpeed = walking.getTrajectoryDirection(Direction.RIGHT).getXInteraction();
		walkingSpeed = speedCoeff*basicSpeed;
if(walkingSpeed==0)
	System.out.print("");
		//System.out.println(getSprite().getColor()+": walkingSpeed="+walkingSpeed);
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage peut traverser les murs */
	private boolean throughBlocks;
	/** indique si le personnage peut traverser les bombes */
	private boolean throughBombs;
	/** indique si le personnage peut traverser le feu (sans brûler) */
	private boolean throughFires;
	
	@Override
	public boolean hasThroughBlocks()
	{	return throughBlocks;	
	}

	@Override
	public boolean hasThroughBombs()
	{	return throughBombs;	
	}

	@Override
	public boolean hasThroughFires()
	{	return throughFires;	
	}

	/**
	 * met à jour les divers pouvoirs du personnage
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
	@Override
	public int getRoundRank()
	{	AiDataZone zone = getTile().getZone();
		int result = zone.getRoundRank(this);
		return result;
	}
	
	@Override
	public int getMatchRank()
	{	AiDataZone zone = getTile().getZone();
		int result = zone.getMatchRank(this);
		return result;
	}
	
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
		result.append(" - bmbCnt.: "+bombNumberCurrent);
		result.append(" - bmbNbr.: "+bombNumberMax);
		result.append(" - bmbRge.: "+getBombRange());
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
