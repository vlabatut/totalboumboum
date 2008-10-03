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
	/** nom associ� � l'action */
	private AiStateName name;
	
	/**
	 * renvoie le nom associ� � l'action
	 */
	public AiStateName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction associ�e � l'action (peut �tre NONE, c'est � dire : l'action n'est pas orient�e) */
	private Direction direction;
	
	/**
	 * renvoie la direction associ�e � l'action,
	 * qui peut �tre NONE, c'est � dire : l'action n'est pas orient�e
	 */
	public Direction getDirection()
	{	return direction;
	}
}
