package org.totalboumboum.ai.v201112.ais.sakaryasar.v2.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v2.SakarYasar;
import org.totalboumboum.engine.content.feature.Direction;

@SuppressWarnings("deprecation")
public class CriterionDestructibleWalls extends AiUtilityCriterionInteger{
	public static final String NAME = "WALLS";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionDestructibleWalls(SakarYasar ai) throws StopRequestException
	{	// init nom
		super(NAME,0,2);
		ai.checkInterruption();
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
	{	ai.checkInterruption();
		int result=0;	
		int i=0;
		AiTile left,right,up,down;
		List <AiBlock> bLeft;
		List <AiBlock> bRight;
		List <AiBlock> bUp;
		List <AiBlock> bDown;

		left = right = up = down = tile;
		
		while(result<2 && i<ai.getZone().getOwnHero().getBombRange()-1){
			ai.checkInterruption();
			left = left.getNeighbor(Direction.LEFT);
			bLeft = left.getBlocks();
			right = right.getNeighbor(Direction.RIGHT);
			bRight = right.getBlocks();
			up = up.getNeighbor(Direction.UP);
			bUp=up.getBlocks();
			down = down.getNeighbor(Direction.DOWN);
			bDown = down.getBlocks();
			
			if(!bLeft.isEmpty() && bLeft.get(0).isDestructible() && result < 2){
				result++;
			}
			if(!bRight.isEmpty() && bRight.get(0).isDestructible() && result < 2){
				result++;
			}
			if(!bUp.isEmpty() && bUp.get(0).isDestructible() && result < 2){
				result++;
			}
			if(!bDown.isEmpty() && bDown.get(0).isDestructible() && result < 2){
				result++;
			}
			i++;
		}
		
		return result;
	}

}
