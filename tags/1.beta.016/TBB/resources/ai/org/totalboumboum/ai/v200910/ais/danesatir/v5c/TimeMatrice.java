package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiStateName;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;


public class TimeMatrice {

	private double[][] matrice;
	private AiZone zone;
	private List<AiBomb> bombs;
	private DaneSatir ai;
	
	public TimeMatrice(DaneSatir ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai=ai;
		this.zone=ai.getPercepts();
		//this.zone.update(this.zone.getLimitTime());
		matrice = new double[this.zone.getHeigh()][this.zone.getWidth()];
		this.bombs = this.zone.getBombs();
		sortBombes();
		createMatrice(this.bombs);
		return;
	}
	
	public void sortBombes() throws StopRequestException {
		ai.checkInterruption();
		Collections.sort(this.bombs,new BombComparator(ai));
//		GeneralFuncs.printBombs(bombs);
	}
	
	public void createMatrice(List<AiBomb> bombes) throws StopRequestException {
		ai.checkInterruption();
		for(AiBomb i : bombes ) {
			ai.checkInterruption();
			expandBomb(i);
		}
	}
	
	public void virExpandBomb(AiTile tile) throws StopRequestException{
		ai.checkInterruption();
		double duration = Limits.bombDuration;
		
		/*FIX: check destructible*/
		int range = this.ai.getOwnHero().getBombRange()-1;
		ArrayList<AiTile> blast = new ArrayList<AiTile>();
		ArrayList<Direction> dirs = new ArrayList<Direction>();
		dirs.add(Direction.UP);
		dirs.add(Direction.DOWN);
		dirs.add(Direction.LEFT);
		dirs.add(Direction.RIGHT);
		
		for(Direction dir : dirs) {
			ai.checkInterruption();
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
	
	private void expandBomb(AiBomb bomb) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		double duration = getTimeToExplode(bomb,ai);
		
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

	public double getTime(AiTile a) throws StopRequestException {
		ai.checkInterruption();
		return this.matrice[a.getLine()][a.getCol()];
	}
	
	public double getTime() throws StopRequestException {
		ai.checkInterruption();
		AiHero hero = zone.getOwnHero();
		AiTile tile = hero.getTile();
		return getTime(tile);
	}
	
	public void setTime(AiTile a, double duration) throws StopRequestException {
		ai.checkInterruption();
		this.matrice[a.getLine()][a.getCol()] = duration;
	}
	
	public AiTile mostSafeCase(AiTile a, boolean justZero) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		MonTile mostSafe = new MonTile(a,0,ai);
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
						stack.push(new MonTile(i,temp.getIter()+1,ai));
						if(isSaferThan(i,mostSafe.getTile()) && justZero == false)
							mostSafe = new MonTile(i,temp.getIter()+1,ai);
					}
				}
			}
		}
		if(TimeMatrice.isSafe(this, mostSafe.getTile(),ai) && justZero == false)
			return mostSafe.getTile();
		return null;
	}
	
	public AiTile mostSafeCase() throws StopRequestException {
		ai.checkInterruption();
		AiHero hero = zone.getOwnHero();
		AiTile tile = hero.getTile();
		return mostSafeCase(tile);
	}
	public AiTile mostSafeCase(AiTile a) throws StopRequestException {
		ai.checkInterruption();
		return mostSafeCase(a,false);
	}
	
	public boolean isSaferThan(AiTile a1, AiTile a2) throws StopRequestException {
		ai.checkInterruption();
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
	
	public static double getTimeToExplode(AiBomb bomb,ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		// make more accurate
		if( bomb.getNormalDuration() - bomb.getTime() < 0)
			return Limits.expandBombTime;
		// Update bombDuration
		if( bomb.getNormalDuration() < Limits.bombDuration || Limits.bombDuration==0)
			Limits.bombDuration=bomb.getNormalDuration();
		return bomb.getNormalDuration() - bomb.getTime();
	}
	
	public static boolean isSafe(TimeMatrice time,AiTile a,ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
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
