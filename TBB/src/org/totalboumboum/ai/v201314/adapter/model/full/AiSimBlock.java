package org.totalboumboum.ai.v201314.adapter.model.full;

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

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiSprite;
import org.totalboumboum.ai.v201314.adapter.data.AiStateName;
import org.totalboumboum.ai.v201314.adapter.data.AiStopType;

/**
 * Simule un bloc du jeu, c'est à dire généralement un mur
 * (pouvant être détruit ou pas). 
 * 
 * @author Vincent Labatut
 */
public final class AiSimBlock extends AiSimSprite implements AiBlock
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée une simulation du bloc passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		Numéro d'identification du bloc.
	 * @param tile
	 * 		Case contenant le bloc.
	 * @param posX
	 * 		Abscisse du bloc.
	 * @param posY
	 * 		Ordonnée du bloc.
	 * @param posZ
	 * 		Hauteur du bloc.
	 * @param state
	 * 		État du bloc.
	 * @param burningDuration
	 * 		Durée de combustion du bloc.
	 * @param currentSpeed
	 * 		Vitesse courante de déplacement du bloc (a priori il est immobile).
	 * @param destructible
	 * 		Sensibilité au feu.
	 * @param stopHeroes
	 * 		Capacité à bloquer les personnages.
	 * @param stopFires
	 * 		Capacité à bloquer le feu.
	 */
	protected AiSimBlock(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed,
			boolean destructible, AiStopType stopHeroes, AiStopType stopFires)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		
		this.destructible = destructible;
		this.stopHeroes = stopHeroes;
		this.stopFires = stopFires;
	}	

	/**
	 * Crée une simulation du bloc passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case contenant le sprite.
	 * @param block
	 * 		Sprite à simuler.
	 */
	protected AiSimBlock(AiSimTile tile, AiBlock block)
	{	super(tile,block);
	
		this.destructible = block.isDestructible();
		this.stopHeroes = block.hasStopHeroes();
		this.stopFires = block.hasStopFires();
		
// NOTE workaround pour que les agents ne croient pas que les blocs de mort subite sont sans danger
if(state.getName()==null || state.getName()==AiStateName.FLYING)
{	this.destructible = false;
	this.stopHeroes = AiStopType.STRONG_STOP;
	this.stopFires = AiStopType.STRONG_STOP;
}
	}	
	
	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si ce bloc peut être détruit par une bombe */
	private boolean destructible;

	@Override
	public boolean isDestructible()
	{	return destructible;		
	}
	
	/**
	 * Modifie la destructibilité de ce mur.
	 * 
	 * @param destructible
	 * 		Nouvelle valeur de l'indicateur de destructibilité du mur.
	 */
	public void setDestructible(boolean destructible)
	{	this.destructible = destructible;
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si ce bloc laisse passer les joueurs */
	private AiStopType stopHeroes;
	/** Indique si ce bloc laisse passer le feu */
	private AiStopType stopFires;
	
	@Override
	public AiStopType hasStopHeroes()
	{	return stopHeroes;
	}
	
	/**
	 * Modifie la façon dont ce bloc interagit
	 * avec les joueurs en termes de collisions.
	 * 
	 * @param stopHeroes
	 * 		Comportement du bloc vis à vis des collisions avec les joueurs.
	 */
	public void setStopHeroes(AiStopType stopHeroes)
	{	this.stopHeroes = stopHeroes;
	}
	
	@Override
	public AiStopType hasStopFires()
	{	return stopFires;
	}

	/**
	 * Modifie la façon dont ce bloc interagit
	 * avec les explosions en termes de collisions.
	 * 
	 * @param stopFires
	 * 		Comportement du bloc vis à vis des collisions avec les explosions.
	 */
	public void setStopFires(AiStopType stopFires)
	{	this.stopFires = stopFires;
	}
	
	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	// par défaut, on bloque
		boolean result = false;
		
		// si le sprite considéré est un personnage
		if(sprite instanceof AiHero)
		{	AiHero hero = (AiHero) sprite;
			if(hero.getTile().equals(getTile())) //simplification
				result = true;
			else if(stopHeroes==AiStopType.NO_STOP)
				result = true;
			else if(stopHeroes==AiStopType.WEAK_STOP)
				result = hero.hasThroughBlocks();
			else if(stopHeroes==AiStopType.STRONG_STOP)
				result = false;
		}
		
		// si le sprite considéré est un feu
		else if(sprite instanceof AiFire)
		{	AiFire fire = (AiFire) sprite;
			if(stopFires==AiStopType.NO_STOP)
				result = true;
			else if(stopFires==AiStopType.WEAK_STOP)
				result = fire.hasThroughBlocks();
			else if(stopFires==AiStopType.STRONG_STOP)
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
	protected void finish()
	{	super.finish();
	}
}
