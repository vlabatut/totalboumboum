package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
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
public class DecisionMaker {

	/** */
	private DaneSatir ai;
	/** */
	private State state;
	/** */
	private TimeMatrice time;

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public DecisionMaker(DaneSatir ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai=ai;
		this.state=State.START;
	}
	/**
	 * Check conditions and set state.
	 * @throws StopRequestException
	 */
	public void makeDecision() throws StopRequestException {
		ai.checkInterruption();
		setTime(new TimeMatrice(ai));
		if(isDanger() && getState()!=State.GOTO && getState()!=State.GOTOBOMB) 
			setState(State.DANGER);
		if(getState()==State.START) {
			if(isEnemyExist()) {
				if(isWayExistToEnemy())
					setState(State.DISCOVER_ENEMY);
				else if(isHaveBomb()) 
					setState(State.EXPLODE_TO_ENEMY);
			}
			else if(isItemExist())
				setState(State.TAKEBONUS);
			else if (isHiddenItemExist() && isHaveBomb())
				setState(State.EXPLODE);
			else setState(State.NONE);
		}
	}

	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isEnemyExist() throws StopRequestException {
		ai.checkInterruption();
		if(this.ai.getPercepts().getHeroes().size()>1)
			return true;
		return false;
	}

	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isHiddenItemExist() throws StopRequestException {
		ai.checkInterruption();
		if(this.ai.getPercepts().getHiddenItemsCount()>0)
			return true;
		return false;
	}

	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isItemExist() throws StopRequestException {
		ai.checkInterruption();
		if(this.ai.getPercepts().getItems().size()>0)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isWayExistToEnemy() throws StopRequestException {
		ai.checkInterruption();
		PathFinder temp = new PathFinder(ai, this.time);
		try {
			temp.calculPath(ai.getEnemies());
			return true;
		} catch (NoWayException e) {
			return false;
		}
	}
	
	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isDanger() throws StopRequestException {
		ai.checkInterruption();
		double dur = this.time.getTime();
		if(dur>0)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isHaveBomb() throws StopRequestException {
		ai.checkInterruption();
		AiHero hero = this.ai.getOwnHero();
		int count=hero.getBombNumber()-hero.getBombCount();
		if (count>0)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public State getState() throws StopRequestException {
		ai.checkInterruption();
		return state;
	}

	/**
	 * 
	 * @param state
	 * @throws StopRequestException
	 */
	public void setState(State state) throws StopRequestException {
		ai.checkInterruption();
		this.state = state;
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public TimeMatrice getTime() throws StopRequestException {
		ai.checkInterruption();
		setTime(new TimeMatrice(ai));
		return time;
	}

	/**
	 * 
	 * @param time
	 * @throws StopRequestException
	 */
	private void setTime(TimeMatrice time) throws StopRequestException {
		ai.checkInterruption();
		this.time = time;
	}
	
	/**
	 * 
	 * @param bomb
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean canWeEscape(AiBomb bomb) throws StopRequestException {
		ai.checkInterruption();
		return canWeEscape(bomb.getTile());
	}
	
	/**
	 * check we can escape, if we put bomb
	 * @param bomb
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean canWeEscape(AiTile bomb) throws StopRequestException {
		ai.checkInterruption();
		TimeMatrice temp = new TimeMatrice(this.ai);
		// Virtually Expand Bomb
		temp.virExpandBomb(bomb);
		
		AiTile safe = temp.mostSafeCase(bomb,true);
		if(safe==null)
			return false;
		Astar astar;
		astar = new Astar(ai,ai.getOwnHero(),
				new MyCost(this.time,ai),
				new BasicHeuristicCalculator(),
				new MySuccessor(this.ai,this.time)
				);
		AiPath temppath = astar.processShortestPath(bomb, safe);
		if(temppath ==null || temppath.isEmpty())
			return false;
		return true;
	}
}
