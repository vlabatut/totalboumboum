package org.totalboumboum.ai.v200708.ais.ciritmutlu.exceptions;

/**
 * Exception levee lorsqu'on tente d'effectuer une operation
 * en utilisant un noeud qui n'appartient pas au graphe considere.
 *
 */
public class AbsentNodeException extends Exception
{	
	/** */
	private static final long serialVersionUID = 1L;
	/** le noeud provoquant l'exeption */
	private Object node;
	
	
	/**
	 * Constructeur. 
	 * @param node	le noeud provoquant l'exeption
	 */
	public AbsentNodeException(Object node)
	{	this.node = node;
	}
	
	/**
	 * Renvoie le noeud qui a provoque l'exception 
	 * @return le noeud qui a provoque l'exception 
	 */
	public Object getNode()
	{	return node;
	}
}
