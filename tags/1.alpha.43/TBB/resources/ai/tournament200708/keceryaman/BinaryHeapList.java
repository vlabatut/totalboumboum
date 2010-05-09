package tournament200708.keceryaman;

import java.util.ArrayList;

public class BinaryHeapList {
	private ArrayList<Node> list;
	
	/**
	 * Adding a new element to the list
	 * @param node
	 */
	public void add ( Node node ){
		// firstly, we add the node to the end of the list
		list.add(node);
		AStar.addToOpenList(node.getX(), node.getY());
		// we got to sort the list after adding the new node to the list
		int length = length();
		sort( length );
//		System.out.println( " -> Node (" + node.getX() + "," + node.getY() + ") is added to the open list!" );
//		System.out.println( "   After: " + this.toString() );
	}
	
	/**
	 * Get the first element of the list
	 */
	public Node getFirst (){
		Node firstNode = list.get(1);
		remove();
		AStar.removeFromOpenList(firstNode.getX(), firstNode.getY());
		return firstNode;
	}
	
	/**
	 * Get the length of the list
	 */
	public int length (){
		return (list.size()-1);
	}
	
	public int realLength (){
		return list.size();
	}
	
	/**
	 * Print out the costs of each element in the list
	 */
	public String toString(){
		String strList = "[";
		int length = length();
		
		for ( int i = 1 ; i <= length ; i++ ){
			strList += getValue(i);
			if ( i < length) strList += ",";
		}
		
		return strList += "]";
	}
	
	/**
	 * Clear the heap
	 */
	public void clear(){
		list.clear();
		list.add(null);
	}
	
	public boolean contains(Node node){
		return list.contains(node);
	}
	
	// TODO
	public void compare(Node node,Node parent){
		int length = length();
		int i = 1;
		boolean bool = false;
		while ( i++ < length && !(bool = list.get(i).getX() == node.getX() && list.get(i).getY() == node.getY()) ){}
		
		if ( bool && (list.get(i).getPathCost() > node.getPathCost() ) ){
			node.setParent(parent);
			node.setPathCost(parent.getPathCost()+10*Map.getValue(node.getX(), node.getY()));
			node.updateCost();
			sort( length() );
//			System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCC");
		}
	}
	
	/**
	 * Sort the list (in the manner of a binary heap)
	 * @param pos
	 */
	private void sort ( int pos ){
		// position of the element's parent
		int posParent = (int) pos/2;

		while ( pos > 1 && getValue(posParent) > getValue(pos) ){
			change(pos,posParent);
			pos = (int) pos/2;
			posParent = (int) pos/2;
		}
	}
	
	/**
	 * Reverse-sort the list (in the manner of a binary heap)
	 * @param pos
	 */
	private void reverseSort ( int pos1 ){
		int pos = 1;
		int temp = pos;
		int child1;
		int child2;
		int length = length();
		boolean loop = true;
		
		while ( loop ){
			temp = pos;
			child1 = 2*pos;
			child2 = child1+1;
			// does both children exist
			if ( child2 <= length ){
				if ( getValue(temp) >= getValue(child1) ) pos = child1;
				if ( getValue(pos) >= getValue(child2) ) pos = child2;
			}
			else if ( child1 <= length ){
				if ( getValue(temp) >= getValue(child1) ) pos = child1;
			}
			
			// if parent's value is bigger than one or both of the children, change them
			if ( temp != pos ){
				change(temp,pos);
			}else{
				loop = false;
			}
		}
	}
	/**
	 * Change two nodes
	 * @param pos1
	 * @param pos2
	 */
	private void change ( int pos1 , int pos2 ){
		Node tempNode = list.get(pos1);
		list.set(pos1, list.get(pos2));
		list.set(pos2, tempNode);
	}
	
	/**
	 * Remove the first element from the list
	 */
	private void remove (){
		int length = length();
		// change the last element with the first element
		change(1,length);
		// remove the first element from the list
		list.remove(length);
		// reverse sort the list
		reverseSort(1);
	}
	
	/**
	 * Returns cost value of the node found in the given position of the list
	 * @param pos
	 * @return
	 */
	private int getValue ( int pos ){
		return list.get(pos).getCost();
	}
	
	//
	// constructor
	//
	public BinaryHeapList(){
		list = new ArrayList<Node>();
		list.add(null);
	}
}
