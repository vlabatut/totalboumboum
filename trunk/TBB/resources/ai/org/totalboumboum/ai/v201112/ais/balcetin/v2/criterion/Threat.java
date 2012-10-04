package org.totalboumboum.ai.v201112.ais.balcetin.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.BalCetin;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.TileProcess;

/**
 * Criterion binary to decide if there is a threat for a tile.	
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class Threat extends AiUtilityCriterionBoolean {
	/** Nom de ce critère */
	public static final String NAME = "Threat";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Threat(BalCetin ai) throws StopRequestException { // init
																		// nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected BalCetin ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////


	TileProcess tp = new TileProcess(this.ai);

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		//is this tile in dangerous tiles ?
		if(tp.getDangerousTiles().contains(tile))
		result = true;
		
		return result;
	}
}
