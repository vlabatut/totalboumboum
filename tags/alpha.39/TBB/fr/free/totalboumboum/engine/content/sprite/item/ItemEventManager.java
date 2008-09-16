package fr.free.totalboumboum.engine.content.sprite.item;

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
	{	//NOTE traitement effectué par l'itemMgr du sprite qui ramasse
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
