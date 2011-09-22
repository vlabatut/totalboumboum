package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * Manages informations in a way to avoid recalculations. Currently not used.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
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
