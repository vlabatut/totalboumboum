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

import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * simule un personnage du jeu, ie un sprite contrôlé par un joueur
 * humain ou une IA.
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimHero extends AiSimSprite
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
	public AiSimHero(AiSimTile tile, double posX, double posY, double posZ,
			int bombRange, int bombNumber, int bombCount,
			boolean throughBlocks, boolean throughBombs, boolean throughFires,
			PredefinedColor color, double walkingSpeed)
	{	super(tile,posX,posY,posZ);
		
		// bombs
		this.bombRange = bombRange;
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
	AiSimHero(AiHero sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		// bombs
		bombRange = sprite.getBombRange();
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
	
	/**
	 * construit une simulation du personnage passé en paramètre,
	 * (donc une simple copie) et la place dans la case indiquée.
	 * 
	 * @param sprite	simulation à copier
	 * @param tile	simulation de la case devant contenir la copie
	 */
	public AiSimHero(AiSimHero sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		// bombs
		bombRange = sprite.bombRange;
		bombNumber = sprite.bombNumber;
		bombCount = sprite.bombCount;
		
		// collisions
		throughBlocks = sprite.throughBlocks;
		throughBombs = sprite.throughBombs;
		throughFires = sprite.throughFires;
		
		// misc
		color = sprite.color;
		walkingSpeed = sprite.walkingSpeed;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** portée des bombes du personnage */
	private int bombRange;
	/** nombre de bombes que le personnage peut poser simultanément (en général) */
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
	 * Ce nombre correspond à la somme du nombre de bombes actuellement déjà 
	 * posées (getBombCount) plus le nombre de bombes que le joueur peut encore poser. 
	 * 
	 * @return	le nombre de bombes simultanément posables (en général)
	 */
	public int getBombNumber()
	{	return bombNumber;
	}
	
	/**
	 * renvoie le nombre de bombes posées par le personnage à ce moment-là.
	 * Ce nombre est limité par la valeur renvoyée par getBombCount()
	 * 
	 * @return	nombre de bombes posées
	 */
	public int getBombCount()
	{	return bombCount;
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
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** vitesse de déplacement au sol du personnage, exprimée en pixel/seconde */
	private double walkingSpeed;
	
	/**
	 * renvoie la vitesse de déplacement au sol de ce personnage,
	 * exprimée en pixel/seconde. il ne s'agit pas de la vitesse 
	 * de déplacement courante, il s'agit de la vitesse du personnage
	 * quand il marche. Cette vitesse peut être modifiée par certains items.
	 * 
	 * @return	la vitesse de déplacement de ce personnage
	 */
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
	
	/**
	 * teste si ce personnage est capable de passer
	 * à travers les (certains) murs
	 * 
	 * @return	vrai si le personnage traverse les murs
	 */
	public boolean hasThroughBlocks()
	{	return throughBlocks;	
	}

	/**
	 * teste si ce personnage est capable de passer
	 * à travers les bombes
	 * 
	 * @return	vrai si le personnage traverse les bombes
	 */
	public boolean hasThroughBombs()
	{	return throughBombs;	
	}

	/**
	 * teste si ce personnage est capable de passer
	 * à travers le feu sans brûler
	 * 
	 * @return	vrai si le personnage résiste au feu
	 */
	public boolean hasThroughFires()
	{	return throughFires;	
	}

	@Override
	public boolean isCrossableBy(AiSimSprite sprite)
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
	void finish()
	{	super.finish();
	}
}
