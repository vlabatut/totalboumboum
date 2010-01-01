package tournament200809.kokciyanmazmanoglu.v1;

import org.totalboumboum.ai.adapter200809.AiAction;


public class Link {

	private Node parent;
	private Node child;
	private AiAction action;
	
	public Link(Node p ,Node c, AiAction a){
		
		this.parent = p;
		this.child = c;
		this.action = a;
	}


	public AiAction getAction() {
		return action;
	}


	public Node getParent() {
		return parent;
	}


	public Node getChild() {
		return child;
	}
	
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
