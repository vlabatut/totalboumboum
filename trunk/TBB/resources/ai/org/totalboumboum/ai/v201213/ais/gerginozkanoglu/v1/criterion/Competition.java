package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1.criterion;

import java.util.Iterator;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1.GerginOzkanoglu;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1.TileCalculation;

/**
 * This criterion will evaluate the tile's accesibility by other agents.
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
public class Competition extends AiUtilityCriterionBoolean<GerginOzkanoglu> {
	/** Criterion's name */
	public static final String NAME = "Competition";

	/**
	 * @param ai
	 * @throws StopRequestException
	 * 
	 */
	public Competition(GerginOzkanoglu ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		TileCalculation calculate = new TileCalculation(this.ai);
		long ourTime = calculate.getTimeForDistance(tile, this.ai.getZone()
				.getOwnHero());
		boolean control = true;
		Iterator<AiHero> iter = this.ai.getZone().getRemainingHeroes()
				.iterator(); // we will change this to getRemainingOpponents
								// later.
		while (iter.hasNext() && control) {
			ai.checkInterruption();
			AiHero hero = iter.next();
			if (!hero.equals(this.ai.getZone().getOwnHero())) {
				long othersTime = calculate.getTimeForDistance(tile, hero);
				if (ourTime > othersTime) {
					result = false;
					control = false;
				}
			}
		}

		return result;
	}
}
