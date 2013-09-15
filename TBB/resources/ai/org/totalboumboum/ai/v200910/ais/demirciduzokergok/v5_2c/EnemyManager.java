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
 * Our attacking strategies is based on 3 strategies:
 * 
 * 1.If we are 3 cases near the enemy(3 cases on line or colon) we drop a
 * bomb.Here we don't control if the bomb is on near the walls or not. The
 * reason for it is that: if we already posed a bomb above and enemy and we
 * approached near it and then posed another one. By this way, we can take the
 * enemy near the danger levels of the bombs which we posed early.
 * 
 * 2.If the path to the enemy is in danger level,we don't make any deplacements
 * and wait the enemy to get closer to us. By this way, when the enemy makes
 * movement to us and when we escape,we can drive the enemy by posing bombs to
 * the cases containing danger levels.
 * 
 * 3. If we are breaking walls and when the path is not safe to approach to the
 * enemy,we complete breaking the which is chosen before and then make the
 * movement to the enemy. The purpose of this action is to make the enemy to
 * make movement to us and then by posing bombs taking it to dangerous cases.
 * 
 * @author Mustafa Göktuğ Düzok
 * 
 */
@SuppressWarnings("deprecation")
public class EnemyManager {

	/**
	 * 
	 * @param ai
	 *            Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public EnemyManager(DemirciDuzokErgok ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		this.ai = ai;
		zone = ai.getPercepts();
		safeMap = new SafetyMap(zone, ai);
		esc = new CanEscapeManager(ai);

		// init destinations:
		arrivedB = false;
		AiHero our_bomberman = zone.getOwnHero();
		possibleDestB = destinationsPossiblesB(our_bomberman.getTile());
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

		updateCostCalculatorB();

		Direction result = Direction.NONE;
		if (hasArrivedB() == false) {
			checkIsOnPathB();

			if (pathB.isEmpty() || !checkPathValidityB()) {
				updatePathB();
			} else {

				AiTile tile = null;

				if (pathB.getLength() > 1) {
					if (safeMap.returnMatrix()[pathB.getTile(1).getLine()][pathB
							.getTile(1).getCol()] <= 0)
						tile = pathB.getTile(1);

				} else if (pathB.getLength() > 0) {
					tile = pathB.getTile(0);

				}

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
	 * Verifies if we arrived the destination case or not.(Here if we are 3
	 * cases near the enemy,we suppose that we arrived because of the attacking
	 * strategie explained above.)
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean hasArrivedB() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		int x, y;
		safeMap = new SafetyMap(zone, ai);
		if (arrivedTileB != null) {
			x = arrivedTileB.getCol() - zone.getOwnHero().getCol();
			y = arrivedTileB.getLine() - zone.getOwnHero().getLine();
			if (x < 0)
				x = -x;
			if (y < 0)
				y = -y;
			if (x == 0) {
				if (y <= 3) {
					arrivedB = true;
				}

			} else if (y == 0) {
				if (x <= 3) {
					arrivedB = true;
				}

			}

		} else if (safeMap.returnMatrix()[zone.getOwnHero().getLine()][zone
				.getOwnHero().getCol()] == safeMap.ENEMIE)
			arrivedB = true;

		return arrivedB;

	}

	/**
	 * This method verifies that if there are any danger levels on the path
	 * which we will make a movement to approch the enemie(like different danger
	 * levels,bombs and fire). If yes, we stop to make the movement.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean canPass() throws StopRequestException {
		ai.checkInterruption();
		int m = 0;
		safeMap = new SafetyMap(zone, ai);
		int stop = 1;
		while (m < pathB.getLength() && pathB.isEmpty() == false && stop == 1) {
			ai.checkInterruption();
			if (safeMap.returnMatrix()[pathB.getTile(m).getLine()][pathB
					.getTile(m).getCol()] != safeMap.SAFE_CASE
					&& safeMap.returnMatrix()[pathB.getTile(m).getLine()][pathB
							.getTile(m).getCol()] != safeMap.ENEMIE) {
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
	 * Returns the lenght of the path.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public int getPathLength() throws StopRequestException {
		ai.checkInterruption();
		return pathB.getLength();

	}

	/**
	 * Method for calculating the cost
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
	 * 
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
	 * This method verifies if we can directly go to the enemie or not:
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean accessiblePath() throws StopRequestException {
		ai.checkInterruption();

		safeMap = new SafetyMap(zone, ai);
		if (safeMap.returnMatrix()[zone.getOwnHero().getLine()][zone
				.getOwnHero().getCol()] == safeMap.ENEMIE
				|| pathB.isEmpty() == false)
			return true;
		else
			return false;
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
		boolean res = false;
		esc = new CanEscapeManager(ai);
		// We choose the numbers here as 3 and 7 because the normal explosion of
		// the bomb is 6 cases of movement and when we are stucked by both
		// sides of walls,we need at least 3 safe cases to escape.
		if (esc.getPathLength() < 3 || esc.getPathLength() > 7) {

			res = false;
		} else
			res = true;
		return res;
	}

	/**
	 * The possible destinations to make the deplacement(Gets the positions of
	 * the enemies and puts them into the list)
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

		AiTile tile_dest_b;
		List<AiTile> result_b = new ArrayList<AiTile>();

		for (int pos_y = 0; pos_y < zone.getHeight(); pos_y++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int pos_x = 0; pos_x < zone.getWidth(); pos_x++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE

				if (safeMap.returnMatrix()[pos_y][pos_x] == safeMap.ENEMIE) {
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
	private CanEscapeManager esc;
	/** */
	private AiTile arrivedTileB;
	/** */
	private List<AiTile> possibleDestB;
	/** */
	private boolean arrivedB;
	/** */
	private AiPath pathB;
}
