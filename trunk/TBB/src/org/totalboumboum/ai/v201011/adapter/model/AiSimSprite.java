package org.totalboumboum.ai.v201011.adapter.model;

import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;

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

/**
 * cette classe permet de simuler les sprites du jeu,
 * et un nombre restreint de leurs propriétés.
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AiSimSprite
{	
	/**
	 * construit une simulation d'un sprite
	 * 
	 * @param tile	simulation de la case contenant le sprite
	 */
	public AiSimSprite(AiSimTile tile)
	{	this.tile = tile;
		state = new AiSimState();
	}

	/**
	 * construit une simulation du sprite passé en paramètre
	 * 
	 * @param tile	simulation de la case contenant le sprite
	 * @param sprite	sprite à simuler
	 */
	public AiSimSprite(AiSimTile tile, AiSprite<?> sprite)
	{	this.tile = tile;
		state = new AiSimState(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** état dans lequel se trouve ce sprite */
	private AiSimState state;

	/** 
	 * renvoie l'état dans lequel se trouve ce sprite
	 * (ie: quelle action il est en train d'effectuer ou de subir)
	 * 
	 * @return	l'état du sprite
	 */
	public AiSimState getState()
	{	return state;
	}
	
	/**
	 * renvoie vrai si ce sprite a été éliminé du jeu
	 * @return	vrai si le sprite n'est plus en jeu
	 */
	public boolean hasEnded()
	{	return state.getName()==AiStateName.ENDED;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** simulation de la case contenant ce sprite */
	private AiSimTile tile;
	
	/** 
	 * renvoie la simulation de la case contenant ce sprite 
	 */
	public AiSimTile getTile()
	{	return tile;
	}
	
	/** 
	 * renvoie le numéro de la ligne contenant ce sprite 
	 * 
	 * @return	le numéro de la ligne du sprite
	 */
	public int getLine()
	{	return tile.getLine();	
	}
	/** 
	 * renvoie le numéro de la colonne contenant ce sprite
	 * 
	 * @return	le numéro de la colonne du sprite
	 */
	public int getCol()
	{	return tile.getCol();	
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiSimSprite)
		{	
//			AiSprite<?> s = (AiSprite<?>)o;	
//			result = sprite==s.sprite;
			result = this==o;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(" ("+getTile().getLine()+";"+getTile().getCol()+")");
		result.append(" - state: "+state.toString());
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Teste si le sprite passé en paramètre est capable de traverser
	 * la case de ce sprite
	 * 
	 *  @param sprite	le sprite à tester
	 *  @return	vrai si ce sprite le laisser passer par sa case 
	 */
	public abstract boolean isCrossableBy(AiSimSprite sprite);
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Complète la copie de sprite passée en paramètre 
	 * avec les informations locales
	 * 
	 *  @param clone	la copie de sprite en cours de construction, à compléter
	 *  @param tile		la case occupée par la copie de sprite
	 */
	public void initCopy(AiSimSprite clone, AiSimTile tile)
	{	// tile
		clone.tile = tile;
		
		// state
		clone.state = state.copy();
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement ce sprite et libère les ressources qu'il occupait
	 */
	void finish()
	{	// state
		state.finish();
		state = null;
		
		// misc
		tile = null;
		
	}
}
