package org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.CoskunOzdemir;

/**
 * Cette classe représente est un simple exemple de critère binaire. Copiez-la,
 * renommez-la, modifiez-la pour l'adapter à vos besoin.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class Duree extends AiUtilityCriterionBoolean
{
	/** Nom de ce critère */
	public static final String NAME = "Duree";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Duree( CoskunOzdemir ai ) throws StopRequestException
	{ // init nom
		super( NAME );

		// init agent
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected CoskunOzdemir ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		boolean result = false;
		if ( this.ai.getZone().getTileDistance(
				this.ai.getZone().getOwnHero().getTile(), tile ) <= 6 )
			result = true;

		return result;
	}
}