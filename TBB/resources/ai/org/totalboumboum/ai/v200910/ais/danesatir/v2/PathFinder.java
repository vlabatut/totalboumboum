package org.totalboumboum.ai.v200910.ais.danesatir.v2;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;

/**
 * 
 * @version 2
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
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
		// 
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
