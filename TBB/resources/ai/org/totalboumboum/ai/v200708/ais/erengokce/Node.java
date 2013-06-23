package org.totalboumboum.ai.v200708.ais.erengokce;

/**
 * 
 * @author Can Eren
 * @author Mustafa Mert Gokce
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
	
	public Node()
	{
//		this.father.x=-1;
//		this.father.y=-1;
		this.father=null;
	}
	
	public Node(int x,int y,Node father)
	{
		this.marked=false;
		this.father=father;
		this.x=x;
		this.y=y;
		this.accessCost=0;
	}
	
	public Node(int x,int y)
	{
		this.x=x;
		this.y=y;
		this.father=null;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getAccessCost() {
		return accessCost;
	}
	public void setAccessCost(int accessCost) {
		this.accessCost = accessCost;
	}
	
	
}
