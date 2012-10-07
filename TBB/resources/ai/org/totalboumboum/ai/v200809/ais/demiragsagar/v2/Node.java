package org.totalboumboum.ai.v200809.ais.demiragsagar.v2;

import org.totalboumboum.ai.v200809.adapter.AiTile;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
@SuppressWarnings("deprecation")
public class Node {

	/** */
	AiTile tile;
	/** */
	double heuristic;

	/**
	 * 
	 * @param courant
	 * @param goal
	 */
	public Node(AiTile courant, AiTile goal) {
		this.tile = courant;
		this.calculeHeuristic(goal);
	}
	/**
	 * 
	 * @param courant
	 */
	public Node(AiTile courant) {
		this.tile = courant;
		this.heuristic = 0;
	}
	/**
	 * 
	 * @param courant
	 * @param heuristic
	 */
	public Node(AiTile courant,double heuristic) {
		this.tile = courant;
		this.heuristic = heuristic;
	}
	/**
	 * 
	 * @return
	 * 		?
	 */
	public double getHeuristic() {
		return heuristic;
	}
	
	/**
	 * 
	 * @param lastTile
	 */
	public void calculeHeuristic(AiTile lastTile) {
		//this.heuristic = (Math.pow(lastTile.getCol() - tile.getCol(), 2) + Math.pow(lastTile.getLine() - tile.getLine(), 2));
		this.heuristic=Math.abs(lastTile.getCol()-tile.getCol())+Math.abs(lastTile.getLine()-tile.getLine());
	}

	/**
	 * 
	 * @param test
	 * @return
	 * 		?
	 */
	public boolean memeCoordonnees(Node test) {
		return this.getTile().getCol() == test.getTile().getCol()
				&& this.getTile().getLine() == test.getTile().getLine();

	}
	
	/**
	 * 
	 * @return
	 * 		?
	 */
	public AiTile getTile()
	{
		return this.tile;
	}
}
