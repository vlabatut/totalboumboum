package tournament200910.danesatir.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.adapter200910.communication.StopRequestException;
import org.totalboumboum.ai.adapter200910.data.AiHero;
import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.path.astar.AstarNode;
import org.totalboumboum.ai.adapter200910.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;


public class MySuccessor extends SuccessorCalculator{
	@SuppressWarnings("unused")
	private DaneSatir ai;
	private TimeMatrice time;
	
	public MySuccessor(DaneSatir ai,TimeMatrice time) throws StopRequestException {
		this.ai=ai;
		this.time=time;
	}
	
	public MySuccessor(DaneSatir ai) throws StopRequestException {
		this(ai,new TimeMatrice(ai));
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
				if(time.getTime(neighbor)> (node.getDepth()+1)*Limits.tileDistance ||
						time.getTime(neighbor)==0)
					result.add(neighbor);
			}
		}
		//
		return result;
	}
}
