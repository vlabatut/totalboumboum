package fr.free.totalboumboum.engine.content.sprite.fire;

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

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class FireEventManager extends EventManager
{
	public FireEventManager(Fire sprite)
	{	super(sprite);
	}
	
	public void initGesture()
	{	gesture = GestureName.BURNING;
		spriteDirection = Direction.NONE;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
	}

/*
 * *****************************************************************
 * ACTION EVENTS
 * *****************************************************************
 */	
	@Override
	public void processEvent(ActionEvent event)
	{	
	}

/*
 * *****************************************************************
 * CONTROL EVENTS
 * *****************************************************************
 */
	@Override
	public void processEvent(ControlEvent event)
	{	
	}

/*
 * *****************************************************************
 * ENGINE EVENTS
 * *****************************************************************
 */
	@Override
	public void processEvent(EngineEvent event)
	{	if(event.getName().equals(EngineEvent.ANIME_OVER))
			engAnimeOver(event);
		else if(event.getName().equals(EngineEvent.TILE_LOWENTER))
			tileEnter(event);
		else if(event.getName().equals(EngineEvent.TOUCH_GROUND))
			tileEnter(event);
	}	

	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureName.BURNING))
		{	gesture = GestureName.ENDED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			sprite.endSprite();
		}
	}

	private void tileEnter(EngineEvent event)
	{	if(gesture.equals(GestureName.BURNING))
		{	Fire fire = (Fire)sprite;
			// fire enters a new tile
			if(event.getSource()==sprite)
			{	Tile tile = sprite.getTile();
				fire.consumeTile(tile);
			}
			// another sprite enters the fire's tile
			else
			{	Sprite s = event.getSource();
				fire.consumeSprite(s);
			}
		}
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
