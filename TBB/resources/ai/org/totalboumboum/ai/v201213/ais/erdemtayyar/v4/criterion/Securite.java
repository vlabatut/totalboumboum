package org.totalboumboum.ai.v201213.ais.erdemtayyar.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v4.ErdemTayyar;

/**
 * 	Crée un nouveau critère entier, pour la sécurité de l'agent
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
		for(AiItem item : tile.getItems()){
		ai.checkInterruption();
		if(!item.getType().isBonus())
			result = 1;
		}
		return result;
	}
}
