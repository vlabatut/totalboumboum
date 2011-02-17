package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.AkbulutKupelioglu;

/**
 * An attack strategy, used to destroy an enemy.
 * @author yasa
 *
 */
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
