package fr.free.totalboumboum.engine.content.sprite.fire;

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.manager.EventManager;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class FireEventManager extends EventManager
{
	public FireEventManager(Fire sprite)
	{	super(sprite);
	}
	
	public void initGesture()
	{	gesture = GestureConstants.BURNING;
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
	{	if(gesture.equals(GestureConstants.BURNING))
		{	gesture = GestureConstants.ENDED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			sprite.endSprite();
		}
	}

	private void tileEnter(EngineEvent event)
	{	if(gesture.equals(GestureConstants.BURNING))
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
