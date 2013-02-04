package org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.kartturgut.v4.KartTurgut;

/**
 * @author Yunus Kart
 * @author Siyabend Turgut
 */
@SuppressWarnings("deprecation")
public class Nonconcurrence extends AiUtilityCriterionBoolean<KartTurgut>
{	/** */
	public static final String	NAME	= "Nonconcurrence";
	/** */
	AiZone zone;
	/** */
	AiHero notrehero;
	/** */
	AiTile notretile;
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Nonconcurrence( KartTurgut ai ) throws StopRequestException
	{
		super( ai,NAME );
		ai.checkInterruption();
	}

	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		zone = this.ai.getZone();
		notrehero = zone.getOwnHero();
		AiTile notreTile = notrehero.getTile();
		int notreDistance = this.ai.getZone().getTileDistance( notreTile, tile );
		for ( AiHero currentEnemy : this.ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			if ( this.ai.getZone().getTileDistance( currentEnemy.getTile(), tile ) < notreDistance ) 
				return false;
		}
		        return true;
	}
}
