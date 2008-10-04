package fr.free.totalboumboum.ai.adapter200809.action;

import fr.free.totalboumboum.engine.content.feature.Direction;

public class AiAction
{
	/**
	 * construit une action non-orient�e (DROP_BOMB,NONE,PUNCH)
	 * @param name	le nom de l'action
	 */
	public AiAction(AiActionName name)
	{	this(name,Direction.NONE);
	}
	
	/**
	 * construit une action orient�e (MOVE)
	 * @param name	le nom de l'action
	 * @param direction	la direction de l'action
	 */
	public AiAction(AiActionName name, Direction direction)
	{	// direction
		this.direction = direction;
		// name
		this.name = name;		
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** nom associ� � l'action */
	private AiActionName name;
	
	/**
	 * renvoie le nom associ� � l'action
	 * 
	 * @return	le nom de l'action
	 */
	public AiActionName getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction associ�e � l'action */
	private Direction direction;
	
	/**
	 * renvoie la direction associ�e � l'action
	 * @return	la direction de l'action
	 */
	public Direction getDirection()
	{	return direction;
	}
}
