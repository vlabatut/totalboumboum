package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v2;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
*
* @author Nadin Kökciyan
* @author Hikmet Mazmanoğlu
*
*/
@SuppressWarnings("deprecation")
public class Node {

	/** */
	private int line;
	/** */
	private int col;
	/** */
	private double cost;
	/** */
	private boolean visited;
	/** */
	private int depth;
	
	/** */
	private Tree tree;
	
	/**
	 * 
	 * @param line
	 * 		Description manquante !
	 * @param col
	 * 		Description manquante !
	 * @param cost
	 * 		Description manquante !
	 * @param t
	 * 		Description manquante !
	 * @param depth
	 * 		Description manquante !
	 */
	public Node(int line, int col, double cost, Tree t, int depth) {
		super();
		this.line = line;
		this.col = col;
		this.depth = depth;
		this.cost = cost;
		this.visited = false;
		tree = t;
	}
	
	/**
	 * 
	 * @param start
	 * 		Description manquante !
	 * @param end
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public  double getH(Node start, Node end){
		double result;
        
		double dx1 = this.getLine() - end.getLine();
		double dy1 = this.getCol() - end.getCol();
		double dx2 = start.getLine() - end.getLine();
		double dy2 = start.getCol() - end.getCol();
		double cross = 10*(dx1*dy2-dx2*dy1);
        cross = Math.abs(cross);
        double real1 = (Math.pow(10*(this.getLine() - end.getLine()), 2) + Math.pow(10*(this.getCol() - end.getCol()), 2));
        
        result = 10*(real1 + Math.abs(dx1)+Math.abs(dy1) + cross*0.0002);
        //result = Math.abs(dx1)+Math.abs(dy1);
        return  result;
	}
	
	@Override
	public  boolean equals(Object object) {
		Node node = (Node)object;
		if ((node.getLine() == this.getLine()) && (node.getCol() == this.getCol()))
			return true;
		else
			return false;

	}



	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public  double getCost() {
		return cost;
	}

	/**
	 * 
	 * @param cost
	 * 		Description manquante !
	 */
	public  void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public  boolean isVisited() {
		return visited;
	}

	/**
	 * 
	 * @param visited
	 * 		Description manquante !
	 */
	public  void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public  String getName(){
		
		return this.getCol() + " / " + this.getLine();
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public  AiTile convertToTile() throws StopRequestException{
		tree.km.checkInterruption();
		return tree.km.getPercepts().getTile(this.getLine(), this.getCol());
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public  int getLine() {
		return line;
	}

	/**
	 * 
	 * @param line
	 * 		Description manquante !
	 */
	public  void setLine(int line) {
		this.line = line;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public  int getCol() {
		return col;
	}

	/**
	 * 
	 * @param col
	 * 		Description manquante !
	 */
	public  void setCol(int col) {
		this.col = col;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * 
	 * @param depth
	 * 		Description manquante !
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
}
