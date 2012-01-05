package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.ArikKoseoglu;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
public class TempsCriter extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "TEMPS";
	public static final int TEMPS_LIMIT = 10;
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public TempsCriter(ArikKoseoglu ai) throws StopRequestException
	{	// init nom
		super(NAME,0,TEMPS_LIMIT);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ArikKoseoglu ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		// larguer plus haut de gameZone/2 (>) larguer plus haut de path entre hero et destination  
		AiZone gameZone = ai.getZone();
		AiHero ownHero = gameZone.getOwnHero();
		AiTile ownTile = ownHero.getTile();
		int distanceAverage= gameZone.getTileDistance(ownTile, tile);
		if(TEMPS_LIMIT < distanceAverage){
			return TEMPS_LIMIT;
		}
		return distanceAverage;
	}
}
