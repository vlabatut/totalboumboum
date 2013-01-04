package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.AlcarKayaYildirim;

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
		this.ai1 = ai;
	}

	/** */
	protected AlcarKayaYildirim ai1;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai1.checkInterruption();
		AiZone zone = this.ai1.getZone();
		boolean result = false;
	
		// If there is an enemy in a 5 tile range of the given tile then it
		// returns true.
		if (zone.getRemainingOpponents().isEmpty() != true) {
			for (AiHero currentopponent : zone.getRemainingOpponents()) {
				ai1.checkInterruption();
				if (this.ai1.getDist(currentopponent.getTile(), tile) <= COMPETITITON_TRUE) {
					result = true;
				} else
					result = false;
			}
		}

		return result;
	}
}
