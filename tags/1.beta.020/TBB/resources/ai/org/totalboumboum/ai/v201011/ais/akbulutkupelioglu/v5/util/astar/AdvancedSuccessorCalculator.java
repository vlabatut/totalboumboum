package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.util.astar;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.util.Matrix;

/**
 * A successor calculator taking into account the bombs, flames and our distance to them.
 * @author yasa
 *
 */
public class AdvancedSuccessorCalculator extends SuccessorCalculator
{
	private Matrix interest = null;
	private AkbulutKupelioglu monIa = null;
	private final int TILE_TRAVERSAL_TIME = -600;
	public AdvancedSuccessorCalculator(Matrix interest, AkbulutKupelioglu monIa) throws StopRequestException
	{
		monIa.checkInterruption();
		this.interest = interest;
		this.monIa = monIa;
	}

	@Override
	public List<AiTile> processSuccessors(AstarNode node) throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiTile> neighbors = node.getTile().getNeighbors();
		List<AiTile> result = new ArrayList<AiTile>();
		for(AiTile neighbor : neighbors)
		{
			monIa.checkInterruption();
			if(neighbor.getBlocks().isEmpty()&&neighbor.getBombs().isEmpty())
				if(interest.getElement(neighbor)<TILE_TRAVERSAL_TIME||interest.getElement(neighbor)>=0)
					result.add(neighbor);
		}
		return result;
	}

}
