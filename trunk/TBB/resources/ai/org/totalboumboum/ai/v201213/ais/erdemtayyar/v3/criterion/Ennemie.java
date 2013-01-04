package org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.ErdemTayyar;

/**
 * Cette classe est un simple exemple de critère binaire, pour renvoyer la distance de l'adversaire,
 * si on est proche comme 2 cases il renvois vrai, sinon faux.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
public class Ennemie extends AiUtilityCriterionBoolean<ErdemTayyar> {
	/**
	 * name
	  */
	public static final String NAME = "Ennemie";
	/**
	 * limit, distance de l'adversaire
	 */
	public static final int limit = 2;
	/**
	 * 
	 * On initialise la valeur dont la domaine de définition est TRUE et FALSE.
	 * Si l'adversaire est faible sa valeur est TRUE sinon FALSE.
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public Ennemie(ErdemTayyar ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();

	}
	
	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{   
		ai.checkInterruption();
		boolean result = false;
	    for ( AiHero currentEnemy : this.ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();

			if ( this.ai.getTs().getDangerousTilesOnBombPut( tile, this.ai.getHero().getBombRange() - limit ).contains( currentEnemy.getTile() ) ) result = true;
		}
		return result;
	}
}
