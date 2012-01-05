package org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.CoskunOzdemir;

/**
 * Cette classe représente est un simple exemple de critère binaire. Copiez-la,
 * renommez-la, modifiez-la pour l'adapter à vos besoin.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class ColPertinence extends AiUtilityCriterionBoolean
{
	/** Nom de ce critère */
	public static final String NAME = "ColPertinence";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ColPertinence( CoskunOzdemir ai ) throws StopRequestException
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
		int bombNumber = this.ai.getZone().getOwnHero().getBombNumberMax();
		int fireRange = this.ai.getZone().getOwnHero().getBombRange();

		if ( tile.getItems().contains( AiItemType.EXTRA_BOMB ) )
		{
			if ( bombNumber < fireRange )
			{
				result = true;
			}
			else
				result = false;
		}

		if ( tile.getItems().contains( AiItemType.EXTRA_FLAME ) )
		{
			if ( bombNumber > fireRange )
			{
				result = true;
			}
			else
				result = false;
		}

		return result;
	}
}
