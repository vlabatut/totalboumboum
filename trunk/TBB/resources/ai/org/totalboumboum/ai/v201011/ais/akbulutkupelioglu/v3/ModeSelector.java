package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.mode.AttackMode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.mode.CollectMode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.mode.Mode;

/**
 * Class responsible for mode selection.
 * 
 * @author Yasa Akbulut
 * 
 */
public class ModeSelector
{
	private static final int BONUS_THRESHOLD = 2; //minimum number of remaining items

	/**
	 * Chooses a mode given some perceptions. It may be a CollectMode or an
	 * AttackMode.
	 * 
	 * @param myZone
	 *            AiZone containing the perceptions.
	 * @param ia
	 *            AkbulutKupelioglu using this. (for checkInterruption())
	 * @return the chosen Mode.
	 * @throws StopRequestException
	 */
	public static Mode selectMode(AiZone myZone, AkbulutKupelioglu ia)
			throws StopRequestException
	{
		ia.checkInterruption();
		Mode result = null;
		// TODO:implement =)
		
		result = CollectMode.getInstance(ia);
		if(myZone.getHiddenItemsCount()<BONUS_THRESHOLD)
		{
			result=AttackMode.getInstance(ia);
		}
		//if we're one on one, we want to attack no matter what
		if(myZone.getRemainingHeroes().size() == 2)
		{
			result=AttackMode.getInstance(ia);
		}
		if(result.interest.getWidth()!=myZone.getWidth()||result.interest.getHeight()!=myZone.getHeight())
			result.resetMatrix(myZone.getWidth(), myZone.getHeight());
		return result;
	}
	
	public static void reset(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		AttackMode.reset(ia);
		CollectMode.reset(ia);
	}
}
