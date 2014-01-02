package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class BombHandler extends AiBombHandler<Agent> {

	/** */
	private AiHero ownHero = null;

	/** */
	private AiZone zone = null;

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
		ownHero = zone.getOwnHero();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de déterminer si l'agent doit poser une bombe ou pas.
	 * 
	 * @return true si l'agent peut poser une bombe false sinon
	 */
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();

		AiTile myTile = ownHero.getTile();

		AiModeHandler<Agent> modeHandler = ai.getModeHandler();
		AiMode mode = modeHandler.getMode();

		boolean needAstar = false;

		if ( (ai.getZone().getRemainingOpponents().size() == 1 && (ownHero.getBombNumberMax()) > 5) ) {
			needAstar = false;
		} else {
			needAstar = true;
		}

		if ( (ownHero.getBombNumberMax() - ownHero.getBombNumberCurrent()) != 0 && (myTile.getBombs().isEmpty()) ) {
			if ( mode == AiMode.COLLECTING ) {
				if ( myTile == ai.endTile && !ai.getIH().usefulItemsExistence() && ai.getCG().wallInOurBlast() ) {
					if ( ai.getTH().IsThereAnyReachableSafeTileBeforeEnnemy(ownHero) ) {
						return true;
					}
				}
			} else {
				if ( !needAstar ) {
					if ( ai.ennemyAccesibility ) {
						if ( (ai.getHH().ennemyInOurBlast()) || ai.getCG().chaineReactionPossibility() || ai.getCG().ennemyBombMe()
								&& !ai.getCG().tooManyBombsControl() ) {
							if ( ai.getTH().isThereAnySafeTile(ownHero) ) {
								return true;
							}
						}
					} else {
						if ( ai.getCG().accesibleDestWallExistence() ) {
							if ( myTile == ai.getWallToReachEnnemy ) {
								if ( ai.getTH().IsThereAnyReachableSafeTileBeforeEnnemy(ownHero) ) {
									return true;
								}
							}
						}
					}
				} else {
					if ( ai.ennemyAccesibility ) {
						if ( ai.getCG().ennemyBombMe() && !ai.getCG().tooManyBombsControl() ) {
							if ( ai.getTH().IsThereAnyReachableSafeTileBeforeEnnemy(ownHero) ) {
								return true;
							}
						} else if ( ai.getCG().chaineReactionPossibility() ) {
							if ( ai.getTH().IsThereAnyReachableSafeTileBeforeEnnemy(ownHero) ) {
								return true;
							}
						} else if ( (ai.getHH().ennemyInOurBlast()) ) {
							if ( ai.getTH().IsThereAnyReachableSafeTileBeforeEnnemy(ownHero) ) {
								return true;
							}
						}
					} else {
						if ( ai.getCG().accesibleDestWallExistence() ) {
							if ( myTile == ai.getWallToReachEnnemy ) {
								if ( ai.getTH().IsThereAnyReachableSafeTileBeforeEnnemy(ownHero) ) {
									return true;
								}
							}
						}
					}
				}
			}
			if ( ai.getIH().dangerousItemOnMyNextTile() && (ownHero.getBombNumberMax() - ownHero.getBombNumberCurrent()) > 1 )
				if ( ai.getTH().IsThereAnyReachableSafeTileBeforeEnnemy(ownHero) ) {
					return true;
				}
		}
		return false;
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
