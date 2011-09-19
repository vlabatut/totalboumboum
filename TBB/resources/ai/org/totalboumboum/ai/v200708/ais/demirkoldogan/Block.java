package org.totalboumboum.ai.v200708.ais.demirkoldogan;

/**
 * une structure qui contient les propriétés nécessaire d'un case de zone.
 * 
 * @author Turkalp Goker Demirkol
 * @author Emre Dogan
 *
 */
public class Block {
	
	private int x; //coordon� horizontale du block
	private int y; //coordon� verticale du block
	private int cost; //cout de ce block
	private int heuristic; //heuristique
	private Block parentBlock; //le block pere de ce block
	
	
	
	//CONSTRUCTOR
	public Block (int x, int y, int cost) 
	{
		this.x = x;
		this.y = y;
		this.cost = cost;
		this.heuristic = -1;  //heuristic inconnu
	}
	//constructor overload
	public Block (int x, int y) 
	{
		this.x = x;
		this.y = y;
		this.cost = -1;       //cost inconnu
		this.heuristic = -1;  //heuristic inconnu
	}
	
	//methode qui renvoie la valeur heuristique
	public int getHeuristic() {
		return heuristic;
	}
	
	//methode qui met en jour la valeur heuristic
	public void setHeuristic(int targetX, int targetY) {
		this.heuristic = Math.abs(this.x - targetX) + Math.abs(this.y - targetY);
	}

	
	// GETTERS & SETTERS
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getCost() {
		return cost;
	}

	public Block getParentBlock() {
		return parentBlock;
	}
	public void setParentBlock(Block parentBlock) {
		this.parentBlock = parentBlock;
	}

	
}
