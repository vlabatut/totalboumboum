package org.totalboumboum.ai.v200708.ais.ciritmutlu.exceptions;


/**
 * Exception levee lorsqu'on tente de creer un noeud dans un graphe
 * et que ce noeud est deje present.
 *
 */
public class ExistingNodeException extends Exception
{	
	/** */
	private static final long serialVersionUID = 1L;
	/** le noeud provoquant l'exeption */
	private Object node;

	/**
	 * Constructeur. 
	 * @param node	le noeud provoquant l'exeption
	 */
	public ExistingNodeException(Object node)
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
