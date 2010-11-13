package org.totalboumboum.ai.v201011.adapter.model;

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

import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * simule un personnage du jeu, ie un sprite contrôlé par un joueur
 * humain ou une IA.
 * 
 * @author Vincent Labatut
 *
 */
final class AiSimHero extends AiSimSprite implements AiHero
{
	/**
	 * crée une simulation du joueur passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param tile	case contenant le sprite
	 * @param bombRange	portées des bombes du personnage
	 * @param bombNumber	nombre de bombes que le personnage peut poser
	 * @param bombCount	nombre de bombes que le personnage a déjà posées
	 * @param throughBlocks	indique si le personnage peut traverser les murs
	 * @param throughBombs	indique si le personnage peut traverser les bombes
	 * @param throughFires	indique si le personnage peut traverser le feu
	 * @param color	couleur du personnage
	 * @param walkingSpeed	vitesse du personnage
	 */
	protected AiSimHero(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed,
			AiBomb bombPrototype, int bombNumber, int bombCount,
			boolean throughBlocks, boolean throughBombs, boolean throughFires,
			PredefinedColor color, double walkingSpeed)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		
		// bombs
		this.bombPrototype = bombPrototype;
		this.bombNumber = bombNumber;
		this.bombCount = bombCount;
		
		// collisions
		this.throughBlocks = throughBlocks;
		this.throughBombs = throughBombs;
		this.throughFires = throughFires;
		
		// misc
		this.color = color;
		this.walkingSpeed = walkingSpeed;
	}	

	/**
	 * crée une simulation du joueur passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à simuler
	 */
	protected AiSimHero(AiHero sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		// bombs
		bombPrototype = sprite.getBombPrototype();
		bombNumber = sprite.getBombNumber();
		bombCount = sprite.getBombCount();
		
		// collisions
		throughBlocks = sprite.hasThroughBlocks();
		throughBombs = sprite.hasThroughBombs();
		throughFires = sprite.hasThroughFires();
		
		// misc
		color = sprite.getColor();
		walkingSpeed = sprite.getWalkingSpeed();
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** exemple de bombe que le personnage peut poser */
	private AiBomb bombPrototype;
	/** nombre de bombes que le personnage peut poser simultanément (en général) */
	private int bombNumber;
	/** nombre de bombes que le personnage a actuellement posées */
	private int bombCount;
	
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
	public int getBombNumber()
	{	return bombNumber;
	}
	
	@Override
	public int getBombCount()
	{	return bombCount;
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
	/** vitesse de déplacement au sol du personnage, exprimée en pixel/seconde */
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

	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	boolean result = false;
		if(sprite instanceof AiSimFire)
			result = true;
		else if(sprite instanceof AiSimHero)
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
		result.append(" - bmbCnt.: "+bombCount);
		result.append(" - bmbNbr.: "+bombNumber);
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
