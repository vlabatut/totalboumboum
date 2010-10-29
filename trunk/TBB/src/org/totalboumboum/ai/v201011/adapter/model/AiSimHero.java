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
 * simule un personnage du jeu, ie un sprite contr�l� par un joueur
 * humain ou une IA.
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimHero extends AiSimSprite
{
	/**
	 * cr�e une simulation du joueur pass� en param�tre,
	 * avec les propri�t�s pass�es en param�tres.
	 * 
	 * @param tile	case contenant le sprite
	 * @param bombRange	port�es des bombes du personnage
	 * @param bombNumber	nombre de bombes que le personnage peut poser
	 * @param bombCount	nombre de bombes que le personnage a d�j� pos�es
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
	 * cr�e une simulation du joueur pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite � simuler
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
	 * construit une simulation du personnage pass� en param�tre,
	 * (donc une simple copie) et la place dans la case indiqu�e.
	 * 
	 * @param sprite	simulation � copier
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
	public int getBombNumber()
	{	return bombNumber;
	}
	
	/**
	 * renvoie le nombre de bombes pos�es par le personnage � ce moment-l�.
	 * Ce nombre est limit� par la valeur renvoy�e par getBombCount()
	 * 
	 * @return	nombre de bombes pos�es
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
	 * @return un symbole de type PredefinedColor repr�sentant une couleur
	 */
	public PredefinedColor getColor()
	{	return color;	
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
	
	/**
	 * teste si ce personnage est capable de passer
	 * � travers les (certains) murs
	 * 
	 * @return	vrai si le personnage traverse les murs
	 */
	public boolean hasThroughBlocks()
	{	return throughBlocks;	
	}

	/**
	 * teste si ce personnage est capable de passer
	 * � travers les bombes
	 * 
	 * @return	vrai si le personnage traverse les bombes
	 */
	public boolean hasThroughBombs()
	{	return throughBombs;	
	}

	/**
	 * teste si ce personnage est capable de passer
	 * � travers le feu sans br�ler
	 * 
	 * @return	vrai si le personnage r�siste au feu
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
