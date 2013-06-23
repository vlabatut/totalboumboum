package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;

import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * We use this class for breaking the walls near us to access the enemy when the
 * enemies are not accessibles.
 * 
 * 
 * @author Mustafa Göktuğ Düzok
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class Wall_Manager_2 {

	/**
	 * 
	 * @param ai
	 *            Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public Wall_Manager_2(DemirciDuzokErgok ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		// safe_map=new Safety_Map(zone);
		this.ai = ai;
		zone = ai.getPercepts();
		safe_map = new Safety_Map(zone);

		// initialise a star
		double costMatrix[][] = new double[zone.getHeight()][zone.getWidth()];
		costCalculator_b = new MatrixCostCalculator(costMatrix);
		hcalcul_b = new BasicHeuristicCalculator();
		star_b = new Astar(ai, ai.getPercepts().getOwnHero(), costCalculator_b,
				hcalcul_b);

		esc = new Can_escape_Manager(ai);
		arrived_b = false;
		// initialising the destinations
		possibleDest_b = destinations_possibles_b(zone.getOwnHero().getTile());
		updatePath_b();
	}

	/**
	 * This method explains the direction to go if we didn't arrived the final
	 * case: Like in Escape Manger,here we control the security level of the
	 * neighbor cases too.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public Direction direcition_updt_b() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		// update the cost method
		updateCostCalculator_b();
		safe_map = new Safety_Map(zone);
		Direction result = Direction.NONE;
		// if we didnt arrive yet we will decide if we will make deplacement to
		// the next case or not:
		if (!hasArrived_b()) {
			checkIsOnPath_b();

			if (path_b.isEmpty() || !checkPathValidity_b()) {
				updatePath_b();
			}

			else {

				AiTile tile = null;
				if (path_b.getLength() > 1) {
					// if one of the cases on the path for accessing the walls
					// is not secure, we stop to make our movement.
					if (safe_map.returnMatrix()[path_b.getTile(1).getLine()][path_b
							.getTile(1).getCol()] <= 0)
						tile = path_b.getTile(1);
				} else if (path_b.getLength() > 0)
					tile = path_b.getTile(0);

				if (tile != null)
					result = zone.getDirection(zone.getOwnHero(), tile);

			}
		}
		return result;
	}

	/**
	 * 
	 * @return ?
	 */
	public boolean canPass() {

		int m = 0;
		int stop = 1;
		while (m < path_b.getLength() && path_b.isEmpty() == false && stop == 1) {
			if (safe_map.returnMatrix()[path_b.getTile(m).getLine()][path_b
					.getTile(m).getCol()] != safe_map.SAFE_CASE) {
				stop = 0;
			}
			m++;
		}
		if (stop == 0)
			return false;
		else
			return true;

	}

	/**
	 * This method verifies that if we have enough space to escape if we place a
	 * bomb.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean canesc() throws StopRequestException {
		esc = new Can_escape_Manager(ai);
		boolean res = true;
		if (esc.getPathLength() < 3 || esc.getPathLength() > 6) {

			res = false;
		} else {
			res = true;

		}
		return res;
	}

	/**
	 * There may be obstacles like fire,walls or bombs on the path which we want
	 * to make the deplacement. This method verifies it.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean checkPathValidity_b() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		boolean result = true;
		Iterator<AiTile> it = path_b.getTiles().iterator();
		while (it.hasNext() && result) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			AiTile tile = it.next();
			result = tile.isCrossableBy(zone.getOwnHero());
		}
		return result;

	}

	/**
	 * Verifies if we on the path chosen to access the wall.
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void checkIsOnPath_b() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		AiTile currentTile = zone.getOwnHero().getTile();
		while (!path_b.isEmpty() && path_b.getTile(0) != currentTile) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			path_b.removeTile(0);
		}

	}

	/**
	 * Verifies if we arrived or not to the final destination.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean hasArrived_b() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		if (arrived_b == false) {
			if (arrived_tile_b == null) {
				arrived_b = true;
			} else {

				AiTile currentTile = ai.getPercepts().getOwnHero().getTile();
				arrived_b = currentTile == arrived_tile_b;
			}

		}

		return arrived_b;
	}

	/**
	 * Method for updating the cost.
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void updateCostCalculator_b() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		double safetyMatrix_b[][] = safe_map.returnMatrix();
		for (int line = 0; line < zone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int col = 0; col < zone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE

				double cost_b = safetyMatrix_b[line][col];
				costCalculator_b.setCost(line, col, cost_b);

			}
		}

	}

	/**
	 * This method takes the possible destinations and chooses a path to access
	 * next to the walls.
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void updatePath_b() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		path_b = star_b.processShortestPath(zone.getOwnHero().getTile(),
				possibleDest_b);
		arrived_tile_b = path_b.getLastTile();

	}

	/**
	 * This method chooses the possible destinations and places them into the
	 * list. In this method we take the safe cases and the bonuses next to the
	 * breakable walls.
	 * 
	 * @param tile
	 *            Description manquante !
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public List<AiTile> destinations_possibles_b(AiTile tile)
			throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		// safe_map=new Safety_Map(zone);
		AiTile tile_dest_b = null;
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiBlock> block_iterator = zone.getBlocks().iterator();
		safe_map = new Safety_Map(zone);
		AiBlock blck;
		while (block_iterator.hasNext() == true) {
			blck = block_iterator.next();
			if (blck.isDestructible()) {
				if (safe_map.returnMatrix()[blck.getLine()][blck.getCol() + 1] == safe_map.SAFE_CASE
						|| safe_map.returnMatrix()[blck.getLine()][blck
								.getCol() + 1] == safe_map.BONUS) {
					tile_dest_b = zone.getTile(blck.getLine(),
							blck.getCol() + 1);
					if (zone.getOwnHero().getTile() != tile_dest_b)
						result.add(tile_dest_b);
				}
				if (safe_map.returnMatrix()[blck.getLine()][blck.getCol() - 1] == safe_map.SAFE_CASE
						|| safe_map.returnMatrix()[blck.getLine()][blck
								.getCol() - 1] == safe_map.BONUS) {
					tile_dest_b = zone.getTile(blck.getLine(),
							blck.getCol() - 1);
					if (zone.getOwnHero().getTile() != tile_dest_b)
						result.add(tile_dest_b);
				}
				if (safe_map.returnMatrix()[blck.getLine() + 1][blck.getCol()] == safe_map.SAFE_CASE
						|| safe_map.returnMatrix()[blck.getLine() + 1][blck
								.getCol()] == safe_map.BONUS) {
					tile_dest_b = zone.getTile(blck.getLine() + 1,
							blck.getCol());
					if (zone.getOwnHero().getTile() != tile_dest_b)
						result.add(tile_dest_b);
				}
				if (safe_map.returnMatrix()[blck.getLine() - 1][blck.getCol()] == safe_map.SAFE_CASE
						|| safe_map.returnMatrix()[blck.getLine() - 1][blck
								.getCol()] == safe_map.BONUS) {
					tile_dest_b = zone.getTile(blck.getLine() - 1,
							blck.getCol());
					if (zone.getOwnHero().getTile() != tile_dest_b)
						result.add(tile_dest_b);
				}

			}

		}

		return result;

	}

	/**
	 * 
	 * @return Description manquante !
	 */
	public boolean isdang() {
		int stop = 0;
		boolean x = false;
		int m = 0;
		while (m < path_b.getLength() && stop == 0) {
			if (safe_map.returnMatrix()[path_b.getTile(m).getLine()][path_b
					.getTile(m).getCol()] != safe_map.SAFE_CASE
					|| safe_map.returnMatrix()[path_b.getTile(m).getLine()][path_b
							.getTile(m).getCol()] == safe_map.BONUS) {
				x = true;
				stop = 1;
			}
			m++;
		}

		return x;
	}

	/** */
	private DemirciDuzokErgok ai;
	/** */
	private AiZone zone;
	/** */
	private Safety_Map safe_map;
	/** */
	private AiTile arrived_tile_b;
	/** */
	private List<AiTile> possibleDest_b;
	/** */
	private boolean arrived_b;
	/** */
	private AiPath path_b;
	/** */
	private Astar star_b;
	/** */
	private HeuristicCalculator hcalcul_b;
	/** */
	private MatrixCostCalculator costCalculator_b;
	/** */
	private Can_escape_Manager esc;
}
