package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4.CiplakErakyol;

/**
 * Critere de pertinence l'adversaire.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
@SuppressWarnings("deprecation")
public class CriterionPertinenceAdv extends
		AiUtilityCriterionBoolean<CiplakErakyol> {
	/** Nom de ce critère */
	public static final String NAME = "PertinentAdv";

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionPertinenceAdv(CiplakErakyol ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// protected CiplakErakyol ai;
	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		for ( AiHero hero : tile.getHeroes() )
		{
			ai.checkInterruption();
			if ( ai.getZone().getOwnHero().getBombNumberMax() >= hero.getBombNumberMax() )
				return true;
		}
		return false;
	}
}