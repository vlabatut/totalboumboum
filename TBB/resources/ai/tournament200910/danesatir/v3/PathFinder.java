package tournament200910.danesatir.v3;

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;

public class PathFinder {
	
	private DaneSatir ai;
	private AiHero hero;
	private AiPath path;

	
	public PathFinder(DaneSatir ai) {
		this(ai,ai.getOwnHero());
	}
	
	public PathFinder(DaneSatir ai, AiHero hero) {
		this.ai=ai;
		this.hero=hero;
	}
	
	public void calculPath(AiTile target) throws StopRequestException, NoWayException {
		ArrayList<AiTile> targets = new ArrayList<AiTile>();
		targets.add(target);
		calculPath(targets);
	}
	
	public void calculPath(List<AiTile> targetList) throws StopRequestException, NoWayException {
		ai.checkInterruption();
		Astar astar;
		astar = new Astar(hero,
				new MyCost(),
				new BasicHeuristicCalculator(),
				new MySuccessor(this.ai)
				);
		AiPath temppath = astar.processShortestPath(hero.getTile(), targetList);
		if(temppath.isEmpty())
			throw new NoWayException();
		if(path==null || path.isEmpty() || temppath.isShorterThan(path))
			this.path=temppath;
	}
	
	public AiPath getPath() {
		return path;
	}
	public void clearPath() {
		this.path=null;
	}
	public AiTile getNextTile() throws StopRequestException {
		ai.checkInterruption();
		if (path==null) {
			GeneralFuncs.printLog(ai, "PathFinder.java: path-> null", VerboseLevel.LOW);
			return null;
		}
		if (path.isEmpty()) {
			GeneralFuncs.printLog(ai, "PathFinder.java: path is over", VerboseLevel.LOW);
			this.ai.setResult(new AiAction(AiActionName.NONE));
			return null;
		}
		AiTile nextTile = this.path.getTile(0);
		AiTile currentTile = this.hero.getTile();
		if (GeneralFuncs.tileCompare(nextTile, currentTile)) {
			this.path.removeTile(nextTile);
			if (path.isEmpty()) {
				GeneralFuncs.printLog(ai, "PathFinder.java: path is over", VerboseLevel.LOW);
				this.ai.setResult(new AiAction(AiActionName.NONE));
				return null;
			}
			nextTile = this.path.getTile(0);
		}
		GeneralFuncs.printLog(ai, "PathFinder.java: path-> "+this.path, VerboseLevel.LOW);
		GeneralFuncs.printLog(ai, "PathFinder.java: nextTile-> "+nextTile, VerboseLevel.LOW);
		return nextTile;
	}
}
