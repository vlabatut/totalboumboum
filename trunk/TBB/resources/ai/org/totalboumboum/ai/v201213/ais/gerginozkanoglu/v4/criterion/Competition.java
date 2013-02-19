package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.criterion;

import java.util.Iterator;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.GerginOzkanoglu;

/**
 * This criterion will evaluate the tile's accesibility by other agents.
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
@SuppressWarnings("deprecation")
public class Competition extends AiUtilityCriterionBoolean<GerginOzkanoglu> {
	/** Criterion's name */
	public static final String NAME = "Competition";

	/**
	 * @param ai
	 * 		description manquante !	
	 * @throws StopRequestException
	 * 		description manquante !	
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
		int ourDistance = Math.abs(this.ai.getZone().getTileDistance(tile,
				this.ai.getZone().getOwnHero().getTile()));

		boolean control = true;
		Iterator<AiHero> iter = this.ai.getZone().getRemainingHeroes()
				.iterator(); // we will change this to getRemainingOpponents
		// later.
		while (iter.hasNext() && control) {
			ai.checkInterruption();
			AiHero hero = iter.next();
			if (!hero.equals(this.ai.getZone().getOwnHero())) {
				int distanceForEnemy = Math.abs(this.ai.getZone()
						.getTileDistance(hero.getTile(), tile));
				if (ourDistance > distanceForEnemy) {
					result = false;
					control = false;
				}
				else
				{
					if(ourDistance == distanceForEnemy)
					{
						if(hero.getWalkingSpeed() > this.ai.getZone().getOwnHero().getWalkingSpeed())
						{
							result = false;
							control = false;
						}
					}
				}
			}
		}

		return result;
	}
}