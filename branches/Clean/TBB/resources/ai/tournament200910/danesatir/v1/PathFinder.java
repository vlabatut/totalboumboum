package tournament200910.danesatir.v1;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.BasicCostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;

public class PathFinder {
	
	private AiTile source;
	private AiTile target;
	private DaneSatir ai;
	private Astar astar;
	
	public PathFinder(DaneSatir ai, AiTile source, AiTile target) throws StopRequestException {
		ai.checkInterruption();
		this.ai=ai;
		this.source = source;
		this.target = target;
	}
	
	public AiPath getPath() throws StopRequestException {
		ai.checkInterruption();
		init();
		return astar.processShortestPath(this.source, this.target);
	}
	private void init() throws StopRequestException
	{	
		ai.checkInterruption();
		astar = new Astar(ai.getOwnHero(),new BasicCostCalculator(),new BasicHeuristicCalculator());
	}

}
