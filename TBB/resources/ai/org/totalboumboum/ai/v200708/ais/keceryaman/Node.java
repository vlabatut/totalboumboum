package org.totalboumboum.ai.v200708.ais.keceryaman;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Serkan Keçer
 * @author Onur Yaman
 *
 */
public class Node {
	/** position */
	private int x;
	/** */
	private int y;
	
	/** costs */
	private int cost;
	/** */
	private int costPath;
	/** */
	private int costHeuristic;
	
	/**
	 * constants
	 */
	private int VAL_HEURISTIC = 10;
	
	/**
	 * nodes
	 */
	private Node parent;
	/** */
	public List<Node> neighbors;
	
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public int getCost (){
		return cost;
	}
	/**
	 * 
	 */
	public void updateCost (){
		cost = costPath + costHeuristic;
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public int getPathCost (){
		return costPath;
	}
	/**
	 * 
	 * @param value
	 */
	public void setPathCost ( int value ){
		costPath = value;
	}
	/**
	 * 
	 * @param node
	 * @return
	 * 		? 
	 */
	public Node setParent ( Node node ){
		parent = node;
		
		return this;
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public Node getParent (){
		return parent;
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public int[] getPosition (){
		int[] pos = {this.x,this.y};
		return pos;
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public int getX (){
		return x;
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public int getY (){
		return y;
	}
	/**
	 * 
	 */
	public void findNeighbors (){
		// neighbors
		neighbors = new ArrayList<Node>();
		// neighbor values
		int topValue = Map.getValue(x, y) ;
		if (y>0){
			topValue = Map.getValue(x, (y-1));
		}
		int rightValue= Map.getValue(x, y) ;;
		if (x<17){
			rightValue = Map.getValue((x+1), y);
		}
		int bottomValue= Map.getValue(x, y) ;;
		if (y<15){
			bottomValue = Map.getValue(x, (y+1));
		}
		int leftValue= Map.getValue(x, y) ;;
		if (x>0){
			leftValue = Map.getValue((x-1), y);
		}
		// change the number to the custom value
		if ( topValue != 2 ){
			int[] temp = {x,y-1};
			if ( !AStar.isInClosedList(temp[0], temp[1]) )
				neighbors.add( new Node( x , (y-1) , this , this.getCost() + 100*topValue , heuristic( x , (y-1) ) ) );
		}
		if ( rightValue != 2 ){
			int[] temp = {x+1,y};
			if ( !AStar.isInClosedList(temp[0], temp[1]) )
				neighbors.add( new Node( (x+1) , y , this , this.getCost() + 100*rightValue , heuristic( (x+1) , y ) ) );
		}
		if ( bottomValue != 2 ){
			int[] temp = {x,y+1};
			if ( !AStar.isInClosedList(temp[0], temp[1]) )
				neighbors.add( new Node( x , (y+1) , this , this.getCost() + 100*bottomValue , heuristic( x , (y+1) ) ) );
		}
		if ( leftValue != 2 ){
			int[] temp = {x-1,y};
			if ( !AStar.isInClosedList(temp[0], temp[1]) )
				neighbors.add( new Node( (x-1) , y , this , this.getCost() + 100* leftValue , heuristic( (x-1) , y ) ) );
		}
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 * 		?
	 */
	private int heuristic ( int x , int y  ){
		int tx = AStar.target[0];
		int ty = AStar.target[1];
		
		return VAL_HEURISTIC * ( Math.abs( tx - x ) + Math.abs( ty - y ) );
	}
	/**
	 * 
	 * @param node
	 * @return
	 * 		? 
	 */
	public boolean equals ( Node node ){
		if ( x == node.getX() && y == node.getY() ){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 * 		? 
	 */
	public boolean equals ( int x , int y){
		if ( x == this.x && y == this.y ){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param parent
	 * @param costPath
	 * @param costHeuristic
	 */
	public Node( int x , int y , Node parent, int costPath , int costHeuristic ){
		this.x = x;
		this.y = y;
		this.cost = costPath + costHeuristic;
		this.costPath = costPath;
		this.costHeuristic = costHeuristic;
		this.parent = parent;
	}
}
