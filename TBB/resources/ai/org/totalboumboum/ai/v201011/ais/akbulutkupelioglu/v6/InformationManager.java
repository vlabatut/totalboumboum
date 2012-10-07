package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * Manages informations in a way to avoid recalculations. Currently not used.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 *
 */
@SuppressWarnings("deprecation")
public class InformationManager
{
	/** */
	AkbulutKupelioglu monIa;
	/** */
	AiZone zone;
	
	/**
	 * 
	 * @param zone
	 * @param ia
	 * @throws StopRequestException
	 */
	public InformationManager(AiZone zone, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		this.zone = zone;		
	}
}
