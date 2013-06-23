/**
 * cette classe consiste des methodes sur tout les bombes et feux dans la zone.
 */
package org.totalboumboum.ai.v200809.ais.akpolatsener.v2;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Cem Akpolat
 * @author Emre Sener
 *
 */
public class Danger {

	/** la classe principal de notre IA */
	AkpolatSener as;

	/** l'objet pour les operations sur les cases */
	TileControl control;

	/** l'objet de la danger le plus proche */
	Object closestDanger;

	public Danger(AkpolatSener as) throws StopRequestException {
		as.checkInterruption();
		this.as = as;
		control = new TileControl(as);
		findClosest(as.currentTile);
	}

	/**
	 * trouve le danger la plus proche à une case donnée
	 * 
	 * @param tile
	 * @throws StopRequestException
	 */
	void findClosest(AiTile tile) throws StopRequestException {
		as.checkInterruption();

		double distToBomb, distToFire;

		AiBomb bomb = findClosestBomb();
		AiFire fire = findClosestFire();

		if (bomb != null)
			distToBomb = control.getHypotenuseTo(tile, bomb.getTile());
		else
			distToBomb = 100;

		if (fire != null)
			distToFire = control.getHypotenuseTo(tile, fire.getTile());
		else
			distToFire = 100;

		if (distToBomb < distToFire)
			closestDanger = bomb;
		else
			closestDanger = fire;

	}

	/**
	 * retourne le danger la plus proche
	 * 
	 * @return
	 */
	public Object getClosestDanger() {
		return closestDanger;
	}

	/**
	 * retourne la bombe la plus proche à une case donnée
	 * 
	 * @return
	 * @throws StopRequestException
	 */
	AiBomb findClosestBomb() throws StopRequestException {
		as.checkInterruption();

		AiBomb bomb, closestBomb = null;
		double distance = 0, minDistance = 50;
		Collection<AiBomb> bombs = as.zone.getBombs();

		Iterator<AiBomb> iter = bombs.iterator();

		while (iter.hasNext()) {
			bomb = iter.next();
			distance = control.getHypotenuseTo(as.currentTile, bomb.getTile());

			if (distance < minDistance) {
				minDistance = distance;
				closestBomb = bomb;
			}
		}

		return closestBomb;

	}

	/**
	 * retourne la feu la plus proche à une case donnée
	 * 
	 * @return
	 * @throws StopRequestException
	 */
	AiFire findClosestFire() throws StopRequestException {
		as.checkInterruption();

		AiFire fire, closestFire = null;
		double distance = 0, minDistance = 50;
		Collection<AiFire> fires = as.zone.getFires();

		Iterator<AiFire> iter = fires.iterator();

		while (iter.hasNext()) {
			fire = iter.next();
			distance = control.getHypotenuseTo(as.currentTile, fire.getTile());

			if (distance < minDistance) {
				minDistance = distance;
				closestFire = fire;
			}
		}

		return closestFire;

	}

	/**
	 * retourne la distance directe entre la danger la plus proche et une case
	 * donnée
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public double getHypotenuseToDanger(AiTile tile)
			throws StopRequestException {
		as.checkInterruption();

		if (closestDanger != null) {
			if (closestDanger instanceof AiBomb)
				return control.getHypotenuseTo(tile, ((AiBomb) closestDanger)
						.getTile());
			else
				return control.getHypotenuseTo(tile, ((AiFire) closestDanger)
						.getTile());
		} else
			return 100;

	}

	/**
	 * retourne la distance de Manhattan entre la danger la plus proche et une
	 * case donnée
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	double getManhattanToDanger(AiTile tile) throws StopRequestException {
		as.checkInterruption();

		if (closestDanger instanceof AiBomb)
			return control.getManhattanTo(tile, ((AiBomb) closestDanger)
					.getTile());
		else
			return control.getManhattanTo(tile, ((AiFire) closestDanger)
					.getTile());

	}

}
