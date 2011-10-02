/**
 * cette classe consiste des methodes sur tout les bonuses et enemies dans la zone.
 */
package org.totalboumboum.ai.v200809.ais.akpolatsener.v2c;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Cem Akpolat
 * @author Emre Şener
 *
 */
@SuppressWarnings("deprecation")
public class Target {

	/** la classe principal de notre IA */
	AkpolatSener as;

	/** l'objet pour les operations sur les cases */
	TileControl control;

	/** l'objet de la cible le plus proche */
	Object closestTarget;

	public Target(AkpolatSener as) throws StopRequestException {
		as.checkInterruption();
		this.as = as;
		control = new TileControl(as);
		findClosest(as.currentTile);

	}

	/**
	 * trouve le cible la plus proche à une case donnée
	 * 
	 * @param tile
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	void findClosest(AiTile tile) throws StopRequestException {
		as.checkInterruption();

		double distToEnemy, distToBonus;

		AiHero enemy = findClosestEnemy();
		AiItem bonus = findClosestBonus();

		distToEnemy = control.getHypotenuseTo(tile, enemy.getTile());

		if (bonus != null)
			distToBonus = control.getHypotenuseTo(tile, bonus.getTile());
		else
			distToBonus = 100;

		if (enemy != null)
			distToEnemy = control.getHypotenuseTo(tile, enemy.getTile());
		else
			distToEnemy = 100;

		if (distToEnemy < distToBonus)
			closestTarget = enemy;
		else
			closestTarget = bonus;

	}

	/**
	 * retourne le cible la plus proche
	 * 
	 * @return
	 * @throws StopRequestException 
	 */
	public Object getClosestTarget() throws StopRequestException {
		as.checkInterruption();
		return closestTarget;
	}

	/**
	 * retourne l'enemie la plus proche à une case donnée
	 * 
	 * @return
	 * @throws StopRequestException
	 */
	AiHero findClosestEnemy() throws StopRequestException {
		as.checkInterruption();
		AiHero enemy, closestEnemy = null;
		double distance = 0, minDistance = 50;
		Collection<AiHero> enemies = as.zone.getHeroes();

		Iterator<AiHero> iter = enemies.iterator();

		while (iter.hasNext()) {
			as.checkInterruption();
			enemy = iter.next();

			if (enemy != as.ownHero) {

				distance = control.getHypotenuseTo(as.currentTile, enemy
						.getTile());

				if (distance < minDistance) {
					minDistance = distance;
					closestEnemy = enemy;
				}
			}
		}

		return closestEnemy;

	}

	/**
	 * retourne le bonus la plus proche à une case donnée
	 * 
	 * @return
	 * @throws StopRequestException
	 */
	AiItem findClosestBonus() throws StopRequestException {
		as.checkInterruption();

		AiItem bonus, closestBonus = null;
		double distance = 0, minDistance = 50;
		Collection<AiItem> bonuses = as.zone.getItems();

		Iterator<AiItem> iter = bonuses.iterator();

		while (iter.hasNext()) {
			as.checkInterruption();
			bonus = iter.next();
			distance = control.getHypotenuseTo(as.currentTile, bonus.getTile());

			if (distance < minDistance) {
				minDistance = distance;
				closestBonus = bonus;
			}
		}

		return closestBonus;

	}

	/**
	 * retourne la distance directe entre la cible la plus proche et une case
	 * donnée
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public double getHypotenuseToTarget(AiTile tile)
			throws StopRequestException {
		as.checkInterruption();

		if (closestTarget instanceof AiHero)
			return control.getHypotenuseTo(tile, ((AiHero) closestTarget)
					.getTile());
		else
			return control.getHypotenuseTo(tile, ((AiItem) closestTarget)
					.getTile());

	}

	/**
	 * retourne la distance de Manhattan entre la cible la plus proche et une
	 * case donnée
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public double getManhattanToTarget(AiTile tile) throws StopRequestException {
		as.checkInterruption();

		if (closestTarget instanceof AiHero)
			return control.getManhattanTo(tile, ((AiHero) closestTarget)
					.getTile());
		else
			return control.getManhattanTo(tile, ((AiItem) closestTarget)
					.getTile());
	}

}
