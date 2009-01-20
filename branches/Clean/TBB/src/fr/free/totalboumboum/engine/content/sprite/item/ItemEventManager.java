package fr.free.totalboumboum.engine.content.sprite.item;

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

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.manager.EventManager;

public class ItemEventManager extends EventManager
{	
	public ItemEventManager(Item sprite)
	{	super(sprite);
	}
	
	public void initGesture()
	{	gesture = GestureConstants.STANDING;
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
	{	if(event.getAction().getName().equals(AbstractAction.CONSUME))
			actionConsume(event);
		else if(event.getAction().getName().equals(AbstractAction.GATHER))
			actionGather(event); 
	}

	private void actionConsume(ActionEvent event)
	{	if(gesture.equals(GestureConstants.STANDING))
		{	gesture = GestureConstants.BURNING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}
		
	private void actionGather(ActionEvent event)
	{	//
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
	}	

	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureConstants.BURNING))
		{	gesture = GestureConstants.ENDED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			sprite.endSprite();
		}
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
