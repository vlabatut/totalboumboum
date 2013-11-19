package org.totalboumboum.ai.v201213.adapter.data.internal;

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

import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiStateName;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * Cette classe permet de représenter les sprites manipulés par le jeu,
 * et un nombre restreint de leurs propriétés, rendues ainsi accessible à l'agent.
 * Le paramètre T détermine le type de sprite représenté : bloc, bombe,
 * feu, sol, personnage ou item. 
 * 
 * @author Vincent Labatut
 *
 * @param <T>
 * 		Type de sprite représenté.
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
abstract class AiDataSprite<T extends Sprite> implements AiSprite
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construit une représentation du sprite passé en paramètre.
	 * 
	 * @param tile
	 * 		Représentation de la case contenant le sprite.
	 * @param sprite
	 * 		Sprite à représenter.
	 */
	protected AiDataSprite(AiDataTile tile, T sprite)
	{	// general
		this.tile = tile;
		this.sprite = sprite;
		id = sprite.getId();
		state = new AiDataState(sprite);
		checked = true;
		
		// burning duration
		burningDuration = -1;
		Gesture gesture = sprite.getGesturePack().getGesture(GestureName.BURNING);
		if(gesture!=null)
		{	AnimeDirection anime = gesture.getAnimeDirection(Direction.NONE);
			if(anime!=null)
				burningDuration = anime.getTotalDuration();
				//System.out.println(sprite.getName()+" : "+burningDuration);			
		}
		
		// hiding state
		if(tile==null)
			state.setHiding();
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour cette représentation du sprite.
	 * 
	 * @param tile
	 * 		La nouvelle case contenant cette représentation.
	 * @param elapsedTime 
	 * 		Temps écoulé depuis la dernière mise à jour.
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
	/** Id du sprite dans le jeu */
	protected int id;
	
	@Override
	public int getId()
	{	return id;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Sprite représenté par cette classe */ 
	protected transient T sprite;

	/**
	 * Teste si cette représentation correspond au sprite passé en paramètre.
	 * 
	 * @param sprite
	 * 		Le sprite dont on veut la représentation.
	 * @return	
	 * 		{@code true} si cette représentation correspond à ce sprite.
	 */
	protected boolean isSprite(T sprite)
	{	return this.sprite == sprite;
	}
	
	/**
	 * Renvoie le sprite correspondant à cette représentation.
	 * 
	 * @return
	 * 		Le sprite correspondant à cette représentation.
	 */
	protected T getSprite()
	{	return sprite;	
	}

	/////////////////////////////////////////////////////////////////
	// CHECK			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Marquage du sprite (permet de détecter quels sprites ont disparu lors de la mise à jour */
	protected boolean checked;

	/**
	 * Teste si sprite est marqué ou pas
	 * 
	 * @return	
	 * 		{@code true} ssi ce sprite est marqué.
	 */
	protected boolean isChecked()
	{	return checked;	
	}
	
	/**
	 * Démarque ce sprite (action réalisée avant la mise à jour de la zone).
	 */
	protected void uncheck()
	{	checked = false; 
	}
	
	/////////////////////////////////////////////////////////////////
	// STATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** État dans lequel se trouve ce sprite */
	protected AiDataState state;

	@Override
	public AiDataState getState()
	{	return state;
	}
	
	/** 
	 * Initialise l'état dans lequel se trouve ce sprite.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour. 
	 */
	private void updateState(long elapsedTime)
	{	state.update(elapsedTime);
	}
	
	/**
	 * Indique que le sprite a été éliminé du jeu.
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
	/** Représentation de la case contenant ce sprite */
	protected AiDataTile tile;
	
	@Override
	public AiDataTile getTile()
	{	return tile;
	}
	
	@Override
	public int getRow()
	{	return tile.getRow();	
	}

	@Override
	public int getCol()
	{	return tile.getCol();	
	}
	
	/////////////////////////////////////////////////////////////////
	// BURN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Temps nécessaire au sprite pour brûler (à condition qu'il puisse brûler) */
	protected long burningDuration = 0;
	
	@Override
	public long getBurningDuration()
	{	return burningDuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Vitesse de déplacement courante en pixels/s (est nulle si le sprite ne bouge pas */
	protected double currentSpeed = 0;
	
	@Override
	public double getCurrentSpeed()
	{	return currentSpeed;
	}

	/**
	 * Met à jour la vitesse de ce sprite.
	 */
	protected void updateSpeed()
	{	Sprite sprite = getSprite();
		currentSpeed = sprite.getCurrentSpeed();
		//System.out.println(getSprite().getColor()+": walkingSpeed="+walkingSpeed);
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Abscisse de ce sprite en pixels */
	protected double posX;
	/** Ordonnée de ce sprite en pixels */
	protected double posY;
	/** Altitude de ce sprite en pixels */
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
	 * Initialise les coordonnées (en pixels) de ce sprite. 
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
	
	@Override
	public int compareTo(AiSprite sprite)
	{	int result = id - sprite.getId();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		String row = "?";
		String col = "?";
		if(tile!=null)
		{	row = Integer.toString(tile.getRow());
			col = Integer.toString(tile.getCol());
		}
		result.append(" ("+row+";"+col+")");
		result.append(" ("+posX+";"+posY+";"+posZ+")");
		result.append(" - state: "+state.toString());
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Termine proprement ce sprite 
	 * et libère les ressources qu'il occupait.
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
