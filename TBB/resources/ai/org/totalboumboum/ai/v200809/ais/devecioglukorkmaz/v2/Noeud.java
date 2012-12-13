package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2;

import org.totalboumboum.ai.v200809.adapter.AiTile;

/**
 * 
 * @author Eser Devecioğlu
 * @author lev Korkmaz
 *
 */
@SuppressWarnings("deprecation")
public class Noeud {
	/** */
	private AiTile tile;
	/** */
	private boolean visited;
	/** */
	private int iteration;

	/**
	 * 
	 */
	public Noeud() {
		this.tile = null;
		this.iteration = -1;
		visited = false;
	}

	/**
	 * 
	 * @param tile
	 */
	public Noeud(AiTile tile) {
		this.tile = tile;
		this.iteration = -1;
		visited = false;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public AiTile getTile() {
		return tile;
	}

	/**
	 * 
	 * @param iteration
	 */
	protected void markVisited(int iteration) {
		visited = true;
		this.iteration = iteration;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public boolean isVisited() {
		return visited;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public int getIteration() {
		return iteration;
	}

	@Override
	public boolean equals(Object object) {
		Noeud noeud = new Noeud();
		noeud = (Noeud) object;
		if ((noeud.getTile()) == tile)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param goal
	 * @return
	 * 		?
	 */
	public int getHeuristic(Noeud goal) {
		int result = 0;
		result = result + Math.abs(tile.getLine() - goal.getTile().getLine());
		result = result + Math.abs(tile.getCol() - goal.getTile().getCol());
		return result;
	}
}
