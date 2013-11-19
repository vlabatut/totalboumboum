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

import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * cette classe permet de représenter les sprites manipulés par le jeu,
 * et un nombre restreint de leurs propriétés, rendues ainsi accessible à l'IA.
 * Le paramètre T détermine le type de sprite représenté : bloc, bombe,
 * feu, sol, personnage ou item. 
 *
 * @param <T>
 * 		type de sprite représenté
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
abstract class AiDataSprite<T extends Sprite> implements AiSprite
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * construit une représentation du sprite passé en paramètre
	 * 
	 * @param tile
	 * 		représentation de la case contenant le sprite
	 * @param sprite
	 * 		sprite à représenter
	 */
	protected AiDataSprite(AiDataTile tile, T sprite)
	{	// general
		this.tile = tile;
		this.sprite = sprite;
		id = sprite.getId();
		state = new AiDataState(sprite);
		
		// burning duration
// à tester
		burningDuration = -1;
		Gesture gesture = sprite.getGesturePack().getGesture(GestureName.BURNING);
		if(gesture!=null)
		{	AnimeDirection anime = gesture.getAnimeDirection(Direction.NONE);
			if(anime!=null)
				burningDuration = anime.getTotalDuration();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour cette représentation du sprite
	 * 
	 * @param tile
	 * 		la nouvelle case contenant cette représentation
	 * @param elapsedTime 
	 * 		?	
	 */
	protected void update(AiDataTile tile, long elapsedTime)
	{	this.tile = tile;
		updateLocation();
		updateState(elapsedTime);
		checked = true;
	}

	/////////////////////////////////////////////////////////////////
	// ID				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** id du sprite dans le jeu */
	protected int id;
	
	@Override
	public int getId()
	{	return id;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sprite représenté par cette classe */ 
	protected transient T sprite;

	/**
	 * teste si cette représentation correspond au sprite passé en paramètre
	 * 
	 * @param sprite
	 * 		le sprite dont on veut la représentation
	 * @return	
	 * 		vrai si cette représentation correspond à ce sprite
	 */
	protected boolean isSprite(T sprite)
	{	return this.sprite == sprite;
	}
	
	/**
	 * renvoie le sprite correspondant à cette représentation
	 * 
	 * @return	le sprite correspondant à cette représentation
	 */
	protected T getSprite()
	{	return sprite;	
	}

	/////////////////////////////////////////////////////////////////
	// CHECK			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** marquage du sprite (permet de détecter quels sprites ont disparu lors de la mise à jour */
	protected boolean checked;

	/**
	 * teste si sprite est marqué ou pas
	 * 
	 * @return	
	 * 		vrai si ce sprite est marqué
	 */
	protected boolean isChecked()
	{	return checked;	
	}
	
	/**
	 * démarque ce sprite (action réalisée avant la mise à jour de la zone)
	 */
	protected void uncheck()
	{	checked = false; 
	}
	
	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** état dans lequel se trouve ce sprite */
	protected AiDataState state;

	@Override
	public AiDataState getState()
	{	return state;
	}
	
	/** 
	 * initialise l'état dans lequel se trouve ce sprite
	 * @param elapsedTime 
	 * 		?	
	 */
	private void updateState(long elapsedTime)
	{	state.update(elapsedTime);
	}
	
	/**
	 * indique que le sprite a été éliminé du jeu
	 */
	protected void setEnded()
	{	state.setEnded();		
	}
	
	@Override
	public boolean hasEnded()
	{	return state.getName()==AiStateName.ENDED;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** représentation de la case contenant ce sprite */
	protected AiDataTile tile;
	
	@Override
	public AiDataTile getTile()
	{	return tile;
	}
	
	@Override
	public int getLine()
	{	return tile.getLine();	
	}

	@Override
	public int getCol()
	{	return tile.getCol();	
	}
	
	/////////////////////////////////////////////////////////////////
	// BURN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps nécessaire au sprite pour brûler (à condition qu'il puisse brûler) */
	protected long burningDuration = 0;
	
	@Override
	public long getBurningDuration()
	{	return burningDuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** vitesse de déplacement courante en pixels/s (est nulle si le sprite ne bouge pas */
	protected double currentSpeed = 0;
	
	@Override
	public double getCurrentSpeed()
	{	return currentSpeed;
	}

	/**
	 * met à jour la vitesse de ce sprite
	 */
	protected void updateSpeed()
	{	Sprite sprite = getSprite();
		currentSpeed = sprite.getCurrentSpeed();
		//System.out.println(getSprite().getColor()+": walkingSpeed="+walkingSpeed);
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse de ce sprite en pixels */
	protected double posX;
	/** ordonnée de ce sprite en pixels */
	protected double posY;
	/** altitude de ce sprite en pixels */
	protected double posZ;

	@Override
	public double getPosX()
	{	return posX;
	}
	
	@Override
	public double getPosY()
	{	return posY;
	}
	
	@Override
	public double getPosZ()
	{	return posZ;
	}
	
	/** 
	 * initialise les coordonnées (en pixels) de ce sprite 
	 */
	private void updateLocation()
	{	posX = sprite.getCurrentPosX();
		posY = sprite.getCurrentPosY();
		posZ = sprite.getCurrentPosZ();
		
//if(Double.isNaN(posX))
//	System.out.println("error");
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiSprite)
		{	AiSprite s = (AiSprite)o;	
			result = id==s.getId();
		}
		return result;
	}

	@Override
    public int hashCode()
    {	return id;
    }
	
	/////////////////////////////////////////////////////////////////
	// TEXT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		String line = "x";
		String col = "x";
		if(tile!=null)
		{	line = Integer.toString(tile.getLine());
			col = Integer.toString(tile.getCol());
		}
		result.append(" ("+line+";"+col+")");
		result.append(" ("+posX+";"+posY+";"+posZ+")");
		result.append(" - state: "+state.toString());
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement ce sprite et libère les ressources qu'il occupait
	 */
	protected void finish()
	{	// state
		state.finish();
		state = null;
		// misc
		tile = null;
		sprite = null;
		
	}
}
