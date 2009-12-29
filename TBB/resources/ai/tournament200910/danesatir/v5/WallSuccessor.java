package tournament200910.danesatir.v5;

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiBlock;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiStateName;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.astar.AstarNode;
import fr.free.totalboumboum.ai.adapter200910.path.astar.successor.BasicSuccessorCalculator;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class WallSuccessor extends BasicSuccessorCalculator {
	@SuppressWarnings("unused")
	private DaneSatir ai;
	private TimeMatrice time;
	
	public WallSuccessor(DaneSatir ai) throws StopRequestException {
		this.ai=ai;
		this.time=new TimeMatrice(ai);
	}
	/**
	 * Ignore Destructible walls
	 */
	@Override
	public List<AiTile> processSuccessors(AstarNode node)
	{	// init
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
				if(blocks.get(0).isDestructible() && blocks.get(0).getState().getName()!=AiStateName.BURNING)
					test=false;
			}
			if(neighbor.isCrossableBy(hero) || test==false) {
				GeneralFuncs.printLog(neighbor+" Time:"+time.getTime(neighbor),VerboseLevel.MED);
				if(time.getTime(neighbor) > (node.getDepth()+1)*Limits.tileDistance ||
						time.getTime(neighbor) == 0)
					result.add(neighbor);
			}
		}
		//
		return result;
	}
}
