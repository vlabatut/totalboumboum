package org.totalboumboum.ai.v200708.ais.erengokce;

/**
 * 
 * @author Can Eren
 * @author Mustafa Mert Gökçe
 *
 */
public class Tree {
	/** */
	Node root;
	/** */
	Node fils1;
	/** */
	Node fils2;
	/** */
	Node fils3;
	/** */
	Node fils4;
	/**
	 * 
	 * @param root
	 * 		Description manquante !
	 */
	//un noeud et ses fils potentiels
	public Tree(Node root)
	{
		//si le noeud a un fils dont le grandpere est lui meme on le supprime en donnant
		//les coordonnees 500,500
		this.root=root;
		this.fils1=new Node((root.x)-1,root.y,root);
		if(root.father!=null)
		if(fils1.equals(root.father.father))
			fils1=new Node(500,500,root);
		this.fils2=new Node((root.x)+1,root.y,root);
		if(root.father!=null)
		if(fils2.equals(root.father.father))
			fils2=new Node(500,500,root);
		this.fils3=new Node(root.x,(root.y)-1,root);
		if(root.father!=null)
		if(fils3.equals(root.father.father))
			fils3=new Node(500,500,root);
		this.fils4=new Node(root.x,(root.y)+1,root);
		if(root.father!=null)
		if(fils4.equals(root.father.father))
			fils4=new Node(500,500,root);
	}
	
	/**
	 * 
	 */
	public void constructTree()
	{
		//
	}
	
}
