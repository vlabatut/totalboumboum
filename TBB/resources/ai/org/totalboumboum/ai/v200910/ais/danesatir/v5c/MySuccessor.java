package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v200910.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 5.c
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class MySuccessor extends SuccessorCalculator{
	private DaneSatir ai;
	private TimeMatrice time;
	
	public MySuccessor(DaneSatir ai,TimeMatrice time) throws StopRequestException {
		ai.checkInterruption();
		this.ai=ai;
		this.time=time;
	}
	
	public MySuccessor(DaneSatir ai) throws StopRequestException {
		this(ai,new TimeMatrice(ai));
		ai.checkInterruption();		
	}
	/**
	 * Expand current tile neighbors and check time with Depth
	 * @throws StopRequestException 
	 * @throws StopRequestException 
	 */
	@Override
	public List<AiTile> processSuccessors(AstarNode node) throws StopRequestException
	{	
		ai.checkInterruption();
		// init
		// avant tout : test d'interruption
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tile = node.getTile();
		AiHero hero = node.getHero();
		// pour chaque case voisine : on la rajoute si elle est traversable
		for(Direction direction: Direction.getPrimaryValues())			
		{	ai.checkInterruption();
			AiTile neighbor = tile.getNeighbor(direction);
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
