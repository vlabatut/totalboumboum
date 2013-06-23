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
	 * 		Description manquante !
	 * @param goal
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
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
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Node(AiTile courant) throws StopRequestException {
		ai.checkInterruption();
		this.tile = courant;
		this.heuristic = 0;
	}
	/**
	 * 
	 * @param courant
	 * 		Description manquante !
	 * @param heuristic
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Node(AiTile courant,double heuristic) throws StopRequestException {
		ai.checkInterruption();
		this.tile = courant;
		this.heuristic = heuristic;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public double getHeuristic() throws StopRequestException {
		ai.checkInterruption();
		return heuristic;
	}

	/**
	 * 
	 * @param lastTile
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void calculeHeuristic(AiTile lastTile) throws StopRequestException {
		ai.checkInterruption();
		//this.heuristic = (Math.pow(lastTile.getCol() - tile.getCol(), 2) + Math.pow(lastTile.getLine() - tile.getLine(), 2));
		this.heuristic=Math.abs(lastTile.getCol()-tile.getCol())+Math.abs(lastTile.getLine()-tile.getLine());
	}

	/**
	 * 
	 * @param test
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean memeCoordonnees(Node test) throws StopRequestException {
		ai.checkInterruption();
		return this.getTile().getCol() == test.getTile().getCol()
				&& this.getTile().getLine() == test.getTile().getLine();

	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile getTile() throws StopRequestException
	{	ai.checkInterruption();
		return this.tile;
	}
}
