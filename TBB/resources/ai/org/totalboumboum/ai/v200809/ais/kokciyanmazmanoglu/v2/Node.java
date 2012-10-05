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

	private int line;
	private int col;
	private double cost;
	private boolean visited;
	private int depth;
	
	private Tree tree;
	
	/**
	 * 
	 * @param line
	 * @param col
	 * @param cost
	 * @param t
	 * @param depth
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
	 * @param end
	 * @return
	 * 		?
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
	 * 		?
	 */
	public  double getCost() {
		return cost;
	}

	/**
	 * 
	 * @param cost
	 */
	public  void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public  boolean isVisited() {
		return visited;
	}

	/**
	 * 
	 * @param visited
	 */
	public  void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public  String getName(){
		
		return this.getCol() + " / " + this.getLine();
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public  AiTile convertToTile() throws StopRequestException{
		tree.km.checkInterruption();
		return tree.km.getPercepts().getTile(this.getLine(), this.getCol());
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public  int getLine() {
		return line;
	}

	/**
	 * 
	 * @param line
	 */
	public  void setLine(int line) {
		this.line = line;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public  int getCol() {
		return col;
	}

	/**
	 * 
	 * @param col
	 */
	public  void setCol(int col) {
		this.col = col;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * 
	 * @param depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
}
