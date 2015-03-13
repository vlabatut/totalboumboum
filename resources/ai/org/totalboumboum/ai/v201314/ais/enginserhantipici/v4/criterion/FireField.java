package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.TunnelHandler;

/**
 * this criterion is for shows the depth of the selected tile in a closed tunnel 
 * First we divide the tunnel in 4 parts
 * criterion values are 1 to 4 ;
 *  for the first part at the beginning of the tunnel, the value is "1" 
 *  "4" for the deepest part
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
@SuppressWarnings("deprecation")
public class FireField extends AiCriterionInteger<Agent>
{	/** Name of the criterion */
	public static final String NAME = "FIRE_FIELD";
	
	/**
	 * Creation of a new boolean criterion
	 * 
	 * @param ai
	 * 		related agent 
	 */
	public FireField(Agent ai)
	{	super(ai,NAME,1,4);
		ai.checkInterruption();
	}
	/**
 	* TunnelHandler
 	*/
	TunnelHandler th;
	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
		int result = 4;
		th = new TunnelHandler(ai);

		int size = ai.getTilesInTunnel().size();
		int range = (int)(ai.getTilesInTunnel().size() / 4);
	
		
		if(size == 1){
			range = 1;
			if (ai.getTilesInTunnel().subList(0, range).contains(tile))
				result = 1;
		}else if(size == 2){
			range = 1;
			if (ai.getTilesInTunnel().subList(0, range).contains(tile))
				result = 1;
			if (ai.getTilesInTunnel().subList(range, 2 * range).contains(tile))
				result = 2;
		}else if(size == 3){
			range = 1;
			if (ai.getTilesInTunnel().subList(0, range).contains(tile))
				result = 1;
			if (ai.getTilesInTunnel().subList(range, 2 * range).contains(tile))
			result = 2;
			if (ai.getTilesInTunnel().subList(2 * range, 3 * range).contains(tile))
				result = 3;	
		}else{
			if (ai.getTilesInTunnel().subList(0, range).contains(tile))
				result = 1;
			if (ai.getTilesInTunnel().subList(range, 2 * range).contains(tile))
				result = 2;
			if (ai.getTilesInTunnel().subList(2 * range, 3 * range).contains(tile))
				result = 3;
			if (ai.getTilesInTunnel().subList(3 * range, 4 * range).contains(tile))
				result = 4;
		}
			return result;
	}
}
