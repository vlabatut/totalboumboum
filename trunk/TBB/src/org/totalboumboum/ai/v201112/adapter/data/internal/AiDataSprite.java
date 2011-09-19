package org.totalboumboum.ai.v201112.adapter.data.internal;

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

import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.ai.v201112.adapter.data.AiStateName;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * cette classe permet de repr�senter les sprites manipul�s par le jeu,
 * et un nombre restreint de leurs propri�t�s, rendues ainsi accessible à l'IA.
 * Le param�tre T d�termine le type de sprite repr�sent� : bloc, bombe,
 * feu, sol, personnage ou item. 
 * 
 * @author Vincent Labatut
 *
 * @param <T>
 * 		type de sprite repr�sent�
 */
abstract class AiDataSprite<T extends Sprite> implements AiSprite
{	
	/**
	 * construit une repr�sentation du sprite pass� en param�tre
	 * 
	 * @param tile
	 * 		repr�sentation de la case contenant le sprite
	 * @param sprite
	 * 		sprite à repr�senter
	 */
	protected AiDataSprite(AiDataTile tile, T sprite)
	{	// general
		this.tile = tile;
		this.sprite = sprite;
		id = sprite.getId();
		state = new AiDataState(sprite);
		
		// burning duration
//TODO à tester
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
	 * met à jour cette repr�sentation du sprite
	 * 
	 * @param tile
	 * 		la nouvelle case contenant cette repr�sentation
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
	/** sprite repr�sent� par cette classe */ 
	protected T sprite;

	/**
	 * teste si cette repr�sentation correspond au sprite pass� en param�tre
	 * 
	 * @param sprite
	 * 		le sprite dont on veut la repr�sentation
	 * @return	
	 * 		vrai si cette repr�sentation correspond à ce sprite
	 */
	protected boolean isSprite(T sprite)
	{	return this.sprite == sprite;
	}
	
	/**
	 * renvoie le sprite correspondant à cette repr�sentation
	 * 
	 * @return	le sprite correspondant à cette repr�sentation
	 */
	protected T getSprite()
	{	return sprite;	
	}

	/////////////////////////////////////////////////////////////////
	// CHECK			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** marquage du sprite (permet de d�tecter quels sprites ont disparu lors de la mise à jour */
	protected boolean checked;

	/**
	 * teste si sprite est marqu� ou pas
	 * 
	 * @return	
	 * 		vrai si ce sprite est marqu�
	 */
	protected boolean isChecked()
	{	return checked;	
	}
	
	/**
	 * d�marque ce sprite (action r�alis�e avant la mise à jour de la zone)
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
	 * initialise l'�tat dans lequel se trouve ce sprite
	 */
	private void updateState(long elapsedTime)
	{	state.update(elapsedTime);
	}
	
	/**
	 * indique que le sprite a �t� �limin� du jeu
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
	/** repr�sentation de la case contenant ce sprite */
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
	/** temps n�cessaire au sprite pour br�ler (� condition qu'il puisse br�ler) */
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
	/** ordonn�e de ce sprite en pixels */
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
	 * termine proprement ce sprite et lib�re les ressources qu'il occupait
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
