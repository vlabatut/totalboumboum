package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2.AlcarKayaYildirim;

/**
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class NonConcurrence extends AiUtilityCriterionBoolean<AlcarKayaYildirim> {
	/** Nom de ce critère */
	public static final String NAME = "NON_CONCURRENCE";
	/** */
	public static final int COMPETITITON_TRUE = 4;

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public NonConcurrence(AlcarKayaYildirim ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		AiZone zone = this.ai.getZone();
		boolean result = false;
	
		// If there is an enemy in a 5 tile range of the given tile then it
		// returns true.
		if (zone.getRemainingOpponents().isEmpty() != true) {
			for (AiHero currentopponent : zone.getRemainingOpponents()) {
				ai.checkInterruption();
				if (this.ai.getDist(currentopponent.getTile(), tile) <= COMPETITITON_TRUE) {
					result = true;
				} else
					result = false;
			}
		}

		return result;
	}
}
