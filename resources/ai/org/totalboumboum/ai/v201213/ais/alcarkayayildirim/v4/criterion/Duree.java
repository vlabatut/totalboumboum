package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4.AlcarKayaYildirim;

/**
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
@SuppressWarnings("deprecation")
public class Duree extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "DUREE";
	
	/**
	 * Distance radius limit to check to determine the tile's state.
	 */
	private final int DISTANCE_UPPER_LIMIT_COL = 3;
	/** */
	private final int DISTANCE_UPPER_LIMIT_ATT = 5;
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Duree(AlcarKayaYildirim ai) throws StopRequestException
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
			if (distanceAverage <= DISTANCE_UPPER_LIMIT_COL)
				result = true;
		}
		if (this.ai.modeHandler.getMode() == AiMode.ATTACKING) {
			if (distanceAverage <= DISTANCE_UPPER_LIMIT_ATT)
				result = true;
		}
	
		return result;
	}
}
