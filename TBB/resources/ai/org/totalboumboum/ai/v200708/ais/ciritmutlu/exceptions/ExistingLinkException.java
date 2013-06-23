package org.totalboumboum.ai.v200708.ais.ciritmutlu.exceptions;

/**
 * Exception levee lorsqu'on tente de creer un lien dans un graphe
 * et que ce lien est deje present.
 *
 */
public class ExistingLinkException extends Exception
{	private static final long serialVersionUID = 1L;
	// le lien provoquant l'exeption
	private Object link;

	/**
	 * Constructeur. 
	 * @param link	le lien provoquant l'exeption
	 */
	public ExistingLinkException(Object link)
	{	this.link = link;	 	
	}
	
	/**
	 * Renvoie le lien qui a provoque l'exception 
	 * @return le lien qui a provoque l'exception 
	 */
	public Object getLink()
	{	return link;
	}
}
