package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.astar;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.Matrix;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class AdvancedSuccessorCalculator extends SuccessorCalculator
{
	private Matrix interest = null;
	private AkbulutKupelioglu monIA = null;
	private final int TILE_TRAVERSAL_TIME = -1200;
	private final int TILE_TRAVERSAL_TIME_MIN = -700; //calculate this
	public AdvancedSuccessorCalculator(Matrix interest, AkbulutKupelioglu monIA) throws StopRequestException
	{
		monIA.checkInterruption();
		this.interest = interest;
		this.monIA = monIA;
	}

	@Override
	public List<AiTile> processSuccessors(AstarNode node) throws StopRequestException
	{
		monIA.checkInterruption();
		List<AiTile> neighbors = node.getTile().getNeighbors();
		List<AiTile> result = new ArrayList<AiTile>();
		for(AiTile neighbor : neighbors)
		{
			monIA.checkInterruption();
			if(neighbor.getBlocks().isEmpty()&&neighbor.getBombs().isEmpty())
				if(interest.getElement(neighbor)<TILE_TRAVERSAL_TIME||interest.getElement(neighbor)>=TILE_TRAVERSAL_TIME_MIN)
					result.add(neighbor);
		}
		return result;
	}

}
