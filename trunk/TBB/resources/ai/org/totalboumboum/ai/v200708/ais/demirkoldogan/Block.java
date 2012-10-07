package org.totalboumboum.ai.v200708.ais.demirkoldogan;

/**
 * une structure qui contient les propriétés nécessaire d'un case de zone.
 * 
 * @author Turkalp Göker Demirkol
 * @author Emre Doğan
 *
 */
public class Block {
	
	/** */
	private int x; //coordoné horizontale du block
	/** */
	private int y; //coordoné verticale du block
	/** */
	private int cost; //cout de ce block
	/** */
	private int heuristic; //heuristique
	/** */
	private Block parentBlock; //le block pere de ce block
	
	
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param cost
	 */
	public Block (int x, int y, int cost) 
	{
		this.x = x;
		this.y = y;
		this.cost = cost;
		this.heuristic = -1;  //heuristic inconnu
	}
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Block (int x, int y) 
	{
		this.x = x;
		this.y = y;
		this.cost = -1;       //cost inconnu
		this.heuristic = -1;  //heuristic inconnu
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	//methode qui renvoie la valeur heuristique
	public int getHeuristic() {
		return heuristic;
	}
	
	/**
	 * 
	 * @param targetX
	 * @param targetY
	 */
	//methode qui met en jour la valeur heuristic
	public void setHeuristic(int targetX, int targetY) {
		this.heuristic = Math.abs(this.x - targetX) + Math.abs(this.y - targetY);
	}

	
	/**
	 * 
	 * @return
	 * 		? 
	 */
	// GETTERS & SETTERS
	public int getX() {
		return x;
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
	 * @return
	 * 		? 
	 */
	public int getCost() {
		return cost;
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public Block getParentBlock() {
		return parentBlock;
	}
	/**
	 * 
	 * @param parentBlock
	 */
	public void setParentBlock(Block parentBlock) {
		this.parentBlock = parentBlock;
	}

	
}
