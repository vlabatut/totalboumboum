package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant les situations blockage de l'ennemie cible.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class BlockingHandler extends AiAbstractHandler<Agent> {
	/** our hero */
	AiHero ourHero;
	/** zone */
	AiZone zone;
	/** our current tile */
	AiTile ourTile;

	/** pour acceder aux methodes de TileCalculationHandler */
	TileCalculationHandler tileCalculationHandler;
	/** pour acceder aux methodes de EnemyHandler */
	EnemyHandler enemyHandler;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected BlockingHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		zone = ai.getZone();
		ourHero = zone.getOwnHero();
	}

	/**
	 * Initialisation de gestionnaire
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected void initHandler(Agent ai) {
		ai.checkInterruption();
		this.ai = ai;
		tileCalculationHandler = ai.tileCalculationHandler;
		enemyHandler = ai.enemyHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Méthode pour calculer la distance de la bloque. Par rapport à cette
	 * valeur, on décide dans le gestionnaire du mode, est-ce que notre bombe a
	 * assez portée pour réaliser la stratégie d'attaque.
	 * 
	 * @param hero
	 *            Les agents dans la zone à part de nous.
	 * 
	 * @return Cette méthode retourne la distance de bloque.
	 */
	public int getBlockSize(AiHero hero) {
		ai.checkInterruption();
		boolean result = true;
		zone = ai.getZone();
		ourHero = zone.getOwnHero();
		ArrayList<AiTile> currentDangerousTiles = tileCalculationHandler
				.getCurrentDangerousTiles();
		int i = 0;
		ourTile = ourHero.getTile();
		AiTile tileHero = hero.getTile();
		ai.checkInterruption();
		if (tileHero != null) {

			while (tileHero.getNeighbor(Direction.DOWN).isCrossableBy(hero,
					false, false, false, false, true, true)) {
				ai.checkInterruption();

				if (result == true) {

					if ((!tileHero.getNeighbor(Direction.LEFT).isCrossableBy(
							hero, false, false, false, false, true, true) || currentDangerousTiles
							.contains(tileHero.getNeighbor(Direction.LEFT)))
							&& (!tileHero.getNeighbor(Direction.RIGHT)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.RIGHT)))) {
						result = true;
						i++;
						tileHero = tileHero.getNeighbor(Direction.DOWN);
					} else {
						i = 0;
						break;
					}
				}

			}

			tileHero = hero.getTile();
			while (tileHero.getNeighbor(Direction.UP).isCrossableBy(hero,
					false, false, false, false, true, true)) {
				ai.checkInterruption();

				if (result == true) {

					if ((!tileHero.getNeighbor(Direction.LEFT).isCrossableBy(
							hero, false, false, false, false, true, true) || currentDangerousTiles
							.contains(tileHero.getNeighbor(Direction.LEFT)))
							&& (!tileHero.getNeighbor(Direction.RIGHT)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.RIGHT)))) {
						result = true;
						i++;
						tileHero = tileHero.getNeighbor(Direction.UP);
					} else {
						i = 0;
						break;
					}
				}

			}

			tileHero = hero.getTile();
			while (tileHero.getNeighbor(Direction.LEFT).isCrossableBy(hero,
					false, false, false, false, true, true)) {
				ai.checkInterruption();

				if (result == true) {

					if ((!tileHero.getNeighbor(Direction.UP).isCrossableBy(
							hero, false, false, false, false, true, true) || currentDangerousTiles
							.contains(tileHero.getNeighbor(Direction.UP)))
							&& (!tileHero.getNeighbor(Direction.DOWN)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.DOWN)))) {
						result = true;
						i++;
						tileHero = tileHero.getNeighbor(Direction.LEFT);
					} else {
						i = 0;
						break;
					}
				}

			}

			tileHero = hero.getTile();
			while (tileHero.getNeighbor(Direction.RIGHT).isCrossableBy(hero,
					false, false, false, false, true, true)) {
				ai.checkInterruption();

				if (result == true) {

					if ((!tileHero.getNeighbor(Direction.UP).isCrossableBy(
							hero, false, false, false, false, true, true) || currentDangerousTiles
							.contains(tileHero.getNeighbor(Direction.UP)))
							&& (!tileHero.getNeighbor(Direction.DOWN)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.DOWN)))) {
						result = true;
						i++;
						tileHero = tileHero.getNeighbor(Direction.RIGHT);
					}

					else {
						i = 0;
						break;
					}
				}

			}
		}
		return i;
	}

	/**
	 * Méthode pour tester si la critère de bloque assure ou pas.
	 * 
	 * @param tile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @return Cette méthode returne une valeur booléan, est-ce que la situation
	 *         d'etre bloque assure.
	 */
	public Boolean isBlockingEnemy(AiTile tile) {
		ai.checkInterruption();
		boolean result = true;
		ourHero = zone.getOwnHero();

		ArrayList<AiTile> currentDangerousTiles = tileCalculationHandler
				.getCurrentDangerousTiles();
		int bombRange = ourHero.getBombRange();
		int i = 0;
		ourTile = ourHero.getTile();
		AiSimZone simZone = new AiSimZone(zone);
		AiBomb myBomb = simZone.createBomb(tile, simZone.getOwnHero());
		if (!simZone.getRemainingOpponents().isEmpty()) {
			AiHero hero = enemyHandler.selectEnemy();
			AiTile tileHero = hero.getTile();
			i = 0;
			if (myBomb.getBlast().contains(tileHero)) {
				while (tileHero.getNeighbor(Direction.DOWN).isCrossableBy(hero,
						false, false, false, false, true, true)
						&& i <= bombRange
						&& !tileHero.getNeighbor(Direction.DOWN).equals(
								myBomb.getTile())) {
					ai.checkInterruption();
					tileHero = tileHero.getNeighbor(Direction.DOWN);
					if (result == true) {
						if ((!tileHero.getNeighbor(Direction.LEFT)
								.isCrossableBy(hero, false, false, false,
										false, true, true) || currentDangerousTiles
								.contains(tileHero.getNeighbor(Direction.LEFT)))
								&& (!tileHero.getNeighbor(Direction.RIGHT)
										.isCrossableBy(hero, false, false,
												false, false, true, true) || currentDangerousTiles
										.contains(tileHero
												.getNeighbor(Direction.RIGHT)))
								&& myBomb.getBlast().contains(tileHero))
							result = true;
						else
							result = false;
					}
					i++;
				}
				i = 0;
				tileHero = hero.getTile();
				while (tileHero.getNeighbor(Direction.UP).isCrossableBy(hero,
						false, false, false, false, true, true)
						&& i <= bombRange
						&& !tileHero.getNeighbor(Direction.UP).equals(
								myBomb.getTile())) {
					ai.checkInterruption();
					tileHero = tileHero.getNeighbor(Direction.UP);
					if (result == true) {
						if ((!tileHero.getNeighbor(Direction.LEFT)
								.isCrossableBy(hero, false, false, false,
										false, true, true) || currentDangerousTiles
								.contains(tileHero.getNeighbor(Direction.LEFT)))
								&& (!tileHero.getNeighbor(Direction.RIGHT)
										.isCrossableBy(hero, false, false,
												false, false, true, true) || currentDangerousTiles
										.contains(tileHero
												.getNeighbor(Direction.RIGHT)))
								&& myBomb.getBlast().contains(tileHero))
							result = true;
						else
							result = false;
					}
					i++;
				}

				i = 0;
				tileHero = hero.getTile();
				while (tileHero.getNeighbor(Direction.LEFT).isCrossableBy(hero,
						false, false, false, false, true, true)
						&& i <= bombRange
						&& !tileHero.getNeighbor(Direction.LEFT).equals(
								myBomb.getTile())) {
					ai.checkInterruption();
					tileHero = tileHero.getNeighbor(Direction.LEFT);
					if (result == true) {
						if ((!tileHero.getNeighbor(Direction.UP).isCrossableBy(
								hero, false, false, false, false, true, true) || currentDangerousTiles
								.contains(tileHero.getNeighbor(Direction.UP)))
								&& (!tileHero.getNeighbor(Direction.DOWN)
										.isCrossableBy(hero, false, false,
												false, false, true, true) || currentDangerousTiles
										.contains(tileHero
												.getNeighbor(Direction.DOWN)))
								&& myBomb.getBlast().contains(tileHero))
							result = true;
						else
							result = false;
					}
					i++;
				}
				i = 0;
				tileHero = hero.getTile();
				while (tileHero.getNeighbor(Direction.RIGHT).isCrossableBy(
						hero, false, false, false, false, true, true)
						&& i <= bombRange
						&& !tileHero.getNeighbor(Direction.RIGHT).equals(
								myBomb.getTile())) {
					ai.checkInterruption();
					tileHero = tileHero.getNeighbor(Direction.RIGHT);
					if (result == true) {
						if ((!tileHero.getNeighbor(Direction.UP).isCrossableBy(
								hero, false, false, false, false, true, true) || currentDangerousTiles
								.contains(tileHero.getNeighbor(Direction.UP)))
								&& (!tileHero.getNeighbor(Direction.DOWN)
										.isCrossableBy(hero, false, false,
												false, false, true, true) || currentDangerousTiles
										.contains(tileHero
												.getNeighbor(Direction.DOWN)))
								&& myBomb.getBlast().contains(tileHero))
							result = true;

						else
							result = false;
					}
					i++;
				}
			} else
				result = false;

		}
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();

	}
}
