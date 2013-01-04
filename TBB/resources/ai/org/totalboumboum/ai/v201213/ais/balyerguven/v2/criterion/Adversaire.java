package org.totalboumboum.ai.v201213.ais.balyerguven.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v2.BalyerGuven;


/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class Adversaire extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** Nom de ce critère */
	public static final String NAME = "Adversaire";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Adversaire(BalyerGuven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		AiTile myTile = this.ai.getHero().getTile();
		int myDistance = this.ai.getZone().getTileDistance( myTile, tile );
		
		for ( AiHero ennemi : this.ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			if ( this.ai.getZone().getTileDistance( ennemi.getTile(), tile ) < myDistance )
				{
				return false;
				}
		}
		return true;
	}
}
