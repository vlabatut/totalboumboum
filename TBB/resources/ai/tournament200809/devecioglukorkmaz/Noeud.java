package tournament200809.devecioglukorkmaz;

import fr.free.totalboumboum.ai.adapter200809.AiTile;

public class Noeud {
	private AiTile tile;
	private boolean visited;
	private int iteration;

	public Noeud() {
		this.tile = null;
		this.iteration = -1;
		visited = false;
	}

	public Noeud(AiTile tile) {
		this.tile = tile;
		this.iteration = -1;
		visited = false;
	}

	public AiTile getTile() {
		return tile;
	}

	protected void markVisited(int iteration) {
		visited = true;
		this.iteration = iteration;
	}

	public boolean isVisited() {
		return visited;
	}

	public int getIteration() {
		return iteration;
	}

	public boolean equals(Object object) {
		Noeud noeud = new Noeud();
		noeud = (Noeud) object;
		if ((noeud.getTile()) == tile)
			return true;
		else
			return false;
	}

	public int getHeuristic(Noeud goal) {
		int result = 0;
		result = result + Math.abs(tile.getLine() - goal.getTile().getLine());
		result = result + Math.abs(tile.getCol() - goal.getTile().getCol());
		return result;
	}
}
