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
public class Duree extends AiUtilityCriterionBoolean<KartTurgut>
{	/** */
	public static final String NAME = "Duree";	
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Duree(KartTurgut ai) throws StopRequestException    
	{
		super(ai,NAME);
		ai.checkInterruption();
	}

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		
		AiZone ZoneDeJeu = ai.getZone();
		AiHero notreHero = ZoneDeJeu.getOwnHero();
		AiTile notreTile = notreHero.getTile();
		int distance= ZoneDeJeu.getTileDistance(notreTile, tile);
		if(distance<8){
			return true;
		}
		return false;
	}
}

