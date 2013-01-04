package org.totalboumboum.ai.v201213.ais.guneysharef.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.guneysharef.v2.GuneySharef;

/**

 * @author Melis Güney
 * @author Seli Sharef
 */
public class NbrNDW extends AiUtilityCriterionInteger<GuneySharef>
{	
	/**
	 * 
	 */
	public static final String NAME = "NbrMurNonDestructible";
	
	/**  
	 * @param ai 
	 * 
	 * @throws StopRequestException	
	 * 		
	 */
	public NbrNDW(GuneySharef ai) throws StopRequestException
	{	super(ai,NAME,0,3);
		ai.checkInterruption();
		this.ai=ai;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Calcule et renvoie la valeur de critère pour la case passée en paramètre. 
	 */
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		int result = 0;
		
		AiMode mode=null;
	
		if(mode == AiMode.ATTACKING){
			for(AiTile n : tile.getNeighbors()){
				ai.checkInterruption();
				for(AiBlock b : n.getBlocks()){
					ai.checkInterruption();
					if(!b.isDestructible()){
						result=result+1;
					}
				}
			}
			
		}
		return result;
	}
}
