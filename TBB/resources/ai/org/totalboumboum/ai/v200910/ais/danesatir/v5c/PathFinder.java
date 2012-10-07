package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

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
 * @version 5.c
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class PathFinder {
	
	/** */
	private DaneSatir ai;
	/** */
	private AiHero hero;
	/** */
	private AiPath path;
	/** */
	private int curIndex;
	/** */
	private List<AiTile> targetList;
	/** */
	private TimeMatrice time;

	/**
	 * Default Constructor for Pathfinder 
	 * @param ai
	 * @param time
	 * @throws StopRequestException 
	 */
	public PathFinder(DaneSatir ai, TimeMatrice time) throws StopRequestException {
		this(ai,ai.getOwnHero(),time);
		ai.checkInterruption();
	}
	
	/**
	 * 
	 * @param ai
	 * @param hero
	 * @param time
	 * @throws StopRequestException
	 */
	public PathFinder(DaneSatir ai, AiHero hero, TimeMatrice time) throws StopRequestException {
		ai.checkInterruption();
		this.ai=ai;
		this.hero=hero;
		this.curIndex=0;
		this.path=null;
		this.targetList=null;
		this.updateTimeMatrice(time);
	}
	
	/**
	 * 
	 * @param target
	 * @throws StopRequestException
	 * @throws NoWayException
	 */
	public void calculPath(AiTile target) throws StopRequestException, NoWayException {
		ai.checkInterruption();
		List<AiTile> targets = new ArrayList<AiTile>();
		targets.add(target);
		calculPath(targets);
	}
	/**
	 * Build Path for Targets
	 * @param targetList
	 * @throws StopRequestException
	 * @throws NoWayException
	 */
	public void calculPath(List<AiTile> targetList) throws StopRequestException, NoWayException {
		ai.checkInterruption();
		Astar astar;
		astar = new Astar(ai,hero,
				new MyCost(this.time,ai),
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
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiPath getPath() throws StopRequestException {
		ai.checkInterruption();
		return path;
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 */
	public void clearPath() throws StopRequestException {
		ai.checkInterruption();
		this.path=null;
	}
	
	/**
	 * Get NextTile and check on arrive and other situations
	 * @return
	 * 		?
	 * @throws StopRequestException
	 * @throws PathOverException
	 * @throws NoWayException
	 */
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
	
	/**
	 * Recalculate Path with TargetList
	 * @throws StopRequestException
	 */
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
	
	/**
	 * 
	 * @param targetTile
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean isTarget(AiTile targetTile) throws StopRequestException {
		ai.checkInterruption();
		if (this.targetList == null)
			return false;
		return this.targetList.contains(targetTile);
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean isArrive() throws StopRequestException {
		ai.checkInterruption();
		return this.isTarget(this.hero.getTile());
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public List<AiTile> getTargets() throws StopRequestException {
		ai.checkInterruption();
		return this.targetList;
	}

	/**
	 * 
	 * @param time
	 * @throws StopRequestException
	 */
	public void updateTimeMatrice(TimeMatrice time) throws StopRequestException {
		ai.checkInterruption();
		this.time = time;
	}
}
