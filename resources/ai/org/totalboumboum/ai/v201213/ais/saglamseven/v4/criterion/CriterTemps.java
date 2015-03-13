package org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.SaglamSeven;

/**
 * Cette classe est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 *  @author Esra Sağlam
 * @author Cihan Adil Seven
 */
@SuppressWarnings("deprecation")
public class CriterTemps extends AiUtilityCriterionBoolean<SaglamSeven>
{	/** Nom de ce critère */
	public static final String NAME = "TEMPS";
	
	/**
	 * 
	 */
	private final int DISTANCE_BEST_COL=4;
	/**
	 * 
	 */
	private final int DISTANCE_BEST_ATT=8;
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterTemps(SaglamSeven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		AiZone gameZone = ai.getZone();
		AiHero ownHero = gameZone.getOwnHero();
		AiTile ownTile = ownHero.getTile();
		int distanceAverage= gameZone.getTileDistance(ownTile, tile);
		
		if (this.ai.modeHandler.getMode() == AiMode.COLLECTING) {
			if (distanceAverage <= DISTANCE_BEST_COL)
				result = true;
		}
		if (this.ai.modeHandler.getMode() == AiMode.ATTACKING) {
			if (distanceAverage <= DISTANCE_BEST_ATT)
				result = true;
		}
	
		return result;
	}}
