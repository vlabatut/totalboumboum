package tournament200809.demiragsagar.v2;

import fr.free.totalboumboum.ai.adapter200809.AiTile;

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
		return this.getTile().getCol() == test.getTile().getCol()
				&& this.getTile().getLine() == test.getTile().getLine();

	}
	
	public AiTile getTile()
	{
		return this.tile;
	}
}
