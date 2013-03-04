package org.totalboumboum.ai.v200910.ais.danesatir.v5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiStateName;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 5
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class TimeMatrice {

	/** */
	private double[][] matrice;
	/** */
	private AiZone zone;
	/** */
	private List<AiBomb> bombs;
	/** */
	private DaneSatir ai;
	
	/**
	 * 
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public TimeMatrice(DaneSatir ai) throws StopRequestException {
		this.ai=ai;
		this.zone=ai.getPercepts();
		//this.zone.update(this.zone.getLimitTime());
		matrice = new double[this.zone.getHeight()][this.zone.getWidth()];
		this.bombs = this.zone.getBombs();
		sortBombes();
		createMatrice(this.bombs);
		return;
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void sortBombes() throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		Collections.sort(this.bombs,new BombComparator());
//		GeneralFuncs.printBombs(bombs);
	}
	
	/**
	 * 
	 * @param bombes
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void createMatrice(List<AiBomb> bombes) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		for(AiBomb i : bombes ) {
			ai.checkInterruption();
			expandBomb(i);
		}
	}
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void virExpandBomb(AiTile tile) throws StopRequestException{
		// avant tout : test d'interruption
		ai.checkInterruption();
		double duration = Limits.bombDuration;
		
		/*FIX: check destructible*/
		int range = this.ai.getOwnHero().getBombRange()-1;
		List<AiTile> blast = new ArrayList<AiTile>();
		List<Direction> dirs = new ArrayList<Direction>();
		dirs.add(Direction.UP);
		dirs.add(Direction.DOWN);
		dirs.add(Direction.LEFT);
		dirs.add(Direction.RIGHT);
		
		for(Direction dir : dirs) {
			AiTile temp = tile;
			for (int i=0;i<range;i++) {
				ai.checkInterruption();
				temp = temp.getNeighbor(dir);
				if(!temp.getBlocks().isEmpty())
					break;
				blast.add(temp);
			}
		}
		
		blast.add(tile);
		
		// seek intersections
		for(AiTile i : blast) {
			ai.checkInterruption();
			if(getTime(i)!=0 && getTime(i)<duration)
				duration=getTime(i);
		}

		// place duration
		for(AiTile i : blast) {
			ai.checkInterruption();
			setTime(i,duration);
		}
	}
	
	/**
	 * 
	 * @param bomb
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void expandBomb(AiBomb bomb) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		double duration = getTimeToExplode(bomb);
		
		List<AiTile> blast = bomb.getBlast();
		blast.add(bomb.getTile());
		
		// seek intersections
		for(AiTile i : blast) {
			ai.checkInterruption();
			if(getTime(i)!=0 && getTime(i)<duration)
				duration=getTime(i);
		}

		// place duration
		for(AiTile i : blast) {
			ai.checkInterruption();
			setTime(i,duration);
		}
	}

	/**
	 * 
	 * @param a
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public double getTime(AiTile a) {
		return this.matrice[a.getLine()][a.getCol()];
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public double getTime() {
		AiHero hero = zone.getOwnHero();
		AiTile tile = hero.getTile();
		return getTime(tile);
	}
	
	/**
	 * 
	 * @param a
	 * 		Description manquante !
	 * @param duration
	 * 		Description manquante !
	 */
	public void setTime(AiTile a, double duration) {
		this.matrice[a.getLine()][a.getCol()] = duration;
	}
	
	/**
	 * 
	 * @param a
	 * 		Description manquante !
	 * @param justZero
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile mostSafeCase(AiTile a, boolean justZero) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		MonTile mostSafe = new MonTile(a,0);
		Stack<MonTile> stack = new Stack<MonTile>();
		Stack<AiTile> processed = new Stack<AiTile>();
		stack.push(mostSafe);
		while(!stack.isEmpty()) {
			ai.checkInterruption();
			MonTile temp = stack.pop();
			processed.push(temp.getTile());
			for(AiTile i : temp.getTile().getNeighbors()) {
				ai.checkInterruption();
				if(i.isCrossableBy(this.ai.getOwnHero()) && !(processed.contains(i))) {
					//GeneralFuncs.printLog(ai, getTime(mostSafe.getTile())+" <?> "+getTime(i), VerboseLevel.HIGH);
					if(getTime(i) == 0)
						return i;
					if( getTime(i) > (temp.getIter()+1)*Limits.tileDistance ) { 
						stack.push(new MonTile(i,temp.getIter()+1));
						if(isSaferThan(i,mostSafe.getTile()) && justZero == false)
							mostSafe = new MonTile(i,temp.getIter()+1);
					}
				}
			}
		}
		if(TimeMatrice.isSafe(this, mostSafe.getTile()) && justZero == false)
			return mostSafe.getTile();
		return null;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile mostSafeCase() throws StopRequestException {
		AiHero hero = zone.getOwnHero();
		AiTile tile = hero.getTile();
		return mostSafeCase(tile);
	}
	
	/**
	 * 
	 * @param a
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile mostSafeCase(AiTile a) throws StopRequestException {
		return mostSafeCase(a,false);
	}
	
	/**
	 * 
	 * @param a1
	 * 		Description manquante !
	 * @param a2
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public boolean isSaferThan(AiTile a1, AiTile a2) {
		double dur1=getTime(a1);
		double dur2=getTime(a2);
		if(dur1==0)
			return true;
		if(dur2==0)
			return false;
		if(dur1>dur2)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param bomb
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static double getTimeToExplode(AiBomb bomb) {
		//
		if( bomb.getNormalDuration() - bomb.getTime() < 0)
			return Limits.expandBombTime;
		// Update bombDuration
		if( bomb.getNormalDuration() < Limits.bombDuration || Limits.bombDuration==0)
			Limits.bombDuration=bomb.getNormalDuration();
		return bomb.getNormalDuration() - bomb.getTime();
	}
	
	/**
	 * 
	 * @param time
	 * 		Description manquante !
	 * @param a
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean isSafe(TimeMatrice time,AiTile a) {
		double dur = time.getTime(a);
		List<AiBlock> blocks = a.getBlocks();
		if(!blocks.isEmpty())
			if(blocks.get(0).getState().getName()==AiStateName.BURNING)
				return false;
		if (!a.getFires().isEmpty())
			return false;
		if (dur == 0 || dur > Limits.dangerLimit)
			return true;
		return false;
	}
}
