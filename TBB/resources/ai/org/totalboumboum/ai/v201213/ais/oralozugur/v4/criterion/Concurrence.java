package org.totalboumboum.ai.v201213.ais.oralozugur.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v4.OralOzugur;

/**
 * Si un ennemi est plus proche que notre agent il vaut 1, sinon 0.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
@SuppressWarnings("deprecation")
public class Concurrence extends AiUtilityCriterionBoolean<OralOzugur>
{	/** Nom de ce critère */
	public static final String NAME = "Concurrence";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Concurrence(OralOzugur ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		
		AiTile myTile = ai.getZone().getOwnHero().getTile();
		int myDistance = ai.getZone().getTileDistance( myTile, tile );

		for (AiHero currentEnemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			if ( ai.getZone().getTileDistance( currentEnemy.getTile(), tile ) < myDistance ) return true;
		}
		return false;
		
	}
}
