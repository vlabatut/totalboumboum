package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
//import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v3.CiplakErakyol;

/**
 * cette critere est pour trouver les duree entre notre agent est les sprites.
 * Critère: Durée
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionDuree extends AiUtilityCriterionInteger<CiplakErakyol> {
	/** Nom de ce critère */
	public static final String NAME = "Duree";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionDuree(CiplakErakyol ai) throws StopRequestException {
		super(ai, NAME, 0, 2); // soit temp limit est 2
		ai.checkInterruption();
		this.ai = ai;
	}

	// protected CiplakErakyol ai;
	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException 
	{
		int result = 0;
		AiZone oyunAlani = ai.getZone();
		AiHero ajan = oyunAlani.getOwnHero();
		AiTile yerimiz = ajan.getTile();
		double mesafe = oyunAlani.getTileDistance(yerimiz, tile); // distance

		if (mesafe>0 && mesafe <= 2) // plus proche
			result = 2;
		else if ( 2 < mesafe && mesafe<= 3) // proche
			result = 1;
		else
			result = 0;
		return result;
	}
}
