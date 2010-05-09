package org.totalboumboum.ai.v200708.ais.hancitoprak;

@SuppressWarnings("unchecked")
public class HanciTprkNode implements Comparable {
	/** coordonnee x de HanciTprkNode */
	private int x;
	/** coordonnee y de HanciTprkNode */
	private int y;
	/** The path cost for this HanciTprkNode */
	private int cost;
	/** le parent du HanciTprkNode, how we reached it in the search */
	private HanciTprkNode parent;
	/**  cost heuristique du HanciTprkNode */
	private int heuristic;
	/** The search depth of this HanciTprkNode */
	private int depth;
	@SuppressWarnings("unused")
	private int f=0;
	
	
	/**
	 * Creation d'un nouveau HanciTprkNode
	 * 
	 * @param x The x coordinate of the HanciTprkNode
	 * @param y The y coordinate of the HanciTprkNode
	 */
	public HanciTprkNode(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Set the parent of this HanciTprkNode
	 * 
	 * @param parent The parent HanciTprkNode which lead us to this HanciTprkNode
	 * @return The depth we have no reached in searching
	 */
	public int setParent(HanciTprkNode parent) {
		depth = parent.depth + 1;
		this.parent = parent;
		
		return depth;
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

	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}
	
	

	public int getF() {
		return cost+heuristic;
	}

	public void setF(int f) {
		this.f = f;
	}

	
	
	public HanciTprkNode getParent() {
		return parent;
	}

	public int getDepth() {
		return depth;
	}

	/**
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(Object other) {
		HanciTprkNode o = (HanciTprkNode) other;
		
		float f = heuristic + cost;
		float of = o.heuristic + o.cost;
		
		if (f < of) {
			return -1;
		} else if (f > of) {
			return 1;
		} else {
			return 0;
		}
	}
}
