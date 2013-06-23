package org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.SaglamSeven;

/**
 * 
 * @author Esra Sağlam
 * @author Cihan Adil Seven
 */
@SuppressWarnings("deprecation")
public class CriterATTPertinence extends AiUtilityCriterionBoolean<SaglamSeven>
{	/** Nom de ce critère */
	public static final String NAME = "ATTAQUE_PERTINENCE";
		
	/**
	 * (Range - CLOSING_LIMIT) determines the tile's state.
	 */
	public static final int CLOSING_LIMIT = 3;
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterATTPertinence(SaglamSeven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiZone zone = ai.getZone();
		AiHero ownHero=zone.getOwnHero();

		boolean result = false;
	
		for ( AiHero currentEnemy : this.ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
	
			if ( this.ai.getDangerousTilesOnBombPut( tile, ownHero.getBombRange() - CLOSING_LIMIT ).contains( currentEnemy.getTile() ) ) result = true;
		}
	
		return result;
	}
}
