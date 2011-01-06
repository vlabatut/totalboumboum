package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v6;

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

/**
 * 
 * @author Can Göçmenoðlu & Irfan Hekimoðlu
 *
 */
public class GocmenogluHekimoglu extends ArtificialIntelligence {
	boolean force_collect;
	boolean debug;
	MatrixCalc matrixCalc;
	Paths paths;
	ActionManager actionMan;

	@Override
	public void init() throws StopRequestException {
		this.checkInterruption();
		this.paths = new Paths(this);
		this.matrixCalc = new MatrixCalc(this, this.paths);
		this.force_collect = false;
		this.debug = true;
		this.actionMan = new ActionManager(this);
	}

	@Override
	public AiAction processAction() throws StopRequestException {
		this.checkInterruption();

		// If our hero is dead, do nothing
		if (this.getPercepts().getOwnHero().hasEnded())
			return new AiAction(AiActionName.NONE);

		// Attack/Collect mode selector
		boolean attack = false;

		// Find accesible tiles
		List<AiTile> tiles = paths.accesibleTiles(this.getPercepts()
				.getOwnHero(), false, true);

		// If enemies are accesible, attack mode
		if (paths.areEnemiesAccesible(tiles) && !force_collect)
			attack = true;

		// Calculate matrix according to our mode selection
		HashMap<AiTile, Double> matrix = matrixCalc.calculateMatrix(tiles,
				attack);

		// show matrix values as text
		if (debug)
			matrixCalc.textTileMap(matrix);

		// Find two highest tile in the matrix, if they are equal, select the
		// closest tile
		AiTile targetTile = paths.kthHighestTile(matrix, 1);
		AiTile targetTile2 = paths.kthHighestTile(matrix, 2);

		if (Math.abs(matrix.get(targetTile) - matrix.get(targetTile2)) < 0.01) {
			if (paths.tileDistNonCyc(this.getPercepts().getOwnHero().getTile(),
					targetTile) > paths.tileDistNonCyc(this.getPercepts()
					.getOwnHero().getTile(), targetTile2)) {
				targetTile = targetTile2;
			}
		}

		// Try to find an action using the action manager
		try {
			AiAction result = actionMan.findAction(targetTile, attack, matrix, force_collect);
			force_collect = actionMan.force_collect_i;
			return result;
		} catch (LimitReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Worst case scenario :)
		AiAction result = new AiAction(AiActionName.NONE);
		return result;
	}

}
