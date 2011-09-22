package org.totalboumboum.ai.v200910.ais.danesatir.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;

/**
 * 
 * @version 4
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class PathFinder {
	
	private DaneSatir ai;
	private AiHero hero;
	private AiPath path;
	private int curIndex;
	private List<AiTile> targetList;
	private TimeMatrice time;

	
	/*public PathFinder(DaneSatir ai) {
		this(ai);
	}*/
	
	public PathFinder(DaneSatir ai, TimeMatrice time) {
		this(ai,ai.getOwnHero(),time);
	}
	
	public PathFinder(DaneSatir ai, AiHero hero, TimeMatrice time) {
		this.ai=ai;
		this.hero=hero;
		this.curIndex=0;
		this.path=null;
		this.targetList=null;
		this.updateTimeMatrice(time);
	}
	
	public void calculPath(AiTile target) throws StopRequestException, NoWayException {
		ai.checkInterruption();
		List<AiTile> targets = new ArrayList<AiTile>();
		targets.add(target);
		calculPath(targets);
	}
	
	public void calculPath(List<AiTile> targetList) throws StopRequestException, NoWayException {
		ai.checkInterruption();
		Astar astar;
		astar = new Astar(ai,hero,
				new MyCost(this.time),
				new BasicHeuristicCalculator(),
				new MySuccessor(this.ai,this.time)
				);
		AiPath temppath = astar.processShortestPath(hero.getTile(), targetList);
		
		if(temppath.isEmpty())
			throw new NoWayException();
		
		if(path==null || temppath.getLength() < path.getLength()-curIndex) {
			this.curIndex=0;
			this.path=temppath;
			this.targetList=targetList;
		}
	}
	
	public AiPath getPath() {
		return path;
	}
	public void clearPath() {
		this.path=null;
	}
	
	public AiTile getNextTile() throws StopRequestException, PathOverException, NoWayException {
		ai.checkInterruption();
		if (path==null) {
			//GeneralFuncs.printLog(ai, "PathFinder.java: path-> null", VerboseLevel.LOW);
			if (this.targetList==null) {
				System.out.println("FIX");
			}
			recalculPath();
			if(path==null)
				throw new NoWayException();
		}
		AiTile currentTile = this.hero.getTile();
		AiTile nextTile = this.path.getTile(curIndex);
		
		if(targetList.contains(currentTile)) {
			//GeneralFuncs.printLog(ai, "PathFinder.java: path is over", VerboseLevel.LOW);
			throw new PathOverException();
		}
		if( !(this.path.getTiles().contains(currentTile)) ) {
			recalculPath();
		}
		else {
			this.curIndex = this.path.getTiles().indexOf(currentTile);
			this.curIndex++;
			if (this.curIndex > this.path.getLength()) {
				System.out.println("FIX");
				recalculPath();
			}
			nextTile = this.path.getTile(curIndex);
		}
		
		//GeneralFuncs.printLog(ai, "PathFinder.java: path-> "+this.path, VerboseLevel.LOW);
		//GeneralFuncs.printLog(ai, "PathFinder.java: nextTile-> "+nextTile, VerboseLevel.LOW);
		
		return nextTile;
	}
	
	private void recalculPath() throws StopRequestException {
		ai.checkInterruption();
		this.curIndex=0;
		this.clearPath();
		
		try {
			this.calculPath(this.targetList);
		} catch (NoWayException e) {
			this.clearPath();
		}
	}
	
	public boolean isTarget(AiTile targetTile) {
		if (this.targetList == null)
			return false;
		return this.targetList.contains(targetTile);
	}
	
	public boolean isArrive() {
		return this.isTarget(this.hero.getTile());
	}
	
	public List<AiTile> getTargets() {
		return this.targetList;
	}

	public void updateTimeMatrice(TimeMatrice time) {
		this.time = time;
	}
}
