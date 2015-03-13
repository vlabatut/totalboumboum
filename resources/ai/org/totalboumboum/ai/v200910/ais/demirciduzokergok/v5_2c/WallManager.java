package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_2c;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class is for breaking the walls when there is no bonus visible in the
 * game zone.
 * 
 * @author Mustafa Göktuğ Düzok
 * 
 */
@SuppressWarnings("deprecation")
public class WallManager {

	/**
	 * 
	 * @param ai
	 *            Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public WallManager(DemirciDuzokErgok ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;
		zone = ai.getPercepts();
		safeMap = new SafetyMap(zone, ai);

		esc = new CanEscapeManager(ai);

		// initialise destinations
		arrivedB = false;
		possibleDestB = destinationsPossiblesB(zone.getOwnHero().getTile());
		updatePathB();
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
	public Direction direcitionUpdtB() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		// on met d'abord à jour la matrice de cout
		updateCostCalculatorB();
		safeMap = new SafetyMap(zone, ai);
		Direction result = Direction.NONE;
		if (!hasArrivedB()) {
			checkIsOnPathB();

			if (pathB.isEmpty() || !checkPathValidityB()) {
				updatePathB();
			}

			else {

				AiTile tile = null;
				if (pathB.getLength() > 1) {
					tile = pathB.getTile(1);
				} else if (pathB.getLength() > 0)
					tile = pathB.getTile(0);

				if (tile != null)
					result = zone.getDirection(zone.getOwnHero(), tile);

			}
		}
		return result;
	}

	/**
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean canPass() throws StopRequestException {
		ai.checkInterruption();
		int m = 0;
		int stop = 1;
		while (m < pathB.getLength() && pathB.isEmpty() == false && stop == 1) {
			ai.checkInterruption();
			if (safeMap.returnMatrix()[pathB.getTile(m).getLine()][pathB
					.getTile(m).getCol()] != safeMap.SAFE_CASE) {
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
		ai.checkInterruption();
		esc = new CanEscapeManager(ai);
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
	public boolean checkPathValidityB() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		boolean result = true;
		Iterator<AiTile> it = pathB.getTiles().iterator();
		while (it.hasNext() && result) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			AiTile tile = it.next();
			result = tile.isCrossableBy(zone.getOwnHero());
		}
		return result;

	}

	/**
	 * Checks if we are on the path which we decided to make the movement.
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void checkIsOnPathB() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		AiTile currentTile = zone.getOwnHero().getTile();
		while (!pathB.isEmpty() && pathB.getTile(0) != currentTile) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			pathB.removeTile(0);
		}

	}

	/**
	 * Verifies if we arrived the final case or not:
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean hasArrivedB() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		if (arrivedB == false) {
			if (arrivedTileB == null) {
				// System.out.println("ssssss");
				arrivedB = true;
			} else {

				AiTile currentTile = ai.getPercepts().getOwnHero().getTile();
				arrivedB = currentTile == arrivedTileB;
			}
		}

		return arrivedB;
	}

	/**
	 * Updates the cost:
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void updateCostCalculatorB() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		double safetyMatrix_b[][] = safeMap.returnMatrix();
		for (int line = 0; line < zone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int col = 0; col < zone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE

				double costB = safetyMatrix_b[line][col];
				ai.costCalculator.setCost(line, col, Math.abs(costB));

			}
		}
	}

	/**
	 * This method takes the path to make the movement using the astar
	 * algorithme.
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void updatePathB() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		pathB = ai.aStar.processShortestPath(zone.getOwnHero().getTile(),possibleDestB);
		ai.updateAstarQueueSize();
		arrivedTileB = pathB.getLastTile();
	}

	/**
	 * The possible destinations to make the deplacement(Gets the positions of
	 * the safe cases neighbor to the breakable walls)
	 * 
	 * @param tile
	 *            Description manquante !
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public List<AiTile> destinationsPossiblesB(AiTile tile)
			throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		// safe_map=new Safety_Map(zone);
		AiTile tile_dest_b = null;
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiBlock> block_iterator = zone.getBlocks().iterator();
		safeMap = new SafetyMap(zone, ai);
		AiBlock blck;
		while (block_iterator.hasNext() == true) {
			ai.checkInterruption();
			blck = block_iterator.next();
			if (blck.isDestructible()) {
				if (safeMap.returnMatrix()[blck.getLine()][blck.getCol() + 1] == safeMap.SAFE_CASE
						|| safeMap.returnMatrix()[blck.getLine()][blck
								.getCol() + 1] == safeMap.BONUS) {
					tile_dest_b = zone.getTile(blck.getLine(),
							blck.getCol() + 1);
					result.add(tile_dest_b);
				}
				if (safeMap.returnMatrix()[blck.getLine()][blck.getCol() - 1] == safeMap.SAFE_CASE
						|| safeMap.returnMatrix()[blck.getLine()][blck
								.getCol() - 1] == safeMap.BONUS) {
					tile_dest_b = zone.getTile(blck.getLine(),
							blck.getCol() - 1);
					result.add(tile_dest_b);
				}
				if (safeMap.returnMatrix()[blck.getLine() + 1][blck.getCol()] == safeMap.SAFE_CASE
						|| safeMap.returnMatrix()[blck.getLine() + 1][blck
								.getCol()] == safeMap.BONUS) {
					tile_dest_b = zone.getTile(blck.getLine() + 1,
							blck.getCol());
					result.add(tile_dest_b);
				}
				if (safeMap.returnMatrix()[blck.getLine() - 1][blck.getCol()] == safeMap.SAFE_CASE
						|| safeMap.returnMatrix()[blck.getLine() - 1][blck
								.getCol()] == safeMap.BONUS) {
					tile_dest_b = zone.getTile(blck.getLine() - 1,
							blck.getCol());
					result.add(tile_dest_b);
				}

			}
		}

		return result;

	}

	/**
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean isdang() throws StopRequestException {
		ai.checkInterruption();
		int stop = 0;
		boolean x = false;
		int m = 0;
		while (m < pathB.getLength() && stop == 0) {
			ai.checkInterruption();
			if (safeMap.returnMatrix()[pathB.getTile(m).getLine()][pathB
					.getTile(m).getCol()] != safeMap.SAFE_CASE
					|| safeMap.returnMatrix()[pathB.getTile(m).getLine()][pathB
							.getTile(m).getCol()] == safeMap.BONUS) {
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
	private SafetyMap safeMap;
	/** */
	private AiTile arrivedTileB;
	/** */
	private List<AiTile> possibleDestB;
	/** */
	private boolean arrivedB;
	/** */
	private AiPath pathB;
	/** */
	private CanEscapeManager esc;
}
