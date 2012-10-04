package org.totalboumboum.ai.v201112.ais.demirsazan.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.DemirSazan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Serdil Demir
 * @author Gokhan Sazan
 */
@SuppressWarnings("deprecation")
public class DistanceCible extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCE_CIBLE&TEMPS";
	/** */
	public static final int DISTANCE_LIMIT = 15;
	
	/**
	 * Crée un nouveau critère binaire.
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public DistanceCible(DemirSazan ai) throws StopRequestException
	{	// init nom
		super(NAME,0,DISTANCE_LIMIT);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
		
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected DemirSazan ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile)  throws StopRequestException
	{	ai.checkInterruption();
	
		AiZone gameZone = ai.getZone();
		AiHero ownHero = gameZone.getOwnHero();
		AiTile ownTile = ownHero.getTile();
		int distanceAverage= gameZone.getTileDistance(ownTile, tile);
		if(DISTANCE_LIMIT < distanceAverage){
			return DISTANCE_LIMIT;
		}
		return distanceAverage;
	}
}
