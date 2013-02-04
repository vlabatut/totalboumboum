package org.totalboumboum.ai.v201213.ais.erdemtayyar.v4;

import java.util.ArrayList;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<ErdemTayyar> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	protected BombHandler(ErdemTayyar ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

		
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException {
		ai.checkInterruption();

		this.ai.setUtilityMap(this.ai.getUtilityHandler().getUtilitiesByTile());
		if (this.ai.getHero().getTile()
				.equals(this.ai.getTileWithBiggestUtility())
				&& !this.ai.getHs().isHeroInDanger()
				&& this.ai.getHs().canReachSafety()) {
			// ATTACK MODE
			
			
			if (this.ai.getModeHandler().getMode().equals(AiMode.ATTACKING)) {
				boolean enemyAccessible = false;
				ArrayList<AiTile> enemyTiles = new ArrayList<AiTile>();
				
				for (AiHero aiHero : this.ai.getZone().getRemainingOpponents()) {
					ai.checkInterruption();
					enemyTiles.add(aiHero.getTile());
				}
				for (AiTile aiTile : enemyTiles) {
					ai.checkInterruption();
					if (this.ai.getTs().getAccessibleTiles().contains(aiTile)) {
						enemyAccessible = true;
						break;
					}
				}
				if (!enemyAccessible) {
					boolean fireInAccessibleTiles = false;
					for (AiTile aiTile : this.ai.getTs().getAccessibleTiles()) {
						ai.checkInterruption();
						if (!aiTile.getFires().isEmpty()) {
							fireInAccessibleTiles = true;
							break;
						}
					}

					if (this.ai.getZone()
							.getBombsByColor(PredefinedColor.ORANGE).isEmpty()
							&& !fireInAccessibleTiles && (this.ai.utilityMap.get(this.ai.getHero().getTile())>10)) {
						return true;
					}
					
				} else {
					if (this.ai.getHs().isHeroInRange()) {
						return true;
					} else {
						return false;
					}
				}
				
			}
			// COLLECT MODE
			else {
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
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();
		
		}
}
