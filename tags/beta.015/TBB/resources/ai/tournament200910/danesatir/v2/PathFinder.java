package tournament200910.danesatir.v2;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;

public class PathFinder {
	
	private AiTile source;
	private AiTile target;
	private DaneSatir ai;
	private Astar astar;
	private AiPath path;
	private long duration;
	
	public PathFinder(DaneSatir ai, AiTile source, AiTile target) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		this.ai=ai;
		this.source = source;
		this.target = target;
		this.path = null;
		this.astar = new Astar(ai,ai.getOwnHero(),
				new SafeCostCalculator(this.ai,this.ai.getTimeMatrice()),
				new BasicHeuristicCalculator());
		this.duration = -1;
	}
	private void calculPath() throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		this.path = astar.processShortestPath(this.source, this.target);
		if (this.path == null)
			this.duration = 0;
		else
			this.duration = this.path.getDuration(this.ai.getOwnHero());
	}
	public AiPath getPath() throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		if (this.duration == -1)
			calculPath();
		return this.path;
	}
	
	public AiTile getNextTile() throws StopRequestException {
		// TODO Auto-generated method stub
		ai.checkInterruption();
		AiTile nextTile = null;
		if (this.duration == -1)
			calculPath();
		if (this.path != null) {
			if(GeneralFuncs.tileCompare(path.getTile(0),this.source))
				path.removeTile(this.source);
			nextTile = path.getTile(0);
		}
		else {
			nextTile=this.source;
		}
		return nextTile;
	}
}
