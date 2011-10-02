package org.totalboumboum.ai.v200809.ais.demiragsagar.v1;

import org.totalboumboum.ai.v200809.adapter.AiTile;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
@SuppressWarnings("deprecation")
public class Node {

	AiTile tile;
	double heuristic;

	public Node(AiTile courant, AiTile goal) {
		this.tile = courant;
		this.calculeHeuristic(goal);
	}

	public Node(AiTile courant) {
		this.tile = courant;
		this.heuristic = 0;
	}
	public Node(AiTile courant,double heuristic) {
		this.tile = courant;
		this.heuristic = heuristic;
	}

	public double getHeuristic() {
		return heuristic;
	}

	public void calculeHeuristic(AiTile lastTile) {
		//this.heuristic = (Math.pow(lastTile.getCol() - tile.getCol(), 2) + Math.pow(lastTile.getLine() - tile.getLine(), 2));
		this.heuristic=Math.abs(lastTile.getCol()-tile.getCol())+Math.abs(lastTile.getLine()-tile.getLine());
	}

	public boolean memeCoordonnees(Node test) {
		return this.tile.getCol() == test.tile.getCol()
				&& this.tile.getLine() == test.tile.getLine();

	}
}
