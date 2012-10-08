package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2c;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

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
	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public Noeud(ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.tile = null;
		this.iteration = -1;
		visited = false;
	}

	/**
	 * 
	 * @param tile
	 * @param ai
	 * @throws StopRequestException
	 */
	public Noeud(AiTile tile, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.tile = tile;
		this.iteration = -1;
		visited = false;
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiTile getTile() throws StopRequestException {
		ai.checkInterruption();
		return tile;
	}

	/**
	 * 
	 * @param iteration
	 * @throws StopRequestException
	 */
	protected void markVisited(int iteration) throws StopRequestException {
		ai.checkInterruption();
		visited = true;
		this.iteration = iteration;
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean isVisited() throws StopRequestException {
		ai.checkInterruption();
		return visited;
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public int getIteration() throws StopRequestException {
		ai.checkInterruption();
		return iteration;
	}

	public boolean equals(Object object) {
		Noeud noeud=null;
		try {
			noeud = new Noeud(ai);
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		noeud = (Noeud) object;
		try {
			if ((noeud.getTile()) == tile)
				return true;
			else
				return false;
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param goal
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public int getHeuristic(Noeud goal) throws StopRequestException {
		ai.checkInterruption();
		int result = 0;
		result = result + Math.abs(tile.getLine() - goal.getTile().getLine());
		result = result + Math.abs(tile.getCol() - goal.getTile().getCol());
		return result;
	}
}
