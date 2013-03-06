package org.totalboumboum.ai.v200708.ais.erengokce;

/**
 * 
 * @author Can Eren
 * @author Mustafa Mert Gökçe
 *
 */
public class Node {

	/** noeud son abscisse,ordonnee, et son noeud pere sil en existe */
	int x;
	/** */
	int y;
	/** */
	int accessCost;
	/** */
	Node father;
	/** est-il visite par l'algorithme */
	boolean marked;
	
	/**
	 * 
	 */
	public Node()
	{
//		this.father.x=-1;
//		this.father.y=-1;
		this.father=null;
	}
	
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @param father
	 * 		Description manquante !
	 */
	public Node(int x,int y,Node father)
	{
		this.marked=false;
		this.father=father;
		this.x=x;
		this.y=y;
		this.accessCost=0;
	}
	
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 */
	public Node(int x,int y)
	{
		this.x=x;
		this.y=y;
		this.father=null;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getX() {
		return x;
	}
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getY() {
		return y;
	}
	/**
	 * 
	 * @param y
	 * 		Description manquante !
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getAccessCost() {
		return accessCost;
	}
	/**
	 * 
	 * @param accessCost
	 * 		Description manquante !
	 */
	public void setAccessCost(int accessCost) {
		this.accessCost = accessCost;
	}
	
	
}
