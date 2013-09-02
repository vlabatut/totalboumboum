package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v2c;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

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
	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param p
	 * 		Description manquante !
	 * @param c
	 * 		Description manquante !
	 * @param a
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Link(Node p ,Node c, AiAction a, ArtificialIntelligence ai) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
		this.parent = p;
		this.child = c;
		this.action = a;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiAction getAction() throws StopRequestException {
		ai.checkInterruption();
		return action;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Node getParent() throws StopRequestException {
		ai.checkInterruption();
		return parent;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Node getChild() throws StopRequestException {
		ai.checkInterruption();
		return child;
	}
	
	@Override
	public boolean equals(Object object)
	{	boolean result = false;
		if(object == null)
			result = false;
		else if(!(object instanceof Link))
			result = false;
		else
		{	Link temp = (Link) object;
			try {
				result = temp.getParent() == getParent()
					&& temp.getChild() == getChild()
					&& temp.getAction() == getAction();
			} catch (StopRequestException e) {
				// 
				//e.printStackTrace();
			}
		}
		return result;
	}
	
}
