package org.totalboumboum.ai.v201213.ais.guneysharef.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.guneysharef.v4.GuneySharef;

/**

 * @author Melis Güney
 * @author Seli Sharef
 */
public class NbrDW extends AiUtilityCriterionInteger<GuneySharef>
{	
	/**
	 * Nom de ce critère
	 */
	public static final String NAME = "NbrMurDestructible";
	
	/**  
	 * @param ai 
	 * 
	 * @throws StopRequestException	
	 * 		
	 */
	public NbrDW(GuneySharef ai) throws StopRequestException
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
		
		AiMode mode=this.ai.getMode();
	
		if(mode == AiMode.COLLECTING){
			for(AiTile n : tile.getNeighbors()){
				ai.checkInterruption();
				for(AiBlock b : n.getBlocks()){
					ai.checkInterruption();
					if(b.isDestructible()){
						result=result+1;
					}
				}
			}
			
		}
		return result;
	}
}
