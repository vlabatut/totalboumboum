package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;

public class CollectStrategy extends Strategy
{

	public CollectStrategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		super(ia);
		ia.checkInterruption();
		// TODO Auto-generated constructor stub
	}

}
