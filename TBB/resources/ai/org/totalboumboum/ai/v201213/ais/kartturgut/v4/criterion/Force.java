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
public class Force extends AiUtilityCriterionBoolean<KartTurgut>
{	/** */
	public static final String NAME = "Force";
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Force(KartTurgut ai) throws StopRequestException
	{	
		super(ai,NAME);
		ai.checkInterruption();
	}

    @Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean resultat = true;
		int flamme , nbr_bombe; 
		
		AiZone zone = ai.getZone();
		AiHero notrehero ;
					
		notrehero = zone.getOwnHero();
		flamme=notrehero.getBombRange();
		nbr_bombe=notrehero.getBombNumberMax();
		if(nbr_bombe>8 && flamme>3)				
		resultat=true;		
		else 
		resultat=false;
		
		return resultat;
	}
}
	
		
	

