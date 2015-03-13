package org.totalboumboum.ai.v200708.ais.ciritmutlu.exceptions;


/**
 * Exception levee lorsqu'on tente d'appliquer une action a un etat
 * et que cette action n'est pas possible pour cet etat.
 *
 */
public class ImpossibleActionException extends Exception
{	
	/** */
	private static final long serialVersionUID = 1L;
	/** l'etat concerne */
	private Object state;
	/** l'action intentee */
	private Object action;

	/**
	 * Constructeur. 
	 * @param state	l'etat concerne
	 * @param action	l'action intentee
	 */
	public ImpossibleActionException(Object state, Object action)
	{	this.state = state;
		this.action = action;
	}
	
	/**
	 * Renvoie l'action qui a provoque l'exception 
	 * @return l'action qui a provoque l'exception 
	 */
	public Object getAction()
	{	return action;
	}

	/**
	 * Renvoie l'etat qui a provoque l'exception 
	 * @return l'etat qui a provoque l'exception 
	 */
	public Object getStat()
	{	return state;
	}
}
