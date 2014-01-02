package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;

/**
 *this criterion is for shows the depth of the selected tile in a closed tunnel 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class FireField extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "FIRE_FIELD";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public FireField(Agent ai)
	{	super(ai,NAME,1,4);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
		int result = 4;
	/*	
		int diff = ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),tile) - 
		ai.getZone().getOwnHero().getBombRange();
		
		if(ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),tile) > 
			ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),ai.getTheEnemyInsideTheTunnel().getTile()))
		{
			result = 3;
		}else if(diff > 0){
			result = 2;
		}else if(diff == 0){
			result = 1;
		}
	*/		
		//distance to enemy inside the tunnel
	/*	int tType = ai.tunnelType(tile);
		if (tType == 3){
			result = 1;
		}
		else{
			result = 0;
		}
		*/
/*		int dist = ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),ai.getTheEnemyInsideTheTunnel().getTile());
		if(dist == 1 || dist == 0)
			result = 1;
		else if(dist > 1 && dist<4 )
			result = 2;
		else if(dist > 4 && dist < 8) 
			result = 3;
		else 
			result = 4;
	*
	*/
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
