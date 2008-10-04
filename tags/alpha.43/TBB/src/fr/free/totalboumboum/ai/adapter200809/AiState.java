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
	/** nom associ� � l'�tat */
	private AiStateName name;
	
	/**
	 * renvoie le nom associ� � l'�tat
	 */
	public AiStateName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction associ�e � l'�tat (peut �tre NONE, c'est � dire : l'�tat n'est pas orient�) */
	private Direction direction;
	
	/**
	 * renvoie la direction associ�e � l'�tat,
	 * qui peut �tre NONE, c'est � dire : l'�tat n'est pas orient�
	 */
	public Direction getDirection()
	{	return direction;
	}
}
