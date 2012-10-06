package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v6;

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

/**
 * This class has functions related to action finding
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 *
 */
@SuppressWarnings("deprecation")
public class ActionManager {
	GocmenogluHekimoglu ai;
	/** */
	public boolean force_collect_i;
	
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public ActionManager(GocmenogluHekimoglu ai) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
	}
	
	/**
	 * Finds a proper action, using the given matrix, target and mode
	 * @param targetTile
	 * @param attack
	 * @param matrix
	 * @param force_collect 
	 * @return
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	AiAction findAction(AiTile targetTile,boolean attack,HashMap<AiTile,Double> matrix,boolean force_collect) throws StopRequestException, LimitReachedException{
		ai.checkInterruption();
		
		force_collect_i = force_collect;
		// Calculate path
		AiPath path = ai.paths.findPath(targetTile);

		// Our agent
		AiHero agent = ai.getPercepts().getOwnHero();

		// If we are in collect mode and we have bombs in the zone, run away
		// or stop
		if (!attack && agent.getBombNumberCurrent() > 0) {
			// check if we are in a negative tile
			double tileval = matrix.get(agent.getTile());
			if (tileval < 0) {
				if (path.isEmpty() || path.getLength() == 1) {
					return new AiAction(AiActionName.NONE);
				} else {
					return new AiAction(AiActionName.MOVE, ai
							.getPercepts().getDirection(path.getTile(0),
									path.getTile(1)));
				}
			} else {
				return new AiAction(AiActionName.NONE);
			}

		} else {
			// Find the nearest enemy
			int enemdist = 10000;
			AiHero nearestEnemy = null;
			for (AiHero enemy : ai.getPercepts().getHeroes()) {
				ai.checkInterruption();
				if (enemy.equals(agent) || enemy.hasEnded())
					continue;

				int tdist = ai.paths.tileDistNonCyc(enemy.getTile(),
						agent.getTile());
				if (tdist < enemdist) {
					nearestEnemy = enemy;
					enemdist = tdist;
				}
			}
			
			// If nearest enemy can't escape if we drop a bomb, drop a bomb. :)
			if (nearestEnemy != null) {
				List<AiTile> enemEscapeTiles = ai.paths.accesibleTiles(
						nearestEnemy, true, true);
				if (enemEscapeTiles.isEmpty()) {
					List<AiTile> escapeTiles = ai.paths.accesibleTiles(ai
							.getPercepts().getOwnHero(), true, true);
					
					// Do we have any tiles left to escape?
					if (escapeTiles.size() > 0) {

						force_collect_i = true;
						return new AiAction(AiActionName.DROP_BOMB);
					}
				}
			}
			
			// Check if we arrived
			if (path.isEmpty() || path.getLength() == 1) {
				AiAction result = null;
				List<AiTile> escapeTiles = ai.paths.accesibleTiles(ai
						.getPercepts().getOwnHero(), true, true);
				
				// Do we have any tiles left to escape?
				if (escapeTiles.size() > 0) {
					result = new AiAction(AiActionName.DROP_BOMB);
					force_collect_i = false;
				} else {
					result = new AiAction(AiActionName.NONE);
					if (attack) {
						force_collect_i = true;
					}
				}

				return result;
			} else {
				AiAction result = new AiAction(AiActionName.MOVE, ai
						.getPercepts().getDirection(path.getTile(0),
								path.getTile(1)));
				force_collect_i = false;
				return result;
			}
		}
	}
}
