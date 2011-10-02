package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
@SuppressWarnings("deprecation")
public class AttackStrategy extends Strategy
{

	public AttackStrategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		super(ia);
		ia.checkInterruption();
		// 
	}

}
