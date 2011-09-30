package tournament200910.danesatir.v3;

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.astar.AstarNode;
import fr.free.totalboumboum.ai.adapter200910.path.astar.successor.SuccessorCalculator;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class MySuccessor extends SuccessorCalculator{
	@SuppressWarnings("unused")
	private DaneSatir ai;
	private TimeMatrice time;
	
	public MySuccessor(DaneSatir ai) throws StopRequestException {
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
			if(neighbor.isCrossableBy(hero)) {
				//GeneralFuncs.printLog(neighbor+" Time:"+time.getTime(neighbor),VerboseLevel.MED);
				if(time.getTime(neighbor)> Limits.dangerLimit+node.getDepth()*Limits.tileDistance ||
						time.getTime(neighbor)==0)
					result.add(neighbor);
			}
		}
		//
		return result;
	}
}