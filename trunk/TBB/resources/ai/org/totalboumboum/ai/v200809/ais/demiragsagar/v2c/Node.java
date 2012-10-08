package org.totalboumboum.ai.v200809.ais.demiragsagar.v2c;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

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
	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param courant
	 * @param goal
	 * @param ai
	 * @throws StopRequestException
	 */
	public Node(AiTile courant, AiTile goal, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.tile = courant;
		this.calculeHeuristic(goal);
	}

	/**
	 * 
	 * @param courant
	 * @throws StopRequestException
	 */
	public Node(AiTile courant) throws StopRequestException {
		ai.checkInterruption();
		this.tile = courant;
		this.heuristic = 0;
	}
	/**
	 * 
	 * @param courant
	 * @param heuristic
	 * @throws StopRequestException
	 */
	public Node(AiTile courant,double heuristic) throws StopRequestException {
		ai.checkInterruption();
		this.tile = courant;
		this.heuristic = heuristic;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public double getHeuristic() throws StopRequestException {
		ai.checkInterruption();
		return heuristic;
	}

	/**
	 * 
	 * @param lastTile
	 * @throws StopRequestException
	 */
	public void calculeHeuristic(AiTile lastTile) throws StopRequestException {
		ai.checkInterruption();
		//this.heuristic = (Math.pow(lastTile.getCol() - tile.getCol(), 2) + Math.pow(lastTile.getLine() - tile.getLine(), 2));
		this.heuristic=Math.abs(lastTile.getCol()-tile.getCol())+Math.abs(lastTile.getLine()-tile.getLine());
	}

	/**
	 * 
	 * @param test
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean memeCoordonnees(Node test) throws StopRequestException {
		ai.checkInterruption();
		return this.getTile().getCol() == test.getTile().getCol()
				&& this.getTile().getLine() == test.getTile().getLine();

	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiTile getTile() throws StopRequestException
	{	ai.checkInterruption();
		return this.tile;
	}
}
