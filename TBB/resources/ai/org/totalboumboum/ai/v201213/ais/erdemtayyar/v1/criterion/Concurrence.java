package org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.ErdemTayyar;

/**
 * 
 * C'est un critere binaire.
 * Cette classe est pour le critere concurrence, pour montrer si on est plus proche a un item que l'adversaire plus proche.
 * 
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
public class Concurrence extends AiUtilityCriterionBoolean<ErdemTayyar> {
	/** Nom de ce critère */
	public static final String NAME = "Concurrence";

	// Constructor
	
	/** 
	 * On initialise la valeur dont la domaine de définition est  TRUE et FALSE.
	 * Si IA est plus proche a un item que l'adversaire sa valeur est TRUE sinon FALSE.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Concurrence(ErdemTayyar ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		AiTile myTile = this.ai.getHero().getTile();
		int myDistance = this.ai.getZone().getTileDistance(myTile, tile);

		for (AiHero currentEnemy : this.ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			if (this.ai.getZone().getTileDistance(currentEnemy.getTile(), tile) < myDistance)
				result = false;
		
		}
		return result;
	}
}
