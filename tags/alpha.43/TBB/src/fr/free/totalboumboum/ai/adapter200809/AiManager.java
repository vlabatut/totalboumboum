package fr.free.totalboumboum.ai.adapter200809;

import java.util.ArrayList;

import fr.free.totalboumboum.ai.AbstractAiManager;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.Player;

public abstract class AiManager extends AbstractAiManager<AiAction>
{
	public AiManager(ArtificialIntelligence ai)
    {	super(ai);
	}

    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void setPercepts()
	{	Player player = getPlayer(); 
		Loop loop = player.getLoop();
		Level level = loop.getLevel();
		AiZone percepts = new AiZone(level,player);
		((ArtificialIntelligence)getAi()).setPercepts(percepts);
	}

    /////////////////////////////////////////////////////////////////
	// REACTION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** simulates control keys */
    private Direction lastMove = Direction.NONE;

	@Override
	public ArrayList<ControlEvent> convertReaction(AiAction value)
	{	ArrayList<ControlEvent> result = new ArrayList<ControlEvent>();
		AiActionName name = value.getName();
		Direction direction = value.getDirection();
		ControlEvent event;
		String code;
		switch(name)
		{	case NONE:
				reactionStop(result);
				break;
			case MOVE:
				if(lastMove!=direction)
					reactionStop(result);
				code = ControlEvent.getCodeFromDirection(direction);
				event = new ControlEvent(code,true);
				result.add(event);
				lastMove = direction;
				break;
			case DROP_BOMB :
			{	reactionStop(result);
				event = new ControlEvent(ControlEvent.DROPBOMB,true);
				result.add(event);
				event = new ControlEvent(ControlEvent.DROPBOMB,false);
			}
			case PUNCH :
			{	reactionStop(result);
				event = new ControlEvent(ControlEvent.PUNCHBOMB,true);
				result.add(event);
				event = new ControlEvent(ControlEvent.PUNCHBOMB,false);
			}
		}
		// 
		return result;
	}
	
	private void reactionStop(ArrayList<ControlEvent> result)
	{	if(lastMove!=Direction.NONE)
		{	String code = ControlEvent.getCodeFromDirection(lastMove);
			ControlEvent event = new ControlEvent(code,false);
			result.add(event);
			lastMove = Direction.NONE;
		}
	}
}
