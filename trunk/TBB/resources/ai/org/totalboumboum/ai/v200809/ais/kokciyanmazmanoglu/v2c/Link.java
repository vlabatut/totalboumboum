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

	private Node parent;
	private Node child;
	private AiAction action;
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param p
	 * @param c
	 * @param a
	 * @param ai
	 * @throws StopRequestException
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
	 * 		?
	 * @throws StopRequestException
	 */
	public AiAction getAction() throws StopRequestException {
		ai.checkInterruption();
		return action;
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public Node getParent() throws StopRequestException {
		ai.checkInterruption();
		return parent;
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public Node getChild() throws StopRequestException {
		ai.checkInterruption();
		return child;
	}
	
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
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
