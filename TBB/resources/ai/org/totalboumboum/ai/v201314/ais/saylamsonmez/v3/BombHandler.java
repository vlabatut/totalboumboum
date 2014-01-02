package org.totalboumboum.ai.v201314.ais.saylamsonmez.v3;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class BombHandler extends AiBombHandler<Agent> {
	/** our hero */
	AiHero ourHero;
	/** zone */
	AiZone zone;
	/** les cases à poser des bombes */
	public AiTile bombeTile = null;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		//verbose = true;

		zone = ai.getZone();
		ourHero = zone.getOwnHero();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();
		boolean result = false;
		AiMode mode = this.ai.modeHandler.getMode();

		print("BombeHandlerdayım ");
		if ((ourHero.getTile().getBombs().size() == 0 && (ourHero
				.getBombNumberCurrent() < ourHero.getBombNumberMax()))) {
			print("ilk ifteyim");

			if (canRun()) {
				print("CANRUNDAYIM");
				if (mode.equals(AiMode.ATTACKING)) {

					print("MODE ATTAQUE");
					if (ai.isBlockingEnemy(ourHero.getTile())) {
						bombeTile = ourHero.getTile();
						result = true;
					}
					if (ai.moveHandler.control) {
						print("BOMBEHANLERDA BOMBA BIRAKIYORUM1");
						result = true;
					}
					Enemy e = new Enemy(ai);
					AiHero hero = e.selectEnemy();

					if (!zone.getRemainingOpponents().isEmpty())
						if (isEnemyInBombRange()
								&& ourHero.getTile() != hero.getTile()
								&& isNotBombeInNeighbors()) {
							bombeTile = ourHero.getTile();
							result = true;
						}

				} else if (mode.equals(AiMode.COLLECTING)) {
					if (ai.getNbMurDetruitofTile(ourHero.getTile()) > 0)
						return true;
					if (ai.moveHandler.control) {
						print("BOMBEHANLERDA BOMBA BIRAKIYORUM2");
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
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isNotBombeInNeighbors() throws StopRequestException {
		ai.checkInterruption();
		boolean bombeControl = true;
		for (AiTile tile : ourHero.getTile().getNeighbors()) {
			ai.checkInterruption();
			if (!tile.getBombs().isEmpty())
				bombeControl = false;
		}
		return bombeControl;
	}

	/**
	 * Methode pour controler s'il est possible échapper après qu'on pose une
	 * bombe.
	 * 
	 * @return S'il est possible d'échapper il retourne vrai, sinon false.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean canRun() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		List<AiTile> reachableTiles = ai.getReachableTiles(ourHero.getTile());
		reachableTiles.removeAll(ai.getCurrentDangerousTiles());
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
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isEnemyInBombRange() throws StopRequestException {
		ai.checkInterruption();
		Enemy e = new Enemy(this.ai);
		AiHero hero = e.selectEnemy();
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
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isNotSecureTilesExist() {
		ai.checkInterruption();
		boolean result = false;
		List<AiTile> reachableTiles = ai.getReachableTiles(ourHero.getTile());

		print("**************************isNotSecure içindeyim");
		reachableTiles.removeAll(ai.getCurrentDangerousTiles());
		if (reachableTiles.isEmpty()) {
			print("**************************isNotSecure içindeyim   IFFEYİZ");
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
