package org.totalboumboum.ai.v200708.ais.keceryaman;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Serkan Keçer
 * @author Onur Yaman
 *
 */
public class AStar {
	/** Class fields */
	public static BinaryHeapList openList = new BinaryHeapList(); // open list (nodes to be visited)
	/** */
	public static List<Node> closedList = new ArrayList<Node>(); // closed list (visited nodes)
	/** */
	private Node current; // current node
	/** */
	private Node neighbor; // a neighbor node (changes during a loop)
	/** */
	private Node targetNode;
	/** */
//	private Node startNode;
	/** */
	public static int[] target; // target node
	/** */
	private Iterator<Node> nodes;
	/** */
	private List<Node> path = new ArrayList<Node>();
	/** */
	public static boolean[] closedListCheck = new boolean[255];
	/** */
	public static boolean[] openListCheck = new boolean[255];
	/** */
	public static boolean[] openListValues = new boolean[255];
	/** */
	private boolean targetAdded = false;
	
	/**
	 * 
	 * @param nodenode
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public  List<Node> bestPath (Node nodenode){
		// initialization
		openList.clear();
		clearOpenListCheck();
		closedList.clear();
		clearClosedListCheck();
		openList.add(nodenode);
//		int i = 0;
		int[] coordinates = null;
		// main loop
		while ( !targetAdded && openList.length() > 0 /*&& i++ < 10*/ ){
//			i++;
			// get the first item of the open list and remove it
			current = openList.getFirst();
			if(current!=null)
			{	// move to the closed list
				closedList.add(current);
				AStar.addToClosedList(current.getX(),current.getY());
				if ( current.getX() == target[0] && current.getY() == target[1] ){
					closedList.add(current);
					AStar.addToClosedList(current.getX(),current.getY());
					targetNode = current;
					setTargetAdded();
				}else{
					current.findNeighbors();
					// iterator for the neighbors
					nodes = current.neighbors.iterator();
					// for each neighbor of the current node
					while ( nodes.hasNext() ){
						neighbor = nodes.next();
						coordinates = neighbor.getPosition();
						// if the neighbor is already visited, just ignore it
						if ( isInClosedList(coordinates[0],coordinates[1]) ){
							continue;
						}
						// if it's never visited and is not in the open list, add it to the open list
						else if ( !isInOpenList(coordinates[0], coordinates[1]) ){
							openList.add(neighbor);
							AStar.addToOpenList(coordinates[0],coordinates[1]);
							neighbor.setParent(current);
						}
						// if it's never visited; but is in the open list, compare it with the one in the open list
						else{
							openList.compare(neighbor,current);
						}
					}// end: while
				}
			}
		}// end: while
		// save the path
		
		while ( current!=null && current.getParent() != null ){
			path.add(current);
			current = current.getParent();
		}
		return path;
	}
	
	@Override
	public String toString (){
		Node node = targetNode;
		String path = "";
		int[] pos;
		while ( node.getParent()!= null ){
			pos = node.getPosition();
			path += "(" + pos[0] + "," + pos[1] +  ") ";
		}
		
		return path;
	}
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 */
	public static void addToClosedList ( int x , int y ){
		int index = x + width*y;
		if(index >= 0 && index < closedListCheck.length)
			AStar.closedListCheck[ index ] = true;
	}
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 */
	public static void addToOpenList ( int x , int y ){
		int index = x + width*y;
		if(index >= 0 && index < openListCheck.length)
			AStar.openListCheck[ index ] = true;
	}
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 */
	public static void removeFromClosedList ( int x , int y ){
		int index = x + width*y;
		if(index >= 0 && index < closedListCheck.length)
			AStar.closedListCheck[ index ] = false;
	}
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 */
	public static void removeFromOpenList ( int x , int y ){
		int index = x + width*y;
		if(index >= 0 && index < openListCheck.length)
			AStar.openListCheck[ index ] = false;
	}
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean isInClosedList ( int x , int y ){
		int index = x + width*y;
		if(index >= 0 && index < closedListCheck.length)
			return AStar.closedListCheck[ index ];
		else
			return false;
	}
	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean isInClosedList ( Node node ){
		return closedList.contains(node);
	}
	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean isInOpenList ( Node node ){
		return openList.contains(node);
	}
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean isInOpenList ( int x , int y ){
		int index = x + width*y;
		if(index>=0 && index<openListCheck.length)
			return AStar.openListCheck[ index ];
		else
			return false;
	}
	/**
	 * 
	 */
	public void clearOpenListCheck (){
		for ( int i = 0 ; i < 255 ; i++ ){
			openListCheck[i] = false;
		}
	}
	/**
	 * 
	 */
	public void clearClosedListCheck (){
		for ( int i = 0 ; i < 255 ; i++ ){
			closedListCheck[i] = false;
		}
	}
	/**
	 * 
	 */
	public void setTargetAdded (){
		targetAdded = true;
	}
	/**
	 * 
	 */
	public void setTargetNotAdded (){
		targetAdded = false;
	}
	/**
	 * @param startNode 
	 * 		Description manquante !
	 * @param target 
	 * 		Description manquante !
	 */
	public AStar(Node startNode, int[] target){
		int i = 0;
		for ( i = 0 ; i < 255 ; i ++ ){
			AStar.closedListCheck[ i ] = false;
		}
		for ( i = 0 ; i < 255 ; i ++ ){
			AStar.openListCheck[ i ] = false;
		}
//		this.startNode = startNode;
		AStar.target = target;
	}
	
	/** Zone width */
	private static int width;
	
	/**
	 * 
	 * @param height
	 * 		Description manquante !
	 * @param width
	 * 		Description manquante !
	 */
	public static void init(int height, int width)
	{
		AStar.width = width;
	}
}
