package org.totalboumboum.ai.v200910.ais.danesatir.v2;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/**
 * 
 * @version 2
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
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
		if(GeneralFuncs.checkVerboseLevel(4))
			GeneralFuncs.printBombs(bombs);
	}
	
	public void createMatrice(List<AiBomb> bombes) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		for(AiBomb i : bombes ) {
			expandBomb(i);
		}
		if(GeneralFuncs.checkVerboseLevel(3))
			GeneralFuncs.printMatrice(this.zone.getWidth(), this.zone.getHeight(), this.matrice);
	}

	private void expandBomb(AiBomb bomb) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		double duration = GeneralFuncs.getTimeToExplode(bomb);
		
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
	
	private void setTime(AiTile a, double duration) {
		this.matrice[a.getLine()][a.getCol()] = duration;
	}
	
	public AiTile mostSafeCase(AiTile a) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		AiTile mostSafe = a;
		Stack<AiTile> stack = new Stack<AiTile>();
		Stack<AiTile> processed = new Stack<AiTile>();
		stack.push(a);
		while(!stack.isEmpty()) {
			ai.checkInterruption();
			AiTile temp = stack.pop();
			processed.push(temp);
			for(AiTile i : temp.getNeighbors()) {
				ai.checkInterruption();
				if(i.isCrossableBy(this.ai.getOwnHero()) && !(processed.contains(i))) {
					if(getTime(i) == 0)
						return i;
					else if(isSaferThen(i,mostSafe))
						mostSafe = i;
					stack.push(i);
				}
			}
		}
		if(getTime(mostSafe)<Limits.dangerLimit)
			return null;
		return mostSafe;
	}
	
	public boolean isSaferThen(AiTile a1, AiTile a2) {
		if(getTime(a1)<getTime(a2))
			return true;
		return false;
	}
}
