package org.totalboumboum.ai.v201011.adapter.model;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * simule un personnage du jeu, ie un sprite contr�l� par un joueur
 * humain ou une IA.
 * 
 * @author Vincent Labatut
 *
 */
final class AiSimHero extends AiSimSprite implements AiHero
{
	/**
	 * cr�e une simulation du personnage pass� en param�tre,
	 * avec les propri�t�s pass�es en param�tres.
	 * 
	 * @param id
	 * 		num�ro d'identification du personnage
	 * @param tile
	 * 		case contenant le personnage
	 * @param posX
	 * 		abscisse du personnage
	 * @param posY
	 * 		ordonn�e du personnage
	 * @param posZ
	 * 		hauteur du personnage
	 * @param state
	 * 		�tat du personnage
	 * @param burningDuration
	 * 		dur�e de combustion du personnage
	 * @param currentSpeed
	 * 		vitesse courante de d�placement du personnage
	 * @param bombPrototype
	 * 		exemple de bombe que le personnage peut poser
	 * @param bombNumber
	 * 		nombre de bombes que le personnage peut poser
	 * @param bombCount
	 * 		nombre de bombes actuellement pos�es
	 * @param throughBlocks
	 * 		capacit� du personnage � traverser les blocs
	 * @param throughBombs
	 * 		capacit� du personnage � traverser les bombes
	 * @param throughFires
	 * 		capacit� du personnage � traverser le feu (sans mourir !)
	 * @param color
	 * 		couleur du personnage
	 * @param walkingSpeed
	 * 		vitesse de d�placement au sol du personnage
	 */
	protected AiSimHero(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed,
			AiBomb bombPrototype, int bombNumber, int bombCount,
			boolean throughBlocks, boolean throughBombs, boolean throughFires,
			PredefinedColor color, double walkingSpeed)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		
		// bombs
		this.bombPrototype = bombPrototype;
		this.bombNumberMax = bombNumber;
		this.bombNumberCurrent = bombCount;
		
		// collisions
		this.throughBlocks = throughBlocks;
		this.throughBombs = throughBombs;
		this.throughFires = throughFires;
		
		// misc
		this.color = color;
		this.walkingSpeed = walkingSpeed;
	}	

	/**
	 * cr�e une simulation du joueur pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	
	 * 		case contenant le sprite
	 * @param hero	
	 * 		sprite � simuler
	 */
	protected AiSimHero(AiHero hero, AiSimTile tile)
	{	super(hero,tile);
		
		// bombs
		bombPrototype = hero.getBombPrototype();
		bombNumberMax = hero.getBombNumberMax();
		bombNumberCurrent = hero.getBombNumberCurrent();
		
		// collisions
		throughBlocks = hero.hasThroughBlocks();
		throughBombs = hero.hasThroughBombs();
		throughFires = hero.hasThroughFires();
		
		// misc
		color = hero.getColor();
		walkingSpeed = hero.getWalkingSpeed();
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** exemple de bombe que le personnage peut poser */
	private AiBomb bombPrototype;
	/** nombre de bombes que le personnage peut poser simultan�ment (en g�n�ral) */
	private int bombNumberMax;
	/** nombre de bombes que le personnage a actuellement pos�es */
	private int bombNumberCurrent;
	
	@Override
	public AiBomb getBombPrototype()
	{	return bombPrototype;
	}
	
	@Override
	public int getBombRange()
	{	return bombPrototype.getRange();
	}
	
	/**
	 * met � jour la port�e des bombes pos�es par ce joueur
	 * 
	 * @param delta
	 * 		la modification � apporter � la port�e des bombes
	 */
	protected void updateBombRange(int delta)
	{	AiSimBomb proto = new AiSimBomb(bombPrototype,null);
		proto.updateRange(delta);
		bombPrototype = proto;
	}

	@Override
	public long getBombDuration()
	{	return bombPrototype.getNormalDuration();
	}

	@Override
	public long getExplosionDuration()
	{	return bombPrototype.getExplosionDuration();
	}
	
	/**
	 * met � jour le nombre de bombes posables simultan�ment par ce joueur
	 * 
	 * @param delta
	 * 		la modification � apporter au nombre de bombes
	 */
	protected void updateBombNumberMax(int delta)
	{	bombNumberMax = bombNumberMax + delta;
	}
	
	@Override
	public int getBombNumberMax()
	{	return bombNumberMax;
	}
	
	/**
	 * met � jour le nombre de bombes actuellement en jeu et appartenant
	 * � ce joueur
	 * 
	 * @param delta
	 * 		la modification � apporter au nombre de bombes
	 */
	protected void updateBombNumberCurrent(int delta)
	{	bombNumberCurrent = bombNumberCurrent + delta;
	}
	
	@Override
	public int getBombNumberCurrent()
	{	return bombNumberCurrent;
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
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** vitesse de d�placement au sol du personnage, exprim�e en pixel/seconde */
	private double walkingSpeed;
	
	@Override
	public double getWalkingSpeed()
	{	return walkingSpeed;	
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

	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	boolean result = false;
		
		// feu : jamais arr�t� par un personnage
		if(sprite instanceof AiSimFire)
			result = true;
		
		// personnages : pareil
		else if(sprite instanceof AiSimHero)
			result = true;
		
		// pour tout le reste, on renverse la relation 
		else
			result = sprite.isCrossableBy(this);
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// RANKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int getRoundRank()
	{	AiSimZone zone = getTile().getZone();
		int result = zone.getRoundRank(this);
		return result;
	}
	
	@Override
	public int getMatchRank()
	{	AiSimZone zone = getTile().getZone();
		int result = zone.getMatchRank(this);
		return result;
	}
	
	@Override
	public int getStatsRank()
	{	AiSimZone zone = getTile().getZone();
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
