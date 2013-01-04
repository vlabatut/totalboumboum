package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1.AlcarKayaYildirim;

/**
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class Concurrence extends AiUtilityCriterionBoolean<AlcarKayaYildirim> {
	/** Nom de ce critère */
	public static final String NAME = "CONCURRENCE";
	/** */
	public static final int COMPETITITON_TRUE = 5;

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public Concurrence(AlcarKayaYildirim ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;

		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		List<AiHero> opponents = zone.getRemainingOpponents();

		for(int i = 0;i<opponents.size();i++)
		{
			ai.checkInterruption();
			
			if(zone.getTileDistance(tile, ownHero.getTile())>zone.getTileDistance(tile,opponents.get(i).getTile()))
			{
				result = true;
			}	
		}

		return result;
	}
}
