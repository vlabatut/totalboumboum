package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v2c;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
*
* @author Nadin Kökciyan
* @author Hikmet Mazmanoğlu
*
*/
public class Node {

	private int line;
	private int col;
	private double cost;
	private boolean visited;
	private int depth;
	
	private Tree tree;
	ArtificialIntelligence ai;
	
	public Node(int line, int col, double cost, Tree t, int depth, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.line = line;
		this.col = col;
		this.depth = depth;
		this.cost = cost;
		this.visited = false;
		tree = t;
	}
	
	
	public  double getH(Node start, Node end) throws StopRequestException{
		ai.checkInterruption();
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
		try {
			if ((node.getLine() == this.getLine()) && (node.getCol() == this.getCol()))
				return true;
			else
				return false;
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		return false;
	}




	public  double getCost() throws StopRequestException {
		ai.checkInterruption();
		return cost;
	}


	public  void setCost(int cost) throws StopRequestException {
		ai.checkInterruption();
		this.cost = cost;
	}


	public  boolean isVisited() throws StopRequestException {
		ai.checkInterruption();
		return visited;
	}


	public  void setVisited(boolean visited) throws StopRequestException {
		ai.checkInterruption();
		this.visited = visited;
	}
	
	public  String getName() throws StopRequestException{
		ai.checkInterruption();
		
		return this.getCol() + " / " + this.getLine();
	}
	
	public  AiTile convertToTile() throws StopRequestException{
		ai.checkInterruption();
		tree.km.checkInterruption();
		return tree.km.getPercepts().getTile(this.getLine(), this.getCol());
	}


	public  int getLine() throws StopRequestException {
		ai.checkInterruption();
		return line;
	}


	public  void setLine(int line) throws StopRequestException {
		ai.checkInterruption();
		this.line = line;
	}


	public  int getCol() throws StopRequestException {
		ai.checkInterruption();
		return col;
	}


	public  void setCol(int col) throws StopRequestException {
		ai.checkInterruption();
		this.col = col;
	}


	public int getDepth() throws StopRequestException {
		ai.checkInterruption();
		return depth;
	}


	public void setDepth(int depth) throws StopRequestException {
		ai.checkInterruption();
		this.depth = depth;
	}
}