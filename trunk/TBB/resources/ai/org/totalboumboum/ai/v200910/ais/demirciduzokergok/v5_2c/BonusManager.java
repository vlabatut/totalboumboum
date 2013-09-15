package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_2c;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class is for collecting the bonus when the bonus is visible in the zone
 * of game.
 * 
 * @author Mustafa Göktuğ Düzok
 * 
 */
@SuppressWarnings("deprecation")
public class BonusManager {

	/**
	 * 
	 * @param ai
	 *            Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public BonusManager(DemirciDuzokErgok ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		this.ai = ai;
		zone = ai.getPercepts();
		// safe_map=new Safety_Map(zone);

		// init destinations
		arrivedB = false;
		// safe_map=new Safety_Map(zone);
		AiHero our_bomberman = zone.getOwnHero();
		possibleDestB = destinationsPossiblesB(our_bomberman.getTile());
		updatePathB();
	}
	
	/**
	 * Method for deplacement according to the path chosen.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public Direction directionUpdtB() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		updateCostCalculatorB();

		Direction result = Direction.NONE;
		if (!hasArrivedB()) {
			checkIsOnPathB();

			if (pathB.isEmpty() || !checkPathValidityB()) {
				updatePathB();
			}

			else {

				AiTile tile = null;
				if (pathB.getLength() > 1)
					tile = pathB.getTile(1);

				else if (pathB.getLength() > 0)
					tile = pathB.getTile(0);

				if (tile != null)
					result = zone.getDirection(zone.getOwnHero(), tile);

			}
		}
		return result;

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
	 * Verifies if the bomberman arrived the last case of the path:
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean hasArrivedB() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		if (arrivedB == false) {
			if (arrivedTileB == null)
				arrivedB = true;
			else {
				AiTile cur_Tile = zone.getOwnHero().getTile();
				arrivedB = (cur_Tile == arrivedTileB);
			}

			boolean x = true;
			int m = 0;
			while (m < pathB.getLength() - 1 && pathB.isEmpty() == false) {
				ai.checkInterruption();
				if (safeMap.returnMatrix()[pathB.getTile(m).getLine()][pathB
						.getTile(m).getCol()] != safeMap.SAFE_CASE)
					x = false;

				m++;
			}

			if (x == false)
				arrivedB = true;

		}

		return arrivedB;

	}

	/**
	 * Updates the cost: Description manquante !
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void updateCostCalculatorB() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		safeMap = new SafetyMap(zone, ai);
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

		pathB = ai.aStar.processShortestPath(ai.getPercepts().getOwnHero().getTile(), possibleDestB);
		ai.updateAstarQueueSize();
		arrivedTileB = pathB.getLastTile();

	}

	/**
	 * verifies if the path is accessible or not:
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean accessiblePath() throws StopRequestException {
		ai.checkInterruption();
		if (pathB.isEmpty() == false)
			return true;
		else
			return false;
	}

	/**
	 * The possible destinations to make the deplacement(Gets the positions of
	 * the bonus visible on the zone of the game)
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
		safeMap = new SafetyMap(zone, ai);
		AiTile tile_dest_b;
		List<AiTile> result_b = new ArrayList<AiTile>();

		for (int pos_y = 0; pos_y < zone.getHeight(); pos_y++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int pos_x = 0; pos_x < zone.getWidth(); pos_x++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE

				if (safeMap.returnMatrix()[pos_y][pos_x] == safeMap.BONUS) {
					tile_dest_b = zone.getTile(pos_y, pos_x);
					result_b.add(tile_dest_b);
				}

			}

		}

		return result_b;

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

}
