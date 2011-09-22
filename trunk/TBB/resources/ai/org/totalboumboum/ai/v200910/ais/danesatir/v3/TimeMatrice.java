package org.totalboumboum.ai.v200910.ais.danesatir.v3;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/**
 * 
 * @version 3
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
public class TimeMatrice {

	private double[][] matrice;
	private AiZone zone;
	private List<AiBomb> bombs;
	private DaneSatir ai;
	
	public TimeMatrice(DaneSatir ai) throws StopRequestException {
		this.ai=ai;
		this.zone=ai.getPercepts();
		matrice = new double[this.zone.getHeight()][this.zone.getWidth()];
		this.bombs = this.zone.getBombs();
		sortBombes();
		createMatrice(this.bombs);
		return;
	}
	
	public void sortBombes() throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		Collections.sort(this.bombs,new BombComparator());
//		GeneralFuncs.printBombs(bombs);
	}
	
	public void createMatrice(List<AiBomb> bombes) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		for(AiBomb i : bombes ) {
			ai.checkInterruption();
			expandBomb(i);
		}
		//if(checkVerbose(Verbose.HIGH))
	//	GeneralFuncs.printMatrice(this.zone.getWidth(), this.zone.getHeigh(), this.matrice);
	}

	private void expandBomb(AiBomb bomb) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		double duration = getTimeToExplode(bomb);
		
		List<AiTile> blast = bomb.getBlast();
		blast.add(bomb.getTile());
		
		// seek intersections
		for(AiTile i : blast)
			if(getTime(i)!=0 && getTime(i)<duration)
				duration=getTime(i);

		// place duration
		for(AiTile i : blast)
			setTime(i,duration);
	}

	public double getTime(AiTile a) {
		return this.matrice[a.getLine()][a.getCol()];
	}
	
	public double getTime() {
		AiHero hero = zone.getOwnHero();
		AiTile tile = hero.getTile();
		return getTime(tile);
	}
	
	public void setTime(AiTile a, double duration) {
		this.matrice[a.getLine()][a.getCol()] = duration;
	}
	
	public AiTile mostSafeCase(AiTile a) throws StopRequestException {
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
					//GeneralFuncs.printLog(ai, getTime(mostSafe.getTile())+" <?> "+getTime(i), VerboseLevel.LOW);
					if(getTime(i) == 0)
						return i;
					else if(isSaferThan(i,mostSafe.getTile()))
						mostSafe = new MonTile(i,temp.getIter()+1);
					if( getTime(i) > Limits.dangerLimit + (temp.getIter()+1)*Limits.tileDistance )
						stack.push(new MonTile(i,temp.getIter()+1));
				}
			}
		}
		if(getTime(mostSafe.getTile())<Limits.dangerLimit)
			return null;
		return mostSafe.getTile();
	}
	
	public AiTile mostSafeCase() throws StopRequestException {
		AiHero hero = zone.getOwnHero();
		AiTile tile = hero.getTile();
		return mostSafeCase(tile);
	}
	
	public boolean isSaferThan(AiTile a1, AiTile a2) {
		if(getTime(a1)>getTime(a2))
			return true;
		return false;
	}
	
	public static double getTimeToExplode(AiBomb bomb) {
		// make more accurate
		if( bomb.getNormalDuration() - bomb.getTime() < 0)
			return Limits.expandBombTime;
		if( bomb.getNormalDuration() < Limits.bombDuration || Limits.bombDuration==0)
			Limits.bombDuration=bomb.getNormalDuration();
		return bomb.getNormalDuration() - bomb.getTime();
	}
}
