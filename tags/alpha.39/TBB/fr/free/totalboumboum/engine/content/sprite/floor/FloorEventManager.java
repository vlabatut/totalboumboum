package fr.free.totalboumboum.engine.content.sprite.floor;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.manager.EventManager;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class FloorEventManager extends EventManager
{
	public FloorEventManager(Sprite sprite)
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
	{	
	}	

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
