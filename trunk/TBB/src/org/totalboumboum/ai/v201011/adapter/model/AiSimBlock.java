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

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;

/**
 * Simule un bloc du jeu, c'est � dire g�n�ralement un mur
 * (pouvant �tre d�truit ou pas). 
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimBlock extends AiSimSprite
{
	/**
	 * cr�e une simulation du bloc pass� en param�tre,
	 * avec les propri�t�s pass�es en param�tres.
	 * 
	 * @param tile	case contenant le sprite
	 * @param destructible	indique si la case est destructible par le feu
	 * @param stopHeroes	indique si la case constitue un obstacle aux personnages
	 * @param stopFire	indique si la case constitue un obstacle au feu
	 */
	public AiSimBlock(AiSimTile tile, boolean destructible, AiStopType stopHeroes, AiStopType stopFires)
	{	super(tile);
		
		this.destructible = destructible;
		this.stopsHeroes = stopHeroes;
		this.stopsFires = stopFires;
	}	

	/**
	 * cr�e une simulation du bloc pass� en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param sprite	sprite � simuler
	 * @param tile	case contenant le sprite
	 */
	AiSimBlock(AiBlock sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		this.destructible = sprite.isDestructible();
		this.stopsHeroes = sprite.isStopsHeroes();
		this.stopsFires = sprite.isStopsFires();
	}	
	
	/**
	 * construit une simulation du sprite pass� en param�tre,
	 * (donc une simple copie) et la place dans la case indiqu�e.
	 * 
	 * @param sprite	simulation � copier
	 * @param tile	simulation de la case devant contenir la copie
	 */
	public AiSimBlock(AiSimBlock sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		destructible = sprite.destructible;
		stopsHeroes = sprite.stopsHeroes;
		stopsFires = sprite.stopsFires;
	}
	
	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce bloc peut �tre d�truit par une bombe */
	private boolean destructible;

	/**
	 * renvoie vrai si ce bloc peut �tre d�truit par une bombe, et faux sinon
	 * 
	 * @return	l'indicateur de destructibilit� du mur
	 */
	public boolean isDestructible()
	{	return destructible;		
	}
	
	/**
	 * modifie la destructibilit� de ce mur
	 * 
	 * @param destructible	nouvelle valeur de l'indicateur de destructibilit� du mur
	 */
	public void setDestructible(boolean destructible)
	{	this.destructible = destructible;
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce bloc laisse passer les joueurs */
	private AiStopType stopsHeroes;
	/** indique si ce bloc laisse passer le feu */
	private AiStopType stopsFires;
	
	/**
	 * indique si ce bloc arr�te les personnages.
	 * <b>ATTENTION :</b> il est recommand� d'utiliser plutot la m�thode isCrossableBy.
	 * 
	 * @return	une valeur AiStopType indiquant si ce bloc arr�te les personnages
	 */
	public AiStopType isStopsHeroes()
	{	return stopsHeroes;
	}
	
	/**
	 * modifie le fait que ce mur emp�che ou non le d�placement des personnages
	 * 
	 * @param stopsHeroes	valeur indiquant si ce mur arr�te les personnages
	 */
	public void setStopsHeroes(AiStopType stopsHeroes)
	{	this.stopsHeroes = stopsHeroes;
	}
	
	/**
	 * indique si ce bloc arr�te les explosions.
	 * <b>ATTENTION :</b> il est recommand� d'utiliser plutot la m�thode isCrossableBy.
	 * 
	 * @return	une valeur AiStopType indiquant si ce bloc arr�te le feu
	 */
	public AiStopType isStopsFires()
	{	return stopsFires;
	}
	
	/**
	 * modifie le fait que ce mur emp�che ou non le feu de passer
	 * 
	 * @param stopsHeroes	valeur indiquant si ce mur arr�te le feu
	 */
	public void setStopsFires(AiStopType stopsFires)
	{	this.stopsFires = stopsFires;
	}
	
	@Override
	public boolean isCrossableBy(AiSimSprite sprite)
	{	// par d�faut, on bloque
		boolean result = false;
		// si le sprite consid�r� est un personnage
		if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero) sprite;
			if(hero.getTile()==getTile()) //simplification
				result = true;
			else if(stopsHeroes==AiStopType.NO_STOP)
				result = true;
			else if(stopsHeroes==AiStopType.WEAK_STOP)
				result = hero.hasThroughBlocks();
			else if(stopsHeroes==AiStopType.STRONG_STOP)
				result = false;
		}
		// si le sprite consid�r� est un feu
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire) sprite;
			if(stopsFires==AiStopType.NO_STOP)
				result = true;
			else if(stopsFires==AiStopType.WEAK_STOP)
				result = fire.hasThroughBlocks();
			else if(stopsFires==AiStopType.STRONG_STOP)
				result = false;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Block: [");
		result.append(super.toString());
		result.append(" - destr.: "+destructible);
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
