package org.totalboumboum.ai.v201112.ais.sakaryasar.v1.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v1.SakarYasar;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
@SuppressWarnings("deprecation")
public class CriterionDestructibleWalls extends AiUtilityCriterionInteger{
	/** */
	public static final String NAME = "WALLS";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionDestructibleWalls(SakarYasar ai) throws StopRequestException
	{	// init nom
		super(NAME,0,2);
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected SakarYasar ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	
		int result=0;	
		int i=0;
		AiTile left,right,up,down;
		
		left = right = up = down = tile;
		
		while(result<2 && i<ai.getZone().getOwnHero().getBombRange()){
			left = left.getNeighbor(Direction.LEFT);
			right = right.getNeighbor(Direction.RIGHT);
			up = up.getNeighbor(Direction.UP);
			down = down.getNeighbor(Direction.DOWN);
			
			if(!left.getBlocks().isEmpty() || !right.getBlocks().isEmpty() || !up.getBlocks().isEmpty() || !down.getBlocks().isEmpty())
				result++;
			i++;
		}
		
		return result;
	}

}
