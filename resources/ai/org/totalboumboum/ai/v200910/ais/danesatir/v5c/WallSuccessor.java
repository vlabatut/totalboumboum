package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiStateName;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v200910.adapter.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 5.c
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class WallSuccessor extends BasicSuccessorCalculator {
	/** */
	private DaneSatir ai;
	/** */
	private TimeMatrice time;
	
	/**
	 * 
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public WallSuccessor(DaneSatir ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai=ai;
		this.time=new TimeMatrice(ai);
	}
	/**
	 * Ignore Destructible walls
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	@Override
	public List<AiTile> processSuccessors(AstarNode node) throws StopRequestException
	{	ai.checkInterruption();
		
		// init
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tile = node.getTile();
		AiHero hero = node.getHero();
		// pour chaque case voisine : on la rajoute si elle est traversable
		for(Direction direction: Direction.getPrimaryValues())			
		{	ai.checkInterruption();
			AiTile neighbor = tile.getNeighbor(direction);
			List<AiBlock> blocks = neighbor.getBlocks();
			boolean test=false;
			if (!blocks.isEmpty()) {
				test=true;
				if(blocks.get(0).isDestructible() && blocks.get(0).getState().getName()!=AiStateName.BURNING)
					test=false;
			}
			if(neighbor.isCrossableBy(hero) || test==false) {
				GeneralFuncs.printLog(neighbor+" Time:"+time.getTime(neighbor),VerboseLevel.MED,ai);
				if(time.getTime(neighbor) > (node.getDepth()+1)*Limits.tileDistance ||
						time.getTime(neighbor) == 0)
					result.add(neighbor);
			}
		}
		//
		return result;
	}
}
