package org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.CoskunOzdemir;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class NonConcurrence extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "NonConcurrence";
	
	/**
	 * Crée un nouveau critère binaire.
	 * @param ai 
	 * 		?
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public NonConcurrence(CoskunOzdemir ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected CoskunOzdemir ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		AiTile myTile = this.ai.getZone().getOwnHero().getTile();
		int myDistance = this.ai.getZone().getTileDistance( myTile, tile );
	
		for ( AiHero currentEnemy : this.ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			if ( this.ai.getZone().getTileDistance( currentEnemy.getTile(), tile ) < myDistance )
				return false;
		}
		return true;
	}
}
