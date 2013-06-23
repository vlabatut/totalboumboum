package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.AkbulutKupelioglu;


/**
 * A survive strategy, used to survive.
 * @author yasa
 *
 */
public class SurviveStrategy extends Strategy
{

	Strategy lastStrategy = null;
	/**
	 * Creates a new SurviveStrategy instance.
	 * @param ia The AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */	
	public SurviveStrategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		super(ia);
		monIa.checkInterruption();
		// 
	}
	
	
	/**
	 * Gets the last active strategy before this AI switched to this strategy.
	 * @return The last actvie strategy.
	 * @throws StopRequestException 
	 */
	public Strategy getLastStrategy() throws StopRequestException
	{
		monIa.checkInterruption();
		return lastStrategy;
	}
	
	/**
	 * Sets the lastStrategy field, to let the AI return to it upon completion of this strategy.
	 * @param lastStrategy The current strategy.
	 * @throws StopRequestException 
	 */
	public void setLastStrategy(Strategy lastStrategy) throws StopRequestException
	{
		monIa.checkInterruption();
		this.lastStrategy = lastStrategy;
	}


	
}
