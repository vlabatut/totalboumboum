package org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.BalCetin;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.TileProcess;

/**
 * Criterion binary to decide if there is a threat for a tile.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class Threat extends AiUtilityCriterionBoolean {
	public static final String NAME = "Threat";

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

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		TileProcess tp = new TileProcess(this.ai);
		boolean result = false;
		// is this tile in dangerous tiles ?
		if (tp.getDangerousTiles().contains(tile))
			result = true;

		return result;
	}
}
