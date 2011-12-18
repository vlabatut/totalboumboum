package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

public class InformationManager
{

	AkbulutKupelioglu monIa;
	AiZone zone;

	
	
	public InformationManager(AiZone zone, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		this.zone = zone;		
	}
	

	
	
	
}