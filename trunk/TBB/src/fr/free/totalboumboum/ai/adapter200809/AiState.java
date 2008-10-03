package fr.free.totalboumboum.ai.adapter200809;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class AiState 
{
	public AiState(Sprite sprite)
	{	// direction
		this.direction = sprite.getActualDirection();
		// name
		String gesture = sprite.getCurrentGesture();
		name = AiStateName.makeNameFromGesture(gesture);		
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associé à l'action */
	private AiStateName name;
	
	/**
	 * renvoie le nom associé à l'action
	 */
	public AiStateName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction associée à l'action (peut être NONE, c'est à dire : l'action n'est pas orientée) */
	private Direction direction;
	
	/**
	 * renvoie la direction associée à l'action,
	 * qui peut être NONE, c'est à dire : l'action n'est pas orientée
	 */
	public Direction getDirection()
	{	return direction;
	}
}
