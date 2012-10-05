package org.totalboumboum.ai.v201112.ais.sakaryasar.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v3.SakarYasar;
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
		
		boolean l,r,u,d;
		l=r=u=d=false;
		while(result<2 && i<ai.getZone().getOwnHero().getBombRange()-1){
			ai.checkInterruption();
			if(left.getBlocks().isEmpty() ){
				left = left.getNeighbor(Direction.LEFT);
			}
			else{
				bLeft = left.getBlocks();
				if(bLeft.get(0).isDestructible() && result < 2 && !l){
					result++;
					l=true;
				}
			}
			if(right.getBlocks().isEmpty()){
				right = right.getNeighbor(Direction.RIGHT);
			}
			else{
				bRight = right.getBlocks();
				if(bRight.get(0).isDestructible() && result < 2 && !r){
					result++;
					r=true;
				}
			}
			if(up.getBlocks().isEmpty()){
				up = up.getNeighbor(Direction.UP);
			}
			else{
				bUp=up.getBlocks();
				if(bUp.get(0).isDestructible() && result < 2 && !u){
					result++;
					u=true;
				}
			}
			if(down.getBlocks().isEmpty()){
				down = down.getNeighbor(Direction.DOWN);
			}
			else{
				bDown = down.getBlocks();
				if(bDown.get(0).isDestructible() && result < 2 && !d){
					result++;
					d=true;
				}
			}
			i++;
		}
		
		return result;
	}

}
