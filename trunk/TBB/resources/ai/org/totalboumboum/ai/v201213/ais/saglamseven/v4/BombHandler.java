package org.totalboumboum.ai.v201213.ais.saglamseven.v4;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Esra Sağlam
 * @author Cihan Adil Seven
 */
public class BombHandler extends AiBombHandler<SaglamSeven> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(SaglamSeven ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = true;

		// TODO à compléter
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException {
		ai.checkInterruption();
		AiHero ownHero = this.ai.getZone().getOwnHero();
		AiTile ownTile = ownHero.getTile();
		AiMode mode = this.ai.modeHandler.getMode();

		if (mode.equals(AiMode.COLLECTING)) {
			if ((ownHero.getBombNumberCurrent() < ownHero.getBombNumberMax())
					&& ownTile.getBombs().isEmpty()) {
				for (Direction direction : Direction.getPrimaryValues()) {
					ai.checkInterruption();
					if (this.ai.destructibleMurCheck(ownTile, direction) == true)
						return true;

				}

			} else {
				return false;

			}

		}

		else if (mode.equals(AiMode.ATTACKING)) {
			for (int i = 0; i < this.ai.getZone().getRemainingOpponents()
					.size(); i++) {
				ai.checkInterruption();

				if (this.ai.canReachSafety(ownHero)
						&& this.ai
								.getDangerousTilesOnBombPut(ownTile, 2)
								.contains(
										this.ai.getZone()
												.getRemainingOpponents().get(i)))
					return true;
			}
		}

		return false;
	}

	/**
	 * @param aiTile
	 * @param direction
	 * @return
	 * @throws StopRequestException
	 */

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		// TODO à compléter, si vous voulez afficher quelque chose
	}
}
