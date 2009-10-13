package fr.free.totalboumboum.engine.content.manager.event;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import java.util.List;

import fr.free.totalboumboum.configuration.RoundVariables;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public abstract class EventManager
{		
	public EventManager(Sprite sprite)
	{	this.sprite = sprite;
		gesture = GestureName.NONE;
		spriteDirection = Direction.NONE;
	}	

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** managed sprite  */
	protected Sprite sprite;
	
	public Sprite getSprite()
	{	return sprite;
	}
	
	public void setSprite(Sprite sprite)
	{	this.sprite = sprite;
	}

	/////////////////////////////////////////////////////////////////
	// GESTURE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** current gesture */
	protected GestureName gesture;
	
	public void initGesture()
	{	gesture = GestureName.NONE;
		spriteDirection = Direction.NONE;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
	}
	
	public GestureName getGesture()
	{	return gesture;
	}
	
	public void setGesture(GestureName gesture)
	{	this.gesture = gesture;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** current direction the sprite is facing */
	protected Direction spriteDirection;
	
	public Direction getSpriteDirection()
	{	return spriteDirection;
	}
	
	public void setSpriteDirection(Direction spriteDirection)
	{	this.spriteDirection = spriteDirection;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS PROCESSING	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract void processEvent(ActionEvent event);
	public abstract void processEvent(ControlEvent event);
	public abstract void processEvent(EngineEvent event);
		
	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	protected void endSprite()
	{	gesture = GestureName.ENDED;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		sprite.changeTile(null);
		RoundVariables.level.removeSprite(sprite);
	}
	
	protected Tile randomlyFindTile()
	{	Tile result = null;
		List<Tile> tileList = RoundVariables.level.getTileList();
		while(result==null && tileList.size()>0)
		{	int index = (int)(Math.random()*tileList.size());
			Tile tile = tileList.get(index);
			sprite.changeTile(tile);
			SpecificAction action = new SpecificAppear(sprite);
			ActionAbility actionAbility = sprite.modulateAction(action);
			// can appear >> select this tile
			if(actionAbility.isActive())
				result = tile;
			// cannot appear >> remove the tile from the list and re-draw
			else
				tileList.remove(index);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// misc
			spriteDirection = null;
			sprite = null;
		}
	}
}
