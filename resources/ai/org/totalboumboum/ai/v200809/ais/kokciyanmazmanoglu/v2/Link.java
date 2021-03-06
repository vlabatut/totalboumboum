package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v2;

import org.totalboumboum.ai.v200809.adapter.AiAction;

/**
*
* @author Nadin Kökciyan
* @author Hikmet Mazmanoğlu
*
*/
@SuppressWarnings("deprecation")
public class Link {

	/** */
	private Node parent;
	/** */
	private Node child;
	/** */
	private AiAction action;
	
	/**
	 * 
	 * @param p
	 * 		Description manquante !
	 * @param c
	 * 		Description manquante !
	 * @param a
	 * 		Description manquante !
	 */
	public Link(Node p ,Node c, AiAction a){
		
		this.parent = p;
		this.child = c;
		this.action = a;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public AiAction getAction() {
		return action;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public Node getChild() {
		return child;
	}
	
	@Override
	public boolean equals(Object object)
	{	boolean result;
		if(object == null)
			result = false;
		else if(!(object instanceof Link))
			result = false;
		else
		{	Link temp = (Link) object;
			result = temp.getParent() == getParent()
				&& temp.getChild() == getChild()
				&& temp.getAction() == getAction();
		}
		return result;
	}
	
}
