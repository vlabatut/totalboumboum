package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class SurviveStrategy extends Strategy
{

	Strategy lastStrategy = null;
	public SurviveStrategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		super(ia);
		monIa.checkInterruption();
		// 
	}
	public Strategy getLastStrategy()
	{
		return lastStrategy;
	}
	public void setLastStrategy(Strategy lastStrategy)
	{
		this.lastStrategy = lastStrategy;
	}
	
	
}
