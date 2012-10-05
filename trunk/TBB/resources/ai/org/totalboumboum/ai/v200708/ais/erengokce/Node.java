package org.totalboumboum.ai.v200708.ais.erengokce;

/**
 * 
 * @author Can Eren
 * @author Mustafa Mert Gökçe
 *
 */
public class Node {

	//noeud son abscisse,ordonnee, et son noeud pere sil en existe
	int x;
	int y;
	int accessCost;
	Node father;
	//est-il visite par l'algorithme
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
	 * @param y
	 * @param father
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
	 * @param y
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
	 * 		? 
	 */
	public int getX() {
		return x;
	}
	/**
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public int getY() {
		return y;
	}
	/**
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public int getAccessCost() {
		return accessCost;
	}
	/**
	 * 
	 * @param accessCost
	 */
	public void setAccessCost(int accessCost) {
		this.accessCost = accessCost;
	}
	
	
}
