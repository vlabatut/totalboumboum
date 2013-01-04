package org.totalboumboum.ai.v201213.ais.oralozugur.v2.criterion;



import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v2.OralOzugur;

/**
 * 
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class BestWall extends AiUtilityCriterionBoolean<OralOzugur>
{	/** Nom de ce critère */
	public static final String NAME = "BestWall";
	
	/**
	 * 
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public BestWall(OralOzugur ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		
		
		
		for(AiTile tileNeighbor : tile.getNeighbors())
		{
			ai.checkInterruption();
			for(AiBlock tileBlock: tileNeighbor.getBlocks())
			{
				ai.checkInterruption();

				if(tileBlock.isDestructible())
				{
					for(AiTile neighbor : tileNeighbor.getNeighbors())
					{
						ai.checkInterruption();
						if(!this.ai.getAccessibleTiles().contains(neighbor))
						{
							result=true;
						}
					}
				}
					
			}
		}
		
	
		return result;
	}
}
