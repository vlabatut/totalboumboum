package fr.free.totalboumboum.engine.content.manager;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public abstract class EventManager
{	/** managed sprite  */
	protected Sprite sprite;
	/** current gesture */
	protected String gesture;
	/** current direction the sprite is facing */
	protected Direction spriteDirection;
		
	
	public EventManager(Sprite sprite)
	{	this.sprite = sprite;
		configuration = sprite.getConfiguration();
		gesture = GestureConstants.NONE;
		spriteDirection = Direction.NONE;
	}	
/*	
	public void initGesture(String gesture, Direction direction)
	{	this.gesture = gesture;
		spriteDirection = direction;
	}
*/
	public abstract void initGesture();
	
	public Sprite getSprite()
	{	return sprite;
	}
	public void setSprite(Sprite sprite)
	{	this.sprite = sprite;
	}

	public String getGesture()
	{	return gesture;
	}
	public void setGesture(String gesture)
	{	this.gesture = gesture;
	}

	public Direction getSpriteDirection()
	{	return spriteDirection;
	}
	public void setSpriteDirection(Direction spriteDirection)
	{	this.spriteDirection = spriteDirection;
	}
	
    private Configuration configuration;
	public Configuration getConfiguration()
	{	return configuration;	
	}
	
	public abstract void processEvent(ActionEvent event);
	public abstract void processEvent(ControlEvent event);
	public abstract void processEvent(EngineEvent event);
	
	
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
