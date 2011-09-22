package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class SurviveStrategy extends Strategy
{

	public SurviveStrategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		super(ia);
		monIa.checkInterruption();
		// 
	}

	
}
