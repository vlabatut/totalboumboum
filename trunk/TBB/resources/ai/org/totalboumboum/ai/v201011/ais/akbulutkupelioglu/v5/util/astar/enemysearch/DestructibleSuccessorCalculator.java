package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.util.astar.enemysearch;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.AkbulutKupelioglu;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * A successor calculator used to find a path passing through the least obstacles.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 *
 */
public class DestructibleSuccessorCalculator extends SuccessorCalculator
{

	AkbulutKupelioglu monIa;
	
	/**
	 * Creates a new instance.
	 * @param ia The AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
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
