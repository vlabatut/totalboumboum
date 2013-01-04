package org.totalboumboum.ai.v201213.ais.erdemtayyar.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v2.ErdemTayyar;

/**
 * 	Crée un nouveau critère entier, pour la sécurité ed l'agent
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
public class Securite extends AiUtilityCriterionInteger<ErdemTayyar> {
	/**
	 * We affect the name of out criteria
	 */
	public static final String NAME = "Securite";
    
	// Constructor
	/**
	 * 
	 * On initialise la valeur dont la domaine de définition est 0, 1 , et 2
	 * Si une case est securé et appartient pas de malus sa valeur est 2,
	 * Si une appartient un malus sa valeur est 1,
	 * sinon sa valeur est 0.
	 *
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Securite(ErdemTayyar ai) throws StopRequestException {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		
		int result = 2;
		
		if (this.ai.getTs().getCurrentDangerousTiles().contains(tile))
			result = 0;
		if(this.ai.getZone().getItems().contains(AiItemType.ANTI_BOMB) || this.ai.getZone().getItems().contains(AiItemType.ANTI_FLAME) || this.ai.getZone().getItems().contains(AiItemType.ANTI_SPEED) || this.ai.getZone().getItems().contains(AiItemType.NO_BOMB) || this.ai.getZone().getItems().contains(AiItemType.NO_FLAME) || this.ai.getZone().getItems().contains(AiItemType.NO_SPEED))
			result = 1;

		return result;
	}
}
