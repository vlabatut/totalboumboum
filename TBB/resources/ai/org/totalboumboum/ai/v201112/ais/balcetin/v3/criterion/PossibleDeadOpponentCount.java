package org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.BalCetin;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.TileProcess;

/**
 * Criterion to decide how many opponent will
 * be dead if we put this tile a bomb.
 *  
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class PossibleDeadOpponentCount extends AiUtilityCriterionInteger {
	/** */
	public static final String NAME = "PossibleDeadOpponentCount";

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public PossibleDeadOpponentCount(BalCetin ai) throws StopRequestException {
		// criterion domain
		super(NAME, 0, 3);
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
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		int count = 0;
		TileProcess tp = new TileProcess(ai);
		AiZone zone = ai.getZone();
		
		//List of remaining opponent heros
		List<AiHero> opponentHeros = zone.getRemainingOpponents();
		for (AiHero aiHero : opponentHeros) {
			ai.checkInterruption();
			
			//heros' tile is in the dangerousTilesOnBombDrop list ? if yes, count++
			if (tp.getDangerousTilesOnBombDrop().contains(aiHero.getTile()))
				count++;
		}
		return count;

	}
}
