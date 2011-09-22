package org.totalboumboum.ai.v200910.ais.danesatir.v3;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v200910.adapter.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 3
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class WallSuccessor extends BasicSuccessorCalculator {
	@SuppressWarnings("unused")
	private DaneSatir ai;
	private TimeMatrice time;
	
	public WallSuccessor(DaneSatir ai) throws StopRequestException {
		this.ai=ai;
		this.time=new TimeMatrice(ai);
	}
	
	@Override
	public List<AiTile> processSuccessors(AstarNode node)
	{	// init
		// avant tout : test d'interruption
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tile = node.getTile();
		AiHero hero = node.getHero();
		// pour chaque case voisine : on la rajoute si elle est traversable
		for(Direction direction: Direction.getPrimaryValues())			
		{	AiTile neighbor = tile.getNeighbor(direction);
			List<AiBlock> blocks = neighbor.getBlocks();
			boolean test=false;
			if (!blocks.isEmpty()) {
				test=true;
				if(blocks.get(0).isDestructible())
					test=false;
			}
			if(neighbor.isCrossableBy(hero) || test==false) {
				GeneralFuncs.printLog(neighbor+" Time:"+time.getTime(neighbor),VerboseLevel.MED);
				if(time.getTime(neighbor)> Limits.dangerLimit+node.getDepth()*Limits.tileDistance ||
						time.getTime(neighbor)==0)
					result.add(neighbor);
			}
		}
		//
		return result;
	}
}
