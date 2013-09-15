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
 * This class is about defense strategie. Our defense strategie is based on the
 * time of explosion of the bombs according to the valeu in our safaty map. The
 * big and positive values express the more dangerous cases and the negative
 * values express the secure places. The value 0 expresses the enemies.
 * 
 * @author Osman Demirci
 * @author Mustafa Göktuğ Düzok
 * @author Hatice Esra Ergök
 * 
 */
@SuppressWarnings("deprecation")
public class EscapeManager {

	/**
	 * 
	 * @param ai
	 *            Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public EscapeManager(DemirciDuzokErgok ai) throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		this.ai = ai;
		zone = ai.getPercepts();
		AiHero ourBomberman = ai.getPercepts().getOwnHero();
		// safe_map=new Safety_Map(zone);

		arrived = false;

		possibleDest = destinations_possibles(ourBomberman.getTile());
		path_init();

	}

	/**
	 * /* This method explains the direction to go if we didn't arrived the
	 * final case: Here, there are some different controls according to our
	 * position.
	 * 
	 * 1. If there are safe cases to escape, we calculate the time of explosion
	 * of bombs and decide if we can reach there before the bomb explores. We
	 * make this calculation by getting the time of explosion of bomb from
	 * Safety_Map class and compare it with the time of our bomberman. passing
	 * time from one case to another one.(We compare here the 2 neighbor cases
	 * in the path)
	 * 
	 * 2. If there are no safe cases to escape(If we are stucked in the range of
	 * 2 bombs) we make a movement to the bomb which has more time left to
	 * explore. By this way, we may escape when the first bomb expores.
	 * 
	 * @return ?
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	Direction direcition_updt() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		Direction result = Direction.NONE;
		cost_calculator_updt();
		if (arrive_or_not() == false) {
			check_on_path();

			// We make another choise of path if the path is empty or if there
			// are obstacles on which
			// we can not cross on.
			if (path.isEmpty() || !checkPathValidity())
				path_init();

			AiTile tile = null;
			if (path.getLength() == 2) {
				tile = path.getTile(1);
			} else if (path.getLength() > 2) {
				// Here we make the decision if to pass the next case of the
				// path or not.(It is sometimes much logic
				// to keep our position instead of making movement to the next
				// case(For example the time of the explosion
				// of the bomb is so soon and we may die if make the movement.In
				// this case it is better to keep the position
				// and wait the bomb to explore and then make the deplacement))
				if (100000 / (safeMap.returnMatrix()[path.getTile(1).getLine()][path
						.getTile(1).getCol()]) > 479)
					tile = path.getTile(1);

			}
			// If we are in the last case, we make the movement:
			else if (path.getLength() == 1)
				tile = path.getTile(0);

			if (tile != null)
				result = zone.getDirection(zone.getOwnHero(), tile);

		}

		// No cases left to escape:

		if (path.getLength() == 0) {
			AiTile my_position = zone.getOwnHero().getTile();

			int k = 1;
			int stop = 0;
			// We declare 2 Arraylist:

			// First one keeps danger level of the cases in which we are
			// stucked.
			List<Double> t = new ArrayList<Double>();
			// The second one keeps the coordinates(tiles) between the bombs we
			// are stucked:
			List<AiTile> at = new ArrayList<AiTile>();

			// iteration on right of our position;
			while (stop == 0 && my_position.getCol() + k < zone.getWidth()) {
				ai.checkInterruption();
				if (safeMap.returnMatrix()[my_position.getLine()][my_position
						.getCol() + k] == safeMap.BOMB
						|| safeMap.returnMatrix()[my_position.getLine()][my_position
								.getCol() + k] == safeMap.DEST_WALL
						|| safeMap.returnMatrix()[my_position.getLine()][my_position
								.getCol() + k] == safeMap.INDEST_WALL
						|| safeMap.returnMatrix()[my_position.getLine()][my_position
								.getCol() + k] == safeMap.FIRE)
					stop = 1;
				else {
					double x3 = safeMap.returnMatrix()[my_position.getLine()][my_position
							.getCol() + k];
					t.add(x3);
					at.add(zone.getTile(my_position.getLine(),
							my_position.getCol() + k));

				}
				k++;
			}
			// iteration on the left side of our position:
			k = 1;
			stop = 0;
			while (stop == 0 && my_position.getCol() - k > 0) {
				ai.checkInterruption();
				if (safeMap.returnMatrix()[my_position.getLine()][my_position
						.getCol() - k] == safeMap.BOMB
						|| safeMap.returnMatrix()[my_position.getLine()][my_position
								.getCol() - k] == safeMap.DEST_WALL
						|| safeMap.returnMatrix()[my_position.getLine()][my_position
								.getCol() - k] == safeMap.INDEST_WALL
						|| safeMap.returnMatrix()[my_position.getLine()][my_position
								.getCol() - k] == safeMap.FIRE)
					stop = 1;
				else {
					double x2 = safeMap.returnMatrix()[my_position.getLine()][my_position
							.getCol() - k];
					t.add(x2);
					at.add(zone.getTile(my_position.getLine(),
							my_position.getCol() - k));
				}
				k++;
			}
			// iteration on the down side of our position:
			k = 1;
			stop = 0;
			while (stop == 0 && my_position.getLine() + k < zone.getHeight()) {
				ai.checkInterruption();
				if (safeMap.returnMatrix()[my_position.getLine() + k][my_position
						.getCol()] == safeMap.BOMB
						|| safeMap.returnMatrix()[my_position.getLine() + k][my_position
								.getCol()] == safeMap.DEST_WALL
						|| safeMap.returnMatrix()[my_position.getLine() + k][my_position
								.getCol()] == safeMap.INDEST_WALL
						|| safeMap.returnMatrix()[my_position.getLine() + k][my_position
								.getCol()] == safeMap.FIRE)
					stop = 1;
				else {
					double x = (safeMap.returnMatrix()[my_position.getLine()
							+ k][my_position.getCol()]);
					t.add(x);
					at.add(zone.getTile(my_position.getLine() + k,
							my_position.getCol()));
				}
				k++;
			}
			// iteration on the up side of our position:

			k = 1;
			stop = 0;
			while (stop == 0 && my_position.getLine() - k > 0) {
				ai.checkInterruption();
				if (safeMap.returnMatrix()[my_position.getLine() - k][my_position
						.getCol()] == safeMap.BOMB
						|| safeMap.returnMatrix()[my_position.getLine() - k][my_position
								.getCol()] == safeMap.DEST_WALL
						|| safeMap.returnMatrix()[my_position.getLine() - k][my_position
								.getCol()] == safeMap.INDEST_WALL
						|| safeMap.returnMatrix()[my_position.getLine() - k][my_position
								.getCol()] == safeMap.FIRE)
					stop = 1;
				else {
					double x4 = safeMap.returnMatrix()[my_position.getLine()
							- k][my_position.getCol()];
					t.add(x4);
					at.add(zone.getTile(my_position.getLine() - k,
							my_position.getCol()));
				}
				k++;
			}

			// we add our position to the arraylists too:
			t.add(safeMap.returnMatrix()[my_position.getLine()][my_position
					.getCol()]);
			at.add(zone.getTile(my_position.getLine(), my_position.getCol()));
			int m = 0;
			int temp = 0;

			// Here we choose the case to make the movement:
			// We choose the one which has the lowest danger level.
			if (t != null) {
				double min = t.get(0);
				while (m < t.size()) {
					ai.checkInterruption();
					if (t.get(m) <= min) {
						min = t.get(m);
						temp = m;

					}

					m++;

				}
				AiTile tile_2 = at.get(temp);
				result = zone.getDirection(zone.getOwnHero(), tile_2);

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
	public boolean checkPathValidity() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
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
	public void check_on_path() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		AiTile currentTile = ai.getPercepts().getOwnHero().getTile();
		while (!path.isEmpty() && path.getTile(0) != currentTile) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			path.removeTile(0);
		}

	}

	/**
	 * Method giving the answer that if we arrived the last coordinate of the
	 * path or not.
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public boolean arrive_or_not() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		if (arrived == false) {
			if (arrivedTile == null)
				arrived = true;
			else {
				AiTile cur_Tile = ai.getPercepts().getOwnHero().getTile();
				if (cur_Tile == arrivedTile)
					arrived = true;
				else
					arrived = false;
			}

		}

		return arrived;

	}

	/**
	 * Mehtod for calculating the cost.
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void cost_calculator_updt() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		safeMap = new SafetyMap(zone, ai);
		double safetyMatrix1[][] = safeMap.returnMatrix();
		for (int line = 0; line < zone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int col = 0; col < zone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE

				double cost1 = safetyMatrix1[line][col];
				ai.costCalculator.setCost(line, col, Math.abs(cost1));

			}
		}

	}

	/**
	 * This method chooses the path to make the movement using the astar
	 * algorithme.
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 * 
	 */
	public void path_init() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		path = ai.aStar.processShortestPath(ai.getPercepts().getOwnHero().getTile(), possibleDest);
		ai.updateAstarQueueSize();
		arrivedTile = path.getLastTile();

	}

	/**
	 * This method is for collecting the possible destinations which we can make
	 * the movement.
	 * 
	 * @param tile
	 *            Description manquante !
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public List<AiTile> destinations_possibles(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		safeMap = new SafetyMap(zone, ai);

		AiTile tile_dest;
		List<AiTile> result = new ArrayList<AiTile>();
		for (int pos_y = 0; pos_y < zone.getHeight(); pos_y++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int pos_x = 0; pos_x < zone.getWidth(); pos_x++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE

				// The cases in which the enemie and the bonus existes are safes
				// too(unless they are in the case of danger)
				if (safeMap.returnMatrix()[pos_y][pos_x] == safeMap.SAFE_CASE
						|| safeMap.returnMatrix()[pos_y][pos_x] == safeMap.BONUS
						|| safeMap.returnMatrix()[pos_y][pos_x] == safeMap.SAFE_CASE
						|| safeMap.returnMatrix()[pos_y][pos_x] == safeMap.ENEMIE) {
					tile_dest = zone.getTile(pos_y, pos_x);
					result.add(tile_dest);
				}

			}

		}

		return result;

	}

	/** */
	private DemirciDuzokErgok ai;
	/** */
	private AiZone zone;
	/** */
	private SafetyMap safeMap;
	/** */
	private AiTile arrivedTile;
	/** */
	private List<AiTile> possibleDest;
	/** */
	private boolean arrived;
	/** */
	private AiPath path;
}
