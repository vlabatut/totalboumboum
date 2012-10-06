package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Onur Büyüktopaç
 * @author Yiğit Turak
 */
@SuppressWarnings("deprecation")
public class SucceurCost extends SuccessorCalculator{
	private List<AiTile> freeList;
	private BuyuktopacTurak bt;
	
	/**
	 * 
	 * @param bt
	 * @param freeList
	 * @throws StopRequestException
	 */
	public SucceurCost(BuyuktopacTurak bt, List<AiTile> freeList) throws StopRequestException{
		bt.checkInterruption();
		this.bt = bt;
		this.freeList = new ArrayList<AiTile>();
		this.freeList = freeList;
	}

	@Override
	public List<AiTile> processSuccessors(AstarNode node) throws StopRequestException {
		bt.checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tile = node.getTile();
		AiHero hero = node.getHero();
		
		// pour chaque case voisine : on la rajoute si elle est traversable
		for(Direction direction: Direction.getPrimaryValues())			
		{	
			bt.checkInterruption();
			AiTile neighbor = tile.getNeighbor(direction);
			if(neighbor.isCrossableBy(hero) && freeList.contains(neighbor))
				result.add(neighbor);
		}

		return result;
	}

}
