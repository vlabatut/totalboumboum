package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v3.criterion;

import java.util.ArrayList;
import java.util.Iterator;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v3.GerginOzkanoglu;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v3.PathCalculation;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v3.TileCalculation;

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
	 * limit to access 2
	 */
	private static int LIMIT_TO_ACCESS_2 = 2;

	/**
	 * limit to access 
	 */
	private static double LIMIT_TO_ACCESS = 2.5;

	/**
	 * Distance limit to check if it is close or far.
	 */
	private final int DISTANCE_UPPER_LIMIT = 6;
	
	/**
	 * first index 
	 */
	private static int ZERO = 0;

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
		PathCalculation calculate = new PathCalculation(this.ai);
		TileCalculation tc = new TileCalculation(this.ai);
		AiPath pathToConsideredTile = calculate.bestPath(this.ai.getZone().getOwnHero(), tile);
		if(pathToConsideredTile == null)
		    result = false;
		else
		{
			long timeToAccess = pathToConsideredTile.getDuration(this.ai.getZone().getOwnHero());
		    if (tc.isDangerous(tile)) // if true, then the tile is dangerous.
			{
				// if considered tile is in the range of a bomb in the zone then it
				// will be added
				// to this list.
				if(this.ai.getZone().getFires().contains(tile))
				{
					AiBomb bomb = this.ai.getZone().getOwnHero().getBombPrototype();
					AiFire fire = tile.getFires().get(ZERO);
					long fireDuration = bomb.getExplosionDuration() - fire.getElapsedTime();
					if(fireDuration > timeToAccess)
					    result = false;
				}
				else
				{
					ArrayList<AiBomb> dangerousBombs = new ArrayList<AiBomb>();
					for (AiBomb bomb : ai.getZone().getBombs()) // this loop will pass
																// through the bombs in
																// the zone.
					{
						ai.checkInterruption();
						if (bomb.isWorking()) {
							Iterator<AiTile> it = tc.rangeOfBombCrossableByHero(bomb.getTile(), bomb.getRange()).iterator(); // this
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
								if (it.next().compareTo(tile) == ZERO) // we compare our
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
				}
			}else if(tc.isThereSuddenDeathEventInTile(tile)) //then there is sudden death event
			{
				AiSuddenDeathEvent event = this.ai.getZone().getNextSuddenDeathEvent();
				long currentTime=this.ai.getZone().getTotalTime();
				if(timeToAccess <= (event.getTime()-currentTime)*LIMIT_TO_ACCESS && (event.getTime()-currentTime) <= timeToAccess*LIMIT_TO_ACCESS_2 )
					result = false;
			}
		    else // then this criteria become "distance"
			{
				if (this.ai.getZone().getTileDistance(
						this.ai.getZone().getOwnHero().getTile(), tile) > DISTANCE_UPPER_LIMIT)
					result = false;
			}
		}
	    
		return result;
	}
}
