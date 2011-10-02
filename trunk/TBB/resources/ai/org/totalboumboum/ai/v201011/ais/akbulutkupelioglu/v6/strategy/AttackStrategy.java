package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.AkbulutKupelioglu;

/**
 * An attack strategy, used to destroy an enemy.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 *
 */
@SuppressWarnings("deprecation")
public class AttackStrategy extends Strategy
{

	/**
	 * Creates a new AttackStrategy instance.
	 * @param ia The AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public AttackStrategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		super(ia);
		ia.checkInterruption();
		// 
	}



}
