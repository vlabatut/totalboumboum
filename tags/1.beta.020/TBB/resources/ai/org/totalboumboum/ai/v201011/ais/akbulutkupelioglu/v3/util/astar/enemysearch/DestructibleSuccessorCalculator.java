package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util.astar.enemysearch;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;
import org.totalboumboum.engine.content.feature.Direction;

public class DestructibleSuccessorCalculator extends SuccessorCalculator
{

	AkbulutKupelioglu monIa;
	
	public DestructibleSuccessorCalculator(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		// 
	}
	
	public List<AiTile> processSuccessors(AstarNode node) throws StopRequestException
	{
		monIa.checkInterruption();
		// init
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tile = node.getTile();
		AiHero hero = node.getHero();
		// pour chaque case voisine : on la rajoute si elle est traversable
		for(Direction direction: Direction.getPrimaryValues())
		{
			monIa.checkInterruption();
			AiTile neighbor = tile.getNeighbor(direction);
			if(neighbor.isCrossableBy(hero))
				result.add(neighbor);
			else
				if(!neighbor.getBlocks().isEmpty())
					if(neighbor.getBlocks().get(0).isDestructible())
						result.add(neighbor);
		}
		//
		return result;
	}

}
