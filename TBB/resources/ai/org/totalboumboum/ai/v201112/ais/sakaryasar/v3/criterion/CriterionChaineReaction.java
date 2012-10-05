package org.totalboumboum.ai.v201112.ais.sakaryasar.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v3.SakarYasar;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
@SuppressWarnings("deprecation")
public class CriterionChaineReaction extends AiUtilityCriterionBoolean{
	/** */
	public static final String NAME = "REACTION";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionChaineReaction(SakarYasar ai) throws StopRequestException
	{	// init nom
		super(NAME);
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
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result=false;	
		int d=0,i=0;
		AiTile left,right,up,down;
		
		left = right = up = down = tile;
		
		while(d<2 && i<ai.getZone().getOwnHero().getBombRange()){
			ai.checkInterruption();
			if(left.getNeighbor(Direction.LEFT).getBlocks().isEmpty())
				left = left.getNeighbor(Direction.LEFT);
			if(right.getNeighbor(Direction.RIGHT).getBlocks().isEmpty())
				right = right.getNeighbor(Direction.RIGHT);
			if(up.getNeighbor(Direction.UP).getBlocks().isEmpty())
				up = up.getNeighbor(Direction.UP);
			if(down.getNeighbor(Direction.DOWN).getBlocks().isEmpty())
				down = down.getNeighbor(Direction.DOWN);
			
			if(!left.getBombs().isEmpty() || !right.getBombs().isEmpty() || !up.getBombs().isEmpty() || !down.getBombs().isEmpty())
				d++;
			i++;
		}
		if(d>0)
			result = true;
		return result;
	}

}
