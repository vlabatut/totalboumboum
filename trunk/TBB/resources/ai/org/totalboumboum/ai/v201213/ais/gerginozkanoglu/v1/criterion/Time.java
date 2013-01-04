package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1.criterion;

import java.util.ArrayList;
import java.util.Iterator;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1.GerginOzkanoglu;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1.TileCalculation;

/**
 * This criterion will measure the time necessary to reach the tile.
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
public class Time extends AiUtilityCriterionBoolean<GerginOzkanoglu> {
	/** Criterion's name */
	public static final String NAME = "Time";

	/**
	 * Distance limit to check if it is close or far.
	 */
	private final int DISTANCE_UPPER_LIMIT = 6;

	/**
	 * 
	 * @param ai
	 * 
	 * @throws StopRequestException
	 * 
	 */
	public Time(GerginOzkanoglu ai) throws StopRequestException {
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
//		long timeToAccess = ((long) this.ai.getZone().getTileDistance(
//				this.ai.getZone().getOwnHero().getTile(), tile))
//				/ ((long) this.ai.getZone().getOwnHero().getCurrentSpeed());
		TileCalculation calculate = new TileCalculation(this.ai);
	    long timeToAccess =	calculate.getTimeForDistance(tile, this.ai.getZone().getOwnHero());
		boolean security = calculate.isDangerous(tile);
		if (security) // if true, then the tile is dangerous.
		{
			// if considered tile is in the range of a bomb in the zone then it
			// will be added
			// to this list.
			ArrayList<AiBomb> dangerousBombs = new ArrayList<AiBomb>();
			for (AiBomb bomb : ai.getZone().getBombs()) // this loop will pass
														// through the bombs in
														// the zone.
			{
				ai.checkInterruption();
				if (bomb.isWorking()) {
					Iterator<AiTile> it = bomb.getBlast().iterator(); // this
																		// iterator
																		// will
																		// pass
																		// through
																		// each
																		// bomb's
																		// range.
					boolean control = true;
					while (it.hasNext() && control) {
						ai.checkInterruption();
						if (it.next().compareTo(tile) == 0) // we compare our
															// tile with the
															// range of bomb.
						{
							dangerousBombs.add(bomb);
							control = false;
						}
					}
				}
			}
			Iterator<AiBomb> it = dangerousBombs.iterator();
			// calculate each bomb's explosion time and compare with the time
			// that our agent reach.
			boolean control = true;
			while (it.hasNext() && control) {
				ai.checkInterruption();
				AiBomb bomb = it.next();
				long timeRemaining = bomb.getNormalDuration()
						- bomb.getElapsedTime();
				if (timeRemaining <= timeToAccess) {
					result = false;
					control = false;
				}
			}
		} else // then this criteria become "distance"
		{
			if (this.ai.getZone().getTileDistance(
					this.ai.getZone().getOwnHero().getTile(), tile) > DISTANCE_UPPER_LIMIT)
				result = false;
		}
		return result;
	}
}
