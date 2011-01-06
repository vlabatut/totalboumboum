package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.AkbulutKupelioglu;

/**
 * A collect strategy, used to destroy walls and search for bonus.
 * @author yasa
 *
 */
public class CollectStrategy extends Strategy
{

	/**
	 * Creates a new CollectStrategy instance.
	 * @param ia The AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public CollectStrategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		super(ia);
		ia.checkInterruption();
		// TODO Auto-generated constructor stub
	}

}
