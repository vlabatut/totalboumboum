package fr.free.totalboumboum.ai.adapter200809.state;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class AiState 
{
	private Sprite sprite;
	
	public AiState(Sprite sprite)
	{	this.sprite = sprite;		
	}
	
	void update()
	{	// direction
		this.direction = sprite.getActualDirection();
		// name
		String gesture = sprite.getCurrentGesture();
		name = AiStateName.makeNameFromGesture(gesture);		
	}
	
	void finish()
	{	sprite = null;
		direction = null;
		name = null;		
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associé à l'état */
	private AiStateName name;
	
	/**
	 * renvoie le nom associé à l'état
	 */
	public AiStateName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction associée à l'état (peut être NONE, c'est à dire : l'état n'est pas orienté) */
	private Direction direction;
	
	/**
	 * renvoie la direction associée à l'état,
	 * qui peut être NONE, c'est à dire : l'état n'est pas orienté
	 */
	public Direction getDirection()
	{	return direction;
	}
}
