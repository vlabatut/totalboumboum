package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<Agent> {
	/** our hero */
	AiHero ourHero;
	/** zone */
	AiZone zone;
	/** les cases à poser des bombes */
	public AiTile bombTile = null;
	/** pour utiliser les methodes qui se trouve dans TileCalculationHandler */
	TileCalculationHandler tileCalculationHandler;
	/** pour acceder aux methodes de BlockingEnemy */
	BlockingHandler blockingHandler;
	/** pour acceder aux methodes de EnemyHandler */
	EnemyHandler enemyHandler;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai) {
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
		blockingHandler = ai.blockingHandler;
		enemyHandler = ai.enemyHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();
		boolean result = false;
		AiMode mode = this.ai.modeHandler.getMode();
		
		if ((ourHero.getTile().getBombs().size() == 0 && (ourHero
				.getBombNumberCurrent() < ourHero.getBombNumberMax()))) {
			

			if (canRun()) {
				
				if (mode.equals(AiMode.ATTACKING)) {

					
					if (blockingHandler.isBlockingEnemy(ourHero.getTile())) {
						bombTile = ourHero.getTile();
						result = true;
					}
					if (ai.moveHandler.control) {
						result = true;
					}

					AiHero hero = enemyHandler.selectEnemy();

					if (!zone.getRemainingOpponents().isEmpty())
						if (isEnemyInBombRange()
								&& ourHero.getTile() != hero.getTile()
								&& isNotBombeInNeighbors()) {
							bombTile = ourHero.getTile();
							result = true;
						}

				} else if (mode.equals(AiMode.COLLECTING)) {
					if (tileCalculationHandler.getNbMurDetruitofTile(ourHero
							.getTile()) > 0)
						return true;
					if (ai.moveHandler.control) {
						result = true;
					}
				}
			}

		}
		return result;
	}

	/**
	 * Methode pour controler s'il y a une bombe dans les cases à coté de nous.
	 * 
	 * @return S'il y en a il retourne vrai, sinon il retourne faux.
	 */
	public boolean isNotBombeInNeighbors() {
		ai.checkInterruption();
		boolean bombControl = true;
		for (AiTile tile : ourHero.getTile().getNeighbors()) {
			ai.checkInterruption();
			if (!tile.getBombs().isEmpty())
				bombControl = false;
		}
		return bombControl;
	}

	/**
	 * Methode pour controler s'il est possible échapper après qu'on pose une
	 * bombe.
	 * 
	 * @return S'il est possible d'échapper il retourne vrai, sinon faux.
	 */
	public boolean canRun() {
		ai.checkInterruption();
		boolean result = false;
		List<AiTile> reachableTiles = tileCalculationHandler
				.getReachableTiles(ourHero.getTile());
		reachableTiles.removeAll(tileCalculationHandler
				.getCurrentDangerousTiles());
		reachableTiles.removeAll(ourHero.getBombPrototype().getBlast());
		if (!reachableTiles.isEmpty()) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Methode pour controler si l'ennemie se trouve dans la porte de nos
	 * bombes.
	 * 
	 * @return Si l'ennemie est dans la porté de nos bombes alors il retourne
	 *         vrai, sinon il retourne faux.
	 */
	public boolean isEnemyInBombRange() {
		ai.checkInterruption();
		AiHero hero = enemyHandler.selectEnemy();
		if (!zone.getRemainingOpponents().isEmpty()) {
			if (ourHero.getBombPrototype().getBlast().contains(hero.getTile())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Methode pour controler s'il y a des cases secures dans la zone
	 * accessible.
	 * 
	 * @return S'il y en a il retourne vrai, sinon il retourne faux.
	 */
	public boolean isNotSecureTilesExist() {
		ai.checkInterruption();
		boolean result = false;
		List<AiTile> reachableTiles = tileCalculationHandler
				.getReachableTiles(ourHero.getTile());

		
		reachableTiles.removeAll(tileCalculationHandler
				.getCurrentDangerousTiles());
		if (reachableTiles.isEmpty()) {
			result = true;
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
